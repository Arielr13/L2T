package com.ROKO.l2t;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.rokomobi.sdk.ROKOLogger;
import com.rokomobi.sdk.analytics.Event;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
		  startActivity(new Intent(Login.this, Home.class));
		} 
	}
	
	public void signup (View v){
		Intent signup = new Intent (this, Signup.class);
		startActivity(signup);
	}
	public void login (View v){
		EditText usernamefield = (EditText)findViewById(R.id.LoginUsername);
		String username = usernamefield.getText().toString().toLowerCase().trim();
		EditText passwordfield = (EditText)findViewById(R.id.LoginPassword);
		String password = passwordfield.getText().toString().toLowerCase().trim();
		
		if(username.equals("")|username==null){
			new AlertDialog.Builder(Login.this)
		    .setTitle("Please Enter a Username!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show();
		}
		else if(password.equals("")|password==null){
			new AlertDialog.Builder(Login.this)
		    .setTitle("Please Enter a Password!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show();
		}
		else{
			ParseUser.logInInBackground(username, password, new LogInCallback() {
					  public void done(ParseUser user, ParseException e) {
					    if (user != null) {
					    	Intent home = new Intent(Login.this, Home.class);
					    	ROKOLogger.addEvent(new Event("User Logged In w/o Facebook"));
						    startActivity(home);
					    } 
					    else {
					    	new AlertDialog.Builder(Login.this)
						    .setTitle("Incorrrect Username/Password!")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						          
						        }
						     })
						    .show();
					    }
					  }
				});
			}
	}
	public void facebookLogin (View v){
		ParseFacebookUtils.logInWithReadPermissionsInBackground(this, null, new LogInCallback() {
			  @Override
			  public void done(ParseUser user, ParseException err) {
			    if (user == null) {
			    } 
			    else if (user.isNew()) {
			    	Intent signup =  new Intent(Login.this, FacebookSignup.class);
			    	startActivity(signup);
			    	finish();
			    } 
			    else {
			    	Intent home = new Intent(Login.this, Home.class);
				    startActivity(home);
				    ROKOLogger.addEvent(new Event("User Logged In With Facebook"));
				    finish();
			    }
			  }
			});
	}
	public void forgotPassword (View v){
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
	}
}
