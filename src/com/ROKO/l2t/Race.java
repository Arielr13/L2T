package com.ROKO.l2t;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Race extends Activity {

	TextView wpm;
	TextView tokens;
	TextView words;
	TextView timer;
	EditText prompt;
	EditText input;
	SeekBar seekbar;
	ImageView movingimage;
	long startTime = 0;
	int totaltime;
	boolean firsttime;
	String sentence[];
	int counter;
	double correctWords=0;
	String setTextString;
	double elapsedseconds;
	int level;
	int wpmcount;
	Handler mHandler = new Handler();
	ParseUser user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.race);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		 	wpm = (TextView)findViewById(R.id.WPM);
			tokens = (TextView)findViewById(R.id.TokenCount);
			words = (TextView)findViewById(R.id.Words);
			timer = (TextView)findViewById(R.id.Timer);
			prompt = (EditText)findViewById(R.id.Prompt);
			input = (EditText)findViewById(R.id.UserInput);
			
			movingimage = (ImageView)findViewById(R.id.MovingImage);
			ParseUser currentUser = ParseUser.getCurrentUser();
			Bitmap bmp = BitmapFactory.decodeByteArray(currentUser.getBytes("avatar"), 0, currentUser.getBytes("avatar").length);
		    movingimage.setImageBitmap(bmp);
		    
			level = getIntent().getExtras().getInt("level");
			firsttime = true;
			
			timer.setText("0:00");
		
			String sentences[] = {
					"The more that you read, the more things you will know. The more that you learn, the more places you'll go.",
					
					"Do not take life too seriously. You will never get out of it alive.",
					
					"As we express our gratitude, we must never forget that the highest appreciation is not to utter words, but to live by them.",
					
					"Let us not seek the Republican answer or the Democratic answer, but the right answer. Let us not seek to fix the blame for the past. Let us accept our own responsibility for the future.",
					
					"A human being has so many skins inside, covering the depths of the heart. We know so many things, "
					+ "but we don't know ourselves! Why, thirty or forty skins or hides, as thick and hard as an ox's or "
					+ "bear's, cover the soul. Go into your own ground and learn to know yourself there.",
					
					"Reading, after a certain age, diverts the mind too much from its creative pursuits. Any man who reads "
					+ "too much and uses his own brain too little falls into lazy habits of thinking.",
					
					"Technology is supposed to make our lives easier, allowing us to do things more quickly and efficiently. "
					+ "But too often it seems to make things harder, leaving us with fifty-button remote controls, "
					+ "digital cameras with hundreds of mysterious features and book-length manuals, and cars with "
					+ "dashboard systems worthy of the space shuttle.",
					
					"Technology gives us power, but it does not and cannot tell us how to use that power. Thanks to technology, "
					+ "we can instantly communicate across the world, but it still doesn't help us know what to say.",
				
					"Technology is such a broad kind of term, it really applies to so many things, from the electric light to running"
					+ " cars on oil. All of these different things can be called technology. I have kind of a love-hate relationship with "
					+ "it, as I expect most people do. With the computer, I spend so many hours sitting in front of a computer.",
					
					"True friendship multiplies the good in life and divides its evils. Strive to have friends, for life without friends "
					+ "is like life on a desert island... to find one real friend in a lifetime is good fortune; to keep him is a blessing.",
					
					"Your work is going to fill a large part of your life, and the only way to be truly satisfied is to do "
					+ "what you believe is great work. And the only way to do great work is to love what you do. "
					+ "If you haven't found it yet, keep looking. Don't settle. As with all matters of the heart, "
					+ "you'll know when you find it.",
					
					"If you live long enough, you'll make mistakes. But if you learn from them, you'll be a better person. "
					+ "It's how you handle adversity, not how it affects you. The main thing is never quit, never quit, never quit.",
					
					"All the adversity I've had in my life, all my troubles and obstacles, have strengthened me... You may not realize it "
					+ "when it happens, but a kick in the teeth may be the best thing in the world for you.",
					
					"In my school, the brightest boys did math and physics, the less bright did physics and chemistry, "
					+ "and the least bright did biology. I wanted to do math and physics, but my father made me do chemistry "
					+ "because he thought there would be no jobs for mathematicians.",
					
					"My father... removed from Kentucky to... Indiana, in my eighth year... It was a wild region, with many bears and other "
					+ "wild animals still in the woods. There I grew up... Of course when I came of age, I did not know much. Still somehow, "
					+ "I could read, write, and cipher... but that was all.",
			};
			
			user = ParseUser.getCurrentUser();
			tokens.setText((user.getInt("tokenCount"))+"");
			
			counter = 0;
			sentence = sentences[level-1].split(" ");
			setTextString="";
			
			for(int i=counter;i<sentence.length;i++){
				setTextString+=sentence[i]+" ";
			}
			prompt.setText("");
			String setTextStringAfterColor="";
			for(int i=counter+1;i<sentence.length;i++){
				setTextStringAfterColor+=sentence[i]+" ";
			}
			prompt.setText(Html.fromHtml("<font color='#0000ff'> "+setTextString.split(" ")[0].trim()+"</font> "+setTextStringAfterColor));
			
			input.addTextChangedListener(new TextWatcher() {
			    @Override
			    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					if(firsttime){
						startTime = System.currentTimeMillis();
				        timerHandler.postDelayed(timerRunnable, 0);
				        firsttime = false;
					}
					parseInput(cs.toString());
			    }
			    @Override
			    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
			    @Override
			    public void afterTextChanged(Editable arg0) { 
			    }
			});
		
	}
	
	public void parseInput(String s){
		//If user is typing word without error
		if((sentence[counter]+" ").contains(s)){
			String setTextStringAfterColor="";
			
			//Logic for user that fixed mistake
			for(int i=counter;i<sentence.length;i++){
				setTextString+=sentence[i]+" ";
			}
			for(int i=counter+1;i<sentence.length;i++){
				setTextStringAfterColor+=sentence[i]+" ";
			}
			if(sentence.length-counter==1){
				prompt.getText().clear();
				prompt.setText(Html.fromHtml("<font color='#0000ff'> "+setTextString.split(" ")[0].trim()+" -- Press Space to Submit</font>"));
			}
			else if((sentence.length-counter>1)){
				prompt.getText().clear();
				prompt.setText(Html.fromHtml("<font color='#0000ff'> "+setTextString.split(" ")[0].trim()+"</font> "+setTextStringAfterColor));
			}
			
			//User submits correct word
			if(s.contains(" ")&&!(s.equals(null)||s.equals(" "))){
				counter++;
				float now = ((float)counter/(float)sentence.length);
				float before = (((float)counter-1)/(float)sentence.length);
				if (before<0){
					before=0;
				}
				if (now<0){
					now=0;
				}
				
				//Character animation
				TranslateAnimation moveLefttoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, before, Animation.RELATIVE_TO_PARENT, now, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
			    moveLefttoRight.setDuration(1000);
			    moveLefttoRight.setFillAfter(true);	
			    movingimage.startAnimation(moveLefttoRight);
				
				correctWords++;
				user.increment("tokenCount");
				setTextString="";
				setTextStringAfterColor="";
				words.setText((int)correctWords+" Words");
				//Toast.makeText(Race.this, (user.getInt("tokenCount"))+"", Toast.LENGTH_SHORT).show();
				tokens.setText(user.getInt("tokenCount")+"");
				double wps = (correctWords/elapsedseconds);
				wpmcount = (int)(wps*60);
				if (wpmcount<150){
					wpm.setText(wpmcount+" WPM");
				}
				else{
					wpm.setText("Max WPM");
				}
				for(int i=counter;i<sentence.length;i++){
					setTextString+=sentence[i]+" ";
				}
				for(int i=counter+1;i<sentence.length;i++){
					setTextStringAfterColor+=sentence[i]+" ";
				}
				if(sentence.length-counter==1){
					prompt.getText().clear();
					input.getText().clear();
					prompt.setText(Html.fromHtml("<font color='#0000ff'> "+setTextString.split(" ")[0].trim()+" -- Press Space to Submit</font>"));
				}
				else if((sentence.length-counter>1)){
					prompt.getText().clear();
					input.getText().clear();
					prompt.setText(Html.fromHtml("<font color='#0000ff'> "+setTextString.split(" ")[0].trim()+"</font> "+setTextStringAfterColor));
				}
				else{
					timerHandler.removeCallbacks(timerRunnable);
					Intent results = new Intent(Race.this, Results.class);
						results.putExtra("level",level);
		        		results.putExtra("words", (int)correctWords);
		        		results.putExtra("wpm", wpmcount);
	            	startActivity(results);
				}
			}	
		}
		//If word user typing is wrong
		else{
			String setTextStringAfterError="";
			for(int i=counter;i<sentence.length;i++){
				setTextString+=sentence[i]+" ";
			}
			for(int i=counter+1;i<sentence.length;i++){
				setTextStringAfterError+=sentence[i]+" ";
			}
			if(sentence.length-counter==1){
				prompt.setText(Html.fromHtml("<font color='#ff0000'> "+setTextString.split(" ")[0].trim()+" -- Press Space to Submit</font>"));
			}
			else{
				prompt.setText(Html.fromHtml("<font color='#ff0000'> "+setTextString.split(" ")[0].trim()+"</font> "+setTextStringAfterError));
			}
		//If user submits, vibrate and refresh input box
			if(s.contains(" ")){
				Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
				 v.vibrate(500);
				if(sentence.length-counter==1){
					prompt.setText(Html.fromHtml("<font color='#0000ff'> "+setTextString.split(" ")[0].trim()+" -- Press Space to Submit</font>"));
				}
				else{
					prompt.setText(Html.fromHtml("<font color='#0000ff'> "+setTextString.split(" ")[0].trim()+"</font> "+setTextStringAfterError));
				}
				input.getText().clear();
			}
		}
	}	
	
	
	
    //Timer forces new activity when time runs out
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {    	
        	int totalseconds = totaltime;
            long millis = System.currentTimeMillis() - startTime;
            elapsedseconds = (millis / 1000);
            int elapsedminutes = (int)elapsedseconds/60;
            int elapsedsecondsshow = (int)elapsedseconds%60;
            String showseconds = "";
            
            if(elapsedseconds<10){
            	showseconds = "0"+elapsedsecondsshow;
            }
            else{
            	showseconds = ""+elapsedsecondsshow;
            }
            
            timer.setText((elapsedminutes)+":"+(showseconds));
            timerHandler.postDelayed(this, 500);
        }
    };
	
	public void back(View v){
		finish();
	}
	
	@Override
	public void onPause() {
	    super.onPause();  // Always call the superclass method first
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    	timerHandler.removeCallbacks(timerRunnable);
    	user.saveInBackground();
	}
}
