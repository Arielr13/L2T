package com.ROKO.l2t;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Results extends Activity {
	
	boolean passed;
	int level;
	ParseUser user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		
		level = getIntent().getExtras().getInt("level");
		int wpm = getIntent().getExtras().getInt("wpm");
		int words = getIntent().getExtras().getInt("words");
		
		TextView LevelResults = (TextView)findViewById(R.id.LevelResult);
		TextView NeedToPass = (TextView)findViewById(R.id.NeedToPass);
		TextView WPMDisplay = (TextView)findViewById(R.id.WPMDisplay);
		TextView WordsDisplay = (TextView)findViewById(R.id.WordsDisplay);
		ImageView ImageResults = (ImageView)findViewById(R.id.ImageResult);
		
		user = ParseUser.getCurrentUser();
		int trialsCompleted = user.getInt("trialsCompleted");
		user.increment("trialsCompleted");
		int AWPM = user.getInt("AWPM");
		user.put("AWPM", ((AWPM*trialsCompleted)+wpm)/(user.getInt("trialsCompleted")));
		
		int needWPM[] = {20, 20, 20, 20, 25, 25, 30, 30, 30, 35, 35, 35, 40, 40, 40, 20};
		
		if(wpm>=needWPM[level-1]){
			passed=true;
			LevelResults.setText("Level Passed!");
			NeedToPass.setText("");
			WPMDisplay.setText(wpm+" WPM");
			WordsDisplay.setText(words +" Words");
			ImageResults.setImageResource(R.drawable.trophy_active);
			user.increment("levelsUnlocked");
		}
		else{
			passed=false;
			LevelResults.setText("Level Failed");
			NeedToPass.setText("You needed " +needWPM[level-1] +" WPM to pass");
			WPMDisplay.setText(wpm+" WPM");
			WordsDisplay.setText(words +" Words");
			Bitmap bmp = BitmapFactory.decodeByteArray(user.getBytes("avatar"), 0, user.getBytes("avatar").length);
		    ImageResults.setImageBitmap(bmp);
			Button next = (Button)findViewById(R.id.Next);
			next.setText("Retry");
		}
	}
	
	public void Done(View v){
		Intent home = new Intent(this, Home.class);
		startActivity(home);
	}
	
	public void Next(View v){
		if(passed){
			Intent race = new Intent(this, Race.class);
			race.putExtra("level",level+1);
			startActivity(race);
		}
		else{
			Intent race = new Intent(this, Race.class);
			race.putExtra("level",level);
			startActivity(race);
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		user.saveInBackground();
	}
}
