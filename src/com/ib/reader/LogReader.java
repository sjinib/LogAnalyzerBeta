package com.ib.reader;

import com.ib.parser.*;
import com.ib.message.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import java.util.HashMap;

public class LogReader {
    private String todayTwsLogFile = new String();
    private String todayIbgLogFile = new String();
    private String todayTwsSettingsFile = new String();
    private String todayIbgSettingsFile = new String();
    
    private String selectedTwsLogFile = new String();
    private String selectedIbgLogFile = new String();
    private String selectedTwsSettingsFile = new String();
    private String selectedIbgSettingsFile = new String();
    
    private String manualSelectedLogFile = new String();
    private String manualSelectedSettingsFile = new String();
    
    private String zipLocation = null;
    private String outputDirectory = null;
    private List<File> twsLogFileList = null;
    private List<File> ibgLogFileList = null;
    private List<File> twsSettingsFileList = null;
    private List<File> ibgSettingsFileList = null;
    
    private MarketDataSettingsMessage mdSettingsMessage;
    private ApiSettingsMessage apiSettingsMessage;
    private EnvSettingsMessage envSettingsMessage;
    
    public LogReader(){
        mdSettingsMessage = new MarketDataSettingsMessage();
        apiSettingsMessage = new ApiSettingsMessage();
        envSettingsMessage = new EnvSettingsMessage();
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
    
    public void resetLogFileList(){
        if(twsLogFileList != null){
            twsLogFileList.clear();
            twsLogFileList = null;
        }
        if(ibgLogFileList != null){
            ibgLogFileList.clear();
            ibgLogFileList = null;            
        }
    }
    
    public void resetSettingsFileList(){
        if(twsSettingsFileList != null){
            twsSettingsFileList.clear();
            twsSettingsFileList = null;
        }
        if(ibgSettingsFileList != null){
            ibgSettingsFileList.clear();
            ibgSettingsFileList = null;
        }
    }
    
    public void extractZip() throws Exception{
        if(zipLocation != null && outputDirectory != null){
            ExtractZip.unZipIt(zipLocation, outputDirectory);
        }
    }
    
    // Return String array of names of tws log list for tws log selector
    public String [] getTwsLogFileListNames() {
        if(twsLogFileList == null){
            try {
                if(loadTwsLogFileList()){
                    String[] list = new String[twsLogFileList.size()];
                    for(int i = 0; i < twsLogFileList.size(); i++){
                        list[i] = twsLogFileList.get(i).getName();
                    }
                    return list;
                } else
                    return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } else {
            String[] list = new String[twsLogFileList.size()];
            for(int i = 0; i < twsLogFileList.size(); i++){
                list[i] = twsLogFileList.get(i).getName();
            }
            return list;
        }
    }
    
    public String [] getIBGLogFilesListNames() {        
        if(ibgLogFileList == null || ibgLogFileList.isEmpty()){
            try {
                if(loadIBGLogFileList() == true){
                    String[] list = new String[ibgLogFileList.size()];
                    for(int i = 0; i < ibgLogFileList.size(); i++){
                        list[i] = ibgLogFileList.get(i).getName();
                    }
                    return list;
                } else 
                    return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } else {
            String[] list = new String[ibgLogFileList.size()];
            for(int i = 0; i < ibgLogFileList.size(); i++){
                list[i] = ibgLogFileList.get(i).getName();
            }
            return list;
        }
    }
    
    public String [] getTwsSettingsFilesListNames() {
        if(twsSettingsFileList == null){
            try {
                if(loadTwsSettingsFileList()){
                    String[] list = new String[twsSettingsFileList.size()];
                    for(int i = 0; i < twsSettingsFileList.size(); i++){
                        list[i] = twsSettingsFileList.get(i).getName();
                    }
                    return list;
                } else
                    return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } else {
            String[] list = new String[twsSettingsFileList.size()];
            for(int i = 0; i < twsSettingsFileList.size(); i++){
                list[i] = twsSettingsFileList.get(i).getName();
            }
            return list;
        }
    }
    
    public String [] getIbgSettingsFilesListNames() {
        if(ibgSettingsFileList == null){
            try {
                if(loadIbgSettingsFileList()){
                    String[] list = new String[ibgSettingsFileList.size()];
                    for(int i = 0; i < ibgSettingsFileList.size(); i++){
                        list[i] = ibgSettingsFileList.get(i).getName();
                    }
                    return list;
                } else
                    return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } else {
            String[] list = new String[ibgSettingsFileList.size()];
            for(int i = 0; i < ibgSettingsFileList.size(); i++){
                list[i] = ibgSettingsFileList.get(i).getName();
            }
            return list;
        }
    }
    
    public boolean loadTwsLogFileList() throws Exception{
        if(outputDirectory == null){
            throw new Exception("Invalid Directory");
        }
        
        if(twsLogFileList != null && !twsLogFileList.isEmpty()){
            twsLogFileList.clear();
        }
        
        File dir = new File(outputDirectory);
        //fileList = (List<File>) FileUtils.listFiles(dir, new String[] {"log", "xml"}, true);
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("tws.log", "tws.*.log", "log.*.txt"));
        twsLogFileList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(twsLogFileList == null || twsLogFileList.isEmpty()){
            //System.out.println("Cannot find file.");
            javax.swing.JOptionPane.showMessageDialog(null, "No TWS log file is found.");
            todayTwsLogFile = null;
            return false;
        }
        
        boolean hasToday = false;
        String dayLogName = new String("log." + CurrentDay.findCurrentDay() + ".txt");
        for(File file : twsLogFileList){
            if(file.getName().equals("tws.log")){
                todayTwsLogFile = "tws.log";
                hasToday = true;
                break;
            }
        }
        if(hasToday == true){
            return true;
        } else {
            for(File file : twsLogFileList){
                if(file.getName().equals(dayLogName)){
                    todayTwsLogFile = dayLogName;
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            } else {
                todayTwsLogFile = twsLogFileList.get(0).getName();
                return true;
            }
        }        
    }
    
    public boolean loadIBGLogFileList() throws Exception{
        if(outputDirectory == null){
            throw new Exception("Invalid Directory");
        }
        
        if(ibgLogFileList != null && !ibgLogFileList.isEmpty()){
            ibgLogFileList.clear();
        }
        
        File dir = new File(outputDirectory);
        //fileList = (List<File>) FileUtils.listFiles(dir, new String[] {"log", "xml"}, true);
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("ibgateway.log", "ibgateway.*.log"));
        ibgLogFileList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(ibgLogFileList == null || ibgLogFileList.isEmpty()){
            javax.swing.JOptionPane.showMessageDialog(null, "No IB Gateway log file is found.");
            todayIbgLogFile = null;
            return false;
        }        
        
        boolean hasToday = false;
        String dayLogName = new String("ibgateway." + CurrentDay.findCurrentDay() + ".log");
        for(File file : ibgLogFileList){
            if(file.getName().equals("ibgateway.log")){
                todayIbgLogFile = "ibgateway.log";
                hasToday = true;
                break;
            }
        }
        if(hasToday == true){
            return true;
        } else {
            for(File file : ibgLogFileList){
                if(file.getName().equals(dayLogName)){
                    todayIbgLogFile = dayLogName;
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            } else {                
                todayIbgLogFile = ibgLogFileList.get(0).getName();
                return true;
            }
        }
    }
    
    public boolean loadTwsSettingsFileList() throws Exception{
        if(outputDirectory == null){
            throw new Exception("Invalid Directory");
        }
        
        if(twsSettingsFileList != null && !twsSettingsFileList.isEmpty()){
            twsSettingsFileList.clear();
        }
        
        File dir = new File(outputDirectory);
        //fileList = (List<File>) FileUtils.listFiles(dir, new String[] {"log", "xml"}, true);
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("tws.*.xml"));
        twsSettingsFileList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(twsSettingsFileList == null || twsSettingsFileList.isEmpty()){
            //System.out.println("Cannot find file.");
            javax.swing.JOptionPane.showMessageDialog(null, "No TWS settings file is found.");
            todayTwsSettingsFile = null;
            return false;
        }
        
        boolean hasToday = false;
        for(File file : twsSettingsFileList){
            if(file.getName().equals("tws.error.xml")){
                todayTwsSettingsFile = "tws.error.xml";
                hasToday = true;
                break;
            }
        }
        if(hasToday == true){
            return true;
        }else {
            todayTwsSettingsFile = twsSettingsFileList.get(0).getName();
            return true;
        }        
    }
    
    public boolean loadIbgSettingsFileList() throws Exception{
        if(outputDirectory == null){
            throw new Exception("Invalid Directory");
        }
        
        if(ibgSettingsFileList != null && !ibgSettingsFileList.isEmpty()){
            ibgSettingsFileList.clear();
        }
        
        File dir = new File(outputDirectory);
        //fileList = (List<File>) FileUtils.listFiles(dir, new String[] {"log", "xml"}, true);
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("ibg.*.xml", "ibg.xml"));
        ibgSettingsFileList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(ibgSettingsFileList == null || ibgSettingsFileList.isEmpty()){
            //System.out.println("Cannot find file.");
            javax.swing.JOptionPane.showMessageDialog(null, "No IB Gateway settings file is found.");
            todayIbgSettingsFile = null;
            return false;
        }
        
        boolean hasToday = false;
        for(File file : ibgSettingsFileList){
            if(file.getName().equals("ibg.xml")){
                todayIbgSettingsFile = "ibg.xml";
                hasToday = true;
            }
        }
        if(hasToday == true){
            return true;
        }else {
            todayIbgSettingsFile = ibgSettingsFileList.get(0).getName();
            return true;
        }        
    }
    
    public String getTodayTwsSettingsFileName(){
        if(todayTwsSettingsFile == null)
            return null;
        return new String(todayTwsSettingsFile);
    }
    
    public String getTodayIbgSettingsFileName(){
        if(todayIbgSettingsFile == null)
            return null;
        return new String(todayIbgSettingsFile);
    }
    
    public String getTodayTwsLogFileName(){
        if(todayTwsLogFile == null)
            return null;
        return new String(todayTwsLogFile);
    }
    
    public String getTodayIbgLogFileName(){
        if(todayIbgLogFile == null)
            return null;
        return new String(todayIbgLogFile);
    }
    
    public void selectTwsLogFile(String s){
        selectedTwsLogFile = s;
    }
    
    public void selectIbgLogFile(String s){
        selectedIbgLogFile = s;
    }
    
    public void selectTwsSettingsLogFile(String s){
        selectedTwsSettingsFile = s;
    }
    
    public void selectIbgSettingsLogFile(String s){
        selectedIbgSettingsFile = s;
    }
    
    public void selectLogFileManual(String s){
        manualSelectedLogFile = s;
    }
    
    public void selectSettingsFileManual(String s){
        manualSelectedSettingsFile = s;
    }
    
    private File getSelectedTwsSettingsFile(){
        for(File file: twsSettingsFileList){
            if(file.getName().equals(this.selectedTwsSettingsFile)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedIbgSettingsFile(){
        for(File file: ibgSettingsFileList){
            if(file.getName().equals(this.selectedIbgSettingsFile)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedTwsLogFile() throws Exception{
        for(File file: twsLogFileList){
            if(file.getName().equals(this.selectedTwsLogFile)){
                return new File(file.getPath());
            }
        }        
        return null;
    }
    
    private File getSelectedIbgLogFile() throws Exception{
        for(File file: ibgLogFileList){
            if(file.getName().equals(this.selectedIbgLogFile)){
                return new File(file.getPath());
            }
        }        
        return null;
    }
    
    private File getSelectedLogFileManual() throws Exception{
        return new File(manualSelectedLogFile);
    }
    
    private File getSelectedSettingsFileManual() throws Exception{
        return new File(manualSelectedSettingsFile);
    }
    
    public void parseSettingsFile(int choice, boolean isTws, boolean useManual) throws Exception{
        File currentSettingsFile;
        if(useManual){
            currentSettingsFile = this.getSelectedSettingsFileManual();
        } else {
            if(isTws == true){
                currentSettingsFile = this.getSelectedTwsSettingsFile();
            } else {
                currentSettingsFile = this.getSelectedIbgSettingsFile();
            }
        }
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(currentSettingsFile);
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
    
    public void parseTwsLogFileDeep(int choice, boolean isTws, boolean useManual, HashMap<Integer, javax.swing.JTextPane> textPaneList) throws Exception{
        File currentLogFile;
        if(useManual){
            currentLogFile = this.getSelectedLogFileManual();
        } else {
            if(isTws == true){
                currentLogFile = this.getSelectedTwsLogFile();
            } else {
                currentLogFile = this.getSelectedIbgLogFile();
            }
        }
        
        switch (choice) {
            case Choices.ENV:
                parseSettingsFile(choice, isTws, useManual);
                TwsLogParserDeep.parseTwsEnvInfo(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.LOGINSEQ:
                TwsLogParserDeep.parseTwsLoginSeqInfo(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.SYSRES:
                TwsLogParserDeep.parseTwsSysRes(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.MKTDATA:
                //System.out.println(mdSettingsMessage.getCopyMdSettingsList().toString());
                //System.out.println(mdSettingsMessage.getCopyEsignalSettingsList().toString());
                //System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListOpt().toString());
                //System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListStk().toString());
                //System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListFut().toString());
                //System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListInd().toString());
                //System.out.println(mdSettingsMessage.getCopySmartRoutSettingsList().toString());
                parseSettingsFile(choice, isTws, useManual);
                TwsLogParserDeep.parseTwsMktData(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.CONN:
                TwsLogParserDeep.parseTwsConn(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.HTBP:
                TwsLogParserDeep.parseTwsHtbp(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.API:
                //System.out.println(apiSettingsMessage.getCopyApiSettingsList().toString());
                //System.out.println(apiSettingsMessage.getCopyApiPrecautionsList().toString());
                //System.out.println(apiSettingsMessage.getCopyTrustedIPs().toString());
                parseSettingsFile(choice, isTws, useManual);
                TwsLogParserDeep.parseTwsApi(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.ORDERSTRDS:
                TwsLogParserDeep.parseTwsOrderTrds(currentLogFile, textPaneList.get(choice));
                break;
            default:
                break;
        }
    }
    
    public void parseTwsLogFileShallow(int choice, boolean isTws, boolean useManual, HashMap<Integer, javax.swing.JTextPane> textPaneList) throws Exception{
        File currentLogFile;
        if(useManual){
            currentLogFile = this.getSelectedLogFileManual();
        } else {
            if(isTws == true){
                currentLogFile = this.getSelectedTwsLogFile();
            } else {
                currentLogFile = this.getSelectedIbgLogFile();
            }
        }
        
        switch (choice) {
            case Choices.ENV:
                //TwsLogParserDeep.parseTwsEnvInfo(twsLogFile, textPaneList.get("ENV"));
                break;
            case Choices.LOGINSEQ:
                //TwsLogParserDeep.parseTwsLoginSeqInfo(twsLogFile);
                break;
            case Choices.SYSRES:
                //TwsLogParserDeep.parseTwsSysRes(twsLogFile);
                break;
            case Choices.MKTDATA:
                System.out.println(mdSettingsMessage.getCopyMdSettingsList().toString());
                System.out.println(mdSettingsMessage.getCopyEsignalSettingsList().toString());
                System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListOpt().toString());
                System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListStk().toString());
                System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListFut().toString());
                System.out.println(mdSettingsMessage.getCopyEsignalSecSettingsListInd().toString());
                System.out.println(mdSettingsMessage.getCopySmartRoutSettingsList().toString());
                //TwsLogParserDeep.parseTwsMktData(twsLogFile);
                break;
            case Choices.CONN:
                //TwsLogParserDeep.parseTwsConn(twsLogFile);
                break;
            case Choices.HTBP:
                //TwsLogParserDeep.parseTwsHtbp(twsLogFile);
                break;
            case Choices.API:
                System.out.println(apiSettingsMessage.getCopyApiSettingsList().toString());
                System.out.println(apiSettingsMessage.getCopyApiPrecautionsList().toString());
                System.out.println(apiSettingsMessage.getCopyTrustedIPs().toString());
                //TwsLogParserDeep.parseTwsApi(twsLogFile);
                break;
            case Choices.ORDERSTRDS:
                //TwsLogParserDeep.parseTwsOrderTrds(twsLogFile);
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
