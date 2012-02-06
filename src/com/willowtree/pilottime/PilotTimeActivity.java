package com.willowtree.pilottime;

import com.willowtree.pilottime.dateslider.DateSlider;
import com.willowtree.pilottime.dateslider.DateTimeMinSlider;
import com.willowtree.pilottime.dateslider.DateTimeSlider;
import com.willowtree.pilottime.dateslider.TimeSlider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PilotTimeActivity extends Activity {
    private Context mContext;
    private Button locationSelectBut;
    private ImageView switchBut;
    private ImageView infoBut;
    private ImageView converterFooterBut;
    private ImageView timeFooterBut;
    private LinearLayout baseTimeBut;
    private LinearLayout resultTimeBut;
    private LinearLayout timeScreenLayout;
    private RelativeLayout converterScreenLayout;
    private LinearLayout baseTimeButDateSlider;
    private TimeZoneObject currentZone;		    //zone that is displayed on main screen
    private TimeZoneObject currentZoneBase;		//base zone that is displayed on converter screen
    private TimeZoneObject currentZoneResult;   //result zone that is displayed on converter screen
    private static long UTCOFFSET=18000000l;
    private static PilotTimeApplication application;
    private boolean onTime;
    private long currentBaseTime;
    private Date currentResultTime;
    private ImageView turningDial;
    private Animation turnLeftFromMid, turnLeft, turnRight;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mContext = this;
        
        currentBaseTime = System.currentTimeMillis(); //default to current time

        onTime = true; //'time' screen is default, false is 'converter' screen

        application = ((PilotTimeApplication) getApplication());

        //button that brings up timezone selector
        locationSelectBut = (Button) findViewById(R.id.location_select_button);
        locationSelectBut.setOnClickListener(locationSelectListener);

        //button that brings up info page
        infoBut = (ImageView) findViewById(R.id.infoButton);
        infoBut.setOnClickListener(infoButListener);

        //button that brings up the converter
        converterFooterBut = (ImageView) findViewById(R.id.converter_footer_light);
        converterFooterBut.setOnClickListener(converterFooterButtonListener);

        //button for base time selector on converter
        baseTimeBut = (LinearLayout) findViewById(R.id.button_convert_left);
        baseTimeBut.setOnClickListener(baseTimeButListener);

        //button for result time selector on converter
        resultTimeBut = (LinearLayout) findViewById(R.id.button_convert_right);
        resultTimeBut.setOnClickListener(resultTimeButListener);

        //button for dateslider
        baseTimeButDateSlider = (LinearLayout) findViewById(R.id.button_basetime_convert);
        baseTimeButDateSlider.setOnClickListener(baseTimeButDateSliderListener);

        timeFooterBut = (ImageView) findViewById(R.id.time_footer_light);
        timeFooterBut.setOnClickListener(timeFooterButtonListener);

        timeScreenLayout = (LinearLayout) findViewById(R.id.bodyTime);
        converterScreenLayout = (RelativeLayout) findViewById(R.id.bodyConverter);

        switchBut = (ImageView) findViewById(R.id.button_switch);
        switchBut.setOnClickListener(switchButListener);
        
        turningDial = (ImageView) findViewById(R.id.turning_dial);
        turnLeftFromMid = AnimationUtils.loadAnimation(PilotTimeActivity.this, R.anim.turn_left_from_mid);
        turnRight = AnimationUtils.loadAnimation(PilotTimeActivity.this, R.anim.turn_right);
        turnLeft = AnimationUtils.loadAnimation(PilotTimeActivity.this, R.anim.turn_left);

        //rotate left from middle to "Time" selection
        turningDial.startAnimation(turnLeftFromMid);

        //set defaults
        TimeZone defaultZone = TimeZone.getDefault();
        application.setTimeZone(new TimeZoneObject(defaultZone.getID()),PilotTimeRefs.BASE_TYPE);
        application.setTimeZone(new TimeZoneObject(defaultZone.getID()),PilotTimeRefs.RESULT_TYPE);
        application.setTimeZone(new TimeZoneObject(defaultZone.getID()),PilotTimeRefs.MAIN_TYPE);
    }

    private OnClickListener switchButListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            application.setTimeZone(currentZoneBase, PilotTimeRefs.RESULT_TYPE);
            application.setTimeZone(currentZoneResult, PilotTimeRefs.BASE_TYPE);
            onResume();
        }
    };

    private OnClickListener baseTimeButDateSliderListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            DateTimeMinSlider dtms = new DateTimeMinSlider(mContext, new DateSlider.OnDateSetListener() {
                @Override
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    currentBaseTime = selectedDate.getTimeInMillis();
                    onResume();
                }
            }, Calendar.getInstance(), 1);
            dtms.show();
        }
    };

    private OnClickListener infoButListener = new OnClickListener() {
        public void onClick(View view) {
            Intent infoIntent = new Intent(view.getContext(), InfoActivity.class);
            startActivity(infoIntent);
        }
    };

    private OnClickListener converterFooterButtonListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(onTime){
                //switch body views
                timeScreenLayout.setVisibility(view.GONE);
                converterScreenLayout.setVisibility(view.VISIBLE);
                //switch which light is 'on'
                converterFooterBut.setImageDrawable(getResources().getDrawable(R.drawable.footer_light_focus));
                timeFooterBut.setImageDrawable(getResources().getDrawable(R.drawable.footer_light));
                //turn dial to the right
                turningDial.startAnimation(turnRight);
            }
            onTime = false; //we are not on the 'time' screen anymore

        }
    };

    private OnClickListener timeFooterButtonListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!onTime){
                //switch body views
                converterScreenLayout.setVisibility(view.GONE);
                timeScreenLayout.setVisibility(view.VISIBLE);
                //switch which light is 'on'
                converterFooterBut.setImageDrawable(getResources().getDrawable(R.drawable.footer_light));
                timeFooterBut.setImageDrawable(getResources().getDrawable(R.drawable.footer_light_focus));
                //turn dial to the left
                turningDial.startAnimation(turnLeft);
            }
            onTime = true;    //we are back on the 'time' screen
        }
    };

    //'main' page location selector
    private OnClickListener locationSelectListener = new OnClickListener(){
        public void onClick(View v){
            //launch location select activity
            Intent locIntent = new Intent(v.getContext(), SelectLocActivity.class);
            locIntent.putExtra("type", PilotTimeRefs.MAIN_TYPE);
            startActivity(locIntent);
            overridePendingTransition(R.anim.itemmoveup, R.anim.itemmoveup2);
        }
    };

    //'converter' page, base location selector
    private OnClickListener baseTimeButListener = new OnClickListener(){
        public void onClick(View v){
            //launch location select activity
            Intent locIntent = new Intent(v.getContext(), SelectLocActivity.class);
            locIntent.putExtra("type", PilotTimeRefs.BASE_TYPE);
            startActivity(locIntent);
            overridePendingTransition(R.anim.itemmoveup, R.anim.itemmoveup2);
        }
    };

    //'converter' page, result location selector
    private OnClickListener resultTimeButListener = new OnClickListener(){
        public void onClick(View v){
            //launch location select activity
            Intent locIntent = new Intent(v.getContext(), SelectLocActivity.class);
            locIntent.putExtra("type", PilotTimeRefs.RESULT_TYPE);
            startActivity(locIntent);
            overridePendingTransition(R.anim.itemmoveup, R.anim.itemmoveup2);
        }
    };

    private Handler mHandler = new Handler();

    private Runnable mUpdateTime = new Runnable(){
        public void run(){
            updateTime();
            mHandler.postDelayed(this, 1000);
        }
    };

    private void updateTime() {
        //textviews containing timezone info [??-HR ZON | UTC +-?]
        final TextView topTimeText1= (TextView) findViewById(R.id.top_time_text1);
        final TextView topTimeText2= (TextView) findViewById(R.id.top_time_text2);

        //textviews containing time [00:00:00]
        final TextView bottomTimeText1= (TextView) findViewById(R.id.bottom_time_text1);
        final TextView bottomTimeText2= (TextView) findViewById(R.id.bottom_time_text2);
        final TextView bottomTimeText3= (TextView) findViewById(R.id.bottom_time_text3);

        //textviews containing [DAY, MON]
        final TextView dayMonthText1= (TextView) findViewById(R.id.day_mo_text1);
        final TextView dayMonthText2= (TextView) findViewById(R.id.day_mo_text2);
        final TextView dayMonthText3= (TextView) findViewById(R.id.day_mo_text3);

        //textviews containing number of month
        final TextView dayText1 = (TextView) findViewById(R.id.day_text1);
        final TextView dayText2 = (TextView) findViewById(R.id.day_text2);
        final TextView dayText3 = (TextView) findViewById(R.id.day_text3);

        //compute utc time and time for current timezone
        Date now = new Date();

        //format times to be displayed
        final SimpleDateFormat formatterTime1 = new SimpleDateFormat( "hh:mm:ss a" );
        formatterTime1.setTimeZone(TimeZone.getTimeZone(currentZone.zoneID));
        final SimpleDateFormat formatterTime2 = new SimpleDateFormat( "kk:mm:ss" );
        formatterTime2.setTimeZone(TimeZone.getTimeZone(currentZone.zoneID));
        final SimpleDateFormat formatterTime3 = new SimpleDateFormat( "kk:mm:ss" );
        formatterTime3.setTimeZone(TimeZone.getTimeZone("UTC"));
        final SimpleDateFormat formatterTime4 = new SimpleDateFormat( "EEE, MMM");
        formatterTime4.setTimeZone(TimeZone.getTimeZone(currentZone.zoneID));
        final SimpleDateFormat formatterTime5 = new SimpleDateFormat( "d");
        formatterTime5.setTimeZone(TimeZone.getTimeZone(currentZone.zoneID));
        final SimpleDateFormat formatterTime6 = new SimpleDateFormat( "EEE, MMM");
        formatterTime6.setTimeZone(TimeZone.getTimeZone(currentZone.zoneID));
        final SimpleDateFormat formatterTime7 = new SimpleDateFormat( "d");
        formatterTime7.setTimeZone(TimeZone.getTimeZone("UTC"));

        topTimeText1.setText( "12-HR " + currentZone.zoneOffsetDisplay );
        topTimeText2.setText( "24-HR " + currentZone.zoneOffsetDisplay );
        bottomTimeText1.setText( formatterTime1.format(now) );
        bottomTimeText2.setText( formatterTime2.format(now) );
        bottomTimeText3.setText( formatterTime3.format(now));
        dayMonthText1.setText( formatterTime4.format(now));
        dayMonthText2.setText( formatterTime4.format(now));
        dayMonthText3.setText( formatterTime4.format(now));
        dayText1.setText(formatterTime5.format(now));
        dayText2.setText(formatterTime5.format(now));
        dayText3.setText(formatterTime5.format(now));
    }

    public void updateConverterDisplay(){
        final TextView baseTimeConvertTopText = (TextView) findViewById(R.id.button_convert_left_top_text);
        final TextView baseTimeConvertBottomText = (TextView) findViewById(R.id.button_convert_left_bottom_text);
        final TextView resultTimeConvertTopText = (TextView) findViewById(R.id.button_convert_right_top_text);
        final TextView resultTimeConvertBottomText = (TextView) findViewById(R.id.button_convert_right_bottom_text);

        baseTimeConvertTopText.setText(currentZoneBase.regionDisplayName);
        if(currentZoneBase.zoneID=="UTC")
            baseTimeConvertBottomText.setText("");
        else
            baseTimeConvertBottomText.setText(currentZoneBase.zoneOffsetDisplay);
        resultTimeConvertTopText.setText(currentZoneResult.regionDisplayName);

        if(currentZoneResult.zoneID=="UTC")
            resultTimeConvertBottomText.setText("");
        else
            resultTimeConvertBottomText.setText(currentZoneResult.zoneOffsetDisplay);

    }

    public void updateConverterBaseTime(){
        final TextView baseTimeTopText = (TextView) findViewById(R.id.button_basetime_top_text);
        final TextView baseTimeBottomText = (TextView) findViewById(R.id.button_basetime_bottom_text);
        final SimpleDateFormat timeFormat = new SimpleDateFormat( "MM/dd/yyyy");
        final SimpleDateFormat timeFormat2 = new SimpleDateFormat( "hh:mma");

        final Date currentBaseTimeDisplay = new Date(currentBaseTime);
        baseTimeTopText.setText(timeFormat.format(currentBaseTimeDisplay));
        baseTimeBottomText.setText(timeFormat2.format(currentBaseTimeDisplay));
    }

    public void updateConverterResultTime(){
        final TextView resultTimeTopText = (TextView) findViewById(R.id.right_timebox_top_text);
        final TextView resultTimeBottomText = (TextView) findViewById(R.id.right_timebox_bottom_text);
        final SimpleDateFormat timeFormat = new SimpleDateFormat( "MM/dd/yyyy");
        final SimpleDateFormat timeFormat2 = new SimpleDateFormat( "hh:mma");

        resultTimeTopText.setText(timeFormat.format(currentResultTime));
        resultTimeBottomText.setText(timeFormat2.format(currentResultTime));
    }

    private Date getResultTime(long bTime){
        final Date theTime = new Date(bTime-currentZoneBase.getDateOffset()+currentZoneResult.getDateOffset());
        return  theTime;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mUpdateTime);
    }

    @Override
    protected void onResume() {
        super.onResume();

        currentZone = application.getTimeZone(PilotTimeRefs.MAIN_TYPE);
        currentZoneBase = application.getTimeZone(PilotTimeRefs.BASE_TYPE);
        currentZoneResult = application.getTimeZone(PilotTimeRefs.RESULT_TYPE);
        currentResultTime = getResultTime(currentBaseTime);

        updateTime();
        updateConverterDisplay();
        updateConverterBaseTime();
        updateConverterResultTime();

        mHandler.removeCallbacks(mUpdateTime);
        mHandler.postDelayed(mUpdateTime, 1000);
        locationSelectBut.setText(currentZone.fullDisplayName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mHandler != null )
            mHandler.removeCallbacks(mUpdateTime);
        mHandler = null;
    }
}