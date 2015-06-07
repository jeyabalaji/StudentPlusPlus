package com.teamjass.student;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class Fragment_22_Subject extends SherlockFragment {

	SQLiteDatabase db;
	Spinner spinner;
	TextView tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_22_ttsub, container, false);
		tv = (TextView) rootView.findViewById(R.id.tt_sub);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_tt);
		spinner = (Spinner) rootView.findViewById(R.id.spinner_subject);
		sublist();
        
    
     spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	 public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
    				long arg3) {
    			// TODO Auto-generated method stub    		 	
    			getTableValues();
    		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
     });
     
		return rootView;
	}
	
	
	
	public void getTableValues() {
		  
  
        try {        	
        	String sub = spinner.getSelectedItem().toString();
        	db= getActivity().openOrCreateDatabase("mydb.db", android.content.Context.MODE_PRIVATE, null);
        	Cursor code = db.rawQuery("select sub_code from subject where title='"+sub+"';", null);
        	code.moveToFirst();
        	String subc = code.getString(0);
            Cursor allrows = db.rawQuery("select day from time_table where " +
            		"hour1 = '"+subc+
            		"' or hour2 = '"+subc+
            		"' or hour3 = '"+subc+
            		"' or hour4 = '"+subc+
            		"' or hour5 = '"+subc+
            		"' or hour6 = '"+subc+
            		"' or hour7 = '"+subc+"';", null);
            String s="";
            while(allrows.moveToNext())
			{
				s = s + (allrows.getString(0))+"\n";
			}
			tv.setText(s);
        	
        }
        catch(Exception e)
        {
        	Log.e("TT", ""+e);
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
	
	
	
	private void sublist(){

		try{
				List<String> sub = new ArrayList<String>();
				db= getActivity().openOrCreateDatabase("mydb.db", android.content.Context.MODE_PRIVATE, null);
	            Cursor allrows = db.rawQuery("select title from subject;", null);
				while(allrows.moveToNext())
				{					
					sub.add(allrows.getString(0));
				}
				
				 // Create an ArrayAdapter using the string array and a default spinner layout
			     ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item, sub);
			     // Specify the layout to use when the list of choices appears
			     dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
			     // Apply the adapter to the spinner
			     spinner.setAdapter(dataAdapter);
			     spinner.setSelection(0);
			     db.close();
				
        } 
		catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Error encountered.",
                    Toast.LENGTH_LONG).show();
        }
		
	}
}
