/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.manager;

import com.ib.reader.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.UnknownServiceException;

public class LogManager_server extends LogManager{
    //private final LogReader reader;
    //private boolean isDeepDiagnostic = true; // True if use deep analysis
    //private boolean isTWS; // If the files being investigated are from TWS (true) or IBG (false)
    private ArrayList<String> userDiagnosticFileList = new ArrayList<String>();
    private String selectedUserDiagnosticFile = new String();
    
    public LogManager_server(){
        super();
        setProxy();
        setTrustManagerForSSL();
    }
    
    private void setProxy(){
        System.getProperties().setProperty("proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", "192.9.168.225");
        System.getProperties().setProperty("http.proxyPort", "3128");
        System.getProperties().setProperty("https.proxyHost", "192.9.168.225");
        System.getProperties().setProperty("https.proxyPort", "3128");
        
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("twserror", "twserr67".toCharArray());
            }
        });
    }
    
    private void setTrustManagerForSSL(){
        SimpleX509TrustManager.disableSSL();
    }
    
    private void addUserDiagnosticFileList(String name){
        if(name != null){
            userDiagnosticFileList.add(name);
        }
    }
    
    public void clearUserDiagnosticFileList(){
        userDiagnosticFileList.clear();
    }
    
    public void loadUserDiagnosticFileList(String username){
        
                try{
                    URL url = new URL("https://wit1.interactivebrokers.com/cgi-bin/tws/tws_error_reader.pl");
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        if(inputLine.contains(">" + username)){
                            String diagnosticFileName = inputLine.replaceAll("\\<.*?>","").replaceAll("\\s+", "");
                            if(diagnosticFileName != null){
                                userDiagnosticFileList.add(diagnosticFileName);
                            }
                        }
                    in.close();
                    
                } catch (UnknownServiceException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }

    }
    
    public String[] getUserDiagnosticFileList(){
        String[] list = new String[userDiagnosticFileList.size()];
        for(int i = 0; i < userDiagnosticFileList.size(); i++){
            list[i] = userDiagnosticFileList.get(i);
        }
        return list;
    }
    
    public void selectUserDiagnosticFile(String s){
        selectedUserDiagnosticFile = s;
    }
    
    // Invoke to extract_local file
    public void extract_local(){
        try {
            super.getReader().extractZip(LogReader.USESERVER);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void setIsTws(boolean isTws){
        super.setIsTws(isTws);
    }
    
    public void setDeepDiagnostic(boolean isDeepDiag){
        super.setDeepDiagnostic(isDeepDiag);
    }
    
    public void setAutoCls(boolean autoCls){
        super.getReader().setAutoCls(autoCls);
    }
    
    public void setIncludeXml(boolean includeXml){
        super.getReader().setIncludeXml(includeXml);
    }
    
    public void setIncludeTrd(boolean includeTrd){
        super.getReader().setIncludeTrd(includeTrd);
    }
    
    // Set location of zipLocation and outputDirectory
    public void setReaderLocation(String zipLocation, String outputDirectory){
        if(zipLocation != null){
            super.getReader().setZipLocation(LogReader.USESERVER, zipLocation);
        }
        if(outputDirectory != null){
            super.getReader().setOutputDirectory(LogReader.USESERVER, outputDirectory);
        }
    }
    
    // Check if the zipLocation is valid
    public boolean checkValidZipLocation(){
        return super.getReader().checkValidZipLocation(LogReader.USESERVER);
    }
    
    public void resetAllFileList(){
        super.getReader().resetLogFileList(LogReader.USESERVER);
        super.getReader().resetSettingsFileList(LogReader.USESERVER);
        super.getReader().resetTradeFileList(LogReader.USESERVER);
    }
    
    // Get list of log file names read, used for displaying on the GUI combo box
    public String[] getLogFileListNames(){
        if(super.isTws() == true){
            return super.getReader().getTwsLogFileListNames(LogReader.USESERVER);
        } else {
            return super.getReader().getIBGLogFilesListNames(LogReader.USESERVER);
        }
    }
    
    public String[] getSettingsFileListNames(){
        if(super.isTws() == true){
            return super.getReader().getTwsSettingsFilesListNames(LogReader.USESERVER);
        } else {
            return super.getReader().getIbgSettingsFilesListNames(LogReader.USESERVER);
        }
    }
    
    public String[] getTradeFileListNames(){
        if(super.isTws() == true){
            return super.getReader().getTradeFileListNames(LogReader.USESERVER);
        } else {
            return null;
        }
    }
    
    public String[] getScreenshotListNames(){
        return super.getReader().getScreenshotListNames(LogReader.USESERVER);
    }
    
    // Get the log file from today, used as the default choice in log combo boxes
    public String getTodayLogFileName(){
        if(super.isTws() == true){
            return super.getReader().getTodayTwsLogFileName();
        } else {
            return super.getReader().getTodayIbgLogFileName();
        }
    }
    
    public String getTodaySettingsFileName(){
        if(super.isTws() == true){
            return super.getReader().getTodayTwsSettingsFileName();
        } else {
            return super.getReader().getTodayIbgSettingsFileName();
        }
    }
    
    public String getTodayTradeFileName(){
        if(super.isTws() == true){
            return super.getReader().getTodayTradeFileName();
        } else {
            return null;
        }
    }
    
    public String getFirstScreenshotName(){
        return super.getReader().getFirstScreenshotName();
    }
    
    // Select file for analyzing. Triggered when selecting log file from GUI combo box
    public void selectLogFile(String s, boolean useManual){
        if(useManual){
            super.getReader().selectLogFileManual(s);
        } else {
            if(super.isTws() == true){
                super.getReader().selectTwsLogFile(s);
            } else {
                super.getReader().selectIbgLogFile(s);
            }
        }
    }
    
    public void selectSettingsFile(String s, boolean useManual){
        if(useManual){
            super.getReader().selectSettingsFileManual(s);
        } else {
            if(super.isTws() == true){
                super.getReader().selectTwsSettingsLogFile(s);
            } else {
                super.getReader().selectIbgSettingsLogFile(s);
            }
        }
    }
    
    public void selectTradeFile(String s){
        if(super.isTws() == true){
            super.getReader().selectTradeFile(s);
        }
    }
    
    public void selectScreenshot(String s){
        super.getReader().selectScreenshot(s);
    }
    
    public void openLogFileInNotePad(boolean useManual){
        try {
            super.getReader().openLogFileInNotePad(super.isTws(), useManual);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void openScreenshot(){
        try {
            super.getReader().openScreenshots();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void regExSearch(boolean useManual, String regEx, boolean isCaseSensitive, javax.swing.JTextPane textPane){
        try {
            super.getReader().regExSearch(super.isTws(), useManual, regEx, isCaseSensitive, textPane);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // Start parsing log information, triggered when one of the analyze button is clicked.
    public void startParse(int choice, boolean useManual, HashMap<Integer, javax.swing.JTextPane> textPaneList){
        try {
            if(super.isDeepDiagnostic() == true){
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                super.getReader().parseTwsLogFileDeep(choice, super.isTws(), useManual, textPaneList);
            } else {
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                super.getReader().parseTwsLogFileShallow(choice, super.isTws(), useManual, textPaneList);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
