package com.rs.tools;

import java.io.IOException;

import com.rs.cache.Cache;

public class Test {

	public static void main(String[] args) throws IOException {
		Cache.init();
		/*
		 * for(int i = 0; i < 24000; i++) { ItemDefinitions.getItemDefinitions(i); }
		 */
		/*
		 * NPCDefinitions def ; for(int i = 8132; i < 8134; i++) { def = NPCDefinitions.getNPCDefinitions(i); if(def.parameters != null) { Integer hpPart1 = (Integer) def.parameters.get(641); Integer hpPart2 = (Integer) def.parameters.get(2580); int hp = 0; if(hpPart1 != null) hp += hpPart1; if(hpPart2 != null) hp += hpPart2; System.out.println(i+": "+hp); } }
		 */
		/*
		 * System.out.println("kbd: "+def.parameters.toString()); def = NPCDefinitions.getNPCDefinitions(2745);
		 * 
		 * System.out.println("jad: "+def.parameters.toString()); def = NPCDefinitions.getNPCDefinitions(8133);
		 * 
		 * System.out.println("corp: "+def.parameters.toString());
		 */
	}
}
