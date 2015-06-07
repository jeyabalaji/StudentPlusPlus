package com.teamjass.student;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class Fragment_21_Day extends SherlockFragment {

	SQLiteDatabase db;
	Spinner spinner;
	TextView tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_21_ttday, container, false);
		tv = (TextView) rootView.findViewById(R.id.tt_Day);
        //final ActionBar actionBar = getActivity().getActionBar();
        //actionBar.setIcon(R.drawable.ic_tt);
		// Locate the ViewPager in viewpager_main.xml
		//ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
		// Set the ViewTTPageAdapter into ViewPager
		//mViewPager.setAdapter(new ViewTTPageAdapter(getChildFragmentManager()));
		spinner = (Spinner) rootView.findViewById(R.id.spinner_day);
        List<String> sub = new ArrayList<String>();
        sub.add("MONDAY");
        sub.add("TUESDAY");
        sub.add("WEDNESDAY");
        sub.add("THURSDAY");
        sub.add("FRIDAY");
        
     // Create an ArrayAdapter using the string array and a default spinner layout
     ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item, sub);
     // Specify the layout to use when the list of choices appears
     dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
     // Apply the adapter to the spinner
     Log.e("NULL POINTER", "DATA ADAPTER = "+dataAdapter);
     Log.e("NULL POINTER", "SPINNER = "+spinner);
     spinner.setAdapter(dataAdapter);
     spinner.setSelection(0);
     spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	 public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
    				long arg3) {  		 	
    			getTableValues();
    		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}
     });
     
		return rootView;
	}
	
	
	
	public void getTableValues() {
		  
  
        try {        	
        	String day = spinner.getSelectedItem().toString();
        	db= getActivity().openOrCreateDatabase("mydb.db", android.content.Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM time_table where day = '"+day+"';", null);
            
        	allrows.moveToFirst();
        	Log.e("TIME TABLE", ""+allrows);
        	String h[] = new String[10];
        	String code;
        	for(int i=1;i<8;i++)
        	{
        		code = allrows.getString(i);
        		Log.e("TIME TABLE", "CODE is "+code);
        		Cursor name = db.rawQuery("SELECT title FROM subject WHERE sub_code='"+code+"';", null);
        		name.moveToFirst();
        		h[i]=name.getString(0);
        	}
//        	String h1 = allrows.getString(1);
//        	String h2 = allrows.getString(2);
//        	String h3 = allrows.getString(3);
//        	String h4 = allrows.getString(4);
//        	String h5 = allrows.getString(5);
//        	String h6 = allrows.getString(6);
//        	String h7 = allrows.getString(7);
        	tv.setText("HOUR 1 - "+h[1]+
        				"\nHOUR 2 - "+h[2]+
        				"\nHOUR 3 - "+h[3]+
        				"\nHOUR 4 - "+h[4]+
        				"\nHOUR 5 - "+h[5]+
        				"\nHOUR 6 - "+h[6]+
        				"\nHOUR 7 - "+h[7]);
        }
        catch(Exception e)
        {
        	Log.e("TT Day", ""+e);
        }
                  
    }

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}
