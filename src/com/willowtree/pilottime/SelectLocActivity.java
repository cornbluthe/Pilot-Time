package com.willowtree.pilottime;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectLocActivity extends Activity {
    private String mType;
    private ImageView backHomeButton;
    private RelativeLayout utcButton;
    private RelativeLayout localButton;
    private ArrayList<TimeZoneObject> m_zones = null;
    private ZoneAdapter m_adapter;
    private Runnable viewZones;
    private ListView mListView;
    private PilotTimeApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectloc);

        utcButton = (RelativeLayout) findViewById(R.id.jumpto_utc_button);
        localButton = (RelativeLayout) findViewById(R.id.jumpto_local_button);
        utcButton.setOnClickListener(utcButtonListener);
        localButton.setOnClickListener(localButtonListener);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mType = extras.getString("type");
        }

        application = ((PilotTimeApplication) getApplication());

        m_zones = new ArrayList<TimeZoneObject>();

        this.m_adapter = new ZoneAdapter(this, R.layout.timeentry, m_zones);

        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(m_adapter);
        mListView.setOnItemClickListener(mListener);

        //button to go back to main
        backHomeButton = (ImageView) findViewById(R.id.back_button);
        backHomeButton.setOnClickListener(backButtonListener);

        getZones();
    }

    OnClickListener utcButtonListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //application.setTimeZone(new TimeZoneObject("", "", "", "UTC"), mType);
            //finish();
            //overridePendingTransition(R.anim.itemmovedown, R.anim.itemmovedown2);
        }
    };

    OnClickListener localButtonListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //application.setTimeZone(new TimeZoneObject("", "", "", "UTC"), mType);
            //finish();
            //overridePendingTransition(R.anim.itemmovedown, R.anim.itemmovedown2);
        }
    };

    OnItemClickListener mListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> a, View v, int position,
                long id) {
            application.setTimeZone(m_zones.get(position), mType);
            finish();
            overridePendingTransition(R.anim.itemmovedown, R.anim.itemmovedown2);
        }
    };

    private OnClickListener backButtonListener = new OnClickListener() {
        public void onClick(View v) {
            Intent mainIntent = new Intent(v.getContext(),
                    PilotTimeActivity.class);
            startActivity(mainIntent);
            finish();
            overridePendingTransition(R.anim.itemmovedown, R.anim.itemmovedown2);
        }
    };

    private void getZones() {
            String[] listItems = TimeZone.getAvailableIDs();
            TimeZone tz = null;
            for (int i = 0; i < listItems.length; i++) {
                tz = TimeZone.getTimeZone(listItems[i]);
                m_zones.add(new TimeZoneObject(listItems[i], getRegionDisplayName(tz),
                        getFullDisplayName(tz), getZoneOffsetDisplay(tz)));
            }
    }

    private class ZoneAdapter extends ArrayAdapter<TimeZoneObject> {
        private ArrayList<TimeZoneObject> items;

        public ZoneAdapter(Context context, int textViewResourceId,
                ArrayList<TimeZoneObject> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.timeentry, null);
            }
            TimeZoneObject z = items.get(position);
            if (z != null) {
                TextView zoneName = (TextView) v.findViewById(R.id.location);
                TextView zoneTla = (TextView) v.findViewById(R.id.zone);

                zoneName.setText(z.fullDisplayName);
                zoneTla.setText(z.zoneOffsetDisplay);
            }
            return v;
        }
    }
    private String getRegionDisplayName(TimeZone tz){
        String timezonename = tz.getID();
        String displayname = timezonename;
        int sep = timezonename.indexOf('/');

        if(-1 != sep)
        {
            displayname = timezonename.substring(sep + 1);
            displayname = displayname.replace("_", " ");
        }
        return displayname;
    }

    private String getFullDisplayName(TimeZone tz){
        String timezonename = tz.getID();
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
