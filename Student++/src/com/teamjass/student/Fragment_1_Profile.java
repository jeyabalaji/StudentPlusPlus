package com.teamjass.student;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class Fragment_1_Profile extends SherlockFragment {
	
	SQLiteDatabase db;
	TextView stuName;
	TextView stuColl;
	TextView stuReg;
	TextView stuYr;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_1_profile, container, false);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_pro);
        
        stuName = (TextView) rootView.findViewById(R.id.stuName);
    	stuColl = (TextView) rootView.findViewById(R.id.stuColl);
    	stuReg = (TextView) rootView.findViewById(R.id.stuRegId);
    	stuYr = (TextView) rootView.findViewById(R.id.stuYrSec);
        
        try {
        	db= getActivity().openOrCreateDatabase("mydb.db", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM profile", null);            
            allrows.moveToFirst();
            stuName.setText(allrows.getString(0));
            stuColl.setText(allrows.getString(2));
            stuReg.setText(allrows.getString(1));
            stuYr.setText(allrows.getString(7));
            allrows.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Oops Something Went Wrong. Please Try Later.",
                    Toast.LENGTH_LONG).show();
        }
        
		return rootView;
		
	}
	
}