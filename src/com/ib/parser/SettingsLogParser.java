package com.ib.parser;

import java.util.HashMap;
import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.swing.text.StyledDocument;
import com.ib.analyzerGui.AnalyzerGUI;

import com.ib.message.*;
import java.util.ArrayList;

public class SettingsLogParser {
    
    public static void parseAPISettingsFile(File settingsFile, SettingsMessage settingsMessage, javax.swing.JTextPane textPane) throws Exception{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(settingsFile);
        doc.getDocumentElement().normalize();
        
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
        
        // Display on text pane
        StyledDocument styledDoc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(styledDoc);
        
        styledDoc.insertString(styledDoc.getLength(), "================= Begin of transcript from file " + settingsFile.getName() + "=================\n\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "API Settings: \n", styledDoc.getStyle("italic"));
        
        HashMap<String, String> copyApiSettingsList = apiSettingsMessage.getCopyApiSettingsList();
        for(int i = 0; i < ApiSettingsMessage.APISETTINGSTAGS.length; i++){
            if (ApiSettingsMessage.APISETTINGSTAGS[i].equals("slowBufferTimeout")){
                styledDoc.insertString(styledDoc.getLength(), ApiSettingsMessage.APISETTINGSTEXT[i], styledDoc.getStyle("regular"));
                if(copyApiSettingsList != null)
                    styledDoc.insertString(styledDoc.getLength(), copyApiSettingsList.get(ApiSettingsMessage.APISETTINGSTAGS[i]) + " seconds\n", styledDoc.getStyle("blackBold"));
                else
                    styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
            } else {
                styledDoc.insertString(styledDoc.getLength(), ApiSettingsMessage.APISETTINGSTEXT[i], styledDoc.getStyle("regular"));
                if(copyApiSettingsList != null)
                    styledDoc.insertString(styledDoc.getLength(), copyApiSettingsList.get(ApiSettingsMessage.APISETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
                else
                    styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
            }
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\nAPI Precautions: \n", styledDoc.getStyle("italic"));
        
        HashMap<String, String> copyApiPrecautionsList = apiSettingsMessage.getCopyApiPrecautionsList();
        for(int i = 0; i < ApiSettingsMessage.APIPRECAUTIONSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), ApiSettingsMessage.APIPRECAUTIONSTEXT[i], styledDoc.getStyle("regular"));
            if(copyApiPrecautionsList != null)
                styledDoc.insertString(styledDoc.getLength(), copyApiPrecautionsList.get(ApiSettingsMessage.APIPRECAUTIONSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        ArrayList<String> copyTrustedIPs = apiSettingsMessage.getCopyTrustedIPs();
        if(copyTrustedIPs != null){
            for(int i = 0; i < apiSettingsMessage.getCopyTrustedIPs().size(); i++){
                styledDoc.insertString(styledDoc.getLength(), copyTrustedIPs.get(i) + "\n", styledDoc.getStyle("blackBold"));
            }
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\n================= End of transcript from file " + settingsFile.getName() + "=================\n\n", styledDoc.getStyle("blackBold"));
    }
    
    public static void parseMDSettingsFile(File settingsFile, SettingsMessage settingsMessage, javax.swing.JTextPane textPane) throws Exception{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(settingsFile);
        doc.getDocumentElement().normalize();
        
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
        
        // Display on text pane
        StyledDocument styledDoc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(styledDoc);
        
        styledDoc.insertString(styledDoc.getLength(), "================= Begin of transcript from file " + settingsFile.getName() + "=================\n\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "Market Data Settings: \n", styledDoc.getStyle("italic"));
        
        HashMap<String, String> copyMdSettingsList = mdSettingsMessage.getCopyMdSettingsList();
        for(int i = 0; i < MarketDataSettingsMessage.MARKETDATASETTINGSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), MarketDataSettingsMessage.MARKETDATASETTINGSTEXT[i], styledDoc.getStyle("regular"));
            if(copyMdSettingsList != null)
                styledDoc.insertString(styledDoc.getLength(), copyMdSettingsList.get(MarketDataSettingsMessage.MARKETDATASETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
            
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\nESignal Settings: \n", styledDoc.getStyle("italic"));
        
        HashMap<String, String> copyEsignalSettingsList = mdSettingsMessage.getCopyEsignalSettingsList();
        for(int i = 0; i < MarketDataSettingsMessage.ESIGNALSETTINGSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), MarketDataSettingsMessage.ESIGNALSETTINGSTEXT[i], styledDoc.getStyle("regular"));
            if(copyEsignalSettingsList != null)
                styledDoc.insertString(styledDoc.getLength(), copyEsignalSettingsList.get(MarketDataSettingsMessage.ESIGNALSETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\nESignal Products Settings: \n", styledDoc.getStyle("italic"));
        
        styledDoc.insertString(styledDoc.getLength(), "STK (US): " + mdSettingsMessage.getCopyEsignalSecSettingsListStk().get("usa").toString() + "\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "STK (INTL): " + mdSettingsMessage.getCopyEsignalSecSettingsListStk().get("intl").toString() + "\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "OPT (US): " + mdSettingsMessage.getCopyEsignalSecSettingsListOpt().get("usa").toString() + "\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "OPT (INTL): " + mdSettingsMessage.getCopyEsignalSecSettingsListOpt().get("intl").toString() + "\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "FUT (US): " + mdSettingsMessage.getCopyEsignalSecSettingsListFut().get("usa").toString() + "\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "FUT (INTL): " + mdSettingsMessage.getCopyEsignalSecSettingsListFut().get("intl").toString() + "\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "IND (US): " + mdSettingsMessage.getCopyEsignalSecSettingsListInd().get("usa").toString() + "\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "IND (INTL): " + mdSettingsMessage.getCopyEsignalSecSettingsListInd().get("intl").toString() + "\n", styledDoc.getStyle("blackBold"));
        
        styledDoc.insertString(styledDoc.getLength(), "\nSmart Routing Settings: \n", styledDoc.getStyle("italic"));
        
        HashMap<String, String> copySmartRouteSettingsList = mdSettingsMessage.getCopySmartRouteSettingsList();
        for(int i = 0; i < MarketDataSettingsMessage.SMARTROUTINGSETTINGSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), MarketDataSettingsMessage.SMARTROUTINGSETTINGSTEXT[i], styledDoc.getStyle("regular"));
            if(copySmartRouteSettingsList != null)
                styledDoc.insertString(styledDoc.getLength(), copySmartRouteSettingsList.get(MarketDataSettingsMessage.SMARTROUTINGSETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\n================= End of transcript from file " + settingsFile.getName() + "=================\n\n", styledDoc.getStyle("blackBold"));
    }
    
    public static void parseEnvSettingsFile(File settingsFile, SettingsMessage settingsMessage, javax.swing.JTextPane textPane) throws Exception{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(settingsFile);
        doc.getDocumentElement().normalize();
        
        EnvSettingsMessage envSettingsMessage = null;
        
        if (settingsMessage instanceof EnvSettingsMessage) {
            envSettingsMessage = (EnvSettingsMessage) settingsMessage;
        }
        
        NodeList envNList = doc.getElementsByTagName("WorkspaceCollection");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                String key = keyElement.getAttribute("currentWorkspaceKey");
                envSettingsMessage.setCurrentWorkSpaceKey(key);
            }
        }
        
        envNList = doc.getElementsByTagName("Workspace");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.WORKSPACETAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    envSettingsMessage.addWorkSpaceKey(attr, attrValue);
                }
            }
        }
        
        envNList = doc.getElementsByTagName("TickerPageSetting");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.TICKERPAGESETTINGSTAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    envSettingsMessage.addTickerPageSettings(attr, attrValue);
                }
            }
        }
        
        envNList = doc.getElementsByTagName("SystemSettings");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.SYSTEMSETTINGSTAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    envSettingsMessage.addSystemSettings(attr, attrValue);
                }
            }
        }
        
        envNList = doc.getElementsByTagName("AccountSettings");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                String attrValue = keyElement.getAttribute("showAllInBaseCurrency");
                envSettingsMessage.setShowAllInBaseCurrency(attrValue);
            }
        }
        
        envNList = doc.getElementsByTagName("UISettings");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.UISETTINGSTAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    envSettingsMessage.addUISettings(attr, attrValue);
                }
            }
        }
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.UIORDERSSETTINGSTAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    envSettingsMessage.addUIOrdsSettings(attr, attrValue);
                }
            }
        }
        
        envNList = doc.getElementsByTagName("OrderWizardDisplayMode");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                String key = keyElement.getAttribute("value");
                envSettingsMessage.setOrderWizardDisplayMode(key);
            }
        }
        
        envNList = doc.getElementsByTagName("OrderSettings");
        
        for (int i = 0; i < envNList.getLength(); i++){
            Node keyNode = envNList.item(i);
            if (keyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element keyElement = (Element) keyNode;
                for(String attr : EnvSettingsMessage.ORDERSETTINGSTAGS){
                    String attrValue = keyElement.getAttribute(attr);
                    envSettingsMessage.addOrderSettings(attr, attrValue);
                }
            }
        }
        
        // Display on text pane
        StyledDocument styledDoc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(styledDoc);
        
        styledDoc.insertString(styledDoc.getLength(), "================= Begin of transcript from file " + settingsFile.getName() + "=================\n\n", styledDoc.getStyle("blackBold"));
        styledDoc.insertString(styledDoc.getLength(), "Workspaces: \n", styledDoc.getStyle("italicBold"));
        styledDoc.insertString(styledDoc.getLength(), "Workspace key currently in use: ", styledDoc.getStyle("italic"));
        
        styledDoc.insertString(styledDoc.getLength(), envSettingsMessage.getCurrentWorkSpaceKey() + "\n", styledDoc.getStyle("blackBold"));
        
        HashMap<String, String> copyWorkSpaceKey = envSettingsMessage.getCopyWorkSpaceKey();
        for(int i = 0; i < EnvSettingsMessage.WORKSPACETAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), EnvSettingsMessage.WORKSPACETEXT[i], styledDoc.getStyle("regular"));
            if(copyWorkSpaceKey != null)
                styledDoc.insertString(styledDoc.getLength(), copyWorkSpaceKey.get(EnvSettingsMessage.WORKSPACETAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\nTWS Tabs: (Origin:6 = Created by the user - Origin:0 = Created by the system)\n", styledDoc.getStyle("italic"));
        
        HashMap<String, String> copyTickerPageSettings = envSettingsMessage.getCopyTickerPageSettings();
        for(int i = 0; i < EnvSettingsMessage.TICKERPAGESETTINGSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), EnvSettingsMessage.TICKERPAGESETTINGSTEXT[i], styledDoc.getStyle("regular"));
            if(copyTickerPageSettings != null)
                styledDoc.insertString(styledDoc.getLength(), copyTickerPageSettings.get(EnvSettingsMessage.TICKERPAGESETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\nTWS System settings:\n", styledDoc.getStyle("italicBold"));
        
        HashMap<String, String> copySystemSettings = envSettingsMessage.getCopySystemSettings();
        for(int i = 0; i < EnvSettingsMessage.SYSTEMSETTINGSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), EnvSettingsMessage.SYSTEMSETTINGSTEXT[i], styledDoc.getStyle("regular"));
            if(copySystemSettings != null)
                styledDoc.insertString(styledDoc.getLength(), copySystemSettings.get(EnvSettingsMessage.SYSTEMSETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\nShow positions in base currency (In Account,Portfolio and Quote monitor pages): ", styledDoc.getStyle("italic"));
        
        styledDoc.insertString(styledDoc.getLength(), envSettingsMessage.getShowAllInBaseCurrency() + "\n", styledDoc.getStyle("blackBold"));
        
        styledDoc.insertString(styledDoc.getLength(), "\nUser Interface Settings: \n", styledDoc.getStyle("italicBold"));
        
        HashMap<String, String> copyUISettings = envSettingsMessage.getCopyUISettings();
        for(int i = 0; i < EnvSettingsMessage.UISETTINGSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), EnvSettingsMessage.UISETTINGSTEXT[i], styledDoc.getStyle("regular"));
            if(copyUISettings != null)
                styledDoc.insertString(styledDoc.getLength(), copyUISettings.get(EnvSettingsMessage.UISETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\nUser Interface Settings Orders Settings: \n", styledDoc.getStyle("italicBold"));
        
        HashMap<String, String> copyUIOrdsSettings = envSettingsMessage.getCopyUIOrdsSettings();
        for(int i = 0; i < EnvSettingsMessage.UIORDERSSETTINGSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), EnvSettingsMessage.UIORDERSSETTINGSTEXT[i], styledDoc.getStyle("regular"));
            if(copyUIOrdsSettings != null)
                styledDoc.insertString(styledDoc.getLength(), copyUIOrdsSettings.get(EnvSettingsMessage.UIORDERSSETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\nOrder Wizard Display Mode (1=Show/Hide Manually 2=Show when an order is created): ", styledDoc.getStyle("italic"));
        
        styledDoc.insertString(styledDoc.getLength(), envSettingsMessage.getCopyOrderWizardDisplayMode() + "\n", styledDoc.getStyle("blackBold"));
        
        styledDoc.insertString(styledDoc.getLength(), "\nOrders Settings: \n", styledDoc.getStyle("italicBold"));
        
        HashMap<String, String> copyOrdersSettings = envSettingsMessage.getCopyOrderSettings();
        for(int i = 0; i < EnvSettingsMessage.ORDERSETTINGSTAGS.length; i++){
            styledDoc.insertString(styledDoc.getLength(), EnvSettingsMessage.ORDERSETTINGSTEXT[i], styledDoc.getStyle("regular"));
            if(copyOrdersSettings != null)
                styledDoc.insertString(styledDoc.getLength(), copyOrdersSettings.get(EnvSettingsMessage.ORDERSETTINGSTAGS[i]) + "\n", styledDoc.getStyle("blackBold"));
            else
                styledDoc.insertString(styledDoc.getLength(), "\n", styledDoc.getStyle("blackBold"));
        }
        
        styledDoc.insertString(styledDoc.getLength(), "\n================= End of transcript from file " + settingsFile.getName() + "=================\n\n", styledDoc.getStyle("blackBold"));
    }
}
