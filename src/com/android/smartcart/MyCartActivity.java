package com.android.smartcart;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.database.InventoryReaderContract;
import com.android.model.Item;

public class MyCartActivity extends SmartCartActivity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "MyCartActivity"; //TAG for Log purposes
	
	//CenterPiece
	private TextView mTotalTextView;
    private LinearLayout mItemizedVerticalLayout;
    private ScrollView mItemizedScrollView;
    private LinearLayout mMainLinearLayout;
    private ImageView mTutorial;
    
    //Recommendations
    private LinearLayout mRecommendationVerticalLayout;
	private ScrollView mRecommendationScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		
		//2. Set Content before calling Super. Because super gets the variable
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		setContentView(R.layout.activity_my_cart);
		super.onCreate(savedInstanceState);
		
		mTotalTextView = (TextView) findViewById(R.id.totalTextView);
		mTotalTextView.setText(SmartCartActivity.model.getPriceHeaderDisplay());
		mItemizedVerticalLayout = (LinearLayout) findViewById(R.id.itemizedVerticalLayout);
		mItemizedScrollView = (ScrollView) findViewById(R.id.itemizedScrollView);
		mMainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
		mTutorial = (ImageView) findViewById(R.id.tutorial);
		
		//3. Recommendation Scroll 
		mRecommendationVerticalLayout = (LinearLayout) findViewById(R.id.recommendationVeticalLayout);
		mRecommendationScrollView = (ScrollView) findViewById(R.id.recommendationScrollview);
				
		getRecommendations(null);
		
		setItemizeView();
		
	}

	public static void getRecommendations(Item item) {
		
		if(SmartCartActivity.model.getRecommendations().size() == 0){
			SQLiteDatabase db = SmartCartActivity.mDbHelper.getWritableDatabase();
			Cursor c = db.rawQuery("select * from inventory", null);
			c.moveToFirst();
			
			String barcode = "";
			String name = "";
			Double sale_price = -1.0;
			Double original_price = -1.0;
			String description = "";
			int location = 0;
			
			for(int i = 0; i < 3; i ++){
				name = c.getString(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME));
				description = c.getString(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_DESCRIPTION));
				
				//Search for keywords in name and Description
				
				barcode = c.getString(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE));
				sale_price = c.getDouble(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE));
				original_price = c.getDouble(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_ORIGINAL_PRICE));
				location = c.getInt(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_LOCATION));
				Item j = new Item(name, description, sale_price, original_price, location, barcode);
				
				//Add maches to Recommendation. 
				SmartCartActivity.model.addRecommendation(j);
				c.moveToNext();
			}
		}
		else if(item != null){
			SQLiteDatabase db = SmartCartActivity.mDbHelper.getWritableDatabase();
			Cursor c = db.rawQuery("select * from inventory", null);
			c.moveToFirst();
			
			String barcode = "";
			String name = "";
			Double sale_price = -1.0;
			Double original_price = -1.0;
			String description = "";
			int location = 0;
			Boolean add = false;
			
			do{
				name = c.getString(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME));
				description = c.getString(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_DESCRIPTION));
				barcode = c.getString(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE));
				sale_price = c.getDouble(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE));
				original_price = c.getDouble(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_ORIGINAL_PRICE));
				location = c.getInt(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_LOCATION));
				Item i = new Item(name, description, sale_price, original_price, location, barcode);
				
				if(add){
					SmartCartActivity.model.addRecommendation(i);
					break;
				}
				if(barcode.equals(item.getBarcode())){
					add = true;
				}
				
			}while(c.moveToNext());
		}
	
		
	}

	/**
	 * For every item, add a view to the Vertical Layout
	 */
	private void setItemizeView() {
		
		mItemizedVerticalLayout.removeAllViews();
		ArrayList<Item> items = SmartCartActivity.model.getItems();
		
		for(Item i: items){
			LinearLayout ll = ItemView.getView(this, i);
			mItemizedVerticalLayout.addView(ll);
		}
		
		//Tax: Only display when there are items in cart. 
		if(items.size() > 0){
			
			if(mTutorial != null){
				mMainLinearLayout.removeView(mTutorial);
				mTutorial = null;
			}
			
			LinearLayout tax = ItemView.getTax(this);
			mItemizedVerticalLayout.addView(tax);
			//Set Scroll to bottom
			mItemizedScrollView.post(new Runnable() {

		        @Override
		        public void run() {
		            mItemizedScrollView.fullScroll(View.FOCUS_DOWN);
		        }
		    });
		}
		
//		else if(items.size() == 0){
//			LinearLayout instruction = getInstruction(this);
//			mItemizedVerticalLayout.addView(instruction);
//		}
		
		
		//TODO: DEBUG DATABASE View
//		TextView text = new TextView(this);
//		text.setText(this.getDatabase(this.mDbHelper.getWritableDatabase()));
//		mItemizedVerticalLayout.addView(text);
		
		

		loadRecommendations();
		
	}

	/**
	 * Load the instruction Image. 
	 * @param context
	 * @return
	 */
	private LinearLayout getInstruction(Context context) {
		LinearLayout toReturn = new LinearLayout(this); 	// rec: a single horizontal rectangle
		toReturn.setOrientation(LinearLayout.HORIZONTAL);
		
		String imageFileName = SmartCartActivity.TUTORIAL_IMAGE;
		ImageView imageView = new ImageView(this);
		
		int imageResourceId = 0;
		Drawable image;
		imageResourceId = this.getResources().getIdentifier(imageFileName, "drawable", getPackageName());
		image = this.getResources().getDrawable(imageResourceId);
		
		imageView.setImageDrawable(image);

		LayoutParams imageParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		imageParams.setMargins(0, 0, 0, 0);
		imageView.setLayoutParams(imageParams);
		toReturn.addView(imageView);
		
		return toReturn;
	}

	/**
	 * Create Linear layout as a recommendation, horizontal. Add to vertical layout
	 * Model should have a list of recommendations = list of Items. 
	 * There should be a "make recommendation" method, run at startup, and after every find, 
	 * and after every add.
	 */
	private void loadRecommendations() {
	
		ArrayList<Item> list = (ArrayList<Item>) SmartCartActivity.model.getRecommendations().clone();
		Collections.reverse(list);
		loadItemsToVerticalLayout(list, mRecommendationVerticalLayout,
				SmartCartActivity.RECOMMENDATION_VERTICAL_LAYOUT);
		
//		for(Item i: list){
//			LinearLayout ll = ItemView.getView(this, i);
//			mRecommendationVerticalLayout.addRecommendationView(ll);
//		}
	}

	
	/**
	 * Sets the
	 */
	private String getDatabase(SQLiteDatabase db){
		
		//Make sure that the database is filled
		Cursor c = db.rawQuery("select * from inventory", null);
		c.moveToFirst();
		String result="";
		do{
			for(int i = 0; i < c.getColumnCount(); i ++){
				result += c.getString(i) + " ";
			}
			result += "\n";
		}while(c.moveToNext());
		
		for(Item i: SmartCartActivity.model.getRecommendations()){
			result += i.getName();
		}
		return "\n -------Database-------------\n" + result;
				
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		
		mTotalTextView.setText(SmartCartActivity.model.getPriceHeaderDisplay());
		setItemizeView();

	}

	@Override
	protected void startMyCartActivity() {
		return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_cart, menu);
		return true;
	}
}
