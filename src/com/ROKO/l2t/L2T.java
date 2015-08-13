package com.ROKO.l2t;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject; 

public class L2T extends Application { 

    @Override public void onCreate() { 
        super.onCreate();
        
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "oK6Y7Ue9CphZnjUCgUxhHdQv67WunWBNAjOO85Yc", "JqAmYL6eYW7vCKBqqRmvjguvKVI71d2bl2Sk36Ta");
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
} 