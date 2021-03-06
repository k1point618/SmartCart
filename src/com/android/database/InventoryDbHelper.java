package com.android.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.android.model.Item;
import com.android.smartcart.SmartCartActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDbHelper extends SQLiteOpenHelper{

	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Inventory.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    
    //Query that creates the database defined in the InventoryReaderContract class
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + InventoryReaderContract.InventoryEntry.TABLE_NAME + " (" +
        InventoryReaderContract.InventoryEntry._ID + " INTEGER PRIMARY KEY," +
        InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE + TEXT_TYPE + COMMA_SEP +
        InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
        InventoryReaderContract.InventoryEntry.COLUMN_NAME_ORIGINAL_PRICE + DOUBLE_TYPE  + COMMA_SEP +
        InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE + DOUBLE_TYPE  + COMMA_SEP +
        InventoryReaderContract.InventoryEntry.COLUMN_NAME_LOCATION + INT_TYPE  + COMMA_SEP +
        InventoryReaderContract.InventoryEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
        " )";

    //Command might be incorrect. 
    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + InventoryReaderContract.InventoryEntry.TABLE_NAME;

    /**
     * Constructor
     * @param context
     */
    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * Create the Database, but executing the Create command
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    
    /**
     * For Upgrading. Currently Not necessary. 
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    
    /**
	 * Static Method that Loads data into the database. This should only be done once
	 * at the program's start. 
	 */
	public static void loadData(Context context, SQLiteDatabase db, String filename){
		
		AssetManager am = context.getAssets();
		
		//Delete everything that already exists in the database, upon installing. 
		db.delete(InventoryReaderContract.InventoryEntry.TABLE_NAME, null, null);
		
		try {
			InputStream is = am.open(filename);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String nextLine = "";
			while((nextLine=br.readLine()) != null){
				//Add line to database
				String[] entry = nextLine.split("\t");
				ContentValues values = new ContentValues();
				values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE, entry[0]);
				values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME, entry[1]);
				values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_ORIGINAL_PRICE, entry[2]);
				values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE, entry[3]);
				values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_LOCATION, entry[4]);
				values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_DESCRIPTION, entry[5]);
				
				db.insert(InventoryReaderContract.InventoryEntry.TABLE_NAME, null, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Add A new Item into the database 
	 * ALSO need to write new items to Database file: data.txt. 
	 * @param context
	 * @param db
	 * @param item
	 */
	public static void addItem(Context context, SQLiteDatabase db, Item item){
		
		ContentValues values = new ContentValues();
		values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE, item.getBarcode());
		values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME, item.getName());
		values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_ORIGINAL_PRICE, item.getOriginalPrice());
		values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE, item.getSalePrice());
		values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_LOCATION, item.getLocation());
		values.put(InventoryReaderContract.InventoryEntry.COLUMN_NAME_DESCRIPTION, item.getDescription());
		db.insert(InventoryReaderContract.InventoryEntry.TABLE_NAME, null, values);
	}
	
	/**
	 * Given a list of keywords, return a list of items that has any of the keywords, in 
	 * its name or description
	 * @param keywords
	 * @return
	 */
	public ArrayList<Item> findOnShelf(String[] keywords){
		ArrayList<Item> matches = new ArrayList<Item>();
	
		if(keywords.length == 0){
			return matches;
		}
		
		//Query the Database
		SQLiteDatabase db = this.getWritableDatabase();
//		String[] projection = {
//				InventoryReaderContract.InventoryEntry._ID,
//				InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE,
//				InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME,
//				InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE,
//				InventoryReaderContract.InventoryEntry.COLUMN_NAME_DESCRIPTION
//		};
//		String sortOrder = InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE;
//		Cursor c = db.query(InventoryReaderContract.InventoryEntry.TABLE_NAME, 
//				projection, 
//				null, 
//				null, null, null, sortOrder);
		
		//Get Result
		String barcode = "";
		String name = "";
		Double sale_price = -1.0;
		Double original_price = -1.0;
		String description = "";
		int location = 0;
		
		//TODO: Temporary until figure out how DB works
		Cursor c = db.rawQuery("select * from inventory", null);
		c.moveToFirst();
		
		do{
			name = c.getString(c.getColumnIndex(
					InventoryReaderContract.InventoryEntry.COLUMN_NAME_NAME));
			description = c.getString(c.getColumnIndex(
					InventoryReaderContract.InventoryEntry.COLUMN_NAME_DESCRIPTION));
			
			//Search for keywords in name and Description
			if (match(keywords, name) || match(keywords, description)){
				barcode = c.getString(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_BARCODE));
				sale_price = c.getDouble(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_SALE_PRICE));
				original_price = c.getDouble(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_ORIGINAL_PRICE));
				location = c.getInt(c.getColumnIndex(
						InventoryReaderContract.InventoryEntry.COLUMN_NAME_LOCATION));
				Item i = new Item(name, description, sale_price, original_price, location, barcode);
				matches.add(i);
			}
		}while(c.moveToNext());
		return matches;
	}

	/**
	 * Returns true if the text contains any of the keywrods. 
	 * @param keywords
	 * @param text
	 * @return
	 */
	private boolean match(String[] keywords, String text) {
		String[] textWords = text.split(" ");
		for(String k: keywords){
			for(String t: textWords){
				if(k.toLowerCase().equals(t.toLowerCase())){
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<Item> getCoupons(){
		ArrayList<Item> coupons = new ArrayList<Item>();
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("select * from inventory", null);
		c.moveToFirst();
		
		String barcode = "";
		String name = "";
		Double sale_price = -1.0;
		Double original_price = -1.0;
		String description = "";
		int location = 0;
		
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
			
			//Search for keywords in name and Description
			if (sale_price < original_price){
				coupons.add(i);
			}
		}while(c.moveToNext());
		
		return coupons;
	}
	
}
