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

public class Sync_3_Attendance{

	SQLiteDatabase db;
	Context c;
	public void sync(Context co){
		c = co;  
        try {       
        	String ip = c.getString(R.string.ipaddress);
            String link = "http://"+ip+":8084/ERP/androattendance";
            new attendance().execute(link);
        }
        catch(Exception e)
        {
        	Log.e("Attendance", ""+e);
        }
                  
    }
	
	private class attendance extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
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
            	db.delete("attendance",null,null);				
				JSONObject jO = new JSONObject(res);
				JSONArray jA = jO.getJSONArray("subs");
				int n = jA.length();
				for(int i=0; i<n; i++)
				{
					JSONObject jOb = jA.getJSONObject(i);					
					ContentValues values = new ContentValues();
					values.put("sub_code", jOb.getString("sub_code"));	      
					values.put("total_hrs", jOb.getString("total"));
	                values.put("present_hrs", jOb.getString("present"));
	                values.put("od_hrs", jOb.getString("onduty"));
	                db.insert("attendance", null, values);
				}
				
        } 
		catch (Exception e) {
			Toast.makeText(c, "Syncronisation Incomplete. (ATTENDANCE)", Toast.LENGTH_LONG).show();
            Log.e("ERROR",e.toString());
        }
		}
		}
	}	
}