/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.logger;

/**
 *
 * @author Siteng Jin
 */
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;
    
    static public void setup() throws IOException {
        
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        
        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        
        logger.setLevel(Level.INFO);
        
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String currDay = "";
        switch(dayOfWeek){
            case Calendar.MONDAY:
                currDay = "Mon";
                break;
            case Calendar.TUESDAY:
                currDay = "Tue";
                break;
            case Calendar.WEDNESDAY:
                currDay = "Wed";
                break;
            case Calendar.THURSDAY:
                currDay = "Thu";
                break;
            case Calendar.FRIDAY:
                currDay = "Fri";
                break;
            case Calendar.SATURDAY:
                currDay = "Sat";
                break;
            case Calendar.SUNDAY:
                currDay = "Sun";
                break;
            default:
                currDay = "Unknown";
                break;
        }
        fileTxt = new FileHandler("log." + currDay + ".txt", false);
        
        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
    }
}
