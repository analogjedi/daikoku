package com.primateer.daikoku.test;

import java.util.Date;

import android.content.pm.PackageManager.NameNotFoundException;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.AmountException;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.GoalSet;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.testutil.DatabaseTestCase;
import com.primateer.daikoku.testutil.TestDataFactory;

public class MealPlanTest extends DatabaseTestCase {

	public void testOnionOilLentils() throws AmountException {
		long onionId = TestDataFactory.inputOnionAndOil();
		long lentilsId = TestDataFactory.inputLentils();

		DBController db = DBController.getInstance();
		Product lentils = (Product) db.load(Product.class, lentilsId);
		Recipe onionOil = (Recipe) db.load(Recipe.class, onionId);
		Meal meal = new Meal();
		meal.add(onionOil);
		meal.add(lentils, new Amount("375g"));

		// ENERGY 110.4 kcal
		// PROTEIN 1.31 g
		// CARBS 4.91 g
		// FAT 9.36 g
		// SATURATED FAT 1.41g
		assertEquals(new Amount("1370.4kcal"),
				meal.getNutrition(Nutrient.TYPE_ENERGY));
		assertEquals(new Amount("87.56g"),
				meal.getNutrition(Nutrient.TYPE_PROTEIN));
		assertEquals(new Amount("192.41g"),
				meal.getNutrition(Nutrient.TYPE_CARBS));
		assertEquals(new Amount("15.36g"), meal.getNutrition(Nutrient.TYPE_FAT));
		assertEquals(new Amount("1.41g"),
				meal.getNutrition(Nutrient.TYPE_SATURATED_FAT));
	}

	public void testDay() throws UnitConversionException, NameNotFoundException {
		TestDataFactory.inputLentils();
		TestDataFactory.inputGoals();

		Date today = new Date();
		Day day = DBController.getInstance().loadAllMeals(today);
		try {
			assertEquals(new Amount("86.25g"),
					day.getNutrition(Nutrient.TYPE_PROTEIN));
		} catch (AmountException e) {
			e.printStackTrace();
		}

		GoalSet goals = DBController.getInstance().loadAllGoals(Scope.PER_DAY);
		assertEquals(Goal.Status.ACHIEVABLE,
				(goals.get(Nutrient.TYPE_ENERGY, Goal.Type.MINIMUM).match(day)));
		assertEquals(Goal.Status.MET,
				(goals.get(Nutrient.TYPE_ENERGY, Goal.Type.MAXIMUM).match(day)));
		assertEquals(
				Goal.Status.ACHIEVABLE,
				(goals.get(Nutrient.TYPE_PROTEIN, Goal.Type.MINIMUM).match(day)));
		assertEquals(Goal.Status.FAILED,
				(goals.get(Nutrient.TYPE_CARBS, Goal.Type.MAXIMUM).match(day)));
		assertEquals(Goal.Status.ACHIEVABLE,
				(goals.get(Nutrient.TYPE_FAT, Goal.Type.MINIMUM).match(day)));
		assertEquals(Goal.Status.MET, (goals.get(Nutrient.TYPE_SATURATED_FAT,
				Goal.Type.MAXIMUM).match(day)));
	}
}
