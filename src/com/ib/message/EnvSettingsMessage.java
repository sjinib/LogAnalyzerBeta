package com.ib.message;

import java.util.ArrayList;
import java.util.HashMap;

public class EnvSettingsMessage extends SettingsMessage {
        private String currentWorkspaceKey;
	private HashMap<String, String> workSpace;
        private HashMap<String, String> tickerPageSettings;
        private HashMap<String, String> systemSettings;
        private String showAllInBaseCurrency;
        private HashMap<String, String> uiSettings;
        private String orderWizardDisplayMode;
        private HashMap<String, String> orderSettings;

        public static final String[] WORKSPACETEXT = { "Workspace name: ", "Mode: ", "WorkspaceKey: " };
        
        public static final String[] TICKERPAGESETTINGSTEXT = { "Tab: ", "Origin: ", "Detached: ", "Layout: ", "Show All Orders: ", "Show Account only order: ", "Show orders panel: ", "Account: " };
        
        public static final String[] SYSTEMSETTINGSTEXT = { "Auto logoff time: ", "Auto save settings: ", "Save watchlist to cloud: ", "Store GTC and DAY orders on separate pending pages: " };
        
        public static final String[] UISETTINGSTEXT = { "Browser: ", "Create audit file: ", "FUT Auto rollover: ", "Display tick-dots instead of colored prices: ", "Rapid STK entry: ", "Round values to the nearest whole number: ", "Round values to the nearest whole numbers in Account window: ", "Do not prompt for MD subscription: ", "Prefer Calculated Volume: ", "Volume - Abbreviated display shows K for thousands or M for millions: ", "Display FX Positions in portfolio (Virtual FX portfolio expanded): ", "Include Today's Opening Position in PnL: ", "Include Away positions to Portfolio and PnL: ", "Auto sort submitted orders: ", "Auto remove completed orders: ", "Leave filled order on the screen for (s): ", "Leave canceled order on the screen for (s): ", "Big scale sizes: ", "Big scale prices: " };
        
        public static final String[] ORDERSETTINGSTEXT = { "Reuse rejected orders: ", "Auto adjust limit price for STP LMT and LIT orders: ", "Use auto-logoff time instead of midnight when downloading completed orders: ", "Auto save default size: " };
        
	public static final String[] WORKSPACETAGS = { "name", "mode",
			"workspaceKey" };

	public static final String[] TICKERPAGESETTINGSTAGS = { "name", "origin",
			"detached", "layout", "showAlOrders", "showAccountOnlyOrders",
			"showOrdersPanel", "account" };

	public static final String[] SYSTEMSETTINGSTAGS = { "autoLogoffTime",
			"autoSaveSettings", "saveWatchlistsToCloud", "twoPendingPages" };
	
	public static final String[] UISETTINGSTAGS = { "browser", "audit", "autoRollover", "rapidStkEntry", "roundValues", "roundValuesOnAccountWindow", "doNotPromptForMdSubscr", "displayTrueTickVolume", "displayAbbreviatedVolume", "includeFxPositions", "includeOpeningPositionInPnL", "showAwayZeroPosition", "useTickDots", "enableOrderSort", "autoRemoveFinishedOrder", "delayForFilled", "delayForCanceled", "bigScaleSizes", "bigScalePrices" };
        
        public static final String[] ORDERSETTINGSTAGS = { "reuseRejectedOrd", "autoAdjustStopLimit", "useAutoLogoffAsMidnight", "saveAsDefaultSize" };
        
        public void setCurrentWorkSpaceKey(String s){
            if(currentWorkspaceKey == null){
                currentWorkspaceKey = new String();
            }
            currentWorkspaceKey = s;
        }
        
        public String getCurrentWorkSpaceKey(){
            return new String(currentWorkspaceKey);
        }
        
	public void addWorkSpaceKey(String key, String value) {
		if (workSpace == null) {
			workSpace = new HashMap<String, String>();
		}
		workSpace.put(key, value);
	}

	public HashMap<String, String> getCopyWorkSpaceKey() {
            if(workSpace != null)
		return new HashMap<String, String>(workSpace);
            return null;
	}
        
        public void addTickerPageSettings(String key, String value) {
		if (tickerPageSettings == null) {
			tickerPageSettings = new HashMap<String, String>();
		}
		tickerPageSettings.put(key, value);
	}

	public HashMap<String, String> getCopyTickerPageSettings() {
            if(tickerPageSettings != null)
		return new HashMap<String, String>(tickerPageSettings);
            return null;
	}
        
        public void addSystemSettings(String key, String value) {
		if (systemSettings == null) {
			systemSettings = new HashMap<String, String>();
		}
		systemSettings.put(key, value);
	}

	public HashMap<String, String> getCopySystemSettings() {
            if(systemSettings != null)
		return new HashMap<String, String>(systemSettings);
            return null;
	}
        
        public void setShowAllInBaseCurrency(String s){
            if(showAllInBaseCurrency == null){
                showAllInBaseCurrency = new String();
            }
            showAllInBaseCurrency = s;
        }
        
        public String getShowAllInBaseCurrency(){
            return new String(showAllInBaseCurrency);
        }
        
        public void addUISettings(String key, String value) {
		if (uiSettings == null) {
			uiSettings = new HashMap<String, String>();
		}
		uiSettings.put(key, value);
	}

	public HashMap<String, String> getUISettings() {
            if(uiSettings != null)
		return new HashMap<String, String>(uiSettings);
            return null;
	}
        
        public void setOrderWizardDisplayMode(String s){
            if(orderWizardDisplayMode == null){
                orderWizardDisplayMode = new String();
            }
            orderWizardDisplayMode = s;
        }
        
        public String getOrderWizardDisplayMode(){
            return new String(orderWizardDisplayMode);
        }
        
        public void addOrderSettings(String key, String value) {
		if (orderSettings == null) {
			orderSettings = new HashMap<String, String>();
		}
		orderSettings.put(key, value);
	}

	public HashMap<String, String> getOrderSettings() {
            if(orderSettings != null)
		return new HashMap<String, String>(orderSettings);
            return null;
	}
}
