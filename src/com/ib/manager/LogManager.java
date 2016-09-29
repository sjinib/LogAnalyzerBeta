/**
 * The LogManager manages commands from GUI and give signals to Reader to perform corresponding actions
 */
package com.ib.manager;

import com.ib.reader.*;
import java.util.HashMap;

public class LogManager {
    private final LogReader reader;
    private boolean isDeepDiagnostic = true; // True if use deep analysis
    private boolean isTWS; // If the files being investigated are from TWS (true) or IBG (false)
    
    public LogManager(){
        reader = new LogReader();
    }
    
    // Invoke to extract file
    public void extract(){
        try {
            reader.extractZip();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void setIsTws(boolean isTws){
        isTWS = isTws;
    }
    
    public void setDeepDiagnostic(boolean isDeepDiag){
        isDeepDiagnostic = isDeepDiag;
    }
    
    public void setAutoCls(boolean autoCls){
        reader.setAutoCls(autoCls);
    }
    
    public void setIncludeXml(boolean includeXml){
        reader.setIncludeXml(includeXml);
    }
    
    public void setIncludeTrd(boolean includeTrd){
        reader.setIncludeTrd(includeTrd);
    }
    
    // Set location of zipLocation and outputDirectory
    public void setReaderLocation(String zipLocation, String outputDirectory){
        if(zipLocation != null){
            reader.setZipLocation(zipLocation);
        }
        if(outputDirectory != null){
            reader.setOutputDirectory(outputDirectory);
        }
    }
    
    // Check if the zipLocation is valid
    public boolean checkValidZipLocation(){
        return reader.checkValidZipLocation();
    }
    
    public void resetAllFileList(){
        reader.resetLogFileList();
        reader.resetSettingsFileList();
        reader.resetTradeFileList();
    }
    
    // Get list of log file names read, used for displaying on the GUI combo box
    public String[] getLogFileListNames(){
        if(isTWS == true){
            return reader.getTwsLogFileListNames();
        } else {
            return reader.getIBGLogFilesListNames();
        }
    }
    
    public String[] getSettingsFileListNames(){
        if(isTWS == true){
            return reader.getTwsSettingsFilesListNames();
        } else {
            return reader.getIbgSettingsFilesListNames();
        }
    }
    
    public String[] getTradeFileListNames(){
        if(isTWS == true){
            return reader.getTradeFileListNames();
        } else {
            return null;
        }
    }
    
    public String[] getScreenshotListNames(){
        return reader.getScreenshotListNames();
    }
    
    // Get the log file from today, used as the default choice in log combo boxes
    public String getTodayLogFileName(){
        if(isTWS == true){
            return reader.getTodayTwsLogFileName();
        } else {
            return reader.getTodayIbgLogFileName();
        }
    }
    
    public String getTodaySettingsFileName(){
        if(isTWS == true){
            return reader.getTodayTwsSettingsFileName();
        } else {
            return reader.getTodayIbgSettingsFileName();
        }
    }
    
    public String getTodayTradeFileName(){
        if(isTWS == true){
            return reader.getTodayTradeFileName();
        } else {
            return null;
        }
    }
    
    public String getFirstScreenshotName(){
        return reader.getFirstScreenshotName();
    }
    
    // Select file for analyzing. Triggered when selecting log file from GUI combo box
    public void selectLogFile(String s, boolean useManual){
        if(useManual){
            reader.selectLogFileManual(s);
        } else {
            if(isTWS == true){
                reader.selectTwsLogFile(s);
            } else {
                reader.selectIbgLogFile(s);
            }
        }
    }
    
    public void selectSettingsFile(String s, boolean useManual){
        if(useManual){
            reader.selectSettingsFileManual(s);
        } else {
            if(isTWS == true){
                reader.selectTwsSettingsLogFile(s);
            } else {
                reader.selectIbgSettingsLogFile(s);
            }
        }
    }
    
    public void selectTradeFile(String s){
        if(isTWS == true){
            reader.selectTradeFile(s);
        }
    }
    
    public void selectScreenshot(String s){
        reader.selectScreenshot(s);
    }
    
    public void openLogFileInNotePad(boolean useManual){
        try {
            reader.openLogFileInNotePad(isTWS, useManual);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void openScreenshot(){
        try {
            reader.openScreenshots();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void regExSearch(boolean useManual, String regEx, boolean isCaseSensitive, javax.swing.JTextPane textPane){
        try {
            reader.regExSearch(isTWS, useManual, regEx, isCaseSensitive, textPane);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // Start parsing log information, triggered when one of the analyze button is clicked.
    public void startParse(int choice, boolean useManual, HashMap<Integer, javax.swing.JTextPane> textPaneList){
        try {
            if(isDeepDiagnostic == true){
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                reader.parseTwsLogFileDeep(choice, isTWS, useManual, textPaneList);
            } else {
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                reader.parseTwsLogFileShallow(choice, isTWS, useManual, textPaneList);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
