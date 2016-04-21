package com.ROKO.l2t;

import com.parse.ParseUser;
import com.rokomobi.sdk.ROKOLogger;
import com.rokomobi.sdk.RokoMobi;
import com.rokomobi.sdk.analytics.Event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		ParseUser user = ParseUser.getCurrentUser();
		
		TextView username =(TextView)findViewById(R.id.UsernameField);
		username.setText(user.getString("username"));
		
		ImageView avatar = (ImageView)findViewById(R.id.Avatar);
		ParseUser currentUser = ParseUser.getCurrentUser();
		Bitmap bmp = BitmapFactory.decodeByteArray(currentUser.getBytes("avatar"), 0, currentUser.getBytes("avatar").length);
	    avatar.setImageBitmap(bmp);

	    //ROKOLogger.addEvent(new Event("Open Home"), new Event("Test"));
	}
	
	@Override
	public void onResume(){
		super.onResume();
		ImageView avatar = (ImageView)findViewById(R.id.Avatar);
		ParseUser currentUser = ParseUser.getCurrentUser();
		Bitmap bmp = BitmapFactory.decodeByteArray(currentUser.getBytes("avatar"), 0, currentUser.getBytes("avatar").length);
	    avatar.setImageBitmap(bmp);
	}
	
	public void goStudy(View v) {
		Intent study = new Intent(this, Study.class);
		startActivity(study);
		ROKOLogger.addEvent(new Event("User Opened 'Study'"));
	}
	public void goTrain(View v) {
		Intent train = new Intent(this, LevelSelect.class);
		startActivity(train);
		ROKOLogger.addEvent(new Event("User Opened 'Train'"));
	}
	public void goChallenge(View v) {
		Intent challenge = new Intent(this, Challenge.class);
		startActivity(challenge);
		ROKOLogger.addEvent(new Event("User Opened 'Challenges'"));
	}
	public void goFriends(View v) {
		Intent friends = new Intent(this, Friends.class);
		startActivity(friends);
		ROKOLogger.addEvent(new Event("User Opened 'Friends'"));
	}
	public void goSettings(View v) {
		Intent settings = new Intent(this, Settings.class);
		startActivity(settings);
		ROKOLogger.addEvent(new Event("User Opened Settings"));
	}
	public void goUser(View v){
		Intent user = new Intent(this, User.class);
		startActivity(user);
		ROKOLogger.addEvent(new Event("User Opened 'Avatar Edit'"));
	}
	
}
