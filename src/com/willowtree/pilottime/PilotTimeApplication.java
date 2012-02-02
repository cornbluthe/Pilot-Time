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
        mPrefsEditor.putString(type + "id", o.zoneID);
        mPrefsEditor.putString(type + "region", o.regionDisplayName);
        mPrefsEditor.putString(type + "fullname", o.fullDisplayName);
        mPrefsEditor.putString(type + "zoneoffset", o.zoneOffsetDisplay);
        mPrefsEditor.commit();
    }

    public TimeZoneObject getTimeZone(String type) {
        String zoneId = mSharedPrefs.getString(type + "id", "UTC");
        String rdn = mSharedPrefs.getString(type + "region", "UTC");
        String fdn = mSharedPrefs.getString(type + "fullname", "UTC");
        String zod = mSharedPrefs.getString(type + "zoneoffset", "0");
        TimeZoneObject o = new TimeZoneObject(zoneId,rdn,fdn,zod);
        return o;
    }
}