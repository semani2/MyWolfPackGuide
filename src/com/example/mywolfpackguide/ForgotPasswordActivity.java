package com.example.mywolfpackguide;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.Toast;
import android.os.Build;

public class ForgotPasswordActivity extends Activity {
	AlertDialogManager alert = new AlertDialogManager();
	EditText email;
	Button getPassword;
	String username = null;
	
	public boolean emailValidator(String email) 
	{
	    Pattern pattern;
	    Matcher matcher;
	    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@ncsu.edu$";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(email);
	    return matcher.matches();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forgot_password);
		email = (EditText) findViewById(R.id.editforgotpwd_emailid);
		getPassword = (Button) findViewById(R.id.email);
		getPassword.setOnClickListener(new View.OnClickListener() {
	            
	           @Override
	           public void onClick(View arg0) {
	             username = email.getText().toString();
	             if(username.trim().length() > 0)
	             {
	            	 Boolean b = emailValidator(username);
	            	 if(b == true)
	            	 {
	            		 //Call async task to retreive password
	            		 GetPassword gp = new GetPassword();
	            		 gp.execute();
	            	 }
	            	 else
	            	 {
	            		 //Please enter valid ncsu.edu email id
	            		 alert.showAlertDialog(ForgotPasswordActivity.this, "Sorry", "Please enter a valid ncsu.edu email id", false);
	            	 }
	             }
	             else
	             {
	            	 //Please enter username
	            	 alert.showAlertDialog(ForgotPasswordActivity.this, "Sorry", "Please enter your registered email id", false);
	             }
	           }
	       });
	       
	}

	public class GetPassword extends AsyncTask<String,Void,Void>
	{
		Element root ;
		 String status = "";
		 AlertDialogManager alert = new AlertDialogManager();
		 RestServiceUrl s = new RestServiceUrl();
		@Override
		protected Void doInBackground(String... params) {
			try{
				StringBuilder sb=new StringBuilder(s.url+"forgotPassword/"+username);
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
				//Passwrod has been sent by email
				Toast.makeText(getApplicationContext(), "Your password has been sent to the entered email id",
						   Toast.LENGTH_LONG).show();
				Intent i = new Intent(ForgotPasswordActivity.this,Login.class);
				startActivity(i);
				finish();
				
			}
			else
			{
				alert.showAlertDialog(ForgotPasswordActivity.this, "Sorry..", "You are not a registered user", false);
			}
			
			super.onPostExecute(result);
		}
		
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_login) {
			Intent i = new Intent(ForgotPasswordActivity.this, Login.class);
			startActivity(i);
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}

	

}
