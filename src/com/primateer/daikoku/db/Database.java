package com.primateer.daikoku.db;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.GoalSet;
import com.primateer.daikoku.model.Observable;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;
import com.primateer.daikoku.model.vos.Recipe;

@SuppressWarnings("rawtypes")
public class Database implements Observable<Class<ValueObject>> {

	private static Database instance;

	/** Notifies observers when the DB has changed. */
	private SimpleObservable<Class<ValueObject>> observable = new SimpleObservable<Class<ValueObject>>();

	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	private Database() {
	}

	private Dao getDao(Class voClass) {
		try {
			return (Dao) Class.forName(
					"com.primateer.daikoku.db." + voClass.getSimpleName()
							+ "Dao").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ValueObject load(Class voClass, long id) {
		return getDao(voClass).load(id);
	}

	public List<ValueObject> loadAll(Class voClass) {
		return getDao(voClass).loadAll();
	}

	public long register(ValueObject vo, Class<? extends ValueObject> voClass) {
		if (vo == null) {
			return -1;
		}
		long id = vo.getId();
		if (id < 0) {
			id = getDao(voClass).insert(vo);
		} else {
			getDao(voClass).update(vo);
		}
		observable.notifyObservers((Class<ValueObject>) voClass);
		return id;
	}

	public long register(ValueObject vo) {
		return register(vo, vo.getClass());
	}

	public boolean delete(ValueObject vo) {
		if (vo == null) {
			return false;
		}
		long id = vo.getId();
		if (id < 0) {// || !getEntries(vo.getClass()).containsKey(id)) {
			return false;
		}
		if (getDao(vo.getClass()).delete(vo) > 0) {
			observable.notifyObservers((Class<ValueObject>) vo.getClass());
			return true;
		}
		return false;
	}

	@Override
	public void addObserver(Observer<Class<ValueObject>> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeObserver(Observer<Class<ValueObject>> observer) {
		observable.removeObserver(observer);
	}

	public Collection<Goal> loadAllGoals(Scope scope) {
		return (new GoalDao()).loadAll(scope);
	}

	public Collection<Recipe> loadFavoriteRecipes() {
		return (new RecipeDao()).loadFavorites();
	}

	public Day loadAllMeals(Date date) {
		return (new DayDao()).load(date);
	}

	public void registerGoalSet(GoalSet data) {
		if (data == null) {
			return;
		}
		new GoalDao().deleteAll();
		for (Goal goal : data) {
			register(goal);
		}
	}
}
