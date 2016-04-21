package com.ROKO.l2t;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Challenge extends FragmentActivity {
	TextView wpm;
	TextView username;
	ParseUser currentUser = ParseUser.getCurrentUser();
	ListView yourturn;
	ListView theirturn;
	ListView history;
	ViewPager pager;
	Button historyButton;
	Button yourTurnButton;
	Button theirTurnButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.challenge);
		
		pager = (ViewPager) findViewById(R.id.ViewPager);
		pager.setOffscreenPageLimit(3);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        
        historyButton = (Button)findViewById(R.id.ChallengeListHistoryButton);
        yourTurnButton = (Button)findViewById(R.id.ChallengeListYourTurnButton);
        theirTurnButton = (Button)findViewById(R.id.ChallengeListTheirTurnButton);
	}
	
	private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
        	YourTurnFragment yourturnfragment = new YourTurnFragment();
        	TheirTurnFragment theirturnfragment = new TheirTurnFragment();
        	HistoryFragment historyfragment = new HistoryFragment();
            switch(pos) {

            case 0: return yourturnfragment.newInstance();
            case 1: return theirturnfragment.newInstance();
            case 2: return historyfragment.newInstance();
            default: return yourturnfragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }       
    }
	
	public class YourTurnFragment extends Fragment {
		ProgressBar loadingIcon;
		ListView list;
		List<YourTurnObject> yourTurnList;
	    List<ParseObject> tempObjectList;
	    Boolean started=false;
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.challenge_listview, container, false);
	        list = (ListView)v.findViewById(R.id.ChallengeList);
	        loadingIcon = (ProgressBar)v.findViewById(R.id.ChallengeLoadingIcon);
	        //Button yourTurnButton = (Button)findViewById(R.id.ChallengeListYourTurnButton);
	        //yourTurnButton.setBackgroundColor(getResources().getColor(R.color.blue));
	        //yourTurnList = new ArrayList<YourTurnObject>();
	        //tempObjectList = new ArrayList<ParseObject>();
	        getObjects();
	        
	        list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					YourTurnObject object = yourTurnList.get(position);
					Intent progress = new Intent(Challenge.this, ChallengeProgress.class);
					progress.putExtra("ChallengeId", object.challenge.getObjectId());
					startActivity(progress);
					finish();
				}	
	        });
	        
	        started=true;
	        return v;
	    }
	    
	    @Override
	    public void setMenuVisibility(final boolean visible) {
	        super.setMenuVisibility(visible);
	        if (visible&&started) {
	        	
		        historyButton.setBackgroundColor(getResources().getColor(R.color.background));
		        yourTurnButton.setBackgroundColor(getResources().getColor(R.color.blue));
		        theirTurnButton.setBackgroundColor(getResources().getColor(R.color.background));
	        }
	    }
	   
	    public YourTurnFragment newInstance(){
	        YourTurnFragment f = new YourTurnFragment();
	        return f;
	    }
	    
	    public class YourTurnObject{
	    	ParseObject challenge;
	    	ParseUser user; 
	    }
	    
	    public YourTurnObject addYourTurnObject(ParseObject challenge, ParseUser user){
	    	YourTurnObject object = new YourTurnObject();
	    	object.challenge = challenge;
	    	object.user = user;
	    	return object;
	    }
	    
	    public void getObjects(){
	    	yourTurnList = new ArrayList<YourTurnObject>();
	        tempObjectList = new ArrayList<ParseObject>();
	    	
	    	ParseQuery<ParseObject> query = new ParseQuery("Challenges");
			query.whereEqualTo("currentTurn", currentUser.getObjectId());
			query.whereNotEqualTo("isOver", true);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> list, ParseException e) {
			        if (e == null&&list.size()>0) {
			           tempObjectList=list;
			           getUsernames(0);
			        } 
			        else{
			        	loadingIcon.setVisibility(View.INVISIBLE);
			        }
			    }
			});
	    }
	    
	    public void getUsernames(final int i){
	    	if(i<tempObjectList.size()){
	    		final ParseObject object = tempObjectList.get(i);
	    		String friendid;
	    		if((object.getString("toUser")+"").equals(currentUser.getObjectId()+"")){
					friendid = object.getString("fromUser");
				}
				else{
					friendid = object.getString("toUser");
				}
	    		
	    		ParseQuery<ParseUser> query = ParseUser.getQuery();
				query.getInBackground(friendid, new GetCallback<ParseUser>() {	
					@Override
					public void done(ParseUser arg0, ParseException arg1) {
						yourTurnList.add(addYourTurnObject(object, arg0));
						getUsernames(i+1);
					}
				});
	    	}
	    	else{	
	    		loadingIcon.setVisibility(View.INVISIBLE);
	    		list.setAdapter(new YourTurnAdapter());
	    	}
	    }
	    
	    public class YourTurnAdapter extends BaseAdapter{
			@Override
			public int getCount() {
				return yourTurnList.size();
			}

			@Override
			public YourTurnObject getItem(int arg0) {
				return yourTurnList.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(final int arg0, View arg1, ViewGroup arg2) {
				if(arg1==null){
					//Toast.makeText(getApplicationContext(), "This was Called", Toast.LENGTH_SHORT).show();
			        LayoutInflater inflater = (LayoutInflater) Challenge.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			        arg1 = inflater.inflate(R.layout.choose_challenge_item, arg2,false);
			        wpm = (TextView)arg1.findViewById(R.id.ChooseChallengeUserWPM);
					username = (TextView)arg1.findViewById(R.id.ChooseChallengeUsernameField);
					wpm.setText(getItem(arg0).user.getInt("AWPM")+"");
					username.setText(getItem(arg0).user.getString("username"));
			    }
				
				return arg1;
				
			}
		}    
	}
	
	
	public class TheirTurnFragment extends Fragment {
		ProgressBar loadingIcon;
		ListView list;
		Boolean started = false;
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.challenge_listview, container, false);
	        list = (ListView)v.findViewById(R.id.ChallengeList);
	        loadingIcon = (ProgressBar)v.findViewById(R.id.ChallengeLoadingIcon);
	        //Button theirTurnButton = (Button)findViewById(R.id.ChallengeListTheirTurnButton);
	        //theirTurnButton.setBackgroundColor(getResources().getColor(R.color.blue));
	        theirTurnList = new ArrayList<TheirTurnObject>();
	        tempObjectList = new ArrayList<ParseObject>();
	        getObjects();
	        
	        list.setClickable(true);
	        list.setFocusable(true);
	        list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					TheirTurnObject object = theirTurnList.get(position);
					Intent progress = new Intent(Challenge.this, ChallengeProgress.class);
					progress.putExtra("ChallengeId", object.challenge.getObjectId());
					startActivity(progress);
					finish();
				}	
	        });
	        
	        started = true;
	        return v;
	    }
	    
	    @Override
	    public void setMenuVisibility(final boolean visible) {
	        super.setMenuVisibility(visible&&started);
	        if (visible) {
		        historyButton.setBackgroundColor(getResources().getColor(R.color.background));
		        yourTurnButton.setBackgroundColor(getResources().getColor(R.color.background));
		        theirTurnButton.setBackgroundColor(getResources().getColor(R.color.blue));
	        }
	    }
	    
	    public TheirTurnFragment newInstance(){
	        TheirTurnFragment f = new TheirTurnFragment();
	        return f;
	    }
	    
	    public class TheirTurnObject{
	    	ParseObject challenge;
	    	ParseUser user; 
	    }
	    
	    public TheirTurnObject addYourTurnObject(ParseObject challenge, ParseUser user){
	    	TheirTurnObject object = new TheirTurnObject();
	    	object.challenge = challenge;
	    	object.user = user;
	    	return object;
	    }
	   
	    ArrayList<TheirTurnObject> theirTurnList;
	    ArrayList<ParseObject> tempObjectList;
	    
	    public void getObjects(){
	    	//Fix This
	    	ParseQuery<ParseObject> query = new ParseQuery("Challenges");
			query.whereEqualTo("fromUser", currentUser.getObjectId());
			query.whereNotEqualTo("currentTurn", currentUser.getObjectId());
			query.whereNotEqualTo("isOver", true);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> list, ParseException e) {
			        if (e == null&&list.size()>0) {
			           tempObjectList.addAll(list);
			           getObjects1();
			        } 
			        else{
			        	getObjects1();
			        }
			    }
			});
	    }
	    
	    public void getObjects1(){
	    	//Fix This
	    	ParseQuery<ParseObject> query = new ParseQuery("Challenges");
			query.whereEqualTo("toUser", currentUser.getObjectId());
			query.whereNotEqualTo("currentTurn", currentUser.getObjectId());
			query.whereNotEqualTo("isOver", true);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> list, ParseException e) {
			        if (e == null&&list.size()>0) {
			           tempObjectList.addAll(list);
			           getUsernames(0);
			        } 
			        else{
			           getUsernames(0);
			        }
			    }
			});
	    }
	    
	    public void getUsernames(final int i){
	    	if(i<tempObjectList.size()){
	    		final ParseObject object = tempObjectList.get(i);
	    		String friendid;
	    		if((object.getString("toUser")+"").equals(currentUser.getObjectId()+"")){
					friendid = object.getString("fromUser");
				}
				else{
					friendid = object.getString("toUser");
				}
	    		
	    		ParseQuery<ParseUser> query = ParseUser.getQuery();
				query.getInBackground(friendid, new GetCallback<ParseUser>() {	
					@Override
					public void done(ParseUser arg0, ParseException arg1) {
						theirTurnList.add(addYourTurnObject(object, arg0));
						getUsernames(i+1);
					}
				});
	    	}
	    	else{
	    		loadingIcon.setVisibility(View.INVISIBLE);
	    		list.setAdapter(new TheirTurnAdapter());
	    	}
	    }
	    
	    public class TheirTurnAdapter extends BaseAdapter{
			@Override
			public int getCount() {
				return theirTurnList.size();
			}

			@Override
			public TheirTurnObject getItem(int arg0) {
				return theirTurnList.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(final int arg0, View arg1, ViewGroup arg2) {
				if(arg1==null){
			        LayoutInflater inflater = (LayoutInflater) Challenge.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			        arg1 = inflater.inflate(R.layout.choose_challenge_item, arg2,false);
			        wpm = (TextView)arg1.findViewById(R.id.ChooseChallengeUserWPM);
					username = (TextView)arg1.findViewById(R.id.ChooseChallengeUsernameField);
			    }
		
				wpm.setText(theirTurnList.get(arg0).user.getInt("AWPM")+"");
				username.setText(theirTurnList.get(arg0).user.getString("username"));
				
				return arg1;
				
			}
		}    
	    
	}
	public class HistoryFragment extends Fragment {
		ProgressBar loadingIcon;
		ListView list;
		Boolean started = false;
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.challenge_listview, container, false);
	        list = (ListView)v.findViewById(R.id.ChallengeList);
	        loadingIcon = (ProgressBar)v.findViewById(R.id.ChallengeLoadingIcon);
	        getObjects();
	        
	        list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					HistoryObject object = historyList.get(position);
					Intent progress = new Intent(Challenge.this, ChallengeResults.class);
					progress.putExtra("ChallengeId", object.challenge.getObjectId());
					startActivity(progress);
					finish();
				}	
	        });
	        started = true;
	        return v;
	    }
	    @Override
	    public void setMenuVisibility(final boolean visible) {
	        super.setMenuVisibility(visible&&started);
	        if (visible) {
	        	Button historyButton = (Button)findViewById(R.id.ChallengeListHistoryButton);
		        historyButton.setBackgroundColor(getResources().getColor(R.color.blue));
		        Button yourTurnButton = (Button)findViewById(R.id.ChallengeListYourTurnButton);
		        yourTurnButton.setBackgroundColor(getResources().getColor(R.color.background));
		        Button theirTurnButton = (Button)findViewById(R.id.ChallengeListTheirTurnButton);
		        theirTurnButton.setBackgroundColor(getResources().getColor(R.color.background));
	        }
	    }
	    public HistoryFragment newInstance(){
	        HistoryFragment f = new HistoryFragment();
	        return f;
	    }
	    
	    public class HistoryObject{
	    	ParseObject challenge;
	    	ParseUser user; 
	    }
	    
	    public HistoryObject addYourTurnObject(ParseObject challenge, ParseUser user){
	    	HistoryObject object = new HistoryObject();
	    	object.challenge = challenge;
	    	object.user = user;
	    	return object;
	    }
	   
	    ArrayList<HistoryObject> historyList;
	    ArrayList<ParseObject> tempObjectList;
	    
	    public void getObjects(){
	    	historyList = new ArrayList<HistoryObject>();
	        tempObjectList = new ArrayList<ParseObject>();
	        
	    	ParseQuery<ParseObject> query = new ParseQuery("Challenges");
			query.whereEqualTo("fromUser", currentUser.getObjectId());
			query.whereEqualTo("isOver", true);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> list, ParseException e) {
			        if (e == null&&list.size()>0) {
			           tempObjectList.addAll(list);
			           getObjects1();
			        } 
			        else{
			        	getObjects1();
			        }
			    }
			});
	    }
	    
	    public void getObjects1(){
	    	ParseQuery<ParseObject> query = new ParseQuery("Challenges");
			query.whereEqualTo("toUser", currentUser.getObjectId());
			query.whereEqualTo("isOver", true);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> list, ParseException e) {
			        if (e == null&&list.size()>0) {
			           tempObjectList.addAll(list);
			           getUsernames(0);
			        } 
			        else{
			           getUsernames(0);
			        }
			    }
			});
	    }
	    
	    public void getUsernames(final int i){
	    	if(i<tempObjectList.size()){
	    		final ParseObject object = tempObjectList.get(i);
	    		String friendid;
	    		if((object.getString("toUser")+"").equals(currentUser.getObjectId()+"")){
					friendid = object.getString("fromUser");
				}
				else{
					friendid = object.getString("toUser");
				}
	    		
	    		ParseQuery<ParseUser> query = ParseUser.getQuery();
				query.getInBackground(friendid, new GetCallback<ParseUser>() {	
					@Override
					public void done(ParseUser arg0, ParseException arg1) {
						historyList.add(addYourTurnObject(object, arg0));
						getUsernames(i+1);
					}
				});
	    	}
	    	else{
	    		loadingIcon.setVisibility(View.INVISIBLE);
	    		list.setAdapter(new TheirTurnAdapter());
	    	}
	    }
	    
	    public class TheirTurnAdapter extends BaseAdapter{
			@Override
			public int getCount() {
				return historyList.size();
			}

			@Override
			public HistoryObject getItem(int arg0) {
				return historyList.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(final int arg0, View arg1, ViewGroup arg2) {
				if(arg1==null){
			        LayoutInflater inflater = (LayoutInflater) Challenge.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			        arg1 = inflater.inflate(R.layout.choose_challenge_item, arg2,false);
			        wpm = (TextView)arg1.findViewById(R.id.ChooseChallengeUserWPM);
					username = (TextView)arg1.findViewById(R.id.ChooseChallengeUsernameField);
					//Toast.makeText(getApplicationContext(), historyList.get(arg0).user.getUsername()+"", Toast.LENGTH_SHORT).show();
					wpm.setText(historyList.get(arg0).user.getInt("AWPM")+"");
					username.setText(historyList.get(arg0).user.getString("username"));
			    }
		
				
				
				return arg1;
				
			}
		}
	}
 
	
	public void newChallenge(View v){
		startActivity(new Intent(Challenge.this, ChooseChallenge.class));
		finish();
	}
	public void yourTurn(View v){
		pager.setCurrentItem(0);
	}
	public void theirTurn(View v){
		pager.setCurrentItem(1);
	}
	public void history(View v){
		pager.setCurrentItem(2);
	}
}