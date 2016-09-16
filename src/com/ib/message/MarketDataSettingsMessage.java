package com.ib.message;

import java.util.HashMap;

public class MarketDataSettingsMessage extends SettingsMessage {
    private HashMap<String, String> marketDataSettingsList;
    private HashMap<String, String> eSignalSettingsList;
    // Samples: {intl={hist=false, depth=true, top=true}, usa={hist=false,
    // depth=true, top=true}}
    private HashMap<String, HashMap<String, String>> eSignalSecSettingsListOpt;
    private HashMap<String, HashMap<String, String>> eSignalSecSettingsListStk;
    private HashMap<String, HashMap<String, String>> eSignalSecSettingsListFut;
    private HashMap<String, HashMap<String, String>> eSignalSecSettingsListInd;
    private HashMap<String, String> smartRoutingSettingsList;
    
    public MarketDataSettingsMessage() {
    }
    
    public static final String[] MARKETDATASETTINGSTEXT = {
        "Show last known quotes for indexes only: ", "Show last known quotes, excluding indexes: ", 
        "Enable Native Indexes Prices : ", "Prefer Native Indexes Prices: ", "Fixed Income Pricing - Show Net Price: "
    };
    
    public static final String[] MARKETDATASETTINGSTAGS = {
        "showFrozenMarketDataForIndex", "showFrozenMarketData",
        "showIndexCapability", "showNativeTop",	"showBondNetPrice" };
    
    public static final String[] ESIGNALSETTINGSTEXT = { "Use eSignal for Market Data: ", 
    "Username: ", "Password: ", "Host Address: ", "Non-paced updating for API: " };
    
    public static final String[] ESIGNALSETTINGSTAGS = { "allowed", "username",
        "password", "host", "apiUnpacedUpdates" };
    
    public static final String[] ESIGNALEXCHANGESETTINGSTAGS = { "varName",
        "top", "depth", "hist" };
    
    public static final String[] SMARTROUTINGSETTINGSTEXT = {"Pre-open re-route: ", 
    "Seek price improvement: ", "Do not route to dark pools: ", "Exclude exchanges with NON-firm quotes: ", 
    "Max distance from NBBO (#=Non defined): "};
    
    public static final String[] SMARTROUTINGSETTINGSTAGS = { "preOpenReroute",
        "favorPriceImprovements", "eTradeOnly", "firmQuoteOnly",
        "nbboPriceCap" };
    
    public void addMdAttrToMDSettingsList(String key, String value) {
        if (marketDataSettingsList == null) {
            marketDataSettingsList = new HashMap<String, String>();
        }
        marketDataSettingsList.put(key, value);
    }
    
    public HashMap<String, String> getCopyMdSettingsList() {
        if(marketDataSettingsList != null)
            return new HashMap<String, String>(marketDataSettingsList);
        return null;
    }
    
    public void addAttrToEsignalSettingsList(String key, String value) {
        if (eSignalSettingsList == null) {
            eSignalSettingsList = new HashMap<String, String>();
        }
        eSignalSettingsList.put(key, value);
    }
    
    public HashMap<String, String> getCopyEsignalSettingsList() {
        if(eSignalSettingsList != null)
            return new HashMap<String, String>(eSignalSettingsList);
        return null;
    }
    
    public void addAttrToEsignalSecSettingsListOpt(String key,
            HashMap<String, String> value) {
        if (eSignalSecSettingsListOpt == null) {
            eSignalSecSettingsListOpt = new HashMap<String, HashMap<String, String>>();
        }
        eSignalSecSettingsListOpt.put(key, value);
    }
    
    public HashMap<String, HashMap<String, String>> getCopyEsignalSecSettingsListOpt() {
        if(eSignalSecSettingsListOpt != null)
            return new HashMap<String, HashMap<String, String>>(eSignalSecSettingsListOpt);
        return null;
    }
    
    public void addAttrToEsignalSecSettingsListStk(String key,
            HashMap<String, String> value) {
        if (eSignalSecSettingsListStk == null) {
            eSignalSecSettingsListStk = new HashMap<String, HashMap<String, String>>();
        }
        eSignalSecSettingsListStk.put(key, value);
    }
    
    public HashMap<String, HashMap<String, String>> getCopyEsignalSecSettingsListStk() {
        if(eSignalSecSettingsListStk != null)
            return new HashMap<String, HashMap<String, String>>(eSignalSecSettingsListStk);
        return null;
    }
    
    public void addAttrToEsignalSecSettingsListFut(String key,
            HashMap<String, String> value) {
        if (eSignalSecSettingsListFut == null) {
            eSignalSecSettingsListFut = new HashMap<String, HashMap<String, String>>();
        }
        eSignalSecSettingsListFut.put(key, value);
    }
    
    public HashMap<String, HashMap<String, String>> getCopyEsignalSecSettingsListFut() {
        return new HashMap<String, HashMap<String, String>>(
                eSignalSecSettingsListFut);
    }
    
    public void addAttrToEsignalSecSettingsListInd(String key,
            HashMap<String, String> value) {
        if (eSignalSecSettingsListInd == null) {
            eSignalSecSettingsListInd = new HashMap<String, HashMap<String, String>>();
        }
        eSignalSecSettingsListInd.put(key, value);
    }
    
    public HashMap<String, HashMap<String, String>> getCopyEsignalSecSettingsListInd() {
        if(eSignalSecSettingsListInd != null)
            return new HashMap<String, HashMap<String, String>>(eSignalSecSettingsListInd);
        return null;
    }
    
    public void addAttrToSmartRoutSettingsList(String key, String value) {
        if (smartRoutingSettingsList == null) {
            smartRoutingSettingsList = new HashMap<String, String>();
        }
        smartRoutingSettingsList.put(key, value);
    }
    
    public HashMap<String, String> getCopySmartRoutSettingsList() {
        if(smartRoutingSettingsList != null)
            return new HashMap<String, String>(smartRoutingSettingsList);
        return null;
    }
    
}
