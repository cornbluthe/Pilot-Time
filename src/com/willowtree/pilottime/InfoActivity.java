package com.willowtree.pilottime;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Created by IntelliJ IDEA.
 * User: george
 * Date: 1/31/12
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class InfoActivity extends Activity{
    private ImageView backBut;
    private TextView infoText;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infopage);
        
        ImageView backBut = (ImageView) findViewById(R.id.backToMainBut)  ;
        backBut.setOnClickListener(backButListener);
        
        TextView infoText = (TextView) findViewById(R.id.infoText);
        infoText.setText(Html.fromHtml(this.getString(R.string.infoText)))  ;
    }


    private OnClickListener backButListener = new OnClickListener(){
        public void onClick(View v) {
            onBackPressed();
        }
    };
}
