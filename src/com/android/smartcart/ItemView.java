package com.android.smartcart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.model.Item;
import com.android.model.SmartCartModel;

public class ItemView{

	/**
	 * Takes an Item and returns the linear layout that represents
	 * the item. 
	 * @param item
	 * @return
	 */
	public static LinearLayout getView(Context context, Item item){
		LinearLayout toReturn = new LinearLayout(context);
		toReturn.setOrientation(LinearLayout.HORIZONTAL);
		
			//1. Add image
			String imageFileName = "m" + item.getBarcode();
			ImageView image = new ImageView(context);
			int imageResourceId=0;
			Drawable drawable;
			try{
				imageResourceId = context.getResources().getIdentifier(imageFileName, "drawable", context.getPackageName());
				drawable = context.getResources().getDrawable(imageResourceId);
			}catch(Exception e){
				imageResourceId = context.getResources().getIdentifier(MyCartActivity.DEFAULT_IMAGE, "drawable", context.getPackageName());
				drawable = context.getResources().getDrawable(imageResourceId);
			}
			image.setImageDrawable(drawable);
			
			toReturn.addView(image, new LayoutParams(120, 120));
			
			//2. Add 2ItemList
			LinearLayout twoItemList = new LinearLayout(context);
			twoItemList.setOrientation(LinearLayout.VERTICAL);
			
				//2.1 Name
				TextView nameTextView = new TextView(context);
				nameTextView.setTextSize(SmartCartActivity.ITEMIZED_FONT_SIZE);
				nameTextView.setText(item.getName());
				nameTextView.setGravity(Gravity.CENTER_VERTICAL);
				twoItemList.addView(nameTextView);
			
				//2.2 Barcode/description
				TextView barcodeView = new TextView(context);
				barcodeView.setTextSize(SmartCartActivity.ITEMIZED_SUB_FONT_SIZE);
				barcodeView.setText(item.getBarcode());
				twoItemList.addView(barcodeView);
				
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
			params.setMargins(5, 3, 5, 2);
			twoItemList.setLayoutParams(params);
			toReturn.addView(twoItemList);
			
			//3. Add Price
			TextView priceTextView = new TextView(context);
			priceTextView.setTextSize(SmartCartActivity.ITEMIZED_FONT_SIZE);
			priceTextView.setText("$" + item.getSalePriceText());
			toReturn.addView(priceTextView);
		
		LayoutParams llParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		toReturn.setLayoutParams(llParams);
		return toReturn;
		
	}

	public static LinearLayout getTax(Context context) {
		LinearLayout toReturn = new LinearLayout(context);
		
		TextView massTax = new TextView(context);
		massTax.setText(SmartCartModel.TAX_TEXT);
		massTax.setTextSize(SmartCartActivity.ITEMIZED_FONT_SIZE);
		massTax.setGravity(Gravity.CENTER_VERTICAL);
		toReturn.addView(massTax);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
		params.setMargins(5, 3, 5, 2);
		massTax.setLayoutParams(params);
		
		TextView priceTextView = new TextView(context);
		priceTextView.setTextSize(SmartCartActivity.ITEMIZED_FONT_SIZE);
		priceTextView.setText("$" + SmartCartActivity.model.getTaxText());
		toReturn.addView(priceTextView);
		
		LayoutParams llParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		toReturn.setLayoutParams(llParams);
		return toReturn;
	}

	public static LinearLayout getTotal(Context context) {
		
		LinearLayout toReturn = new LinearLayout(context);
		
		TextView total = new TextView(context);
		total.setText("TOTAL DUE:");
		total.setTextSize(SmartCartActivity.ITEMIZED_FONT_SIZE);
		total.setGravity(Gravity.CENTER_VERTICAL);
		toReturn.addView(total);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
		params.setMargins(5, 3, 5, 2);
		total.setLayoutParams(params);
		
		TextView priceTextView = new TextView(context);
		priceTextView.setTextSize(SmartCartActivity.ITEMIZED_FONT_SIZE);
		priceTextView.setText("$" + SmartCartActivity.model.getTotalPrice());
		toReturn.addView(priceTextView);
		
		LayoutParams llParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		toReturn.setLayoutParams(llParams);
		return toReturn;
	}
	
	/**
	 * Not currently used. 
	 * @param context
	 * @param item
	 * @return
	 */
	public static LinearLayout getRecommendationView(Context context, Item item){
		LinearLayout toReturn = new LinearLayout(context);
		toReturn.setOrientation(LinearLayout.HORIZONTAL);
		
			//1. Add image
			String imageFileName = "m" + item.getBarcode();
			ImageView image = new ImageView(context);
			int imageResourceId=0;
			Drawable drawable;
			try{
				imageResourceId = context.getResources().getIdentifier(imageFileName, "drawable", context.getPackageName());
				drawable = context.getResources().getDrawable(imageResourceId);
			}catch(Exception e){
				imageResourceId = context.getResources().getIdentifier(MyCartActivity.DEFAULT_IMAGE, "drawable", context.getPackageName());
				drawable = context.getResources().getDrawable(imageResourceId);
			}
			image.setImageDrawable(drawable);
			
			toReturn.addView(image, new LayoutParams(120, 120));
			
			//2. Add 2ItemList
			LinearLayout twoItemList = new LinearLayout(context);
			twoItemList.setOrientation(LinearLayout.VERTICAL);
			
				//2.1 Name
				TextView nameTextView = new TextView(context);
				nameTextView.setTextSize(SmartCartActivity.ITEMIZED_FONT_SIZE);
				nameTextView.setText(item.getName());
				nameTextView.setGravity(Gravity.CENTER_VERTICAL);
				twoItemList.addView(nameTextView);
			
				//2.2 Barcode/description
				TextView barcodeView = new TextView(context);
				barcodeView.setTextSize(SmartCartActivity.ITEMIZED_SUB_FONT_SIZE);
				barcodeView.setText(item.getBarcode());
				twoItemList.addView(barcodeView);
				
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
			params.setMargins(5, 3, 5, 2);
			twoItemList.setLayoutParams(params);
			toReturn.addView(twoItemList);
			
			//3. Add Price
			TextView priceTextView = new TextView(context);
			priceTextView.setTextSize(SmartCartActivity.ITEMIZED_FONT_SIZE);
			priceTextView.setText("$" + item.getSalePriceText());
			toReturn.addView(priceTextView);
		
		LayoutParams llParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		toReturn.setLayoutParams(llParams);
		return toReturn;
		
	}
}
