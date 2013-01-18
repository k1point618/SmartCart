package com.android.smartcart;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
	public static final int FIND_RESULT_VERTICAL_LAYOUT = 33;
	public static final int RECOMMENDATION_VERTICAL_LAYOUT = 333;
	public static final int COUPON_VERTICAL_LAYOUT = 3333;
	public final static int ITEMIZED_FONT_SIZE = 30;
	public final static int ITEMIZED_SUB_FONT_SIZE = 20;
	public static final String TUTORIAL_IMAGE = "tutorial";
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
				startFindItemActivity(null);
				break;
			case R.id.button3:
				startMyCartActivity();
				break;
			case R.id.button4:
				startCouponsActivity();
				break;
			case R.id.button5:
				startCheckoutActivity();
				break;
		}
	}

	
	//Action Response Methods to each of the 5 buttons, 
	//Should be overridden in sub classes. 
	protected void startCheckoutActivity() {
		Intent intent = new Intent(this, CheckoutActivity.class);
		startActivity(intent);
	}

	protected void startCouponsActivity() {
		Intent intent = new Intent(this, CouponsActivity.class);
		startActivity(intent);
	}

	//This is the Default activity
	protected void startMyCartActivity() {
		Intent intent = new Intent(this, MyCartActivity.class);
		startActivity(intent);
	}

	protected void startFindItemActivity(String search) {
		Intent intent = new Intent(this, FindItemActivity.class);
		intent.putExtra("search", search);
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
	protected void loadItemsToVerticalLayout(ArrayList<Item> items, LinearLayout mVerticalLayout, int layoutID){
		
		/**
		 * TODO: This removes Find items from previous find... need fix
		 */
		mVerticalLayout.removeAllViews();
		
		for(Item item: items){
			LinearLayout rec = new LinearLayout(this); 	// rec: a single horizontal rectangle
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
			image = resize(image, 129, 120);
			imageButton.setImageDrawable(image);
			imageButton.setContentDescription(item.getName());
			imageButton.setOnClickListener(new OnClickListener(){
				@Override 
				public void onClick(View view){
					startFindItemActivity(view.getContentDescription().toString());
				}
			});
			
			LayoutParams imageParams = new LayoutParams(120, 120);
			imageButton.setLayoutParams(imageParams);
			rec.addView(imageButton);
			
			TextView label = new TextView(this);
			label.setText(item.getName() + "\n$" + item.getSalePriceText());
			//If layout is Find's Vertical Result Layout, then Show Location:
			if(layoutID == FIND_RESULT_VERTICAL_LAYOUT){
				imageButton.setEnabled(false);
				label.setText(item.getName() + "\n$" + item.getSalePriceText() + 
						"\nLocated at Shelf Number: " + item.getLocation());
			}
			else if(layoutID == COUPON_VERTICAL_LAYOUT){
				label.setText(item.getName() + "\nUsed to be: $" + item.getOriginalPriceText() + 
						"\nNOW: $" + item.getSalePriceText());
			}
			
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
			params.setMargins(5, 5, 5, 5);
			label.setLayoutParams(params);
			rec.addView(label);
			mVerticalLayout.addView(rec);
		}
	}
	
	/**
	 * Resizes Drwable
	 * @param image
	 * @return
	 */
	private Drawable resize(Drawable image, int w, int h) {
	    Bitmap d = ((BitmapDrawable)image).getBitmap();
	    int imageW = d.getWidth();
	    int imageH = d.getHeight();
	    
	    if(imageW/(double)imageH >= w/(double)h){
	    	w = imageW * h / imageH;
	    }else{
	    	h = imageH * w / imageW;
	    }
	    Bitmap bitmapOrig = Bitmap.createScaledBitmap(d, w, h, false);
	    return new BitmapDrawable(bitmapOrig);
	}
}
