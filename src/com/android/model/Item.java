package com.android.model;

/**
 * An item has Name, Description, price, weight, location
 * @author kerengu
 *
 */ 
public class Item {

	private String name;
	private String description;
	private double salePrice;
	private double originalPrice;
	private int location;
	private String barcode;
	
	//Other Attributes no in-use so far. 
	private double weight;
	private int count;
	
	public Item(String n, String d, double sp, double op, int l, String b){
		this.name = n;
		this.description = d;
		this.salePrice = sp;
		this.originalPrice = op;
		this.location = l;
		this.barcode = b;
	}
	
	/**
	 * Generate Getters and setters for all attribute of an item;
	 * @return
	 */
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getSalePriceText() {
		return String.format("%.2f", this.salePrice);
	}

	public double getSalePrice() {
		return this.salePrice;
	}
	
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getOriginalPriceText() {
		return String.format("%.2f", this.originalPrice);
	}

	public double getOriginalPrice(){
		return this.originalPrice;
	}
	
	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public int getLocation() {
		return location;
	}


	public void setLocation(int location) {
		this.location = location;
	}


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String itemize(){
		return name + "\t" + barcode + "\t" + salePrice;
	}
	@Override
	public String toString() {
		return "Item [name=" + name + ", description=" + description
				+ ", salePrice=" + salePrice + ", originalPrice="
				+ originalPrice + ", location=" + location + ", barcode="
				+ barcode  + ", weight="
				+ weight + ", count=" + count + "]";
	}

	
}
