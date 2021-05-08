package com.rs.game.player;

import java.util.Arrays;

import com.rs.cache.io.OutputStream;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemsEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.utils.Utils;

public class Appearance {

	private transient short renderEmote;
	private byte title;
	private short[] bodyStyle;
	private byte[] bodyColors;
	private boolean male;
	private transient boolean glowRed;
	private transient byte[] appearanceBlock;
	private transient byte[] encryptedAppearenceBlock;
	private transient short transformedNpcId;
	private transient boolean hidePlayer;

	private transient Player player;

	public Appearance() {
		male = true;
		renderEmote = -1;
		title = -1;
		resetAppearence();
	}

	//EradicationX Methods
	public void setLooks(short[] look) {
		for (byte i = 0; i < this.bodyStyle.length; i = (byte) (i + 1))
			if (look[i] != -1)
				this.bodyStyle[i] = look[i];
	}

	public void copyColors(short[] colors) {
		for (byte i = 0; i < this.bodyColors.length; i = (byte) (i + 1))
			if (colors[i] != -1)
				this.bodyColors[i] = (byte) colors[i];
	}

	//End eradication
	public void setGlowRed(boolean glowRed) {
		this.glowRed = glowRed;
		generateAppearenceData();
	}

	public void setPlayer(Player player) {
		this.player = player;
		transformedNpcId = -1;
		renderEmote = -1;
		if (bodyStyle == null)
			resetAppearence();
	}

	public void transformIntoNPC(int id) {
		transformedNpcId = (short) id;
		generateAppearenceData();
	}

	public int getTransformedNPCId() {
		return this.transformedNpcId;
	}

	public void switchHidden() {
		hidePlayer = !hidePlayer;
		generateAppearenceData();
	}

	public boolean isHidden() {
		return hidePlayer;
	}

	public boolean isGlowRed() {
		return glowRed;
	}

	public void generateAppearenceData(){
		OutputStream stream = new OutputStream();
		int flag = 0;

		if (!male)
			flag |= 0x1;

		if (transformedNpcId >= 0 && NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean3190)
			flag |= 0x2;

		String title = male || !male ? null : "";

		boolean titleGoesBeforeName = false;

		if (title != null)
			flag |= titleGoesBeforeName ? 0x40 : 0x80;

		stream.writeByte(flag);

		if (title != null)
			stream.writeGJString(title);

		stream.writeByte(player.hasSkull() ? player.getPlayerDetails().getSkullId() : -1);
		stream.writeByte(player.getPrayer().getPrayerHeadIcon());
		stream.writeByte(hidePlayer ? 1 : 0);

		if (transformedNpcId >= 0) { // PLAYER IS INSTANCEOF NPC
			stream.writeShort(-1); // 65535 tells it a npc
			stream.writeShort(transformedNpcId);
			stream.writeByte(0);
		}

		else {
			int model;
			for (int index = 0; index < 4; index++) {
				model = player.getEquipment().getItem(index) == null ? -1 : player.getEquipment().getItem(index).getId();
				if (model == -1)
					stream.writeByte(0);
				else
					stream.writeShort(16384 + model);
			}

			model =  player.getEquipment().getChestId();
			stream.writeShort(model == -1 ? 0x100 + getTopStyle() : 16384 + model);

			model =  player.getEquipment().getShieldId();
			if (model == -1)
				stream.writeByte(0);
			else
				stream.writeShort(16384 + model);

			model =  player.getEquipment().getChestId();
			stream.writeShort(0x100 + getArmsStyle());

			model =  player.getEquipment().getLegsId();
			stream.writeShort(model == -1 ? 0x100 + getLegsStyle() : 16384 + model);

			model =  player.getEquipment().getHatId();
			stream.writeShort(0x100 + getHairStyle());//(hairBit == 4 ? male ? 5 : 49 : getHair()));


			model =  player.getEquipment().getGlovesId();
			stream.writeShort(model == -1 ? 0x100 + getWristsStyle() : 16384 + model);

			model =  player.getEquipment().getBootsId();
			stream.writeShort(model == -1 ? 0x100 + getShoeStyle() : 16384 + model);

			model = player.getEquipment().getHatId();
			stream.writeShort(0x100 + (male ? getBeardStyle() : -1));//(beardBit == 4 ? (male ? 14 : -1) : (male ? getBeard() : -1)));

			model = player.getEquipment().getAuraId();
			if (model == -1)
				stream.writeByte(0);
			else
				stream.writeShort(16384 + model);

			int pos = stream.getOffset();
			stream.writeShort(0);
			int hash = 0;
			int slotFlag = -1;
			int[] milestone_cape_color = new int[] { 45758, 13434, 13434, 45758 };
			for (int slotId = 0; slotId < player.getEquipment().getItems().getSize(); slotId++) {
				if (Equipment.DISABLED_SLOTS[slotId] != 0)
					continue;
				slotFlag++;
				model = player.getEquipment().getItem(slotId) == null ? -1 : player.getEquipment().getItem(slotId).getId();
				boolean costume = false;//model != -1 && player.getCosmeticsManager().isCostume(slotId, model);
				int costumeColor = 0;//player.getInt(Key.COSTUME_COLOR);
				@SuppressWarnings("unused")
				ItemDefinitions defs = ItemDefinitions.getItemDefinitions(model);
				if (costume){
					hash |= 1 << slotFlag;
					stream.writeByte(0x4);
					int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
					stream.writeShort(slots);
					for (int i = 0; i < 4; i++)
						stream.writeShort(costumeColor);
				}
				else
					switch (slotId){
						case Equipment.SLOT_HAT:
							switch (model){
								case 20768:
								case 20770:
								case 20771:
									hash |= 1 << slotFlag;
									stream.writeByte(0x4);
									int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
									stream.writeShort(slots);
									for (int i = 0; i < 4; i++)
										stream.writeShort(milestone_cape_color[i]);
									break;
							}
							break;
						case Equipment.SLOT_CAPE:
							switch (model){
								case 20767:
								case 20769:
								case 20771:
									hash |= 1 << slotFlag;
									stream.writeByte(0x4);
									int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
									stream.writeShort(slots);
									for (int i = 0; i < 4; i++)
										stream.writeShort(milestone_cape_color[i]);
									break;
								case 6570:
									//	stream.writeShort(420);
									break;
								case 20708:
//						ClansManager manager = player.getClanManager();
//						if (manager == null)
//							continue;
//						int[] colors = manager.getClan().getMottifColors();
//						defs = ItemDefinitions.getItemDefinitions(20709);
//						boolean modifyColor = !Arrays.equals(colors,
//								defs.originalModelColors);
//						int bottomMotif = manager.getClan().getMottifBottom();
//						int topMotif = manager.getClan().getMottifTop();
//						if (bottomMotif == 0 && topMotif == 0 && !modifyColor)
//							continue;
//						hash |= 1 << slotFlag;
//						stream.writeByte((modifyColor ? 0x4 : 0)
//								| (bottomMotif != 0 || topMotif != 0 ? 0x8 : 0));
//						if (modifyColor) {
//							slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
//							stream.writeShort(slots);
//							for (int i = 0; i < 4; i++)
//								stream.writeShort(colors[i]);
//						}
//						if (bottomMotif != 0 || topMotif != 0) {
//							slots = 0 | 1 << 4;
//							stream.writeByte(slots);
//							stream.writeShort(ClansManager
//									.getMottifTexture(topMotif));
//							stream.writeShort(ClansManager
//									.getMottifTexture(bottomMotif));
//						}
									break;
							}
							break;
						case Equipment.SLOT_WEAPON:
							switch (model){
								case 20709:
//						ClansManager manager = player.getClanManager();
//						if (manager == null)
//							continue;
//						int[] colors = manager.getClan().getMottifColors();
//						defs = ItemDefinitions.getItemDefinitions(20709);
//						boolean modifyColor = !Arrays.equals(colors,
//								defs.originalModelColors);
//						int bottomMotif = manager.getClan().getMottifBottom();
//						int topMotif = manager.getClan().getMottifTop();
//						if (bottomMotif == 0 && topMotif == 0 && !modifyColor)
//							continue;
//						hash |= 1 << slotFlag;
//						stream.writeByte((modifyColor ? 0x4 : 0)
//								| (bottomMotif != 0 || topMotif != 0 ? 0x8 : 0));
//						if (modifyColor) {
//							int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
//							stream.writeShort(slots);
//							for (int i = 0; i < 4; i++)
//								stream.writeShort(colors[i]);
//						}
//						if (bottomMotif != 0 || topMotif != 0) {
//							int slots = 0 | 1 << 4;
//							stream.writeByte(slots);
//							stream.writeShort(ClansManager
//									.getMottifTexture(topMotif));
//							stream.writeShort(ClansManager
//									.getMottifTexture(bottomMotif));
//						}
									break;
							}
							break;
						case Equipment.SLOT_AURA:
							int auraId = player.getEquipment().getAuraId();
							if (auraId == -1 || !player.getAuraManager().isActivated())
								continue;
							ItemDefinitions auraDefs = ItemDefinitions.getItemDefinitions(auraId);
							if (auraDefs.getMaleWornModelId1() == -1 || auraDefs.getFemaleWornModelId1() == -1)
								continue;
							hash |= 1 << slotFlag;
							stream.writeByte(0x1); // modify model ids
							int modelId = player.getAuraManager().getAuraModelId();
							stream.writeBigSmart(modelId); // male modelid1
							stream.writeBigSmart(modelId); // female modelid1
							if (auraDefs.getMaleWornModelId2() != -1 || auraDefs.getFemaleWornModelId2() != -1) {
								int modelId2 = player.getAuraManager().getAuraModelId2();
								stream.writeBigSmart(modelId2);
								stream.writeBigSmart(modelId2);
							}
							break;
					}
			}
			int pos2 = stream.getOffset();
			stream.setOffset(pos);
			stream.writeShort(hash);
			stream.setOffset(pos2);
		}

		for (int index = 0; index < bodyColors.length; index++)
			stream.writeByte(bodyColors[index]);

		int player_render_emote = getRenderEmote();

		player.getPackets().sendGlobalConfig(779, player_render_emote); //sets player model render emote on interfaces

		stream.writeShort(player_render_emote);

		stream.writeString(player.getDisplayName());

		boolean inWilderness = false;

		stream.writeByte(inWilderness ? 126 : 138);
		stream.writeByte(138);
		stream.writeByte(inWilderness ? 23 : -1); //wilderness level

		stream.writeByte(transformedNpcId >= 0 ? 1 : 0);

		if (transformedNpcId >= 0) {
			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
			stream.writeShort(defs.anInt876);
			stream.writeShort(defs.anInt842);
			stream.writeShort(defs.anInt884);
			stream.writeShort(defs.anInt875);
			stream.writeByte(defs.anInt875);
		}

		byte[] appeareanceData = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
		this.appearanceBlock = appeareanceData;
		encryptedAppearenceBlock = md5Hash;

	}

	public void generateAppearenceDataMiddle() {
		OutputStream stream = new OutputStream();
		int flag = 0;
		if (!male)
			flag |= 0x1;
		if (transformedNpcId >= 0 && NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean3190)
			flag |= 0x2;
		if(title != 0)
			flag |= title >= 32 && title <= 37 ? 0x80 : 0x40; //after/before
		stream.writeByte(flag);
		if(title != 0) {
			String titleName = title == 666 ? "<col=C12006>Phantom </col>" : ClientScriptMap.getMap(male ? 1093 : 3872).getStringValue(title);
			stream.writeGJString(titleName);
		}
		stream.writeByte(player.hasSkull() ? player.getPlayerDetails().getSkullId() : -1); // pk// icon
		stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
		stream.writeByte(hidePlayer ? 1 : 0);
		// npc
		if (transformedNpcId >= 0) {
			stream.writeShort(-1); // 65535 tells it a npc
			stream.writeShort(transformedNpcId);
			stream.writeByte(0);
		} else {
			for (int index = 0; index < 4; index++) {
				Item item = player.getEquipment().getItems().get(index);
				if (glowRed) {
					if (index == 0) {
						stream.writeShort(32768 + ItemsEquipIds.getEquipId(2910));
						continue;
					}
					if (index == 1) {
						stream.writeShort(32768 + ItemsEquipIds.getEquipId(14641));
						continue;
					}
				}
				if (item == null)
					stream.writeByte(0);
				else
					stream.writeShort(32768 + item.getEquipId());
			}
			Item item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			stream.writeShort(item == null ? 0x100 + bodyStyle[2] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);

			if (item == null)
				stream.writeByte(0);
			else
				stream.writeShort(32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);

			if (item == null || !Equipment.hideArms(item))
				stream.writeShort(0x100 + bodyStyle[3]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2908) : item == null ? 0x100 + bodyStyle[5] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
			if (!glowRed && (item == null || !Equipment.hideHair(item)))
				stream.writeShort(0x100 + bodyStyle[0]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2912) : item == null ? 0x100 + bodyStyle[4] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2904) : item == null ? 0x100 + bodyStyle[6] : 32768 + item.getEquipId());
			//tits for female, bear for male
			item = player.getEquipment().getItems().get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
			if (item == null || (male && Equipment.showBear(item)))
				stream.writeShort(0x100 + bodyStyle[1]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_AURA);
			if (item == null)
				stream.writeByte(0);
			else
				stream.writeShort(32768 + item.getEquipId()); //Fixes the winged auras lookIing fucked.
			int pos = stream.getOffset();
			stream.writeShort(0);
			int hash = 0;
			int slotFlag = -1;
			for (int slotId = 0; slotId < player.getEquipment().getItems().getSize(); slotId++) {
				if (Equipment.DISABLED_SLOTS[slotId] != 0)
					continue;
				slotFlag++;
				if(slotId == Equipment.SLOT_HAT) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == 20768 || hatId == 20770 || hatId == 20772) {
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(hatId-1);
						if ((hatId == 20768	&& Arrays.equals(player.getPlayerDetails().getMaxedCapeCustomized(), defs.originalModelColors) || ((hatId == 20770 || hatId == 20772)
								&& Arrays.equals(player.getPlayerDetails().getCompletionistCapeCustomized(), defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] hat = hatId == 20768 ? player.getPlayerDetails().getMaxedCapeCustomized() : player.getPlayerDetails().getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(hat[i]);
					}
				}else if (slotId == Equipment.SLOT_CAPE) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(capeId);
						if ((capeId == 20767 && Arrays.equals(player.getPlayerDetails().getMaxedCapeCustomized(), defs.originalModelColors) || ((capeId == 20769 || capeId == 20771)
								&& Arrays.equals(player.getPlayerDetails().getCompletionistCapeCustomized(), defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] cape = capeId == 20767 ? player.getPlayerDetails().getMaxedCapeCustomized() : player.getPlayerDetails().getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(cape[i]);
					}
				} else if (slotId == Equipment.SLOT_AURA) {
					int auraId = player.getEquipment().getAuraId();
					if (auraId == -1 || !player.getAuraManager().isActivated())
						continue;
					ItemDefinitions auraDefs = ItemDefinitions.getItemDefinitions(auraId);
					if(auraDefs.getMaleWornModelId1() == -1 || auraDefs.getFemaleWornModelId1() == -1)
						continue;
					hash |= 1 << slotFlag;
					stream.writeByte(0x1); // modify model ids
					int modelId = player.getAuraManager().getAuraModelId();
					stream.writeBigSmart(modelId); // male modelid1
					stream.writeBigSmart(modelId); // female modelid1
					if(auraDefs.getMaleWornModelId2() != -1 || auraDefs.getFemaleWornModelId2() != -1) {
						int modelId2 = player.getAuraManager().getAuraModelId2();
						stream.writeBigSmart(modelId2);
						stream.writeBigSmart(modelId2);
					}
				}
			}
			int pos2 = stream.getOffset();
			stream.setOffset(pos);
			stream.writeShort(hash);
			stream.setOffset(pos2);
		}

		for (int index = 0; index < bodyColors.length; index++)
			// colour length 10
			stream.writeByte(bodyColors[index]);

		stream.writeShort(getRenderEmote());
		stream.writeString(player.getDisplayName());
		boolean pvpArea = World.isPvpArea(player);
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);
		stream.writeByte(-1); // higher level acc name appears in front :P
		stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
		// need to send more
		// data
		if (transformedNpcId >= 0) {
			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
			stream.writeShort(defs.anInt876);
			stream.writeShort(defs.anInt842);
			stream.writeShort(defs.anInt884);
			stream.writeShort(defs.anInt875);
			stream.writeByte(defs.anInt875);
		}

		// done separated for safe because of synchronization
		byte[] appeareanceData = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
		this.appearanceBlock = appeareanceData;
		encryptedAppearenceBlock = md5Hash;
	}

	public void generateAppearenceDataUNKOWN() {
		OutputStream stream = new OutputStream();
		int flag = 0;
		if (!male)
			flag |= 0x1;
		if (transformedNpcId >= 0 && NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean3190)
			flag |= 0x2;
		if (title != 0)
			flag |= title >= 32 && title <= 37 ? 0x80 : 0x40; //after/before
		stream.writeByte(flag);
		if (title != 0) {
			String titleName = title == 666 ? "<col=C12006>Phantom </col>" : ClientScriptMap.getMap(male ? 1093 : 3872).getStringValue(title);
			stream.writeGJString(titleName);
		}
		stream.writeByte(player.hasSkull() ? player.getPlayerDetails().getSkullId() : -1); // pk// icon
		stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
		stream.writeByte(hidePlayer ? 1 : 0);
		// npc
		if (transformedNpcId >= 0) {
			stream.writeShort(-1); // 65535 tells it a npc
			stream.writeShort(transformedNpcId);
			stream.writeByte(0);
		} else {
			for (int index = 0; index < 4; index++) {
				Item item = player.getEquipment().getItems().get(index);
				if (glowRed) {
					if (index == 0) {
						stream.writeShort(32768 + ItemsEquipIds.getEquipId(2910));
						continue;
					}
					if (index == 1) {
						stream.writeShort(32768 + ItemsEquipIds.getEquipId(14641));
						continue;
					}
				}
				if (item == null)
					stream.writeByte(0);
				else
					stream.writeShort(32768 + item.getEquipId());
			}
			Item item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			stream.writeShort(item == null ? 0x100 + bodyStyle[2] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);

			if (item == null)
				stream.writeByte(0);
			else
				stream.writeShort(32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);

			if (item == null || !Equipment.hideArms(item))
				stream.writeShort(0x100 + bodyStyle[3]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2908) : item == null ? 0x100 + bodyStyle[5] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
			if (!glowRed && (item == null || !Equipment.hideHair(item)))
				stream.writeShort(0x100 + bodyStyle[0]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2912) : item == null ? 0x100 + bodyStyle[4] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2904) : item == null ? 0x100 + bodyStyle[6] : 32768 + item.getEquipId());
			//tits for female, bear for male
			item = player.getEquipment().getItems().get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
			if (item == null || (male && Equipment.showBear(item)))
				stream.writeShort(0x100 + bodyStyle[1]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_AURA);
			if (item == null)
				stream.writeByte(0);
			else
				stream.writeShort(32768 + item.getEquipId()); //Fixes the winged auras lookIing fucked.
			int pos = stream.getOffset();
			stream.writeShort(0);
			int hash = 0;
			int slotFlag = -1;
			for (int slotId = 0; slotId < player.getEquipment().getItems().getSize(); slotId++) {
				if (Equipment.DISABLED_SLOTS[slotId] != 0)
					continue;
				slotFlag++;
				if (slotId == Equipment.SLOT_HAT) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == 20768 || hatId == 20770 || hatId == 20772) {
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(hatId - 1);
						if ((hatId == 20768 && Arrays.equals(player.getPlayerDetails().getMaxedCapeCustomized(), defs.originalModelColors) || ((hatId == 20770 || hatId == 20772)
								&& Arrays.equals(player.getPlayerDetails().getCompletionistCapeCustomized(), defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] hat = hatId == 20768 ? player.getPlayerDetails().getMaxedCapeCustomized() : player.getPlayerDetails().getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(hat[i]);
					}
				} else if (slotId == Equipment.SLOT_CAPE) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(capeId);
						if ((capeId == 20767 && Arrays.equals(player.getPlayerDetails().getMaxedCapeCustomized(), defs.originalModelColors) || ((capeId == 20769 || capeId == 20771)
								&& Arrays.equals(player.getPlayerDetails().getCompletionistCapeCustomized(), defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] cape = capeId == 20767 ? player.getPlayerDetails().getMaxedCapeCustomized() : player.getPlayerDetails().getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(cape[i]);
					}
				} else if (slotId == Equipment.SLOT_AURA) {
					int auraId = player.getEquipment().getAuraId();
					if (auraId == -1 || !player.getAuraManager().isActivated())
						continue;
					ItemDefinitions auraDefs = ItemDefinitions.getItemDefinitions(auraId);
					if (auraDefs.getMaleWornModelId1() == -1 || auraDefs.getFemaleWornModelId1() == -1)
						continue;
					hash |= 1 << slotFlag;
					stream.writeByte(0x1); // modify model ids
					int modelId = player.getAuraManager().getAuraModelId();
					stream.writeBigSmart(modelId); // male modelid1
					stream.writeBigSmart(modelId); // female modelid1
					if (auraDefs.getMaleWornModelId2() != -1 || auraDefs.getFemaleWornModelId2() != -1) {
						int modelId2 = player.getAuraManager().getAuraModelId2();
						stream.writeBigSmart(modelId2);
						stream.writeBigSmart(modelId2);
					}
				}
			}
			int pos2 = stream.getOffset();
			stream.setOffset(pos);
			stream.writeShort(hash);
			stream.setOffset(pos2);
		}

		for (int index = 0; index < bodyColors.length; index++)
			// colour length 10
			stream.writeByte(bodyColors[index]);

		stream.writeShort(getRenderEmote());
		stream.writeString(player.getDisplayName());
		boolean pvpArea = World.isPvpArea(player);
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);
		stream.writeByte(-1); // higher level acc name appears in front :P
		stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
		// need to send more
		// data
		if (transformedNpcId >= 0) {
			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
			stream.writeShort(defs.anInt876);
			stream.writeShort(defs.anInt842);
			stream.writeShort(defs.anInt884);
			stream.writeShort(defs.anInt875);
			stream.writeByte(defs.anInt875);
		}

		// done separated for safe because of synchronization
		byte[] appeareanceData = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
		this.appearanceBlock = appeareanceData;
		encryptedAppearenceBlock = md5Hash;
	}

	public int getSize() {
		if (transformedNpcId >= 0)
			return NPCDefinitions.getNPCDefinitions(transformedNpcId).size;
		return 1;
	}

	public void setRenderEmote(short id) {
		this.renderEmote = id;
		generateAppearenceData();
	}

	public int getRenderEmote() {
		if (renderEmote >= 0)
			return renderEmote;
		if (transformedNpcId >= 0)
			return NPCDefinitions.getNPCDefinitions(transformedNpcId).renderEmote;
		return player.getEquipment().getWeaponRenderEmote();
	}

	public void resetAppearence() {
		bodyStyle = new short[7];
		bodyColors = new byte[10];
		setMale();
	}

	public void setMale() {
		bodyStyle[0] = 3; // Hair
		bodyStyle[1] = 14; // Beard
		bodyStyle[2] = 18; // Torso
		bodyStyle[3] = 26; // Arms
		bodyStyle[4] = 34; // Bracelets
		bodyStyle[5] = 38; // Legs
		bodyStyle[6] = 42; // Shoes~

		bodyColors[2] = 16;
		bodyColors[1] = 16;
		bodyColors[0] = 3;
		male = true;
	}

	public void female() {
		bodyStyle[0] = 48; // Hair
		bodyStyle[1] = 57; // Beard
		bodyStyle[2] = 57; // Torso
		bodyStyle[3] = 65; // Arms
		bodyStyle[4] = 68; // Bracelets
		bodyStyle[5] = 77; // Legs
		bodyStyle[6] = 80; // Shoes

		bodyColors[2] = 16;
		bodyColors[1] = 16;
		bodyColors[0] = 3;
		male = false;
	}

	public byte[] getAppeareanceBlocks() {
		return appearanceBlock;
	}

	public byte[] getMD5AppeareanceDataHash() {
		return encryptedAppearenceBlock;
	}

	public boolean isMale() {
		return male;
	}

	public void setBodyStyle(int i, short i2) {
		bodyStyle[i] = i2;
	}

	public void setAppearanceBlocks(int i, int i2) {
		appearanceBlock[i] = (byte) i2;
	}

	public void setBodyColor(int i, int i2) {
		bodyColors[i] = (byte) i2;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public void setHairStyle(short i) {
		bodyStyle[0] = i;
	}

	public void setTopStyle(short i) {
		bodyStyle[2] = i;
	}

	public int getTopStyle() {
		return bodyStyle[2];
	}


	public void setArmsStyle(short i) {
		bodyStyle[3] = i;
	}

	public int getArmsStyle() {
		return bodyStyle[3];
	}

	public void setWristsStyle(short i) {
		bodyStyle[4] = i;
	}

	public int getWristsStyle() {
		return bodyStyle[4];
	}

	public void setLegsStyle(short i) {
		bodyStyle[5] = i;
	}

	public int getLegsStyle() {
		return bodyStyle[5];
	}

	public int getHairStyle() {
		return bodyStyle[0];
	}

	public void setBeardStyle(byte i) {
		bodyStyle[1] = i;
	}

	public int getBeardStyle() {
		return bodyStyle[1];
	}

	public void setFacialHair(byte i) {
		bodyStyle[1] = i;
	}

	public short getFacialHair() {
		return bodyStyle[1];
	}

	public void setSkinColor(int color) {
		bodyColors[4] = (byte) color;
	}

	public int getSkinColor() {
		return bodyColors[4];
	}

	public void setHairColor(int color) {
		bodyColors[0] = (byte) color;
	}

	public void setTopColor(int color) {
		bodyColors[1] = (byte) color;
	}

	public void setLegsColor(int color) {
		bodyColors[2] = (byte) color;
	}

	public byte getHairColor() {
		return bodyColors[0];
	}

	public byte[] getBodyColors() {
		return bodyColors;
	}


	public void setShoeStyle(short i) {
		bodyStyle[6] = i;
	}

	public short getShoeStyle() {
		return bodyStyle[6];
	}

	public short[] getBodyStyles() {
		return bodyStyle;
	}

	public void setTitle(byte title) {
		this.title = title;
		generateAppearenceData();
	}
}