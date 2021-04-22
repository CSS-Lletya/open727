package com.rs.game.dialogue;

import java.util.HashMap;

import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class DialogueEventRepository {
	
	public static final Class<? extends DialogueEventListener> getListener(String key, Player player, Object... args){
		return handledDialogues.get(key);
	}

	private static final HashMap<Object, Class<? extends DialogueEventListener>> handledDialogues = new HashMap<Object, Class<? extends DialogueEventListener>>();
	
	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<DialogueEventListener>[] regular = Utils.getClasses("com.rs.game.dialogue.impl");
			for (Class<DialogueEventListener> c : regular) {
				if (c.isAnonymousClass()) // next
					continue;
				handledDialogues.put(c.getSimpleName(), c);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
