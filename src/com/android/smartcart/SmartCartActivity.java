package com.android.smartcart;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
    
    public static String USER_EMAIL = "";
    
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
	
	public static ArrayList<String> feedback = new ArrayList<String>();
	
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
		startActivity(intent);
		
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
		MyCartActivity.FIRST_TIME = true;
		Intent intent = new Intent(this, WelcomeActivity.class);
		startActivity(intent);
		return;
		
	}

	/**
	 * EndSession will clear existing data in the model, and return to Welcome Window
	 */
	private void endSession() {
		Intent intent = new Intent(this, ThankYouActivity.class);
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
	
		mVerticalLayout.removeAllViews();
		int counter = 0;
		int IMAGE_SIZE = 100;
		
		for(Item item: items){
			
			//Limit the number of items revealved for Recommendation. 
			counter ++;	
			if (layoutID == RECOMMENDATION_VERTICAL_LAYOUT){
				if (counter > 10){
					break;
				}
			}
			
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
			image = resize(image, IMAGE_SIZE, IMAGE_SIZE);
			imageButton.setImageDrawable(image);
			imageButton.setContentDescription(item.getName());
			imageButton.setOnClickListener(new OnClickListener(){
				@Override 
				public void onClick(View view){
					startFindItemActivity(view.getContentDescription().toString());
				}
			});
			
			LayoutParams imageParams = new LayoutParams(IMAGE_SIZE, IMAGE_SIZE);
			imageParams.setMargins(0, 3, 3, 3);
			imageButton.setLayoutParams(imageParams);
			rec.addView(imageButton);
			
			// ADD LABEL to the itemized List.
			TextView label = new TextView(this);
			label.setTextSize(20);
			label.setText(item.getName() + "\n$" + item.getSalePriceText());
			//If layout is Find's Vertical Result Layout, then Show Location:
			if(layoutID == FIND_RESULT_VERTICAL_LAYOUT){
				//Enable Clicking and onclick listener.
				imageButton.setEnabled(true);
				label.setText(item.getName() + "\n$" + item.getSalePriceText() + 
						"\nLocated at Shelf Number: " + item.getLocation());
			}
			else if(layoutID == COUPON_VERTICAL_LAYOUT){
				label.setText(item.getName() + "\nUsed to be: $" + item.getOriginalPriceText() + 
						"\nNOW: $" + item.getSalePriceText());
			}
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
			params.setMargins(5, 5, 5, 5);
			label.setLayoutParams(params);
			rec.addView(label);
			
			//4. Add Thumbs Down Button
			if(layoutID == RECOMMENDATION_VERTICAL_LAYOUT){
				ImageButton removeButton = new ImageButton(this);
				String deleteFileName = "dislike";
				int deleteResourceid=0;
				Drawable deleteDrawable;
				deleteResourceid = this.getResources().getIdentifier(deleteFileName, "drawable", this.getPackageName());
				deleteDrawable = this.getResources().getDrawable(deleteResourceid);
				removeButton.setImageDrawable(deleteDrawable);
				removeButton.setContentDescription(item.getBarcode());
				removeButton.setOnClickListener(new OnClickListener(){
					@Override 
					public void onClick(View view){
						removeRecommendation(view.getContentDescription().toString());
					}
				});
				LayoutParams deleteParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
				deleteParams.gravity = Gravity.CENTER_VERTICAL;
				removeButton.setLayoutParams(deleteParams);
				removeButton.setBackgroundResource(R.drawable.recommendation_background_color);
				rec.addView(removeButton);
			}
			
			// Add Discount and UP Button for Coupons
			if(layoutID == COUPON_VERTICAL_LAYOUT){
				
				//ADD DISCOUNT %OFF in RED
				TextView discountLabel = new TextView(this);
				double discount = 1-item.getSalePrice()/item.getOriginalPrice();
				discount = round(discount*100, 2);
				discountLabel.setText(discount + "% OFF");
				discountLabel.setTextAppearance(getApplicationContext(), R.style.boldText);
				discountLabel.setTextSize(30);
				discountLabel.setTextColor(getResources().getColor(R.color.Discount));
				
				LayoutParams discountParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
				discountParams.setMargins(5, 5, 5, 5);
				discountParams.gravity = Gravity.CENTER_VERTICAL;
				discountLabel.setLayoutParams(discountParams);
				rec.addView(discountLabel);
				
				//Search Button ---? Find.
				ImageButton findButton = new ImageButton(this);
				String deleteFileName = "find";
				int deleteResourceid=0;
				Drawable deleteDrawable;
				deleteResourceid = this.getResources().getIdentifier(deleteFileName, "drawable", this.getPackageName());
				deleteDrawable = this.getResources().getDrawable(deleteResourceid);
				findButton.setImageDrawable(deleteDrawable);
				findButton.setContentDescription(item.getName());	//Find using the Item's Name
				findButton.setOnClickListener(new OnClickListener(){
					@Override 
					public void onClick(View view){
						startFindItemActivity(view.getContentDescription().toString());
					}
				});
				LayoutParams deleteParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
				deleteParams.gravity = Gravity.CENTER_VERTICAL;
				findButton.setLayoutParams(deleteParams);
				findButton.setBackgroundResource(R.drawable.recommendation_background_color);
				rec.addView(findButton);
			}
			
			mVerticalLayout.addView(rec);
		}
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	/**
	 * Resizes Drwable
	 * @param image
	 * @return
	 */
	protected Drawable resize(Drawable image, int w, int h) {
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

	/**
	 * Remove item from list and then refresh;
	 * @param barcode
	 */
	public void removeItem(String barcode) {
		SmartCartActivity.model.deleteItem(barcode);
		Intent intent = new Intent(this, MyCartActivity.class);
		startActivity(intent);
		
	}
	
	/**
	 * Remove item from list and then refresh;
	 * @param barcode
	 */
	public void removeRecommendation(String barcode) {
		SmartCartActivity.model.deleteRecommendation(barcode);
		Intent intent = new Intent(this, MyCartActivity.class);
		startActivity(intent);
		
	}
}
