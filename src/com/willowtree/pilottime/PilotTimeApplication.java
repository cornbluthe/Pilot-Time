package com.willowtree.pilottime;

import android.app.Application;
import android.content.SharedPreferences;

public class PilotTimeApplication extends Application {
	private SharedPreferences mSharedPrefs;
	private SharedPreferences.Editor mPrefsEditor;

	@Override
	public void onCreate() {
		mSharedPrefs = getSharedPreferences("pilottime", 0);
		mPrefsEditor = mSharedPrefs.edit();
	}

	public void setTimeZone(TimeZoneObject o) {
		mPrefsEditor.putString("zone", o.getZoneName());
		mPrefsEditor.putString("region", o.getRegionName());
		mPrefsEditor.putString("tla", o.getTla());
		mPrefsEditor.putString("offset", o.getTimeOffset());
		mPrefsEditor.commit();
	}

	public TimeZoneObject getTimeZone() {
		String zone = mSharedPrefs.getString("zone", "Eastern");
		String region = mSharedPrefs.getString("region", "-5");
		String tla = mSharedPrefs.getString("tla", "EST");
		String offset = mSharedPrefs.getString("offset", "US");
		TimeZoneObject o = new TimeZoneObject(zone, offset, tla, region);
		return o;
	}
}