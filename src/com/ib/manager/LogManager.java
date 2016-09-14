package com.ib.manager;

import com.ib.reader.*;
import java.util.HashMap;

public class LogManager {
    private final LogReader reader;
    private boolean isDeepDiagnostic = true;
    private boolean isTWS; // If the files being investigated are from TWS (true) or IBG (false)
    
    public LogManager(){
        reader = new LogReader();
    }
    
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
    
    public void setReaderLocation(String zipLocation, String outputDirectory){
        if(zipLocation != null){
            reader.setZipLocation(zipLocation);
        }
        if(outputDirectory != null){
            reader.setOutputDirectory(outputDirectory);
        }
    }
    
    public void resetAllFileList(){
        reader.resetLogFileList();
        reader.resetSettingsFileList();
    }
    
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
    
    public void startParse(int choice, boolean useManual, HashMap<Integer, javax.swing.JTextPane> textPaneList){
        try {
            if(isDeepDiagnostic == true){
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                reader.parseTwsLogFileDeep(choice, isTWS, useManual, textPaneList);
            } else {
                //reader.parseSettingsFile(choice, isTWS, useManual, textPaneList);
                //reader.parseTwsLogFileShallow(choice, useManual, textPaneList);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
