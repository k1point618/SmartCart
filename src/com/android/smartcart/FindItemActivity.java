package com.android.smartcart;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class FindItemActivity extends SmartCartActivity implements View.OnClickListener {

	/**
	 * Attributes
	 */
	private static final String TAG = "FindItemActivity"; //TAG for Log purposes
	
	//CenterPiece
	private EditText mFindEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		setContentView(R.layout.activity_find_item);
		super.onCreate(savedInstanceState);
		mFindEditText = (EditText) findViewById(R.id.find_edit_text);
		mFindEditText.clearFocus();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	@Override
	protected void startFindItemActivity() {
		
		return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_find_item, menu);
		return true;
	}
}
