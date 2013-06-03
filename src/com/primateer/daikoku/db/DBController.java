package com.primateer.daikoku.db;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Event.Listener;
import com.primateer.daikoku.model.GoalSet;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;
import com.primateer.daikoku.model.vos.Recipe;

@SuppressWarnings("rawtypes")
public class DBController implements Event.Registry {
	
	public static class DBChangedEvent extends Event {
		public final Class<? extends ValueObject> type;
		public DBChangedEvent(Class<? extends ValueObject> type) {
			this.type = type;
		}
	}

	private static DBController instance;
	
	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

	public static DBController getInstance() {
		if (instance == null) {
			instance = new DBController();
		}
		return instance;
	}

	private DBController() {
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

	@SuppressWarnings("unchecked")
	public List<ValueObject> loadAll(Class voClass) {
		return getDao(voClass).loadAll();
	}

	@SuppressWarnings("unchecked")
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
		dispatcher.dispatch(new DBChangedEvent(voClass));
		return id;
	}

	public long register(ValueObject vo) {
		return register(vo, vo.getClass());
	}

	@SuppressWarnings("unchecked")
	public boolean delete(ValueObject vo) {
		if (vo == null) {
			return false;
		}
		long id = vo.getId();
		if (id < 0) {// || !getEntries(vo.getClass()).containsKey(id)) {
			return false;
		}
		if (getDao(vo.getClass()).delete(vo) > 0) {
			dispatcher.dispatch(new DBChangedEvent(vo.getClass()));
			return true;
		}
		return false;
	}

	public GoalSet loadAllGoals(Scope scope) {
		GoalSet goals = new GoalSet();
		Collection<Goal> data = (new GoalDao()).loadAll(scope);
		for (Goal goal : data) {
			goals.add(goal);
		}
		return goals;
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

	@Override
	public void addEventListener(Class<? extends Event> type, Listener listener) {
		dispatcher.addEventListener(type, listener);
	}

	@Override
	public void removeEventListener(Class<? extends Event> type, Listener listener) {
		dispatcher.removeEventListener(type, listener);
	}
}
