package com.rs.game.player;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import com.rs.cache.io.OutputStream;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemsEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.utils.Utils;
public class Appearance implements Serializable {

	private static final long serialVersionUID = 7655608569741626591L;

	private transient int renderEmote;
	private int title;
	private int[] bodyStyle;
	private byte[] bodyColors;
	private boolean male;
	private boolean glowRed;
	private byte[] appearanceBlock;
	private byte[] encryptedAppearenceBlock;
	private short transformedNpcId;
	private boolean hidePlayer;

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

		stream.writeByte(player.hasSkull() ? player.getSkullId() : -1);
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
		stream.writeByte(player.hasSkull() ? player.getSkullId() : -1); // pk// icon
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
						if ((hatId == 20768	&& Arrays.equals(player.getMaxedCapeCustomized(), defs.originalModelColors) || ((hatId == 20770 || hatId == 20772)
								&& Arrays.equals(player.getCompletionistCapeCustomized(), defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] hat = hatId == 20768 ? player.getMaxedCapeCustomized() : player.getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(hat[i]);
					}
				}else if (slotId == Equipment.SLOT_CAPE) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(capeId);
						if ((capeId == 20767 && Arrays.equals(player.getMaxedCapeCustomized(), defs.originalModelColors) || ((capeId == 20769 || capeId == 20771)
								&& Arrays.equals(player.getCompletionistCapeCustomized(), defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] cape = capeId == 20767 ? player.getMaxedCapeCustomized() : player.getCompletionistCapeCustomized();
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
		stream.writeByte(player.hasSkull() ? player.getSkullId() : -1); // pk// icon
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
						if ((hatId == 20768 && Arrays.equals(player.getMaxedCapeCustomized(), defs.originalModelColors) || ((hatId == 20770 || hatId == 20772)
								&& Arrays.equals(player.getCompletionistCapeCustomized(), defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] hat = hatId == 20768 ? player.getMaxedCapeCustomized() : player.getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(hat[i]);
					}
				} else if (slotId == Equipment.SLOT_CAPE) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(capeId);
						if ((capeId == 20767 && Arrays.equals(player.getMaxedCapeCustomized(), defs.originalModelColors) || ((capeId == 20769 || capeId == 20771)
								&& Arrays.equals(player.getCompletionistCapeCustomized(), defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] cape = capeId == 20767 ? player.getMaxedCapeCustomized() : player.getCompletionistCapeCustomized();
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

	public void setRenderEmote(int id) {
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
		bodyStyle = new int[7];
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

	public void setBodyStyle(int i, int i2) {
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

	public void setHairStyle(int i) {
		bodyStyle[0] = i;
	}

	public void setTopStyle(int i) {
		bodyStyle[2] = i;
	}

	public int getTopStyle() {
		return bodyStyle[2];
	}


	public void setArmsStyle(int i) {
		bodyStyle[3] = i;
	}

	public int getArmsStyle() {
		return bodyStyle[3];
	}

	public void setWristsStyle(int i) {
		bodyStyle[4] = i;
	}

	public int getWristsStyle() {
		return bodyStyle[4];
	}

	public void setLegsStyle(int i) {
		bodyStyle[5] = i;
	}

	public int getLegsStyle() {
		return bodyStyle[5];
	}

	public int getHairStyle() {
		return bodyStyle[0];
	}

	public void setBeardStyle(int i) {
		bodyStyle[1] = i;
	}

	public int getBeardStyle() {
		return bodyStyle[1];
	}

	public void setFacialHair(int i) {
		bodyStyle[1] = i;
	}

	public int getFacialHair() {
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

	public int getHairColor() {
		return bodyColors[0];
	}

	public byte[] getBodyColors() {
		return bodyColors;
	}


	public void setShoeStyle(int i) {
		bodyStyle[6] = i;
	}

	public int getShoeStyle() {
		return bodyStyle[6];
	}

	public int[] getBodyStyles() {
		return bodyStyle;
	}

	public void setTitle(int title) {
		this.title = title;
		generateAppearenceData();
	}
}
//}
//public class Appearance implements Serializable {
//
//	private static final long serialVersionUID = 7655608569741626586L;
//
//	private transient int renderEmote;
//	private int title;
//	private int[] appearance;
//	private byte[] colour;
//	private boolean isMale;
//
//	/*
//	 * This is used in the Nex combat.
//	 */
//	private transient boolean glowRed;
//
//	private transient byte[] appearanceData;
//	private transient byte[] md5AppeareanceDataHash;
//	private transient short transformedNpcId;
//	private transient boolean hidePlayer;
//
//	private transient static final int[] MALE_HAIR_STYLES = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 185, 187, 189, 191, 193, 195,
//			197, 261, 262, 263, 264, 265, 257, 256, 259, 309, 314, 313, 310, 315, 312, 311, 316 },
//
//			BEARD_STYLES = { 14, 13, 98, 308, 305, 307, 10, 15, 16, 100, 12, 11, 102, 306, 99, 101, 104, 17, 103 },
//
//			FEMALE_HAIR_STYLES = { 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 135, 136, 137, 138, 139, 140, 141, 142, 143,
//					144, 145, 146, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 317, 320, 326, 318, 319,
//					323, 321, 322, 324, 325 };
//
//	private transient Player player;
//
//	public Appearance() {
//		isMale = true;
//		renderEmote = -1;
//		title = -1;
//		resetAppearence();
//	}
//
//	//EradicationX Methods
//	public void setLooks(short[] look) {
//		System.out.println(appearance.length + " : " + look.length);
//		for (byte i = 0; i < this.appearance.length; i = (byte) (i + 1))
//			if (look[i] != -1)
//				this.appearance[i] = look[i];
//	}
//
//	public void copyColors(short[] colors) {
//		for (byte i = 0; i < this.colour.length; i = (byte) (i + 1))
//			if (colors[i] != -1)
//				this.colour[i] = (byte) colors[i];
//	}
//
//	public void setGlowRed(boolean glowRed) {
//		this.glowRed = glowRed;
//		generateAppearanceData();
//	}
//
//	public void setPlayer(Player player) {
//		this.player = player;
//		transformedNpcId = -1;
//		renderEmote = -1;
//		if (appearance == null)
//			randomiseClothes(true);
//			//resetAppearence();
//	}
//
//	public void transformIntoNPC(int id) {
//		transformedNpcId = (short) id;
//		generateAppearanceData();
//	}
//
//	public void switchHidden() {
//		hidePlayer = !hidePlayer;
//		generateAppearanceData();
//	}
//
//	public boolean isHidden() {
//		return hidePlayer;
//	}
//
//	public boolean isGlowRed() {
//		return glowRed;
//	}
//
//	public void generateAppearanceDataNEW(){
//
//		OutputStream stream = new OutputStream();
//
//		int flag = 0;
//
//		if (!isMale)
//			flag |= 0x1;
//
//		if (transformedNpcId >= 0 && NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean3190)
//			flag |= 0x2;
//
//		String title = isMale || !isMale ? null : "";
//
//		boolean titleGoesBeforeName = false;
//
//		if (title != null)
//			flag |= titleGoesBeforeName ? 0x40 : 0x80;
//
//		stream.writeByte(flag);
//
//		if (title != null)
//			stream.writeGJString(title);
//
//		stream.writeByte(player.hasSkull() ? player.getSkullId() : -1);
//		stream.writeByte(player.getPrayer().getPrayerHeadIcon());
//		stream.writeByte(hidePlayer ? 1 : 0);
//
//		if (transformedNpcId >= 0) { // PLAYER IS INSTANCEOF NPC
//			stream.writeShort(-1); // 65535 tells it a npc
//			stream.writeShort(transformedNpcId);
//			stream.writeByte(0);
//		}
//
//		else {
//			int model;
//			for (int index = 0; index < 4; index++) {
//				model = player.getEquipment().getItem(index) == null ? -1 : player.getEquipment().getItem(index).getId();
//					if (model == -1)
//						stream.writeByte(0);
//					else
//						stream.writeShort(16384 + model);
//			}
//
//			model =  player.getEquipment().getChestId();
//			stream.writeShort(model == -1 ? 0x100 + getTop() : 16384 + model);
//
//			model =  player.getEquipment().getShieldId();
//			if (model == -1)
//				stream.writeByte(0);
//			else
//				stream.writeShort(16384 + model);
//
//			model =  player.getEquipment().getChestId();
//			stream.writeShort(0x100 + getSleeves());
//
//			model =  player.getEquipment().getLegsId();
//			stream.writeShort(model == -1 ? 0x100 + getLegs() : 16384 + model);
//
//			model =  player.getEquipment().getHatId();
//			stream.writeShort(0x100 + getHair());//(hairBit == 4 ? male ? 5 : 49 : getHair()));
//
//
//			model =  player.getEquipment().getGlovesId();
//			stream.writeShort(model == -1 ? 0x100 + getWrists() : 16384 + model);
//
//			model =  player.getEquipment().getBootsId();
//			stream.writeShort(model == -1 ? 0x100 + getShoes() : 16384 + model);
//
//			model = player.getEquipment().getHatId();
//			stream.writeShort(0x100 + (isMale ? getBeard() : -1));//(beardBit == 4 ? (male ? 14 : -1) : (male ? getBeard() : -1)));
//
//			model = player.getEquipment().getAuraId();
//			if (model == -1)
//				stream.writeByte(0);
//			else
//				stream.writeShort(16384 + model);
//
//			int pos = stream.getOffset();
//			stream.writeShort(0);
//			int hash = 0;
//			int slotFlag = -1;
//			int[] milestone_cape_color = new int[] { 45758, 13434, 13434, 45758 };
//			for (int slotId = 0; slotId < player.getEquipment().getItems().getSize(); slotId++) {
//				if (Equipment.DISABLED_SLOTS[slotId] != 0)
//					continue;
//				slotFlag++;
//				model = player.getEquipment().getItem(slotId) == null ? -1 : player.getEquipment().getItem(slotId).getId();
//				boolean costume = false;//model != -1 && player.getCosmeticsManager().isCostume(slotId, model);
//				int costumeColor = 0;//player.getInt(Key.COSTUME_COLOR);
//				@SuppressWarnings("unused")
//				ItemDefinitions defs = ItemDefinitions.getItemDefinitions(model);
//				if (costume){
//					hash |= 1 << slotFlag;
//					stream.writeByte(0x4);
//					int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
//					stream.writeShort(slots);
//					for (int i = 0; i < 4; i++)
//						stream.writeShort(costumeColor);
//				}
//				else
//				switch (slotId){
//				case Equipment.SLOT_HAT:
//					switch (model){
//					case 20768:
//					case 20770:
//					case 20771:
//						hash |= 1 << slotFlag;
//						stream.writeByte(0x4);
//						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
//						stream.writeShort(slots);
//						for (int i = 0; i < 4; i++)
//							stream.writeShort(milestone_cape_color[i]);
//					break;
//					}
//				break;
//				case Equipment.SLOT_CAPE:
//					switch (model){
//					case 20767:
//					case 20769:
//					case 20771:
//						hash |= 1 << slotFlag;
//						stream.writeByte(0x4);
//						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
//						stream.writeShort(slots);
//						for (int i = 0; i < 4; i++)
//							stream.writeShort(milestone_cape_color[i]);
//					break;
//					case 6570:
//					//	stream.writeShort(420);
//						break;
//					case 20708:
////						ClansManager manager = player.getClanManager();
////						if (manager == null)
////							continue;
////						int[] colors = manager.getClan().getMottifColors();
////						defs = ItemDefinitions.getItemDefinitions(20709);
////						boolean modifyColor = !Arrays.equals(colors,
////								defs.originalModelColors);
////						int bottomMotif = manager.getClan().getMottifBottom();
////						int topMotif = manager.getClan().getMottifTop();
////						if (bottomMotif == 0 && topMotif == 0 && !modifyColor)
////							continue;
////						hash |= 1 << slotFlag;
////						stream.writeByte((modifyColor ? 0x4 : 0)
////								| (bottomMotif != 0 || topMotif != 0 ? 0x8 : 0));
////						if (modifyColor) {
////							slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
////							stream.writeShort(slots);
////							for (int i = 0; i < 4; i++)
////								stream.writeShort(colors[i]);
////						}
////						if (bottomMotif != 0 || topMotif != 0) {
////							slots = 0 | 1 << 4;
////							stream.writeByte(slots);
////							stream.writeShort(ClansManager
////									.getMottifTexture(topMotif));
////							stream.writeShort(ClansManager
////									.getMottifTexture(bottomMotif));
////						}
//					break;
//					}
//				break;
//				case Equipment.SLOT_WEAPON:
//					switch (model){
//					case 20709:
////						ClansManager manager = player.getClanManager();
////						if (manager == null)
////							continue;
////						int[] colors = manager.getClan().getMottifColors();
////						defs = ItemDefinitions.getItemDefinitions(20709);
////						boolean modifyColor = !Arrays.equals(colors,
////								defs.originalModelColors);
////						int bottomMotif = manager.getClan().getMottifBottom();
////						int topMotif = manager.getClan().getMottifTop();
////						if (bottomMotif == 0 && topMotif == 0 && !modifyColor)
////							continue;
////						hash |= 1 << slotFlag;
////						stream.writeByte((modifyColor ? 0x4 : 0)
////								| (bottomMotif != 0 || topMotif != 0 ? 0x8 : 0));
////						if (modifyColor) {
////							int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
////							stream.writeShort(slots);
////							for (int i = 0; i < 4; i++)
////								stream.writeShort(colors[i]);
////						}
////						if (bottomMotif != 0 || topMotif != 0) {
////							int slots = 0 | 1 << 4;
////							stream.writeByte(slots);
////							stream.writeShort(ClansManager
////									.getMottifTexture(topMotif));
////							stream.writeShort(ClansManager
////									.getMottifTexture(bottomMotif));
////						}
//					break;
//					}
//				break;
//				case Equipment.SLOT_AURA:
//					int auraId = player.getEquipment().getAuraId();
//					if (auraId == -1 || !player.getAuraManager().isActivated())
//						continue;
//					ItemDefinitions auraDefs = ItemDefinitions.getItemDefinitions(auraId);
//					if (auraDefs.getMaleWornModelId1() == -1 || auraDefs.getFemaleWornModelId1() == -1)
//						continue;
//					hash |= 1 << slotFlag;
//					stream.writeByte(0x1); // modify model ids
//					int modelId = player.getAuraManager().getAuraModelId();
//					stream.writeBigSmart(modelId); // male modelid1
//					stream.writeBigSmart(modelId); // female modelid1
//					if (auraDefs.getMaleWornModelId2() != -1 || auraDefs.getFemaleWornModelId2() != -1) {
//						int modelId2 = player.getAuraManager().getAuraModelId2();
//						stream.writeBigSmart(modelId2);
//						stream.writeBigSmart(modelId2);
//					}
//				break;
//				}
//			}
//			int pos2 = stream.getOffset();
//			stream.setOffset(pos);
//			stream.writeShort(hash);
//			stream.setOffset(pos2);
//		}
//
//		for (int index = 0; index < colour.length; index++)
//			stream.writeByte(colour[index]);
//
//		int player_render_emote = getRenderEmote();
//
//		player.getPackets().sendGlobalConfig(779, player_render_emote); //sets player model render emote on interfaces
//
//		stream.writeShort(player_render_emote);
//
//		stream.writeString(player.getDisplayName());
//
//		boolean inWilderness = false;
//
//		stream.writeByte(inWilderness ? 126 : 138);
//		stream.writeByte(138);
//		stream.writeByte(inWilderness ? 23 : -1); //wilderness level
//
//		stream.writeByte(transformedNpcId >= 0 ? 1 : 0);
//
//		if (transformedNpcId >= 0) {
//			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
//			stream.writeShort(defs.anInt876);
//			stream.writeShort(defs.anInt842);
//			stream.writeShort(defs.anInt884);
//			stream.writeShort(defs.anInt875);
//			stream.writeByte(defs.anInt875);
//		}
//
//		byte[] appeareanceData = new byte[stream.getOffset()];
//		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
//		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
//		this.appearanceData = appeareanceData;
//		md5AppeareanceDataHash = md5Hash;
//
//
//
////		OutputStream stream = new OutputStream();
////		int flag = 0;
////		if (!isMale)
////			flag |= 0x1;
////		if (transformedNpcId >= 0 && NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean3190)
////			flag |= 0x2;
////		if (title != 0)
////			flag |= title >= 32 && title <= 37 ? 0x80 : 0x40; // after/before
////		stream.writeByte(flag);
////		if (title != 0) {
////			String titleName = title == 666 ? "<col=C12006>Phantom </col>"
////					: ClientScriptMap.getMap(isMale ? 1093 : 3872).getStringValue(title);
////			stream.writeGJString(titleName);
////		}
////		stream.writeByte(player.hasSkull() ? player.getSkullId() : -1); // pk// icon
////		stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
////		stream.writeByte(hidePlayer ? 1 : 0);
////		// npc
////		if (transformedNpcId >= 0) {
////			stream.writeShort(-1); // 65535 tells it a npc
////			stream.writeShort(transformedNpcId);
////			stream.writeByte(0);
////		} else {
////			for (int index = 0; index < 4; index++) {
////				Item item = player.getEquipment().getItems().get(index);
////				if (glowRed) {
////					if (index == 0) {
////						stream.writeShort(32768 + ItemsEquipIds.getEquipId(2910));
////						continue;
////					}
////					if (index == 1) {
////						stream.writeShort(32768 + ItemsEquipIds.getEquipId(14641));
////						continue;
////					}
////				}
////				if (item == null)
////					stream.writeByte(0);
////				else
////					stream.writeShort(32768 + item.getEquipId());
////			}
////
////			Item item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
////			stream.writeShort(item == null ? 0x100 + getTop() : 32768 + item.getEquipId());
////
////			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);
////			if (item == null)
////				stream.writeByte(0);
////			else
////				stream.writeShort(32768 + item.getEquipId());
////
////			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
////			if (item == null || !Equipment.hideArms(item))
////				stream.writeShort(0x100 + getSleeves());
////			else
////				stream.writeByte(0);
////
////			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
////			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2908)
////					: item == null ? 0x100 + getLegs() : 32768 + item.getEquipId());
////
////			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
////			if (!glowRed && (item == null || !Equipment.hideHair(item)))
////				stream.writeShort(0x100 + getHair());
////			else
////				stream.writeByte(0);
////
////			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
////			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2912)
////					: item == null ? 0x100 + getWrists() : 32768 + item.getEquipId());
////
////			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
////			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2904)
////					: item == null ? 0x100 + getShoes() : 32768 + item.getEquipId());
////			// tits for female, bear for male
////
////
////			item = player.getEquipment().getItems().get(isMale ? Equipment.SLOT_HAT : Equipment.SLOT_HAT);
////			if (isMale && (item == null || (isMale && Equipment.showBear(item))))
////				stream.writeShort(0x100 + getBeard());
////			else
////				stream.writeByte(0);
////			item = player.getEquipment().getItems().get(Equipment.SLOT_AURA);
////			if (item == null)
////				stream.writeByte(0);
////			else
////				stream.writeShort(32768 + item.getEquipId()); // Fixes the winged auras lookIing fucked.
////			int pos = stream.getOffset();
////			stream.writeShort(0);
////			int hash = 0;
////			int slotFlag = -1;
////			for (int slotId = 0; slotId < player.getEquipment().getItems().getSize(); slotId++) {
////				if (Equipment.DISABLED_SLOTS[slotId] != 0)
////					continue;
////				slotFlag++;
////				if (slotId == Equipment.SLOT_HAT) {
////					int hatId = player.getEquipment().getHatId();
////					if (hatId == 20768 || hatId == 20770 || hatId == 20772) {
////						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(hatId - 1);
////						if ((hatId == 20768 && Arrays.equals(player.getMaxedCapeCustomized(), defs.originalModelColors)
////								|| ((hatId == 20770 || hatId == 20772) && Arrays
////								.equals(player.getCompletionistCapeCustomized(), defs.originalModelColors))))
////							continue;
////						hash |= 1 << slotFlag;
////						stream.writeByte(0x4); // modify 4 model colors
////						int[] hat = hatId == 20768 ? player.getMaxedCapeCustomized()
////								: player.getCompletionistCapeCustomized();
////						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
////						stream.writeShort(slots);
////						for (int i = 0; i < 4; i++)
////							stream.writeShort(hat[i]);
////					}
////				} else if (slotId == Equipment.SLOT_CAPE) {
////					int capeId = player.getEquipment().getCapeId();
////					if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
////						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(capeId);
////						if ((capeId == 20767 && Arrays.equals(player.getMaxedCapeCustomized(), defs.originalModelColors)
////								|| ((capeId == 20769 || capeId == 20771) && Arrays
////								.equals(player.getCompletionistCapeCustomized(), defs.originalModelColors))))
////							continue;
////						hash |= 1 << slotFlag;
////						stream.writeByte(0x4); // modify 4 model colors
////						int[] cape = capeId == 20767 ? player.getMaxedCapeCustomized()
////								: player.getCompletionistCapeCustomized();
////						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
////						stream.writeShort(slots);
////						for (int i = 0; i < 4; i++)
////							stream.writeShort(cape[i]);
////					}
////				} else if (slotId == Equipment.SLOT_AURA) {
////					int auraId = player.getEquipment().getAuraId();
////					if (auraId == -1 || !player.getAuraManager().isActivated())
////						continue;
////					ItemDefinitions auraDefs = ItemDefinitions.getItemDefinitions(auraId);
////					if (auraDefs.getMaleWornModelId1() == -1 || auraDefs.getFemaleWornModelId1() == -1)
////						continue;
////					hash |= 1 << slotFlag;
////					stream.writeByte(0x1); // modify model ids
////					int modelId = player.getAuraManager().getAuraModelId();
////					stream.writeBigSmart(modelId); // male modelid1
////					stream.writeBigSmart(modelId); // female modelid1
////					if (auraDefs.getMaleWornModelId2() != -1 || auraDefs.getFemaleWornModelId2() != -1) {
////						int modelId2 = player.getAuraManager().getAuraModelId2();
////						stream.writeBigSmart(modelId2);
////						stream.writeBigSmart(modelId2);
////					}
////				}
////			}
////			int pos2 = stream.getOffset();
////			stream.setOffset(pos);
////			stream.writeShort(hash);
////			stream.setOffset(pos2);
////		}
////
////		for (int index = 0; index < colour.length; index++)
////			// colour length 10
////			stream.writeByte(colour[index]);
////
////		stream.writeShort(getRenderEmote());
////		stream.writeString(player.getDisplayName());
////		boolean pvpArea = World.isPvpArea(player);
////		stream.writeByte(
////				pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
////		stream.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);
////		stream.writeByte(-1); // higher level acc name appears in front :P
////		stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
////		// need to send more
////		// data
////		if (transformedNpcId >= 0) {
////			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
////			stream.writeShort(defs.anInt876);
////			stream.writeShort(defs.anInt842);
////			stream.writeShort(defs.anInt884);
////			stream.writeShort(defs.anInt875);
////			stream.writeByte(defs.anInt875);
////		}
////
////		// done separated for safe because of synchronization
////		byte[] appeareanceData = new byte[stream.getOffset()];
////		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
////		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
////		this.appearanceData = appeareanceData;
////		md5AppeareanceDataHash = md5Hash;
//
//	}
//
//	public void switchGender(){
//		appearance[HAIR] = isMale ? new Random().nextInt(FEMALE_HAIR_STYLES.length) : new Random().nextInt(MALE_HAIR_STYLES.length);
//		appearance[BEARD] = isMale ? 0 : new Random().nextInt(BEARD_STYLES.length);
//		isMale = !isMale;
//	}
//
//	public void generateAppearanceData() {
//		OutputStream stream = new OutputStream();
//		int flag = 0;
//		if (!isMale)
//			flag |= 0x1;
//		if (transformedNpcId >= 0 && NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean3190)
//			flag |= 0x2;
//		if (title != 0)
//			flag |= title >= 32 && title <= 37 ? 0x80 : 0x40; // after/before
//		stream.writeByte(flag);
//		if (title != 0) {
//			String titleName = title == 666 ? "<col=C12006>Phantom </col>"
//					: ClientScriptMap.getMap(isMale ? 1093 : 3872).getStringValue(title);
//			stream.writeGJString(titleName);
//		}
//		stream.writeByte(player.hasSkull() ? player.getSkullId() : -1); // pk// icon
//		stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
//		stream.writeByte(hidePlayer ? 1 : 0);
//		// npc
//		if (transformedNpcId >= 0) {
//			stream.writeShort(-1); // 65535 tells it a npc
//			stream.writeShort(transformedNpcId);
//			stream.writeByte(0);
//		} else {
//			for (int index = 0; index < 4; index++) {
//				Item item = player.getEquipment().getItems().get(index);
//				if (glowRed) {
//					if (index == 0) {
//						stream.writeShort(32768 + ItemsEquipIds.getEquipId(2910));
//						continue;
//					}
//					if (index == 1) {
//						stream.writeShort(32768 + ItemsEquipIds.getEquipId(14641));
//						continue;
//					}
//				}
//				if (item == null)
//					stream.writeByte(0);
//				else
//					stream.writeShort(32768 + item.getEquipId());
//			}
//
//			Item item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
//			stream.writeShort(item == null ? 0x100 + getTop() : 32768 + item.getEquipId());
//
//			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);
//			if (item == null)
//				stream.writeByte(0);
//			else
//				stream.writeShort(32768 + item.getEquipId());
//
//			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
//			if (item == null || !Equipment.hideArms(item))
//				stream.writeShort(0x100 + getSleeves());
//			else
//				stream.writeByte(0);
//
//			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
//			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2908)
//					: item == null ? 0x100 + getLegs() : 32768 + item.getEquipId());
//
//			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
//			if (!glowRed && (item == null || !Equipment.hideHair(item)))
//				stream.writeShort(0x100 + getHair());
//			else
//				stream.writeByte(0);
//
//			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
//			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2912)
//					: item == null ? 0x100 + getWrists() : 32768 + item.getEquipId());
//
//			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
//			stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2904)
//					: item == null ? 0x100 + getShoes() : 32768 + item.getEquipId());
//			// tits for female, bear for male
//
//
//			item = player.getEquipment().getItems().get(isMale ? Equipment.SLOT_HAT : Equipment.SLOT_HAT);
//			if (isMale && (item == null || (isMale && Equipment.showBear(item))))
//				stream.writeShort(0x100 + getBeard());
//			else
//				stream.writeByte(0);
//			item = player.getEquipment().getItems().get(Equipment.SLOT_AURA);
//			if (item == null)
//				stream.writeByte(0);
//			else
//				stream.writeShort(32768 + item.getEquipId()); // Fixes the winged auras lookIing fucked.
//			int pos = stream.getOffset();
//			stream.writeShort(0);
//			int hash = 0;
//			int slotFlag = -1;
//			for (int slotId = 0; slotId < player.getEquipment().getItems().getSize(); slotId++) {
//				if (Equipment.DISABLED_SLOTS[slotId] != 0)
//					continue;
//				slotFlag++;
//				if (slotId == Equipment.SLOT_HAT) {
//					int hatId = player.getEquipment().getHatId();
//					if (hatId == 20768 || hatId == 20770 || hatId == 20772) {
//						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(hatId - 1);
//						if ((hatId == 20768 && Arrays.equals(player.getMaxedCapeCustomized(), defs.originalModelColors)
//								|| ((hatId == 20770 || hatId == 20772) && Arrays
//										.equals(player.getCompletionistCapeCustomized(), defs.originalModelColors))))
//							continue;
//						hash |= 1 << slotFlag;
//						stream.writeByte(0x4); // modify 4 model colors
//						int[] hat = hatId == 20768 ? player.getMaxedCapeCustomized()
//								: player.getCompletionistCapeCustomized();
//						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
//						stream.writeShort(slots);
//						for (int i = 0; i < 4; i++)
//							stream.writeShort(hat[i]);
//					}
//				} else if (slotId == Equipment.SLOT_CAPE) {
//					int capeId = player.getEquipment().getCapeId();
//					if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
//						ItemDefinitions defs = ItemDefinitions.getItemDefinitions(capeId);
//						if ((capeId == 20767 && Arrays.equals(player.getMaxedCapeCustomized(), defs.originalModelColors)
//								|| ((capeId == 20769 || capeId == 20771) && Arrays
//										.equals(player.getCompletionistCapeCustomized(), defs.originalModelColors))))
//							continue;
//						hash |= 1 << slotFlag;
//						stream.writeByte(0x4); // modify 4 model colors
//						int[] cape = capeId == 20767 ? player.getMaxedCapeCustomized()
//								: player.getCompletionistCapeCustomized();
//						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
//						stream.writeShort(slots);
//						for (int i = 0; i < 4; i++)
//							stream.writeShort(cape[i]);
//					}
//				} else if (slotId == Equipment.SLOT_AURA) {
//					int auraId = player.getEquipment().getAuraId();
//					if (auraId == -1 || !player.getAuraManager().isActivated())
//						continue;
//					ItemDefinitions auraDefs = ItemDefinitions.getItemDefinitions(auraId);
//					if (auraDefs.getMaleWornModelId1() == -1 || auraDefs.getFemaleWornModelId1() == -1)
//						continue;
//					hash |= 1 << slotFlag;
//					stream.writeByte(0x1); // modify model ids
//					int modelId = player.getAuraManager().getAuraModelId();
//					stream.writeBigSmart(modelId); // male modelid1
//					stream.writeBigSmart(modelId); // female modelid1
//					if (auraDefs.getMaleWornModelId2() != -1 || auraDefs.getFemaleWornModelId2() != -1) {
//						int modelId2 = player.getAuraManager().getAuraModelId2();
//						stream.writeBigSmart(modelId2);
//						stream.writeBigSmart(modelId2);
//					}
//				}
//			}
//			int pos2 = stream.getOffset();
//			stream.setOffset(pos);
//			stream.writeShort(hash);
//			stream.setOffset(pos2);
//		}
//
//		for (int index = 0; index < colour.length; index++)
//			// colour length 10
//			stream.writeByte(colour[index]);
//
//		stream.writeShort(getRenderEmote());
//		stream.writeString(player.getDisplayName());
//		boolean pvpArea = World.isPvpArea(player);
//		stream.writeByte(
//				pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
//		stream.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);
//		stream.writeByte(-1); // higher level acc name appears in front :P
//		stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
//															// need to send more
//															// data
//		if (transformedNpcId >= 0) {
//			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
//			stream.writeShort(defs.anInt876);
//			stream.writeShort(defs.anInt842);
//			stream.writeShort(defs.anInt884);
//			stream.writeShort(defs.anInt875);
//			stream.writeByte(defs.anInt875);
//		}
//
//		// done separated for safe because of synchronization
//		byte[] appeareanceData = new byte[stream.getOffset()];
//		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
//		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
//		this.appearanceData = appeareanceData;
//		md5AppeareanceDataHash = md5Hash;
//	}
//
//	public int getSize() {
//		if (transformedNpcId >= 0)
//			return NPCDefinitions.getNPCDefinitions(transformedNpcId).size;
//		return 1;
//	}
//
//	public void setRenderEmote(int id) {
//		this.renderEmote = id;
//		generateAppearanceData();
//	}
//
//	public int getRenderEmote() {
//		if (renderEmote >= 0)
//			return renderEmote;
//		if (transformedNpcId >= 0)
//			return NPCDefinitions.getNPCDefinitions(transformedNpcId).renderEmote;
//		return player.getEquipment().getWeaponRenderEmote();
//	}
//
//	public void resetAppearence() {
//		appearance = new int[12];
//		colour = new byte[10];
//		randomiseClothes(true);
//	}
//
//	public void male() {
//		appearance[0] = 3; // Hair
//		appearance[1] = 14; // Beard
//		appearance[2] = 18; // Torso
//		appearance[3] = 26; // Arms
//		appearance[4] = 34; // Bracelets
//		appearance[5] = 38; // Legs
//		appearance[6] = 42; // Shoes~
//
//		colour[2] = 16;
//		colour[1] = 16;
//		colour[0] = 3;
//		isMale = true;
//	}
//
//	public void female() {
//		appearance[0] = 48; // Hair
//		appearance[1] = 57; // Beard
//		appearance[2] = 57; // Torso
//		appearance[3] = 65; // Arms
//		appearance[4] = 68; // Bracelets
//		appearance[5] = 77; // Legs
//		appearance[6] = 80; // Shoes
//
//		colour[2] = 16;
//		colour[1] = 16;
//		colour[0] = 3;
//		isMale = false;
//	}
//
//	public byte[] getAppeareanceData() {
//		return appearanceData;
//	}
//
//	public byte[] getMD5AppeareanceDataHash() {
//		return md5AppeareanceDataHash;
//	}
//
//	public boolean isMale() {
//		return isMale;
//	}
//
//	public void setLook(int i, int i2) {
//		appearance[i] = i2;
//	}
//
//	public void setColor(int i, int i2) {
//		colour[i] = (byte) i2;
//	}
//
//	public void setMale(boolean male) {
//		this.isMale = male;
//	}
//
//	public void setHairStyle(int i) {
//		appearance[0] = i;
//	}
//
//	public void setTopStyle(int i) {
//		appearance[2] = i;
//	}
//
//	public int getTopStyle() {
//		return appearance[2];
//	}
//
//	public void setArmsStyle(int i) {
//		appearance[3] = i;
//	}
//
//	public void setWristsStyle(int i) {
//		appearance[4] = i;
//	}
//
//	public void setLegsStyle(int i) {
//		appearance[5] = i;
//	}
//
//	public int getHairStyle() {
//		return appearance[0];
//	}
//
//	public void setBeardStyle(int i) {
//		appearance[1] = i;
//	}
//
//	public int getBeardStyle() {
//		return appearance[1];
//	}
//
//	public void setFacialHair(int i) {
//		appearance[1] = i;
//	}
//
//	public int getFacialHair() {
//		return appearance[1];
//	}
//
//	public void setSkinColor(int color) {
//		colour[4] = (byte) color;
//	}
//
//	public int getSkinColor() {
//		return colour[4];
//	}
//
//	public void setHairColor(int color) {
//		colour[0] = (byte) color;
//	}
//
//	public void setTopColor(int color) {
//		colour[1] = (byte) color;
//	}
//
//	public void setLegsColor(int color) {
//		colour[2] = (byte) color;
//	}
//
//	public int getHairColor() {
//		return colour[0];
//	}
//
//	public void setTitle(int title) {
//		this.title = title;
//		generateAppearanceData();
//	}
//
//	public static final int HAIR = 0, TOP = 1, LEGS = 2, SHOES = 3, SKIN = 4, BEARD = 5, ARMS = 6, WRISTS = 7;
//
//	public int getHair(){
//		return isMale ? MALE_HAIR_STYLES[appearance[HAIR]] : FEMALE_HAIR_STYLES[appearance[HAIR]];
//	}
//
//	public int getBeard(){
//		return BEARD_STYLES[appearance[BEARD]];
//	}
//
//	public int getTop(){
//		return isMale ? Clothes.ORDINAL_TOPS_1[appearance[TOP]] : Clothes.ORDINAL_TOPS_2[appearance[TOP]];
//	}
//
//	public int getSleeves(){
//		return appearance[TOP] < 32 ? isMale ? Clothes.ORDINAL_VARIENT_ARMS_1[appearance[ARMS]] : Clothes.ORDINAL_VARIENT_ARMS_2[appearance[ARMS]] : isMale? Clothes.ORDINAL_ARMS_1[appearance[ARMS]] : Clothes.ORDINAL_ARMS_2[appearance[ARMS]];
//	}
//
//	public int getWrists(){
//		return appearance[TOP] < 32 ? isMale ? Clothes.ORDINAL_VARIENT_WRISTS_1[appearance[WRISTS]] : Clothes.ORDINAL_VARIENT_WRISTS_2[appearance[WRISTS]]  : isMale ? Clothes.ORDINAL_WRISTS_1[appearance[WRISTS]] : Clothes.ORDINAL_WRISTS_2[appearance[WRISTS]];
//	}
//
//	public int getLegs(){
//		return isMale ? Clothes.ORDINAL_LEGGINGS_1[appearance[LEGS]] : Clothes.ORDINAL_LEGGINGS_2[appearance[LEGS]];
//	}
//
//	public int getShoes(){
//		return isMale? Clothes.ORDINAL_SHOES_1[appearance[SHOES]] : Clothes.ORDINAL_SHOES_2[appearance[SHOES]];
//	}
//
//	public void randomiseClothes(boolean ismale){
//		this.isMale = ismale;
//
//		appearance[HAIR] = new Random().nextInt(ismale ? 32 : 44);//random
//		appearance[BEARD] = new Random().nextInt(19);
//		colour[HAIR] = 25;
//		colour[SKIN] = 3;
//
//		int temp = Utils.random(31);
//		appearance[TOP] = temp;
//		appearance[ARMS] = temp;
//		appearance[WRISTS] = temp;
//		appearance[LEGS] = Utils.random(31);
//		appearance[SHOES] = Utils.random(ismale ? 15 : 16);
//
//		colour[TOP] = (byte) ClientScriptMap.getMap(3282).getIntValue(Utils.random(227));
//		colour[LEGS] = (byte) ClientScriptMap.getMap(3284).getIntValue(Utils.random(227));
//		colour[SHOES] = (byte) ClientScriptMap.getMap(3297).getIntValue(Utils.random(204));
//	}
//}