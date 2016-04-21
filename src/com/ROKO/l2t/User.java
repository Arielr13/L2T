package com.ROKO.l2t;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rokomobi.sdk.ROKOLogger;
import com.rokomobi.sdk.analytics.Event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class User extends FragmentActivity {
	
	FileOutputStream fOut;
	ParseUser currentUser = ParseUser.getCurrentUser();
	Button bodyButton;
	Button eyesButton;
	Button teethButton;
	ViewPager pager;
	int body = currentUser.getInt("body");
	int eye = currentUser.getInt("eyes");
	int teeth = currentUser.getInt("teeth");
	Bitmap main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);
		//setImage();
		
		bodyButton = (Button)findViewById(R.id.BodyButton);
		eyesButton = (Button)findViewById(R.id.EyeButton);
		teethButton = (Button)findViewById(R.id.TeethButton);
		
		pager = (ViewPager) findViewById(R.id.userViewPager);
		pager.setOffscreenPageLimit(3);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        
        TextView tokens = (TextView)findViewById(R.id.UserTokens);
        tokens.setText(currentUser.getInt("tokenCount")+" Tokens");
        
        final ImageView imageView = (ImageView)findViewById(R.id.PreviewImage);
        //Bitmap bmp = BitmapFactory.decodeByteArray(currentUser.getBytes("avatar"), 0, currentUser.getBytes("avatar").length);
	    //imageView.setImageBitmap(bmp);
	    setImage(body, eye, teeth);
	}
	
	
	private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
        	BodyFragment bodyFragment = new BodyFragment();
        	EyesFragment eyesFragment = new EyesFragment();
        	TeethFragment teethFragment = new TeethFragment();
        	
            switch(pos) {

            case 0: return bodyFragment.newInstance();
            case 1: return eyesFragment.newInstance();
            case 2: return teethFragment.newInstance();
            default: return bodyFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }       
    }
	
	public class BodyFragment extends Fragment {
	    Boolean started=false;
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.user_fragment, container, false);
	        GridView gridview = (GridView)v.findViewById(R.id.GridView);
	        gridview.setAdapter(new ImageAdapter(User.this, 1));
	        gridview.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	                body = position+1;
	                setImage(body, eye, teeth);
	            }
	        });
	        
	        started=true;
	        return v;
	    }
	    
	    @Override
	    public void setMenuVisibility(final boolean visible) {
	        super.setMenuVisibility(visible);
	        if (visible&&started) {
	        	 bodyButton.setBackgroundColor(getResources().getColor(R.color.blue));
			     eyesButton.setBackgroundColor(getResources().getColor(R.color.background));
			     teethButton.setBackgroundColor(getResources().getColor(R.color.background));
	        }
	    }
	    
	    public BodyFragment newInstance(){
	        BodyFragment f = new BodyFragment();
	        return f;
	    }
	}
	
	public class EyesFragment extends Fragment {
	    Boolean started=false;
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.user_fragment, container, false);
	        GridView gridview = (GridView)v.findViewById(R.id.GridView);
	        gridview.setAdapter(new ImageAdapter(User.this, 2));
	        gridview.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	                eye = position+1;
	                setImage(body, eye, teeth);
	            }
	        });
	        started=true;
	        return v;
	    }
	    
	    @Override
	    public void setMenuVisibility(final boolean visible) {
	        super.setMenuVisibility(visible);
	        if (visible&&started) {
	        	bodyButton.setBackgroundColor(getResources().getColor(R.color.background));
			     eyesButton.setBackgroundColor(getResources().getColor(R.color.blue));
			     teethButton.setBackgroundColor(getResources().getColor(R.color.background));
	        }
	    }
	    
	    public EyesFragment newInstance(){
	        EyesFragment f = new EyesFragment();
	        return f;
	    }
	}
	
	public class TeethFragment extends Fragment {
	    Boolean started=false;
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.user_fragment, container, false);
	        GridView gridview = (GridView)v.findViewById(R.id.GridView);
	        gridview.setAdapter(new ImageAdapter(User.this, 3));
	        gridview.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	                teeth = position+1;
	                setImage(body, eye, teeth);
	            }
	        });
	        started=true;
	        return v;
	    }
	    
	    @Override
	    public void setMenuVisibility(final boolean visible) {
	        super.setMenuVisibility(visible);
	        if (visible&&started) {
	        	bodyButton.setBackgroundColor(getResources().getColor(R.color.background));
			     eyesButton.setBackgroundColor(getResources().getColor(R.color.background));
			     teethButton.setBackgroundColor(getResources().getColor(R.color.blue));
	        }
	    }
	    
	    public TeethFragment newInstance(){
	        TeethFragment f = new TeethFragment();
	        return f;
	    }
	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private int bodyPart;

	    public ImageAdapter(Context c, int b) {
	        mContext = c;
	        bodyPart = b;
	    }

	    public int getCount() {
	    	if(bodyPart==1){
	    		return 9;
	    	}
	    	else{
	    		return 6;
	    	}
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {
	            // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            //imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setAdjustViewBounds(true);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        
	        if(bodyPart==1){
	        	imageView.setImageResource(bodyIds[position]);
	        	imageView.setPadding(50, 50, 50, 50);
	    	}
	    	else if (bodyPart==2){
	    		imageView.setImageResource(eyeIds[position]);
	    	}
	        else{
	        	imageView.setImageResource(teethIds[position]);
	        }
	        
	        return imageView;
	    }

	    // references to our images
	    private Integer[] bodyIds = {
	            R.drawable.body_1, R.drawable.body_2,
	            R.drawable.body_3, R.drawable.body_4,
	            R.drawable.body_5, R.drawable.body_6,
	            R.drawable.body_7, R.drawable.body_8,
	            R.drawable.body_9,
	    };
	    private Integer[] eyeIds = {
	            R.drawable.eyes_1, R.drawable.eyes_2,
	            R.drawable.eyes_3, R.drawable.eyes_4,
	            R.drawable.eyes_5, R.drawable.eyes_6,
	    };
	    private Integer[] teethIds = {
	            R.drawable.teeth_1, R.drawable.teeth_2,
	            R.drawable.teeth_3, R.drawable.teeth_4,
	            R.drawable.teeth_5, R.drawable.teeth_6,
	    };
	}
	
	public void Body(View v){
		pager.setCurrentItem(0);
	}
	public void Mouth(View v){
		pager.setCurrentItem(1);
	}
	public void Teeth(View v){
		pager.setCurrentItem(2);
	}
	public void save(View v){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    main.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    byte[] data = stream.toByteArray();
	    
	    currentUser.put("avatar", data);
	    currentUser.saveInBackground(new SaveCallback() {
	    	public void done(ParseException e) {
	    		if (e == null) {
	    			currentUser.put("body", body);
	    			currentUser.put("eyes", eye);
	    			currentUser.put("teeth", teeth);
	    			currentUser.saveInBackground();
	    			startActivity(new Intent(User.this, Home.class));
	    			ROKOLogger.addEvent(new Event("User Saved New Avatar"));
	    		} 
	    	}
	    });
	}
	
	//----------------------------------------------------------------------------------------//
	
	public void setImage(int b, int e, int t){
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();  // deprecated
		int height = display.getHeight(); //deprecated
		
		int bodyWidth = 350;
		int bodyHeight = 350;
		int originalWidth = 700;
		int originalHeight = 700;
		
		int bodyResource;
		int eyeResource;
		int teethResource;
		switch(b){
			case 1: bodyResource = R.drawable.body_1;break;
			case 2: bodyResource = R.drawable.body_2;break;
			case 3: bodyResource = R.drawable.body_3;break;
			case 4: bodyResource = R.drawable.body_4;break;
			case 5: bodyResource = R.drawable.body_5;break;
			case 6: bodyResource = R.drawable.body_6;break;
			case 7: bodyResource = R.drawable.body_7;break;
			case 8: bodyResource = R.drawable.body_8;break;
			case 9: bodyResource = R.drawable.body_9;break;
			default: bodyResource = R.drawable.body_1;break;
		}
		switch(e){
			case 1: eyeResource = R.drawable.eyes_1;break;
			case 2: eyeResource = R.drawable.eyes_2;break;
			case 3: eyeResource = R.drawable.eyes_3;break;
			case 4: eyeResource = R.drawable.eyes_4;break;
			case 5: eyeResource = R.drawable.eyes_5;break;
			case 6: eyeResource = R.drawable.eyes_6;break;
			default: eyeResource = R.drawable.eyes_1;break;
		}
		switch(t){
			case 1: teethResource = R.drawable.teeth_1;break;
			case 2: teethResource = R.drawable.teeth_2;break;
			case 3: teethResource = R.drawable.teeth_3;break;
			case 4: teethResource = R.drawable.teeth_4;break;
			case 5: teethResource = R.drawable.teeth_5;break;
			case 6: teethResource = R.drawable.teeth_6;break;
			default: teethResource = R.drawable.teeth_1;break;
		}
		
		final ImageView imageView = (ImageView)findViewById(R.id.PreviewImage);
		//Bitmap premain = BitmapFactory.decodeResource(getResources(), R.drawable.temp_monster);
		main = Bitmap.createBitmap(350, 350, Config.ARGB_8888);
		//Bitmap main = premain.copy(Bitmap.Config.ARGB_8888, true);
		Bitmap prebody = BitmapFactory.decodeResource(getResources(), bodyResource);
		Bitmap body = prebody.copy(Bitmap.Config.ARGB_8888, true);
		Bitmap preeye= BitmapFactory.decodeResource(getResources(), eyeResource);
		Bitmap eye = preeye.copy(Bitmap.Config.ARGB_8888, true);
		Bitmap preteeth= BitmapFactory.decodeResource(getResources(), teethResource);
		Bitmap teeth = preteeth.copy(Bitmap.Config.ARGB_8888, true);
		
		Canvas canvas = new Canvas(main);
		float scale = getResources().getDisplayMetrics().density;
		canvas.drawBitmap(body, new Rect(0, 0, originalWidth, originalHeight), new Rect(0, 0, bodyWidth, bodyHeight), null);		//Main body is 900*900, width a padding of 50
		canvas.drawBitmap(eye, new Rect(0, 0, originalWidth, originalHeight), new Rect(0, 0, bodyWidth, bodyHeight), null);
		canvas.drawBitmap(teeth, new Rect(0, 0, originalWidth, originalHeight), new Rect(0, 0, bodyWidth, bodyHeight), null);
		imageView.setImageBitmap(main);
		
		
		/*
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    main.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    byte[] data = stream.toByteArray();
	    
	    currentUser.put("avatar", data);
	    currentUser.saveInBackground(new SaveCallback() {
	    	public void done(ParseException e) {
	    		if (e == null) {
	    			Bitmap bmp = BitmapFactory.decodeByteArray(currentUser.getBytes("avatar"), 0, currentUser.getBytes("avatar").length);
	    		    imageView.setImageBitmap(bmp);
	    		} 
	    	}
	    });
	    */
	}

}
