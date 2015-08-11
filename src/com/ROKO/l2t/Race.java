package com.ROKO.l2t;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.race);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		seekbar = (SeekBar)findViewById(R.id.SeekBar);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	        int originalProgress;
	        @Override
	        public void onStopTrackingTouch(SeekBar seekBar) {
	        }
	        @Override
	        public void onStartTrackingTouch(SeekBar seekBar) {
	            originalProgress = seekBar.getProgress();
	        }
	        @Override
	        public void onProgressChanged(SeekBar seekBar, int arg1, boolean fromUser) {
	            if(fromUser == true){
	                seekBar.setProgress(originalProgress);
	            }               
	        }
	    });
		
		 new Thread(new Runnable() {
		        @Override
		        public void run() { 
		            while (true) {
		                try {
		                    Thread.sleep(100);
		                    mHandler.post(new Runnable() {

		                        @Override
		                        public void run() {
		                        	seekbar.setMax(0);
		                    		seekbar.setMax(100);
		                    		double progress = (wpmcount/100)*100;
		                    		seekbar.setProgress((int)progress);
		                        }
		                    });
		                } catch (Exception e) {
		                    
		                }
		            }
		        }
		    }).start();
		 
		 
		wpm = (TextView)findViewById(R.id.WPM);
		tokens = (TextView)findViewById(R.id.TokenCount);
		words = (TextView)findViewById(R.id.Words);
		timer = (TextView)findViewById(R.id.Timer);
		prompt = (EditText)findViewById(R.id.Prompt);
		input = (EditText)findViewById(R.id.UserInput);
		level = getIntent().getExtras().getInt("level");
		firsttime = true;
		
		timer.setText("0:00");
		
		
		String sentences[] = {
				"Imagination is more important than knowledge",
				"If music be the food of love, play on",
				"The way to get started is to quit talking and begin doing",
				"Obstacles are those frightful things you see when you take your eyes off the goal",
				"I skate where the puck is going to be, not where it has been",
				"When you come to a fork in the road, take it",
				"We may affirm absolutely that nothing great in the world has been accomplished without passion",
				"The life which is unexamined is not worth living",
				"Live as if you were to die tomorrow. Learn as if you were to live forever",
				"Ask not what your country can do for you, ask what you can do for your country"};
		
		
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
		if((sentence[counter]+" ").contains(s)){
			String setTextStringAfterColor="";
			
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
			
			if(s.contains(" ")){
				counter++;
				correctWords++;
				setTextString="";
				setTextStringAfterColor="";
				words.setText((int)correctWords+" Words");
				tokens.setText((int)correctWords+"");
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
	}
}
