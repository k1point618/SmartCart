package com.android.smartcart;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.model.Item;

public class MyCartActivity extends SmartCartActivity implements View.OnClickListener{

	/**
	 * Attributes
	 */
	private static final String TAG = "MyCartActivity"; //TAG for Log purposes
	public static final String DEFAULT_IMAGE = "m";
	
	//CenterPiece
	private TextView mTotalTextView;
    private LinearLayout mItemizedVerticalLayout;
    private ScrollView mItemizedScrollView;
    
    //Recommendations
    private LinearLayout mRecommendationVerticalLayout;
	private ScrollView mRecommendationScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		//2. Set Content before calling Super. Because super gets the variable
		setContentView(R.layout.activity_my_cart);
		super.onCreate(savedInstanceState);
		
		mTotalTextView = (TextView) findViewById(R.id.totalTextView);
		mTotalTextView.setText(SmartCartActivity.model.getPriceHeaderDisplay());
		mItemizedVerticalLayout = (LinearLayout) findViewById(R.id.itemizedVerticalLayout);
		mItemizedScrollView = (ScrollView) findViewById(R.id.itemizedScrollView);
		
		//3. Recommendation Scroll 
		mRecommendationVerticalLayout = (LinearLayout) findViewById(R.id.recommendationVeticalLayout);
		mRecommendationScrollView = (ScrollView) findViewById(R.id.recommendationScrollview);
				
		//Tests
		Item i = new Item("Chocolate", null, 4, 4, 0, "1234", null);
		model.addRecommendation(i);
		
		setItemizeView();
		loadRecommendations();
		
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
		
		//Tax and Total
		LinearLayout tax = ItemView.getTax(this);
		mItemizedVerticalLayout.addView(tax);
		LinearLayout total = ItemView.getTotal(this);
		mItemizedVerticalLayout.addView(total);
		
		//TODO
		TextView text = new TextView(this);
		text.setText(this.getDatabase(this.mDbHelper.getWritableDatabase()));
		mItemizedVerticalLayout.addView(text);
		
		//Set Scroll to bottom
		mItemizedScrollView.post(new Runnable() {

	        @Override
	        public void run() {
	            mItemizedScrollView.fullScroll(View.FOCUS_DOWN);
	        }
	    });

		
	}

	/**
	 * Create Linear layout as a recommendation, horizontal. Add to vertical layout
	 * Model should have a list of recommendations = list of Items. 
	 * There should be a "make recommendation" method, run at startup, and after every find, 
	 * and after every add.
	 */
	private void loadRecommendations() {
		
		
		for(Item item: SmartCartActivity.model.getRecommendations()){
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
			mRecommendationVerticalLayout.addView(rec);
		}
		
		
		
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
