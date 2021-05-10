package main.impl.npcs;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import main.listener.NPCType;
import main.wrapper.NPCSignature;
import npc.NPC;

@NPCSignature(name = {"Man"}, npcId = {})
public class Man implements NPCType {

	
	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		player.dialog(new DialogueEventListener(player) {
			@Override
			public void start() {
				player(happy_plain, "Hello!");
				mes("He doesn't seem interested in talking.");
			}
		});
	}
}