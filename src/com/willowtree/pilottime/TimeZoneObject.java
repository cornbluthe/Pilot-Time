package com.willowtree.pilottime;

import java.util.Date;

public class TimeZoneObject {
	private String regionName;
	private String zoneName;
	private String timeOffset;
	private String tla;
		
	//computes the GMT offset in milliseconds
	public long getDateOffset(){
		int temp = 0;
		if(timeOffset.contains("-")){
			temp = Integer.parseInt(timeOffset.substring(1)) * -1;
		}
		else if(timeOffset.contains("+")){
			temp = Integer.parseInt(timeOffset.substring(1));
		}
		long dateOffset = (long) (temp * 60 * 60 * 1000); 
		return dateOffset;
	}
	
	public String getZoneName() {
		return zoneName;
	}
	
	public String getRegionName() {
		return regionName;
	}
    
        public String getFormattedRegionandZone(){
            if (regionName=="UTC"){
                return "UTC";
            }
            else{
                return regionName + "|" + zoneName;
            }
        }

	public String getTimeOffset() {
		return timeOffset;
	}

	public String getTla() {
		if (tla==null) return "";
		return tla;
	}
	
	public String getZoneDesc(){
		if (tla!=null)
			return (tla + " | UTC " + timeOffset);
		else
			return ("UTC " + timeOffset);
	}
	
	public TimeZoneObject(String zoneName, String timeOffset, String tla, String regionName) {
		super();
		this.regionName = regionName;
		this.zoneName = zoneName;
		this.timeOffset = timeOffset;
		this.tla = tla;
	}
	
}
