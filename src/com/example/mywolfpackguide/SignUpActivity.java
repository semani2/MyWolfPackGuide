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

import com.example.mywolfpackguide.MyInterests.AddInterest;

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

public class SignUpActivity extends Activity {
	
	public boolean emailValidator(String email) 
	{
	    Pattern pattern;
	    Matcher matcher;
	    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@ncsu.edu$";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(email);
	    return matcher.matches();
	}
	
	
	
	String username,password,name,conpassword;
	EditText etName,etUsername,etPassword,etConPassword;
	Button signup;
	// Alert Dialog Manager
	   AlertDialogManager alert = new AlertDialogManager();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_up);
		etName = (EditText)findViewById(R.id.editname);
		etUsername = (EditText)findViewById(R.id.editemailid_signup);
		etPassword = (EditText)findViewById(R.id.editpwd_signup);
		etConPassword = (EditText)findViewById(R.id.editconpwd_signup);
		signup = (Button)findViewById(R.id.email);
		signup.setOnClickListener(new View.OnClickListener() {
            
	           @Override
	           public void onClick(View arg0) {
	        	   Boolean result;
	               name = etName.getText().toString();
	               username = etUsername.getText().toString();
	               password = etPassword.getText().toString();
	               conpassword = etConPassword.getText().toString();
	               if(username.trim().length() > 0 && password.trim().length() > 0 && name.trim().length() > 0 && conpassword.trim().length() > 0)
	               {
	            	   result = emailValidator(username);
	            	   if(result == true)
	            	   {
	            		   //Check for password and confirm password
	            		   if(password.equalsIgnoreCase(conpassword))
	            		   {
	            			   //Call async task
	            			   
	            			  name = name.replaceAll(" ", "%20");
	            			   SignUpTask sut = new SignUpTask();
	            			   sut.execute();
	            		   }
	            		   else
	            		   {
	            			   // Password mismatch
	            			   alert.showAlertDialog(SignUpActivity.this, "Sorry!", "Password Mismatch..", false);
	            		   }
	            	   }
	            	   else
	            	   {
	            		   //Please enter valid ncsu.edu email
	            		   alert.showAlertDialog(SignUpActivity.this, "Sorry!", "Please enter a vaild ncsu.edu email id", false);
	            	   }
	               }
	               else
	               {
	            	   alert.showAlertDialog(SignUpActivity.this, "Sorry!", "Please fill in all the fields", false);
	               }
	           }
	       });
		
	}

	public class SignUpTask extends AsyncTask<String,Void,Void>
	{
		Element root ;
		String status = "";
		AlertDialogManager alert = new AlertDialogManager();
		RestServiceUrl s = new RestServiceUrl();
		@Override
		protected Void doInBackground(String... params) {
			try{
				StringBuilder sb=new StringBuilder(s.url+"userRegistration/"+name+","+username+","+password);
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
				alert.showAlertDialog(SignUpActivity.this, "SignUp succesful", "You have been registered with My WolfPack Guide", true);
				Intent i = new Intent(getApplicationContext(), Login.class);
	            startActivity(i);
	            finish();
			}
			else if(status.equalsIgnoreCase("User already exists"))
			{
				alert.showAlertDialog(SignUpActivity.this, "SignUp failed..", "User with email id already exists", false);
			}
			else
			{
				alert.showAlertDialog(SignUpActivity.this, "SignUp failed..", "Please try again"+status, false);
			}
			super.onPostExecute(result);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_login) {
			Intent i = new Intent(SignUpActivity.this, Login.class);
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	

}
