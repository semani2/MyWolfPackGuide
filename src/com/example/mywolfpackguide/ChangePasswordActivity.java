package com.example.mywolfpackguide;

import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.example.mywolfpackguide.Login.LoginTask;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.os.Build;

public class ChangePasswordActivity extends Activity {
	EditText etOldPass,etNewPass,etConPass;
	String oldPass,newPass,conpass,username;
	Button update;
	SessionManager session;
	AlertDialogManager alert = new AlertDialogManager();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change_password);
		etOldPass = (EditText) findViewById(R.id.editoldPwd);
		etNewPass = (EditText) findViewById(R.id.editnewPwd);
		etConPass = (EditText) findViewById(R.id.editconPwd);
		update = (Button) findViewById(R.id.change);
		session = new SessionManager(getApplicationContext());
		update.setOnClickListener(new View.OnClickListener() {
            
	           @Override
	           public void onClick(View arg0) {
	        	   username = session.getUserDetails();
	        	   oldPass = etOldPass.getText().toString();
	        	   newPass = etNewPass.getText().toString();
	               conpass = etConPass.getText().toString(); 
	               if(oldPass.trim().length() > 0 && newPass.trim().length() > 0 && conpass.trim().length() > 0 )
	               {
	            	   if(newPass.equalsIgnoreCase(conpass))
	            	   {
	            		   // Call new async task for updating password
	            		   UpdatePassword up = new UpdatePassword();
	            		   up.execute();
	            	   }
	            	   else
	            	   {
	            		   //Password mismatch
	            		   alert.showAlertDialog(ChangePasswordActivity.this, "Sorry!", "Password Mismatch", false);
	            	   }
	               }
	               else
	               {
	            	   //All details mandatory
	            	   alert.showAlertDialog(ChangePasswordActivity.this, "Sorry!", "Please enter all details", false);
	               }   
	            	 
	           }
	       });
	}
	
	public class UpdatePassword extends AsyncTask<String,Void,Void>
	{
			Element root ;
		 String status = "";
		 AlertDialogManager alert = new AlertDialogManager();
		 RestServiceUrl s = new RestServiceUrl();
		@Override
		protected Void doInBackground(String... params) {
			try{
				StringBuilder sb=new StringBuilder(s.url+"updatePassword/"+username+","+oldPass+","+newPass);
				String findlink=sb.toString();
				//HTTP request for Rest Service
				HttpClient client=new DefaultHttpClient();
	 	   	    HttpGet request=new HttpGet();
	 	   		request.setURI(new URI(findlink)) ;
	 	   		HttpResponse response=client.execute(request);
	 	   		//XML Parsing for response
	 	    	DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();	
	 	   	   	DocumentBuilder Builder=factory.newDocumentBuilder();
	 	   	   	Document  dom=Builder.parse(response.getEntity().getContent());
	 	   	   	dom.getDocumentElement().normalize();
	 	   	   	root=dom.getDocumentElement();
	 	   	   	status = root.getTextContent().toString();
			}
			catch(Exception e)
			{
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(status.equalsIgnoreCase("success"))
			{
				//Login successful, create session
				alert.showAlertDialog(ChangePasswordActivity.this, "Success !", "Your password has been updated", true);
				
			}
			else if(status.equalsIgnoreCase("old password mismatch"))
			{
				alert.showAlertDialog(ChangePasswordActivity.this, "Update failed..", "Please enter your correct password", false);
			}
			else
			{
				alert.showAlertDialog(ChangePasswordActivity.this, "Login failed..", "Invalid email id entered", false);
			}
			super.onPostExecute(result);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_password, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(ChangePasswordActivity.this,Settings.class);
			startActivity(i);
			return true;
		}
		if( id == R.id.action_logout)
		{
			session = new SessionManager(getApplicationContext());
			session.logoutUser();
			return true;
		}
		if( id == R.id.action_home)
		{
			Intent i = new Intent(ChangePasswordActivity.this,HomeActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
