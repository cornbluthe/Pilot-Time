package com.willowtree.pilottime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
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
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infopage);
        
        ImageView backBut = (ImageView) findViewById(R.id.backToMainBut)  ;
        backBut.setOnClickListener(backButListener);

        ImageView wtaLink = (ImageView) findViewById(R.id.wtalink);
        wtaLink.setOnClickListener(wtaLinkListener);
        
        TextView infoText = (TextView) findViewById(R.id.infoText);
        infoText.setText(Html.fromHtml(this.getString(R.string.infoText)))  ;
        
        TextView shareButton = (TextView) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(shareButtonListener);
    }

    private OnClickListener wtaLinkListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse("http://www.willowtreeapps.com/mobile");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    };

    private OnClickListener backButListener = new OnClickListener(){
        public void onClick(View v) {
            onBackPressed();
        }
    };
    
    private OnClickListener shareButtonListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "market://details?id=com.willowtreeapps.pilottime";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pilot Time App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    };
            
}
