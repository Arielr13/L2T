package com.ROKO.l2t;

import java.util.ArrayList;
import java.util.List;

import com.ROKO.l2t.LevelSelect.LevelSelectAdapter;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseChallenge extends Activity {
	
	EditText searchbar;
	ParseUser currentUser = ParseUser.getCurrentUser();
	Boolean stopRequest = false;
	Boolean stopRequested = false;
	Boolean friend = false;
	ListView lv;
	ChooseChallengeAdapter adapter = new ChooseChallengeAdapter();
	TextView wpm;
	TextView username;
	ArrayList<ParseUser> friendsList = new ArrayList<ParseUser>();
	String friendid;
	int size;
	TextView notifications;
	ImageButton add;
	Boolean returnvalue = true;
	TextView popupusername;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_challenge);
		searchbar=(EditText)findViewById(R.id.ChooseChallengeSearch);

		lv = (ListView)findViewById(R.id.ChooseChallengeList);
		getRelations();
		lv.setOnItemClickListener(new OnItemClickListener()
		{
		      @Override
		      public void onItemClick(AdapterView<?> arg0, View view, int pos,long arg3) {
		    	  
		    	  final ParseObject challenge=new ParseObject("Challenges");
		    	  challenge.put("fromUser", currentUser.getObjectId()+"");
		    	  challenge.put("toUser", friendsList.get(pos).getObjectId()+"");
		    	  challenge.put("currentTurn", currentUser.getObjectId()+"");
		    	  challenge.put("fromUserWPM", 0);
		    	  challenge.put("toUserWPM", 0);
		    	  challenge.put("fromUserTrials", 0);
		    	  challenge.put("toUserTrials", 0);
		    	  challenge.put("isOver", false);
		    	  
		    	  challenge.saveInBackground(
	    			  new SaveCallback() {
	    				  @Override
						  public void done(ParseException arg0) {
	    					  Intent movetoProgressScreen = new Intent(ChooseChallenge.this, ChallengeProgress.class);
	    					  movetoProgressScreen.putExtra("ChallengeId", challenge.getObjectId()+"");
	    					  finish();
	    					  startActivity(movetoProgressScreen);
	    				  }
	    			  });
		      }
		});
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		friendsList = new ArrayList<ParseUser>();
		getRelations();
	}
	
	public void getRelations(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("fromUser", currentUser.getObjectId()+"");
		query.whereEqualTo("areFriends", true);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> list, ParseException e) {
		        if (e == null) {
		        	getRelations1(list, list.size(),0);
		        }
		    }
		});		
	}
	public void getRelations1(final List<ParseObject> listParam, final int listSize, final int i){
		ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friends");
		query1.whereEqualTo("toUser", currentUser.getObjectId()+"");
		query1.whereEqualTo("areFriends", true);
		query1.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> list, ParseException e) {
		        if (e == null) {
		        	List<ParseObject> finalList = new ArrayList<ParseObject>();
		        	finalList.addAll(listParam);
		        	finalList.addAll(list);
		        	getUsers(finalList, list.size()+listSize,0);
		        	if((list.size()+listSize)==0){
		        		findViewById(R.id.LoadingIcon).setVisibility(View.INVISIBLE);
		        	}
		        } 
		    }
		});
	}
	public void getUsers(final List<ParseObject> list, final int listSize, final int i){
		size = listSize;
		if(i==listSize){
			lv.setAdapter(adapter);
		}
		else{
			if((list.get(i).getString("toUser")+"").equals(currentUser.getObjectId()+"")){
				friendid = list.get(i).getString("fromUser");
			}
			else{
				friendid = list.get(i).getString("toUser");
			}
			
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.getInBackground(friendid, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser arg0, ParseException arg1) {
					//Toast.makeText(Friends.this, arg0.getObjectId()+"", Toast.LENGTH_SHORT).show();
					addUser(arg0);
					getUsers(list, listSize,i+1);
				}
				
			});
		}
		
	}
	
	public void addUser(ParseUser user){
		friendsList.add(user);
	}
	

	public class ChooseChallengeAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return size;
		}

		@Override
		public ParseObject getItem(int arg0) {
			return friendsList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null){
		        LayoutInflater inflater = (LayoutInflater) ChooseChallenge.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        arg1 = inflater.inflate(R.layout.choose_challenge_item, arg2,false);
		        wpm = (TextView)arg1.findViewById(R.id.ChooseChallengeUserWPM);
				username = (TextView)arg1.findViewById(R.id.ChooseChallengeUsernameField);
		    }
			
			findViewById(R.id.LoadingIcon).setVisibility(View.INVISIBLE);
			wpm.setText(friendsList.get(arg0).getInt("AWPM")+"");
			username.setText(friendsList.get(arg0).getUsername()+"");
			
			return arg1;
			
		}
	}
	}
	
	
	
	
	
	
	
	
	
	

