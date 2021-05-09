package main.impl.npcs;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.impl.BankerD;
import com.rs.game.dialogue.impl.TestD;
import com.rs.game.player.Player;

import main.listener.NPCType;
import main.wrapper.NPCSignature;
import npc.NPC;

@NPCSignature(name = {"Banker"}, npcId = {})
public class Banker implements NPCType {
	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		player.dialog(new BankerD(player, npc));
	}
}