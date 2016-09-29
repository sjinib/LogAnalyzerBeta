/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.ib.parser;

import com.ib.analyzerGui.AnalyzerGUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Siteng Jin
 */
public class RegExSearch {
    public static void search(String regEx, boolean isCaseSensitive, File twsLogFile, javax.swing.JTextPane textPane) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(twsLogFile));
        
        StyledDocument doc = textPane.getStyledDocument();
        AnalyzerGUI.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of RegEx Search of \"" + regEx + "\" from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        outerLoop: while((currentLine = br.readLine()) != null) {
            Pattern pattern;
            if(isCaseSensitive == true){
                pattern = Pattern.compile(regEx);
            } else {
                pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            }
            
            Matcher matcher = pattern.matcher(currentLine);
            
            if(matcher.find()){
                doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("gray"));
            }
        }
        
        doc.insertString(doc.getLength(), "\n================= End of RegEx Search from file " + twsLogFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
    }
}
