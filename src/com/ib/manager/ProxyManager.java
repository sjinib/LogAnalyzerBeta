/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.manager;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.prefs.*;
import org.apache.log4j.Logger;
import com.ib.config.*;


/**
 *
 * @credit to Ernie Parker
 */
public class ProxyManager {
    private final static Logger LOG = Logger.getLogger(ProxyManager.class);
    
    private static ProxyManager _instance = null;
    
    public final static String USECUSTOMPROXYPREF = "LOGANALYZER_USD_CUSTOM_PROXY";
    public final static String PROXYPREF = "LOGANALYZER_PROXY_HOST";
    public final static String PORTPREF = "LOGANALYZER_PROXY_PORT";
    
    private Preferences prefs;
    
    private ProxyManager(){
        prefs = Preferences.userNodeForPackage(this.getClass());
    }
    
    public static ProxyManager getInstance(){
        if(_instance == null){
            _instance = new ProxyManager();
        }
        return _instance;
    }
    
    public void setupProxyUsingConfig(){
        LOG.debug("Setting up proxy from Configs");
        
        String useSystemProxy = ConfigReader.getInstance().getConfig(Configs.USE_SYSTEM_PROXY);
        if(useSystemProxy.equalsIgnoreCase("NO")){
            String proxy = ConfigReader.getInstance().getConfig(Configs.PREFERRED_PROXY_HOST);
            String port = ConfigReader.getInstance().getConfig(Configs.PREFERRED_PROXY_PORT);
            
            System.setProperty("java.net.useSystemProxies","false");
            
            Properties systemProperties = System.getProperties();
            systemProperties.setProperty("http.proxyHost", proxy);
            systemProperties.setProperty("http.proxyPort", port);
            systemProperties.setProperty("https.proxyHost", proxy);
            systemProperties.setProperty("https.proxyPort", port);
            
            LOG.debug("Setup done with proxy host: " + proxy + ", port: " + port + " from Configs.");
        } else {
            System.setProperty("java.net.useSystemProxies","true");
            
            LOG.debug("Setup done with using system proxy from Configs");
        }
        
        setupAuthentication();
    }
    
    private void setupAuthentication(){
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("twserror", "twserr67".toCharArray());
            }
        });
    }
}
