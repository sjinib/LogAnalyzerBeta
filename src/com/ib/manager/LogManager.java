package com.ib.manager;

import com.ib.parser.*;
import com.ib.reader.*;
import java.io.File;
import com.ib.parser.Choices;

public class LogManager {
	private LogReader reader;
	private boolean isDeepDiagnostic;
	
	public LogManager(){
		reader = new LogReader(true);
		this.isDeepDiagnostic = true;
	}
	
	public void extract(){
		try {
			reader.extractZip();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void setReaderLocation(String zipLocation, String outputDirectory){
		if(zipLocation != null){
			reader.setZipLocation(zipLocation);
		}
		if(outputDirectory != null){
			reader.setOutputDirectory(outputDirectory);
		}
	}
        
        public void loadFileList(){
            
        }
	
	public void start(){
		try {
			reader.loadTwsLogFilesList();
			//File currentSettingsFile = reader.getTwsErrorXml();
			//reader.parseSettingsFile(currentSettingsFile, Choices.API);
			//reader.parseSettingsFile(currentSettingsFile, Choices.MD);
			if(this.isDeepDiagnostic == true){
				//reader.parseTwsLogFileDeep(Choices.ENV);
			} else if(this.isDeepDiagnostic == false){
				//reader.parseTwsLogFileShallow(Choices.ENV);
			}
			//reader.testPrint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
