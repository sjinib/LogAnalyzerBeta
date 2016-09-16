package com.ib.parser;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.text.StyledDocument;
import com.ib.demoFrame.demoFrame;

public class TradeFileParser {
    public static void parseTradeFile(File tradeFile, javax.swing.JTextPane textPane) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(tradeFile));
        
        StyledDocument doc = textPane.getStyledDocument();
        demoFrame.addStylesToDocument(doc);
        
        doc.insertString(doc.getLength(), "================= Begin of transcript from file " + tradeFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        
        String currentLine;
        
        while((currentLine = br.readLine()) != null) {
            doc.insertString(doc.getLength(), currentLine + "\n", doc.getStyle("green"));
        }
        
        doc.insertString(doc.getLength(), "\n================= End of transcript from file " + tradeFile.getName() + "=================\n\n", doc.getStyle("blackBold"));
        textPane.setCaretPosition(0);
        
        if(br != null){
            br.close();
        }
        
    }
}
