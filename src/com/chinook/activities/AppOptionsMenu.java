package com.chinook.activities;

import com.chinook.app.AppConstants;
import com.chinook.app.io.AppIO;
import com.chinook.app.io.DatabaseConstants;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public final class AppOptionsMenu {

//	static final int HOME = Menu.FIRST + 0;
//	static final int MAP = Menu.FIRST + 1;
	static final int SETTINGS = Menu.FIRST + 0;
//	static final int INFO = Menu.FIRST + 3;
//	static final int HELP = Menu.FIRST + 4;
//	static final int SMS = Menu.FIRST + 5;
	static final int SYNC = Menu.FIRST + 1;
	static final int QUIT = Menu.FIRST + 2;

	private static final AppIO appIO = new AppIO(1);

	public AppOptionsMenu() {

	}

	public static void buildOptionMenu(Menu menu) {
//		MenuItem item1 = menu.add(0, HOME, Menu.NONE, "Home").setIcon(R.drawable.ic_menu_home);
//		MenuItem item2 = menu.add(0, MAP, Menu.NONE, "Map").setIcon(R.drawable.ic_menu_compass);
		MenuItem item3 = menu.add(0, SETTINGS, Menu.NONE, "Settings").setIcon(R.drawable.ic_menu_preferences);
//		MenuItem item4 = menu.add(0, INFO, Menu.NONE, "Info").setIcon(R.drawable.ic_menu_info_details);
//		MenuItem item5 = menu.add(0, HELP, Menu.NONE, "Call Help").setIcon(R.drawable.ic_menu_call);
//		MenuItem item6 = menu.add(0, SMS, Menu.NONE, "Share Route Info").setIcon(R.drawable.ic_menu_send);
		MenuItem item7 = menu.add(0, SYNC, Menu.NONE, "Sync Database").setIcon(R.drawable.ic_menu_refresh);
		MenuItem item8 = menu.add(0, QUIT, Menu.NONE, "Exit Application").setIcon(R.drawable.ic_menu_close_clear_cancel);

	}

	public static boolean handleMenuSelection(MenuItem item, Activity activity,	String startID, String endID, Entry entryActivity) {
		int itemID = item.getItemId(); // get id of menu item picked

		switch (itemID) {
//		case HOME:
//			Log.i("MENU","class: " + activity.getClass().getSimpleName() + ", HOME_CLASS: "	+ AppConstants.CLASS_ENTRY.getSimpleName());
//			if (!activity.getClass().getSimpleName().toString().equals(AppConstants.CLASS_ENTRY.getSimpleName().toString())) {
//				Intent intent1 = new Intent(activity, AppConstants.CLASS_ENTRY);
//				activity.startActivity(intent1);
//			}
//			return true;
//		case MAP:
//			Log.i("MENU","class: " + activity.getClass().getSimpleName() + ", CLASS_ENTRY: " + AppConstants.CLASS_ENTRY.getSimpleName());
//			if (!activity.getClass().getSimpleName().toString().equals(AppConstants.CLASS_ENTRY.getSimpleName().toString())) {
//				Intent intent2 = new Intent(activity, AppConstants.CLASS_ENTRY);
//				intent2.putExtra("mode", "browse");
//				intent2.putExtra("bldg", AppPrefs.getBrowseBldg(activity));
//				intent2.putExtra("tab", 3);
//				activity.startActivity(intent2);
//			} else {
//				if (entryActivity != null) entryActivity.changeTabs(3);
//			}
//			return true;
//		case SMS:
//			Log.i("MENU","class: " + activity.getClass().getSimpleName() + ", SHARE_CLASS: "	+ AppConstants.CLASS_SHARE.getSimpleName());
//			if (!activity.getClass().getSimpleName().toString().equals(AppConstants.CLASS_SHARE.getSimpleName().toString())) {
//				Intent intent1 = new Intent(activity, AppConstants.CLASS_SHARE);
//				activity.startActivity(intent1);
//			}
//			return true;
//		case HELP:
//			Uri uri1 = Uri.parse("tel:" + AppPrefs.getDefaultPhone(activity));
//			Intent intent3 = new Intent(Intent.ACTION_VIEW, uri1);
//			activity.startActivity(intent3);
//			return true;
		case SETTINGS:
			Log.i("MENU","class: " + activity.getClass().getSimpleName() + ", PREF_CLASS: "	+ AppConstants.CLASS_PREF.getSimpleName());
			if (!activity.getClass().getSimpleName().toString().equals(AppConstants.CLASS_PREF.getSimpleName().toString())) {
				Intent intent5 = new Intent(activity, AppConstants.CLASS_PREF);
				activity.startActivity(intent5);
			}
			return true;
		case QUIT:
			// activity.finish();
			activity.moveTaskToBack(true);
			return true;
		case SYNC:
			appIO.syncDatabase_async(AppConstants.PROGRESS_DIALOG, activity);
			return true;
//		case INFO:
//			Uri uri3 = Uri.parse("http://www.massgeneral.org/contact/default.aspx");
//        	Intent i3 = new Intent(Intent.ACTION_VIEW,uri3);
//            activity.startActivity(i3);
//			return true;
		default:
			return false;
		}
	}
}
