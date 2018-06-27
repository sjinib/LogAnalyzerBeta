/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.config;

import java.util.Properties;
import java.util.HashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import org.apache.log4j.Logger;

/**
 *
 * @author Siteng Jin
 */
public class ConfigWriter {
    private static final String FILENAME = "resources/config.properties";    
    private static final Logger LOG = Logger.getLogger(ConfigReader.class);
    
    protected static ConfigWriter _instance = null;
    
    public static ConfigWriter getInstance(){
        if(_instance == null){
            _instance = new ConfigWriter();
        }
        return _instance;
    }
    
    public void createDefaultConfigFile(){
        try{
            Properties props = getDefaultConfigs();
            File configFile = new File(FILENAME);
            FileOutputStream fos = new FileOutputStream(configFile);
            props.store(fos, "");
            fos.close();
            LOG.info("Created new config file.");
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
    }
    
    private void overwriteCurrentConfigFile(HashMap<String, String> configs){
        try{
            Properties props = new Properties();
            for(String key : configs.keySet()){
                props.setProperty(key, configs.get(key));
            }
            File configFile = new File(FILENAME);
            FileOutputStream fos = new FileOutputStream(configFile);
            props.store(fos, "");
            fos.close();
            LOG.debug("Overwriten config file with: " + configs);
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
    }
    
    private Properties getDefaultConfigs(){
        Properties props = new Properties();
        props.setProperty(Configs.USE_SYSTEM_PROXY, "YES");
        props.setProperty(Configs.PREFERRED_PROXY_HOST, "");
        props.setProperty(Configs.PREFERRED_PROXY_PORT, "");
        props.setProperty(Configs.TODAY_ONLY, "YES");
        props.setProperty(Configs.DEFAULT_DOWNLOAD_DIRECTORY, System.getProperty("java.io.tmpdir"));
        props.setProperty(Configs.DEFAULT_EXTRACT_DIRECTORY, System.getProperty("java.io.tmpdir"));
        props.setProperty(Configs.AUTO_CLEAR, "YES");
        props.setProperty(Configs.INCLUDE_XML_IN_ANALYSIS, "YES");
        props.setProperty(Configs.INCLUDE_TRD_IN_ANALYSIS, "NO");
        props.setProperty(Configs.MATCH_CASE_IN_REG_SEARCH, "YES");
        
        props.setProperty(Configs.USERNAME, "");
        props.setProperty(Configs.PASSWORD, "");
        
        return props;
    }
    
    public HashMap<String, String> createDefaultConfigsMap(){
        Properties props = this.getDefaultConfigs();
        HashMap<String, String> configs = new HashMap<String, String>();
        Enumeration enuKeys = props.keys();
        while(enuKeys.hasMoreElements()){
            String key = (String) enuKeys.nextElement();
            String value = props.getProperty(key);
            if(configs.containsKey(key)){
                configs.replace(key, value);
            } else {
                configs.put(key, value);
            }
        }
        return configs;
    }
    
    public void overwriteConfig(String config, String value){
        HashMap<String, String> configs = ConfigReader.getInstance().getConfigMap();
        if(Configs.getAllConfigs().contains(config)){
            configs.replace(config, value);
        }
        this.overwriteCurrentConfigFile(configs);
    }
}
