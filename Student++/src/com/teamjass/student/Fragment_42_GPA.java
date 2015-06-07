package com.teamjass.student;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class Fragment_42_GPA extends SherlockFragment {

	SQLiteDatabase db;
	
	TextView gpa, cgpa;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_42_exgpa, container, false);
		gpa = (TextView) rootView.findViewById(R.id.gpa_view);
		cgpa = (TextView) rootView.findViewById(R.id.gpa_cgpa);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_tt);
		
		getTableValues();
    
     
		return rootView;
	}
	
	
	
	public void getTableValues() {
		  
  
        try {        	
        	
        	db= getActivity().openOrCreateDatabase("mydb.db", android.content.Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM gpa;", null);
            String gpaText="";
            allrows.moveToFirst();
        	for(int i=0;i<8;i++)
        	{
        		float sgpa = allrows.getFloat(i);
        		if(sgpa!=0)
        			gpaText+=("\nSemester "+(i+1)+"      "+sgpa);
        	}
        	gpa.setText(gpaText);
        	cgpa.setText(""+allrows.getFloat(8));
        }
        catch(Exception e)
        {
        	Log.e("GPA", ""+e);
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
