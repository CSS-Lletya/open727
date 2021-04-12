package com.rs.game.player.dialogues;

public class LunarAltar extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Change spellbooks?", "Yes, replace my spellbook.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.getCombatDefinitions().getSpellBook() != 430) {
				sendDialogue("Your mind clears and you switch", "back to the lunar spellbook.");
				player.getCombatDefinitions().setSpellBook(2);
			} else {
				sendDialogue("Your mind clears and you switch", "back to the normal spellbook.");
				player.getCombatDefinitions().setSpellBook(0);
			}
		} else
			end();
	}

	@Override
	public void finish() {

	}

}
