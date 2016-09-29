/**
 * The Reader class is the major class that handles all the file reading, parsing and data populating
 */
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
    // String name of today's log file
    private String todayTwsLogFile = new String();
    private String todayIbgLogFile = new String();
    private String todayTwsSettingsFile = new String();
    private String todayIbgSettingsFile = new String();
    private String todayTradeFile = new String();
    private String firstScreenshot = new String();
    
    // Name of selected log file for analyze
    private String selectedTwsLogFile = new String();
    private String selectedIbgLogFile = new String();
    private String selectedTwsSettingsFile = new String();
    private String selectedIbgSettingsFile = new String();
    private String selectedTradeFile = new String();
    private String selectedScreenshot = new String();
    
    // Name of manual selected log file for analyze
    private String manualSelectedLogFile = new String();
    private String manualSelectedSettingsFile = new String();
    
    private String zipLocation = null; // Location of stored uploaded diagnostic .zip file
    private String outputDirectory = null; // Location of the extracted folder
    
    // List of log files read from extracted folder
    private List<File> twsLogFileList = null;
    private List<File> ibgLogFileList = null;
    private List<File> twsSettingsFileList = null;
    private List<File> ibgSettingsFileList = null;
    private List<File> tradeFileList = null;
    private List<File> screenshotList = null;
    
    // Containers to store settings information
    private final MarketDataSettingsMessage mdSettingsMessage;
    private final ApiSettingsMessage apiSettingsMessage;
    private final EnvSettingsMessage envSettingsMessage;
    
    private boolean autoCls = true; // Auto clear display pane after extraction
    private boolean includeXml = true; // Include settings xml examination
    private boolean includeTrd = false; // Include .trd file exmanination
    
    public LogReader(){
        mdSettingsMessage = new MarketDataSettingsMessage();
        apiSettingsMessage = new ApiSettingsMessage();
        envSettingsMessage = new EnvSettingsMessage();
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
    
    public boolean checkValidZipLocation(){
        if(zipLocation == null)
            return false;
        if(zipLocation.endsWith(".zip")){
            return true;
        } else {
            return false;
        }
    }
    
    public void setAutoCls(boolean autoCls){
        this.autoCls = autoCls;
    }
    
    public void setIncludeXml(boolean includeXml){
        this.includeXml = includeXml;
    }
    
    public void setIncludeTrd(boolean includeTrd){
        this.includeTrd = includeTrd;
    }
    
    // Reset read list
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
    
    public void resetTradeFileList(){
        if(tradeFileList != null){
            tradeFileList.clear();
            tradeFileList = null;
        }        
    }
    
    public void resetScreenshotList(){
        if(screenshotList != null){
            screenshotList.clear();
            screenshotList = null;
        }
    }
    
    // Extract file at zipLocation to outputDirectory
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
    
    public String [] getTradeFileListNames() {
        if(tradeFileList == null){
            try {
                if(loadTradeFileList()){
                    String[] list = new String[tradeFileList.size()];
                    for(int i = 0; i < tradeFileList.size(); i++){
                        list[i] = tradeFileList.get(i).getName();
                    }
                    return list;
                } else
                    return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } else {
            String[] list = new String[tradeFileList.size()];
            for(int i = 0; i < tradeFileList.size(); i++){
                list[i] = tradeFileList.get(i).getName();
            }
            return list;
        }
    }
    
    public String [] getScreenshotListNames() {
        if(screenshotList == null){
            try {
                if(loadScreenshotList()){
                    String[] list = new String[screenshotList.size()];
                    for(int i = 0; i < screenshotList.size(); i++){
                        list[i] = screenshotList.get(i).getName();
                    }
                    return list;
                } else
                    return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } else {
            String[] list = new String[screenshotList.size()];
            for(int i = 0; i < screenshotList.size(); i++){
                list[i] = screenshotList.get(i).getName();
            }
            return list;
        }
    }
    
    // Find all log file names and store in list, returns true if loading is successful
    public boolean loadTwsLogFileList() throws Exception{
        if(outputDirectory == null){
            throw new Exception("Invalid Directory");
        }
        
        if(twsLogFileList != null && !twsLogFileList.isEmpty()){
            twsLogFileList.clear();
        }
        
        File dir = new File(outputDirectory);
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("tws.log", "tws.*.log", "log.*.txt"));
        twsLogFileList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(twsLogFileList == null || twsLogFileList.isEmpty()){
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
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("tws.*.xml"));
        twsSettingsFileList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(twsSettingsFileList == null || twsSettingsFileList.isEmpty()){
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
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("ibg.*.xml", "ibg.xml"));
        ibgSettingsFileList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(ibgSettingsFileList == null || ibgSettingsFileList.isEmpty()){
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
    
    public boolean loadTradeFileList() throws Exception{
        if(outputDirectory == null){
            throw new Exception("Invalid Directory");
        }
        
        if(tradeFileList != null && !tradeFileList.isEmpty()){
            tradeFileList.clear();
        }
        
        File dir = new File(outputDirectory);
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("*.trd"));
        tradeFileList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(tradeFileList == null || tradeFileList.isEmpty()){
            todayTradeFile = null;
            return false;
        }
        
        boolean hasToday = false;
        String dayLogName = new String(CurrentDay.findCurrentDay() + ".trd");
        
        for(File file : tradeFileList){
            if(file.getName().equals(dayLogName)){
                todayTradeFile = dayLogName;
                hasToday = true;
                break;
            }
        }
        if(hasToday == true){
            return true;
        } else {
            todayTradeFile = tradeFileList.get(0).getName();
            return true;
        }
        
    }
    
    public boolean loadScreenshotList() throws Exception{
        if(outputDirectory == null){
            throw new Exception("Invalid Directory");
        }
        
        if(screenshotList != null && !screenshotList.isEmpty()){
            screenshotList.clear();
        }
        
        File dir = new File(outputDirectory);
        IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("screenshot*.jpg"));
        screenshotList = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
        
        if(screenshotList == null || screenshotList.isEmpty()){
            firstScreenshot = null;
            return false;
        }
        
        firstScreenshot = screenshotList.get(0).getName();
        return true;        
    }
    
    public String getTodayTwsSettingsFileName(){
        return new String(todayTwsSettingsFile);
    }
    
    public String getTodayIbgSettingsFileName(){
        return new String(todayIbgSettingsFile);
    }
    
    public String getTodayTwsLogFileName(){
        return new String(todayTwsLogFile);
    }
    
    public String getTodayIbgLogFileName(){
        return new String(todayIbgLogFile);
    }
    
    public String getTodayTradeFileName(){
        return new String(todayTradeFile);
    }
    
    public String getFirstScreenshotName(){
        return new String(firstScreenshot);
    }
    
    // Select file for analysis
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
    
    public void selectTradeFile(String s){
        selectedTradeFile = s;
    }
    
    public void selectScreenshot(String s){
        selectedScreenshot = s;
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
    
    private File getSelectedTradeFile() throws Exception{
        for(File file: tradeFileList){
            if(file.getName().equals(this.selectedTradeFile)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedScreenshot() throws Exception{
        for(File file: screenshotList){
            if(file.getName().equals(this.selectedScreenshot)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedLogFileManual() throws Exception{
        if(manualSelectedLogFile == null){
            return null;
        }
        return new File(manualSelectedLogFile);
    }
    
    private File getSelectedSettingsFileManual() throws Exception{
        if(manualSelectedSettingsFile == null){
            return null;
        }
        return new File(manualSelectedSettingsFile);
    }
    
    public void parseSettingsFile(int choice, boolean isTws, boolean useManual, HashMap<Integer, javax.swing.JTextPane> textPaneList) throws Exception{
        if(includeXml == true){
            
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
            
            if(currentSettingsFile == null){
                return;
            }
            
            switch (choice) {
                case Choices.API:
                    SettingsLogParser.parseAPISettingsFile(currentSettingsFile, apiSettingsMessage, textPaneList.get(choice));
                    break;
                case Choices.MKTDATA:
                    SettingsLogParser.parseMDSettingsFile(currentSettingsFile, mdSettingsMessage, textPaneList.get(choice));
                    break;
                case Choices.ENV:
                    SettingsLogParser.parseEnvSettingsFile(currentSettingsFile, envSettingsMessage, textPaneList.get(choice));
                    break;
                default:
                    break;
            }
        } else {
            return;
        }
    }
    
    public void parseTradeFile(int choice, HashMap<Integer, javax.swing.JTextPane> textPaneList) throws Exception{
        if(includeTrd == true){
            File currentTradeFile = this.getSelectedTradeFile();
            
            if(currentTradeFile == null){
                return;
            }
            
            TradeFileParser.parseTradeFile(currentTradeFile, textPaneList.get(choice));
        } else {
            return;
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
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                parseSettingsFile(choice, isTws, useManual, textPaneList);
                TwsLogParserDeep.parseTwsEnvInfo(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.LOGINSEQ:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                TwsLogParserDeep.parseTwsLoginSeqInfo(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.SYSRES:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                TwsLogParserDeep.parseTwsSysRes(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.MKTDATA:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                parseSettingsFile(choice, isTws, useManual, textPaneList);
                TwsLogParserDeep.parseTwsMktData(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.CONN:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                TwsLogParserDeep.parseTwsConn(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.HTBP:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                TwsLogParserDeep.parseTwsHtbp(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.API:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                parseSettingsFile(choice, isTws, useManual, textPaneList);
                TwsLogParserDeep.parseTwsApi(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.ORDERSTRDS:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                parseTradeFile(choice, textPaneList);
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
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                parseSettingsFile(choice, isTws, useManual, textPaneList);
                TwsLogParserShallow.parseTwsEnvInfo(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.LOGINSEQ:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                TwsLogParserShallow.parseTwsLoginSeqInfo(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.SYSRES:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                TwsLogParserShallow.parseTwsSysRes(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.MKTDATA:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                parseSettingsFile(choice, isTws, useManual, textPaneList);
                TwsLogParserShallow.parseTwsMktData(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.CONN:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                TwsLogParserShallow.parseTwsConn(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.HTBP:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                TwsLogParserShallow.parseTwsHtbp(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.API:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                parseSettingsFile(choice, isTws, useManual, textPaneList);
                TwsLogParserShallow.parseTwsApi(currentLogFile, textPaneList.get(choice));
                break;
            case Choices.ORDERSTRDS:
                if(autoCls == true){
                    clearTextPane(textPaneList.get(choice));
                }
                parseTradeFile(choice, textPaneList);
                TwsLogParserShallow.parseTwsOrderTrds(currentLogFile, textPaneList.get(choice));
                break;
            default:
                break;
        }
    }
    
    public void regExSearch(boolean isTws, boolean useManual, String regEx, boolean isCaseSensitive, javax.swing.JTextPane textPane) throws Exception{
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
        
        RegExSearch.search(regEx, isCaseSensitive, currentLogFile, textPane);
    }
    
    private void clearTextPane(javax.swing.JTextPane textPane){
        textPane.setText(null);
    }
    
    public void openLogFileInNotePad(boolean isTws, boolean useManual) throws Exception{
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
        
        java.awt.Desktop.getDesktop().open(currentLogFile);
    }
    
    public void openScreenshots() throws Exception{        
        java.awt.Desktop.getDesktop().open(getSelectedScreenshot());
    }
}
