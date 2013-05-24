package com.primateer.daikoku.test;

import java.util.Date;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Meal.State;
import com.primateer.daikoku.model.vos.Supply;
import com.primateer.daikoku.testutil.DatabaseTestCase;
import com.primateer.daikoku.testutil.TestDataFactory;

public class SupplyTest extends DatabaseTestCase {

	public void testLentilSupply() {
		DBController db = DBController.getInstance();
		long lentilsId = TestDataFactory.inputLentils();
		Product lentils = (Product) db.load(
				Product.class, lentilsId);
		
		Meal meal1 = new Meal();
		meal1.setDue(new Date());
		meal1.add(lentils,new Amount("100g"));
		meal1.setState(State.CONSUMED);
		
		Meal meal2 = new Meal();
		meal2.setDue(new Date());
		meal2.add(lentils,new Amount("200g"));
		meal2.setState(State.PREPARED);

		Meal meal3 = new Meal();
		meal3.setDue(new Date());
		meal3.add(lentils,new Amount("400g"));
		meal3.setState(State.RESERVED);

		Meal meal4 = new Meal();
		meal4.setDue(new Date());
		meal4.add(lentils,new Amount("800g"));
		meal4.setState(State.SCHEDULED);
		
		Supply lentilSupply = new Supply(lentils, new Amount("2kg"));
		
		long supplyId = db.register(lentilSupply);
		db.register(meal1);
		db.register(meal2);
		db.register(meal3);
		db.register(meal4);
		
		Supply supply = (Supply)db.load(Supply.class,supplyId);
		assertEquals(new Amount("2kg"), supply.getTotal());
		assertEquals(new Amount("300g"), supply.getConsumed());
		assertEquals(new Amount("400g"), supply.getReserved());
		assertEquals(new Amount("1.3kg"), supply.getAvailable());
	}
}
