package main.impl.rsinterface;

import java.util.List;

import com.rs.Settings;
import com.rs.cache.io.InputStream;
import com.rs.cores.WorldThread;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.dialogue.impl.DestroyItemD;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.content.Foods;
import com.rs.game.player.content.Pots;
import com.rs.game.route.CoordsEvent;
import com.rs.game.task.Task;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

import main.InventoryDispatcher;
import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;
import npc.NPC;
import npc.familiar.Familiar.SpecialAttack;
import npc.pet.Pet;

@RSInterfaceSignature(interfaceId = {679})
public class InventoryInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if (componentId == 0) {
			if (slotId > 27 || player.getInterfaceManager().containsInventoryInter())
				return;
			Item item = player.getInventory().getItem(slotId);
			if (item == null || item.getId() != slotId2)
				return;
			
			switch(packetId) {
			case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
				long time = Utils.currentTimeMillis();
				if (player.getLockDelay() >= time || player.getEmotesManager().getNextEmoteEnd() >= time)
					return;
				player.stopAll(false);
				if (Foods.eat(player, item, slotId))
					return;
				if (Pots.pot(player, item, slotId))
					return;
				InventoryDispatcher.execute(player, item, 1);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
				if (player.isEquipDisabled())
					return;
				long passedTime = Utils.currentTimeMillis() - WorldThread.LAST_CYCLE_CTM;
				World.get().submit(new Task(passedTime >= 600 ? 0 : passedTime > 330 ? 1 : 0) {
					
					@Override
					protected void execute() {
						List<Byte> slots = player.getSwitchItemCache();
						int[] slot = new int[slots.size()];
						for (int i = 0; i < slot.length; i++)
							slot[i] = slots.get(i);
						player.getSwitchItemCache().clear();
						InventoryInterfaceTypePlugin.sendWear(player, slot);
						player.stopAll(false, true, false);
						this.cancel();
					}
				});
				if (player.getSwitchItemCache().contains(slotId))
					return;
				player.getSwitchItemCache().add(slotId);
				InventoryDispatcher.execute(player, item, 2);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
				InventoryDispatcher.execute(player, item, 3);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
				InventoryDispatcher.execute(player, item, 4);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON5_PACKET:
				InventoryDispatcher.execute(player, item, 5);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON6_PACKET:
				InventoryDispatcher.execute(player, item, 6);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON8_PACKET:
				long dropTime = Utils.currentTimeMillis();
				if (player.getLockDelay() >= dropTime || player.getEmotesManager().getNextEmoteEnd() >= dropTime)
					return;
				if (!player.getControlerManager().canDropItem(item))
					return;
				player.stopAll(false);
				
				if (item.getDefinitions().isOverSized()) {
					player.getPackets().sendGameMessage("The item appears to be oversized.");
					player.getInventory().deleteItem(item);
					return;
				}

				if(player.getToolbelt().getToolbeltItems().contains(item.getId())) {
					player.getToolbelt().addItem(slotId, item);
					return;
				}
				if (item.getDefinitions().isDestroyItem()) {
					DestroyItemD.INSTANCE.sendChatInterface(player, item);
					return;
				}
				if (player.getPetManager().spawnPet(item.getId(), true)) {
					return;
				}
				InventoryDispatcher.execute(player, item, 7);
				player.getInventory().deleteItem(slotId, item);
				if (player.getCharges().degradeCompletly(item))
					return;
				FloorItem.createGroundItem(item, new WorldTile(player), player, false, 180, true);
				player.getPackets().sendSound(2739, 0, 1);
				break;
			case 81:
				player.getInventory().sendExamine(slotId);
				InventoryDispatcher.execute(player, item, 8);
				break;
			}
		}
	}
	
	public static void handleItemOnItem(final Player player, InputStream stream) {
		int toSlot = stream.readShortLE128();
		int fromSlot = stream.readShortLE();
		int itemUsedWithId = stream.readShortLE128();
		int interfaceId2 = stream.readIntLE() >> 16;
		int interfaceId = stream.readIntV2() >> 16;
		int itemUsedId = stream.readShortLE();
		
		if (Settings.DEBUG)
			System.out.println(String.format("fromInter: %s, toInter: %s, fromSlot: %s, toSlot %s, item1: %s, item2: %s", interfaceId, interfaceId2, fromSlot, toSlot, itemUsedId, itemUsedWithId));
		
		//fromInter: 44498944, toInter: 44498944, fromSlot: 11694, toSlot 0, item1: 14484, item2: 8
		
		if ((interfaceId2 == 747 || interfaceId2 == 662) && interfaceId == Inventory.INVENTORY_INTERFACE) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(toSlot);
				}
			}
			return;
		}
		if (interfaceId == Inventory.INVENTORY_INTERFACE && interfaceId == interfaceId2
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28)
				return;
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null || itemUsed.getId() != itemUsedId
					|| usedWith.getId() != itemUsedWithId)
				return;
			player.stopAll();
			
			if (Settings.DEBUG)
				Logger.log("ItemHandler", "Used:" + itemUsed.getId() + ", With:" + usedWith.getId());
		}
	}

	public static void handleItemOnNPC(final Player player, final NPC npc, final Item item) {
		if (item == null) {
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
					return;
				}
				if (npc instanceof Pet) {
					player.faceEntity(npc);
					player.getPetManager().eat(item.getId(), (Pet) npc);
					return;
				}
			}
		}, npc.getSize()));
	}
}