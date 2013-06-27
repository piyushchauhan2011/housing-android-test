package com.example.testoth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// THIS IS CHECKED

public class RegisterActivity extends Activity {

	EditText regUsername;
	EditText regName;
	EditText regPassword;
	EditText regConfirm;
	EditText regCollege;
	EditText regTagline;
	EditText regMobileno;
	
	TextView txtErrors;
	
	DBHelper mHelper;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Register!");
		setContentView(R.layout.activity_register);
		// Show the Up button in the action bar.
		setupActionBar();
		
		mHelper = new DBHelper(this);
		mHelper.open();
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void registerUser(View v) {
		// TODO
		// Test with the Data
		
		regUsername = (EditText) findViewById(R.id.username);
		regPassword = (EditText) findViewById(R.id.password);
		regConfirm = (EditText) findViewById(R.id.confirmPassword);
		regTagline = (EditText) findViewById(R.id.tagline);
		regCollege = (EditText) findViewById(R.id.college);
		regName = (EditText) findViewById(R.id.name);
		regMobileno = (EditText) findViewById(R.id.regMobileno);
		txtErrors = (TextView) findViewById(R.id.regErrors);
		
		String username = regUsername.getText().toString();
		String password = regPassword.getText().toString();
		String confirm = regConfirm.getText().toString();
		String tagline = regTagline.getText().toString();
		String college = regCollege.getText().toString();
		String name = regName.getText().toString();
		String mobileno = regMobileno.getText().toString();

		String errors = "";
		int error_count = 0;

		EmailValidator checkEmail = new EmailValidator();
		if (!checkEmail.validate(username)) {
			errors += "Enter a valid email address!\n";
			error_count++;
		}

		PasswordValidator checkPassword = new PasswordValidator();
		if (!checkPassword.validate(password)) {
			errors += "Enter a strong password\n (Must include ...)\n";
			error_count++;
		}
		
		if (confirm.compareTo(password)!=0) {
			errors += "Passwords must match!\n";
			error_count++;
		}
		
		if(tagline.compareTo("")==0) {
			errors += "Tagline must not be blank!\n";
			error_count++;
		}

		if(college.compareTo("")==0) {
			errors += "College must not be blank!\n";
			error_count++;
		}
		
		if(name.compareTo("")==0) {
			errors += "Name must not be blank!\n";
			error_count++;
		}
		
		if(mobileno.compareTo("")==0) {
			errors += "mobile no. must not be blank!\n";
			error_count++;
		}
		
		errors += "NUMBER OF ERRORS = " + error_count;
		if(error_count==0) {
			mHelper.registerUser(username, password, name, mobileno, tagline, college);
			Toast.makeText(this, "Successfully Registered, Login to Continue!", Toast.LENGTH_LONG).show();
			finish();
		} else {
			txtErrors.setText(errors);
		}
	}

}
