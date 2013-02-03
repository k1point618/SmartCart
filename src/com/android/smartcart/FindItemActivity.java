package com.android.smartcart;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.model.Item;

public class FindItemActivity extends SmartCartActivity implements View.OnClickListener {

	/**
	 * Attributes
	 */
	private static final String TAG = "FindItemActivity"; //TAG for Log purposes
	
	//CenterPiece
	private EditText mFindEditText;
	private LinearLayout mResultVerticalLayout;
	private Button mFindButton;
	private TextView mResultTextView;
	private RelativeLayout mMapRelativeLayout;
	private ImageView mHere;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out); //Override Default Android Transition
		setContentView(R.layout.activity_find_item);
		super.onCreate(savedInstanceState);
		
		mResultVerticalLayout = (LinearLayout) findViewById(R.id.findResultVerticalLayout);
		mMapRelativeLayout = (RelativeLayout) findViewById(R.id.mapRelativeLayout);
		
		mHere = new ImageView(this);
		mMapRelativeLayout.addView(mHere);
		
		mFindButton = (Button) findViewById(R.id.find_button);
		mFindButton.setOnClickListener(this);
		
		mResultTextView = (TextView) findViewById(R.id.FindResultTextView);
		
		mFindEditText = (EditText) findViewById(R.id.find_edit_text);
		mFindEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
				if(actionId == EditorInfo.IME_ACTION_DONE){
					find(mFindEditText.getText().toString());
					return true;
				}
				return false;
			}
		});
		
		Intent myIntent = getIntent();
		if(myIntent != null){
			String search = myIntent.getStringExtra("search");
			if (search != null){
				find(search);
			}
		}
	}
	
	@Override
	protected void startFindItemActivity(String search) {
		return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_find_item, menu);
		return true;
	}
	
	@Override
	public void onClick(View view){
		super.onClick(view);
		
		int id = view.getId();
		switch(id){
			case R.id.find_button:
				find(mFindEditText.getText().toString());
				break;
		}
		
	}
	
	/**
	 * Reacts to clicking the Find button. 
	 * @param editable
	 */
	public void find(String searchWords){
		
		String[] keywords = searchWords.split(" ");
		//1. Find Possible Shelf Numbers:
		ArrayList<Item> matches = SmartCartActivity.mDbHelper.findOnShelf(keywords);
		
		//2. (Maybe) Use Items and Location and get Image Location
		
		//4. Put Result on Screen
			//4.1: Result Column
		mResultVerticalLayout.removeAllViews();
		mResultTextView.setText("Found " + matches.size() + " Result(s):");
		loadItemsToVerticalLayout(matches, mResultVerticalLayout, SmartCartActivity.FIND_RESULT_VERTICAL_LAYOUT);
		this.mFindEditText.setText("");
		
		//Hind the keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mFindEditText.getWindowToken(), 0);
		
	}
	
	/**
	 * Local's Loading the Item to vertical Layout. 
	 */
	protected void loadItemsToVerticalLayout(ArrayList<Item> items, LinearLayout mVerticalLayout, int layoutID){
		
		/**
		 * TODO: This removes Find items from previous find... need fix
		 */
		mVerticalLayout.removeAllViews();
		
		//Show first item's Location
		if(!items.isEmpty()){
			setLocation(items.get(0).getLocation());
		}
		
		for(final Item item: items){
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
			imageParams.setMargins(3, 3, 3, 3);
			imageButton.setLayoutParams(imageParams);
			rec.addView(imageButton);
			
			TextView label = new TextView(this);
			label.setText(item.getName() + "\n$" + item.getSalePriceText());
			label.setTextSize(20);
			//If layout is Find's Vertical Result Layout, then Show Location:
			if(layoutID == FIND_RESULT_VERTICAL_LAYOUT){
				//Enable Clicking and onclick listener.
				imageButton.setEnabled(true);
				imageButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view){
						setLocation(item.getLocation());
					}

				});
			}
			
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
			params.setMargins(5, 5, 5, 5);
			label.setLayoutParams(params);
			rec.addView(label);
			mVerticalLayout.addView(rec);
		}
	}
	
	/**
	 * Set the icon to the image given the location. 
	 * @param location
	 */
	private void setLocation(int location) {
		
		this.mMapRelativeLayout.removeView(mHere);
		
		//First create ImageView
		String imageFileName = "here";
		mHere = new ImageView(this);
		
		int imageResourceId = 0;
		Drawable image;
		imageResourceId = this.getResources().getIdentifier(imageFileName, "drawable", getPackageName());
		image = this.getResources().getDrawable(imageResourceId);
		mHere.setImageDrawable(image);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_TOP);
		params.addRule(RelativeLayout.ALIGN_LEFT);
		
		int left = (int) (Math.random()*450 + 100);
		int top = (int) (Math.random()*350 + 50);
		
		params.setMargins(left, top, 0, 0);
		
		mHere.setLayoutParams(params);
		this.mMapRelativeLayout.addView(mHere);
	}
	
}
