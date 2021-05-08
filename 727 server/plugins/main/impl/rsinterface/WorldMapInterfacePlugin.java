package main.impl.rsinterface;

import com.rs.game.player.Player;
import com.rs.game.player.actions.Rest;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.Utils;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 548, 746, 750, 749, 755, 1214})
public class WorldMapInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if ((interfaceId == 548 && componentId == 35) || (interfaceId == 746 && componentId == 55)) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
				player.getSkills().switchXPDisplay();
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
				player.getSkills().switchXPPopup();
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
				player.getSkills().setupXPCounter();
		} else if ((interfaceId == 746 && componentId == 208) || (interfaceId == 548 && componentId == 167)) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				if (player.getInterfaceManager().containsScreenInter()) {
					player.getPackets()
							.sendGameMessage("Please finish what you're doing before opening the price checker.");
					return;
				}
				player.stopAll();
				player.getPriceCheckManager().openPriceCheck();
			}
			if (packetId == 96) {
				System.out.println("Toggle money pouch");
			}
			if (packetId == 27) {
				System.out.println("Withdraw money pouch");
			}
			if (packetId == 68) {
				System.out.println("Examine money pouch");
			}
		}
		if (interfaceId == 548 || interfaceId == 746) {
			if ((interfaceId == 548 && componentId == 157) || (interfaceId == 746 && componentId == 200 && packetId == 96)) {
				if (player.getInterfaceManager().containsScreenInter()
						|| player.getInterfaceManager().containsInventoryInter()) {
					// TODO cant open sound
					player.getPackets()
							.sendGameMessage("Please finish what you're doing before opening the world map.");
					return;
				}
				// world map open
				player.getPackets().sendWindowsPane(755, 0);
				int posHash = player.getX() << 14 | player.getY();
				player.getPackets().sendGlobalConfig(622, posHash); // map open
				// center
				// pos
				player.getPackets().sendGlobalConfig(674, posHash); // player
				// position
			} 
			if (packetId == 27) {
				System.out.println("Clear World map marker");
			}
			else if (componentId == 217 && interfaceId == 548){
				player.getPackets().sendOpenURL("https://support.runescape.com/hc/en-gb");
				player.getPackets()
				.sendGameMessage("support.");
			}
			
			else if ((interfaceId == 548 && componentId == 35) || (interfaceId == 746 && componentId == 54)) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getSkills().switchXPDisplay();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getSkills().switchXPPopup();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getSkills().setupXPCounter();
			} else if ((interfaceId == 746 && componentId == 167) || (interfaceId == 548 && componentId == 159)) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					if (player.getInterfaceManager().containsScreenInter()) {
						player.getPackets()
								.sendGameMessage("Please finish what you're doing before opening the price checker.");
						return;
					}
					player.stopAll();
					player.getPriceCheckManager().openPriceCheck();
				}
			}
		}
		if (interfaceId == 755) {
			if (componentId == 44)
				player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, 2);
			else if (componentId == 42) {
				player.getHintIconsManager().removeAll();// TODO find hintIcon index
				player.getPackets().sendConfig(1159, 1);
			}
		}
		if (interfaceId == 749) {
			if (componentId == 4) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) // activate
					player.getPrayer().switchQuickPrayers();
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) // switch
					player.getPrayer().switchSettingQuickPrayer();
			}
		} else if (interfaceId == 750) {
			if (componentId == 4) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.toogleRun(player.isResting() ? false : true);
					if (player.isResting())
						player.stopAll();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (player.isResting()) {
						player.stopAll();
						return;
					}
					long currentTime = Utils.currentTimeMillis();
					if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
						player.getPackets().sendGameMessage("You can't rest while perfoming an emote.");
						return;
					}
					if (player.getLockDelay() >= currentTime) {
						player.getPackets().sendGameMessage("You can't rest while perfoming an action.");
						return;
					}
					player.stopAll();
					player.getActionManager().setAction(new Rest());
				}
			}
		}
		if (interfaceId == 1214) {
			player.getSkills().handleSetupXPCounter(componentId);
		}
	}
}