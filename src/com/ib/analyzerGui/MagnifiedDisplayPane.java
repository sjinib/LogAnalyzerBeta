/**
 * The MagnifiedDisplayPane class: used for popping up a larger display pane when
 * double clicking on a certain display pane for detailed analysis for the specific
 * topic
 */
package com.ib.analyzerGui;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
/**
 *
 * @author Siteng Jin
 */
public class MagnifiedDisplayPane extends JFrame{
    public MagnifiedDisplayPane(JTextPane textPane, JScrollPane scrollPane){
        setTitle(textPane.getName());
        setPreferredSize(new java.awt.Dimension(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width-300, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-300));
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getPreferredSize().width/2, dim.height/2-getPreferredSize().height/2);
        
        javax.swing.JTextPane newTextPane = new javax.swing.JTextPane();
        newTextPane.setEditable(false);
        newTextPane.setDocument(textPane.getStyledDocument());
        
        javax.swing.JScrollPane newScrollPane = new javax.swing.JScrollPane(newTextPane);
        newScrollPane.setViewportView(newTextPane);
        newScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(((TitledBorder) scrollPane.getBorder()).getTitleColor(), 2, true), ((TitledBorder) scrollPane.getBorder()).getTitle(), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), ((TitledBorder) scrollPane.getBorder()).getTitleColor()));
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(newScrollPane)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(newScrollPane)
        );
        
        pack();
    }    
}
