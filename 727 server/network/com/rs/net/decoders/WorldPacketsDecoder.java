package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.cache.io.InputStream;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.impl.DestroyItemD;
import com.rs.game.item.AutomaticGroundItem;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.player.FriendChatsManager;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.PublicChatMessage;
import com.rs.game.player.actions.PlayerFollow;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.route.RouteFinder;
import com.rs.game.route.strategy.FixedTileStrategy;
import com.rs.game.route.strategy.RouteEvent;
import com.rs.net.Session;
import com.rs.utils.Huffman;
import com.rs.utils.IntegerInputAction;
import com.rs.utils.Logger;
import com.rs.utils.StringInputAction;
import com.rs.utils.Utils;

import main.CommandDispatcher;
import main.NPCDispatcher;
import main.ObjectDispatcher;
import main.RSInterfaceDispatcher;
import main.impl.rsinterface.BankPinInterfacePlugin;
import main.impl.rsinterface.InventoryInterfacePlugin;
import npc.NPC;
import npc.familiar.Familiar;
import npc.familiar.Familiar.SpecialAttack;
import player.PlayerCombat;
import skills.Skills;
import skills.magic.Magic;
import skills.summoning.Summoning;

public final class WorldPacketsDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[104];

	//WALKING
	private final static int WALKING_PACKET = 33;
	private final static int MINI_WALKING_PACKET = 42;
	
	//BUTTONS
	public final static int ACTION_BUTTON1_PACKET = 96;
	public final static int ACTION_BUTTON2_PACKET = 27;
	public final static int ACTION_BUTTON3_PACKET = 68;
	public final static int ACTION_BUTTON4_PACKET = 9;
	public final static int ACTION_BUTTON5_PACKET = 72;
	public final static int ACTION_BUTTON6_PACKET = 19;
	public final static int ACTION_BUTTON7_PACKET = 23;
	public final static int ACTION_BUTTON8_PACKET = 21;
	public final static int ACTION_BUTTON9_PACKET = 22;
	public final static int ACTION_BUTTON10_PACKET = 81;
	
	//PLAYER BUTTONS
	private final static int PLAYER_OPTION_1_PACKET = 66;
	private final static int PLAYER_OPTION_2_PACKET = 6;
	private final static int PLAYER_OPTION_3_PACKET = 31;
	private final static int PLAYER_OPTION_4_PACKET = 89;
	private final static int PLAYER_OPTION_5_PACKET = 103;
	private final static int PLAYER_OPTION_6_PACKET = 1;
	private final static int PLAYER_OPTION_7_PACKET = 51;
	private final static int PLAYER_OPTION_8_PACKET = 94;
	@SuppressWarnings("unused")
	private final static int PLAYER_OPTION_9_PACKET = 53;
	@SuppressWarnings("unused")
	private final static int PLAYER_OPTION_10_PACKET = 70;
	
	//OBJECT BUTTONS
	private final static int OBJECT_CLICK1_PACKET = 75;
	private final static int OBJECT_CLICK2_PACKET = 93;
	private final static int OBJECT_CLICK3_PACKET = 38;
	private final static int OBJECT_CLICK4_PACKET = 32;
	private final static int OBJECT_CLICK5_PACKET = 48;
	private final static int OBJECT_EXAMINE_PACKET = 73;
	
	
	//NPC BUTTONS
	private final static int ATTACK_NPC = 16;
	private static final int NPC_EXAMINE_PACKET = 3;
	private final static int NPC_CLICK1_PACKET = 65;
	private final static int NPC_CLICK2_PACKET = 50;
	private final static int NPC_CLICK3_PACKET = 77;
	private final static int NPC_CLICK4_PACKET = 95;
	
	//MISC
	private final static int COMMANDS_PACKET = 85;
	private final static int DIALOGUE_CONTINUE_PACKET = 49;
	private final static int SCREEN_PACKET = 84;
	private final static int CLOSE_INTERFACE_PACKET = 60;
	private final static int REPORT_ABUSE_PACKET = 100;
	private final static int ADD_IGNORE_PACKET = 34;
	private final static int REMOVE_IGNORE_PACKET = 12;
	private final static int ADD_FRIEND_PACKET = 26;
	private final static int REMOVE_FRIEND_PACKET = 29;
	private final static int KEY_PRESSED_PACKET = 28;
	private final static int INACTIVITY_PACKET = 0;
	private final static int ENTER_LONGSTRING_PACKET = 87;
	private final static int ENTER_NAME_PACKET = 80;
	private final static int ENTER_INTEGER_PACKET = 58;
	private static final int GRAND_EXCHANGE_PACKET = 17;
	private final static int COLOR_ID_PACKET = 11;
	private final static int CHAT_TYPE_PACKET = 30;
	
	//INTERFACE COMPONENT INTERACTION
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 74;
	private final static int INTERFACE_ON_INTERFACE_PACKET = 4;
	
	//FRIENDS CHAT
	private final static int JOIN_FRIEND_CHAT_PACKET = 71;
	private final static int CHANGE_FRIEND_CHAT_PACKET = 7;
	private final static int KICK_FRIEND_CHAT_PACKET = 91;
	
	//CHAT TYPES
	private final static int CHAT_PACKET = 86;
	@SuppressWarnings("unused")
	private final static int PUBLIC_QUICK_CHAT_PACKET = 64;
	
	private final static int SEND_FRIEND_MESSAGE_PACKET = 15;
	@SuppressWarnings("unused")
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 14;
	
	private final static int ITEM_TAKE_PACKET = 54;
	private final static int GROUND_ITEM_EXAMINE = 61;
 
	private final static int INTERFACE_ON_OBJECT = 98;
	private final static int INTERFACE_ON_PLAYER = 13;
	private final static int INTERFACE_ON_NPC = 41;
	
	

	public final static int WORLD_MAP_CLICK = 5;
	private final static int DONE_LOADING_REGION_PACKET = -1;
	
	private final static int AFK_PACKET = -1;
	public final static int RECEIVE_PACKET_COUNT_PACKET = -1;
	private final static int MOVE_CAMERA_PACKET = 83; 
	private final static int CLICK_PACKET = 59; 
	private final static int MOVE_MOUSE_PACKET = -1;
	private final static int IN_OUT_SCREEN_PACKET = -1;
	private final static int PING_PACKET = -1;

	private final static int MAGIC_ON_ITEM_PACKET = -1; //ignore this one - shitty configuration
	

	static {
		PACKET_SIZES[0] = 0;
		PACKET_SIZES[1] = 3;
		PACKET_SIZES[2] = 4;
		PACKET_SIZES[3] = 3;
		PACKET_SIZES[4] = 16;
		PACKET_SIZES[5] = 4;
		PACKET_SIZES[6] = 3;
		PACKET_SIZES[7] = -1;
		PACKET_SIZES[8] = 7;
		PACKET_SIZES[9] = 8;
		PACKET_SIZES[10] = -1;
		PACKET_SIZES[11] = 2;
		PACKET_SIZES[12] = -1;
		PACKET_SIZES[13] = 11;
		PACKET_SIZES[14] = -1;
		PACKET_SIZES[15] = -2;
		PACKET_SIZES[16] = 3;
		PACKET_SIZES[17] = 2;
		PACKET_SIZES[18] = 4;
		PACKET_SIZES[19] = 8;
		PACKET_SIZES[20] = 3;
		PACKET_SIZES[21] = 8;
		PACKET_SIZES[22] = 8;
		PACKET_SIZES[23] = 8;
		PACKET_SIZES[24] = 7;
		PACKET_SIZES[25] = 7;
		PACKET_SIZES[26] = -1;
		PACKET_SIZES[27] = 8;
		PACKET_SIZES[28] = -2;
		PACKET_SIZES[29] = -1;
		PACKET_SIZES[30] = 1;
		PACKET_SIZES[31] = 3;
		PACKET_SIZES[32] = 9;
		PACKET_SIZES[33] = 5;
		PACKET_SIZES[34] = -1;
		PACKET_SIZES[35] = -2;
		PACKET_SIZES[36] = -1;
		PACKET_SIZES[37] = 2;
		PACKET_SIZES[38] = 9;
		PACKET_SIZES[39] = -1;
		PACKET_SIZES[40] = -1;
		PACKET_SIZES[41] = 11;
		PACKET_SIZES[42] = 18;
		PACKET_SIZES[43] = 7;
		PACKET_SIZES[44] = 9;
		PACKET_SIZES[45] = 1;
		PACKET_SIZES[46] = 12;
		PACKET_SIZES[47] = 4;
		PACKET_SIZES[48] = 9;
		PACKET_SIZES[49] = 6;
		PACKET_SIZES[50] = 3;
		PACKET_SIZES[51] = 3;
		PACKET_SIZES[52] = -1;
		PACKET_SIZES[53] = 3;
		PACKET_SIZES[54] = 7;
		PACKET_SIZES[55] = 4;
		PACKET_SIZES[56] = -2;
		PACKET_SIZES[57] = 7;
		PACKET_SIZES[58] = 4;
		PACKET_SIZES[59] = 6;
		PACKET_SIZES[60] = 0;
		PACKET_SIZES[61] = 7;
		PACKET_SIZES[62] = 1;
		PACKET_SIZES[63] = 4;
		PACKET_SIZES[64] = -1;
		PACKET_SIZES[65] = 3;
		PACKET_SIZES[66] = 3;
		PACKET_SIZES[67] = 15;
		PACKET_SIZES[68] = 8;
		PACKET_SIZES[69] = -1;
		PACKET_SIZES[70] = 3;
		PACKET_SIZES[71] = -1;
		PACKET_SIZES[72] = 8;
		PACKET_SIZES[73] = 9;
		PACKET_SIZES[74] = 16;
		PACKET_SIZES[75] = 9;
		PACKET_SIZES[76] = 0;
		PACKET_SIZES[77] = 3;
		PACKET_SIZES[78] = -1;
		PACKET_SIZES[79] = 1;
		PACKET_SIZES[80] = -1;
		PACKET_SIZES[81] = 8;
		PACKET_SIZES[82] = 4;
		PACKET_SIZES[83] = 4;
		PACKET_SIZES[84] = 6;
		PACKET_SIZES[85] = -1;
		PACKET_SIZES[86] = -1;
		PACKET_SIZES[87] = -1;
		PACKET_SIZES[88] = 2;
		PACKET_SIZES[89] = 3;
		PACKET_SIZES[90] = -1;
		PACKET_SIZES[91] = -1;
		PACKET_SIZES[92] = -2;
		PACKET_SIZES[93] = 9;
		PACKET_SIZES[94] = 3;
		PACKET_SIZES[95] = 3;
		PACKET_SIZES[96] = 8;
		PACKET_SIZES[97] = -1;
		PACKET_SIZES[98] = 17;
		PACKET_SIZES[99] = -2;
		PACKET_SIZES[100] = -1;
		PACKET_SIZES[101] = -2;
		PACKET_SIZES[102] = -2;
		PACKET_SIZES[103] = 3;
	}

	private Player player;
	private int chatType;

	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
			int packetId = stream.readPacket(player);
			if (packetId >= PACKET_SIZES.length || packetId < 0) {
				if (Settings.DEBUG)
					System.out.println("PacketId " + packetId + " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				if (Settings.DEBUG)
					System.out.println("Invalid size for PacketId " + packetId + ". Size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				if (Settings.DEBUG)
					System.out.println("PacketId " + packetId + " has fake size. - expected size " + length);
				// break;

			}

			if (packetId < 0) {
				System.out.println("PacketId " + packetId + " has . - expected size " + length);
			}

			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}

	public static void decodeLogicPacket(final Player player, LogicPacket packet) {
		int packetId = packet.getId();
		InputStream stream = new InputStream(packet.getData());
		if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() > currentTime)
				return;
			if (player.getFreezeDelay() >= currentTime) {
				player.getPackets().sendGameMessage("A magical force prevents you from moving.");
				return;
			}
			@SuppressWarnings("unused")
			int length = stream.getLength();
			/*
			 * if (packetId == MINI_WALKING_PACKET) length -= 13;
			 */
			
			boolean forceRun = stream.readUnsignedByte() == 1;
			if (forceRun)
				player.setRun(forceRun);
			int baseX = stream.readUnsignedShort();
			int baseY = stream.readUnsignedShortLE();
	
            int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getHeight(), player.getSize(), new FixedTileStrategy(baseX, baseY), true);
			if (steps > 25)
	            steps = 25;

	        player.stopAll();

	        player.setNextFaceEntity(null);
	        
	        player.getSkillActionTask().ifPresent(skill -> skill.cancel());

	        if (steps > 0) {

	            @SuppressWarnings("unused")
				int x = 0, y = 0;
	            for (int step = 0; step < steps; step++) {
	                x = baseX + stream.readUnsignedByte();
	                y = baseY + stream.readUnsignedByte();
	            }
	            
	            int[] bufferX = RouteFinder.getLastPathBufferX();
	            int[] bufferY = RouteFinder.getLastPathBufferY();
	            int last = -1;
	            for (int i = steps - 1; i >= 0; i--) {
	                if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true))
	                    break;
	                last = i;
	            }

	            if (last != -1) {
	                WorldTile tile = new WorldTile(bufferX[last], bufferY[last], player.getHeight());
	                player.getPackets().sendMinimapFlag(tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()), tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
	            } else {
	                player.getPackets().sendResetMinimapFlag();
	            }
	        }
		} 
		//TODO do player buttons here
		
//		switch (packetId){
//		case PLAYER_OPTION_1_PACKET:
//		case PLAYER_OPTION_2_PACKET:
//		case PLAYER_OPTION_3_PACKET:
//		case PLAYER_OPTION_4_PACKET:
//		case PLAYER_OPTION_5_PACKET:
//		case PLAYER_OPTION_6_PACKET:
//		case PLAYER_OPTION_7_PACKET:
//		case PLAYER_OPTION_8_PACKET:
//		
//			int playerIndex = stream.readUnsignedShort(); //incorrect returns 32k
//			boolean forceRun = stream.read128Byte() == 1;
//			if (forceRun)
//				player.setRun(true);
//			Player p2 = World.getPlayers().get(playerIndex);
//			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
//				return;
//			player.getPackets().sendGameMessage("Sent player interact packet, id: " +packetId);
//			return;
//		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	 if (packetId == INTERFACE_ON_OBJECT) {
		 

		 	int x = stream.readUnsignedShortLE128();
		 	boolean forceRun = stream.readUnsigned128Byte() == 1;
		 	int objectId = stream.readIntV1();
		 	int interfaceHash = stream.readInt();
		 	int itemId = stream.readUnsignedShortLE();
		 	int slot = stream.readUnsignedShort128();
		 	int y = stream.readUnsignedShortLE();
			
		 	int interfaceId = interfaceHash >> 16;
		 	int componentId = interfaceHash - (interfaceId << 16);
			
			System.out.println(String.format("%s, %s, %s, %s, %s, %s, %s", x, forceRun, objectId, interfaceHash, itemId, slot, y));
			System.out.println(String.format("%s, %s,", interfaceId, componentId));
			
			//3095, 0, 17010, 44498944, 11694, 0, 3503
//			0, 17010,
			
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() >= currentTime || player.getEmotesManager().getNextEmoteEnd() >= currentTime)
				return;
			final WorldTile tile = new WorldTile(x, y, player.getHeight());
			int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(objectId, tile);
			if (mapObject == null || mapObject.getId() != objectId)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(objectId, mapObject.getType(), mapObject.getRotation(), x, y, player.getHeight());
			final Item item = player.getInventory().getItem(slot);
			if (player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (item == null || item.getId() != itemId)
				return;
			player.stopAll(false); // false
			if (forceRun)
				player.setRun(forceRun);
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE: // inventory
				ObjectDispatcher.handleItemOnObject(player, object, interfaceId, item);
				break;
			}
		} else if (packetId == PLAYER_OPTION_2_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerFollow(p2));
		} else if (packetId == PLAYER_OPTION_4_PACKET) {
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort(); //incorrect returns 32k
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			if (player.isCantTrade()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target " + p2.getDisplayName());
				return;
			}

			if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
				p2.getTemporaryAttributtes().remove("TradeTarget");
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.getTemporaryAttributtes().put("TradeTarget", p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);
		} else if (packetId == PLAYER_OPTION_1_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			int playerIndex = stream.readUnsignedShort(); //incorrect returns 32k
			boolean forceRun = stream.read128Byte() == 1;
			if (forceRun)
				player.setRun(true);
			Player targetPlayer = World.getPlayers().get(playerIndex);
			if (targetPlayer == null || targetPlayer.isDead() || targetPlayer.hasFinished()
					|| !player.getMapRegionsIds().contains(targetPlayer.getRegionId()))
				return;
			if (targetPlayer == null || targetPlayer.isDead() || targetPlayer.hasFinished() || !player.getMapRegionsIds().contains(targetPlayer.getRegionId()))
				return;
			
			if (player.getLockDelay() > Utils.currentTimeMillis() || !player.getControlerManager().canPlayerOption1(targetPlayer))
				return;
			if (!player.isCanPvp())
				return;
			if (!player.getControlerManager().canAttack(targetPlayer))
				return;
			
			if (!player.isCanPvp() || !targetPlayer.isCanPvp()) {
				player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
				return;
			}
			if (!targetPlayer.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != targetPlayer && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You are already in combat.");
					return;
				}
				if (targetPlayer.getAttackedBy() != player && targetPlayer.getAttackedByDelay() > Utils.currentTimeMillis()) {
					if (targetPlayer.getAttackedBy() instanceof NPC) {
						targetPlayer.setAttackedBy(player); // changes enemy to player,
						// player has priority over
						// npc on single areas
					} else {
						player.getPackets().sendGameMessage("That player is already in combat.");
						return;
					}
				}
			}
			
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(targetPlayer));
		} else if (packetId == ATTACK_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
				return;
			}
			if (player.getLockDelay() > Utils.currentTimeMillis()) {
				return;
			}
			int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
			boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
			if (forceRun)
				player.setRun(forceRun);
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || !npc.getDefinitions().hasAttackOption()) {
				return;
			}
			if (!player.getControlerManager().canAttack(npc)) {
				return;
			}
			if (npc instanceof Familiar) {
				Familiar familiar = (Familiar) npc;
				if (familiar == player.getFamiliar()) {
					player.getPackets().sendGameMessage("You can't attack your own familiar.");
					return;
				}
				if (!familiar.canAttack(player)) {
					player.getPackets().sendGameMessage("You can't attack this npc.");
					return;
				}
			} else if (!npc.isForceMultiAttacked()) {
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
						player.getPackets().sendGameMessage("You are already in combat.");
						return;
					}
					if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
						player.getPackets().sendGameMessage("This npc is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(npc));
			player.getWatchMap().get("TOLERANCE").reset();
		}
		
		
		else if (packetId == GRAND_EXCHANGE_PACKET){
			int itemId = stream.readUnsignedShort();
			player.getPackets().sendGameMessage("ge Choose item: "+itemId);
			//player.getGeManager().chooseItem(itemId);
		}
		
		
		
		else if (packetId == INTERFACE_ON_PLAYER) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			
			int slot = stream.readUnsignedShort();
			int playerIndex = stream.readUnsignedShortLE();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			int interfaceHash = stream.readIntV2();
			int itemId = stream.readUnsignedShortLE();
			
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			
			System.out.println(String.format("%s, %s, %s, %s, %s,", slot, playerIndex, forceRun, interfaceHash, itemId));
			System.out.println(String.format("%s, %s,", interfaceId, componentId));
			
			
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			player.stopAll(false);
			if (forceRun)
				player.setRun(forceRun);
			switch (interfaceId) {
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 15) || (interfaceId == 662 && componentId == 65) || (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 18) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 24 || interfaceId == 747 && componentId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (!player.isCanPvp() || !p2.isCanPvp()) {
						player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
						return;
					}
					if (!player.getFamiliar().canAttack(p2)) {
						player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18);
						player.getFamiliar().setTarget(p2);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2.getCoordFaceY(p2.getSize()), p2.getHeight()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
								player.getPackets().sendGameMessage("That " + (player.getAttackedBy() instanceof Player ? "player" : "npc") + " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets().sendGameMessage("That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(p2));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 86: // teleblock
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 91: // fire surge
				case 99: // storm of armadyl
				case 36: // bind
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2.getCoordFaceY(p2.getSize()), p2.getHeight()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
								player.getPackets().sendGameMessage("That " + (player.getAttackedBy() instanceof Player ? "player" : "npc") + " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets().sendGameMessage("That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(p2));
					}
					break;
				}
				break;
			}
			if (Settings.DEBUG)
				System.out.println("Spell:" + componentId);
		} else if (packetId == INTERFACE_ON_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
	
			int interfaceHash = stream.readIntV2();
			int npcIndex = stream.readUnsignedShortLE128();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			int itemId = stream.readUnsignedShortLE128();
			int interfaceSlot = stream.readUnsignedShort128();
			
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			
			System.out.println(String.format("%s, %s, %s, %s, %s,", interfaceHash, npcIndex, forceRun, itemId, interfaceSlot));
			System.out.println(String.format("%s, %s,", interfaceId, componentId));
			
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			player.stopAll(false);
			if (forceRun)
				player.setRun(forceRun);
			if (interfaceId != Inventory.INVENTORY_INTERFACE) {
				if (!npc.getDefinitions().hasAttackOption()) {
					player.getPackets().sendGameMessage("You can't attack this npc.");
					return;
				}
			}
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(interfaceSlot);
				if (item == null || !player.getControlerManager().processItemOnNPC(npc, item))
					return;
				InventoryInterfacePlugin.handleItemOnNPC(player, npc, item);
				break;
			case 1165:
				Summoning.attackDreadnipTarget(npc, player);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 15) || (interfaceId == 662 && componentId == 65) || (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 18 || interfaceId == 747 && componentId == 24) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage("You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar().canAttack(familiar.getOwner())) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getHeight()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage("You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage("You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(npc));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 93:
				case 91: // fire surge
				case 99: // storm of Armadyl
				case 36: // bind
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getHeight()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage("You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage("You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(npc));
					}
					break;
				}
				break;
			}
			if (Settings.DEBUG)
				System.out.println("Spell:" + componentId);
		}
		
	 	if (packetId == OBJECT_CLICK1_PACKET)
			ObjectDispatcher.handleOption(player, stream, 1);
		else if (packetId == OBJECT_CLICK2_PACKET)
			ObjectDispatcher.handleOption(player, stream, 2);
		else if (packetId == OBJECT_CLICK3_PACKET)
			ObjectDispatcher.handleOption(player, stream, 3);
		else if (packetId == OBJECT_CLICK4_PACKET)
			ObjectDispatcher.handleOption(player, stream, 4);
		else if (packetId == OBJECT_CLICK5_PACKET)
			ObjectDispatcher.handleOption(player, stream, 5);
		else if (packetId == ITEM_TAKE_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() > currentTime)
				// || player.getFreezeDelay() >= currentTime)
				return;

//			int y = stream.readUnsignedShort();
//			int x = stream.readUnsignedShortLE();
//			final int id = stream.readUnsignedShort();
//			boolean forceRun = stream.read128Byte() == 1;
			
			
			final int id = stream.readShortLE128();
			boolean forceRun =  stream.readUnsignedByteC() == 1; 
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShort128();
			
			System.out.println(x+", "+ y +", "+id +", "+forceRun);
			//0, 4153, 3503, 3216
			//3216, 3503, 0, 4153
			
			final WorldTile tile = new WorldTile(x, y, player.getHeight());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId)){
				return;
			}
			final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
			if (item == null)
				return;
			System.out.println(item.getId());
			player.stopAll(false);
			if (forceRun)
				player.setRun(forceRun);
			player.setRouteEvent(new RouteEvent(item, new Runnable() {
				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
					if (item == null)
						return;
					/*
					 * if (player.getRights() > 0 || player.isSupporter())
					 * player.getPackets().sendGameMessage("This item was dropped by [Username] "+item.getOwner().getUsername()+
					 * " [DiplayName] "+item.getOwner().getDisplayName());
					 */ player.setNextFaceWorldTile(tile);
					
					player.addWalkSteps(tile.getX(), tile.getY(), 1);
					AutomaticGroundItem.pickup(tile, item);
					FloorItem.removeGroundItem(player, item);
				}
			}, false));
		}
	 	NPCDispatcher.executeMobInteraction(player, stream, packetId == NPC_CLICK1_PACKET ? 1 :packetId ==  NPC_CLICK2_PACKET ? 2 :packetId ==  NPC_CLICK3_PACKET ? 3 : packetId == NPC_CLICK4_PACKET ? 4 : 5);
	}

	public void processPackets(final int packetId, InputStream stream, int length) {
		player.setPacketsDecoderPing(Utils.currentTimeMillis());
//		System.out.println(packetId);
		if (packetId == PING_PACKET) {
			// kk we ping :)
		} else if (packetId == MOVE_MOUSE_PACKET) {
			// USELESS PACKET
		} else if (packetId == RECEIVE_PACKET_COUNT_PACKET) {
			// interface packets
			stream.readInt();
		} else if (packetId == INTERFACE_ON_INTERFACE_PACKET) {
			InventoryInterfacePlugin.handleItemOnItem(player, stream);
		} else if (packetId == MAGIC_ON_ITEM_PACKET) {
			int inventoryInter = stream.readInt() >> 16;
			int itemId = stream.readShort128();
			@SuppressWarnings("unused")
			int junk = stream.readShort();
			@SuppressWarnings("unused")
			int itemSlot = stream.readShortLE();
			int interfaceSet = stream.readIntV1();
			int spellId = interfaceSet & 0xFFF;
			int magicInter = interfaceSet >> 16;
			if (inventoryInter == 149 && magicInter == 192) {
				switch (spellId) {
				case 59:// High Alch
					if (player.getSkills().getLevel(Skills.MAGIC) < 55) {
						player.getPackets().sendGameMessage("You do not have the required level to cast this spell.");
						return;
					}
					if (itemId == 995) {
						player.getPackets().sendGameMessage("You can't alch this!");
						return;
					}
					if (player.getEquipment().getWeaponId() == 1401 || player.getEquipment().getWeaponId() == 3054 || player.getEquipment().getWeaponId() == 19323) {
						if (!player.getInventory().containsItem(561, 1)) {
							player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
							return;
						}
						player.setNextAnimation(new Animation(9633));
						player.setNextGraphics(new Graphics(112));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue() >> 6);
					} else {
						if (!player.getInventory().containsItem(561, 1) || !player.getInventory().containsItem(554, 5)) {
							player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
							return;
						}
						player.setNextAnimation(new Animation(713));
						player.setNextGraphics(new Graphics(113));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(554, 5);
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue() >> 6);
					}
					break;
				default:
					System.out.println("Spell:" + spellId + ", Item:" + itemId);
				}
				System.out.println("Spell:" + spellId + ", Item:" + itemId);
			}
		} else if (packetId == AFK_PACKET) {
			player.getSession().getChannel().close();
		} else if (packetId == CLOSE_INTERFACE_PACKET) {
			if (player.hasStarted() && !player.hasFinished() && !player.isActive()) { // used for old welcome screen
				player.run();
				return;
			}
			player.stopAll();
		} else if (packetId == MOVE_CAMERA_PACKET) {
			// not using it atm
			stream.readUnsignedShort();
			stream.readUnsignedShort();
		} else if (packetId == IN_OUT_SCREEN_PACKET) {
			// not using this check because not 100% efficient
			@SuppressWarnings("unused")
			boolean inScreen = stream.readByte() == 1;
		} else if (packetId == SCREEN_PACKET) {
			byte displayMode = (byte) stream.readUnsignedByte();
			player.setScreenWidth((short) stream.readUnsignedShort());
			player.setScreenHeight((short) stream.readUnsignedShort());
			@SuppressWarnings("unused")
			boolean switchScreenMode = stream.readUnsignedByte() == 1;
			if (!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode() || !player.getInterfaceManager().containsInterface(742))
				return;
			player.setDisplayMode(displayMode);
			player.getInterfaceManager().removeAll();
			player.getInterfaceManager().sendInterfaces();
			player.getInterfaceManager().sendInterface(742);
		} else if (packetId == CLICK_PACKET) {
			int mouseHash = stream.readShortLE128();
			int mouseButton = mouseHash >> 15;
			int time = mouseHash - (mouseButton << 15); // time
			int positionHash = stream.readIntV1();
			int y = positionHash >> 16; // y;
			int x = positionHash - (y << 16); // x
			@SuppressWarnings("unused")
			boolean clicked;
			// mass click or stupid autoclicker, lets stop lagg
			if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0 || y > player.getScreenHeight()) {
				// player.getSession().getChannel().close();
				clicked = false;
				return;
			}
			clicked = true;
		} else if (packetId == DIALOGUE_CONTINUE_PACKET) {
			int interfaceHash = stream.readIntV1();//stream.readInt();
			int junk = stream.readShortLE128();//stream.readShort128();
			int interfaceId = interfaceHash >> 16;
			int buttonId = (interfaceHash & 0xFF);
			System.out.println("interId: "+interfaceId+", buttonId: "+buttonId);

			if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				// hack, or server error or client error
				// player.getSession().getChannel().close();
				return;
			}
			if (!player.isActive() || !player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (Settings.DEBUG)
				Logger.log(this, "Dialogue: " + interfaceId + ", " + buttonId + ", " + junk);
			int componentId = interfaceHash - (interfaceId << 16);
			BankPinInterfacePlugin.finishPinDialogue(player, interfaceId, componentId);
			new DestroyItemD(DestroyItemD.INSTANCE.getItem()).executeDestroy(player, interfaceId, componentId);
			if (DialogueEventListener.main(player, componentId))
				return;
		} else if (packetId == WORLD_MAP_CLICK) {
			int coordinateHash = stream.readIntLE();
			int x = coordinateHash >> 14;
			int y = coordinateHash & 0x3fff;
			int plane = coordinateHash >> 28;
			Integer hash = (Integer) player.getTemporaryAttributtes().get("worldHash");
			if (hash == null || coordinateHash != hash)
				player.getTemporaryAttributtes().put("worldHash", coordinateHash);
			else {
				player.getTemporaryAttributtes().remove("worldHash");
				player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2, -1, true);
				player.getPackets().sendConfig(1159, coordinateHash);
			}
		} else if (packetId == ACTION_BUTTON1_PACKET || packetId == ACTION_BUTTON2_PACKET || packetId == ACTION_BUTTON4_PACKET || packetId == ACTION_BUTTON5_PACKET || packetId == ACTION_BUTTON6_PACKET || packetId == ACTION_BUTTON7_PACKET || packetId == ACTION_BUTTON8_PACKET || packetId == ACTION_BUTTON3_PACKET || packetId == ACTION_BUTTON9_PACKET || packetId == ACTION_BUTTON10_PACKET) {
			RSInterfaceDispatcher.handleButtons(player, stream, packetId);
		} 
		else if (packetId ==  ENTER_LONGSTRING_PACKET){
			if (!player.isActive() || player.isDead())
				return;
			int byte0 = stream.readUnsignedByte();
			String v1 = stream.readString();
			if (v1.equals(""))
				return;
			String value = Utils.getCharacterFromByte(byte0) + v1;
			if (player.getTemporaryAttributtes().get("string_input_action") != null) {
				StringInputAction action = (StringInputAction) player.getTemporaryAttributtes().remove("string_input_action");
				action.handle(value);
				return;
			}
			player.getPackets().sendGameMessage(""+value);
		}
		else if (packetId == ENTER_NAME_PACKET) {
			if (!player.isActive() || player.isDead())
				return;
			int byte0 = stream.readUnsignedByte();
			String v1 = stream.readString();
			if (v1.equals(""))
				return;
			String value = Utils.getCharacterFromByte(byte0) + v1;
			
			if (player.getInterfaceManager().containsInterface(1108))
				player.getFriendsIgnores().setChatPrefix(value);
//			else if (player.getTemporaryAttributtes().get("yellcolor") == Boolean.TRUE) {
//				if (value.length() != 6) {
//					player.getDialogueManager().startDialogue("SimpleMessage", "The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
//				} else if (Utils.containsInvalidCharacter(value) || value.contains("_")) {
//					player.getDialogueManager().startDialogue("SimpleMessage", "The requested yell color can only contain numeric and regular characters.");
//				} else {
//					player.getPlayerDetails().setYellColor(value);
//					player.getDialogueManager().startDialogue("SimpleMessage", "Your yell color has been changed to <col=" + player.getPlayerDetails().getYellColor() + ">" + player.getPlayerDetails().getYellColor() + "</col>.");
//				}
//				player.getTemporaryAttributtes().put("yellcolor", Boolean.FALSE);
//			}
		} else if (packetId == ENTER_INTEGER_PACKET) {
			if (!player.isActive() || player.isDead())
				return;
			int value = stream.readInt();
			if (player.getTemporaryAttributtes().get("integer_input_action") != null) {
				IntegerInputAction action = (IntegerInputAction) player.getTemporaryAttributtes().remove("integer_input_action");
				action.handle(value);
				return;
			}
			if ((player.getInterfaceManager().containsInterface(762) && player.getInterfaceManager().containsInterface(763)) || player.getInterfaceManager().containsInterface(11)) {
				if (value < 0)
					return;
				Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bank_item_X_Slot");
				if (bank_item_X_Slot == null)
					return;
				player.getBank().setLastX(value);
				player.getBank().refreshLastX();
				if (player.getTemporaryAttributtes().remove("bank_isWithdraw") != null)
					player.getBank().withdrawItem(bank_item_X_Slot, value);
				else
					player.getBank().depositItem(bank_item_X_Slot, value, player.getInterfaceManager().containsInterface(11) ? false : true);
			} else if (player.getInterfaceManager().containsInterface(206) && player.getInterfaceManager().containsInterface(207)) {
				if (value < 0)
					return;
				Integer pc_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("pc_item_X_Slot");
				if (pc_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("pc_isRemove") != null)
					player.getPriceCheckManager().removeItem(pc_item_X_Slot, value);
				else
					player.getPriceCheckManager().addItem(pc_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(671) && player.getInterfaceManager().containsInterface(665)) {
				if (player.getFamiliar() == null || player.getFamiliar().getBob() == null)
					return;
				if (value < 0)
					return;
				Integer bob_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null)
					player.getFamiliar().getBob().removeItem(bob_item_X_Slot, value);
				else
					player.getFamiliar().getBob().addItem(bob_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(335) && player.getInterfaceManager().containsInterface(336)) {
				if (value < 0)
					return;
				Integer trade_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("trade_item_X_Slot");
				if (trade_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("trade_isRemove") != null)
					player.getTrade().removeItem(trade_item_X_Slot, value);
				else
					player.getTrade().addItem(trade_item_X_Slot, value);
			}
		} else if (packetId == SWITCH_INTERFACE_ITEM_PACKET) {
			stream.readUnsignedShortLE();//skip in stream
			int fromSlot = stream.readUnsignedShortLE();
			int toSlot = stream.readUnsignedShortLE();
			stream.readUnsignedShortLE();//skip, idk what these are?
			int fromComponentId = stream.readShort();
			int fromInterfaceId = stream.readUnsignedShort();
			int toComponentId = stream.readUnsignedShortLE();

			//temporary, did I miss the from interface in the stream or is there no way to go between interfaces??
			int toInterfaceId = fromInterfaceId;

			if(Settings.DEBUG)
				System.out.println(String.format("fromInterfaceID: %s, toInterfaceID: %s, fromcompID: %s, tocompID %s, fromSlot: %s, toSlot: %s",
					fromInterfaceId, toInterfaceId, fromComponentId, toComponentId, fromSlot, toSlot));


			
			if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId || Utils.getInterfaceDefinitionsSize() <= toInterfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(fromInterfaceId) || !player.getInterfaceManager().containsInterface(toInterfaceId))
				return;
			if (fromComponentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
				return;
			if (toComponentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
				return;
			if (fromInterfaceId == Inventory.INVENTORY_INTERFACE && fromComponentId == 0 && toInterfaceId == Inventory.INVENTORY_INTERFACE && toComponentId == 0) {
				toSlot -= 28;
				if (toSlot < 0 || toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 763 && fromComponentId == 0 && toInterfaceId == 763 && toComponentId == 0) {
				if (toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
				player.getBank().switchItem(fromSlot, toSlot, fromComponentId, toComponentId);
			}
			if (Settings.DEBUG)
				System.out.println("Switch item interface " + fromInterfaceId + " from slot " + fromSlot + " to slot " + toSlot);
		} else if (packetId == DONE_LOADING_REGION_PACKET) {
			//bit off but ye for most part this is done.
			player.getWatchMap().get("TOLERANCE").reset();
		} 
		//TODO queue
		else if (packetId == WALKING_PACKET 
				|| packetId == MINI_WALKING_PACKET 
				|| packetId == ITEM_TAKE_PACKET 
				|| packetId == PLAYER_OPTION_2_PACKET 
						|| packetId == PLAYER_OPTION_4_PACKET || packetId == PLAYER_OPTION_3_PACKET 
				|| packetId == PLAYER_OPTION_1_PACKET 
				|| packetId == ATTACK_NPC 
				|| packetId == INTERFACE_ON_PLAYER 
				|| packetId == INTERFACE_ON_NPC 
				|| packetId == NPC_CLICK1_PACKET 
				|| packetId == NPC_CLICK2_PACKET 
				|| packetId == NPC_CLICK3_PACKET 
				|| packetId == OBJECT_CLICK1_PACKET 
				|| packetId == SWITCH_INTERFACE_ITEM_PACKET 
				|| packetId == OBJECT_CLICK2_PACKET 
				|| packetId == OBJECT_CLICK3_PACKET 
				|| packetId == OBJECT_CLICK4_PACKET 
				|| packetId == OBJECT_CLICK5_PACKET 
				|| packetId == INTERFACE_ON_OBJECT)
			player.addLogicPacketToQueue(new LogicPacket((byte) packetId, length, stream));
		else if (packetId == OBJECT_EXAMINE_PACKET) {
			ObjectDispatcher.handleOption(player, stream, -1);
		} else if (packetId == NPC_EXAMINE_PACKET) {
			NPCDispatcher.handleExamine(player, stream);
		} else if (packetId == JOIN_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			int byte0 = stream.readUnsignedByte();
			String username = stream.readString();
			String supposed_username = Utils.getCharacterFromByte(byte0) + username;
			player.getPackets().sendGameMessage(String.format("byte0: %s, string: %s, fixedEnded: %s", byte0, username, supposed_username));
			FriendChatsManager.joinChat(supposed_username, player);
		} else if (packetId == KICK_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 1000); // avoids
			int byte0 = stream.readUnsignedByte();
			String username = stream.readString();
			String supposed_username = Utils.getCharacterFromByte(byte0) + username;
			player.kickPlayerFromFriendsChannel(supposed_username);
		} else if (packetId == CHANGE_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted() || !player.getInterfaceManager().containsInterface(1108))
				return;
			int byte0 = stream.readUnsignedByte();
			int rank = stream.readUnsigned128Byte();
			String username = stream.readString();
			String supposed_username = Utils.getCharacterFromByte(byte0) + username;
			player.getFriendsIgnores().changeRank(supposed_username, rank);
		} else if (packetId == ADD_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			int byte0 = stream.readUnsignedByte();
			String username = stream.readString();
			String supposed_username = Utils.getCharacterFromByte(byte0) + username;
			player.getFriendsIgnores().addFriend(supposed_username);
		} else if (packetId == REMOVE_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			int byte0 = stream.readUnsignedByte();
			String username = stream.readString();
			String supposed_username = Utils.getCharacterFromByte(byte0) + username;
			player.getFriendsIgnores().removeFriend(supposed_username);
		} else if (packetId == ADD_IGNORE_PACKET) {
			if (!player.hasStarted())
				return;
			int byte0 = stream.readUnsignedByte();
			String username = stream.readString();
			boolean until_logout = stream.readUnsignedByte() == 1;
			//player.getPackets().sendGameMessage(String.format("byte0: %s, name: %s, logout_byte: %s", unknownByte0, name, task_boo));
			String supposed_username = Utils.getCharacterFromByte(byte0) + username;
			player.getFriendsIgnores().addIgnore(supposed_username, until_logout);
		} else if (packetId == REMOVE_IGNORE_PACKET) {
			if (!player.hasStarted())
				return;
			int byte0 = stream.readUnsignedByte();
			String username = stream.readString();
			String supposed_username = Utils.getCharacterFromByte(byte0) + username;
			//player.getPackets().sendGameMessage(String.format("byte0: %s, name: %s", unknownByte0, name));
			player.getFriendsIgnores().removeIgnore(supposed_username);
		} else if (packetId == SEND_FRIEND_MESSAGE_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getPlayerDetails().getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
				return;
			}
			String username = stream.readString();
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null)
				return;

			player.getFriendsIgnores().sendMessage(p2, Utils.fixChatMessage(Huffman.readEncryptedMessage(150, stream)));
//		} else if (packetId == SEND_FRIEND_QUICK_CHAT_PACKET) {
//			if (!player.hasStarted())
//				return;
//			String username = stream.readString();
//			int fileId = stream.readUnsignedShort();
//			byte[] data = null;
//			if (length > 3 + username.length()) {
//				data = new byte[length - (3 + username.length())];
//				stream.readBytes(data);
//			}
//			data = Utils.completeQuickMessage(player, fileId, data);
//			Player p2 = World.getPlayerByDisplayName(username);
//			if (p2 == null)
//				return;
//			player.getFriendsIgnores().sendQuickChatMessage(p2, new QuickChatMessage(fileId, data));
//		} else if (packetId == PUBLIC_QUICK_CHAT_PACKET) {
//			if (!player.hasStarted())
//				return;
//			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
//				return;
//			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
//			// just tells you which client script created packet
//			@SuppressWarnings("unused")
//			boolean secondClientScript = stream.readByte() == 1;// script 5059
//			// or 5061
//			int fileId = stream.readUnsignedShort();
//			byte[] data = null;
//			if (length > 3) {
//				data = new byte[length - 3];
//				stream.readBytes(data);
//			}
//			data = Utils.completeQuickMessage(player, fileId, data);
//			if (chatType == 0)
//				player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
//			else if (chatType == 1)
//				player.sendFriendsChannelQuickMessage(new QuickChatMessage(fileId, data));
//			else if (Settings.DEBUG)
//				Logger.log(this, "Unknown chat type: " + chatType);
		} else if (packetId == CHAT_TYPE_PACKET) {
			chatType = stream.readUnsignedByte();
		} else if (packetId == CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			int moveEffect = stream.readUnsignedByte();
			String message = Huffman.readEncryptedMessage(200, stream);
			if (message == null || message.replaceAll(" ", "").equals(""))
				return;
			if (message.startsWith("::") || message.startsWith(";;")) {
				// if command exists and processed wont send message as public
				// message
				CommandDispatcher.processCommand(player, message.replace("::", "").replace(";;", ""), false, false);
				return;
			}
			if (player.getPlayerDetails().getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
				return;
			}
			int effects = (colorEffect << 8) | (moveEffect & 0xff);
			if (chatType == 1)
				player.sendFriendsChannelMessage(Utils.fixChatMessage(message));
			else
				player.sendPublicChatMessage(new PublicChatMessage(Utils.fixChatMessage(message), effects));
			if (Settings.DEBUG)
				Logger.log(this, "Chat type: " + chatType);
		} else if (packetId == COMMANDS_PACKET) {
			if (!player.isActive())
				return;
			boolean clientCommand = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			boolean unknown = stream.readUnsignedByte() == 1;
			String command = stream.readString();
			if (!CommandDispatcher.processCommand(player, command, true, clientCommand) && Settings.DEBUG)
				Logger.log(this, "Command: " + command);
		} else if (packetId == COLOR_ID_PACKET) {
			if (!player.hasStarted())
				return;
			int colorId = stream.readUnsignedShort();
			if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null)
				SkillCapeCustomizer.handleSkillCapeCustomizerColor(player, colorId);
		} 
		else if (packetId == GROUND_ITEM_EXAMINE){
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			final int id = stream.readShortLE128();
			boolean forceRun =  stream.readUnsignedByteC() == 1; 
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShort128();
			if (forceRun)
				player.setRun(true);
			final WorldTile tile = new WorldTile(x, y, player.getHeight());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id,
					tile, player);
			if (item == null)
				return;
			player.stopAll(false);
			final FloorItem floorItem = World.getRegion(regionId)
					.getGroundItem(id, tile, player);
			if (floorItem == null)
				return;
			player.getPackets().sendGameMessage("examined floor item");
		}
		else if (packetId == REPORT_ABUSE_PACKET) {
			if (!player.hasStarted())
				return;
			int byte0 = stream.readUnsignedByte(); //correlating to first letter? keystroke > alphabet/numeric letter??
			String remaining_username = stream.readString(); //username missing the first letter, i.e when reporting "jordan" this will return "ordan"
			int byte2 = stream.readUnsignedByte(); //mute type i.e "bug abuse", "scamming"
			int byte3 = stream.readUnsignedByte(); // this is 1 when mute and 0 when no mute
			String string2 = stream.readString(); //doesn't return anything... empty.. ""
			
			String supposed_username = Utils.getCharacterFromByte(byte0) + remaining_username;
			
			System.out.println(String.format("b1: %s, string1: %s, b2: %s, b3: %s, string2: %s", byte0, remaining_username, byte2, byte3, string2));
			player.getPackets().sendGameMessage(supposed_username+"  "+ String.format("b1: %s, string1: %s, b2: %s, b3: %s, string2: %s", byte0, remaining_username, byte2, byte3, string2));
			/*@SuppressWarnings("unused")
			String username = stream.readString();
			@SuppressWarnings("unused")
			int type = stream.readUnsignedByte();
			@SuppressWarnings("unused")
			boolean mute = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			String unknown2 = stream.readString();*/
		} 
		
		else if (packetId == KEY_PRESSED_PACKET){
			@SuppressWarnings("unused")
			int short0 = stream.readUnsignedShort();
			// Can utilize this packet to Close interfaces, open URLS based on key press, such.
//			player.getPackets().sendGameMessage("pressed: "+Utils.getKeyPressedFromListenerByte(short0));
			switch (short0) {
				case 3328:
					player.closeInterfaces();
			}
		}
		else if (packetId ==  INACTIVITY_PACKET){
			//player.getPackets().sendGameMessage("sent inactivity packet!");
		}
		
		else {
//			if (Settings.DEBUG)
//				Logger.log(this, "Missing packet " + packetId + ", expected size: " + length + ", actual size: " + PACKET_SIZES[packetId]);
		}
	}

}
