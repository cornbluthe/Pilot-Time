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
        regionDisplayName = getRegionDisplayName(zID);
        fullDisplayName = getFullDisplayName(zID);
        zoneOffsetDisplay = getZoneOffsetDisplay(TimeZone.getTimeZone(zID));
    }

    public TimeZoneObject(String zID, String rdn) {
        //super();
        zoneID = zID;
        regionDisplayName = rdn;
        fullDisplayName = getFullDisplayName(zID);
        zoneOffsetDisplay = getZoneOffsetDisplay(TimeZone.getTimeZone(zID));
    }
    public TimeZoneObject(String zID, String rdn, String fdn) {
        //super();
        zoneID = zID;
        regionDisplayName = rdn;
        fullDisplayName = fdn;
        zoneOffsetDisplay = getZoneOffsetDisplay(TimeZone.getTimeZone(zID));
    }
    public TimeZoneObject(String zID, String rdn, String fdn, String zod) {
        //super();
        zoneID = zID;
        regionDisplayName = rdn;
        fullDisplayName = fdn;
        zoneOffsetDisplay = zod;
    }
    private String getRegionDisplayName(String tzid){
        String timezonename = tzid;
        String displayname = timezonename;
        int sep = timezonename.indexOf('/');

        if(-1 != sep)
        {
            displayname = timezonename.substring(sep + 1);
            displayname = displayname.replace("_", " ");
        }
        return displayname;
    }

    private String getFullDisplayName(String tzid){
        String timezonename = tzid;
        String displayname = timezonename;
        int sep = timezonename.indexOf('/');

        if(-1 != sep)
        {
            displayname = timezonename.substring(0,sep) + ", " + timezonename.substring(sep + 1);
            displayname = displayname.replace("_", " ");
        }

        return displayname;
    }

    private String getZoneOffsetDisplay(TimeZone tz){
        long rawOffsetHrs = tz.getRawOffset()/(1000*60*60);
        String display = "";
        if(rawOffsetHrs>0)
            display="+ " + rawOffsetHrs;
        else if (rawOffsetHrs<0)
            display="- " + -1*rawOffsetHrs;
        int x = tz.SHORT;
        if (x==0)
            return  "UTC " + display;
        else
            return x + " | UTC " + display;
    }
}
