package com.example.testoth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditDetails extends Activity {
	// THIS CLASS IS CHECKED

	EditText editUsername;
	EditText editPassword;
	EditText editConfirmPassword;
	EditText editTagline;
	EditText editCollege;
	EditText editName;
	EditText editMobileno;
	TextView txtErrors;
	
	DBHelper mHelper;
	
	SharedPreferences pref;
	String currentUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Edit Information!");
		setContentView(R.layout.activity_edit_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		mHelper = new DBHelper(this);
		mHelper.open();
		
		pref = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
		currentUsername = pref.getString("user_email", "");
		
		Log.i("EDIT_PIYUSH", currentUsername);
		
		editUsername = (EditText) findViewById(R.id.editUsername);
		editPassword = (EditText) findViewById(R.id.editPassword);
		editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
		editTagline = (EditText) findViewById(R.id.editTagline);
		editCollege = (EditText) findViewById(R.id.editCollege);
		editName = (EditText) findViewById(R.id.editName);
		editMobileno = (EditText) findViewById(R.id.mobileno);
		txtErrors = (TextView) findViewById(R.id.errors);
		
		Cursor mCursor_login = mHelper.fetchUserByUsername(currentUsername);
		mCursor_login.moveToFirst();
		Cursor mCursor_profile = mHelper.fetchUsersByEmail(currentUsername);
		mCursor_profile.moveToFirst();
		
		editUsername.setText(mCursor_login.getString(0)); // username
		editPassword.setText(mCursor_login.getString(1)); // password
		editConfirmPassword.setText(mCursor_login.getString(1)); // confirm password
		editTagline.setText(mCursor_profile.getString(3)); // tagline
		editCollege.setText(mCursor_profile.getString(5)); // college
		editName.setText(mCursor_profile.getString(1)); // name
		editMobileno.setText(mCursor_profile.getString(2)); // mobile no
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
		getMenuInflater().inflate(R.menu.edit_details, menu);
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

	public void saveDetails(View v) {
		// TODO
		// Check the method for test cases
		
		String username = editUsername.getText().toString();
		String password = editPassword.getText().toString();
		String confirm = editConfirmPassword.getText().toString();
		String tagline = editTagline.getText().toString();
		String college = editCollege.getText().toString();
		String name = editName.getText().toString();
		String mobileno = editMobileno.getText().toString();

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
			mHelper.updateProfile(currentUsername, username, password, name, mobileno, tagline, college);
			Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
			finish();
		} else {
			txtErrors.setText(errors);
		}
		
	}
}
