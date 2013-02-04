package com.android.model;

import java.util.ArrayList;

public class SmartCartModel {

	private ArrayList<Item> items;
	public final String PRICE_HEADER_TAG = "TOTAL DUE: \t\t$";
	private ArrayList<Item> recommendations;
	public final static String TAX_TEXT = "MASS TAX 6%";
	private static final double TAX_RATE = 0.06;
	
	public SmartCartModel(){
		this.items = new ArrayList<Item>();
		this.recommendations = new ArrayList<Item>();
	}
	
	/**
	 * Add An item to the recommendation. 
	 * @param item
	 */
	public void addRecommendation(Item item){
		if(!this.recommendations.contains(item)){
			this.recommendations.add(item);
		}
	}
	
	/**
	 * Add a new item onto the cart
	 * @param i
	 */
	public void addItem(Item i){
		items.add(i);
	}
	
	/**
	 * Header
	 * @return
	 */
	public String getPriceHeaderDisplay() {
		
		if(this.items == null || this.items.size() == 0){
			return PRICE_HEADER_TAG + "0.00";
		}
		
		return PRICE_HEADER_TAG + getTotalPrice();
	}
	
	/**
	 * Returns the sum of the prices in Items
	 * @return
	 */
	public String getTotalPrice(){
		double total = 0;
		for(Item i: items){
			total += i.getSalePrice();
		}
		total += this.getTax();
		return String.format("%.2f", total);
	}

	public String getItemizedList() {
		String toReturn = "";
		for (Item i: this.items){
			toReturn += i.itemize() + "\n";
		}
		return toReturn;
	}
	
	/**
	 * Getters and Setters for Stuff
	 * @return
	 */
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public ArrayList<Item> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(ArrayList<Item> recommendations) {
		this.recommendations = recommendations;
	}
	
	public double getTax(){
		double total = 0;
		for(Item i: items){
			total += i.getSalePrice();
		}
		return total*TAX_RATE;
	}
	
	public String getTaxText(){
		
		return String.format("%.2f", getTax());
	}
	
	/**
	 * Delete the item with the input barcode. 
	 * @param barcode
	 */
	public void deleteItem(String barcode){
		Item toDelete = null;
		for(Item i: items){
			if(barcode.equals(i.getBarcode())){
				toDelete = i;
			}
		}
		items.remove(toDelete);
	}

	/**
	 * Delete the item with the input barcode. 
	 * @param barcode
	 */
	public void deleteRecommendation(String barcode){
		Item toDelete = null;
		for(Item i: recommendations){
			if(barcode.equals(i.getBarcode())){
				toDelete = i;
			}
		}
		recommendations.remove(toDelete);
	}

}
