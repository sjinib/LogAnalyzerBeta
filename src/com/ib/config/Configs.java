/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.config;

import java.util.HashSet;

/**
 *
 * @author Siteng Jin
 */
public class Configs {
    public static final String USE_SYSTEM_PROXY = "USE_SYSTEM_PROXY";
    public static final String PREFERRED_PROXY_HOST = "PREFERRED_PROXY_HOST";
    public static final String PREFERRED_PROXY_PORT = "PREFERRED_PROXY_PORT";
    public static final String TODAY_ONLY = "TODAY_ONLY";
    public static final String DEFAULT_DOWNLOAD_DIRECTORY = "DEFAULT_DOWNLOAD_DIRECTORY";
    public static final String DEFAULT_EXTRACT_DIRECTORY = "DEFAULT_EXTRACT_DIRECTORY";
    public static final String AUTO_CLEAR = "AUTO_CLEAR";
    public static final String INCLUDE_XML_IN_ANALYSIS = "INCLUDE_XML_IN_ANALYSIS";
    public static final String INCLUDE_TRD_IN_ANALYSIS = "INCLUDE_TRD_IN_ANALYSIS";
    public static final String MATCH_CASE_IN_REG_SEARCH = "MATCH_CASE_IN_REG_SEARCH";
    
    
    public static HashSet<String> getAllConfigs(){
        HashSet<String> configs = new HashSet<String>();
        configs.add(USE_SYSTEM_PROXY);
        configs.add(PREFERRED_PROXY_HOST);
        configs.add(PREFERRED_PROXY_PORT);
        configs.add(TODAY_ONLY);
        configs.add(DEFAULT_DOWNLOAD_DIRECTORY);
        configs.add(DEFAULT_EXTRACT_DIRECTORY);
        configs.add(AUTO_CLEAR);
        configs.add(INCLUDE_XML_IN_ANALYSIS);
        configs.add(INCLUDE_TRD_IN_ANALYSIS);
        configs.add(MATCH_CASE_IN_REG_SEARCH);
        return configs;
    }
}
