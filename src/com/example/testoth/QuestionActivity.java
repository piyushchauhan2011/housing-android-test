package com.example.testoth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {

	int currentQuestion;
	SharedPreferences pref;
	Editor editPref;
	String current_user;
	
	DBHelper mHelper;
	public final static String TAG = "QUESTION_PIYUSH";
	
	int question_id;
	String answer;
	String hint1;
	String hint2;
	
	EditText userAnswer;
	ImageView displayQuestion;
	TextView showQuestionNo;
	
	Cursor mCursor_question;
	Cursor mCursor;
	
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
		
		mCursor = mHelper.fetchUsersByEmail(current_user);
		Log.i(TAG, mCursor.getCount()+"");
		mCursor.moveToFirst();
		
		currentQuestion = Integer.parseInt(mCursor.getString(8));
		
		// fetch question row from questions table
		mCursor_question = mHelper.getQuestionByID(currentQuestion+"");
		mCursor_question.moveToFirst();
		question_id = Integer.parseInt(mCursor_question.getString(1));
		answer = mCursor_question.getString(2);
		hint1 = mCursor_question.getString(3);
		hint2 = mCursor_question.getString(4);
		
		Log.i(TAG , currentQuestion+"");
		
		userAnswer = (EditText) findViewById(R.id.userAnswer);
		
		displayQuestion = (ImageView) findViewById(R.id.displayQuestion);
		displayQuestion.setImageResource(question_id);
		showQuestionNo = (TextView) findViewById(R.id.showQuestionNo);
		showQuestionNo.setText(currentQuestion+"");
		
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
		// CHECKED
		// check the Answer corresponding to current Question
		// Display Toast correct, wrong, take hint etc..
		// if correct update to next question the current interface
		// and add points accordingly, hint 1 = 100-50, hint 2 = 100-100;
		// update the database fields and row
		String u_answer = userAnswer.getText().toString();
		u_answer = u_answer.toLowerCase();
		answer = answer.toLowerCase();
		if(answer.compareTo(u_answer)==0) {
			Toast.makeText(this, "Correct Answer!", Toast.LENGTH_LONG).show();
			// success and increment the points and update the profile table
			// update the UI for next question
			Cursor mCursor_updated = mHelper.fetchUsersByEmail(current_user);
			
			int currentScore = Integer.parseInt(mCursor_updated.getString(4));
			int hint=1;
			currentScore+=100;
			mHelper.updateProfileForHint(current_user, currentScore+"", hint+"");
			String solved_questions = mCursor_updated.getString(7);
			solved_questions+=(","+currentQuestion);
			mHelper.updateProfileForCurrent(current_user, currentScore+"", (currentQuestion+=1)+"", solved_questions);
			Bundle tempBundle = new Bundle();
			onCreate(tempBundle);
		} else {
			Toast.makeText(this, "Wrong Answer", Toast.LENGTH_LONG).show();
		}
	}

	public void showHint(View v) {
		// CHECKED
		// retrieve hint for the current question from db
		// make a message string
		// display toast with the current message string
		Cursor mCursor_updated = mHelper.fetchUsersByEmail(current_user);
		String tmp = mCursor_updated.getString(10);
		int hint = Integer.parseInt(tmp);
		int currentScore = Integer.parseInt(mCursor_updated.getString(4));
		if(hint==1) {
			Toast.makeText(this, "Hint : " + hint1, Toast.LENGTH_LONG).show();
			hint++;
			currentScore+=(-50);
			mHelper.updateProfileForHint(current_user, currentScore+"", hint+"");	
		} else if(hint==2) {
			Toast.makeText(this, "Answer : " + hint2, Toast.LENGTH_LONG).show();
			hint=1;
			currentScore+=(-50);
			mHelper.updateProfileForHint(current_user, currentScore+"", hint+"");
			
			Cursor mCursor_new = mHelper.fetchUsersByEmail(current_user);
			currentScore = Integer.parseInt(mCursor_new.getString(4));
			currentScore+=100;
			String solved_questions = mCursor_new.getString(7);
			solved_questions+=(","+currentQuestion);
			mHelper.updateProfileForCurrent(current_user, currentScore+"", (currentQuestion+=1)+"", solved_questions);
			Bundle tempBundle = new Bundle();
			onCreate(tempBundle);
		}
	}
}
