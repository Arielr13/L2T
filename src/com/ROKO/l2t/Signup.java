package com.ROKO.l2t;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends Activity {
	
	EditText usernamefield;
	EditText emailfield;
	EditText passwordfield;
	EditText confirmpasswordfield;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		ParseUser.logOut();
	}
	
	public void signup (View v){
		usernamefield = (EditText)findViewById(R.id.SignupUsername);
		String username = usernamefield.getText().toString().toLowerCase().trim();
		emailfield = (EditText)findViewById(R.id.SignupEmail);
		String email = emailfield.getText().toString().toLowerCase().trim();
		passwordfield = (EditText)findViewById(R.id.SignupPassword);
		String password = passwordfield.getText().toString().toLowerCase().trim();
		confirmpasswordfield = (EditText)findViewById(R.id.SignupConfirmPassword);
		String confirmpassword = confirmpasswordfield.getText().toString().toLowerCase().trim();
		
		if(!(password.equals(confirmpassword))){
			new AlertDialog.Builder(Signup.this)
		    .setTitle("Passwords Don't Match!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show();
		}
		else if(username.equals("")|username==null){
			new AlertDialog.Builder(Signup.this)
		    .setTitle("Please Enter a Username!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show();
		}
		else if(email.equals("")|email==null){
			new AlertDialog.Builder(Signup.this)
		    .setTitle("Please Enter an Email!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show();
		}
		else if(password.equals("")|password==null){
			new AlertDialog.Builder(Signup.this)
		    .setTitle("Please Enter a Password!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show();
		}
		else if(confirmpassword.equals("")|confirmpassword==null){
			new AlertDialog.Builder(Signup.this)
		    .setTitle("Please Confirm Your Password!")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		          
		        }
		     })
		    .show();
		}
		else{
			ParseUser user = new ParseUser();
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail(email);
			user.put("levelsUnlocked", 1);
			user.put("trialsCompleted", 0);
			user.put("AWPM", 0);
			user.put("tokenCount", 0);
			user.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			      Intent home = new Intent(Signup.this, Home.class);
			      startActivity(home);
			    } else {
			    	String error1 = e.toString().replace("com.parse.ParseRequest$ParseRequestException: ","");
			    	String error2 = Character.toUpperCase(error1.charAt(0)) + error1.substring(1);
			    	new AlertDialog.Builder(Signup.this)
				    .setTitle(error2+"!")
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
	public void signupFacebook (View v){
		ParseFacebookUtils.logInWithReadPermissionsInBackground(this, null, new LogInCallback() {
			  @Override
			  public void done(ParseUser user, ParseException err) {
			    if (user == null) {
			    } 
			    else if (user.isNew()) {
			    	Intent signup =  new Intent(Signup.this, FacebookSignup.class);
			    	startActivity(signup);
			    	finish();
			    } 
			    else {
			    	Intent home = new Intent(Signup.this, Home.class);
				    startActivity(home);
				    finish();
			    }
			  }
			});
	}
	public void alreadyAccount (View v){
		Intent login = new Intent (this, Login.class);
		startActivity(login);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
	}
}
