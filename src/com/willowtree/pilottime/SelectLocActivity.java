package com.willowtree.pilottime;

import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectLocActivity extends Activity {
	private ImageView backHomeButton;
	private ArrayList<TimeZoneObject> m_zones = null;
	private ZoneAdapter m_adapter;
	private Runnable viewZones;
	private ListView mListView;
	private PilotTimeApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectloc);
		
		application = ((PilotTimeApplication) getApplication());

		m_zones = new ArrayList<TimeZoneObject>();
		this.m_adapter = new ZoneAdapter(this, R.layout.timeentry, m_zones);

		viewZones = new Runnable() {
			@Override
			public void run() {
				getZones();
			}
		};

		mListView = (ListView) findViewById(R.id.list);
		mListView.setAdapter(m_adapter);
		mListView.setOnItemClickListener(mListener);

		// declare button
		backHomeButton = (ImageView) findViewById(R.id.back_button);
		backHomeButton.setOnClickListener(backButtonListener);

		Thread thread = new Thread(null, viewZones, "populateZones");
		thread.start();
	}

	OnItemClickListener mListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> a, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			application.setTimeZone(m_zones.get(position));
			finish();
		}
	};

	private OnClickListener backButtonListener = new OnClickListener() {
		public void onClick(View v) {
			// launch location select activity
			Intent mainIntent = new Intent(v.getContext(),
					PilotTimeActivity.class);
			startActivity(mainIntent);
			// TODO get selection and set as result
			// setResult(SELECTION,mainIntent);
			finish();
		}
	};

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (m_zones != null && m_zones.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < m_zones.size(); i++)
					m_adapter.add(m_zones.get(i));
			}
			m_adapter.notifyDataSetChanged();
		}
	};

	private void getZones() {
		try {
			m_zones = new ArrayList<TimeZoneObject>();
			// TODO: create TimeZone arrayList based on arrays.xml
			String[] regions = getResources().getStringArray(R.array.regions);
			String[] zones = getResources().getStringArray(R.array.zones);
			String[] offsets = getResources().getStringArray(R.array.offsets);
			String[] tla = getResources().getStringArray(R.array.tla);
			for (int i = 0; i < regions.length; i++) {
				TimeZoneObject x = new TimeZoneObject(zones[i], offsets[i],
						tla[i], regions[i]);
				m_zones.add(x);
			}
			// Thread.sleep(5000);
			Log.i("ARRAY", "" + m_zones.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
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
				if (zoneName != null) {
					zoneName.setText(z.getZoneName());
				}
				if (zoneTla != null) {
					zoneTla.setText(z.getZoneDesc());
				}
			}
			return v;
		}
	}
}
