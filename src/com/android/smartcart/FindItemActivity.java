package com.android.smartcart;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
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
		setContentView(R.layout.activity_find_item);
		super.onCreate(savedInstanceState);
		mFindEditText = (EditText) findViewById(R.id.find_edit_text);
		mFindEditText.clearFocus();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		mResultVerticalLayout = (LinearLayout) findViewById(R.id.findResultVerticalLayout);
		
		mFindButton = (Button) findViewById(R.id.find_button);
		mFindButton.setOnClickListener(this);
		
		mResultTextView = (TextView) findViewById(R.id.FindResultTextView);
		
	}
	
	@Override
	protected void startFindItemActivity() {
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
	private void find(String searchWords){
		
		String[] keywords = searchWords.split(" ");
		//1. Find Possible Shelf Numbers:
		ArrayList<Item> matches = SmartCartActivity.mDbHelper.findOnShelf(keywords);
		
		//2. (Maybe) Use Items and Location and get Image Location
		
		//4. Put Result on Screen
			//4.1: Result Column
		mResultVerticalLayout.removeAllViews();
		mResultTextView.setText("Found " + matches.size() + " Result(s):");
		loadItemsToVerticalLayout(matches, mResultVerticalLayout);
		this.mFindEditText.setText("");
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
