/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.manager;

import com.ib.reader.*;

/**
 *
 * @author Siteng Jin
 */
public abstract class LogManager {
    private final LogReader reader;
    private boolean isDeepDiagnostic = true; // True if use deep analysis
    private boolean isTWS; // If the files being investigated are from TWS (true) or IBG (false)
    
    public LogManager(){
        reader = new LogReader();
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
}
