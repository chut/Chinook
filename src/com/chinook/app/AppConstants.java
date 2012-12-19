package com.chinook.app;

import com.chinook.activities.AppPreferencesActivity;
import com.chinook.activities.Entry;
import com.chinook.activities.MapViewActivity;
import com.chinook.activities.ShareRouteActivity;
//import com.chinook.activities.WebViewActivity;
import com.chinook.activities.TestActivity;

public final class AppConstants {
	public static final String GLOBAL_IMG_STORAGE_LOC = ""; //some location on android storage
	public static final String GLOBAL_WEB_SERVER_ADDRESS = "wayfinder.mapsdb.com";
	public static final String GLOBAL_WEB_SITE = "http://" + GLOBAL_WEB_SERVER_ADDRESS + "/cs460spr2012/mobile/route.jsp";
	public static final boolean GLOBAL_METHOD_TRACING = false;	// not used at this time
	public static final boolean GLOBAL_DEBUGGING = false;	// not used at this time
	
//	public static final Class CLASS_WEBVIEW = WebViewActivity.class;
	public static final Class CLASS_PREF = AppPreferencesActivity.class;
	public static final Class CLASS_ENTRY = Entry.class;
	public static final Class CLASS_SHARE = ShareRouteActivity.class;
	//public static final Class CLASS_MAP = MapViewActivity.class;
	public static final Class CLASS_MAP = TestActivity.class;

	
	// database connection providers
	public static final int PROVIDER_EXT_HTTP_APACHE = 1;	
	public static final int PROVIDER_EXT_SOCKET = 2;
	public static final int PROVIDER_INT_SQLITE = 3;
	public static final int DATABASE_PROVIDER_ALGORITHM = PROVIDER_INT_SQLITE;	// this is where we set which database the algorithm will use
	public static final int DATABASE_PROVIDER_UI = PROVIDER_INT_SQLITE;				// this is where we set which database UI functions will use
		
	// progress options
	public static final int PROGRESS_NONE = 0;
	public static final int PROGRESS_BAR = 1;
	public static final int PROGRESS_BAR_INDETERMINATE = 2;
	public static final int PROGRESS_DIALOG = 3;
	
	// for fling gestures
	public static final int SWIPE_MIN_DISTANCE = 120;
	public static final int SWIPE_MAX_OFF_PATH = 250;
	public static final int SWIPE_THRESHOLD_VELOCITY = 200;
		
}
