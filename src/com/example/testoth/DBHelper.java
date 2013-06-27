package com.example.testoth;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper {

	public static final String KEY_USERNAME = "username"; // username is email address | primary key also
	public static final String KEY_PASSWORD = "password";
	
	public static final String KEY_ID = "_id"; // primary key
	public static final String KEY_DATE = "date";
	public static final String KEY_NOTIFICATION = "notification";
	
	public static final String KEY_EMAIL = "email"; // primary key
	public static final String KEY_NAME = "name";
	public static final String KEY_MOBILENO = "mobileno";
	public static final String KEY_TAGLINE = "tagline";
	public static final String KEY_SCORE_ACHIEVED = "score_achieved";
	public static final String KEY_COLLEGE = "college";
	public static final String KEY_PIC = "pic";
	public static final String KEY_SOLVED_QUESTIONS = "solved_questions";
	public static final String KEY_CURRENT = "current";
	public static final String KEY_TIME = "time";
	public static final String KEY_HINT = "hint";
	
	public static final String KEY_QUESTION_NO = "question_no"; // primary key
	public static final String KEY_QUESTION_ID = "question_id";
	public static final String KEY_ANSWER = "answer";
	public static final String KEY_HINT1 = "hint1";
	public static final String KEY_HINT2 = "hint2";
	

	private static final String TAG = "DBHelper";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "treasure_hunt";
	private static final String LOGIN_TABLE = "users_login";
	private static final String PROFILE_TABLE = "users_profile";
	private static final String NOTIFICATIONS_TABLE = "notifications";
	private static final String QUESTIONS_TABLE = "questions";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	private static final String CREATE_LOGIN_TABLE = "CREATE TABLE if not exists "
			+ LOGIN_TABLE + " (" 
				+ KEY_USERNAME	+ " PRIMARY KEY ,"
				+ KEY_PASSWORD  + ");";
	
	private static final String CREATE_PROFILE_TABLE = "CREATE TABLE if not exists "
			+ PROFILE_TABLE + " (" 
				+ KEY_EMAIL	+ " PRIMARY KEY ,"
				+ KEY_NAME + ", "
				+ KEY_MOBILENO + ", "
				+ KEY_TAGLINE + ", "
				+ KEY_SCORE_ACHIEVED + ", "
				+ KEY_COLLEGE + ", "
				+ KEY_PIC + ", "
				+ KEY_SOLVED_QUESTIONS + ", "
				+ KEY_CURRENT + ", "
				+ KEY_TIME + ", "
				+ KEY_HINT + ");";
	
	private static final String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE if not exists "
			+ NOTIFICATIONS_TABLE + " (" 
				+ KEY_ID + " integer PRIMARY KEY autoincrement,"
				+ KEY_DATE + ", "
				+ KEY_NOTIFICATION + ");";
	
	private static final String CREATE_QUESTIONS_TABLE = "CREATE TABLE if not exists "
			+ QUESTIONS_TABLE + " (" 
				+ KEY_QUESTION_NO + " PRIMARY KEY ,"
				+ KEY_QUESTION_ID + ", "
				+ KEY_ANSWER + ", "
				+ KEY_HINT1 + ", "
				+ KEY_HINT2 + ");";
	

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "On Create method is called()!");
			
			Log.w(TAG, CREATE_LOGIN_TABLE);
			db.execSQL(CREATE_LOGIN_TABLE);
			
			Log.w(TAG, CREATE_PROFILE_TABLE);
			db.execSQL(CREATE_PROFILE_TABLE);
			
			Log.w(TAG, CREATE_NOTIFICATIONS_TABLE);
			db.execSQL(CREATE_NOTIFICATIONS_TABLE);
			
			Log.w(TAG, CREATE_QUESTIONS_TABLE);
			db.execSQL(CREATE_QUESTIONS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATIONS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE);
			onCreate(db);
		}
	}

	public DBHelper(Context ctx) {
		// CHECKED
		this.mCtx = ctx;
	}

	public DBHelper open() throws SQLException {
		// CHECKED
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		Log.i(TAG, "Creating the database!");
		return this;
	}

	public void close() {
		// CHECKED
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public long registerUser(String username, String password, String name,
			String mobileno, String tagline, String college) {
		// CHECKED
		ContentValues login_initialValues = new ContentValues();
		login_initialValues.put(KEY_USERNAME, username);
		login_initialValues.put(KEY_PASSWORD, password);
		
		ContentValues user_initialValues = new ContentValues();
		user_initialValues.put(KEY_EMAIL, username);
		user_initialValues.put(KEY_NAME, name);
		user_initialValues.put(KEY_MOBILENO, mobileno);
		user_initialValues.put(KEY_TAGLINE, tagline);
		user_initialValues.put(KEY_COLLEGE, college);
		user_initialValues.put(KEY_CURRENT, "1");
		Date tmp = new Date();
		String tmp_date = "" + tmp.getTime();
		user_initialValues.put(KEY_TIME, tmp_date);
		user_initialValues.put(KEY_HINT, "1");
		user_initialValues.put(KEY_SCORE_ACHIEVED, "0");
		user_initialValues.put(KEY_SOLVED_QUESTIONS, "0");
		user_initialValues.put(KEY_PIC, "");
		
		
		if( mDb.insert(LOGIN_TABLE, null, login_initialValues)!=-1 
				&& mDb.insert(PROFILE_TABLE, null, user_initialValues)!=-1) {
			return 1;
		} else {
			return -1;
		}	
	}

	public boolean deleteAllUsers() {
		// CHECKED
		int doneDelete = 0;
		doneDelete = mDb.delete(LOGIN_TABLE, null, null);
		Log.w(TAG, Integer.toString(doneDelete));
		doneDelete = mDb.delete(PROFILE_TABLE, null, null);
		Log.w(TAG, Integer.toString(doneDelete));
		return doneDelete > 0;

	}

	public Cursor fetchUsersByEmail(String inputText) throws SQLException {
		// CHECKED
		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = mDb.query(PROFILE_TABLE, new String[] { KEY_EMAIL,
					KEY_NAME, KEY_MOBILENO, KEY_TAGLINE, KEY_SCORE_ACHIEVED, KEY_COLLEGE, KEY_SOLVED_QUESTIONS, KEY_TIME }
					, null,	null, null, null, null);

		} else {
			mCursor = mDb.query(true, PROFILE_TABLE, new String[] { KEY_EMAIL,
					KEY_NAME, KEY_MOBILENO, KEY_TAGLINE, KEY_SCORE_ACHIEVED, KEY_COLLEGE, KEY_SOLVED_QUESTIONS, KEY_TIME }
					, KEY_EMAIL + " like '%" + inputText + "%'", null, null, null, null, null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor fetchAllUsers() {
		// CHECKED
		Cursor mCursor = mDb.query(PROFILE_TABLE, new String[] { KEY_EMAIL,
				KEY_NAME, KEY_MOBILENO, KEY_TAGLINE, KEY_SCORE_ACHIEVED, KEY_COLLEGE, KEY_SOLVED_QUESTIONS, KEY_TIME }
				, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetchUserByUsername(String username) {
		// CHECKED
		Cursor mCursor = mDb.query(true, LOGIN_TABLE, new String[] { KEY_USERNAME,  KEY_PASSWORD },
					KEY_USERNAME + " = '" + username + "'", null, null, null, null, null);
		return mCursor;				
	}
	
	public boolean updateProfile(String old_username, String username, String password, String name,
			String mobileno, String tagline, String college) {
		// CHECKED
		boolean result = false;
		ContentValues login_updates = new ContentValues();
		login_updates.put(KEY_USERNAME, username);
		login_updates.put(KEY_PASSWORD, password);
		result = mDb.update(LOGIN_TABLE, login_updates, KEY_USERNAME + " = '" + old_username + "'", null) > 0;
		
		ContentValues profile_updates = new ContentValues();
		profile_updates.put(KEY_EMAIL, username);
		profile_updates.put(KEY_NAME, name);
		profile_updates.put(KEY_MOBILENO, mobileno);
		profile_updates.put(KEY_TAGLINE, tagline);
		profile_updates.put(KEY_COLLEGE, college);
		result = mDb.update(PROFILE_TABLE, profile_updates, KEY_EMAIL + " = '" + old_username + "'", null) > 0;
		return result;
	}
	
	public boolean updateProfileForCurrent(String username, String new_score, String current, String solved_questions) {
		// TODO
		// update table with new score and increment the current question of the user
		// update the solved questions field
		return true;
	}
	
	public boolean updateProfileForHint(String username, String updated_score, String hint) {
		// TODO
		// update the table with hint
		// deduced new score should be updated
		return true;
	}
	
	public Cursor getQuestionByID(String question_no) {
		// CHECKED
		Cursor mCursor = mDb.query(true, QUESTIONS_TABLE, new String[] { KEY_QUESTION_NO,  KEY_QUESTION_ID,  KEY_ANSWER,  KEY_HINT1,  KEY_HINT2 }
		, KEY_QUESTION_NO + " = '" + question_no + "'", null, null, null, null, null);
		return mCursor;
	}

	public long insertQuestion(String question_no, String question_id, String answer, String hint1, String hint2) {
		// CHECKED
		ContentValues questionData = new ContentValues();
		questionData.put(KEY_QUESTION_NO, question_no);
		questionData.put(KEY_QUESTION_ID, question_id);
		questionData.put(KEY_ANSWER, answer);
		questionData.put(KEY_HINT1, hint1);
		questionData.put(KEY_HINT2, hint2);
		
		return mDb.insert(QUESTIONS_TABLE, null , questionData);
	}
	public void insertSomeQuestions() {
		// TODO
		// Insert questions via method insertQuestion(...)
	}
	
	public long insertNotification(String notification) {
		Date current = new Date();
		ContentValues notif = new ContentValues();
		notif.put(KEY_DATE, current.toString());
		notif.put(KEY_NOTIFICATION, notification);
		
		return mDb.insert(NOTIFICATIONS_TABLE, null, notif);
	}
	
	public void insertSomeNotifications() {
		// TODO
		// Insert some notifications using insertNotification method
	}

}
