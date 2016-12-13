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
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownServiceException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

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
        System.setProperty("java.net.useSystemProxies","true");
        
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("twserror", "twserr67".toCharArray());
            }
        });
    }
    
    private void setTrustManagerForSSL(){
        SimpleX509TrustManager.disableSSL();
    }
    
    public void clearUserDiagnosticFileList(){
        userDiagnosticFileList.clear();
    }
    
    public boolean loadUserDiagnosticFileList(String username, boolean todayOnly){
        if(todayOnly){
            try{
                org.jsoup.nodes.Document doc = Jsoup.connect("https://wit1.interactivebrokers.com/cgi-bin/tws/tws_error_reader.pl")
                        .timeout(10000)
                        .get();
                
                Elements contents = doc.getElementsContainingOwnText(username);
                String date = getTodayDate();
                for(Element link: contents){
                    String diagnosticFileName = link.getElementsByAttribute("name").text();
                    //System.out.println(text);
                    String[] sp = diagnosticFileName.split("\\.");
                    //System.out.println(s[2]);
                    if((sp[2].compareTo(date)) >= 0){
                        //System.out.println(s[2]);
                        if(diagnosticFileName == null){
                            userDiagnosticFileList = new ArrayList<String>();
                        }
                        userDiagnosticFileList.add(diagnosticFileName);
                    } else {
                        return userDiagnosticFileList.isEmpty() ? false : true;
                    }
                }
                return userDiagnosticFileList.isEmpty() ? false : true;
            } catch (UnknownServiceException e){
                //e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            } catch (IOException e){
                //e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else {
            try{
                URL url = new URL("https://wit1.interactivebrokers.com/cgi-bin/tws/tws_error_reader.pl");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                
                // Load all
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    if(inputLine.contains(">" + username)){
                        String diagnosticFileName = inputLine.replaceAll("\\<.*?>","").replaceAll("\\s+", "");
                        if(diagnosticFileName == null){
                            userDiagnosticFileList = new ArrayList<String>();
                        }
                        userDiagnosticFileList.add(diagnosticFileName);
                    }
                in.close();
                return userDiagnosticFileList.isEmpty() ? false : true;
            } catch (UnknownServiceException e){
                //e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            } catch (IOException e){
                //e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        
        return false;
        
    }
    
    private String getTodayDate(){
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(today);
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
    
    public String getSelectedUserDiagnosticFile(){
        if(selectedUserDiagnosticFile == null)
                return null;
            return new String(selectedUserDiagnosticFile);
    }
    
    public boolean downloadDiagnosticFile(String zipLocation){
        try{
            URL downloadLink = new URL(new String("https://206.106.137.34/tws/data/" + selectedUserDiagnosticFile));
            HttpURLConnection con = (HttpURLConnection)downloadLink.openConnection();
            
            con.setRequestMethod("GET");
            InputStream input = con.getInputStream();
            FileOutputStream output = new FileOutputStream(zipLocation);
            CopyFile.copy(input, output, 1024);
            this.getReader().setZipLocation(LogReader.USESERVER, zipLocation);
            return true;
        } catch (UnknownServiceException e){
            //e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        } catch (IOException e){
            //e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
    
    // Invoke to extract file
    public void extract(){
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
        // Deprecated
        //super.setDeepDiagnostic(isDeepDiag);
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
    
    public String getZipLocation(){
        return super.getReader().getZipLocation(LogReader.USESERVER);
    }
    
    // Check if the zipLocation is valid
    public boolean checkValidZipLocation(){
        return super.getReader().checkValidZipLocation(LogReader.USESERVER);
    }
    
    public void resetAllFileList(){
        super.getReader().resetLogFileList(LogReader.USESERVER);
        super.getReader().resetSettingsFileList(LogReader.USESERVER);
        super.getReader().resetTradeFileList(LogReader.USESERVER);
        super.getReader().resetScreenshotList(LogReader.USESERVER);
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
            return super.getReader().getTodayTwsLogFileName(LogReader.USESERVER);
        } else {
            return super.getReader().getTodayIbgLogFileName(LogReader.USESERVER);
        }
    }
    
    public String getTodaySettingsFileName(){
        if(super.isTws() == true){
            return super.getReader().getTodayTwsSettingsFileName(LogReader.USESERVER);
        } else {
            return super.getReader().getTodayIbgSettingsFileName(LogReader.USESERVER);
        }
    }
    
    public String getTodayTradeFileName(){
        if(super.isTws() == true){
            return super.getReader().getTodayTradeFileName(LogReader.USESERVER);
        } else {
            return null;
        }
    }
    
    public String getFirstScreenshotName(){
        return super.getReader().getFirstScreenshotName(LogReader.USESERVER);
    }
    
    // Select file for analyzing. Triggered when selecting log file from GUI combo box
    public void selectLogFile(String s){
        if(super.isTws() == true){
            super.getReader().selectTwsLogFile(LogReader.USESERVER, s);
        } else {
            super.getReader().selectIbgLogFile(LogReader.USESERVER, s);
        }
    }
    
    public void selectSettingsFile(String s, boolean useManual){
        if(useManual){
            super.getReader().selectSettingsFileManual(s);
        } else {
            if(super.isTws() == true){
                super.getReader().selectTwsSettingsLogFile(LogReader.USESERVER, s);
            } else {
                super.getReader().selectIbgSettingsLogFile(LogReader.USESERVER, s);
            }
        }
    }
    
    public void selectTradeFile(String s){
        if(super.isTws() == true){
            super.getReader().selectTradeFile(LogReader.USESERVER, s);
        }
    }
    
    public void selectScreenshot(String s){
        super.getReader().selectScreenshot(LogReader.USESERVER, s);
    }
    
    public void openLogFileInNotePad(boolean useManual){
        try {
            super.getReader().openLogFileInNotePad(LogReader.USESERVER, super.isTws(), useManual);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void openScreenshot(){
        try {
            super.getReader().openScreenshots(LogReader.USESERVER);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void regExSearch(String regEx, boolean isCaseSensitive, javax.swing.JTextPane textPane){
        try {
            super.getReader().regExSearch(LogReader.USESERVER, super.isTws(), false, regEx, isCaseSensitive, textPane);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean checkFileSizeForAll(){
        try {
            return super.getReader().checkFileSizeForAll(LogReader.USESERVER, super.isTws(), false);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    // Start parsing log information, triggered when one of the analyze button is clicked.
    public void startParse(int choice, HashMap<Integer, javax.swing.JTextPane> textPaneList){
        try {
            if(super.isDeepDiagnostic() == true){
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                super.getReader().parseTwsLogFileDeep(LogReader.USESERVER, choice, super.isTws(), false, textPaneList);
            } else {
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                super.getReader().parseTwsLogFileShallow(LogReader.USESERVER, choice, super.isTws(), false, textPaneList);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void reset(){
        super.getReader().resetServer();
        
        userDiagnosticFileList.clear();
        selectedUserDiagnosticFile = "";
    }
}
