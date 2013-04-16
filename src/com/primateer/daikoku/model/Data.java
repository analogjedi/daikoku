package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.db.Dao;

@SuppressWarnings("rawtypes")
public class Data {

	private static Data instance;

	private Map<Class, Map<Long, ValueObject>> registry =
			new HashMap<Class, Map<Long, ValueObject>>();

	public static Data getInstance() {
		if (instance == null) {
			instance = new Data();
		}
		return instance;
	}
	
	private Data() {}
	
	private Map<Long,ValueObject> getEntries(Class voClass) {
		if (!registry.containsKey(voClass)) {
			registry.put(voClass, new HashMap<Long,ValueObject>());
		}
		return registry.get(voClass);
	}
	
	private Dao getDao(Class voClass) {
		try {
			return (Dao)Class.forName("com.primateer.daikoku.db." +
					voClass.getSimpleName() + "Dao").newInstance();
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
		Map<Long, ValueObject> entries = registry.get(voClass);
		ValueObject vo = entries.get(id);
		
		if (vo == null) {
			vo = (ValueObject)getDao(voClass).load(id);
			register(vo);
		}
		return vo;
	}
	
	public long register(ValueObject vo) {
		if (vo == null) {
			return -1;
		}
		long id = vo.getId();
		if (id < 0) {
			id = getDao(vo.getClass()).insert(vo);
		}
		if (id >= 0) {
			getEntries(vo.getClass()).put(id,vo);
		}
		return id;
	}
}
