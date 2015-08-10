package com.ROKO.l2t;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Race extends Activity {

	TextView wpm;
	TextView tokens;
	TextView words;
	TextView timer;
	EditText prompt;
	EditText input;
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
		level = getIntent().getExtras().getInt("level");
		firsttime = true;
		
		
		int times[] = {
				30,
				30,
				30,
				30,
				30,
				30,
				30,
				30,
				30,
				30,};
		
		
		totaltime=times[level-1]-1;
		timer.setText(times[level-1]/60+":"+times[level-1]%60);
		
		
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
		prompt.setText(setTextString.trim());
		
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
		if(s.contains(" ")){
			if(s.trim().equals(sentence[counter])){
				correctWords++;
			}
			counter++;
			prompt.getText().clear();
			setTextString="";
			for(int i=counter;i<sentence.length;i++){
				setTextString+=sentence[i]+" ";
			}
			if(setTextString.trim().equals("")){
				timerHandler.removeCallbacks(timerRunnable);
				Intent results = new Intent(Race.this, Results.class);
					results.putExtra("level",level);
	        		results.putExtra("words", (int)correctWords);
	        		results.putExtra("wpm", wpmcount);
            	startActivity(results);
			}
			prompt.setText(setTextString.trim());
			input.getText().clear();
			
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
            int totalsecondsremaining = totalseconds-(int)elapsedseconds;
            int minutesremaining=totalsecondsremaining/60;
            int secondsremaining=totalsecondsremaining%60;
            String showseconds;
            if(secondsremaining<10){
            	showseconds = "0"+secondsremaining;
            }
            else{
            	showseconds = ""+secondsremaining;
            }
            
            timer.setText((minutesremaining)+":"+(showseconds));
            
            if(totalsecondsremaining==0){
            	timerHandler.removeCallbacks(timerRunnable);
            	Intent results = new Intent(Race.this, Results.class);
            		results.putExtra("level",level);
            		results.putExtra("words", (int)correctWords);
            		results.putExtra("wpm", wpmcount);
            	startActivity(results);
            }
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
	}
}
