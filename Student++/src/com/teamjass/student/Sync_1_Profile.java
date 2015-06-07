package com.teamjass.student;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Sync_1_Profile {
	
	SQLiteDatabase db;
	Context c;
	TextView stuName;
	TextView stuColl;
	TextView stuReg;
	TextView stuYr;
		
	public void sync(Context co) {
		
		 c=co;
        Log.e("RESULT", "TESTING SYNC");
        String ip = c.getString(R.string.ipaddress);
        String link = "http://"+ip+":8084/ERP/androstudinfojson";
        
        
        	new HttpAsyncTask().execute(link);
	}
	
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

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
            try {
            	JSONObject jO = new JSONObject(res);
            	db= c.openOrCreateDatabase("mydb.db", Context.MODE_PRIVATE, null);
            	db.delete("profile",null,null);
            	ContentValues values = new ContentValues();
                values.put("name", jO.getString("name"));
                values.put("regno", jO.getString("regno"));
                values.put("campus", jO.getString("campus"));
                values.put("class",jO.getString("class"));
                values.put("course",jO.getString("course"));
                values.put("dept",jO.getString("dept"));
                values.put("dob",jO.getString("dob"));
                values.put("gender",jO.getString("gender"));                
                db.insert("profile", null, values);
                db.close();
            } 
            catch (Exception e) {
            	Toast.makeText(c, "Syncronisation Incomplete. (PROFILE)", Toast.LENGTH_LONG).show();
                Log.e("ERROR",e.toString());
            }
            }
		}
	}
}
