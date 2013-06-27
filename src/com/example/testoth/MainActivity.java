package com.example.testoth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	DBHelper mHelper;
	EditText loginUsername;
	EditText loginPassword;
	
	SharedPreferences prefs;
	Editor prefEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		prefs = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
		
		if(prefs.getBoolean("is_set_session", false) == true) {
			Intent userDetails = new Intent(this, UserDetails.class);
			startActivity(userDetails);
		}
		
		mHelper = new DBHelper(this);
		mHelper.open();
		
		Toast.makeText(this, "Hello Application!", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void checkLogin(View v) {
		// TODO - Test the running example
		// check the login details by running a query in database
		loginUsername = (EditText) findViewById(R.id.loginUsername);
		loginPassword = (EditText) findViewById(R.id.loginPassword);
		
		String username = loginUsername.getText().toString();
		Cursor login = mHelper.fetchUserByUsername(username);
		if(login != null) {
			String password = login.getString(1);
			
			// if success start user details activity
			if(loginPassword.getText().toString().compareTo(password) == 0) {
				// start a session of current user using shared preferences
				prefEdit = prefs.edit();
				prefEdit.putString("user_email", username);
				prefEdit.putBoolean("is_set_session", true);
				prefEdit.commit();
				
				Intent userDetails = new Intent(this, UserDetails.class);
				startActivity(userDetails);
			} // else display toast message saying login failed check username or password
			else {
				Toast.makeText(this, "Check your password!", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Check your username!", Toast.LENGTH_LONG).show();
		}		
	}
	
	public void openRegisterActivity(View v) {
		// just start the register activity
		Intent registerActivity = new Intent(this, RegisterActivity.class);
		startActivity(registerActivity);
	}
	
	public void showNotificationDialog(View v) {
		// fetch the notifications from database
		
		// make the toast message
		String notificationMessage = "This is a sample Text!";
		Toast.makeText(this, notificationMessage, Toast.LENGTH_LONG).show();
		
		// TODO
		// Instead of Toast Use Dialog Box to show all possible notifications
	}
	
	public void openRankActivity(View v) {
		// just start the Rank Activity
		Intent rankActivity = new Intent(this, RankActivity.class);
		startActivity(rankActivity);
	}
}
