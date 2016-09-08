package com.ib.manager;

import com.ib.parser.*;
import com.ib.reader.*;
import java.io.File;
import com.ib.parser.Choices;
import java.util.HashMap;

public class LogManager {
	private LogReader reader;
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
        
        public void selectLogFile(String s){
            if(isTWS == true){
                reader.selectTwsLogFile(s);
            } else {
                reader.selectIbgLogFile(s);
            }
        }
        
        public void selectSettingsFile(String s){
            if(isTWS == true){
                reader.selectTwsSettingsLogFile(s);
            } else {
                reader.selectIbgSettingsLogFile(s);
            }
        }
        
        public void startParse(int choice, HashMap<String, javax.swing.JTextPane> textPaneList){
            try {
                if(isDeepDiagnostic == true){
                    reader.parseTwsLogFileDeep(choice, isTWS, textPaneList);
                } else {
                    reader.parseTwsLogFileShallow(choice, textPaneList);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
	
        /*
	public void start(){
		try {
			reader.loadTwsLogFileList();
			//File currentSettingsFile = reader.getTwsErrorXml();
			//reader.parseSettingsFile(currentSettingsFile, Choices.API);
			//reader.parseSettingsFile(currentSettingsFile, Choices.MD);
			if(this.isDeepDiagnostic == true){
				reader.parseTwsLogFileDeep(Choices.ENV);
			} else if(this.isDeepDiagnostic == false){
				//reader.parseTwsLogFileShallow(Choices.ENV);
			}
			//reader.testPrint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        */
}
