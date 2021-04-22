package com.rs.game.player.controlers;

import java.util.HashMap;

import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class ControlerHandler {

	private static final HashMap<Object, Class<Controler>> handledControlers = new HashMap<Object, Class<Controler>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			
			Class<Controler>[] regular = Utils.getClasses("com.rs.game.player.controlers");
			for (Class<Controler> c : regular) {
				if (c.isAnonymousClass()) // next
					continue;
				handledControlers.put(c.getSimpleName(), c);
			}
			
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final Controler getControler(Object key) {
		if (key instanceof Controler)
			return (Controler) key;
		Class<Controler> classC = handledControlers.get(key);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}
}
