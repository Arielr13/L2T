package com.ROKO.l2t;

import java.io.ByteArrayOutputStream;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
			String lastname = "";
			ParseUser user = new ParseUser();
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail(email);
			user.put("levelsUnlocked", 1);
			user.put("trialsCompleted", 0);
			user.put("AWPM", 0);
			user.put("tokenCount", 0);
			user.put("body", 1);
			user.put("eyes", 1);
			user.put("teeth", 1);
			
			//Avatar Stuff
			int bodyWidth = 350;
			int bodyHeight = 350;
			int originalWidth = 700;
			int originalHeight = 700;
			
			
			Bitmap main = Bitmap.createBitmap(350, 350, Config.ARGB_8888);
			Bitmap prebody = BitmapFactory.decodeResource(getResources(), R.drawable.body_1);
			Bitmap body = prebody.copy(Bitmap.Config.ARGB_8888, true);
			Bitmap preeye= BitmapFactory.decodeResource(getResources(), R.drawable.eyes_1);
			Bitmap eye = preeye.copy(Bitmap.Config.ARGB_8888, true);
			Bitmap preteeth= BitmapFactory.decodeResource(getResources(), R.drawable.teeth_1);
			Bitmap teeth = preteeth.copy(Bitmap.Config.ARGB_8888, true);
			
			Canvas canvas = new Canvas(main);
			float scale = getResources().getDisplayMetrics().density;
			canvas.drawBitmap(body, new Rect(0, 0, originalWidth, originalHeight), new Rect(0, 0, bodyWidth, bodyHeight), null);		//Main body is 900*900, width a padding of 50
			canvas.drawBitmap(eye, new Rect(0, 0, originalWidth, originalHeight), new Rect(0, 0, bodyWidth, bodyHeight), null);
			canvas.drawBitmap(teeth, new Rect(0, 0, originalWidth, originalHeight), new Rect(0, 0, bodyWidth, bodyHeight), null);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    main.compress(Bitmap.CompressFormat.PNG, 100, stream);
		    byte[] data = stream.toByteArray();
		    
		    //Finish Avatar Stuff
		    user.put("avatar", data);
		    
			user.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			      Intent home = new Intent(Signup.this, Home.class);
			      ROKOLogger.addEvent(new Event("User Signed Up w/o Facebook"));
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
				    ROKOLogger.addEvent(new Event("User Logged In With Facebook"));
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
