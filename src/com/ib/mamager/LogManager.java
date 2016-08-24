package com.ib.mamager;

import com.ib.parser.*;
import com.ib.reader.*;
import java.io.File;
import com.ib.parser.Choices;

public class LogManager {
	private String twsVersion;
	private String apiVersion;
	private LogReader reader;
	private boolean isDeepDiagnostic;
	
	public LogManager(String twsVersion, String apiVersion){
		this.twsVersion = twsVersion;
		this.apiVersion = apiVersion;
	}
	
	public LogManager(){
		this.twsVersion = null;
		this.apiVersion = null;
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
	
	public void start(){
		try {
			reader.loadFilesList();
			//File currentSettingsFile = reader.getTwsErrorXml();
			//reader.parseSettingsFile(currentSettingsFile, Choices.API);
			//reader.parseSettingsFile(currentSettingsFile, Choices.MD);
			if(this.isDeepDiagnostic == true){
				reader.parseTwsLogFileDeep(Choices.ENV);
			} else if(this.isDeepDiagnostic == false){
				reader.parseTwsLogFileShallow(Choices.ENV);
			}
			//reader.testPrint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
