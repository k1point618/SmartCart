package com.android.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class WelcomeActivity extends Activity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "Welcome"; //TAG for Log purposes
	
	private ImageButton button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		//2. Set Content before calling Super. Because super gets the variable
		setContentView(R.layout.activity_welcome);
		super.onCreate(savedInstanceState);
		
		button = (ImageButton) findViewById(R.id.WelcomeButton);
		button.setOnClickListener(this);
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome, menu);
		return true;
	}

	/**
	 * Click Listener for Menu Options. 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//Handle item selection
		Intent intent = null;
		switch(item.getItemId()){
			case R.id.menu_end_session:
				SmartCartActivity.model = null;
				intent = new Intent(this, ThankYouActivity.class);
				startActivity(intent);
				return true;
			case R.id.menu_restart:
				SmartCartActivity.model = null;
				intent = new Intent(this, WelcomeActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
			case R.id.WelcomeButton:
				Intent intent = new Intent(this, MyCartActivity.class);
				startActivity(intent);
				break;
		}

	}
}
