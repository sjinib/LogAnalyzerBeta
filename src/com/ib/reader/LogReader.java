package com.ib.reader;

import com.ib.parser.*;
import com.ib.message.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import java.util.HashMap;

public class LogReader {
	boolean isTWS;
	private String twsLogFile = "tws.log";
	private String settingsFile = "tws.error.xml";
	
	private String zipLocation = null;
	private String outputDirectory = null;
	private List<File> fileList;
	
	private MarketDataSettingsMessage mdSettingsMessage;
	private ApiSettingsMessage apiSettingsMessage;
	private EnvSettingsMessage envSettingsMessage;
	
	public LogReader(boolean isTWS){
		mdSettingsMessage = new MarketDataSettingsMessage();
		apiSettingsMessage = new ApiSettingsMessage();
		envSettingsMessage = new EnvSettingsMessage();
		this.isTWS = isTWS;
		//outputDirectory = "/Users/sitengjin/Documents/Github/LogAnalyzer/Temp";
		this.outputDirectory = new String("C:/Users/Siteng Jin/Documents/GitHub/LogAnalyzer/Temp");
	}
	
	public void setOutputDirectory(String dir){
		if(dir != null){
			outputDirectory = dir;
		}
	}
	
	public void setZipLocation(String dir){
		if(dir != null){
			zipLocation = dir;
		}
	}
	
	public void extractZip() throws Exception{
		if(zipLocation != null && outputDirectory != null){
			ExtractZip.unZipIt(zipLocation, outputDirectory);
		}
	}
	
	public void loadFilesList() throws Exception{
		if(outputDirectory == null){
			throw new Exception("Invalid Directory");
		}
		
		if(fileList == null){
			File dir = new File(outputDirectory);
			fileList = (List<File>) FileUtils.listFiles(dir, new String[] {"log", "xml"}, true);
		}
	}
	
	public File getSettingsFile(){
		for(File file: fileList){
			if(file.getName().equals(this.settingsFile)){
				return new File(file.getPath());
			}
		}
		return null;
	}
	
	public File getTwsLogFile() throws Exception{
		loadFilesList();
		
		for(File file: fileList){
			if(file.getName().equals(this.twsLogFile)){
				return new File(file.getPath());
			}
		}
		
		return null;
	}
	
	public void parseSettingsFile(int choice) throws Exception{
		File settings = this.getSettingsFile();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(settings);
		doc.getDocumentElement().normalize();
		
		switch (choice) {
		case Choices.API:
			SettingsLogParser.parseAPISettingsFile(doc, apiSettingsMessage);
			break;
		case Choices.MKTDATA:
			SettingsLogParser.parseMDSettingsFile(doc, mdSettingsMessage);
			break;
		case Choices.ENV:
			SettingsLogParser.parseEnvSettingsFile(doc, envSettingsMessage);
			break;
		default:
			break;
		}
	}
	
	public void parseTwsLogFileDeep(int choice) throws Exception{
		File twsLogFile = this.getTwsLogFile();
		
		switch (choice) {
		case Choices.ENV:
			parseSettingsFile(choice);
			TwsLogParserDeep.parseTwsEnvInfo(twsLogFile);
			break;
		case Choices.LOGINSEQ:
			TwsLogParserDeep.parseTwsLoginSeqInfo(twsLogFile);
			break;
		case Choices.SYSRES:
			TwsLogParserDeep.parseTwsSysRes(twsLogFile);
			break;
		case Choices.MKTDATA:
			parseSettingsFile(choice);
			System.out.println(mdSettingsMessage.getCopyMdSettingsList().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSettingsList().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListOpt().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListStk().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListFut().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListInd().toString());
			System.out.println(mdSettingsMessage.getCopySmartRoutSettingsList().toString());
			TwsLogParserDeep.parseTwsMktData(twsLogFile);
			break;
		case Choices.CONN:
			TwsLogParserDeep.parseTwsConn(twsLogFile);
			break;
		case Choices.HTBP:
			TwsLogParserDeep.parseTwsHtbp(twsLogFile);
			break;		
		case Choices.API:
			parseSettingsFile(choice);
			System.out.println(apiSettingsMessage.getCopyApiSettingsList().toString());
			System.out.println(apiSettingsMessage.getCopyApiPrecautionsList().toString());
			System.out.println(apiSettingsMessage.getCopyTrustedIPs().toString());
			TwsLogParserDeep.parseTwsApi(twsLogFile);	
			break;
		case Choices.ORDERSTRDS:
			TwsLogParserDeep.parseTwsOrderTrds(twsLogFile);
			break;	
		default:
			break;
		}
	}
	
	public void parseTwsLogFileShallow(int choice) throws Exception{
		File twsLogFile = this.getTwsLogFile();
		
		TwsLogParserDeep twsLogParser = new TwsLogParserDeep();
		
		switch (choice) {
		case Choices.ENV:
			TwsLogParserDeep.parseTwsEnvInfo(twsLogFile);
			break;
		case Choices.LOGINSEQ:
			TwsLogParserDeep.parseTwsLoginSeqInfo(twsLogFile);
			break;
		case Choices.SYSRES:
			TwsLogParserDeep.parseTwsSysRes(twsLogFile);
			break;
		case Choices.MKTDATA:
			parseSettingsFile(choice);
			System.out.println(mdSettingsMessage.getCopyMdSettingsList().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSettingsList().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListOpt().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListStk().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListFut().toString());
			System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListInd().toString());
			System.out.println(mdSettingsMessage.getCopySmartRoutSettingsList().toString());
			TwsLogParserDeep.parseTwsMktData(twsLogFile);
			break;
		case Choices.CONN:
			TwsLogParserDeep.parseTwsConn(twsLogFile);
			break;
		case Choices.HTBP:
			TwsLogParserDeep.parseTwsHtbp(twsLogFile);
			break;		
		case Choices.API:
			parseSettingsFile(choice);
			System.out.println(apiSettingsMessage.getCopyApiSettingsList().toString());
			System.out.println(apiSettingsMessage.getCopyApiPrecautionsList().toString());
			System.out.println(apiSettingsMessage.getCopyTrustedIPs().toString());
			TwsLogParserDeep.parseTwsApi(twsLogFile);			
			break;
		case Choices.ORDERSTRDS:
			TwsLogParserDeep.parseTwsOrderTrds(twsLogFile);
			break;	
		default:
			break;
		}
	}
	
	public void testPrint(){
		System.out.println(apiSettingsMessage.getCopyApiSettingsList().toString());
		System.out.println(apiSettingsMessage.getCopyApiPrecautionsList().toString());
		System.out.println(apiSettingsMessage.getCopyTrustedIPs().toString());
		System.out.println(mdSettingsMessage.getCopyMdSettingsList().toString());
		System.out.println(mdSettingsMessage.getCopyEsignalSettingsList().toString());
		System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListOpt().toString());
		System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListStk().toString());
		System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListFut().toString());
		System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListInd().toString());
		System.out.println(mdSettingsMessage.getCopySmartRoutSettingsList().toString());
	}
	
}
