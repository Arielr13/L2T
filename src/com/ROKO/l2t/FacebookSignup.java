package com.ROKO.l2t;

import java.io.ByteArrayOutputStream;

//import com.facebook.AccessToken;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.HttpMethod;
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
import android.content.pm.PackageInstaller.Session;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
			user.saveInBackground();
			ROKOLogger.addEvent(new Event("User Signed Up With Facebook"));
			startActivity(new Intent(this, Home.class));
			finish();
		}
	}
}
