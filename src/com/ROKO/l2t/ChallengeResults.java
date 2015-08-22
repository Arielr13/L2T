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
import android.widget.ImageView;
import android.widget.TextView;

public class ChallengeResults extends Activity {

	ParseUser currentUser = ParseUser.getCurrentUser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge_results);
		populate();
	}
	
	public void populate(){
		String challengeId = getIntent().getStringExtra("ChallengeId");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Challenges");
		query.getInBackground(challengeId, new GetCallback<ParseObject>() {
			  public void done(ParseObject object, ParseException e) {
			    if (e == null) {
			    	ImageView fromUserMovingImage = (ImageView)findViewById(R.id.ResultsFromUserMovingImage);
					ImageView toUserMovingImage = (ImageView)findViewById(R.id.ResultsToUserMovingImage);
			    	TranslateAnimation fromUserAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, (((float) object.getInt("fromUserWPM"))/((float) 100)), Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
				    fromUserAnimation.setDuration(1500);
				    fromUserAnimation.setFillAfter(true);	
				    fromUserMovingImage.startAnimation(fromUserAnimation);
				    
				    TranslateAnimation toUserAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, (((float) object.getInt("toUserWPM"))/((float) 100)), Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
				    toUserAnimation.setDuration(1000);
				    toUserAnimation.setFillAfter(true);	
				    toUserMovingImage.startAnimation(toUserAnimation);
				    
			    	if(object.getString("fromUser").equals(currentUser.getObjectId()+"")){
				    	 TextView wpm = (TextView)findViewById(R.id.ChallengeResultsWPM);
				    	 wpm.setText(object.getInt("fromUserWPM")+" WPM");
				    	 
				    	 TextView result = (TextView)findViewById(R.id.Result);
				    	 
				    	 if(object.getInt("fromUserWPM")>object.getInt("toUserWPM")){
				    		 result.setText("You Won!");
				    	 }
				    	 else if(object.getInt("fromUserWPM")==object.getInt("toUserWPM")){
				    		 result.setText("You Tied");
				    	 }
				    	 else{
				    		 result.setText("You Lost :(");
				    	 }
				     }
				     else{
				    	 TextView wpm = (TextView)findViewById(R.id.ChallengeResultsWPM);
				    	 wpm.setText(object.getInt("toUserWPM")+" WPM");
				    	 
				    	 TextView result = (TextView)findViewById(R.id.Result);
				    	 
				    	 if(object.getInt("toUserWPM")>object.getInt("fromUserWPM")){
				    		 result.setText("You Won!");
				    	 }
				    	 else if(object.getInt("toUserWPM")==object.getInt("fromUserWPM")){
				    		 result.setText("You Tied");
				    	 }
				    	 else{
				    		 result.setText("You Lost :(");
				    	 }
				     }
			    }
			 }
		});
	}
	
	public void home(View v){
		startActivity(new Intent(ChallengeResults.this, Home.class));
		finish();
	}
}
