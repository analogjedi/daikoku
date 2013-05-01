package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primateer.daikoku.db.Dao;

@SuppressWarnings("rawtypes")
public class Data {

	private static Data instance;

	private Map<Class, Map<Long, ValueObject>> registry = new HashMap<Class, Map<Long, ValueObject>>();

	public static Data getInstance() {
		if (instance == null) {
			instance = new Data();
		}
		return instance;
	}

	private Data() {
	}

	private Map<Long, ValueObject> getEntries(Class voClass) {
		if (!registry.containsKey(voClass)) {
			registry.put(voClass, new HashMap<Long, ValueObject>());
		}
		return registry.get(voClass);
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

	public ValueObject get(Class voClass, long id) {
		Map<Long, ValueObject> entries = getEntries(voClass);
		ValueObject vo = entries.get(id);

		if (vo == null) {
			vo = (ValueObject) getDao(voClass).load(id);
			register(vo);
		}
		return vo;
	}

	public List<ValueObject> getAll(Class voClass) {
		List<ValueObject> vos = getDao(voClass).loadAll();
		Map<Long,ValueObject> entries = getEntries(voClass);
		for (ValueObject vo : vos) {
			entries.put(vo.getId(), vo);
		}
		return vos;
	}
	
	public long register(ValueObject vo, Class<? extends ValueObject> voClass) {
		if (vo == null) {
			return -1;
		}
		long id = vo.getId();
		if (id < 0) {
			id = getDao(voClass).insert(vo);
		}
		if (id >= 0) {
			getEntries(voClass).put(id, vo);
		}
		return id;		
	}

	public long register(ValueObject vo) {
		return register(vo,vo.getClass());
	}

	public boolean delete(ValueObject vo) {
		if (vo == null) {
			return false;
		}
		long id = vo.getId();
		if (id < 0 || !getEntries(vo.getClass()).containsKey(id)) {
			return false;
		}
		if (getDao(vo.getClass()).delete(vo) > 0) {
			getEntries(vo.getClass()).remove(id);
			return true;
		}
		return false;
	}
}
