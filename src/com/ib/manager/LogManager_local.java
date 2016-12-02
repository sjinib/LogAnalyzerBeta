/**
 * The LogManager manages commands from GUI and give signals to Reader to perform corresponding actions
 */
package com.ib.manager;

import com.ib.reader.*;
import java.util.HashMap;

public class LogManager_local extends LogManager{
    //private final LogReader reader;
    //private boolean isDeepDiagnostic = true; // True if use deep analysis
    //private boolean isTWS; // If the files being investigated are from TWS (true) or IBG (false)
    
    public LogManager_local(){
        super();
    }   
    
    // Invoke to extract file
    public void extract(){
        try {
            super.getReader().extractZip(LogReader.USELOCAL);
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
            super.getReader().setZipLocation(LogReader.USELOCAL, zipLocation);
        }
        if(outputDirectory != null){
            super.getReader().setOutputDirectory(LogReader.USELOCAL, outputDirectory);
        }
    }
    
    // Check if the zipLocation is valid
    public boolean checkValidZipLocation(){
        return super.getReader().checkValidZipLocation(LogReader.USELOCAL);
    }
    
    public void resetAllFileList(){
        super.getReader().resetLogFileList(LogReader.USELOCAL);
        super.getReader().resetSettingsFileList(LogReader.USELOCAL);
        super.getReader().resetTradeFileList(LogReader.USELOCAL);
        super.getReader().resetScreenshotList(LogReader.USELOCAL);
    }
    
    // Get list of log file names read, used for displaying on the GUI combo box
    public String[] getLogFileListNames(){
        if(super.isTws() == true){
            return super.getReader().getTwsLogFileListNames(LogReader.USELOCAL);
        } else {
            return super.getReader().getIBGLogFilesListNames(LogReader.USELOCAL);
        }
    }
    
    public String[] getSettingsFileListNames(){
        if(super.isTws() == true){
            return super.getReader().getTwsSettingsFilesListNames(LogReader.USELOCAL);
        } else {
            return super.getReader().getIbgSettingsFilesListNames(LogReader.USELOCAL);
        }
    }
    
    public String[] getTradeFileListNames(){
        if(super.isTws() == true){
            return super.getReader().getTradeFileListNames(LogReader.USELOCAL);
        } else {
            return null;
        }
    }
    
    public String[] getScreenshotListNames(){
        return super.getReader().getScreenshotListNames(LogReader.USELOCAL);
    }
    
    // Get the log file from today, used as the default choice in log combo boxes
    public String getTodayLogFileName(){
        if(super.isTws() == true){
            return super.getReader().getTodayTwsLogFileName(LogReader.USELOCAL);
        } else {
            return super.getReader().getTodayIbgLogFileName(LogReader.USELOCAL);
        }
    }
    
    public String getTodaySettingsFileName(){
        if(super.isTws() == true){
            return super.getReader().getTodayTwsSettingsFileName(LogReader.USELOCAL);
        } else {
            return super.getReader().getTodayIbgSettingsFileName(LogReader.USELOCAL);
        }
    }
    
    public String getTodayTradeFileName(){
        if(super.isTws() == true){
            return super.getReader().getTodayTradeFileName(LogReader.USELOCAL);
        } else {
            return null;
        }
    }
    
    public String getFirstScreenshotName(){
        return super.getReader().getFirstScreenshotName(LogReader.USELOCAL);
    }
    
    // Select file for analyzing. Triggered when selecting log file from GUI combo box
    public void selectLogFile(String s, boolean useManual){
        if(useManual){
            super.getReader().selectLogFileManual(s);
        } else {
            if(super.isTws() == true){
                super.getReader().selectTwsLogFile(LogReader.USELOCAL, s);
            } else {
                super.getReader().selectIbgLogFile(LogReader.USELOCAL, s);
            }
        }
    }
    
    public void selectSettingsFile(String s, boolean useManual){
        if(useManual){
            super.getReader().selectSettingsFileManual(s);
        } else {
            if(super.isTws() == true){
                super.getReader().selectTwsSettingsLogFile(LogReader.USELOCAL, s);
            } else {
                super.getReader().selectIbgSettingsLogFile(LogReader.USELOCAL, s);
            }
        }
    }
    
    public void selectTradeFile(String s){
        if(super.isTws() == true){
            super.getReader().selectTradeFile(LogReader.USELOCAL, s);
        }
    }
    
    public void selectScreenshot(String s){
        super.getReader().selectScreenshot(LogReader.USELOCAL, s);
    }
    
    public void openLogFileInNotePad(boolean useManual){
        try {
            super.getReader().openLogFileInNotePad(LogReader.USELOCAL, super.isTws(), useManual);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void openScreenshot(){
        try {
            super.getReader().openScreenshots(LogReader.USELOCAL);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void regExSearch(boolean useManual, String regEx, boolean isCaseSensitive, javax.swing.JTextPane textPane){
        try {
            super.getReader().regExSearch(LogReader.USELOCAL, super.isTws(), useManual, regEx, isCaseSensitive, textPane);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean checkFileSizeForAll(boolean useManual){
        try {
            return super.getReader().checkFileSizeForAll(LogReader.USELOCAL, super.isTws(), useManual);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    // Start parsing log information, triggered when one of the analyze button is clicked.
    public void startParse(int choice, boolean useManual, HashMap<Integer, javax.swing.JTextPane> textPaneList){
        try {
            if(super.isDeepDiagnostic() == true){
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                super.getReader().parseTwsLogFileDeep(LogReader.USELOCAL, choice, super.isTws(), useManual, textPaneList);
            } else {
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                super.getReader().parseTwsLogFileShallow(LogReader.USELOCAL, choice, super.isTws(), useManual, textPaneList);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void reset(){
        super.getReader().resetLocal();
    }
}
