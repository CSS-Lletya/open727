package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

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
		player(head_side2side_squinty_eyed, "bye...");
		//chatoption("Choose an Option:", "That's a cool item!", this::cool_item, "That item looks like shit...", this::shit_item, "Meh, it's an okay item...", this::meh_item);
		option("Choose an Option:", 
			"That's a cool item!", 
			() -> {
				player(happy, button_name());
				npc(normal, "Right, thanks for saying that");
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
				player(angry, button_name());
				npc(sad, "ya ik its shit");
				player(normal, "okay, bye");
			},
			
			"Meh, it's an okay item...", 
			() -> {
				player(drunk, button_name());
				npc(sad, "bye...");
			});
	}
	
	@SuppressWarnings("unused")
	private void give(int id){
		player.getInventory().addItem(id, 1);
		item(id, 1, "here you go!");
		
	}
	
	@SuppressWarnings("unused")
	private void cool_item(){
		player.getInventory().addItem(23659, 1);
		item(23659, 1, "here you go!");
	}
	
	@SuppressWarnings("unused")
	private void shit_item(){
		player.getInventory().addItem(5733, 1);
		item(5733, 1, "here you go!");
	}
	
	@SuppressWarnings("unused")
	private void meh_item(){
		player.getInventory().addItem(4153, 1);
		item(4153, 1, "here you go!");
	}
}