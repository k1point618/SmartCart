package com.android.smartcart;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out); //Override Default Android Transition
		setContentView(R.layout.activity_find_item);
		super.onCreate(savedInstanceState);
		
		mResultVerticalLayout = (LinearLayout) findViewById(R.id.findResultVerticalLayout);
		
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
		
			//4.2 (Later) Add icon on Correstponding Location on the Map. 
	}
	
	
	
	
	
	
	
	
	
	
	
}
