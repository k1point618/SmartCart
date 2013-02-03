package com.android.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ThankYouActivity extends Activity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "ThankYou"; //TAG for Log purposes

	private Button yesButton;
	private Button noButton;
	private EditText email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		//1.5
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);	//Override Default Android Transition
		
		//2. Set Content before calling Super. Because super gets the variable
		setContentView(R.layout.activity_thankyou);
		super.onCreate(savedInstanceState);
		
		yesButton = (Button) this.findViewById(R.id.YesButton);
		yesButton.setOnClickListener(this);
		noButton = (Button) this.findViewById(R.id.NoButton);
		noButton.setOnClickListener(this);
		email = (EditText) this.findViewById(R.id.Email);
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_coupons, menu);
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
	public void onClick(View view) {
		int id = view.getId();
		
		/**
		 * When click on view.ID, perform ___
		 */
		switch(id){
			case R.id.YesButton:
				yes();
				break;
			case R.id.NoButton:
				no();
				break;
		}
	}

	/**
	 * User Doesn't like SmartCart
	 */
	private void no() {
		SmartCartActivity.USER_EMAIL = email.getText().toString();
		Intent intent = new Intent(this, ByeActivity.class);
		startActivity(intent);
	}

	/**
	 * User Likes smartcart
	 */
	private void yes() {
		SmartCartActivity.USER_EMAIL = email.getText().toString();
		Intent intent = new Intent(this, ByeActivity.class);
		startActivity(intent);
	}

}
