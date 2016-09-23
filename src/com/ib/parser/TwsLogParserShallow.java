package com.ib.parser;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.text.StyledDocument;
import com.ib.demoFrame.AnalyzerGUI;

public class TwsLogParserShallow {
    // Choice(1)
    public static void parseTwsLoginSeqInfo(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "Jts dir is", ": Build", "java version =", "sun.arch.data.model =", "os =", "username =", "Setting dir is", "Started on", "Default timezone:", "Locale=", "locale:", "runtime =", "vendor =", "host =", "remotePort =", "PROXY", "vm name =", "S3:", "dayInit:" };
        
        String[] filter2 = { "Login failed", "Failed token authentication", "S3: Error" };
        
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
    
    // Choice(2)
    public static void parseTwsSysRes(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "JTS-AWT-Monitor:", "has not responded", "OutOfMemoryError:" };
        
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
    
    // Choice(3)
    public static void parseTwsEnvInfo(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { "<html>", "SPLASH", "moving dialog to front" };
        
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
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("gray"));
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
                
        String[] filter1 = { "not received heartbeat", "competing" };
        
        String[] filter2 = { "creating socket failed", ": Error connecting", "DISCONNECT_ON_BROKEN_SOCKET", "DISCONNECT_ON_CONNECTION_FAILURE", "DISCONNECT_AUTHORIZATION_FILED", "NO_PING_RESPONSE", "NO_AUTH_RESPONSE" };
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            if (currentLine.contains("TWS RESTART")) {
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("purpleBold"));
                System.out.println(currentLine);
                continue outerLoop;
            }
            
            for(String str : filter1){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("orange"));
                    System.out.println(currentLine);
                    continue outerLoop;
                }
            }
            
            for(String str : filter2){
                if(currentLine.contains(str)){
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("red"));
                    System.out.println(currentLine);
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
        
        String[] filter1 = { "ORDER IS NOT ACCEPTED", "Invalid effective time", "The time or time-zone entered is invalid", "You must enter a valid price", "Limit price too far outside of NBBO", "does not comply", "does not conform", "Order size is not within the limits", "order rejected:" };
        
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
        
        String[] filter1 = { "more deep books", "Market data is over the limit", "Historical data request pacing violation", "The application was disconnected from the HMDS Server", "No market data", "Trading TWS session is connected from a different IP address" };
        
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
    
    // Choice(7)
    public static void parseTwsApi(File twsLogFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        String[] filter1 = { ":DET]", ":SYS]", "Max API Backfill", "API server listening", "API Max tickers", ":INFO] Handling incoming", ":INFO] Processing", ":INFO] Placing" };
        
        String[] filter2 = { ":ERR]", "Address already in use" };        
        
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
        
        String[] filter1 = { "HTBP" };
        
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
                    doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("regular"));
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
