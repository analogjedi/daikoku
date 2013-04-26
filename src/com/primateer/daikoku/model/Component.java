package com.primateer.daikoku.model;


public class Component {

	public final Ingredient ingredient;
	public final Amount amount;
	
	public Component(Ingredient ingredient, Amount amount) {
		this.ingredient = ingredient;
		this.amount = amount;
	}
	
	public Component(Ingredient item) {
		this(item,item.getDefaultAmount());
	}
}
