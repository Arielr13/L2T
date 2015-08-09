package com.ROKO.l2t;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ROKO.l2t.Study.Definition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LevelSelect extends Activity {

	int unlocked = 5;
	LevelSelectAdapter levelselectadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_select);
		
		ListView lv = (ListView)findViewById(R.id.LevelListView);
		levelselectadapter = new LevelSelectAdapter();
		lv.setAdapter(levelselectadapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				int level = levelselectadapter.getItem(arg2);
				if(level>unlocked){
					Toast.makeText(getApplicationContext(), "Level Locked" ,Toast.LENGTH_SHORT).show();
				}
				else{
					Intent train = new Intent(LevelSelect.this, Race.class);
					train.putExtra("level", level);
					startActivity(train);
				}
			}
		});
	}
	
	public class LevelSelectAdapter extends BaseAdapter{
		
		List<Integer> levelList = new ArrayList<Integer>();	 
		public LevelSelectAdapter(){
			for(int i=1; i<=10; i++){
				levelList.add(i);
			}
		}
		
		@Override
		public int getCount() {
			return levelList.size();
		}

		@Override
		public Integer getItem(int arg0) {
			return levelList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			if(arg1==null){
		        LayoutInflater inflater = (LayoutInflater) LevelSelect.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        arg1 = inflater.inflate(R.layout.level_select_item, arg2,false);;
		    }
			
			TextView LevelText = (TextView)arg1.findViewById(R.id.LevelText);
			Integer level = levelList.get(arg0);
			LevelText.setText("Level " +level);
			LevelText.setTextColor(Color.parseColor("#ffffff"));
			
			
			if(level>unlocked){
				LevelText.setTextColor(Color.parseColor("#505050"));
			}
			if(level>=unlocked){
				ImageView trophy = (ImageView)arg1.findViewById(R.id.TrophyImage);
				trophy.setImageResource(R.drawable.trophy);
			}
			if(level<unlocked){
				ImageView trophy = (ImageView)arg1.findViewById(R.id.TrophyImage);
				trophy.setImageResource(R.drawable.trophy_active);
			}
			return arg1;
		}
	}
}
