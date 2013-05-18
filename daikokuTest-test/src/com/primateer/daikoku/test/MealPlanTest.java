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

	private void inputLentils() {
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
		db.register(meal1);
		db.register(meal2);
		db.register(meal2); // deliberate duplicate
		db.register(meal3);
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
		assertEquals(Goal.Status.UNRATED, (goals.get(
				Nutrient.TYPE_SATURATED_FAT, Goal.Type.MAXIMUM).match(day)));
	}
}
