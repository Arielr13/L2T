package com.ROKO.l2t;

import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller.Session;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class FacebookSignup extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_signup);
	}
	
	public void signup(View v){
		EditText usernamefield = (EditText)findViewById(R.id.SignupFacebookUsername);
		String username = usernamefield.getText().toString().toLowerCase().trim();
		EditText emailfield = (EditText)findViewById(R.id.SignupFacebookEmail);
		String email = emailfield.getText().toString().toLowerCase().trim();
		
		if(username.equals("")|username==null){
			new AlertDialog.Builder(FacebookSignup.this)
		    .setTitle("Please Enter a Username!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show();
		}
		else if(email.equals("")|email==null){
			new AlertDialog.Builder(FacebookSignup.this)
		    .setTitle("Please Enter an Email!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show(); 
		}
		else if(!(email.contains("@")&&email.contains("."))){
			new AlertDialog.Builder(FacebookSignup.this)
		    .setTitle("Please Enter a Valid Email!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show(); 
		}
		else{
			ParseUser user = ParseUser.getCurrentUser();
			user.setEmail(email);
			user.setUsername(username);
			user.put("levelsUnlocked", 1);
			user.put("trialsCompleted", 0);
			user.put("AWPM", 0);
			user.put("tokenCount", 0);
			user.saveInBackground();
			startActivity(new Intent(this, Home.class));
			finish();
		}
	}
}
