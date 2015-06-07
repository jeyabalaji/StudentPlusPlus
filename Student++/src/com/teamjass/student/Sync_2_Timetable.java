package com.teamjass.student;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Sync_2_Timetable {

	SQLiteDatabase db;
	Context c;
	public void sync(Context co) {
		                	
        c=co;
        try {
        	String ip = c.getString(R.string.ipaddress);
            String link = "http://"+ip+":8084/ERP/androtime_table";
            new timetable().execute(link);
        }
        catch(Exception e)
        {
        	Log.e("TT", ""+e);
        }
                  
    }

	private class timetable extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();			
			SharedPreferences prefs = c.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
	        String rno = prefs.getString("regNo", "NULL");
	        postParameters.add(new BasicNameValuePair("regno", rno));
	        Log.e("PARAMETERS", ""+postParameters);
	        String response = null;
	        
	        try{
	        response = CustomHttpClient.executeHttpPost(urls[0], postParameters);
	        }
	        catch(Exception e){
	        	Log.e("HTTP Exception", ""+e);
	        }
	        
			return response;
		}
		
		
		protected void onPostExecute(String res) {
			Log.e("Result Set", "is "+res);
			if(res!=null)
			{
			db= c.openOrCreateDatabase("mydb.db", Context.MODE_PRIVATE, null);
            try {
            	db.delete("time_table",null,null);				
				JSONObject jO = new JSONObject(res);
				JSONArray jA = jO.getJSONArray("timetable");
				int n = jA.length();
				for(int i=0; i<n; i++)
				{
					JSONObject jOb = jA.getJSONObject(i);
					ContentValues values = new ContentValues();
	                values.put("day", jOb.getString("day"));
	                values.put("hour1", jOb.getString("hour1"));
	                values.put("hour2", jOb.getString("hour2"));
	                values.put("hour3", jOb.getString("hour3"));
	                values.put("hour4", jOb.getString("hour4"));
	                values.put("hour5", jOb.getString("hour5"));
	                values.put("hour6", jOb.getString("hour6"));
	                values.put("hour7", jOb.getString("hour7"));
	                values.put("hour8", jOb.getString("hour8"));
	                db.insert("time_table", null, values);
				}
        } 
		catch (Exception e) {
			Toast.makeText(c, "Syncronisation Incomplete. (TIMETABLE)", Toast.LENGTH_LONG).show();
            Log.e("ERROR",e.toString());
        }
		}
		}
	}
	
}
