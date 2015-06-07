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

public class Fragment_3_Attendance extends SherlockFragment {

	SQLiteDatabase db;
	Spinner spinner;
	TextView tot, pres, od, abse, curr, exp;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_3_att, container, false);
		tot = (TextView) rootView.findViewById(R.id.gpa_view);
		pres = (TextView) rootView.findViewById(R.id.present);
		od = (TextView) rootView.findViewById(R.id.onduty);
		abse = (TextView) rootView.findViewById(R.id.absent);
		curr = (TextView) rootView.findViewById(R.id.curr_percent);
		exp = (TextView) rootView.findViewById(R.id.exp_percent);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_tt);
		spinner = (Spinner) rootView.findViewById(R.id.sub_spinner);
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
            Cursor allrows = db.rawQuery("select * from attendance where sub_code=(select sub_code from subject where title='"+sub+"');", null);
            allrows.moveToFirst();
            float total, present, odu;		
			total = allrows.getInt(1);
			present = allrows.getInt(2);
			odu = allrows.getInt(3);
			
			tot.setText(""+Math.round(total));
			pres.setText(""+Math.round(present));
			od.setText(""+Math.round(odu));
			abse.setText(""+Math.round((total-present)));
			double cp = Math.floor(((present+odu)/total)*100);
			double ep = Math.floor(((present+odu)/(total+1))*100);				
			Log.e("VALUES", "CP = "+cp+"EP = "+ep);
			curr.setText(""+cp);
			exp.setText(""+ep);	
        }
        catch(Exception e)
        {
        	Log.e("ATT", ""+e);
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
	            Cursor allrows = db.rawQuery("select  title from subject where type in('C','L','E') ;", null);
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
