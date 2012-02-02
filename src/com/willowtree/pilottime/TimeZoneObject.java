package com.willowtree.pilottime;

import java.util.Date;
import java.util.TimeZone;

public class TimeZoneObject {
    public String zoneID;
    public String regionDisplayName;
    public String fullDisplayName;
    public String zoneOffsetDisplay;

    //computes the GMT offset in milliseconds
    public long getDateOffset(){
        return TimeZone.getTimeZone(zoneID).getRawOffset();
    }

    public TimeZoneObject(String zID) {
        //super();
        zoneID = zID;
    }

    public TimeZoneObject(String zID, String rdn) {
        //super();
        zoneID = zID;
        regionDisplayName = rdn;
    }
    public TimeZoneObject(String zID, String rdn, String fdn) {
        //super();
        zoneID = zID;
        regionDisplayName = rdn;
        fullDisplayName = fdn;
    }
    public TimeZoneObject(String zID, String rdn, String fdn, String zod) {
        //super();
        zoneID = zID;
        regionDisplayName = rdn;
        fullDisplayName = fdn;
        zoneOffsetDisplay = zod;
    }
}
