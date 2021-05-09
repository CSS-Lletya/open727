package main.impl.npcs;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.impl.TestD;
import com.rs.game.player.Player;
import main.listener.NPCType;
import main.wrapper.NPCSignature;
import npc.NPC;

@NPCSignature(name = {"Ozan"}, npcId = {})
public class Man implements NPCType {

	
	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		/*
		 * For a full dialogue script use this.
		 */
//		player.dialog(new TestD(player, npc));
		
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
						/*Player: Um... would you care to be more specific?
						Man: I can't. I only saw it in the dark.
				Player: You only saw this THING in the dark?
				Man: I was sleeping peacefully one night, when suddenly I woke up and saw it through the window. It was LOOKING at me! I haven't dared go out since then. It's had me trapped in here for days! I've packed my
				Man: bags so I can escape, but the THING's still out there waiting for me to come out! If I have to stay in here much longer I'll go mad! MAD!! MAD!!
						Player: There's no THING outside. Just come out and get some fresh air before you go funny in the head.
				Man: You want me to go outside?
				Player: I think you might just have dreamed about the THING.
				Man: You want me to believe that it's not real?
				Player: Please come outside!
						Man: No! No! I know what you are! You're in league with the THING! It keeps sending people in here to trick me into going outside! They keep stealing from me too! GO AWAY!
				Player: I'm not trying to trick you!
				Man: Get thee gone, trickster!
						Player: Sheesh...*/
			}
		}.begin());
	}
}