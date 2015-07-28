package com.chen.OSU_Printing_Helper;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);


        TextView text0 = (TextView)findViewById(R.id.author_textview);
        text0.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));

        TextView text1 = (TextView)findViewById(R.id.name_textview);
        text1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));

        TextView text2 = (TextView)findViewById(R.id.email_textview);
        text2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));

        TextView text3 = (TextView)findViewById(R.id.info_textview);
        text3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));

        TextView text4 = (TextView)findViewById(R.id.link_textview);
        text4.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));

    }

}
