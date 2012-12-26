package com.android.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class InventoryReaderContract {

	/**
	 * Make constructor Private so no accidental creation of the
	 * class =)
	 */
	private InventoryReaderContract(){
		
	}
	
	/**
	 * Represents an item in the inventory. 
	 * (Not an item, but a representation of the item in the DB)
	 * @author kerengu
	 */
	public class InventoryEntry implements BaseColumns{

		public static final String TABLE_NAME = "inventory";
		
		public static final String COLUMN_NAME_BARCODE = "Barcode";
	    public static final String COLUMN_NAME_NAME = "Name";
	    public static final String COLUMN_NAME_ORIGINAL_PRICE = "OriginalPrice";
	    public static final String COLUMN_NAME_SALE_PRICE = "SalePrice";
	    public static final String COLUMN_NAME_LOCATION = "Location";
	    public static final String COLUMN_NAME_DESCRIPTION = "Description";
		 
	}
	
	
}
