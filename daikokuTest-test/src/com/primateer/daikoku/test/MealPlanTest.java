package com.primateer.daikoku.test;

import java.util.Date;

import android.content.pm.PackageManager.NameNotFoundException;

import com.primateer.daikoku.db.DayDao;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.testutil.DatabaseTestCase;

public class MealPlanTest extends DatabaseTestCase {

	public void testDay() throws UnitConversionException, NameNotFoundException {
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
		meal1.setRecipe(recipeLentils);
		meal1.setDue(today);
		meal1.setState(Meal.STATE_CONSUMED);

		Meal meal2 = new Meal();
		meal2.setLabel("2nd meal");
		meal2.setRecipe(recipeLentils);
		meal2.setDue(today);
		meal2.setState(Meal.STATE_CONSUMED);

		Meal meal3 = new Meal();
		meal3.setLabel("3rd meal");
		meal3.setRecipe(recipeLentils);
		meal3.setDue(today);
		meal3.setState(Meal.STATE_SCHEDULED);

		Data model = Data.getInstance();
		model.register(meal1);
		model.register(meal2);
		model.register(meal2); // deliberate duplicate
		model.register(meal3);

		Day day = new DayDao().load(today);
		assertEquals(new Amount("86.25g"),
				day.getTotalNutrition(Nutrient.TYPE_PROTEIN));
	}
}
