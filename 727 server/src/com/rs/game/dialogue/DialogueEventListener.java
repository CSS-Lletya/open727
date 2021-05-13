package com.rs.game.dialogue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import npc.NPC;

public abstract class DialogueEventListener implements DialogueFaceExpression {
	
	private ArrayList<DialogueEvent> dialogueEvent = new ArrayList<DialogueEvent>();
	
	private int page, previousOptionPressed;
	
	protected transient Player player;

	private Object[] args;
	
	public DialogueEventListener mes(String message, Object...args){
		dialogueEvent.add(new DialogueEvent((byte) 0, String.format(message, args)));
		return this;
	}
	
	public DialogueEventListener player(int face, String message, Object...args){
		dialogueEvent.add(new DialogueEntityEvent(true, face, String.format(message, args)));
		return this;
	}
	
	public DialogueEventListener npc(int face, String message, Object... args){
		dialogueEvent.add(new DialogueEntityEvent(false, face, String.format(message, args)));
		return this;
	}
	
	public DialogueEventListener item(int itemId, String message, Object...args){
		return item(itemId, 1, message, args);
	}
	
	public DialogueEventListener item(int itemId, int amount, String message, Object...args){
		dialogueEvent.add(new DialogueItemEvent(itemId, amount, String.format(message, args)));
		return this;
	}

	public void option(String title, String option1, Runnable task){
		dialogueEvent.add(new DialogueOptionEvent(title, option1, task));
	}
	
	public void option(String title, String option1, Runnable task1, String option2, Runnable task2){
		dialogueEvent.add(new DialogueOptionEvent(title, option1, task1, option2, task2));
	}
	
	public void option(String title, String option1, Runnable task1, String option2, Runnable task2, String option3, Runnable task3){
		dialogueEvent.add(new DialogueOptionEvent(title, option1, task1, option2, task2, option3, task3));
	}
	
	public void option(String title, String option1, Runnable task1, String option2, Runnable task2, String option3, Runnable task3, String option4, Runnable task4){
		dialogueEvent.add(new DialogueOptionEvent(title, option1, task1, option2, task2, option3, task3, option4, task4));
	}
	
	public void option(String title, String option1, Runnable task1, String option2, Runnable task2, String option3, Runnable task3, String option4, Runnable task4, String option5, Runnable task5){
		dialogueEvent.add(new DialogueOptionEvent(title, option1, task1, option2, task2, option3, task3, option4, task4, option5, task5));
	}
	
	public void removeContinue(){
		dialogueEvent.get(dialogueEvent.size() - 1).removeContinueButton();
	}
	
	public DialogueEventListener(Player player, Object...args){
		this.player = player;
		this.args = args;
	}
	
	public abstract void start();
	
	public void onClose(){
		
	}
	
	public DialogueEventListener begin(){
		start();
		listenToDialogueEvent(0);
		return this;
	}
	
	public void complete(){
		player.getInterfaceManager().closeChatBoxInterface();
		onClose();
		player.getTemporaryAttributtes().remove("dialogue_event");
	}
	
	/**
	 * 
	 * @return the name of the option the player clicked
	 */
	public String button_name(){
		DialogueEvent previousDialogue = dialogueEvent.get(Math.max(0, page - 1));
		if (page > 0 && previousDialogue.getType() == 3){
			DialogueOptionEvent event = (DialogueOptionEvent) previousDialogue;
			return event.getNames()[previousOptionPressed - 1];
		}
		return "null";
	}
	
	public int ordinalButton(int button){
		return new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 3, 4, 5 }[button];
	}
	
	public void listenToDialogueEvent(int button){
		
		DialogueEvent previousDialogue = dialogueEvent.get(Math.max(0, page - 1));
		
		if (page > 0 && previousDialogue.getType() == 3){
			DialogueOptionEvent event = (DialogueOptionEvent) previousDialogue;
			previousOptionPressed = ordinalButton(button);
			Runnable task = event.getTasks()[previousOptionPressed - 1];
			if (task == null){
				complete();
				return;
			}
			task.run();
		}
		
		if (page >= dialogueEvent.size()){
			complete();
			return;
		}
		
		DialogueEvent dialogue = dialogueEvent.get(page);
		
		page++;
		
		switch (dialogue.getType()){
		case 0:
		{
			player.getInterfaceManager().sendChatBoxInterface(1186);
			player.getPackets().sendIComponentText(1186, 1, dialogue.getText());
			player.getPackets().sendHideIComponent(1186, 7, dialogue.isRemoveContinue());
		}
			break;
		case 1:
		{
			DialogueEntityEvent event = (DialogueEntityEvent) dialogue;
			
			NPC npc = args.length > 0 ? (NPC) args[0] : null;
			
			if (npc != null && Utils.getDistance(player, npc) < 3){
				player.faceEntity(npc);
				player.setNextFaceEntity(npc);
				npc.faceEntity(player);
				npc.setNextFaceEntity(player);
			}
			
			if (event.entityPlayer()){
				
				player.getInterfaceManager().sendChatBoxInterface(1191);
				player.getPackets().sendIComponentText(1191, 8, player.getDisplayName());
				player.getPackets().sendIComponentText(1191, 17, event.getText());
				player.getPackets().sendPlayerOnIComponent(1191, 15);
				player.getPackets().sendIComponentAnimation(event.getFace(), 1191, 15);
				player.getPackets().sendHideIComponent(1191, 18, event.isRemoveContinue());
				
			}
			else {
				
				player.getInterfaceManager().sendChatBoxInterface(1184);
				player.getPackets().sendIComponentText(1184, 17, header(npc));
				player.getPackets().sendIComponentText(1184, 13, event.getText());
				player.getPackets().sendNPCOnIComponent(1184, 11, npc.getId());
				player.getPackets().sendHideIComponent(1184, 18, event.isRemoveContinue());
				player.getPackets().sendIComponentAnimation(event.getFace(), 1184, 11);
				
			}
			
			
		}
			break;
		case 2:
		{
			 DialogueItemEvent event = (DialogueItemEvent) dialogue;
			 player.getInterfaceManager().sendChatBoxInterface(1189);
	         player.getPackets().sendItemOnIComponent(1189, 1, event.getItemId(), event.getAmount());
	         player.getPackets().sendIComponentText(1189, 4, event.getText());
	         player.getPackets().sendHideIComponent(1189, 10, event.isRemoveContinue());
		}
			break;
		case 3:
		{
			 DialogueOptionEvent event = (DialogueOptionEvent) dialogue;
			 player.getInterfaceManager().sendChatBoxInterface(1188);
			 player.getInterfaceManager().sendChatBoxInterface(1188);
		       
			 int index = 0;
			 String[] options = event.getOptionTextArray();
			 Object params[] = new Object[options.length + 1];
		     params[index++] = Integer.valueOf(options.length);
		     List<String> optionsList = Arrays.asList(options);
		     Collections.reverse(optionsList);
		     for(Iterator<String> iterator = optionsList.iterator(); iterator.hasNext();) 
		          params[index++] = (String)iterator.next();
		     
		     player.getPackets().sendIComponentText(1188, 20, event.getText());
		     player.getPackets().sendRunScript(5589, params);
		}
			break;
		}
	}
	
	private String header(NPC npc){
		NPCDefinitions defs = npc.getDefinitions();
		int npcId = defs.npcId;
		switch (npcId){
		case 8070:
			return "Gjalp";
		default:
			if (defs.name == null || defs.name.equals("null")){
				return "A strange voice";
			}
			return defs.name;
		}
	}
	
	public static boolean main(Player player, int i){
		System.out.println("dialogue compId: "+i);
		DialogueEventListener dialogue = (DialogueEventListener) player.getTemporaryAttributtes().get("dialogue_event");
		if (dialogue == null)
			return false;
		dialogue.listenToDialogueEvent(i);
		return true;
	}


}
