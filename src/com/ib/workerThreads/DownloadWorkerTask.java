/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.workerThreads;

import javax.swing.SwingWorker;
import com.ib.manager.LogManager_server;
import org.apache.log4j.Logger;
import java.util.List;
import com.ib.analyzerGui.AnalyzerGUI;
import com.ib.reader.LogReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;

/**
 *
 * @author Siteng Jin
 */
public class DownloadWorkerTask extends SwingWorker<Boolean, Integer>{
    private static final Logger LOG = Logger.getLogger(DownloadWorkerTask.class);
    
    public static final Object DOWNLOADEXTRACTLOCK = new Object();
    public static final int UPPERDOWNLOADPROGRESS = 50;
    
    public static DownloadWorkerTask _instance = null;
    
    private LogManager_server m_serverManager = null;
    private boolean isDownloadFinished = false;
    private String zipLocation = null;
    
    private DownloadWorkerTask(){
        m_serverManager = LogManager_server.getInstance();
    }
    
    public static DownloadWorkerTask getInstance(){
        if(_instance == null){
            _instance = new DownloadWorkerTask();
        }
        return _instance;
    }
    
    @Override
    public Boolean doInBackground(){
        synchronized(DOWNLOADEXTRACTLOCK){
            if(downloadDiagnosticFile(zipLocation)){
                this.setIsDownloadFinished(true);
                DOWNLOADEXTRACTLOCK.notifyAll();
                return true;
            } else {
                this.setIsDownloadFinished(false);
                DOWNLOADEXTRACTLOCK.notifyAll();
                return false;
            }
        }
    }
    
    public boolean downloadDiagnosticFile(String zipLocation){
        try{
            URL downloadLink = new URL(new String("https://206.106.137.34/tws/data/" + m_serverManager.getSelectedUserDiagnosticFile()));
            LOG.debug("Downloading diagnostic file from: " + downloadLink.toString());
            HttpURLConnection con = (HttpURLConnection)downloadLink.openConnection();
            
            con.setRequestMethod("GET");
            InputStream input = con.getInputStream();
            FileOutputStream fos = new FileOutputStream(zipLocation);
            
            //LOG.debug("Getting content length...");
            int fileSize = con.getContentLength();
            LOG.debug("Content length = " + fileSize);
            
            byte[] buf = new byte[1024];
            
            int downloadedFileSize = 0;
            int lastUpdatedProgress = 0;
            
            int n = input.read(buf);
            while(n >= 0){
                downloadedFileSize += n;
                
                int downloadedPct = (int) ((((double)downloadedFileSize) / ((double)fileSize)) * 100d);
                
                int currentProgress = (int) (downloadedPct * ((double) UPPERDOWNLOADPROGRESS/100d));
                
                fos.write(buf, 0, n);
                
                if(currentProgress > lastUpdatedProgress){
                    publish(currentProgress);
                    lastUpdatedProgress = currentProgress;
                    LOG.debug("Downloaded File Size = " + downloadedFileSize + ", Downloaded Percentage = " + downloadedPct + 
                        ", Current Progress = " + currentProgress);
                }
                
                n = input.read(buf);
            }
            m_serverManager.getReader().setZipLocation(LogReader.USESERVER, zipLocation);
            return true;
        } catch (UnknownServiceException e){
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            LOG.debug(e.getMessage(), e);
            return false;
        } catch (IOException e){
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            LOG.debug(e.getMessage(), e);
            return false;
        }
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

    public String getZipLocation() {
        return zipLocation;
    }

    public void setZipLocation(String zipLocation) {
        this.zipLocation = zipLocation;
    }

    public boolean getIsDownloadFinished() {
        return isDownloadFinished;
    }

    public void setIsDownloadFinished(boolean isDownloadFinished) {
        this.isDownloadFinished = isDownloadFinished;
    }
    
    
}
