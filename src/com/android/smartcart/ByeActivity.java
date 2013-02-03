package com.android.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ByeActivity extends Activity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "ThankYou"; //TAG for Log purposes

	private TextView email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);	//Override Default Android Transition
		
		//2. Set Content before calling Super. Because super gets the variable
		setContentView(R.layout.activity_bye);
		super.onCreate(savedInstanceState);
		
		email = (TextView) this.findViewById(R.id.ByeEmail);
		email.setText(SmartCartActivity.USER_EMAIL + " .");
		
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_checkout, menu);
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
		// TODO Auto-generated method stub
		
	}


}
