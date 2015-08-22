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
import android.widget.AdapterView.OnItemClickListener;

public class Friends extends Activity {
	
	EditText searchbar;
	ParseUser currentUser = ParseUser.getCurrentUser();
	Boolean stopRequest = false;
	Boolean stopRequested = false;
	Boolean friend = false;
	ListView lv;
	FriendsAdapter adapter;
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
		setContentView(R.layout.friends);
		adapter = new FriendsAdapter();
		add = (ImageButton)findViewById(R.id.FriendsAddButton);
		searchbar=(EditText)findViewById(R.id.FriendsSearch);

		lv = (ListView)findViewById(R.id.FriendsList);
		getRelations();
		setNotifications();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		friendsList = new ArrayList<ParseUser>();
		getRelations();
		setNotifications();
	}
	
	public void notification(View v){
		notifications = (TextView)findViewById(R.id.Notifications);
		if(!(notifications.getText().toString().equals("0"))){
			startActivity(new Intent(Friends.this, FriendsNotifications.class));
		}
	}
	
	public void addFriend (View v){
		String inputUsername = searchbar.getText().toString().toLowerCase().trim();
		add.setEnabled(false);
		findViewById(R.id.LoadingIcon).setVisibility(View.VISIBLE);
		isUser(inputUsername);
		
	}
	
	public void isUser(final String username){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<ParseUser>() {
		    public void done(List<ParseUser> userList, ParseException e) {
		        if (e == null && userList.size()==0) {
		        	findViewById(R.id.LoadingIcon).setVisibility(View.INVISIBLE);
	            	new AlertDialog.Builder(Friends.this)
				    .setTitle("That User Doesn't Exist!")
				    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	add.setEnabled(true);
				        }
				     })
				    .show();
		        }
		        else if (userList.size()>0){
		        	areFriends(userList.get(0));
		        }
		    }
		});
	}
	
	public void areFriends(final ParseUser user){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("areFriends", true);
		query.whereEqualTo("fromUser", currentUser.getObjectId()+"");
		query.whereEqualTo("toUser", user.getObjectId()+"");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> list, ParseException e) {
		    	findViewById(R.id.LoadingIcon).setVisibility(View.INVISIBLE);
		    	if (e == null && list.size()>0) {
	            	new AlertDialog.Builder(Friends.this)
				    .setTitle("You Are Already Friends!")
				    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	add.setEnabled(true);
				        }
				     })
				    .show();
		        }
		    	else{
		    		areFriends1(user);
		    	}
		    }
		});
	}
	public void areFriends1(final ParseUser user){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("areFriends", true);
		query.whereEqualTo("toUser", currentUser.getObjectId()+"");
		query.whereEqualTo("fromUser", user.getObjectId()+"");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> list, ParseException e) {
		    	findViewById(R.id.LoadingIcon).setVisibility(View.INVISIBLE);
		    	if (e == null && list.size()>0) {
	            	new AlertDialog.Builder(Friends.this)
				    .setTitle("You Are Already Friends!")
				    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	add.setEnabled(true);
				        }
				     })
				    .show();
		        }
		    	else{
		    		alreadyRequested(user);
		    	}
		    }
		});
	}
	public void alreadyRequested(final ParseUser user){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("areFriends", false);
		query.whereEqualTo("toUser", user.getObjectId()+"");
		query.whereEqualTo("fromUser", currentUser.getObjectId()+"");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> list, ParseException e) {
		    	if (e == null && list.size()>0) {
	            	new AlertDialog.Builder(Friends.this)
				    .setTitle("You Already Sent a Request to This User!")
				    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	add.setEnabled(true);
				        }
				     })
				    .show();
		        }
		    	else{
		    		theyRequested(user);
		    	}
		    }
		});
	}
	public void theyRequested(final ParseUser user){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("areFriends", false);
		query.whereEqualTo("fromUser", user.getObjectId()+"");
		query.whereEqualTo("toUser", currentUser.getObjectId()+"");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> list, ParseException e) {
		    	if (e == null && list.size()>0) {
		    		ParseObject relation = list.get(0);
        	    	relation.put("areFriends", true);
        	    	relation.saveInBackground();
        	    	new AlertDialog.Builder(Friends.this)
				    .setTitle("This User Already Sent You a Request!")
				    .setMessage("You Two Are Now Friends!")
				    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	add.setEnabled(true);
				        	getRelations();
		        	    	setNotifications();
				        }
				     })
				    .show();
		        }
		    	else{
		    		createRelation(user);
		    	}
		    }
		});
	}
	
	public void createRelation(final ParseUser user){
		ParseObject relation = new ParseObject("Friends");
    	relation.put("fromUser",  currentUser.getObjectId()+"");
    	relation.put("toUser", user.getObjectId()+"");
    	relation.put("areFriends", false);
    	relation.saveInBackground();
    	new AlertDialog.Builder(Friends.this)
	    .setTitle("Request Sent to "+ user.getUsername()+"!")
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	add.setEnabled(true);
	        }
	     })
	    .show();
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
	public void setNotifications(){
		notifications = (TextView)findViewById(R.id.Notifications);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("toUser", currentUser.getObjectId()+"");
		query.whereEqualTo("areFriends", false);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> list, ParseException e) {
		        if (e == null) {
		        	notifications.setText(list.size()+"");
		        }
		    }
		});	
	}
	

	public class FriendsAdapter extends BaseAdapter{
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
		        LayoutInflater inflater = (LayoutInflater) Friends.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        arg1 = inflater.inflate(R.layout.friends_item, arg2,false);
		        wpm = (TextView)arg1.findViewById(R.id.UserWPM);
				username = (TextView)arg1.findViewById(R.id.UsernameField);
		    }
			ImageButton settings = (ImageButton)arg1.findViewById(R.id.FriendsSettings);
			settings.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		        	View popupView = layoutInflater.inflate(R.layout.friends_popout, null);
		        	float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
		        	
		            final PopupWindow popupWindow = new PopupWindow(popupView,
		                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		            popupWindow.setBackgroundDrawable(new BitmapDrawable());
		            popupWindow.setOutsideTouchable(true);
		            popupWindow.setTouchable(true);
		            popupWindow.setFocusable(true);
		            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0); 
		           
		            popupusername=(TextView)popupView.findViewById(R.id.PopupUsername);
		            popupusername.setText(friendsList.get(arg0).getUsername()+"");
		            
		            TextView popupunfriend=(TextView)popupView.findViewById(R.id.PopupUnfriend);
		            popupunfriend.setOnClickListener(new OnClickListener()
		            {
		                @Override
		                public void onClick(View v)
		                {
		                    ParseQuery<ParseObject> query = new ParseQuery("Friends");
		            		query.whereEqualTo("fromUser", currentUser.getObjectId()+"");
		            		query.whereEqualTo("toUser", friendsList.get(arg0).getObjectId()+"");
		            		query.whereEqualTo("areFriends", true);
		            		query.findInBackground(new FindCallback<ParseObject>() {
		            		    public void done(List<ParseObject> list, ParseException e) {
		            		        if (e == null&&list.size()>0) {
		            		        	ParseObject relation = list.get(0);
		            		        	relation.deleteInBackground(new DeleteCallback() {
											@Override
											public void done(ParseException arg0) {
												getRelations();
							        	    	setNotifications();
												popupWindow.dismiss();
											}
		    		            		});
		            		        }
		            		    }
		            		});	
		            		
		            		ParseQuery<ParseObject> query1 = new ParseQuery("Friends");
		            		query1.whereEqualTo("toUser", currentUser.getObjectId()+"");
		            		query1.whereEqualTo("fromUser", friendsList.get(arg0).getObjectId()+"");
		            		query1.whereEqualTo("areFriends", true);
		            		query1.findInBackground(new FindCallback<ParseObject>() {
		            		    public void done(List<ParseObject> list, ParseException e) {
		            		        if (e == null&&list.size()>0) {
		            		        	ParseObject relation = list.get(0);
		            		        	relation.deleteInBackground(new DeleteCallback() {
											@Override
											public void done(ParseException arg0) {
												getRelations();
												setNotifications();
												popupWindow.dismiss();
											}
		    		            		});
		            		        }
		            		    }
		            		});	
		                }
		            });

		            TextView popupcancel=(TextView)popupView.findViewById(R.id.PopupCancel);
		            popupcancel.setOnClickListener(new OnClickListener()
		            {
		                @Override
		                public void onClick(View v)
		                {
		                	popupWindow.dismiss();
		                }
		            });

		        }
		        
		    });
			
			findViewById(R.id.LoadingIcon).setVisibility(View.INVISIBLE);
			wpm.setText(friendsList.get(arg0).getInt("AWPM")+"");
			username.setText(friendsList.get(arg0).getUsername()+"");
			
			return arg1;
			
		}
	}
	}
	
	
	
	
	
	
	
	
	
	

