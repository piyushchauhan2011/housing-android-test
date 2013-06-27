package com.example.testoth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class QuestionActivity extends Activity {

	int currentQuestion;
	SharedPreferences pref;
	Editor editPref;
	String current_user;
	DBHelper mHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Play Game!");
		setContentView(R.layout.activity_question);
		// Show the Up button in the action bar.
		setupActionBar();
		
		pref = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
		current_user = pref.getString("user_email", "");
		mHelper = new DBHelper(this);
		mHelper.open();
		
		Cursor mCursor = mHelper.fetchUsersByEmail(current_user);
		currentQuestion = Integer.parseInt(mCursor.getString(1));
		
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
		getMenuInflater().inflate(R.menu.question, menu);
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
	
	public void submitAnswer(View v) {
		// TODO
		// check the Answer corresponding to current Question
		// Display Toast correct, wrong, take hint etc..
		// if correct update to next question the current interface
		// and add points accordingly, hint 1 = 100-25, hint 2 = 100-50, hint 3 = 0
		// update the database fields and row
		
		
	}

	public void showHint(View v) {
		// TODO
		// retrieve hint for the current question from db
		// make a message string
		// display toast with the current message string
	}
}
