package main.impl.npcs;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import main.listener.NPCType;
import main.wrapper.NPCSignature;
import npc.NPC;

@NPCSignature(name = {"Ozan"}, npcId = {})
public class TestNPC implements NPCType {


    @Override
    public void execute(Player player, NPC npc, int option) throws Exception {
        /*
         * For a full dialogue script use this.
         */
//        player.dialog(new TestD(player, npc));

        /*
         * For a quick access dialogue entry use this
         * (example)
         */
		player.dialog(new DialogueEventListener(player, npc) {
			@Override
			public void start() {
				player(happy, "Hello.");
				npc(scared,"Quickly - Tell me, is it still there?");
				player(question, "Is what still where?");
				npc(dispair, "The THING, the THING! It was just outside my house! Has it gone away yet? Or is it still lurking out there, waiting for me to go outside?");
				player(annoyed, "I didn't see any THING out there, just a couple of guards. What did it look like?");
				npc(shock_omg, "Ohhh, it was HORRIBLE! It was an enormous THING, with TEETH and EYES and... and... and THINGS!");
			}
		});
    }
}