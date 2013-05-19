package com.primateer.daikoku.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.pm.PackageManager.NameNotFoundException;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.AmountException;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.GoalSet;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.testutil.DatabaseTestCase;

public class MealPlanTest extends DatabaseTestCase {

	private long onionAndOilId;
	private long lentilsId;

	private void inputGoals() {
		List<Goal> goals = new ArrayList<Goal>();
		goals.add(new Goal(Goal.Type.MINIMUM, Scope.PER_DAY,
				Nutrient.TYPE_ENERGY, new Amount(1700, Unit.UNIT_KILOCALORIE)));
		goals.add(new Goal(Goal.Type.MAXIMUM, Scope.PER_DAY,
				Nutrient.TYPE_ENERGY, new Amount(2000, Unit.UNIT_KILOCALORIE)));
		goals.add(new Goal(Goal.Type.MINIMUM, Scope.PER_DAY,
				Nutrient.TYPE_PROTEIN, new Amount(100, Unit.UNIT_GRAM)));
		goals.add(new Goal(Goal.Type.MAXIMUM, Scope.PER_DAY,
				Nutrient.TYPE_CARBS, new Amount(50, Unit.UNIT_GRAM)));
		goals.add(new Goal(Goal.Type.MINIMUM, Scope.PER_DAY, Nutrient.TYPE_FAT,
				new Amount(60, Unit.UNIT_GRAM)));
		goals.add(new Goal(Goal.Type.MAXIMUM, Scope.PER_DAY,
				Nutrient.TYPE_SATURATED_FAT, new Amount(10, Unit.UNIT_GRAM)));

		DBController db = DBController.getInstance();
		for (Goal goal : goals) {
			db.register(goal);
		}
	}

	private void inputOnionAndOil() {
		onionAndOilId = -1;

		Product onion = new Product();
		onion.setLabel("Zwiebeln");
		Nutrition onionNutrition = new Nutrition();
		onionNutrition.setNutrient(new Nutrient(Nutrient.TYPE_ENERGY,
				new Amount("28kcal")));
		onionNutrition.setNutrient(new Nutrient(Nutrient.TYPE_PROTEIN,
				new Amount("1.3g")));
		onionNutrition.setNutrient(new Nutrient(Nutrient.TYPE_CARBS,
				new Amount("4.9g")));
		onionNutrition.setNutrient(new Nutrient(Nutrient.TYPE_FAT, new Amount(
				"0.2g")));
		onion.setNutrition(onionNutrition);
		onion.setAmount(new Amount("100g"));
		onion.setUnits(1);

		Product oil = new Product();
		oil.setLabel("Olivenöl");
		Nutrition oilNutrition = new Nutrition();
		oilNutrition.setReferenceAmount(new Amount("100ml"));
		oilNutrition.setNutrient(new Nutrient(Nutrient.TYPE_ENERGY, new Amount(
				"824kcal")));
		oilNutrition.setNutrient(new Nutrient(Nutrient.TYPE_PROTEIN,
				new Amount("0.1g")));
		oilNutrition.setNutrient(new Nutrient(Nutrient.TYPE_CARBS, new Amount(
				"0.1g")));
		oilNutrition.setNutrient(new Nutrient(Nutrient.TYPE_FAT, new Amount(
				"91.6g")));
		oilNutrition.setNutrient(new Nutrient(Nutrient.TYPE_SATURATED_FAT,
				new Amount("14.1g")));
		oil.setNutrition(oilNutrition);
		oil.setAmount(new Amount("500ml"));
		oil.setUnits(50);

		// ENERGY 110.4 kcal
		// PROTEIN 1.31 g
		// CARBS 4.91 g
		// FAT 9.36 g
		// SATURATED FAT 1.41g

		Recipe onionAndOil = new Recipe();
		onionAndOil.setLabel("Zwiebeln & Öl");
		onionAndOil.add(onion, new Amount("1units"));
		onionAndOil.add(oil, new Amount("10ml"));

		onionAndOilId = DBController.getInstance().register(onionAndOil);
	}

	private void inputLentils() {
		lentilsId = -1;
		Date today = new Date();

		Nutrition lentilsNutrition = new Nutrition();
		lentilsNutrition.setNutrient(new Nutrient(Nutrient.TYPE_ENERGY,
				new Amount("336kcal")));
		lentilsNutrition.setNutrient(new Nutrient(Nutrient.TYPE_PROTEIN,
				new Amount("23g")));
		lentilsNutrition.setNutrient(new Nutrient(Nutrient.TYPE_CARBS,
				new Amount("50g")));
		lentilsNutrition.setNutrient(new Nutrient(Nutrient.TYPE_FAT,
				new Amount("1.6g")));

		Product lentils = new Product();
		lentils.setLabel("lentils");
		lentils.setNutrition(lentilsNutrition);
		lentils.setAmount(new Amount("500g"));
		lentils.setUnits(0);

		Recipe recipeLentils = new Recipe();
		recipeLentils.setLabel("bowl of lentils");
		recipeLentils.add(lentils, new Amount("125g"));

		Meal meal1 = new Meal();
		meal1.setLabel("1st meal");
		meal1.add(recipeLentils);
		meal1.setDue(today);
		meal1.setState(Meal.State.CONSUMED);

		Meal meal2 = new Meal();
		meal2.setLabel("2nd meal");
		meal2.add(recipeLentils);
		meal2.setDue(today);
		meal2.setState(Meal.State.CONSUMED);

		Meal meal3 = new Meal();
		meal3.setLabel("3rd meal");
		meal3.add(recipeLentils);
		meal3.setDue(today);
		meal3.setState(Meal.State.SCHEDULED);

		// TOTAL 375 g
		// ENERGY 1260 kcal
		// PROTEIN 86.25 g
		// CARBS 187.5 g
		// FAT 6.0 g

		DBController db = DBController.getInstance();
		lentilsId = db.register(lentils);
		db.register(meal1);
		db.register(meal2);
		db.register(meal2); // deliberate duplicate
		db.register(meal3);
	}

	public void testOnionOilLentils() throws AmountException {
		inputOnionAndOil();
		inputLentils();

		DBController db = DBController.getInstance();
		Product lentils = (Product) db.load(Product.class, lentilsId);
		Recipe onionOil = (Recipe) db.load(Recipe.class, onionAndOilId);
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
		inputLentils();
		inputGoals();

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
