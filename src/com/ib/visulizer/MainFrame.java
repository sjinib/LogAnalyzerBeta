package com.ib.visulizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.ib.visulizer.NewTabbedPanel;
import com.ib.mamager.*;

public class MainFrame extends JFrame {
	static MainFrame INSTANCE;
	// private final NewTabbedPanel m_tabbedPanel = new NewTabbedPanel(true);

	private LogManager manager;
	private NewTabbedPanel tabbedPanel = new NewTabbedPanel(true);
	private ConfigurePanel configurePanel = new ConfigurePanel();

	public static void main(String[] args) {
		start(new MainFrame());
	}

	public static void start(MainFrame mainFrame) {
		INSTANCE = mainFrame;
		INSTANCE.run();
	}

	public MainFrame() {
		super();
		manager = new LogManager();
	}

	private void run() {
		this.setTitle("Log Analyzer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1024, 768);

		tabbedPanel.addTab("Configure", configurePanel);

		this.setVisible(true);
	}

	public class newJPanel extends JPanel {
		public newJPanel() {
			super();

		}
	}

	public class ConfigurePanel extends JPanel {
		public ConfigurePanel(){
    		JLabel subject = new JLabel("Log Configuration");
    		JLabel importTitle1 = new JLabel("Import downloaded diagnostic zip file:");
    		JLabel importTitle2 = new JLabel("Import single TWS log file:");
    		JLabel importTitle3 = new JLabel("Import single settings file:");    		
    		
    		JTextField zipLocation = new JTextField("Location...", 20);
    		JTextField extractLocation = new JTextField("Location...", 20);
    		
    		JPanel status = new JPanel();
    		JLabel extractStatus = new JLabel("Extraction Status:");
    		JLabel twsLogStatus = new JLabel("TWS Log Status:");
    		JLabel settingsStatus = new JLabel("Settings File Status:");
    		
    		HtmlButton browse1 = new HtmlButton("Browse") {
    			@Override public void actionPerformed(){
    				//browse file
    				zipLocation.setText("C:/Users/Siteng Jin/Documents/GitHub/LogAnalyzer/piotr888.dhmejdfcm.20160629212226.zip");
    				int l = zipLocation.getText().lastIndexOf('/');
    				extractLocation.setText(zipLocation.getText().substring(0, l) + "/Temp");
    				
    				manager.setReaderLocation(zipLocation.getText(), extractLocation.getText());
    			}
    		};
    		
    		HtmlButton browse2 = new HtmlButton("Browse") {
    			@Override public void actionPerformed(){
    				//browse file
    				extractLocation.setText("C:/Users/Siteng Jin/Documents/GitHub/LogAnalyzer/Temp");
    				
    				manager.setReaderLocation(null, extractLocation.getText());
    			}
    		};
    		
    		HtmlButton extract = new HtmlButton("Extract") {
    			@Override public void actionPerformed(){
    				manager.extract();
    				extractStatus.setText("Extraction Status: Success");
    			}
    		};
    		
    		JList logFileChooser = new JList();
    		JList settingsFileChooser = new JList();
    	}
	}
}
