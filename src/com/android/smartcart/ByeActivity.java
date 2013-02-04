package com.android.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ByeActivity extends Activity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "ThankYou"; //TAG for Log purposes

	private TextView email;
	private Button feedback;
	private TextView feedbackTextView;
	
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
		
		feedback = (Button) this.findViewById(R.id.feedback);
		feedback.setOnClickListener(this);
		
		feedbackTextView = (TextView) this.findViewById(R.id.feedbackTextView);
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
		int id = arg0.getId();
		/**
		 * When click on view.ID, perform ___
		 */
		switch(id){
			case R.id.feedback:
				showFeedback();
				break;
		}
	}

	//Show Feadback
	private void showFeedback(){
		String feedbackString = "";
		for(String line: SmartCartActivity.feedback){
			feedbackString += line + "\t\t";
		}
		feedbackTextView.setText(feedbackString);
		feedbackTextView.setSelectAllOnFocus(true);
	}

}
