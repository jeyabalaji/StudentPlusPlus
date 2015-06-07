package com.teamjass.student;
 
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class Login_page extends Activity {
	
	Context c = this;
	Sync_0_Sublist sub = new Sync_0_Sublist();
	Sync_1_Profile pro = new Sync_1_Profile();
	Sync_2_Timetable tt = new Sync_2_Timetable();
	Sync_3_Attendance att = new Sync_3_Attendance();
	Sync_41_Internals in = new Sync_41_Internals();
	Sync_42_GPA gpa = new Sync_42_GPA();
	Sync_5_Library lib = new Sync_5_Library();
	
    EditText un,pw;
    TextView error;
    Button ok;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        un=(EditText)findViewById(R.id.regNo);
        pw=(EditText)findViewById(R.id.pwd);
        ok=(Button)findViewById(R.id.loginbtn);
        error=(TextView)findViewById(R.id.loginMsg);

        ok.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {

                error.setText("Validating Credentials...");
                String ip = getResources().getString(R.string.ipaddress);
                String link = "http://"+ip+":8084/ERP/androvalid";                
                new HttpAsyncTask().execute(link);
            }
        });
    }
    
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", un.getText().toString()));
            postParameters.add(new BasicNameValuePair("userpass", pw.getText().toString()));
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
                     
            try {
            	if(res.charAt(0)=='1')
                {
                   
                    SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);	
                    Editor editor = sharedpreferences.edit();
                    editor.putString("regNo", un.getText().toString());
                    editor.putString("pass", pw.getText().toString());
                    editor.putInt("status", 1);
                    editor.commit();
                    
                    sub.sync(c);
	        		pro.sync(c);
	        		tt.sync(c);
	        		att.sync(c);
	        		in.sync(c);
	        		gpa.sync(c);
	        		lib.sync(c);
                    
                    Intent i = new Intent(Login_page.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);                  
    				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    				startActivity(i);
    				error.setText("PLEASE ENTER YOUR REGISTER NUMBER AND PASSWORD.");
                }
                else
                {
                    error.setText("Wrong Username and Password.");
                }

            } 
            catch (Exception e) {
            	error.setText("Oops Something Went Wrong. Please Try Again.");
                Log.e("ERROR",e.toString());
            }
		}
	}
	
	
}