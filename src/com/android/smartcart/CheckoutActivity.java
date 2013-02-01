package com.android.smartcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class CheckoutActivity extends Activity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "CheckoutActivity"; //TAG for Log purposes
	private LinearLayout mSwipeNSign;
	private TextView mTotalTextView;
	private LinearLayout mCheckoutVerticalLayout;
	private Button mBackButton;
	private DrawView mSignature;
	private TextView mCheckoutInstruction;
	private LinearLayout mInstructionLine;
	private final int mClear_id = 12345;
	private final int mFinish_id = 23412;
	
	//TODO: TEMP
	private Button mMockButton;
	private final int mMockButton_id=1010110;
	
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
		mBackButton = (Button) findViewById(R.id.checkoutBackButton);
		mBackButton.setOnClickListener(this);
		mCheckoutInstruction = (TextView) findViewById(R.id.checkoutInstruction);
		mInstructionLine = (LinearLayout) findViewById(R.id.instruction_line);
		
//		//TODO: Clicking on mSqipeNSign will = input
//		mMockButton = new Button(this);
//		mMockButton.setText("Click Here");
//		mMockButton.setContentDescription("mMockButton");
//		mMockButton.setId(mMockButton_id);
//		mMockButton.setOnClickListener(this);
//		mSwipeNSign.addView(mMockButton);
		
		SwipeListenerThread slt = new SwipeListenerThread(this);
		Thread t = new Thread(slt);
		t.start();
	}
	
	/**
	 * Listener Thread for Swiping. 
	 * @author kerengu
	 *
	 */
	public class SwipeListenerThread implements Runnable{

		private SoundMeter sm;
		private boolean listening = true;
		private CheckoutActivity activity;
		public SwipeListenerThread(CheckoutActivity c){
			sm = new SoundMeter();
			sm.start();
			activity = c;
		}
		
		@Override
		public void run() {
			while(listening){
				double amp = sm.getAmplitude();
				Log.i(TAG, "SwipeListenerThread() ... Listening = " + amp);
			
				//TODO: Frequency here
				if(amp > 12 && amp < 13){
					listening = false;
					activity.runOnUiThread(new Runnable(){
						@Override 
						public void run(){
							processing();
							Handler handler = new Handler(); 
						    handler.postDelayed(new Runnable() { 
						         public void run() { 
						        	 swiped(0);
						        	 addButtons();
						         } 
						    }, 2000); 
						}
					});
				}
				//Wait for 1 sec
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			sm.stop();
		}
		
	}
	
	@Override
	public void onClick(View view){
		
		int id = view.getId();
		
		switch(id){
			case 1010110:	//TODO: this case to be deleted 
				processing();
				Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	 swiped(0);
			        	 addButtons();
			         } 
			    }, 2000); 
			    break;
			case R.id.checkoutBackButton:
				back();
				break;
			case 12345:
				swiped(0);
				break;
			case 23412:
				completeTrasaction();
				break;
			
		}
		
	}

	/**
	 * Complete transaction by asking for their email, after signing. 
	 */
	private void completeTrasaction() {
		Intent intent = new Intent(this, ThankYouActivity.class);
		startActivity(intent);
	}

	private void processing() {
		mCheckoutInstruction.setText("Processing your transaction... ");
	}

	/**
	 * Button5 is now the Back Button
	 */
	private void back() {
		finish();
	}

	/**
	 * Should be triggered when the mic detects some sort of input!
	 * @param cardNumber
	 */
	private void swiped(int cardNumber) {
		/**
		 * Assume the Card gets Charged.
		 */
		
		//Make the signature pad
		mSignature =  new DrawView(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, mSwipeNSign.getMeasuredHeight());
		params.setMargins(5, 5, 5, 5);
		mSignature.setLayoutParams(params);
		mSignature.setBackgroundColor(R.drawable.white);
		mSwipeNSign.removeAllViews();
		mSwipeNSign.addView(mSignature);
		
		//Change the instruction
		mCheckoutInstruction.setText("Please Sign Above");
		
	}
	
	private void addButtons() {
		//Add Buttons
		Button clear = new Button(this);
		clear.setText("Clear");
		LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
		param.setMargins(5, 0, 5, 0);
		clear.setLayoutParams(param);
		clear.setId(mClear_id);
		clear.setOnClickListener(this);
		mInstructionLine.addView(clear);
		
		Button finish = new Button(this);
		finish.setText("Finish");
		finish.setLayoutParams(param);
		finish.setId(mFinish_id);
		finish.setOnClickListener(this);
		mInstructionLine.addView(finish);
	}

	
}
