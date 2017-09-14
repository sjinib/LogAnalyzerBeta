/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.ib.workerThreads;

import com.ib.manager.LogManager_server;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.ib.analyzerGui.AnalyzerGUI;

/**
 *
 * @author Siteng Jin
 */
public class LoadUserWorkerTask extends SwingWorker<Boolean, Object> {
    private static final Logger LOG = Logger.getLogger(DownloadWorkerTask.class);
    
    private static LoadUserWorkerTask _instance = null;
    
    private String username = null;
    
    private LogManager_server m_serverManager = null;
    
    private LoadUserWorkerTask(){
        m_serverManager = LogManager_server.getInstance();
    }
    
    public static LoadUserWorkerTask getInstance(){
        if(_instance == null){
            _instance = new LoadUserWorkerTask();
        }
        return _instance;
    }
    
    public void setInstanceUsername(String username){
        _instance.username = username;
    }
    
    @Override
    public Boolean doInBackground(){
        m_serverManager.clearUserDiagnosticFileList();
        
        return m_serverManager.loadUserDiagnosticFileList(username, AnalyzerGUI.getInstance().todayOnlyCheckBox.isSelected());
    }
    
    @Override
    protected void done(){
        try{
            if(get()){
                AnalyzerGUI.getInstance().handleUserDiagnosticFileBox();
                AnalyzerGUI.getInstance().loadingLabel.setVisible(false);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "No diagnostic file has been found for the username: " + username);
                AnalyzerGUI.getInstance().loadingLabel.setVisible(false);
            }
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
        
        _instance = null;
    }
}
