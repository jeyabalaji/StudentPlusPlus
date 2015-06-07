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

public class Fragment_5_Library extends SherlockFragment {
	
	SQLiteDatabase db;
	TextView books;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_5_library, container, false);
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_pro);
        
        books = (TextView) rootView.findViewById(R.id.lib_books);
    	    	 
       
        String s="";   
        try {
        	db= getActivity().openOrCreateDatabase("mydb.db", Context.MODE_PRIVATE, null);
            Cursor allrows = db.rawQuery("SELECT * FROM library", null);
            System.out.println("COUNT : " + allrows.getCount());
            
            while(allrows.moveToNext())
            {
            s = s + (allrows.getString(0))+"\n";
			s = s + (allrows.getString(1))+"\n";
			s = s + (allrows.getString(2))+"\n\n";
            }
            allrows.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error encountered.",
                    Toast.LENGTH_LONG).show();
        }
        books.setText(s);
		return rootView;
	}
}
