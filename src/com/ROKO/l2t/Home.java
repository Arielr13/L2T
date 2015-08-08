package com.ROKO.l2t;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
	}
	
	public void goStudy(View v) {
		Intent study = new Intent(this, Study.class);
		startActivity(study);
	}
	public void goTrain(View v) {
		Toast toast = Toast.makeText(getApplicationContext(), "Train", Toast.LENGTH_SHORT);
		toast.show();
	}
	public void goChallenge(View v) {
		Toast toast = Toast.makeText(getApplicationContext(), "Challenge", Toast.LENGTH_SHORT);
		toast.show();
	}
	public void goFriends(View v) {
		Toast toast = Toast.makeText(getApplicationContext(), "Friends", Toast.LENGTH_SHORT);
		toast.show();
	}
	public void goSettings(View v) {
		Intent settings = new Intent(this, Settings.class);
		startActivity(settings);
	}
	public void goUser(View v){
		Toast toast = Toast.makeText(getApplicationContext(), "User", Toast.LENGTH_SHORT);
		toast.show();
	}
	
}
