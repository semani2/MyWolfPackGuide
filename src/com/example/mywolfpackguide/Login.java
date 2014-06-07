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

public class Login extends Activity {
    
   // Email, password edittext
   EditText txtUsername, txtPassword;
    
   // login button
   Button btnLogin, btnSignup,btnfp;
    
   // Alert Dialog Manager
   AlertDialogManager alert = new AlertDialogManager();
    
   // Session Manager Class
   SessionManager session;
   
   //Username and Password
   String username,password;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.activity_login); 
        
       // Session Manager
       session = new SessionManager(getApplicationContext());                
        
       // Email, Password input text
       txtUsername = (EditText) findViewById(R.id.txtUsername);
       txtPassword = (EditText) findViewById(R.id.txtPassword); 
        
       //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        
        
       // Login button
       btnLogin = (Button) findViewById(R.id.btnLogin);
        //Sign up button
       btnSignup = (Button)findViewById(R.id.btnSignup);
       btnfp = (Button)findViewById(R.id.btnfp); 
       // Login button click event
       btnLogin.setOnClickListener(new View.OnClickListener() {
            
           @Override
           public void onClick(View arg0) {
               // Get username, password from EditText
               username = txtUsername.getText().toString();
               password = txtPassword.getText().toString();
                
               // Check if username, password is filled                
               if(username.trim().length() > 0 && password.trim().length() > 0){
                   	LoginTask lt = new LoginTask();
                   	lt.execute();
               }else{
                   // user didn't entered username or password
                   // Show alert asking him to enter the details
                   alert.showAlertDialog(Login.this, "Login failed..", "Please enter username and password", false);
               }
                
           }
       });
       
       btnSignup.setOnClickListener(new View.OnClickListener() {
           
           @Override
           public void onClick(View arg0) {
               Intent i = new Intent(Login.this,SignUpActivity.class );
               startActivity(i);
               finish();
               
                
           }
       });
       btnfp.setOnClickListener(new View.OnClickListener() {
           
           @Override
           public void onClick(View arg0) {
               Intent i = new Intent(Login.this,ForgotPasswordActivity.class );
               startActivity(i);
               finish();
               
                
           }
       });
   }
   
   public class LoginTask extends AsyncTask<String,Void,Void>
   {
	   	 Element root ;
		 String status = "";
		 AlertDialogManager alert = new AlertDialogManager();
		 RestServiceUrl s = new RestServiceUrl();
	@Override
	protected Void doInBackground(String... params) {
		try{
			StringBuilder sb=new StringBuilder(s.url+"userLogin/"+username+","+password);
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
	
	
	//Post execute method
	@Override
	protected void onPostExecute(Void result) {
		if(status.equalsIgnoreCase("success"))
		{
			//Login successful, create session
			session.createLoginSession(username);
			Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
		}
		else if(status.equalsIgnoreCase("incorrect password"))
		{
			alert.showAlertDialog(Login.this, "Login failed..", "Incorrect Password entered", false);
		}
		else
		{
			alert.showAlertDialog(Login.this, "Login failed..", "Invalid email id entered", false);
		}
		super.onPostExecute(result);
	}
	   
   }
}