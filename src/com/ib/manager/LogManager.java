/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.manager;

import com.ib.reader.*;
import java.util.prefs.Preferences;

/**
 *
 * @author Siteng Jin
 */
public abstract class LogManager {
    private final LogReader reader;
    private boolean isDeepDiagnostic = true; // True if use deep analysis
    private boolean isTWS = true; // If the files being investigated are from TWS (true) or IBG (false)
    
    private Preferences prefs;
    
    public LogManager(){
        reader = new LogReader();
        prefs = Preferences.userNodeForPackage(this.getClass());
    }
    
    public LogReader getReader(){
        return reader;
    }
    
    public boolean isTws(){
        return isTWS;
    }
    
    public void setIsTws(boolean isTws){
        isTWS = isTws;
    }
    
    public boolean isDeepDiagnostic(){
        return isDeepDiagnostic;
    }
    
    public void setDeepDiagnostic(boolean isDeepDiag){
        isDeepDiagnostic = isDeepDiag;
    }
    
    public void openLogFileInNotePad(boolean useManual){}
    
    public void showInFolder(){}
    
    public void openScreenshot(){}
    
    public Preferences getPref(){
        return prefs;
    }
}
