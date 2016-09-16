package com.ib.message;

import java.util.ArrayList;
import java.util.HashMap;

public class ApiSettingsMessage extends SettingsMessage {
    
    private HashMap<String, String> apiSettingsList;
    private HashMap<String, String> apiPrecautionsList;
    private ArrayList<String> trustedIPs;
    
    public ApiSettingsMessage() {
    }
    
    public static final String[] APISETTINGSTEXT = {"Enable ActiveX and Socket Clients: ", "Enable DDE Clients: ",
        "Read-Only API: ", "Download open orders on connection: ", "Include FX positions when sending portoflio: ",
        "Send status updates for EFP and Volatility orders with \"Continuoues \" flag: ", "Socket port: ",
        "Use negative numbers to bind automatic orders: ", "Create API message log file: ", "Include market data in API log file: ",
        "Logging Level: ", "Master API Client ID: ", "Timeout to send bulk data to API: ", "Component Exch. Separator: ",
        "Allow connections from localhost only: "};
    
    public static final String[] APIPRECAUTIONSTEXT = {"Bypass Order Precautions for API Orders: ",
        "Bypass Bond warning for API Orders: ", "Bypass negative yield to worst confirmation for API Orders: ",
        "Bypass Called Bond warning for API Orders: ", "Bypass \"same action pair trade\" warning for API orders: "};
    
    public static final String[] APISETTINGSTAGS = { "socketClient", "dde", "readOnlyApi",
        "autoOpenOrdDonwload", "includeFxPositions",
        "includeContinuousUpdateChanges", "port", "useNegativeAutoRange",
        "createApiMsgLogFile", "includeMktData", "logLevel",
        "masterClientID", "slowBufferTimeout", "compExchSeparator",
        "allowOnlyLocalhost" };
    
    public static final String[] APIPRECAUTIONSTAGS = {
        "overridePrecautionaryConstraints", "bypassBondWarning",
        "bypassYtwWarning", "bypassCalledBondWarning",
        "bypassSameActionPairTradeWarning" };
    
    public void addTrustedIPs(String ip) {
        if (trustedIPs == null) {
            trustedIPs = new ArrayList<String>();
        }
        trustedIPs.add(ip);
    }
    
    public ArrayList<String> getCopyTrustedIPs() {
        if(trustedIPs != null)
            return new ArrayList<String>(trustedIPs);
        return null;
    }
    
    public void addApiAttrToSettingsList(String key, String value) {
        if (apiSettingsList == null) {
            apiSettingsList = new HashMap<String, String>();
        }
        apiSettingsList.put(key, value);
    }
    
    public HashMap<String, String> getCopyApiSettingsList() {
        if(apiSettingsList != null)
            return new HashMap<String, String>(apiSettingsList);
        return null;
    }
    
    public void addApiAttrToPrecautionsList(String key, String value) {
        if (apiPrecautionsList == null) {
            apiPrecautionsList = new HashMap<String, String>();
        }
        apiPrecautionsList.put(key, value);
    }
    
    public HashMap<String, String> getCopyApiPrecautionsList() {
        if(apiPrecautionsList != null)
            return new HashMap<String, String>(apiPrecautionsList);
        return null;
    }
}
