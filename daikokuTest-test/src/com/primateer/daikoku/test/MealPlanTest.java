package com.primateer.daikoku.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.pm.PackageManager.NameNotFoundException;

import com.primateer.daikoku.db.Database;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.testutil.DatabaseTestCase;

public class MealPlanTest extends DatabaseTestCase {

	public void testDay() throws UnitConversionException, NameNotFoundException {
		List<Goal> goals = new ArrayList<Goal>();
		goals.add(new Goal(Goal.Type.MINIMUM, Scope.PER_DAY,
				Nutrient.TYPE_ENERGY, new Amount(1500, Unit.UNIT_KILOCALORIE)));
		goals.add(new Goal(Goal.Type.MAXIMUM, Scope.PER_DAY,
				Nutrient.TYPE_ENERGY, new Amount(2000, Unit.UNIT_KILOCALORIE)));
		goals.add(new Goal(Goal.Type.MINIMUM, Scope.PER_DAY,
				Nutrient.TYPE_PROTEIN, new Amount(30, Unit.UNIT_GRAM)));

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

		Database model = Database.getInstance();
		model.register(meal1);
		model.register(meal2);
		model.register(meal2); // deliberate duplicate
		model.register(meal3);

		for (Goal goal : goals) {
			model.register(goal);
		}

		Day day = Database.getInstance().loadAllMeals(today);
		assertEquals(new Amount("86.25g"),
				day.getNutrition(Nutrient.TYPE_PROTEIN));

		List<ValueObject> goalVos = Database.getInstance().loadAll(Goal.class);
		assertEquals(Goal.Status.ACHIEVABLE, ((Goal) goalVos.get(0)).match(day));
		assertEquals(Goal.Status.MET, ((Goal) goalVos.get(1)).match(day));
		assertEquals(Goal.Status.MET, ((Goal) goalVos.get(2)).match(day));
	}
}
