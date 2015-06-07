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
import android.widget.Toast;

public class Sync_42_GPA {

	SQLiteDatabase db;
	Context c;
	
	public void sync(Context co){
		c = co;
		
        try {        	
        	String ip = c.getString(R.string.ipaddress);
            String link = "http://"+ip+":8084/ERP/androcgpa";
            new internals().execute(link);
        }
        catch(Exception e)
        {
        	Log.e("GPA", ""+e);
        }
                  
    }
	
	private class internals extends AsyncTask<String, Void, String> {

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
            	db.delete("gpa",null,null);
				
				JSONObject jO = new JSONObject(res);
				ContentValues values = new ContentValues();
                values.put("cgpa", jO.getString("cgpa"));
            	for(int i=1;i<9;i++)
            	{
            		float sgpa = Float.parseFloat(jO.getString("sem"+i));
            		values.put("sem"+i, sgpa);            		
            	}
            	db.insert("gpa", null, values);
            	db.close();
        } 
		catch (Exception e) {
			Toast.makeText(c, "Syncronisation Incomplete. (GPA)", Toast.LENGTH_LONG).show();
            Log.e("ERROR",e.toString());
        }
		}
		}
	}	
}
