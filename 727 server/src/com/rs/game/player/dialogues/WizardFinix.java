package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.npc.NPC;

public class WizardFinix extends Dialogue {

	private NPC npc;

	@Override
	public void start() {
		stage = Byte.parseByte(parameters[0].toString());
		npc = (NPC) parameters[1];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name, "Hello. How can I help you?", },
				IS_NPC, npc.getId(), 9843);
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue("Select an Option", "Hello. Who are you?", "What can I do here?",
					"Can I have some runes?", "Can you help me find a wizard in yellow ropes?",
					"Can you teleport me back to the Wizards' Tower?");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case 11:
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
								"I'm " + NPCDefinitions.getNPCDefinitions(npc.getId()).name
										+ ". I am a student of runecrafting.", },
						IS_NPC, npc.getId(), 9843);
				stage = 3;
				break;
			}
			break;
		case 3:
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Ah, pleased to meet you, " + NPCDefinitions.getNPCDefinitions(npc.getId()).name + ". I'm "
									+ player.getDisplayName() + ".",
							"What are you doing here?" },
					IS_PLAYER, -1, 9827);
			stage = 4;
			break;
		case 4:
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
							"I'm creating runes by siphoning energy out of the", "Runespan." },
					IS_NPC, npc.getId(), 9843);
			stage = 5;
			break;
		case 5:
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Creating runes by siphoning?" }, IS_PLAYER, -1, 9812);
			stage = 6;
			break;
		case 6:
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
							"Yes, it's a new way to make runes. I discovered it,", "actually!" },
					IS_NPC, npc.getId(), 9843);
			stage = 7;
			break;
		case 7:
			sendOptionsDialogue("Select an Option", "Why don't you just use altars?",
					"How do you make runes by siphoning?");
			stage = 8;
			break;
		case 8:
			switch (componentId) {
			case 11:
				sendEntityDialogue(SEND_3_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
								"That is another one of my discoveries! Runecrafting",
								"altars will not last forever. One day, the altars will run", "out!" },
						IS_NPC, npc.getId(), 9843);
				stage = 9;
				break;
			case 13:
				sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
						"Well, firstly, I have to warn you that any runes you make",
						"are taken by the Runecrafting Guild when you leave.",
						"They discovered the portal to this plane,", "and have laid claim to it as their own." },
						IS_NPC, npc.getId(), 9843);
				stage = 23;
				break;
			}
			break;
		case 9:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Run out?" }, IS_PLAYER, -1,
					9824);
			stage = 10;
			break;
		case 10:
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
							"Yes - of course, it is still only a theory. The Runecrafting",
							"Guild wizards are still sceptical, but they cannot prove me", "wrong." },
					IS_NPC, npc.getId(), 9772);
			stage = 11;
			break;
		case 11:
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { player.getDisplayName(),
					"How will humans perform magic if they cannot make", "runes?" }, IS_PLAYER, -1, 9824);
			stage = 12;
			break;
		case 12:
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
					"Exactly. That is why learning to siphon energy from the", "world around us is important." },
					IS_NPC, npc.getId(), 9772);
			stage = 13;
			break;
		case 13:
			sendEntityDialogue(
					SEND_2_TEXT_CHAT, new String[] { player.getDisplayName(),
							"You mean I could create runes by pulling energy from", "Gielinor itself?" },
					IS_PLAYER, -1, 9824);
			stage = 14;
			break;
		case 14:
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
							"Theorectially, yes, but there is no runecrafter skilled enough",
							"to do this yet. In theory, runecrafting and magic", "should be a self-supporting cycle." },
					IS_NPC, npc.getId(), 9832);
			stage = 15;
			break;
		case 15:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
					"So how can I learn to siphon energy if it is so difficult?" }, IS_PLAYER, -1, 9764);
			stage = 16;
			break;
		case 16:
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
							"That is why we are in this area. In the Runespan, energy is",
							"still in a very raw state and not tightly bound to",
							"matter, as it is in Gielinor. The wizards of the",
							"Runecrafting Guild have been using their tower to study" },
					IS_NPC, npc.getId(), 9843);
			stage = 17;
			break;
		case 17:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
					"runic energy in the Runespan for years." }, IS_NPC, npc.getId(), 9843);
			stage = 18;
			break;
		case 18:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "So that means..." },
					IS_PLAYER, -1, 9824);
			stage = 19;
			break;
		case 19:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
					"... we are outside the Runecrafting Guild - yes!" }, IS_NPC, npc.getId(), 9843);
			stage = 20;
			break;
		case 20:
			sendOptionsDialogue("Select an Option", "So, how do you make runes by siphoning?",
					"Can you teleport me back to the Wizards' Tower?");
			stage = 21;
			break;
		case 21:
			switch (componentId) {
			case 11:
				sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
						"Well, firstly, I have to warn you that any runes you make",
						"are taken by the Runecrafting Guild when you leave.",
						"They discovered the portal to this plane,", "and have laid claim to it as their own." },
						IS_NPC, npc.getId(), 9843);
				stage = 23;
				break;
			case 13:
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name, "Sure" }, IS_NPC,
						npc.getId(), 9764);
				stage = 22;
				break;
			}
			break;
		case 22:
			player.getDialogueManager().startDialogue("RuneSpanLeaving");
			break;
		case 23:
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npc.getId()).name,
							"Don't worry too much about that, though. Any runes you",
							"do make will award you points to send at my shop in the",
							"Wizards' Tower. You will learn to become a better", "runecrafter faster here, too." },
					IS_NPC, npc.getId(), 9843);
			stage = 24;
			break;
		case 24:
			sendOptionsDialogue("Select an Option", "How do I get rune essence?", "How do I make runes?",
					"How do I get around?", "Is there anthig else to do here?", "Why don't you just use the altars?");
			stage = 25;
			break;
		}
	}

	@Override
	public void finish() {

	}

}
