package com.rs.game.dialogue;

import java.util.HashMap;

import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class DialogueEventRepository {
	
	private static HashMap<String, DialogueEventListener> repository = initialize();
	
	public static final DialogueEventListener getListener(String key, Player player, Object... args){
		return repository.get(key);
	}

	private static HashMap<String, DialogueEventListener> initialize(){
		HashMap<String, DialogueEventListener> initialize = new HashMap<>();
		try {

			for (Class<?> c : Utils.getClasses("com.rs.game.dialogue.container")) {

				if (c.isAnonymousClass())
					continue;

				DialogueEventListener listener = (DialogueEventListener) c.newInstance();
				
				initialize.put(c.getName().toLowerCase().replace("_d", ""), listener);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return initialize;
	}

}
