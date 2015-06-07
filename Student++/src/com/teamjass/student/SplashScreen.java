package com.teamjass.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

	Context c = this;
	Sync_0_Sublist sub = new Sync_0_Sublist();
	Sync_1_Profile pro = new Sync_1_Profile();
	Sync_2_Timetable tt = new Sync_2_Timetable();
	Sync_3_Attendance att = new Sync_3_Attendance();
	Sync_41_Internals in = new Sync_41_Internals();
	Sync_42_GPA gpa = new Sync_42_GPA();
	Sync_5_Library lib = new Sync_5_Library();
	
	
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		        int st = prefs.getInt("status", 0);
		        Intent i;
		        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	        	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	        	boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		        if (st==0)
		        {
		        	if(!isConnected)
		        	{
		        		AlertDialog alertDialog = new AlertDialog.Builder(
		    	                SplashScreen.this).create();
	    	        
		    	        // Setting Dialog Title.DialogInterface.BUTTON_POSITIVE
		    	        alertDialog.setTitle("Network Error.");

		    	        // Setting Dialog Message
		    	        alertDialog.setMessage("No internet Connection. Please connect to the internet and try again.");

		    	        // Setting OK Button
		    	        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
		    	        		public void onClick(DialogInterface dialog, int which) {
		    	        		System.exit(0);
		    	        	}
		    	});
		    	        alertDialog.show();
		        	}
		        	else
		        	{i = new Intent(SplashScreen.this, Login_page.class);
		        	startActivity(i);}
		        }
		        else
		        {
		        	
		        	if(isConnected)
		        	{
		        		sub.sync(c);
		        		pro.sync(c);
		        		tt.sync(c);
		        		att.sync(c);
		        		in.sync(c);
		        		gpa.sync(c);
		        		lib.sync(c);
		        	}
		        	i = new Intent(SplashScreen.this, MainActivity.class);
		        	startActivity(i);
		        }
				

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
