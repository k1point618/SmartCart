package com.android.smartcart;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.android.model.Item;

public class CouponsActivity extends SmartCartActivity implements View.OnClickListener {

	/**
	 * Attributes
	 */
	private static final String TAG = "CouponsActivity"; //TAG for Log purposes
	private LinearLayout mCouponVerticalLayout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);	//Override Default Android Transition
		setContentView(R.layout.activity_coupons);
		super.onCreate(savedInstanceState);
		
		mCouponVerticalLayout = (LinearLayout) findViewById(R.id.CouponVerticalLayout);
		loadCoupons();
	}
	
	@Override
	protected void startCouponsActivity() {
		return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_coupons, menu);
		return true;
	}
	
	/**
	 * Get and Displays the list of coupons. 
	 */
	private void loadCoupons(){
		ArrayList<Item> coupons = SmartCartActivity.mDbHelper.getCoupons();
		
		loadItemsToVerticalLayout(coupons, mCouponVerticalLayout, SmartCartActivity.COUPON_VERTICAL_LAYOUT);
	}
	
	
}
