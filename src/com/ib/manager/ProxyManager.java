/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.manager;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Date;
import java.util.Properties;
import java.util.prefs.*;
import java.util.logging.Logger;


/**
 *
 * @credit to Ernie Parker
 */
public class ProxyManager {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public final static String USECUSTOMPROXYPREF = "LOGANALYZER_USD_CUSTOM_PROXY";
    public final static String PROXYPREF = "LOGANALYZER_PROXY_HOST";
    public final static String PORTPREF = "LOGANALYZER_PROXY_PORT";
    
    private Preferences prefs;
    
    public ProxyManager(){
        prefs = Preferences.userNodeForPackage(this.getClass());
    }
    
    public void setupProxyPreference(boolean useCustomProxy, String proxyHost, String proxyPort){
        prefs.put("LOGANALYZER_USD_CUSTOM_PROXY", Boolean.toString(useCustomProxy).toUpperCase());
        prefs.put("LOGANALYZER_PROXY_HOST", proxyHost);
        prefs.put("LOGANALYZER_PROXY_PORT", proxyPort);
    }
    
    public void setupProxyManual(String proxy, String port){
        LOGGER.info((new Date()).toString() + " - " + "Manually settings proxy host: " + proxy + ", port: " + port);
        
        System.setProperty("java.net.useSystemProxies","false");
        
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("http.proxyHost", proxy);
        systemProperties.setProperty("http.proxyPort", port);
        systemProperties.setProperty("https.proxyHost", proxy);
        systemProperties.setProperty("https.proxyPort", port);
        
        LOGGER.info((new Date()).toString() + " - " + "Setup done with proxy host: " + proxy + ", port: " + port + " from manual input.");
        /*
        LOGGER.info((new Date()).toString() + " - " + "Http.ProxyHost: " + systemProperties.getProperty("http.proxyHost") +
        ", Http.ProxyPort: " + systemProperties.getProperty("http.proxyPort") +
        ", Https.ProxyHost: " + systemProperties.getProperty("https.proxyHost") +
        ", Https.ProxyPort: " + systemProperties.getProperty("https.proxyPort"));
        */        

        setupAuthentication();
    }
    
    public void setupProxyUsingPref(){
        LOGGER.info((new Date()).toString() + " - " + "Setting up proxy from Prefeference");
        
        String[] proxyPrefs = getProxyPref();
        String useCustomProxyPref = proxyPrefs[0];
        if(useCustomProxyPref.equalsIgnoreCase("TRUE")){
            String proxy = proxyPrefs[1];
            String port = proxyPrefs[2];
            
            System.setProperty("java.net.useSystemProxies","false");
            
            Properties systemProperties = System.getProperties();
            systemProperties.setProperty("http.proxyHost", proxy);
            systemProperties.setProperty("http.proxyPort", port);
            systemProperties.setProperty("https.proxyHost", proxy);
            systemProperties.setProperty("https.proxyPort", port);
            
            LOGGER.info((new Date()).toString() + " - " + "Setup done with proxy host: " + proxy + ", port: " + port + " from Preference.");
        } else {
            System.setProperty("java.net.useSystemProxies","true");
            
            LOGGER.info((new Date()).toString() + " - " + "Setup done with use system proxy from Preference");
        }
        
        setupAuthentication();
    }
    
    public String[] getProxyPref(){
        String useCustomProxyPref = prefs.get(USECUSTOMPROXYPREF, "FALSE");
        String proxy = prefs.get(PROXYPREF, "");
        String port = prefs.get(PORTPREF, "");
        String[] result = {useCustomProxyPref, proxy, port};
        return result;
    }
    
    private void setupAuthentication(){
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("twserror", "twserr67".toCharArray());
            }
        });
    }
}
