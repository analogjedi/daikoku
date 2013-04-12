package com.primateer.daikoku.test;

import java.util.Date;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.test.AndroidTestCase;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Meal;
import com.primateer.daikoku.model.Model;
import com.primateer.daikoku.model.Nutrition;
import com.primateer.daikoku.model.Product;
import com.primateer.daikoku.model.Recipe;
import com.primateer.daikoku.pojos.Amount;
import com.primateer.daikoku.pojos.Amount.UnitConversionException;
import com.primateer.daikoku.pojos.Day;

public class MealPlanTest extends AndroidTestCase {

	public void testDay() throws UnitConversionException, NameNotFoundException {
		Date today = new Date();

		boolean DEBUGGABLE = (Application.getInstance().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		boolean TEST_ONLY = (Application.getInstance().getApplicationInfo().flags & ApplicationInfo.FLAG_TEST_ONLY) != 0;

		System.out.println("debuggable: " + DEBUGGABLE);
		System.out.println("test_only: " + TEST_ONLY);

		Nutrition lentilsNutrition = new Nutrition();
		lentilsNutrition.setNutrient("E", new Amount("336kcal"));
		lentilsNutrition.setNutrient("P", new Amount("23g"));
		lentilsNutrition.setNutrient("C", new Amount("50g"));
		lentilsNutrition.setNutrient("F", new Amount("1.6g"));

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
		meal1.setState(Meal.CONSUMED);

		Meal meal2 = new Meal();
		meal2.setLabel("2nd meal");
		meal2.setRecipe(recipeLentils);
		meal2.setDue(today);
		meal2.setState(Meal.CONSUMED);

		Meal meal3 = new Meal();
		meal3.setLabel("3rd meal");
		meal3.setRecipe(recipeLentils);
		meal3.setDue(today);
		meal3.setState(Meal.SCHEDULED);

		Model model = Model.getInstance();
		model.register(meal1);
		model.register(meal2);
		model.register(meal3);

		Day day = model.getDay(today);
		assertEquals(new Amount("69g"), day.getTotalNutrition("P"));
	}
}
