package com.android.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class ThankYouActivity extends Activity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "ThankYou"; //TAG for Log purposes

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		//2. Set Content before calling Super. Because super gets the variable
		setContentView(R.layout.activity_thankyou);
		super.onCreate(savedInstanceState);
		
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_coupons, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//Handle item selection
		switch(item.getItemId()){
			case R.id.menu_end_session:
				endSession();
				return true;
			case R.id.menu_restart:
				restart();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Restart will clear existing data in the model, and return to MyCartActivity
	 */
	private void restart() {
		SmartCartActivity.model = null;
		Intent intent = new Intent(this, MyCartActivity.class);
		startActivity(intent);
		return;
		
	}

	/**
	 * EndSession will clear existing data in the model, and return to Welcome Window
	 */
	private void endSession() {
		SmartCartActivity.model = null;
		Intent intent = new Intent(this, WelcomeActivity.class);
		startActivity(intent);
		return;
	}
	@Override
	public void onClick(View arg0) {
		
	}

}
