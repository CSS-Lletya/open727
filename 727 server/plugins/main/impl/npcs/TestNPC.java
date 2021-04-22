package main.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

import main.listener.NPCType;
import main.wrapper.NPCSignature;

@NPCSignature(name = {"Ozan"}, npcId = {})
public class TestNPC implements NPCType {

	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		System.out.println("HI");
		System.out.println(option);
	}
}