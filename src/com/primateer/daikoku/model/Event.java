package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Event {

	public interface Listener {
		void onEvent(Event event);
	}

	public interface Dispatcher {
		void addListener(Class<? extends Event> type, Listener listener);

		void removeListener(Class<? extends Event> type, Listener listener);

		void dispatch(Event event);
	}

	public class SimpleDispatcher implements Dispatcher {

		private Map<Class<? extends Event>, List<Listener>> listeners = new HashMap<Class<? extends Event>, List<Listener>>();

		private List<Listener> getListeners(Class<? extends Event> type) {
			List<Listener> list = listeners.get(type);
			if (list == null) {
				list = new ArrayList<Listener>();
				listeners.put(type, list);
			}
			return list;
		}

		@Override
		public void addListener(Class<? extends Event> type, Listener listener) {
			getListeners(type).add(listener);
		}

		@Override
		public void removeListener(Class<? extends Event> type,
				Listener listener) {
			getListeners(type).remove(listener);
		}

		@Override
		public void dispatch(Event event) {
			for (Listener listener : listeners.get(event.getType())) {
				listener.onEvent(event);
			}
		}

	}

	public Class<? extends Event> getType() {
		return this.getClass();
	}
}
