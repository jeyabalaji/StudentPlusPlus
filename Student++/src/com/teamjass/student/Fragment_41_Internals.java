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

public class Fragment_41_Internals extends SherlockFragment {

	SQLiteDatabase db;
	Spinner spinner;
	TextView tot, ct1, ct2, modex, spt, att;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_41_exinternals, container, false);
		tot = (TextView) rootView.findViewById(R.id.int_tot);
		ct1 = (TextView) rootView.findViewById(R.id.int_ct1);
		ct2 = (TextView) rootView.findViewById(R.id.int_ct2);
		modex = (TextView) rootView.findViewById(R.id.int_modex);
		spt = (TextView) rootView.findViewById(R.id.int_spt);
		att = (TextView) rootView.findViewById(R.id.int_att);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_exam);
		spinner = (Spinner) rootView.findViewById(R.id.int_subSpin);
		sublist();
        
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
        	String sub = spinner.getSelectedItem().toString();
        	db= getActivity().openOrCreateDatabase("mydb.db", android.content.Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("select * from internal where sub_code=(select sub_code from subject where title='"+sub+"');", null);
            allrows.moveToFirst();
            float CT1, CT2, ModEx, SurpTest;			
			CT1 = allrows.getFloat(1);
			CT2 = allrows.getFloat(2);
			ModEx = allrows.getFloat(3);
			SurpTest = allrows.getFloat(4);
			
			ct1.setText(""+CT1);
			ct2.setText(""+CT2);
			modex.setText(""+ModEx);
			spt.setText(""+SurpTest);
			float total = CT1 + CT2 + ModEx + SurpTest + 5;
			tot.setText(""+total);
        }
        catch(Exception e)
        {
        	Log.e("INT", ""+e);
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
	            Cursor allrows = db.rawQuery("select  title from subject where type in('C') ;", null);
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
