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
public class ExtractWorkerTaskServer extends SwingWorker<Boolean, Integer>{
    private static final Logger LOG = Logger.getLogger(ExtractWorkerTaskServer.class);
    
    private static ExtractWorkerTaskServer _instance = null;
    
    private LogManager_server m_serverManager = null;
    
    private static final int BUFFER_SIZE = 4096;
    
    private ExtractWorkerTaskServer(){
        m_serverManager = LogManager_server.getInstance();
    }
    
    public static ExtractWorkerTaskServer getInstance(){
        if(_instance == null){
            _instance = new ExtractWorkerTaskServer();
        }
        return _instance;
    }
    
    @Override
    public Boolean doInBackground(){
        synchronized(DownloadWorkerTask.DOWNLOADEXTRACTLOCK){
            while(!DownloadWorkerTask.getInstance().getIsDownloadFinished()){
                try{
                    DownloadWorkerTask.DOWNLOADEXTRACTLOCK.wait();
                    LOG.info("ExtractWorker: Waiting for DownloadWorker to be finished.");
                } catch (Exception e){
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        LOG.info("ExtractWorker: DownloadWorker finished, start extraction.");
        
        String diagnosticFileName = m_serverManager.getSelectedUserDiagnosticFile();
        String diagnosticFileNameNoZip = diagnosticFileName.substring(0, diagnosticFileName.lastIndexOf(".zip"));
        File extractDirectory = new File(AnalyzerGUI.getInstance().extractDirectoryServer.getText() + "\\" + diagnosticFileNameNoZip);
        LOG.debug("Extract directory: " + extractDirectory + " (" + AnalyzerGUI.getInstance().extractDirectoryServer.getText() + " + " + diagnosticFileNameNoZip + ")");
        if(extractDirectory.isDirectory()){ // If the output directory selected already exist
            int reply = javax.swing.JOptionPane.showConfirmDialog(null, "The extract directory already exists. Do you want to clear the " + extractDirectory.getAbsolutePath() + " folder first?");
            if(reply == javax.swing.JOptionPane.YES_OPTION){
                try{
                    FileUtils.cleanDirectory(extractDirectory); // Clear extract directory
                } catch (IOException e){
                    javax.swing.JOptionPane.showMessageDialog(null, "Failed to clear directory");
                    LOG.error(e.getMessage(), e);
                }
                
                //extractDirectory1.setText(extractDirectory.toString());
                m_serverManager.setReaderLocation(null, extractDirectory.getAbsolutePath());
                
                extractionWork();
                
                // Have main window show extracted directory
                if(AnalyzerGUI.getInstance().useImportServerBtn.isSelected()){
                    AnalyzerGUI.getInstance().setTitle("Log Analyzer - " + AnalyzerGUI.getInstance().extractDirectoryServer.getText() + "\\" + diagnosticFileNameNoZip);
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Please choose a valid directory");
                //extractDirectory1.setText(System.getProperty("java.io.tmpdir"));
                AnalyzerGUI.getInstance().extractProgressBarServer.setValue(0); /*Progress bar setting*/
            }
        }
        else {
            extractDirectory.mkdir();
            
            //extractDirectory1.setText(extractDirectory.toString());
            m_serverManager.setReaderLocation(null, extractDirectory.getAbsolutePath());
            
            extractionWork();
            
            // Have main window show extracted directory
            if(AnalyzerGUI.getInstance().useImportServerBtn.isSelected()){
                AnalyzerGUI.getInstance().setTitle("Log Analyzer - " + AnalyzerGUI.getInstance().extractDirectoryServer.getText() + "\\" + diagnosticFileNameNoZip);
            }
        }
        
        DownloadWorkerTask.getInstance().setIsDownloadFinished(false);
        return true;
    }
    
    private void extractionWork(){
        // Initialize combo box
        AnalyzerGUI.getInstance().initializedComboBoxServer();
        
        m_serverManager.resetAllFileList();
        
        m_serverManager.extract();
        AnalyzerGUI.getInstance().setIsExtracted_server(true);;
        if(AnalyzerGUI.getInstance().twsRadio1.isSelected()){
            // Populate and select log file for combo box
            AnalyzerGUI.getInstance().handleLogComboBox_tws(LogReader.USESERVER);
            
            // Populate and select settings file for combo box
            AnalyzerGUI.getInstance().handleSettingsComboBox_tws(LogReader.USESERVER);
            
            // Populate and select trade file for combo box
            AnalyzerGUI.getInstance().handleTradeComboBox(LogReader.USESERVER);
            
            // Populate and select screenshot for combo box
            AnalyzerGUI.getInstance().handleScreenshotComboBox(LogReader.USESERVER);
        } else if(AnalyzerGUI.getInstance().ibgRadio1.isSelected()){
            // Populate and select log file for combo box
            AnalyzerGUI.getInstance().handleLogComboBox_ibg(LogReader.USESERVER);
            
            // Populate and select settings file for combo box
            AnalyzerGUI.getInstance().handleSettingsComboBox_ibg(LogReader.USESERVER);
            
            // Populate and select screenshot for combo box
            AnalyzerGUI.getInstance().handleScreenshotComboBox(LogReader.USESERVER);
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
            int currentProgress = (int) (zippedPct * ((double) (100 - DownloadWorkerTask.UPPERDOWNLOADPROGRESS)/100d) + DownloadWorkerTask.UPPERDOWNLOADPROGRESS);
            //LOG.debug("Zipped Percentage = " + zippedPct + ", Current Progress = " + currentProgress);
            publish(currentProgress);
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
            AnalyzerGUI.getInstance().extractProgressBarServer.setValue(progress);
        }
    }
}
