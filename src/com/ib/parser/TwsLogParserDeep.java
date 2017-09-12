package com.ib.parser;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.text.StyledDocument;
import com.ib.analyzerGui.AnalyzerGUI;

public class TwsLogParserDeep {
    // Choice(3)
    public static void parseTwsEnvInfo(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "partitionList():", "estoring", "PROXY Cleared", "Jts dir is", "Build ", "java version =", "sun.arch.data.model =", "os =", "username =", "set audit xml file:", "Started on", "timezone:", "Locale=", "runtime =", "vendor =", "host =", "remotePort =", "S3: checkbox", "EventQueue-0] - CCP reported time:", "Monitor: Monitor:" };
        
        String[] filter2 = { "there is no internet connectivity" };
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
                    continue outerLoop;
                }
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
    }
    
    // Choice(1)
    public static void parseTwsLoginSeqInfo(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "SPLASH Creating screen", "SPLASH Connecting to server", "SPLASH Requesting startup parameters", "SPLASH Retrieving settings key", "SPLASH Loading resources", "SPLASH Initializing environment", "SPLASH Reading market rules", "SPLASH Reading settings file", "SPLASH Loading window factories", "SPLASH Initializing resources", "SPLASH Processing startup parameters", "SPLASH Loading order defaults", "SPLASH Initializing managers", "SPLASH Starting application", "SPLASH Closing screen and saving", "S3:", "Received login", "Saving settings", "factory settings", "Copying file", "Verifying saved file", "EventQueue-0] - CCP reported time:" };
        
        String[] filter2 = { "PROXY Failed to connect", "upload failed", "Login failed", "Failed token authentication", "S3: Error", "failed to save settings file", "verification failed for settings file", "Error parsing xml", "Error reading settings file" };
        
        String[] filter3 = { "Trading Login Handoff class", "SPLASH Close pressed", "Started auto log off", "Application exit", "Competition", "competition", "Competing", "User initiated exit session", "Crossing day in suspended state", "Day cross detected", "SYSTEM CLOCK HAS BEEN CHANGED BY USER" };
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("red"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter3){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("orange"));
                    continue outerLoop;
                }
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
    }
    
    // Choice(2)
    public static void parseTwsSysRes(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "Memory:total=", "CPU:cur=", "CPU Usage" };
        
        String[] filter2 = { "of live threads:" };
        
        String[] filter3 = { "has not responded", "OutOfMemoryError:" };
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("darkGreen"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("orange"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter3){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("red"));
                    continue outerLoop;
                }
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
    }
    
    // Choice(6)
    public static void parseTwsMktData(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "TWS Max tickers", "GRAPHS | ChartPanel", "creating subchart", "CHARTS | ChartPanel" };
        
        String[] filter2 = { "more deep books", "Market data is over the limit", "Historical data request pacing violation", "The application was disconnected from the HMDS Server", "No market data", "Trading TWS session is connected from a different IP address", "timed out waiting for market data" };
        
        String[] filter3 = { "No historical market data available","Requesting delayed data for q(ChartPanel", "Reset market data subscription", "Reset deep data subscription", "Delayed HMDS queries on config" };
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("red"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter3){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("orange"));
                    continue outerLoop;
                }
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
    }
    
    // Choice(4)
    public static void parseTwsConn(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "changing state for connection", "via ccp", "] - Connecting", "Connected, sending auth", "natively", "Passed", " Connected to ", "DISCONNECT_BY_DESIGN", "DISCONNECT_ON_EXIT_SESSION", "DISCONNECT_ON_REDIRECTION", "DISCONNECT_ON_INACTIVITY", "DISCONNECT_ON_SETTINGS", "DISCONNECT_NATIVE_CONNECTION_RESTORED", "NS_AUTH_START", "native upgrade", "Received heartbeat" };
        
        String[] filter2 = { "server error, will retry in seconds", "not received heartbeat", "competing", "SPLASH Creating mini screen for reconnect", ": connecting to server", "SPLASH Connecting to server", ": Authenticating" };
        
        String[] filter3 = { "creating socket failed", "java.net.SocketException:", ": Error connecting", "connection error,", "DISCONNECT_ON_BROKEN_SOCKET", "DISCONNECT_ON_CONNECTION_FAILURE", "DISCONNECT_AUTHORIZATION_FILED", "NO_PING_RESPONSE", "NO_AUTH_RESPONSE", "connection to ccp" };
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("orange"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter3){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("red"));
                    continue outerLoop;
                }
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
    }
    
    // Choice(9)
    public static void parseTwsHtbp(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "HTBP Hot Backup PGL generated", "HTBP Host:", "HTBP Hijacked host", "HTBP Main", "HTBP Notify", "HTBP S", "HTBP U", "HTBP Top", "HTBP Peer connected", "->Accessible" };
        
        String[] filter2 = { "Disconnected from live peer", "HTBP Hot Backup peer resolution failed", "Connection establish failed", "Failed to probe", "->Not Accessible", "failed, remaining attempts:" };
        
        String[] filter3 = { "HotBackupStatusManager: Status sent" };
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("red"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter3){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("blue"));
                    continue outerLoop;
                }
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
    }
    
    // Choice(7)
    public static void parseTwsApi(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { ":DET]", ":SYS]", "Max API Backfill", "API server listening", "API Max tickers", ":INFO] Handling incoming", ":INFO] Processing", ":INFO] Placing" };
        
        String[] filter2 = { ":ERR]", "Address already in use" };
        
        //String[] filter3 = { "has not responded", "OutOfMemoryError:" };
        String[] filter3 = {};
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("red"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter3){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("blueBold"));
                    continue outerLoop;
                }
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
    }
    
    // Choice(5)
    public static void parseTwsOrderTrds(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "moving dialog to front: Order Confirmation class", "Submitted exercise request", "on pending page", "your order will not be placed at the exchange", "Restore an order", "Current order recovery state" };
        
        String[] filter2 = { "FixOrderState", "skipping trade:", "what-if" };
        
        String[] filter3 = { "Order value is too large", "WARNING: Please note that the qualifying equity within your account", "ALERT: Your account,", "URGENT: one or more futures", "URGENT: Please note", "ORDER IS NOT ACCEPTED", "Order would cross related resting order", "Invalid effective time", "The time or time-zone entered is invalid", "You must enter a valid price", "Limit price too far outside of NBBO", "does not comply", "does not conform", "Order size is not within the limits", "order rejected:", "The contract is not available for short sale", "Some positions could not be closed", "The price cannot be empty", "COULD NOT VALUE THIS CONTRACT AT THIS TIME" };
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("blue"));
                    continue outerLoop;
                }
            }
            
            for(String str : filter3){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("red"));
                    continue outerLoop;
                }
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
    }
}
