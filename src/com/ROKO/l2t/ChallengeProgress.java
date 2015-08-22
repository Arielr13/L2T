package com.ROKO.l2t;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
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
		button.setVisibility(View.INVISIBLE);
		
		final String challengeId = getIntent().getStringExtra("ChallengeId");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Challenges");
		query.getInBackground(challengeId, new GetCallback<ParseObject>() {
			  public void done(ParseObject object, ParseException e) {
			    if (e == null) {
			    	if(object.getString("currentTurn").equals(currentUser.getObjectId())&&!(object.getBoolean("isOver"))){
			    		button.setVisibility(View.VISIBLE);
			    	}
			    } 
			  }
		});
	}
	public void populate(){
		String challengeId = getIntent().getStringExtra("ChallengeId");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Challenges");
		query.getInBackground(challengeId, new GetCallback<ParseObject>() {
			  public void done(ParseObject object, ParseException e) {
			    if (e == null) {
			    	ImageView fromUserMovingImage = (ImageView)findViewById(R.id.FromUserMovingImage);
					ImageView toUserMovingImage = (ImageView)findViewById(R.id.ToUserMovingImage);
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
}
