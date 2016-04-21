package com.ROKO.l2t;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseUser;
import com.rokomobi.sdk.ROKOLogger;
import com.rokomobi.sdk.analytics.Event;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
	}
	
	public void logout(View v){
		ParseUser.logOut();
		Intent login = new Intent(this , Login.class);
		startActivity(login);
		ROKOLogger.addEvent(new Event("User Logged Out"));
	}
}
