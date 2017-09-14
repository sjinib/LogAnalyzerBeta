/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.ib.workerThreads;

import java.io.File;
import java.io.IOException;
import javax.swing.SwingWorker;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import com.ib.analyzerGui.AnalyzerGUI;
import com.ib.manager.*;
import com.ib.reader.LogReader;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Siteng Jin
 */
public class ExtractWorkerTaskLocal extends SwingWorker<Boolean, Integer>{
    private static final Logger LOG = Logger.getLogger(ExtractWorkerTaskLocal.class);
    
    private static ExtractWorkerTaskLocal _instance = null;
    
    private LogManager_local m_localManager = null;
    
    private static final int BUFFER_SIZE = 4096;
    
    private ExtractWorkerTaskLocal(){
        m_localManager = LogManager_local.getInstance();
    }
    
    public static ExtractWorkerTaskLocal getInstance(){
        if(_instance == null){
            _instance = new ExtractWorkerTaskLocal();
        }
        return _instance;
    }
    
    @Override
    public Boolean doInBackground(){
        extractionWork();
        return true;
    }
    
    private void extractionWork(){
        // Initialize combo box
        AnalyzerGUI.getInstance().initializedComboBoxLocal();
        
        m_localManager.resetAllFileList();
        
        m_localManager.extract();
        
        AnalyzerGUI.getInstance().setIsExtracted_local(true);;
        
        if(AnalyzerGUI.getInstance().twsRadio2.isSelected()){
            // Populate and select log file for combo box
            AnalyzerGUI.getInstance().handleLogComboBox_tws(LogReader.USELOCAL);
            
            // Populate and select settings file for combo box
            AnalyzerGUI.getInstance().handleSettingsComboBox_tws(LogReader.USELOCAL);
            
            // Populate and select trade file for combo box
            AnalyzerGUI.getInstance().handleTradeComboBox(LogReader.USELOCAL);
            
            // Populate and select screenshot for combo box
            AnalyzerGUI.getInstance().handleScreenshotComboBox(LogReader.USELOCAL);
        } else if(AnalyzerGUI.getInstance().ibgRadio2.isSelected()){
            // Populate and select log file for combo box
            AnalyzerGUI.getInstance().handleLogComboBox_ibg(LogReader.USELOCAL);
            
            // Populate and select settings file for combo box
            AnalyzerGUI.getInstance().handleSettingsComboBox_ibg(LogReader.USELOCAL);
            
            // Populate and select screenshot for combo box
            AnalyzerGUI.getInstance().handleScreenshotComboBox(LogReader.USELOCAL);
        }
        
        if(AnalyzerGUI.getInstance().autoClsCheck.isSelected()){
            AnalyzerGUI.getInstance().clearAllTextPanelList();
        }
    }
    
    public void unZipIt(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        else {
            FileUtils.cleanDirectory(destDir);
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = null;
        int zipFileSize = (int) new File(zipFilePath).length();
        //LOG.debug("Zip File Size = " + zipFileSize);
        int zippedFileSize = 0;
        // iterates over entries in the zip file
        while ((entry = zipIn.getNextEntry()) != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zippedFileSize += (int) entry.getCompressedSize();
            int zippedPct = (int) ((((double)zippedFileSize) / ((double)zipFileSize)) * 100d);
            //LOG.debug("Zipped Percentage = " + zippedPct + ", Current Progress = " + zippedPct);
            publish(zippedPct);
            zipIn.closeEntry();
        }
        zipIn.close();
        publish(100);
        
        
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    
    @Override
    public void done(){
        _instance = null;
    }
    
    @Override
    public void process(List<Integer> chuncks){
        for(Integer progress: chuncks){
            AnalyzerGUI.getInstance().extractProgressBarLocal.setValue(progress);
        }
    }
}
