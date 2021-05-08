package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import npc.NPC;

public class TestD extends DialogueEventListener {

	public TestD(Player player, NPC npc){
		super(player, npc);
	}
	
	@Override
	public void start() {
		player(happy, "Hi there");
		item(11694, "Hi, I'm an item");
		npc(head_side2side_happy, "lol...");
		player(happy, "wow ur a retard");
		mes("yo sup i'm a regular message");
		player(suspicious, "bye...");
		//chatoption("Choose an Option:", "That's a cool item!", this::cool_item, "That item looks like shit...", this::shit_item, "Meh, it's an okay item...", this::meh_item);
		option("Choose an Option:", 
			"That's a cool item!", 
			() -> {
				player(happy, button_name());
				npc(plain_talking, "Right, thanks for saying that");
				option("So take cool item?", 
						"yes", 
						() -> {
							player.getInventory().addItem(23659, 1);
							item(23659, "here you go!");
						},
						
						"no", null);
			},
			
			"That item looks like shit...", 
			() -> {
				player(angry_2, button_name());
				npc(sad, "ya ik its shit");
				player(plain_talking, "okay, bye");
			},
			
			"Meh, it's an okay item...", 
			() -> {
				player(drunk_happy_tired, button_name());
				npc(sad, "bye...");
			});
	}
}