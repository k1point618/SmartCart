package com.android.smartcart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
public class CheckoutActivity extends SmartCartActivity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "CheckoutActivity"; //TAG for Log purposes
	private LinearLayout mSwipeNSign;
	private TextView mTotalTextView;
	private LinearLayout mCheckoutVerticalLayout;
	private Button mMockButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);	//Override Default Android Transition
		setContentView(R.layout.activity_checkout);
		super.onCreate(savedInstanceState);
		
		mSwipeNSign = (LinearLayout) findViewById(R.id.swipe_n_sign);
		mTotalTextView = (TextView) findViewById(R.id.totalTextView);
		mTotalTextView.setText(SmartCartActivity.model.getPriceHeaderDisplay());
		mCheckoutVerticalLayout = (LinearLayout) findViewById(R.id.checkoutVerticalLayout);
		
		//TODO: Clicking on mSqipeNSign will = input
		mMockButton = new Button(this);
		mMockButton.setText("Click Here");
		mMockButton.setContentDescription("mMockButton");
		mMockButton.setOnClickListener(this);
		mSwipeNSign.addView(mMockButton);
	}
	
	@Override
	public void onClick(View view){
		super.onClick(view);
		if(view.getContentDescription().equals("mMockButton")){
			swiped(0);
		}
	}

	private void swiped(int cardNumber) {
		/**
		 * Assume the Card gets Charged.
		 */
		mSwipeNSign.removeAllViews();
		
		//Make Signature pad
		
		
	}
	
	
}
