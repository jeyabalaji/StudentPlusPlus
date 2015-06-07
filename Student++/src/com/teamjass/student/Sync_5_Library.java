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

public class Sync_5_Library {
	
	Context c;
	SQLiteDatabase db;
		
	public void sync(Context co) {
		c = co;
		  	try{
        Log.e("RESULT", "TESTING 4 5 6");
        String ip = c.getString(R.string.ipaddress);
        String link = "http://"+ip+":8084/ERP/androlib";
        new HttpAsyncTask().execute(link);
		  	}
		  	catch(Exception e){
		  		Log.e("Library", ""+e);
		  	}
   
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
			db= c.openOrCreateDatabase("mydb.db", Context.MODE_PRIVATE, null);
            try {
            	db.delete("library",null,null);
				JSONObject jOb = new JSONObject(res);
				JSONArray jA = jOb.getJSONArray("books");
				int n = jA.length();
				for(int i=0; i<n; i++)
				{
					JSONObject jO = jA.getJSONObject(i);
					ContentValues values = new ContentValues();
	                values.put("book_name", jO.getString("name"));
	                values.put("author", jO.getString("author"));
	                values.put("due_date", jO.getString("due"));	                        
	                db.insert("library", null, values);
				}				
            } 
            catch (Exception e) {
            	Toast.makeText(c, "Syncronisation Incomplete. (LIBRARY)", Toast.LENGTH_LONG).show();
                Log.e("ERROR",e.toString());
            }
            db.close();
			}
		}
	}
}
