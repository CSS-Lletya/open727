package main.impl.npcs;

import com.rs.game.dialogue.impl.TestD;
import com.rs.game.player.Player;

import main.listener.NPCType;
import main.wrapper.NPCSignature;
import npc.NPC;

@NPCSignature(name = {"Man"}, npcId = {})
public class TestNPC implements NPCType {

	
	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		/*
		 * For a full dialogue script use this.
		 */
		player.dialog(new TestD(player, npc));
		
		/*
		 * For a quick access dialogue entry use this
		 * (example)
		 */
//		player.dialog(new DialogueEventListener(player) {
//			@Override
//			public void start() {
//				mes("lol");
//			}
//		}.begin());
	}
}