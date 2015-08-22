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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsNotifications extends Activity {
	ParseUser currentUser = ParseUser.getCurrentUser();
	int size;
	ListView lv;
	RequestsAdapter adapter=new RequestsAdapter();
	String friendid;
	List<ParseUser>friendsList = new ArrayList<ParseUser>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_notifications);
		lv = (ListView)findViewById(R.id.RequestsList);
		getRelations();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		getRelations();
	}
	
	public void getRelations(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("toUser", currentUser.getObjectId()+"");
		query.whereEqualTo("areFriends", false);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> list, ParseException e) {
		        if (e == null) {
		        	getUsers(list, list.size(),0);
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
			friendid = list.get(i).getString("fromUser");
			
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.getInBackground(friendid, new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser arg0, ParseException arg1) {
					addUser(arg0);
					getUsers(list, listSize,i+1);
				}
				
			});
		}
	}
	public void addUser(ParseUser user){
		friendsList.add(user);
	}
		
	public class RequestsAdapter extends BaseAdapter{
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
		        LayoutInflater inflater = (LayoutInflater) FriendsNotifications.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        arg1 = inflater.inflate(R.layout.friends_notification_item, arg2,false);
		    }
			findViewById(R.id.NotificationsLoadingIcon).setVisibility(View.INVISIBLE);
			
			TextView username = (TextView)arg1.findViewById(R.id.RequestsUsername);
			username.setText(friendsList.get(arg0).getUsername()+"");
			
			Button accept = (Button)arg1.findViewById(R.id.AcceptButton);
			Button reject = (Button)arg1.findViewById(R.id.RejectButton);
			
			accept.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
            		query.whereEqualTo("toUser", currentUser.getObjectId()+"");
            		query.whereEqualTo("fromUser", friendsList.get(arg0).getObjectId()+"");
            		query.whereEqualTo("areFriends", false);
            		query.findInBackground(new FindCallback<ParseObject>() {
            		    public void done(List<ParseObject> list, ParseException e) {
            		        if (e == null) {
            		        	ParseObject relation = list.get(0);
            		        	relation.put("areFriends", true);
            		        	relation.saveInBackground();
            		        	getRelations();
            		        }
            		    }
            		});		
                }
            });
			reject.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
            		query.whereEqualTo("toUser", currentUser.getObjectId()+"");
            		query.whereEqualTo("fromUser", friendsList.get(arg0).getObjectId()+"");
            		query.whereEqualTo("areFriends", false);
            		query.findInBackground(new FindCallback<ParseObject>() {
            		    public void done(List<ParseObject> list, ParseException e) {
            		        if (e == null) {
            		        	ParseObject relation = list.get(0);
            		        	relation.deleteInBackground();
            		        	getRelations();
            		        }
            		    }
            		});		
                }
            });
			
			return arg1;
			
		}
	}
}

