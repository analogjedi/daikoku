package com.primateer.daikoku.test;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.AmountException;
import com.primateer.daikoku.model.ShoppingList;
import com.primateer.daikoku.model.Unit;
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

		ShoppingList list = ShoppingList.getInstance();
		list.add(onionOil);
		list.add(onionOil);
		list.add(lentils);
		list.add(lentils, new Amount("300g"));
		list.setChecked(lentils, true);

		assertEquals(0, new Amount("0.8kg").compareTo(list.getAmount(lentils)));
		assertTrue(list.isChecked(lentils));
		for (Product product : onionOil.getIngredients().keySet()) {
			if (product.getAmount().unit.type == Unit.Type.VOLUME) {
				assertEquals(0,
						new Amount("20ml").compareTo(list.getAmount(product)));
			} else {
				assertEquals(0,
						new Amount("200g").compareTo(list.getAmount(product)));
			}
		}
	}
}
