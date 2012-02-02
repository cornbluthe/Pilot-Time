package com.willowtree.pilottime;

import android.app.Application;
import android.content.SharedPreferences;
import java.util.Date;

public class PilotTimeApplication extends Application {
	private SharedPreferences mSharedPrefs;
	private SharedPreferences.Editor mPrefsEditor;

	@Override
	public void onCreate() {
		mSharedPrefs = getSharedPreferences("pilottime", 0);
		mPrefsEditor = mSharedPrefs.edit();
	}

	public void setTimeZone(TimeZoneObject o, String type) {
		mPrefsEditor.putString(type + "zone", o.getZoneName());
		mPrefsEditor.putString(type + "region", o.getRegionName());
		mPrefsEditor.putString(type + "tla", o.getTla());
		mPrefsEditor.putString(type + "offset", o.getTimeOffset());
		mPrefsEditor.commit();
	}
                                      
	public TimeZoneObject getTimeZone(String type) {
		String zone = mSharedPrefs.getString(type + "zone", "Eastern");
		String region = mSharedPrefs.getString(type + "region", "-5");
		String tla = mSharedPrefs.getString(type + "tla", "EST");
		String offset = mSharedPrefs.getString(type + "offset", "US");
		TimeZoneObject o = new TimeZoneObject(zone, offset, tla, region);
		return o;
	}
}