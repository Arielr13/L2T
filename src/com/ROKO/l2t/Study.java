package com.ROKO.l2t;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Study extends Activity {
	
	DictionaryAdapter dictionaryAdapter;
	ListView dictionary;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study);
        
        
        //Set Adapter for ListView
        dictionaryAdapter = new DictionaryAdapter();
        dictionary = (ListView)findViewById(R.id.Dictionary);
        dictionary.setAdapter(dictionaryAdapter);
        
        //Read input from search bar after every key press
        EditText searchbar = (EditText)findViewById(R.id.Search);
        searchbar.addTextChangedListener(new TextWatcher() {          
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {                                   
                    String string = s.toString();
                    dictionaryAdapter.refreshSearch(string);
                    dictionary.setAdapter(dictionaryAdapter);
            }
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {				
			}
			@Override
			public void afterTextChanged(Editable s) {
			}                       
        });
        
    }
	
	//Define Definition class
	public class Definition implements Comparable<Definition>{
		String abbreviation;
		String definition;
		
		@Override
		public int compareTo(Definition another) {
			return  abbreviation.compareTo(another.abbreviation);
		}
	}

	//Custom adapter for dictionary
	public class DictionaryAdapter extends BaseAdapter{

		List<Definition> definitionList = getDataForListView("");
		
		public void refreshSearch (String s){
			definitionList = getDataForListView(s);
		}
		
		@Override
		public int getCount() {
			return definitionList.size();
		}

		@Override
		public Definition getItem(int arg0) {
			return definitionList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null){
		        LayoutInflater inflater = (LayoutInflater) Study.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        arg1 = inflater.inflate(R.layout.dictionary_item, arg2,false);
		    }
			
			TextView Abbreviation = (TextView)arg1.findViewById(R.id.Abbreviation);
			TextView Definition = (TextView)arg1.findViewById(R.id.Definition);
			
			Definition definition = definitionList.get(arg0);
			
			Abbreviation.setText(definition.abbreviation);
			Definition.setText(definition.definition);
			
			return arg1;
		}
		
		public Definition getDefinition(int position){
			return definitionList.get(position);
		}
		
		//Create shortcut method of adding definitions
		public Definition set(String abb, String def){
			
			Definition definition = new Definition();
			definition.abbreviation = abb;
			definition.definition = def;
			
			
			return definition;
		}
		//Define list of definitions to be used by the adapter
		public List<Definition> getDataForListView(String search){
			
		    	List<Definition> definitionsList = new ArrayList<Definition>();	    	
		    	definitionsList.add(set(
		    			"2MORO",
		    			"Tomorrow"
		    			));
		    			definitionsList.add(set(
		    			"2NITE",
		    			"Tonight"
		    			));
		    			definitionsList.add(set(
		    			"BRB",
		    			"Be Right Back"
		    			));
		    			definitionsList.add(set(
		    			"BTW",
		    			"By The Way"
		    			));
		    			definitionsList.add(set(
		    			"B4N",
		    			"Bye For Now"
		    			));
		    			definitionsList.add(set(
		    			"BCNU",
		    			"Be Seeing You"
		    			));
		    			definitionsList.add(set(
		    			"DBEYR",
		    			"Don’t Believe Everything You Read"
		    			));
		    			definitionsList.add(set(
		    			"DILLIGAD",
		    			"Do I Look Like I Give A Damn?"
		    			));
		    			definitionsList.add(set(
		    			"CYA",
		    			"See Ya"
		    			));
		    			definitionsList.add(set(
		    			"FUD",
		    			"Fear, Uncertainty, and Disinformation "
		    			));
		    			definitionsList.add(set(
		    			"FWIW ",
		    			"For What It's Worth -or- Forgot Where I Was"
		    			));
		    			definitionsList.add(set(
		    			"GR8",
		    			"Great"
		    			)); definitionsList.add(set(
		    			"ILY",
		    			"I Love You"
		    			));
		    			definitionsList.add(set(
		    			"IMHO",
		    			"In My Humble Opinion"
		    			));
		    			definitionsList.add(set(
		    			"IRL",
		    			"In Real Life "
		    			));
		    			definitionsList.add(set(
		    			"ISO ",
		    			"In Search Of"
		    			));
		    			definitionsList.add(set(
		    			"JK",
		    			"Just Kidding"
		    			));

		    			definitionsList.add(set(
		    			"L8R",
		    			"Later"
		    			));
		    			definitionsList.add(set(
		    			"LMBO",
		    			"Laughing My Butt Off"
		    			));
		    			definitionsList.add(set(
		    			"LOL",
		    			"Laughing Out Loud"
		    			));
		    			definitionsList.add(set(
		    			"LYLAS",
		    			"Love You Like A Sister"
		    			));
		    			definitionsList.add(set(
		    			"MHOTY",
		    			"My Hat's Off To You"
		    			));
		    			definitionsList.add(set(
		    			"NIMBY",
		    			"Not In My Back Yard"
		    			));
		    			definitionsList.add(set(
		    			"NP",
		    			"No Problem -or- Nosy Parents"
		    			));
		    			definitionsList.add(set(
		    			"NUB",
		    			"New person to a site or game"
		    			));
		    			definitionsList.add(set(
		    			"OIC",
		    			"Oh, I See"
		    			));
		    			definitionsList.add(set(
		    			"OMG",
		    			"Oh My God "
		    			));
		    			definitionsList.add(set(
		    			"OT",   
		    			"Off Topic"
		    			));
		    			definitionsList.add(set(
		    			"POV",
		    			"Point Of View"
		    			)); definitionsList.add(set(
		    			"RBTL",
		    			"Read Between The Lines"
		    			));
		    			definitionsList.add(set(
		    			"BFF",
		    			"Best Friends Forever "
		    			));
		    			definitionsList.add(set(
		    			"RT",
		    			"Real Time"
		    			));
		    			definitionsList.add(set(
		    			"THX or TX or THKS ",
		    			"Thanks"
		    			));
		    			definitionsList.add(set(
		    			"SH",
		    			"Stuff Happens"
		    			));
		    			definitionsList.add(set(
		    			"SITD",
		    			"Still In The Dark"
		    			));
		    			definitionsList.add(set(
		    			"SOL",
		    			"Sooner Or Later "
		    			));
		    			definitionsList.add(set(
		    			"STBY",
		    			"Sucks To Be You"
		    			));
		    			definitionsList.add(set(
		    			"SWAK",
		    			"Sealed (or Sent) With A Kiss"
		    			));
		    			definitionsList.add(set(
		    			"RTM",
		    			"Read The Manual"
		    			));
		    			definitionsList.add(set(
		    			"TLC",
		    			"Tender Loving Care"
		    			));
		    			definitionsList.add(set(
		    			"TMI",
		    			"Too Much Information"
		    			));
		    			definitionsList.add(set(
		    			"TTYL",
		    			"Talk To You Later"
		    			));
		    			definitionsList.add(set(
		    			"TYVM",
		    			"Thank You Very Much"
		    			));
		    			definitionsList.add(set(
		    			"VBG ",
		    			"Very Big Grin"
		    			));
		    			definitionsList.add(set(
		    			"WEG",
		    			"Wicked Evil Grin"
		    			)); definitionsList.add(set(
		    			"WYWH",
		    			"Wish You Were Here"
		    			));



	
		    	//Search logic
		    	List<Definition> finalDefinitionsList = new ArrayList<Definition>();
		    	for(int i=0;i<definitionsList.size();i++){
		            if(definitionsList.get(i).abbreviation.toLowerCase().contains(search.toLowerCase()) || definitionsList.get(i).definition.toLowerCase().contains(search.toLowerCase())){
		            	finalDefinitionsList.add(definitionsList.get(i));
		            }
		    	}

		    	Collections.sort(finalDefinitionsList);
		    	return finalDefinitionsList;
		    	
		}
	}
}
