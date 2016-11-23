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
    public final static int USESERVER = 1;
    public final static int USELOCAL = 2;
    // Name of selected log file from server download for analyze
    private String selectedTwsLogFile_server = new String();
    private String selectedIbgLogFile_server = new String();
    private String selectedTwsSettingsFileserver = new String();
    private String selectedIbgSettingsFileserver = new String();
    private String selectedTradeFileserver = new String();
    private String selectedScreenshotserver = new String();
    
    // Name of selected log file from import for analyze
    private String selectedTwsLogFile_local = new String();
    private String selectedIbgLogFile_local = new String();
    private String selectedTwsSettingsFile_local = new String();
    private String selectedIbgSettingsFile_local = new String();
    private String selectedTradeFile_local = new String();
    private String selectedScreenshot_local = new String();
    
    // Name of manual selected log file for analyze
    private String selectedLogFile_manual = new String();
    private String selectedSettingsFile_manual = new String();
    
    // String name of today's log file from server download
    private String todayTwsLogFile_server = new String();
    private String todayIbgLogFile_server= new String();
    private String todayTwsSettingsFile_server = new String();
    private String todayIbgSettingsFile_server = new String();
    private String todayTradeFile_server = new String();
    private String firstScreenshot_server = new String();
    
    // String name of today's log file from import
    private String todayTwsLogFile_local = new String();
    private String todayIbgLogFile_local = new String();
    private String todayTwsSettingsFile_local = new String();
    private String todayIbgSettingsFile_local = new String();
    private String todayTradeFile_local = new String();
    private String firstScreenshot_local = new String();
    
    // String name of file locations from server download
    private String zipLocation_server = null; // Location of stored uploaded diagnostic .zip file
    private String outputDirectory_server = null; // Location of the extracted folder
    
    // String name of file locations from import
    private String zipLocation_local = null; // Location of stored uploaded diagnostic .zip file
    private String outputDirectory_local = null; // Location of the extracted folder
    
    // List of log files read from extracted folder from server download
    private List<File> twsLogFileList_server = null;
    private List<File> ibgLogFileList_server = null;
    private List<File> twsSettingsFileList_server = null;
    private List<File> ibgSettingsFileList_server = null;
    private List<File> tradeFileList_server = null;
    private List<File> screenshotList_server = null;
    
    // List of log files read from extracted folder from import
    private List<File> twsLogFileList_local = null;
    private List<File> ibgLogFileList_local = null;
    private List<File> twsSettingsFileList_local = null;
    private List<File> ibgSettingsFileList_local = null;
    private List<File> tradeFileList_local = null;
    private List<File> screenshotList_local = null;
    
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
    
    public void setOutputDirectory(int method, String dir){
        if(dir != null){
            if(method == LogReader.USESERVER)
                outputDirectory_server = dir;
            else if(method == LogReader.USELOCAL)
                outputDirectory_local = dir;
        }
    }
    
    public void setZipLocation(int method, String dir){
        if(dir != null){
            if(method == LogReader.USESERVER)
                zipLocation_server = dir;
            else if(method == LogReader.USELOCAL)
                zipLocation_local = dir;
        }
    }
    
    public boolean checkValidZipLocation(int method){
        String currentZipLocation = null;
        if(method == LogReader.USESERVER)
            currentZipLocation = zipLocation_server;
        else if(method == LogReader.USELOCAL)
            currentZipLocation = zipLocation_local;
        
        if(currentZipLocation == null)
            return false;
        if(currentZipLocation.endsWith(".zip")){
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
    public void resetLogFileList(int method){
        if(method == LogReader.USESERVER){
            if(twsLogFileList_server != null){
                twsLogFileList_server.clear();
                twsLogFileList_server = null;
            }
            if(ibgLogFileList_server != null){
                ibgLogFileList_server.clear();
                ibgLogFileList_server = null;
            }
        } else if(method == LogReader.USELOCAL){
            if(twsLogFileList_local != null){
                twsLogFileList_local.clear();
                twsLogFileList_local = null;
            }
            if(ibgLogFileList_local != null){
                ibgLogFileList_local.clear();
                ibgLogFileList_local = null;
            }
        }
    }
    
    public void resetSettingsFileList(int method){
        if(method == LogReader.USESERVER){
            if(twsSettingsFileList_server != null){
                twsSettingsFileList_server.clear();
                twsSettingsFileList_server = null;
            }
            if(ibgSettingsFileList_server != null){
                ibgSettingsFileList_server.clear();
                ibgSettingsFileList_server = null;
            }
        } else if(method == LogReader.USELOCAL){
            if(twsSettingsFileList_local != null){
                twsSettingsFileList_local.clear();
                twsSettingsFileList_local = null;
            }
            if(ibgSettingsFileList_local != null){
                ibgSettingsFileList_local.clear();
                ibgSettingsFileList_local = null;
            }
        }
    }
    
    public void resetTradeFileList(int method){
        if(method == LogReader.USESERVER){
            if(tradeFileList_server != null){
                tradeFileList_server.clear();
                tradeFileList_server = null;
            }
        } else if(method == LogReader.USELOCAL){
            if(tradeFileList_local != null){
                tradeFileList_local.clear();
                tradeFileList_local = null;
            }
        }
    }
    
    public void resetScreenshotList(int method){
        if(method == LogReader.USESERVER){
            if(screenshotList_server != null){
                screenshotList_server.clear();
                screenshotList_server = null;
            }
        } else if(method == LogReader.USELOCAL){
            if(screenshotList_local != null){
                screenshotList_local.clear();
                screenshotList_local = null;
            }
        }
    }
    
    // Extract file at zipLocation_local to outputDirectory_local
    public void extractZip(int method) throws Exception{
        if(method == LogReader.USESERVER){
            if(zipLocation_server != null && outputDirectory_server != null){
                ExtractZip.unZipIt(zipLocation_server, outputDirectory_server);
            }
        } else if(method == LogReader.USELOCAL){
            if(zipLocation_local != null && outputDirectory_local != null){
                ExtractZip.unZipIt(zipLocation_local, outputDirectory_local);
            }
        }
    }
    
    // Return String array of names of tws log list for tws log selector
    public String [] getTwsLogFileListNames(int method) {
        if(method == LogReader.USESERVER){
            if(twsLogFileList_server == null){
                try {
                    if(loadTwsLogFileList(method)){
                        String[] list = new String[twsLogFileList_server.size()];
                        for(int i = 0; i < twsLogFileList_server.size(); i++){
                            list[i] = twsLogFileList_server.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[twsLogFileList_server.size()];
                for(int i = 0; i < twsLogFileList_server.size(); i++){
                    list[i] = twsLogFileList_server.get(i).getName();
                }
                return list;
            }
        } else if(method == LogReader.USELOCAL){
            if(twsLogFileList_local == null){
                try {
                    if(loadTwsLogFileList(method)){
                        String[] list = new String[twsLogFileList_local.size()];
                        for(int i = 0; i < twsLogFileList_local.size(); i++){
                            list[i] = twsLogFileList_local.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[twsLogFileList_local.size()];
                for(int i = 0; i < twsLogFileList_local.size(); i++){
                    list[i] = twsLogFileList_local.get(i).getName();
                }
                return list;
            }
        } else {
            return null;
        }
    }
    
    public String [] getIBGLogFilesListNames(int method) {
        if(method == LogReader.USESERVER){
            if(ibgLogFileList_server == null || ibgLogFileList_server.isEmpty()){
                try {
                    if(loadIBGLogFileList(method) == true){
                        String[] list = new String[ibgLogFileList_server.size()];
                        for(int i = 0; i < ibgLogFileList_server.size(); i++){
                            list[i] = ibgLogFileList_server.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[ibgLogFileList_server.size()];
                for(int i = 0; i < ibgLogFileList_server.size(); i++){
                    list[i] = ibgLogFileList_server.get(i).getName();
                }
                return list;
            }
        } else if(method == LogReader.USELOCAL){
            if(ibgLogFileList_local == null || ibgLogFileList_local.isEmpty()){
                try {
                    if(loadIBGLogFileList(method) == true){
                        String[] list = new String[ibgLogFileList_local.size()];
                        for(int i = 0; i < ibgLogFileList_local.size(); i++){
                            list[i] = ibgLogFileList_local.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[ibgLogFileList_local.size()];
                for(int i = 0; i < ibgLogFileList_local.size(); i++){
                    list[i] = ibgLogFileList_local.get(i).getName();
                }
                return list;
            }
        } else {
            return null;
        }
    }
    
    public String [] getTwsSettingsFilesListNames(int method) {
        if(method == LogReader.USESERVER){
            if(twsSettingsFileList_server == null){
                try {
                    if(loadTwsSettingsFileList(method)){
                        String[] list = new String[twsSettingsFileList_server.size()];
                        for(int i = 0; i < twsSettingsFileList_server.size(); i++){
                            list[i] = twsSettingsFileList_server.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[twsSettingsFileList_server.size()];
                for(int i = 0; i < twsSettingsFileList_server.size(); i++){
                    list[i] = twsSettingsFileList_server.get(i).getName();
                }
                return list;
            }
        } else if(method == LogReader.USELOCAL){
            if(twsSettingsFileList_local == null){
                try {
                    if(loadTwsSettingsFileList(method)){
                        String[] list = new String[twsSettingsFileList_local.size()];
                        for(int i = 0; i < twsSettingsFileList_local.size(); i++){
                            list[i] = twsSettingsFileList_local.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[twsSettingsFileList_local.size()];
                for(int i = 0; i < twsSettingsFileList_local.size(); i++){
                    list[i] = twsSettingsFileList_local.get(i).getName();
                }
                return list;
            }
        } else {
            return null;
        }
    }
    
    public String [] getIbgSettingsFilesListNames(int method) {
        if(method == LogReader.USESERVER){
            if(ibgSettingsFileList_server == null){
                try {
                    if(loadIbgSettingsFileList(method)){
                        String[] list = new String[ibgSettingsFileList_server.size()];
                        for(int i = 0; i < ibgSettingsFileList_server.size(); i++){
                            list[i] = ibgSettingsFileList_server.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[ibgSettingsFileList_server.size()];
                for(int i = 0; i < ibgSettingsFileList_server.size(); i++){
                    list[i] = ibgSettingsFileList_server.get(i).getName();
                }
                return list;
            }
        } else if(method == LogReader.USELOCAL){
            if(ibgSettingsFileList_local == null){
                try {
                    if(loadIbgSettingsFileList(method)){
                        String[] list = new String[ibgSettingsFileList_local.size()];
                        for(int i = 0; i < ibgSettingsFileList_local.size(); i++){
                            list[i] = ibgSettingsFileList_local.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[ibgSettingsFileList_local.size()];
                for(int i = 0; i < ibgSettingsFileList_local.size(); i++){
                    list[i] = ibgSettingsFileList_local.get(i).getName();
                }
                return list;
            }
        } else {
            return null;
        }
    }
    
    public String [] getTradeFileListNames(int method) {
        if(method == LogReader.USESERVER){
            if(tradeFileList_server == null){
                try {
                    if(loadTradeFileList(method)){
                        String[] list = new String[tradeFileList_server.size()];
                        for(int i = 0; i < tradeFileList_server.size(); i++){
                            list[i] = tradeFileList_server.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[tradeFileList_server.size()];
                for(int i = 0; i < tradeFileList_server.size(); i++){
                    list[i] = tradeFileList_server.get(i).getName();
                }
                return list;
            }
        } else if(method == LogReader.USELOCAL){
            if(tradeFileList_local == null){
                try {
                    if(loadTradeFileList(method)){
                        String[] list = new String[tradeFileList_local.size()];
                        for(int i = 0; i < tradeFileList_local.size(); i++){
                            list[i] = tradeFileList_local.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[tradeFileList_local.size()];
                for(int i = 0; i < tradeFileList_local.size(); i++){
                    list[i] = tradeFileList_local.get(i).getName();
                }
                return list;
            }
        } else {
            return null;
        }
    }
    
    public String [] getScreenshotListNames(int method) {
        if(method == LogReader.USESERVER){
            if(screenshotList_server == null){
                try {
                    if(loadScreenshotList(method)){
                        String[] list = new String[screenshotList_server.size()];
                        for(int i = 0; i < screenshotList_server.size(); i++){
                            list[i] = screenshotList_server.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[screenshotList_server.size()];
                for(int i = 0; i < screenshotList_server.size(); i++){
                    list[i] = screenshotList_server.get(i).getName();
                }
                return list;
            }
        } else if(method == LogReader.USELOCAL){
            if(screenshotList_local == null){
                try {
                    if(loadScreenshotList(method)){
                        String[] list = new String[screenshotList_local.size()];
                        for(int i = 0; i < screenshotList_local.size(); i++){
                            list[i] = screenshotList_local.get(i).getName();
                        }
                        return list;
                    } else
                        return null;
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                String[] list = new String[screenshotList_local.size()];
                for(int i = 0; i < screenshotList_local.size(); i++){
                    list[i] = screenshotList_local.get(i).getName();
                }
                return list;
            }
        } else {
            return null;
        }
    }
    
    // Find all log file names and store in list, returns true if loading is successful
    public boolean loadTwsLogFileList(int method) throws Exception{
        if(method == LogReader.USESERVER){
            if(outputDirectory_server == null){
                throw new Exception("Invalid Directory");
            }
            
            if(twsLogFileList_server != null && !twsLogFileList_server.isEmpty()){
                twsLogFileList_server.clear();
            }
            
            File dir = new File(outputDirectory_server);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("tws.log", "tws.*.log", "log.*.txt"));
            twsLogFileList_server = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(twsLogFileList_server == null || twsLogFileList_server.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(null, "No TWS log file is found.");
                todayTwsLogFile_server = null;
                return false;
            }
            
            boolean hasToday = false;
            String dayLogName = new String("log." + CurrentDay.findCurrentDay() + ".txt");
            for(File file : twsLogFileList_server){
                if(file.getName().equals("tws.log")){
                    todayTwsLogFile_server = "tws.log";
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            } else {
                for(File file : twsLogFileList_server){
                    if(file.getName().equals(dayLogName)){
                        todayTwsLogFile_server = dayLogName;
                        hasToday = true;
                        break;
                    }
                }
                if(hasToday == true){
                    return true;
                } else {
                    todayTwsLogFile_server = twsLogFileList_server.get(0).getName();
                    return true;
                }
            }
        } else if(method == LogReader.USELOCAL){
            if(outputDirectory_local == null){
                throw new Exception("Invalid Directory");
            }
            
            if(twsLogFileList_local != null && !twsLogFileList_local.isEmpty()){
                twsLogFileList_local.clear();
            }
            
            File dir = new File(outputDirectory_local);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("tws.log", "tws.*.log", "log.*.txt"));
            twsLogFileList_local = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(twsLogFileList_local == null || twsLogFileList_local.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(null, "No TWS log file is found.");
                todayTwsLogFile_local = null;
                return false;
            }
            
            boolean hasToday = false;
            String dayLogName = new String("log." + CurrentDay.findCurrentDay() + ".txt");
            for(File file : twsLogFileList_local){
                if(file.getName().equals("tws.log")){
                    todayTwsLogFile_local = "tws.log";
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            } else {
                for(File file : twsLogFileList_local){
                    if(file.getName().equals(dayLogName)){
                        todayTwsLogFile_local = dayLogName;
                        hasToday = true;
                        break;
                    }
                }
                if(hasToday == true){
                    return true;
                } else {
                    todayTwsLogFile_local = twsLogFileList_local.get(0).getName();
                    return true;
                }
            }
        } else {
            return false;
        }
    }
    
    public boolean loadIBGLogFileList(int method) throws Exception{
        if(method == LogReader.USESERVER){
            if(outputDirectory_server == null){
                throw new Exception("Invalid Directory");
            }
            
            if(ibgLogFileList_server != null && !ibgLogFileList_server.isEmpty()){
                ibgLogFileList_server.clear();
            }
            
            File dir = new File(outputDirectory_server);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("ibgateway.log", "ibgateway.*.log"));
            ibgLogFileList_server = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(ibgLogFileList_server == null || ibgLogFileList_server.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(null, "No IB Gateway log file is found.");
                todayIbgLogFile_server = null;
                return false;
            }
            
            boolean hasToday = false;
            String dayLogName = new String("ibgateway." + CurrentDay.findCurrentDay() + ".log");
            for(File file : ibgLogFileList_server){
                if(file.getName().equals("ibgateway.log")){
                    todayIbgLogFile_server = "ibgateway.log";
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            } else {
                for(File file : ibgLogFileList_server){
                    if(file.getName().equals(dayLogName)){
                        todayIbgLogFile_server = dayLogName;
                        hasToday = true;
                        break;
                    }
                }
                if(hasToday == true){
                    return true;
                } else {
                    todayIbgLogFile_server = ibgLogFileList_server.get(0).getName();
                    return true;
                }
            }
        } else if(method == LogReader.USELOCAL){
            if(outputDirectory_local == null){
                throw new Exception("Invalid Directory");
            }
            
            if(ibgLogFileList_local != null && !ibgLogFileList_local.isEmpty()){
                ibgLogFileList_local.clear();
            }
            
            File dir = new File(outputDirectory_local);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("ibgateway.log", "ibgateway.*.log"));
            ibgLogFileList_local = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(ibgLogFileList_local == null || ibgLogFileList_local.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(null, "No IB Gateway log file is found.");
                todayIbgLogFile_local = null;
                return false;
            }
            
            boolean hasToday = false;
            String dayLogName = new String("ibgateway." + CurrentDay.findCurrentDay() + ".log");
            for(File file : ibgLogFileList_local){
                if(file.getName().equals("ibgateway.log")){
                    todayIbgLogFile_local = "ibgateway.log";
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            } else {
                for(File file : ibgLogFileList_local){
                    if(file.getName().equals(dayLogName)){
                        todayIbgLogFile_local = dayLogName;
                        hasToday = true;
                        break;
                    }
                }
                if(hasToday == true){
                    return true;
                } else {
                    todayIbgLogFile_local = ibgLogFileList_local.get(0).getName();
                    return true;
                }
            }
        } else {
            return false;
        }
    }
    
    public boolean loadTwsSettingsFileList(int method) throws Exception{
        if(method == LogReader.USESERVER){
            if(outputDirectory_server == null){
                throw new Exception("Invalid Directory");
            }
            
            if(twsSettingsFileList_server != null && !twsSettingsFileList_server.isEmpty()){
                twsSettingsFileList_server.clear();
            }
            
            File dir = new File(outputDirectory_server);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("tws.*.xml"));
            twsSettingsFileList_server = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(twsSettingsFileList_server == null || twsSettingsFileList_server.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(null, "No TWS settings file is found.");
                todayTwsSettingsFile_server = null;
                return false;
            }
            
            boolean hasToday = false;
            for(File file : twsSettingsFileList_server){
                if(file.getName().equals("tws.error.xml")){
                    todayTwsSettingsFile_server = "tws.error.xml";
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            }else {
                todayTwsSettingsFile_server = twsSettingsFileList_server.get(0).getName();
                return true;
            }
        } else if(method == LogReader.USELOCAL){
            if(outputDirectory_local == null){
                throw new Exception("Invalid Directory");
            }
            
            if(twsSettingsFileList_local != null && !twsSettingsFileList_local.isEmpty()){
                twsSettingsFileList_local.clear();
            }
            
            File dir = new File(outputDirectory_local);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("tws.*.xml"));
            twsSettingsFileList_local = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(twsSettingsFileList_local == null || twsSettingsFileList_local.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(null, "No TWS settings file is found.");
                todayTwsSettingsFile_local = null;
                return false;
            }
            
            boolean hasToday = false;
            for(File file : twsSettingsFileList_local){
                if(file.getName().equals("tws.error.xml")){
                    todayTwsSettingsFile_local = "tws.error.xml";
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            }else {
                todayTwsSettingsFile_local = twsSettingsFileList_local.get(0).getName();
                return true;
            }
        } else {
            return false;
        }
    }
    
    public boolean loadIbgSettingsFileList(int method) throws Exception{
        if(method == LogReader.USESERVER){
            if(outputDirectory_server == null){
                throw new Exception("Invalid Directory");
            }
            
            if(ibgSettingsFileList_server != null && !ibgSettingsFileList_server.isEmpty()){
                ibgSettingsFileList_server.clear();
            }
            
            File dir = new File(outputDirectory_server);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("ibg.*.xml", "ibg.xml"));
            ibgSettingsFileList_server = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(ibgSettingsFileList_server == null || ibgSettingsFileList_server.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(null, "No IB Gateway settings file is found.");
                todayIbgSettingsFile_server = null;
                return false;
            }
            
            boolean hasToday = false;
            for(File file : ibgSettingsFileList_server){
                if(file.getName().equals("ibg.xml")){
                    todayIbgSettingsFile_server = "ibg.xml";
                    hasToday = true;
                }
            }
            if(hasToday == true){
                return true;
            }else {
                todayIbgSettingsFile_server = ibgSettingsFileList_server.get(0).getName();
                return true;
            }
        } else if(method == LogReader.USELOCAL){
            if(outputDirectory_local == null){
                throw new Exception("Invalid Directory");
            }
            
            if(ibgSettingsFileList_local != null && !ibgSettingsFileList_local.isEmpty()){
                ibgSettingsFileList_local.clear();
            }
            
            File dir = new File(outputDirectory_local);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("ibg.*.xml", "ibg.xml"));
            ibgSettingsFileList_local = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(ibgSettingsFileList_local == null || ibgSettingsFileList_local.isEmpty()){
                javax.swing.JOptionPane.showMessageDialog(null, "No IB Gateway settings file is found.");
                todayIbgSettingsFile_local = null;
                return false;
            }
            
            boolean hasToday = false;
            for(File file : ibgSettingsFileList_local){
                if(file.getName().equals("ibg.xml")){
                    todayIbgSettingsFile_local = "ibg.xml";
                    hasToday = true;
                }
            }
            if(hasToday == true){
                return true;
            }else {
                todayIbgSettingsFile_local = ibgSettingsFileList_local.get(0).getName();
                return true;
            }
        } else {
            return false;
        }
    }
    
    public boolean loadTradeFileList(int method) throws Exception{
        if(method == LogReader.USESERVER){
            if(outputDirectory_server == null){
                throw new Exception("Invalid Directory");
            }
            
            if(tradeFileList_server != null && !tradeFileList_server.isEmpty()){
                tradeFileList_server.clear();
            }
            
            File dir = new File(outputDirectory_server);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("*.trd"));
            tradeFileList_server = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(tradeFileList_server == null || tradeFileList_server.isEmpty()){
                todayTradeFile_server = null;
                return false;
            }
            
            boolean hasToday = false;
            String dayLogName = new String(CurrentDay.findCurrentDay() + ".trd");
            
            for(File file : tradeFileList_server){
                if(file.getName().equals(dayLogName)){
                    todayTradeFile_server = dayLogName;
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            } else {
                todayTradeFile_server = tradeFileList_server.get(0).getName();
                return true;
            }
        } else if(method == LogReader.USELOCAL){
            if(outputDirectory_local == null){
                throw new Exception("Invalid Directory");
            }
            
            if(tradeFileList_local != null && !tradeFileList_local.isEmpty()){
                tradeFileList_local.clear();
            }
            
            File dir = new File(outputDirectory_local);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("*.trd"));
            tradeFileList_local = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(tradeFileList_local == null || tradeFileList_local.isEmpty()){
                todayTradeFile_local = null;
                return false;
            }
            
            boolean hasToday = false;
            String dayLogName = new String(CurrentDay.findCurrentDay() + ".trd");
            
            for(File file : tradeFileList_local){
                if(file.getName().equals(dayLogName)){
                    todayTradeFile_local = dayLogName;
                    hasToday = true;
                    break;
                }
            }
            if(hasToday == true){
                return true;
            } else {
                todayTradeFile_local = tradeFileList_local.get(0).getName();
                return true;
            }
        } else {
            return false;
        }
    }
    
    public boolean loadScreenshotList(int method) throws Exception{
        if(method == LogReader.USESERVER){
            if(outputDirectory_server == null){
                throw new Exception("Invalid Directory");
            }
            
            if(screenshotList_server != null && !screenshotList_server.isEmpty()){
                screenshotList_server.clear();
            }
            
            File dir = new File(outputDirectory_server);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("screenshot*.jpg"));
            screenshotList_server = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(screenshotList_server == null || screenshotList_server.isEmpty()){
                firstScreenshot_server = null;
                return false;
            }
            
            firstScreenshot_server = screenshotList_server.get(0).getName();
            return true;
        } else if(method == LogReader.USELOCAL){
            if(outputDirectory_local == null){
                throw new Exception("Invalid Directory");
            }
            
            if(screenshotList_local != null && !screenshotList_local.isEmpty()){
                screenshotList_local.clear();
            }
            
            File dir = new File(outputDirectory_local);
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("screenshot*.jpg"));
            screenshotList_local = (List<File>) FileUtils.listFiles(dir, fileFilter, null);
            
            if(screenshotList_local == null || screenshotList_local.isEmpty()){
                firstScreenshot_local = null;
                return false;
            }
            
            firstScreenshot_local = screenshotList_local.get(0).getName();
            return true;
        } else {
            return false;
        }
    }
    
    public String getTodayTwsSettingsFileName(){
        if(todayTwsSettingsFile_local == null)
            return null;
        return new String(todayTwsSettingsFile_local);
    }
    
    public String getTodayIbgSettingsFileName(){
        if(todayIbgSettingsFile_local == null)
            return null;
        return new String(todayIbgSettingsFile_local);
    }
    
    public String getTodayTwsLogFileName(){
        if(todayTwsLogFile_local == null)
            return null;
        return new String(todayTwsLogFile_local);
    }
    
    public String getTodayIbgLogFileName(){
        if(todayIbgLogFile_local == null)
            return null;
        return new String(todayIbgLogFile_local);
    }
    
    public String getTodayTradeFileName(){
        if(todayTradeFile_local == null)
            return null;
        return new String(todayTradeFile_local);
    }
    
    public String getFirstScreenshotName(){
        if(firstScreenshot_local == null)
            return null;
        return new String(firstScreenshot_local);
    }
    
    // Select file for analysis
    public void selectTwsLogFile(String s){
        selectedTwsLogFile_local = s;
    }
    
    public void selectIbgLogFile(String s){
        selectedIbgLogFile_local = s;
    }
    
    public void selectTwsSettingsLogFile(String s){
        selectedTwsSettingsFile_local = s;
    }
    
    public void selectIbgSettingsLogFile(String s){
        selectedIbgSettingsFile_local = s;
    }
    
    public void selectTradeFile(String s){
        selectedTradeFile_local = s;
    }
    
    public void selectScreenshot(String s){
        selectedScreenshot_local = s;
    }
    
    public void selectLogFileManual(String s){
        selectedLogFile_manual = s;
    }
    
    public void selectSettingsFileManual(String s){
        selectedSettingsFile_manual = s;
    }
    
    private File getSelectedTwsSettingsFile(){
        for(File file: twsSettingsFileList_local){
            if(file.getName().equals(this.selectedTwsSettingsFile_local)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedIbgSettingsFile(){
        for(File file: ibgSettingsFileList_local){
            if(file.getName().equals(this.selectedIbgSettingsFile_local)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedTwsLogFile() throws Exception{
        for(File file: twsLogFileList_local){
            if(file.getName().equals(this.selectedTwsLogFile_local)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedIbgLogFile() throws Exception{
        for(File file: ibgLogFileList_local){
            if(file.getName().equals(this.selectedIbgLogFile_local)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedTradeFile() throws Exception{
        for(File file: tradeFileList_local){
            if(file.getName().equals(this.selectedTradeFile_local)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedScreenshot() throws Exception{
        for(File file: screenshotList_local){
            if(file.getName().equals(this.selectedScreenshot_local)){
                return new File(file.getPath());
            }
        }
        return null;
    }
    
    private File getSelectedLogFileManual() throws Exception{
        if(selectedLogFile_manual == null){
            return null;
        }
        return new File(selectedLogFile_manual);
    }
    
    private File getSelectedSettingsFileManual() throws Exception{
        if(selectedSettingsFile_manual == null){
            return null;
        }
        return new File(selectedSettingsFile_manual);
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
