package com.example.testoth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserDetails extends Activity {

	DBHelper mHelper;
	SharedPreferences pref;
	String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Minion Warrior!");
		setContentView(R.layout.activity_user_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		mHelper = new DBHelper(this);
		pref = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
		username = pref.getString("user_email", "");
		
		Cursor mCursor = mHelper.fetchUsersByEmail(username);
		String email = mCursor.getString(0);
		//String name = mCursor.getString(1); // TODO make a field and display the name in textview
		//String mobileno = mCursor.getString(2);
		String tagline = mCursor.getString(3);
		String score_achieved = mCursor.getString(4);
		String college = mCursor.getString(5);
		// String pic = mCursor.getString(6); // TODO to implemented ( think some method )
		String solved_questions = mCursor.getString(7);
		String current = mCursor.getString(8);
		//String time = mCursor.getString(9);
		//String hint = mCursor.getString(10);
		
		TextView txtUsername = (TextView) findViewById(R.id.displayUsername);
		TextView txtCollege = (TextView) findViewById(R.id.displayCollege);
		TextView txtCurrentQuestion = (TextView) findViewById(R.id.displayCurrentQuestion);
		TextView txtQuestionsAttempted = (TextView) findViewById(R.id.displayQuestionsAttempted);
		TextView txtQuestionsRemaining = (TextView) findViewById(R.id.displayQuestionsRemaining);
		TextView txtDisplayScore = (TextView) findViewById(R.id.displayScore);
		TextView txtTagline = (TextView) findViewById(R.id.displayTagline);
		ImageView imgPic = (ImageView) findViewById(R.id.displayRandomMinion);
		
		txtUsername.setText(email);
		txtCollege.setText(college);
		txtCurrentQuestion.setText(current);
		txtQuestionsAttempted.setText(solved_questions);
		
		String remaining_questions = "";
		// from the (current question)++ concatenate all the questions onwards into remaining_questions
		txtQuestionsRemaining.setText(remaining_questions);
		
		txtDisplayScore.setText(score_achieved);
		txtTagline.setText(tagline);
		
		// display minion using the pic string stored in db ( think of some method )
		imgPic.setImageResource(R.drawable.ic_launcher);
			
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
		getMenuInflater().inflate(R.menu.user_details, menu);
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
	
	public void playGame(View v) {
		Intent questionActivity = new Intent(this, QuestionActivity.class);
		startActivity(questionActivity);
	}
	
	public void openRankActivity(View v) {
		Intent rankActivity = new Intent(this, RankActivity.class);
		startActivity(rankActivity);
	}

	public void editUserDetails(View v) {
		Intent editDetails = new Intent(this, EditDetails.class);
		startActivity(editDetails);
	}
}
