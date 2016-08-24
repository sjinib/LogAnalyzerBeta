package com.ib.message;

import java.util.ArrayList;

public class EnvSettingsMessage extends SettingsMessage {
	private ArrayList<String> workSpaceKey;

	public static final String[] WORKSPACETAGS = { "name", "mode",
			"workspaceKey" };

	public static final String[] TICKERPAGESETTINGSTAGS = { "name", "origin",
			"detached", "layout", "showAlOrders", "showAccountOnlyOrders",
			"showOrdersPanel", "account" };

	public static final String[] SYSTEMSETTINGSTAGS = { "autoLogoffTime",
			"autoSaveSettings", "saveWatchlistsToCloud", "twoPendingPages" };
	
	public static final String[] UISETTINGS = { "browser", "audit", "autoRollover", "rapidStkEntry", "roundValues", "roundValuesOnAccountWindow", "doNotPromptForMdSubscr", "displayTrueTickVolume", "displayAbbreviatedVolume", "includeFxPositions", "includeOpeningPositionInPnL", "showAwayZeroPosition", "useTickDots", "enableOrderSort", "autoRemoveFinishedOrder", "delayForFilled", "delayForCanceled", "bigScaleSizes", "bigScalePrices" };

	public void addWorkSpaceKey(String key) {
		if (workSpaceKey == null) {
			workSpaceKey = new ArrayList<String>();
		}
		workSpaceKey.add(key);
	}

	public ArrayList<String> getCopyWorkSpaceKey() {
		return new ArrayList<String>(workSpaceKey);
	}
}
