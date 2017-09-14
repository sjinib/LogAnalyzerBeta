/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.config;

import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;
import java.util.HashMap;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import com.ib.analyzerGui.AnalyzerGUI;
import com.ib.manager.ProxyManager;

/**
 *
 * @author Siteng Jin
 */
public class ConfigReader {
    private static final String FILENAME = "resources/config.properties";    
    private static final Logger LOG = Logger.getLogger(ConfigReader.class);
    
    protected static ConfigReader _instance = null;
    
    private HashMap<String, String> m_config = null;
    private AnalyzerGUI m_analyzerGUI = null;
    
    private ConfigReader(){ }
    
    public void readProperties(){
        try{
            File file = new File(FILENAME);
            LOG.debug("Config File path: " + file.getAbsolutePath());
            if(file.exists() && !file.isDirectory()){
                LOG.debug("Config file found. Reading configs...");
                Properties m_prop = new Properties();
                FileInputStream fileInput = new FileInputStream(file);
                m_prop = new Properties();
                m_prop.load(fileInput);
                fileInput.close();
                
                if(m_config == null){
                    m_config = new HashMap<String, String>();
                }
                
                Enumeration enuKeys = m_prop.keys();
                while(enuKeys.hasMoreElements()){
                    String key = (String) enuKeys.nextElement();
                    String value = m_prop.getProperty(key);
                    if(m_config.containsKey(key)){
                        m_config.replace(key, value);
                    } else {
                        m_config.put(key, value);
                    }
                }
                LOG.debug("Successfully loaded config: " + m_config.toString());
            } else {
                LOG.debug("Config file not found. Creating new configs...");
                ConfigWriter.getInstance().createDefaultConfigFile();
                m_config = ConfigWriter.getInstance().createDefaultConfigsMap();
                LOG.debug("Successfully created new config: " + m_config.toString());
            }
        } catch (IOException ex){
            LOG.error(ex.getMessage(), ex);
        }
    }
    
    public void assignProperties(){
        if(m_config == null){
            readProperties();
        }
        
        if(m_analyzerGUI == null){
            m_analyzerGUI = AnalyzerGUI.getInstance();
        }
        
        // Proxy configs
        ProxyManager.getInstance().setupProxyUsingConfig();
        
        // Today only
        String todayOnly = m_config.get(Configs.TODAY_ONLY);
        m_analyzerGUI.todayOnlyCheckBox.setSelected(todayOnly.equalsIgnoreCase("YES"));
        
        // Directories
        m_analyzerGUI.loadDirectory1.setText(m_config.get(Configs.DEFAULT_DOWNLOAD_DIRECTORY));
        m_analyzerGUI.extractDirectoryServer.setText(m_config.get(Configs.DEFAULT_EXTRACT_DIRECTORY));
        
        // Filter settings
        String autoClear = m_config.get(Configs.AUTO_CLEAR);
        String includeXmlInAnalysis = m_config.get(Configs.INCLUDE_XML_IN_ANALYSIS);
        String includeTrdInAnalysis = m_config.get(Configs.INCLUDE_TRD_IN_ANALYSIS);
        String matchCaseInRegSearch = m_config.get(Configs.MATCH_CASE_IN_REG_SEARCH);
        
        m_analyzerGUI.autoClsCheck.setSelected(autoClear.equalsIgnoreCase("YES"));
        m_analyzerGUI.includeXmlCheck.setSelected(includeXmlInAnalysis.equalsIgnoreCase("YES"));
        m_analyzerGUI.includeTrdFileCheck.setSelected(includeTrdInAnalysis.equalsIgnoreCase("YES"));
        m_analyzerGUI.matchCaseCheck.setSelected(matchCaseInRegSearch.equalsIgnoreCase("YES"));
    }
    
    public String getConfig(String key){
        // null is returned if no key is found
        if(m_config == null){
            readProperties();
        }
        return m_config.get(key);
    }
    
    public void setConfig(String key, String value){
        if(m_config.containsKey(key)){
            m_config.replace(key, value);
        }
    }
    
    public HashMap<String, String> getConfigMap(){
        if(m_config == null){
            this.readProperties();
        }
        return m_config;
    }
    
    public void setConfigMap(HashMap<String, String> map){
        m_config = map;
    }
    
    public static ConfigReader getInstance(){
        if(_instance == null){
            _instance = new ConfigReader();
        }
        return _instance;
    }
}
