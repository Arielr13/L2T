package com.ROKO.l2t;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.FacebookSdk;
//import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.rokomobi.sdk.ROKOLogger;
import com.rokomobi.sdk.RokoMobi;
import com.rokomobi.sdk.analytics.Event;

import android.os.Vibrator;
import bolts.*;

public class L2T extends Application { 

    @Override public void onCreate() { 
        super.onCreate();
        
        //Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "oK6Y7Ue9CphZnjUCgUxhHdQv67WunWBNAjOO85Yc", "JqAmYL6eYW7vCKBqqRmvjguvKVI71d2bl2Sk36Ta");
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        
        
        RokoMobi.start(this);
        
        //Roko Analytics
        ROKOLogger.addEvent(new Event("App Initialized"));
    }
} 