package com.ROKO.l2t;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChallengeProgress extends Activity {
	ParseUser currentUser = ParseUser.getCurrentUser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_progress);
		populate();
		final Button button = (Button)findViewById(R.id.TurnButton);
		final Button homebutton = (Button)findViewById(R.id.HomeButton);
		button.setVisibility(View.INVISIBLE);
		homebutton.setVisibility(View.INVISIBLE);
		
		final String challengeId = getIntent().getStringExtra("ChallengeId");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Challenges");
		query.getInBackground(challengeId, new GetCallback<ParseObject>() {
			  public void done(ParseObject object, ParseException e) {
			    if (e == null) {
			    	if(object.getString("currentTurn").equals(currentUser.getObjectId())&&(!(object.getBoolean("isOver")))){
			    		button.setVisibility(View.VISIBLE);
			    	}
			    	else{
			    		homebutton.setVisibility(View.VISIBLE);
			    	}
			    }
			  }
		});
	}
	public void populate(){
		String challengeId = getIntent().getStringExtra("ChallengeId");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Challenges");
		query.getInBackground(challengeId, new GetCallback<ParseObject>() {
			  public void done(final ParseObject object, ParseException e) {
			    if (e == null) {
			    	final ImageView fromUserMovingImage = (ImageView)findViewById(R.id.FromUserMovingImage);
					final ImageView toUserMovingImage = (ImageView)findViewById(R.id.ToUserMovingImage);
			    	
		    		if((object.getString("toUser")+"").equals(currentUser.getObjectId()+"")){
		    			ParseQuery<ParseUser> query = ParseUser.getQuery();
						query.getInBackground(object.getString("fromUser"), new GetCallback<ParseUser>() {	
							@Override
							public void done(ParseUser arg0, ParseException arg1) {
								ParseUser currentUser = ParseUser.getCurrentUser();
								Bitmap cur = BitmapFactory.decodeByteArray(currentUser.getBytes("avatar"), 0, currentUser.getBytes("avatar").length);
							    toUserMovingImage.setImageBitmap(cur);
							    Bitmap other = BitmapFactory.decodeByteArray(arg0.getBytes("avatar"), 0, arg0.getBytes("avatar").length);
							    fromUserMovingImage.setImageBitmap(other);
							}
						});
					}
					else{
						ParseQuery<ParseUser> query = ParseUser.getQuery();
						query.getInBackground(object.getString("toUser"), new GetCallback<ParseUser>() {	
							@Override
							public void done(ParseUser arg0, ParseException arg1) {
								ParseUser currentUser = ParseUser.getCurrentUser();
								Bitmap cur = BitmapFactory.decodeByteArray(currentUser.getBytes("avatar"), 0, currentUser.getBytes("avatar").length);
							    fromUserMovingImage.setImageBitmap(cur);
							    Bitmap other = BitmapFactory.decodeByteArray(arg0.getBytes("avatar"), 0, arg0.getBytes("avatar").length);
							    toUserMovingImage.setImageBitmap(other);
							}
						});
					}
		    		
		    		
					
					TranslateAnimation fromUserAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, (((float) object.getInt("fromUserWPM"))/((float) 100)), Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
				    fromUserAnimation.setDuration(1500);
				    fromUserAnimation.setFillAfter(true);	
				    fromUserMovingImage.startAnimation(fromUserAnimation);
				    
				    TranslateAnimation toUserAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, (((float) object.getInt("toUserWPM"))/((float) 100)), Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
				    toUserAnimation.setDuration(1000);
				    toUserAnimation.setFillAfter(true);	
				    toUserMovingImage.startAnimation(toUserAnimation);
				    
				    if(object.getString("fromUser").equals(currentUser.getObjectId()+"")){
				    	 TextView wpm = (TextView)findViewById(R.id.ChallengeProgressWPM);
				    	 wpm.setText(object.getInt("fromUserWPM")+" WPM");
				    	 
				    	 TextView place = (TextView)findViewById(R.id.Place);
				    	 
				    	 if(object.getInt("fromUserWPM")>object.getInt("toUserWPM")){
				    		 place.setText("1st");
				    	 }
				    	 else if(object.getInt("fromUserWPM")==object.getInt("toUserWPM")){
				    		 place.setText("Tied");
				    	 }
				    	 else{
				    		 place.setText("2nd");
				    	 }
				     }
				     else{
				    	 TextView wpm = (TextView)findViewById(R.id.ChallengeProgressWPM);
				    	 wpm.setText(object.getInt("toUserWPM")+" WPM");
				    	 
				    	 TextView place = (TextView)findViewById(R.id.Place);
				    	 
				    	 if(object.getInt("toUserWPM")>object.getInt("fromUserWPM")){
				    		 place.setText("1st");
				    	 }
				    	 else if(object.getInt("toUserWPM")==object.getInt("fromUserWPM")){
				    		 place.setText("Tied");
				    	 }
				    	 else{
				    		 place.setText("2nd");
				    	 }
				     }
				    
				    TextView tokens = (TextView)findViewById(R.id.ChallengeProgressTokens);
			    	tokens.setText(currentUser.getInt("tokenCount")+"");
			    } 
			  }
		});
	}
	public void turn(View v){
		final String challengeId = getIntent().getStringExtra("ChallengeId");
		Intent race = new Intent(ChallengeProgress.this, ChallengeRace.class);
		race.putExtra("ChallengeId", challengeId);
		startActivity(race);
		finish();
	}
	public void home(View v){
		startActivity(new Intent(ChallengeProgress.this, Home.class));
		finish();
	}
}
