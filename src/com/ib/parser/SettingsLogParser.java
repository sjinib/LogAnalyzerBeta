package com.ib.parser;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.ib.message.*;

public class SettingsLogParser {
    
    public static void parseAPISettingsFile(Document doc, SettingsMessage settingsMessage) {
        
        ApiSettingsMessage apiSettingsMessage = null;
        
        if (settingsMessage instanceof ApiSettingsMessage) {
            apiSettingsMessage = (ApiSettingsMessage) settingsMessage;
        }
        
        NodeList apiNList = doc.getElementsByTagName("ApiSettings");
        Node apiNode = apiNList.item(0);
        
        if (apiNode.getNodeType() == Node.ELEMENT_NODE) {
            Element apiElement = (Element) apiNode;
            
            for (String attr : ApiSettingsMessage.APISETTINGSTAGS) {
                String attrValue = apiElement.getAttribute(attr);
                apiSettingsMessage.addApiAttrToSettingsList(attr, attrValue);
            }
            
            for (String attr : ApiSettingsMessage.APIPRECAUTIONSTAGS) {
                String attrValue = apiElement.getAttribute(attr);
                apiSettingsMessage.addApiAttrToPrecautionsList(attr, attrValue);
            }
            
            NodeList listOfStrings = apiElement.getElementsByTagName("String");
            
            for (int i = 0; i < listOfStrings.getLength(); i++) {
                apiSettingsMessage.addTrustedIPs(listOfStrings.item(i).getTextContent());
            }
        }
    }
    
    public static void parseMDSettingsFile(Document doc, SettingsMessage settingsMessage) {
        
        MarketDataSettingsMessage mdSettingsMessage = null;
        
        if (settingsMessage instanceof MarketDataSettingsMessage) {
            mdSettingsMessage = (MarketDataSettingsMessage) settingsMessage;
        }
        
        NodeList mdNList = doc.getElementsByTagName("MarketDataSettings");
        Node mdNode = mdNList.item(0);
        
        if (mdNode.getNodeType() == Node.ELEMENT_NODE) {
            Element mdElement = (Element) mdNode;
            
            for (String attr : MarketDataSettingsMessage.MARKETDATASETTINGSTAGS) {
                String attrValue = mdElement.getAttribute(attr);
                mdSettingsMessage.addMdAttrToMDSettingsList(attr, attrValue);
            }
        }
        
        mdNList = doc.getElementsByTagName("SmartRoutingSettings");
        mdNode = mdNList.item(0);
        
        if (mdNode.getNodeType() == Node.ELEMENT_NODE) {
            Element mdElement = (Element) mdNode;
            
            for (String attr : MarketDataSettingsMessage.SMARTROUTINGSETTINGSTAGS) {
                String attrValue = mdElement.getAttribute(attr);
                mdSettingsMessage.addAttrToSmartRoutSettingsList(attr, attrValue);
            }
        }
        
        mdNList = doc.getElementsByTagName("ESignalSettings");
        mdNode = mdNList.item(0);
        
        if (mdNode.getNodeType() == Node.ELEMENT_NODE) {
            Element mdElement = (Element) mdNode;
            
            for (String attr : MarketDataSettingsMessage.ESIGNALSETTINGSTAGS) {
                String attrValue = mdElement.getAttribute(attr);
                mdSettingsMessage.addAttrToEsignalSettingsList(attr, attrValue);
            }
            
            NodeList esSecList = mdElement.getElementsByTagName("ESignalExchangeSettings");
            for (int i = 0; i < esSecList.getLength(); i++) {
                
                Node esSecNode = esSecList.item(i);
                
                if (mdNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element esSecElement = (Element) esSecNode;
                    String key = null;
                    if (i < 2) {
                        HashMap<String, String> attrValue = new HashMap<String, String>();
                        for (String attr : MarketDataSettingsMessage.ESIGNALEXCHANGESETTINGSTAGS) {
                            if(attr.equals("varName")){
                                key = esSecElement.getAttribute(attr);
                            } else {
                                String value = esSecElement.getAttribute(attr);
                                attrValue.put(attr, value);
                            }
                        }
                        mdSettingsMessage.addAttrToEsignalSecSettingsListOpt(key, attrValue);
                    } else if (i < 4) {
                        HashMap<String, String> attrValue = new HashMap<String, String>();
                        for (String attr : MarketDataSettingsMessage.ESIGNALEXCHANGESETTINGSTAGS) {
                            if(attr.equals("varName")){
                                key = esSecElement.getAttribute(attr);
                            } else {
                                String value = esSecElement.getAttribute(attr);
                                attrValue.put(attr, value);
                            }
                        }
                        mdSettingsMessage.addAttrToEsignalSecSettingsListStk(key, attrValue);
                    } else if (i < 6) {
                        HashMap<String, String> attrValue = new HashMap<String, String>();
                        for (String attr : MarketDataSettingsMessage.ESIGNALEXCHANGESETTINGSTAGS) {
                            if(attr.equals("varName")){
                                key = esSecElement.getAttribute(attr);
                            } else {
                                String value = esSecElement.getAttribute(attr);
                                attrValue.put(attr, value);
                            }
                        }
                        mdSettingsMessage.addAttrToEsignalSecSettingsListFut(key, attrValue);
                    } else {
                        HashMap<String, String> attrValue = new HashMap<String, String>();
                        for (String attr : MarketDataSettingsMessage.ESIGNALEXCHANGESETTINGSTAGS) {
                            if(attr.equals("varName")){
                                key = esSecElement.getAttribute(attr);
                            } else {
                                String value = esSecElement.getAttribute(attr);
                                attrValue.put(attr, value);
                            }
                        }
                        mdSettingsMessage.addAttrToEsignalSecSettingsListInd(key, attrValue);
                    }
                }
            }
        }
        
    }
    
    public static void parseEnvSettingsFile(Document doc, SettingsMessage settingsMessage){
        EnvSettingsMessage envSettingsMessage = null;
        
        if (settingsMessage instanceof EnvSettingsMessage) {
            envSettingsMessage = (EnvSettingsMessage) settingsMessage;
        }
        
        NodeList envNList = doc.getElementsByTagName("WorkspaceCollection");
        System.out.println("Workspaces:");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                String key = keyElement.getAttribute("currentWorkspaceKey");
                System.out.println("Workspace key currently in use: " + key);
            }
        }
        
        envNList = doc.getElementsByTagName("Workspace");
        System.out.println("");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.WORKSPACETAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    if(!attrValue.isEmpty()){
                        System.out.println(attrValue);
                    } else {
                        System.out.println("Not present");
                    }
                }
            }
        }
        
        envNList = doc.getElementsByTagName("TickerPageSetting");
        System.out.println("TWS Tabs:");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.TICKERPAGESETTINGSTAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    if(!attrValue.isEmpty()){
                        System.out.println(attrValue);
                    } else {
                        System.out.println("Not present");
                    }
                }
            }
        }
        
        envNList = doc.getElementsByTagName("SystemSettings");
        System.out.println("TWS System settings:");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.SYSTEMSETTINGSTAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    if(!attrValue.isEmpty()){
                        System.out.println(attrValue);
                    } else {
                        System.out.println("Not present");
                    }
                }
            }
        }
        
        envNList = doc.getElementsByTagName("AccountSettings");
        System.out.println("Show positions in base currency (In Account,Portfolio and Quote monitor pages): ");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                String attrValue = keyElement.getAttribute("showAllInBaseCurrency");
                if(!attrValue.isEmpty()){
                    System.out.println(attrValue);
                } else {
                    System.out.println("Not present");
                }
            }
        }
        
        envNList = doc.getElementsByTagName("UISettings");
        System.out.println("User Interface Settings: ");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.UISETTINGS){
                    String attrValue = keyElement.getAttribute(attr);
                    if(!attrValue.isEmpty()){
                        System.out.println(attrValue);
                    } else {
                        System.out.println("Not present");
                    }
                }
            }
        }
    }    
}
