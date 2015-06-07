package com.teamjass.student;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity {

	// Declare Variables
	SQLiteDatabase db;
	
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;
	String[] title;
	String[] subtitle;
	int[] icon;
	Fragment prof = new Fragment_1_Profile();
	Fragment timtab = new Fragment_2_TimeTable();
	Fragment att = new Fragment_3_Attendance();
	Fragment exams = new Fragment_4_Exams();
	Fragment lib = new Fragment_5_Library();
	Fragment sett = new Fragment_6_Settings();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from drawer_main.xml
		setContentView(R.layout.drawer_main);
		
		db= openOrCreateDatabase("mydb.db", MODE_PRIVATE, null);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `subject` " +
				"( `type` varchar(2) NOT NULL DEFAULT 'C', " +
				"`sub_code` varchar(10) NOT NULL DEFAULT '', " +
				"`title` varchar(50) DEFAULT NULL );");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `time_table` " +
				"(`day` varchar(10) DEFAULT NULL,  " +
				"`hour1` varchar(10) DEFAULT NULL,  " +
				"`hour2` varchar(10) DEFAULT NULL,  " +
				"`hour3` varchar(10) DEFAULT NULL,  " +
				"`hour4` varchar(10) DEFAULT NULL,  " +
				"`hour5` varchar(10) DEFAULT NULL,  " +
				"`hour6` varchar(10) DEFAULT NULL,  " +
				"`hour7` varchar(10) DEFAULT NULL,  " +
				"`hour8` varchar(10) DEFAULT NULL);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `attendance` " +
				"(  `sub_code` varchar(10) NOT NULL DEFAULT '',  " +
				"`total_hrs` int(3) DEFAULT NULL,  " +
				"`present_hrs` int(3) DEFAULT NULL,  " +
				"`od_hrs` int(3) DEFAULT NULL);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `gpa` " +
				"( `sem1` float DEFAULT '0',  " +
				"`sem2` float DEFAULT '0',  " +
				"`sem3` float DEFAULT '0',  " +
				"`sem4` float DEFAULT '0',  " +
				"`sem5` float DEFAULT '0',  " +
				"`sem6` float DEFAULT '0',  " +
				"`sem7` float DEFAULT '0',  " +
				"`sem8` float DEFAULT '0',  " +
				"`cgpa` float DEFAULT '0');");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `internal` (  " +
				"`sub_code` varchar(10) NOT NULL,  " +
				"`CT1` float DEFAULT '0',  " +
				"`CT2` float DEFAULT '0',  " +
				"`model` float DEFAULT '0',  " +
				"`surprise_test` float DEFAULT '0');");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `library` (  " +
				"`book_name` varchar(100) DEFAULT NULL,  " +
				"`author` varchar(80) DEFAULT NULL,  " +
				"`due_date` date DEFAULT NULL);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `profile` (  " +
				"`name` varchar(50) NOT NULL,  " +
				"`regno` varchar(10) NOT NULL,  " +
				"`campus` varchar(15) DEFAULT NULL,  " +
				"`course` varchar(10) DEFAULT NULL,  " +
				"`dept` varchar(30) DEFAULT NULL,  " +
				"`dob` date DEFAULT NULL,  " +
				"`gender` varchar(2) DEFAULT 'M',  " +
				"`class` varchar(10) DEFAULT NULL,  " +
				"PRIMARY KEY (`regno`));");
		
		db.close();
		// Get the Title
		mTitle = mDrawerTitle = getTitle();

		// Generate title
		title = new String[] { "Profile", "Time Table", "Attendance", "Exams", "Library","Settings", "Log Out" };

		// Generate subtitle
		subtitle = new String[] {"Personal Profarma", "Know Your Day", "Be There", "Score More", "Be On Time","Set Things Right", "Exit"};

		// Generate icon
		icon = new int[] { R.drawable.ic_pro, R.drawable.ic_tt, R.drawable.ic_att, R.drawable.ic_exam, R.drawable.ic_lib, R.drawable.ic_set, R.drawable.ic_logout };

		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.listview_drawer);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass string arrays to MenuListAdapter
		mMenuAdapter = new MenuListAdapter(MainActivity.this, title, subtitle,
				icon);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(mMenuAdapter);

		// Capture listview menu item click
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
		        final ActionBar actionBar = getActionBar();
		        actionBar.setIcon(R.drawable.ic_launcher);
				getSupportActionBar().setTitle(mDrawerTitle);
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// ListView click listener in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Locate Position
		if (position==6)
		{
			db= openOrCreateDatabase("mydb.db", MODE_PRIVATE, null);
			db.delete("subject", null, null);
			db.delete("time_table",null,null);
			db.delete("internal",null,null);
			db.delete("library",null,null);
			db.delete("profile",null,null);
			db.delete("gpa",null,null);
			db.delete("attendance",null,null);
			db.close();
			
			SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);	
            Editor editor = sharedpreferences.edit();
            editor.putInt("status", 0);
            editor.commit();
			Intent i = new Intent(MainActivity.this, Login_page.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);                  
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(i);
		}
		else{
		switch (position) {
		case 0:
			ft.replace(R.id.content_frame, prof);
			break;
		case 1:
			ft.replace(R.id.content_frame, timtab);
			break;
		case 2:
			ft.replace(R.id.content_frame, att);
			break;
		case 3:
			ft.replace(R.id.content_frame, exams);
			break;
		case 4:
			ft.replace(R.id.content_frame, lib);
			break;
		case 5:
			ft.replace(R.id.content_frame, sett);
			break;
		}
		ft.commit();
		mDrawerList.setItemChecked(position, true);
		// Get the title followed by the position
		setTitle(title[position]);
		}
		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	private Boolean exit = false;
	@Override
	    public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		if(fm.getBackStackEntryCount()==0){
	        if (exit)
	        {	        	
	        	this.finish();	        	
	        }
	            
	        else {
	            Toast.makeText(this, "Press Back again to Exit.",
	                    Toast.LENGTH_SHORT).show();
	            exit = true;
	            new Handler().postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                    exit = false;
	                }
	            }, 3 * 1000);

	        }
		}
		
		else
		{
			fm.popBackStack();			
		}

	    }
}
