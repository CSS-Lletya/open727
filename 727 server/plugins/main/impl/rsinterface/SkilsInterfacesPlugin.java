package main.impl.rsinterface;

import com.rs.game.player.Player;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;
import skills.Skills;

@RSInterfaceSignature(interfaceId = { 320, 449, 1218 })
public class SkilsInterfacesPlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		switch(interfaceId) {
		case 1218:
			if ((componentId >= 33 && componentId <= 55) || componentId == 120 || componentId == 151
			|| componentId == 189)
				player.getPackets().sendInterface(false, 1218, 1, 1217); // seems to fix
			break;
		case 320:
			player.stopAll();
			@SuppressWarnings("unused")
			int lvlupSkill = -1;
			int skillMenu = -1;
			switch (componentId) {
			case 150: // Attack
				skillMenu = 1;
				if (player.getTemporaryAttributtes().remove("leveledUp[0]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 1);
				} else {
					lvlupSkill = 0;
					player.getPackets().sendConfig(1230, 10);
				}
				break;
			case 9: // Strength
				skillMenu = 2;
				if (player.getTemporaryAttributtes().remove("leveledUp[2]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 2);
				} else {
					lvlupSkill = 2;
					player.getPackets().sendConfig(1230, 20);
				}
				break;
			case 22: // Defence
				skillMenu = 5;
				if (player.getTemporaryAttributtes().remove("leveledUp[1]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 5);
				} else {
					lvlupSkill = 1;
					player.getPackets().sendConfig(1230, 40);
				}
				break;
			case 40: // Ranged
				skillMenu = 3;
				if (player.getTemporaryAttributtes().remove("leveledUp[4]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 3);
				} else {
					lvlupSkill = 4;
					player.getPackets().sendConfig(1230, 30);
				}
				break;
			case 58: // Prayer
				if (player.getTemporaryAttributtes().remove("leveledUp[5]") != Boolean.TRUE) {
					skillMenu = 7;
					player.getPackets().sendConfig(965, 7);
				} else {
					lvlupSkill = 5;
					player.getPackets().sendConfig(1230, 60);
				}
				break;
			case 71: // Magic
				if (player.getTemporaryAttributtes().remove("leveledUp[6]") != Boolean.TRUE) {
					skillMenu = 4;
					player.getPackets().sendConfig(965, 4);
				} else {
					lvlupSkill = 6;
					player.getPackets().sendConfig(1230, 33);
				}
				break;
			case 84: // Runecrafting
				if (player.getTemporaryAttributtes().remove("leveledUp[20]") != Boolean.TRUE) {
					skillMenu = 12;
					player.getPackets().sendConfig(965, 12);
				} else {
					lvlupSkill = 20;
					player.getPackets().sendConfig(1230, 100);
				}
				break;
			case 102: // Construction
				skillMenu = 22;
				if (player.getTemporaryAttributtes().remove("leveledUp[21]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 22);
				} else {
					lvlupSkill = 21;
					player.getPackets().sendConfig(1230, 698);
				}
				break;
			case 145: // Hitpoints
				skillMenu = 6;
				if (player.getTemporaryAttributtes().remove("leveledUp[3]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 6);
				} else {
					lvlupSkill = 3;
					player.getPackets().sendConfig(1230, 50);
				}
				break;
			case 15: // Agility
				skillMenu = 8;
				if (player.getTemporaryAttributtes().remove("leveledUp[16]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 8);
				} else {
					lvlupSkill = 16;
					player.getPackets().sendConfig(1230, 65);
				}
				break;
			case 28: // Herblore
				skillMenu = 9;
				if (player.getTemporaryAttributtes().remove("leveledUp[15]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 9);
				} else {
					lvlupSkill = 15;
					player.getPackets().sendConfig(1230, 75);
				}
				break;
			case 46: // Thieving
				skillMenu = 10;
				if (player.getTemporaryAttributtes().remove("leveledUp[17]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 10);
				} else {
					lvlupSkill = 17;
					player.getPackets().sendConfig(1230, 80);
				}
				break;
			case 64: // Crafting
				skillMenu = 11;
				if (player.getTemporaryAttributtes().remove("leveledUp[12]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 11);
				} else {
					lvlupSkill = 12;
					player.getPackets().sendConfig(1230, 90);
				}
				break;
			case 77: // Fletching
				skillMenu = 19;
				if (player.getTemporaryAttributtes().remove("leveledUp[9]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 19);
				} else {
					lvlupSkill = 9;
					player.getPackets().sendConfig(1230, 665);
				}
				break;
			case 90: // Slayer
				skillMenu = 20;
				if (player.getTemporaryAttributtes().remove("leveledUp[18]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 20);
				} else {
					lvlupSkill = 18;
					player.getPackets().sendConfig(1230, 673);
				}
				break;
			case 108: // Hunter
				skillMenu = 23;
				if (player.getTemporaryAttributtes().remove("leveledUp[22]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 23);
				} else {
					lvlupSkill = 22;
					player.getPackets().sendConfig(1230, 689);
				}
				break;
			case 140: // Mining
				skillMenu = 13;
				if (player.getTemporaryAttributtes().remove("leveledUp[14]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 13);
				} else {
					lvlupSkill = 14;
					player.getPackets().sendConfig(1230, 110);
				}
				break;
			case 135: // Smithing
				skillMenu = 14;
				if (player.getTemporaryAttributtes().remove("leveledUp[13]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 14);
				} else {
					lvlupSkill = 13;
					player.getPackets().sendConfig(1230, 115);
				}
				break;
			case 34: // Fishing
				skillMenu = 15;
				if (player.getTemporaryAttributtes().remove("leveledUp[10]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 15);
				} else {
					lvlupSkill = 10;
					player.getPackets().sendConfig(1230, 120);
				}
				break;
			case 52: // Cooking
				skillMenu = 16;
				if (player.getTemporaryAttributtes().remove("leveledUp[7]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 16);
				} else {
					lvlupSkill = 7;
					player.getPackets().sendConfig(1230, 641);
				}
				break;
			case 130: // Firemaking
				skillMenu = 17;
				if (player.getTemporaryAttributtes().remove("leveledUp[11]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 17);
				} else {
					lvlupSkill = 11;
					player.getPackets().sendConfig(1230, 649);
				}
				break;
			case 125: // Woodcutting
				skillMenu = 18;
				if (player.getTemporaryAttributtes().remove("leveledUp[8]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 18);
				} else {
					lvlupSkill = 8;
					player.getPackets().sendConfig(1230, 660);
				}
				break;
			case 96: // Farming
				skillMenu = 21;
				if (player.getTemporaryAttributtes().remove("leveledUp[19]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 21);
				} else {
					lvlupSkill = 19;
					player.getPackets().sendConfig(1230, 681);
				}
				break;
			case 114: // Summoning
				skillMenu = 24;
				if (player.getTemporaryAttributtes().remove("leveledUp[23]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 24);
				} else {
					lvlupSkill = 23;
					player.getPackets().sendConfig(1230, 705);
				}
				break;
			case 120: // Dung
				skillMenu = 25;
				if (player.getTemporaryAttributtes().remove("leveledUp[24]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 25);
				} else {
					lvlupSkill = 24;
					player.getPackets().sendConfig(1230, 705);
				}
				break;
			}

			player.getInterfaceManager().sendScreenInterface(317, 1218);
			player.getPackets().sendInterface(false, 1218, 1, 1217); // seems to fix
			if (lvlupSkill != -1)
				switchFlash(player, lvlupSkill, false);
			if (skillMenu != -1)
				player.getTemporaryAttributtes().put("skillMenu", skillMenu);
			else if (interfaceId == 1218) {
				if ((componentId >= 33 && componentId <= 55) || componentId == 120 || componentId == 151
						|| componentId == 189)
					player.getPackets().sendInterface(false, 1218, 1, 1217); // seems to fix
			break;
			}
		case 449:
			int skillMenuX = -1;
			if (player.getTemporaryAttributtes().get("skillMenu") != null)
				skillMenuX = (Integer) player.getTemporaryAttributtes().get("skillMenu");
			if (componentId >= 10 && componentId <= 25)
				player.getPackets().sendConfig(965, ((componentId - 10) * 1024) + skillMenuX);
			else if (componentId == 29)
				// close inter
				player.stopAll();
			break;
		}
	}
	
	  public static void switchFlash(Player player, int skill, boolean on) {
	        int id = 0;
	        if (skill == Skills.ATTACK)
	            id = 4732;
	        if (skill == Skills.STRENGTH)
	            id = 4733;
	        if (skill == Skills.DEFENCE)
	            id = 4734;
	        if (skill == Skills.RANGE)
	            id = 4735;
	        if (skill == Skills.PRAYER)
	            id = 4736;
	        if (skill == Skills.MAGIC)
	            id = 4737;
	        if (skill == Skills.HITPOINTS)
	            id = 4738;
	        if (skill == Skills.AGILITY)
	            id = 4739;
	        if (skill == Skills.HERBLORE)//4736
	            id = 4740;
	        if (skill == Skills.THIEVING)
	            id = 4741;
	        if (skill == Skills.CRAFTING)
	            id = 4742;
	        if (skill == Skills.FLETCHING)
	            id = 4743;
	        if (skill == Skills.MINING)
	            id = 4744;
	        if (skill == Skills.SMITHING)
	            id = 4745;
	        if (skill == Skills.FISHING)
	            id = 4746;
	        if (skill == Skills.COOKING)
	            id = 4747;
	        if (skill == Skills.FIREMAKING)
	            id = 4748;
	        if (skill == Skills.WOODCUTTING)
	            id = 4749;
	        if (skill == Skills.RUNECRAFTING)//4754
	            id = 4750;
	        if (skill == Skills.SLAYER)
	            id = 4751;
	        if (skill == Skills.FARMING)
	            id = 4752;
	        if (skill == Skills.CONSTRUCTION)
	            id = 4753;
	        if (skill == Skills.HUNTER)
	            id = 4754;
	        if (skill == Skills.SUMMONING)
	            id = 4755;
	        if (skill == Skills.DUNGEONEERING)
	            id = 7756;
	        player.getPackets().sendConfig(id, on ? 1 : 0);
	    }
}