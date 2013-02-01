package com.android.smartcart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.database.InventoryDbHelper;
import com.android.database.InventoryReaderContract;
import com.android.model.Item;

public class AddItemActivity extends SmartCartActivity{

	/**
	 * Attributes
	 */
	private static final String TAG = "AddItemActivity"; //TAG for Log purposes
	
	//Add Item Dialog
    private AlertDialog mAddItemDialog;
    private AlertDialog mNewItemDialog;
    
    //Constants
    private static final int ADD_ITEM_DIALOG = 1;					//Dialog
	private static final int NEW_ITEM_DIALOG = 2;					//Dialog
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		//1. Add debug message to log file
		Log.i(TAG, "onCreate() ... savedIstanceState = " + savedInstanceState);
		setContentView(R.layout.empty_activity);
		super.onCreate(savedInstanceState);
		showAddItemDialog();
	}
	
	/**
	 * Called by Pressing Button 1
	 */
	private void showAddItemDialog() {
		showDialog(ADD_ITEM_DIALOG);
		
	}
	
	private void showNewItemDialog() {
		showDialog(NEW_ITEM_DIALOG);
		EditText e = (EditText) mNewItemDialog.findViewById(R.id.new_item_name_edit_text);
		e.setSelected(true);
	}
	@Override
	protected Dialog onCreateDialog(int id){
		
		// This is only run once (per dialog), the very first time
        // a given dialog needs to be shown.
		
		switch(id){
			case ADD_ITEM_DIALOG:
				LayoutInflater factory = LayoutInflater.from(this);
				final View addItemDialogView = factory.inflate(R.layout.add_item_dialog, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Add Item to Cart: ")
					.setIcon(0)
					.setView(addItemDialogView)
					.setPositiveButton(
							R.string.add_item_dialog_add, 
							new DialogInterface.OnClickListener(){
								
								@Override
								public void onClick(DialogInterface dialog, int whichButton){
									//USER Clicked OK
									dialog.dismiss();
									addNewItemFromBarcode();
								}

							})
					.setNegativeButton(
							R.string.add_item_dialog_cancel,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									//USER Clicked CANCEL
									//Nothing?
									finish();
								}
							});
				mAddItemDialog = builder.create();
				return mAddItemDialog;
			case NEW_ITEM_DIALOG:
				LayoutInflater factory2 = LayoutInflater.from(this);
				final View addItemDialogView2 = factory2.inflate(R.layout.new_item_dialog, null);
				AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
				builder2.setTitle("This item is not in the database. Please add item detail below.  ")
					.setIcon(0)
					.setView(addItemDialogView2)
					.setPositiveButton(
							R.string.new_item_dialog_database, 
							new DialogInterface.OnClickListener(){
								
								@Override
								public void onClick(DialogInterface dialog, int whichButton){
									//USER Clicked Add to Database
									addNewItemToDatabase(false);
									finish();
								}
							})
					.setNegativeButton(
							R.string.new_item_dialog_cancel,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									//USER Clicked CANCEL
									//Nothing?
									finish();
								}
							})
					.setNeutralButton(
							R.string.new_item_dialog_database_cart,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									addNewItemToDatabase(true);
									finish();
								}
							});
				mNewItemDialog = builder2.create();
//				EditText mBarcodeEditText = (EditText) mNewItemDialog.findViewById(R.id.new_item_barcode_edit_text);
				EditText addItemBarcode = (EditText) mAddItemDialog.findViewById(R.id.barcode_edit_text);
//				Editable e = addItemBarcode.getText();
//				String s = e.toString();
//				mBarcodeEditText.setText(s);
				addItemBarcode.setText("");
				return mNewItemDialog;
				
		}
		
		//When Dialog ID doesn't match anything
		return null;
	}

	/**
	 * When Supervised User clicks ADD upon inputting all info regarding 
	 * a new item. Add the item to database. Precondition: Assuming Barcode does not 
	 * already exist. 
	 */
	private void addNewItemToDatabase(boolean cart) {
		//Get inputs: Total 7 of them. 
		EditText mNameEditText = (EditText) mNewItemDialog.findViewById(R.id.new_item_name_edit_text);
		EditText mBarcodeEditText = (EditText) mNewItemDialog.findViewById(R.id.new_item_barcode_edit_text);
		EditText mSalePriceEditText = (EditText) mNewItemDialog.findViewById(R.id.new_item_sale_price_edit_text);
		EditText mOriginalPriceEditText = (EditText) mNewItemDialog.findViewById(R.id.new_item_listed_price_edit_text);
		EditText mImageEditText = (EditText) mNewItemDialog.findViewById(R.id.new_item_image_edit_text);
		EditText mLocationEditText = (EditText) mNewItemDialog.findViewById(R.id.new_item_location_edit_text);
		EditText mDescriptionEditText = (EditText) mNewItemDialog.findViewById(R.id.new_item_description_edit_text);
		
		//Check for required fields: Barcode, Price,
		if(mBarcodeEditText.getText().length() == 0 || mOriginalPriceEditText.getText().length() == 0){
			showNewItemDialog();
			return;
		}
		String iBarcode = mBarcodeEditText.getText().toString();
		String iOPrice = mOriginalPriceEditText.getText().toString();
		
		// Name can be default
		String iName = "";
		if(mNameEditText.getText().length() == 0){
			iName = "Item" + iBarcode;
		}
		else{
			iName = mNameEditText.getText().toString();
		}
		
		//Sale Price can be Original if Not entered
		String iSPrice = mSalePriceEditText.getText().toString();
		if(mSalePriceEditText.getText().length() == 0){
			iSPrice = iOPrice;
		}
		
		//If Location is none, then -1
		String iLocation = mLocationEditText.getText().toString();
		if(mLocationEditText.getText().length() == 0){
			iLocation = "-1";
		}
		//Create Item
		Item item = new Item(iName, mDescriptionEditText.getText().toString(), 
				Double.valueOf(iSPrice), Double.valueOf(iOPrice), 
				Integer.valueOf(iLocation),
				iBarcode);
		
		//Add to database
		InventoryDbHelper.addItem(this, mDbHelper.getWritableDatabase(), item);
		
		//Add to cart, or not
		if(cart){
			model.addItem(item);
			MyCartActivity.getRecommendations(item);
			
		}
				
		//Clear All fields
		mNameEditText.setText("");
		mBarcodeEditText.setText("");
		mSalePriceEditText.setText("");
		mOriginalPriceEditText.setText("");
		mImageEditText.setText("");
		mLocationEditText.setText("");
		mDescriptionEditText.setText("");
		
	}
	
	/**
	 * When the user clicks OK upon inputting a Barcode,
	 * the new item needs to 
	 * 		1) Be added to the View if it exists
	 * 		2) Prompt product detail if item doesn't exist yet. 
	 */
	private void addNewItemFromBarcode() {
	
		//Get the Barcode entered
		EditText mBarcodeEditText = (EditText) mAddItemDialog.findViewById(R.id.barcode_edit_text);
		String inputBarcode = mBarcodeEditText.getText().toString();
		
		//Query the Database
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String[] projection = {
				InventoryReaderContract.InventoryEntry._ID,
				InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE,
				InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME,
				InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE,
				InventoryReaderContract.InventoryEntry.COLUMN_NAME_ORIGINAL_PRICE,
		};
		String sortOrder = InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE;
		Cursor c = db.query(InventoryReaderContract.InventoryEntry.TABLE_NAME, 
				projection, 
				null, 
				null, null, null, sortOrder);
		
		//Get Result
		String barcode = "";
		String name = "";
		Double sale_price = -1.0;
		Double original_price = -1.0;
		c.moveToFirst();
		do{
			barcode = c.getString(c.getColumnIndex(
					InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE));
			if (barcode.equals(inputBarcode)){
				name = c.getString(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME));
				sale_price = c.getDouble(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE));
				original_price = c.getDouble(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_ORIGINAL_PRICE));
				break;
			}
		}while(c.moveToNext());
		
		//If nothing is found - no matching barcode
		if(name == "" || sale_price == -1.0){
			showNewItemDialog();
			return;
		}
		
		//Create Item and Add Item to cart
		Item item = new Item(name, null, sale_price, original_price, 0, barcode);
		model.addItem(item);
		MyCartActivity.getRecommendations(item);
	
		startMyCartActivity();
		mBarcodeEditText.setText("");
		finish();
		
	}
	
}
