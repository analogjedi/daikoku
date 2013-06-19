package com.primateer.daikoku.testutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.model.vos.Goal.Scope;

public class TestDataFactory {
	public static void inputGoals() {
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
	
	public static long inputOnionAndOil() {
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

		return DBController.getInstance().register(onionAndOil);
	}
	
	public static long inputLentils() {
		return inputLentils(new Date());
	}
	
	public static long inputLentils(Date day) {
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
		meal1.setDue(day);
		meal1.setState(Meal.State.CONSUMED);

		Meal meal2 = new Meal();
		meal2.setLabel("2nd meal");
		meal2.add(recipeLentils);
		meal2.setDue(day);
		meal2.setState(Meal.State.CONSUMED);

		Meal meal3 = new Meal();
		meal3.setLabel("3rd meal");
		meal3.add(recipeLentils);
		meal3.setDue(day);
		meal3.setState(Meal.State.SCHEDULED);

		// TOTAL 375 g
		// ENERGY 1260 kcal
		// PROTEIN 86.25 g
		// CARBS 187.5 g
		// FAT 6.0 g

		DBController db = DBController.getInstance();
		long lentilsId = db.register(lentils);
		db.register(meal1);
		db.register(meal2);
		db.register(meal2); // deliberate duplicate
		db.register(meal3);
		
		return lentilsId;
	}
}
