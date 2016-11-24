package com.ib.testPack;

import com.ib.manager.*;

public class testAnalyzer {
	public static void main(String [] arg){
		//LogManager_local manager = new LogManager_local();
		//manager.start();
                LogManager_server manager = new LogManager_server();
                //manager.downloadDiagnosticFile(zipLocation);
	}
}
