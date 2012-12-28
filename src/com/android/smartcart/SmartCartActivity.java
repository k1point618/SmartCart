package com.android.smartcart;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.database.InventoryDbHelper;
import com.android.model.Item;
import com.android.model.SmartCartModel;

public class SmartCartActivity extends Activity implements View.OnClickListener{

	//UI Elements
		//Buttons
	protected Button mAddItemButton;
    private Button mFindItemButton;
    private Button mMyCartButton;
    private Button mCouponButton;
    private Button mCheckoutButton;
    
    //Model
    protected static SmartCartModel model;
    
    //Database
    protected static InventoryDbHelper mDbHelper;
    
    //Constants
    private static final int ADD_ITEM_DIALOG = 1;					//Dialog
	private static final int NEW_ITEM_DIALOG = 2;					//Dialog
    private final String TAG = "SmartCartActivity";
    private static final String RAW_DATA_FILENAME = "data.txt";		//Database
    public static final String DEFAULT_IMAGE = "m";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		super.onCreate(savedInstanceState);
		//Look up UI Elements
		mAddItemButton = (Button) findViewById(R.id.button1);
		mAddItemButton.setOnClickListener(this);
		
		mFindItemButton = (Button) findViewById(R.id.button2);
		mFindItemButton.setOnClickListener(this);
		
		mMyCartButton = (Button) findViewById(R.id.button3);
		mMyCartButton.setOnClickListener(this);
		
		mCouponButton = (Button) findViewById(R.id.button4);
		mCouponButton.setOnClickListener(this);
		
		mCheckoutButton = (Button) findViewById(R.id.button5);
		mCheckoutButton.setOnClickListener(this);
		
		if(SmartCartActivity.model == null){
			//Initiate Model
			model = new SmartCartModel();
			//Create and load The Database ======================
			
			mDbHelper = new InventoryDbHelper(this);
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			InventoryDbHelper.loadData(this, db, RAW_DATA_FILENAME);
			
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		
		/**
		 * When click on view.ID, perform ___
		 */
		switch(id){
			case R.id.button1:
				startAddItemActivity();
				break;
			case R.id.button2:
				startFindItemActivity();
				break;
			case R.id.button3:
				startMyCartActivity();
				break;
			case R.id.button4:
				startCouponActivity();
				break;
			case R.id.button5:
				startCheckoutActivity();
				break;
		}
	}

	
	//Action Response Methods to each of the 5 buttons, 
	//Should be overridden in sub classes. 
	protected void startCheckoutActivity() {
		// TODO Auto-generated method stub
		
	}

	protected void startCouponActivity() {
		// TODO Auto-generated method stub
		
	}

	//This is the Default activity
	protected void startMyCartActivity() {
		Intent intent = new Intent(this, MyCartActivity.class);
		startActivity(intent);
	}

	protected void startFindItemActivity() {
		Intent intent = new Intent(this, FindItemActivity.class);
		startActivity(intent);
	}
	
	protected void startAddItemActivity() {
		Intent intent = new Intent(this, AddItemActivity.class);
		startActivityForResult(intent, 0);
		
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
		//TODO: When Welcome window is introduced, start Welcome window here. 
		Intent intent = new Intent(this, WelcomeActivity.class);
		startActivity(intent);
		return;
	}
	
	/**
	 * Given a list of items and a Vertical Linear Layout:
	 * Load the list of items to the layout with Item Image as Button, Item description,
	 * and Item price. 
	 * @param items
	 * @param mVerticalLayout
	 */
	protected void loadItemsToVerticalLayout(ArrayList<Item> items, LinearLayout mVerticalLayout){
		
		for(Item item: items){
			LinearLayout rec = new LinearLayout(this);
			rec.setOrientation(LinearLayout.HORIZONTAL);
			
			String imageFileName = "m" + item.getBarcode();
			ImageButton imageButton = new ImageButton(this);
			
			int imageResourceId = 0;
			Drawable image;
			try{
				imageResourceId = this.getResources().getIdentifier(imageFileName, "drawable", getPackageName());
				image = this.getResources().getDrawable(imageResourceId);
			}catch(Exception e){
				imageResourceId = this.getResources().getIdentifier(DEFAULT_IMAGE, "drawable", getPackageName());
				image = this.getResources().getDrawable(imageResourceId);
			}
			imageButton.setImageDrawable(image);
			imageButton.setOnClickListener(new OnClickListener(){
				@Override 
				public void onClick(View arg0){
					startFindItemActivity();
				}
			});
			rec.addView(imageButton);
			
			TextView label = new TextView(this);
			label.setText(item.getName() + "\n$" + item.getSalePriceText());
			rec.addView(label);
			mVerticalLayout.addView(rec);
		}
	}
}
