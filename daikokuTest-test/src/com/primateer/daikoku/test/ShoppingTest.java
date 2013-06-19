package com.primateer.daikoku.test;

import java.util.Date;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.AmountException;
import com.primateer.daikoku.model.ShoppingList;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.testutil.DatabaseTestCase;
import com.primateer.daikoku.testutil.TestDataFactory;

public class ShoppingTest extends DatabaseTestCase {

	public void testSimpleShopping() throws AmountException {
		long onionId = TestDataFactory.inputOnionAndOil();
		long lentilsId = TestDataFactory.inputLentils();

		DBController db = DBController.getInstance();
		Recipe onionOil = (Recipe) db.load(Recipe.class, onionId);
		Product lentils = (Product) db.load(Product.class, lentilsId);

		ShoppingList list = new ShoppingList();
		list.add(onionOil);
		list.add(onionOil);
		list.add(lentils);
		list.add(lentils, new Amount("300g"));
		list.setChecked(lentils, true);

		long listId = db.register(list);
		list = (ShoppingList) db.load(ShoppingList.class, listId);

		for (Product product : list.getItems().keySet()) {
			if (product.getId() == lentils.getId()) {
				assertEquals(0,
						new Amount("0.8kg").compareTo(list.getAmount(product)));
				assertTrue(list.isChecked(product));
			} else {
				if (product.getAmount().unit.type == Unit.Type.VOLUME) {
					assertEquals(0, new Amount("20ml").compareTo(list
							.getAmount(product)));
				} else {
					assertEquals(0, new Amount("200g").compareTo(list
							.getAmount(product)));
				}
			}
		}
	}

	public void testShoppingMeal() {
		long onionId = TestDataFactory.inputOnionAndOil();
		long lentilsId = TestDataFactory.inputLentils();

		DBController db = DBController.getInstance();
		Product lentils = (Product) db.load(Product.class, lentilsId);
		Recipe onionOil = (Recipe) db.load(Recipe.class, onionId);
		Meal meal = new Meal();
		meal.add(onionOil);
		meal.add(lentils, new Amount("375g"));
		meal.add(lentils, new Amount("105g"));

		ShoppingList list = new ShoppingList();
		list.add(meal);

		for (Product product : list.getItems().keySet()) {
			if (product.getId() == lentils.getId()) {
				assertEquals(0,
						new Amount("0.48kg").compareTo(list.getAmount(product)));
			} else {
				if (product.getAmount().unit.type == Unit.Type.VOLUME) {
					assertEquals(0, new Amount("10ml").compareTo(list
							.getAmount(product)));
				} else {
					assertEquals(0, new Amount("100g").compareTo(list
							.getAmount(product)));
				}
			}
		}
	}

	public void testShoppingDaySpan() {
		long lentilsId = TestDataFactory.inputLentils();
		DBController db = DBController.getInstance();
		Product lentils = (Product) db.load(Product.class, lentilsId);

		TestDataFactory.inputLentils(Helper.addDays(new Date(), -2));
		TestDataFactory.inputLentils(Helper.addDays(new Date(), -1));
		TestDataFactory.inputLentils(Helper.addDays(new Date(), 1));
		TestDataFactory.inputLentils(Helper.addDays(new Date(), 2));
		TestDataFactory.inputLentils(Helper.addDays(new Date(), 3));

		ShoppingList list;

		list = new ShoppingList();
		list.add(new Date());
		for (Product product : list.getItems().keySet()) {
			if (product.getId() == lentils.getId()) {
				assertEquals(0, new Amount("0.375kg").compareTo(list
						.getAmount(product)));
			}
		}

		list = new ShoppingList();
		list.add(Helper.addDays(new Date(), -5), Helper.addDays(new Date(), 5));
		for (Product product : list.getItems().keySet()) {
			if (product.getId() == lentils.getId()) {
				assertEquals(0, new Amount("2.25kg").compareTo(list
						.getAmount(product)));
			}
		}

		list = new ShoppingList();
		list.add(Helper.addDays(new Date(), -1), Helper.addDays(new Date(), 1));
		for (Product product : list.getItems().keySet()) {
			if (product.getId() == lentils.getId()) {
				assertEquals(0, new Amount("1.125kg").compareTo(list
						.getAmount(product)));
			}
		}

	}
}
