package com.rs.game.minigames;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Inventory;
import com.rs.game.player.actions.Summoning.Pouches;
import com.rs.game.player.content.Foods.Food;
import com.rs.game.player.content.Pots;
import com.rs.game.player.controlers.Controler;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

/**
 * A controler used for the Refuge of Fear minigame.
 * @author Apache ah64
 * @author Emperor
 */
public final class RefugeOfFear extends Controler {

	/**
	 * The random instance, used for randomizing values.
	 */
	private static final Random RANDOM = new Random();

	/**
	 * The spawn coordinates for the 'lower' bosses..
	 */
	private final WorldTile[] spawnCoordinates = new WorldTile[4];

	/**
	 * The npcs.
	 */
	private final ArrayList<NPC> npcs = new ArrayList<NPC>();

	/**
	 * The boundary chunks.
	 */
	private int[] boundChunks;

	/**
	 * The outside area ('lobby') coordinates.
	 */
	public static final WorldTile OUTSIDE_AREA = new WorldTile(3149, 4664, 0);

	/**
	 * The keys used to open the gates for each boss. Fear Power Darkness Death
	 */
	private static final Item[] KEYS = { new Item(13073), new Item(13304), new Item(11043), new Item(11041) };

	/**
	 * A list of spawned world objects, used for cleaning up the dynamic mapregion.
	 */
	private final List<WorldObject> spawnedObjects = new ArrayList<WorldObject>();

	/**
	 * The cutscene used.
	 */
	private String cutscene;

	@Override
	public void start() {
		int itemid = hasFamiliarPouch();
		if (!player.isExtremeDonator()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You have to be an extreme donator to enter this minigame.");
			player.getControlerManager().removeControlerWithoutCheck();
			return;
		} else if (hasFollower()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You cannot take a familiar into Refuge of Fear.");
			player.getControlerManager().removeControlerWithoutCheck();
			return;
		} else if (itemid != -1) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You cannot take "
					+ ItemDefinitions.getItemDefinitions(itemid).getName().toLowerCase() + " into Refuge of Fear.");
			player.getControlerManager().removeControlerWithoutCheck();
			return;
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				boundChunks = RegionBuilder.findEmptyChunkBound(3, 3);
				RegionBuilder.copyAllPlanesMap(392, 580, boundChunks[0], boundChunks[1], 4, 3);
				player.getInterfaceManager().closeMagicBook();
				player.getInterfaceManager().closeEmotes();
				player.setNextWorldTile(getWorldTile(13, 18));
				player.setForceMultiArea(true);
				// Spawn objects.
				WorldTile base = getWorldTile(0, 0);
				/* North-east chamber */
				spawnedObjects.add(new WorldObject(40194, 0, 0, base.getX() + 19, base.getY() + 14, 0));
				spawnedObjects.add(new WorldObject(40186, 0, 0, base.getX() + 19, base.getY() + 13, 0));
				spawnedObjects.add(new WorldObject(40194, 0, 0, base.getX() + 19, base.getY() + 12, 0));
				/* South-east chamber */
				spawnedObjects.add(new WorldObject(40194, 0, 0, base.getX() + 19, base.getY() + 8, 0));
				spawnedObjects.add(new WorldObject(40186, 0, 0, base.getX() + 19, base.getY() + 7, 0));
				spawnedObjects.add(new WorldObject(40194, 0, 0, base.getX() + 19, base.getY() + 6, 0));
				/* North-west chamber */
				spawnedObjects.add(new WorldObject(40194, 0, 2, base.getX() + 7, base.getY() + 14, 0));
				spawnedObjects.add(new WorldObject(40186, 0, 2, base.getX() + 7, base.getY() + 13, 0));
				spawnedObjects.add(new WorldObject(40194, 0, 2, base.getX() + 7, base.getY() + 12, 0));
				/* South-west chamber */
				spawnedObjects.add(new WorldObject(40194, 0, 2, base.getX() + 7, base.getY() + 8, 0));
				spawnedObjects.add(new WorldObject(40186, 0, 2, base.getX() + 7, base.getY() + 7, 0));
				spawnedObjects.add(new WorldObject(40194, 0, 2, base.getX() + 7, base.getY() + 6, 0));
				for (WorldObject object : spawnedObjects) {
					World.spawnObject(object, true);
				}
				spawnBosses();
				World.addGroundItem(KEYS[RANDOM.nextInt(KEYS.length)], getWorldTile(13, 9), player, false, -1, false,
						false, -1);
			}
		}, 50, TimeUnit.MILLISECONDS);
	}

	/**
	 * Checks if the player's inventory contains familiar pouches.
	 * @return The item id.
	 */
	private int hasFamiliarPouch() {
		for (int i = 0; i < 28; i++) {
			Item item = player.getInventory().getItem(i);
			if (item != null && Pouches.forId(item.getId()) != null) {
				return item.getId();
			}
		}
		for (int i = 0; i < 12; i++) {
			Item item = player.getEquipment().getItem(i);
			if (item != null && Pouches.forId(item.getId()) != null) {
				return item.getId();
			}
		}
		// TODO: Pets maybe?
		return -1;
	}

	/**
	 * If the player has a follower.
	 * @return {@code True} if so.
	 */
	private boolean hasFollower() {
		return player.getFamiliar() != null || player.getPet() != null;
	}

	/**
	 * Gets the world tile in the dynamic mapregion with the offsets.
	 * @param mapX The x offset.
	 * @param mapY The y offset.
	 * @return The constructed world tile.
	 */
	public WorldTile getWorldTile(int mapX, int mapY) {
		return new WorldTile((boundChunks[0] << 3) + mapX, (boundChunks[1] << 3) + mapY, 0);
	}

	@Override
	public boolean login() {
		return true;
	}

	@Override
	public boolean logout() {
		endActivity(true);
		player.setLocation(OUTSIDE_AREA);
		return true;
	}

	@Override
	public void forceClose() {
		endActivity(false);
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		Item item = player.getInventory().getItem(slotId);
		if (interfaceId == Inventory.INVENTORY_INTERFACE) {
			if (Food.forId(item.getId()) != null || Pots.getPot(item.getId()) != null) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You cannot eat food or drink potions in this area!");
				return false;
			}
		} else if (interfaceId == 762 && player.getRights() == 0) {
			player.getDialogueManager().startDialogue("SimpleMessage", "The bank cannot be used in Refuge of Fear.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 20573) {
			return false;
		} else if (object.getId() == 20572) {
			endActivity(false);
			return false;
		} else if (object.getId() == 40186) { // Gates to the bosses.
			int keyId;
			String doorType;
			if (object.matches(getWorldTile(19, 13))) { // Master of death.
				keyId = 11041;
				doorType = "stench of death.";
			} else if (object.matches(getWorldTile(19, 7))) { // Master of darkness.
				keyId = 11043;
				doorType = "consuming darkness.";
			} else if (object.matches(getWorldTile(7, 13))) { // Master of power.
				keyId = 13304;
				doorType = "powerful shock.";
			} else if (object.matches(getWorldTile(7, 7))) { // Master of fear.
				keyId = 13073;
				doorType = "overwhelming fear.";
				cutscene = "MasterOfFear";
			} else {
				return true;
			}
			final String finalDoorType = doorType;
			final int finalKey = keyId;
			boolean hasKey = false;
			for (Item key : KEYS) {
				if (player.getInventory().containsItem(key.getId(), 1)) {
					hasKey = true;
					break;
				}
			}
			if (!hasKey) {
				player.getPackets().sendGameMessage("The door is locked.");
				return false;
			}
			player.setNextAnimation(new Animation(881));
			player.getPackets().sendGameMessage("You try to open the rusty lock with the key...");
			CoresManager.fastExecutor.schedule(new TimerTask() {
				@Override
				public void run() {
					if (!player.getInventory().containsItem(finalKey, 1)) {
						player.applyHit(
								new Hit(player, (int) (player.getHitpoints() * 0.15), HitLook.REGULAR_DAMAGE, 15));
						player.getPackets().sendGraphics(new Graphics(1310), object);
						player.getPackets().sendGameMessage(
								"You get hurt by a" + (finalKey == 13073 ? "n " : " ") + finalDoorType);
						return;
					}
					player.getInventory().deleteItem(finalKey, 1);
					player.getPackets().sendGameMessage("You hear a click and the door unlocks.");
					player.getPackets().sendGameMessage(
							"Something about this key protects you against the sudden " + finalDoorType);
					if (cutscene != null) {
						player.getCutscenesManager().play(cutscene);
					}
					object.setRotation(1);
				}
			}, 1200);
			return false;
		}
		return true;
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					endActivity(false);
					player.reset();
					this.stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	/**
	 * Spawns all the lower bosses.
	 */
	public void spawnBosses() {
		WorldTile[] coords = getSpawnCoordinates();
		int i = 0;
		for (Bosses monster : Bosses.values()) {
			NPC npc = new NPC(monster.getId(), coords[i++], -1, false, false);
			npc.setName(monster.getName());
			npc.setCombatLevel((monster.getCombat() + player.getSkills().getCombatLevel() / 2 + RANDOM.nextInt(5)));
			npc.setHitpoints(monster.getConstitution());
			npc.setRandomWalk(false);
			npc.setAtMultiArea(true);
			npcs.add(npc);
		}
		// Not needed anymore. npcs.add(new Wolverine(player, 14899, getWorldTile(13, 9), -1, true));
	}

	/**
	 * Gets the default spawn coordinates for the bosses.
	 * @return The spawn coordinates.
	 */
	private WorldTile[] getSpawnCoordinates() {
		spawnCoordinates[0] = getWorldTile(4, 7);
		spawnCoordinates[1] = getWorldTile(4, 13);
		spawnCoordinates[2] = getWorldTile(22, 7);
		spawnCoordinates[3] = getWorldTile(22, 13);
		return spawnCoordinates;
	}

	/**
	 * Checks if the world tile is located in the Refuge of Fear activity.
	 * @param tile The world tile.
	 * @return {@code True} if so.
	 */
	public static boolean isInRefugeOfFear(WorldTile tile) {
		return tile.getX() >= 3135 && tile.getX() <= 3160 && tile.getY() >= 4640 && tile.getY() <= 4660;
	}

	/**
	 * Removes all the NPCs in this activity.
	 */
	private void removeNPCs() {
		for (NPC npc : npcs) {
			npc.finish();
		}
		npcs.clear();
	}

	/**
	 * Ends the activity.
	 * @param logout If we are logging out.
	 */
	private void endActivity(boolean logout) {
		if (!logout && !player.isDead()) {
			if (player.getAttackedBy() != null) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You cannot escape while you're under attack!");
				return;
			}
		}
		player.setForceMultiArea(false);
		player.getControlerManager().removeControlerWithoutCheck();
		player.setNextWorldTile(OUTSIDE_AREA);
		player.getInterfaceManager().sendMagicBook();
		player.getInterfaceManager().sendEmotes();
		removeNPCs();
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChunks[0], boundChunks[1], 4, 3);
				for (WorldObject object : spawnedObjects) {
					World.removeObject(object, true);
				}
			}
		}, 50, TimeUnit.MILLISECONDS);
	}

	/**
	 * Represents the 'lower' bosses of this activity.
	 * @author Apache ah64
	 */
	public static enum Bosses {

		FEAR_BOSS(15172, 80, 990, 0, "Master of Fear"), POWER_BOSS(15173, 80, 990, 0, "Master of Power"), DARKNESS_BOSS(
				15175, 80, 990, 0, "Master of Darkness"), DEATH_BOSS(15176, 80, 990, 0, "Master of Death");

		private int id;
		private int combat;
		private int constitution;
		private int floor;
		private String name;

		private Bosses(int id, int combat, int constitution, int floor, String name) {
			this.id = id;
			this.combat = combat;
			this.constitution = constitution;
			this.floor = floor;
			this.setName(name);
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getCombat() {
			return combat;
		}

		public void setCombat(int combat) {
			this.combat = combat;
		}

		public int getConstitution() {
			return constitution;
		}

		public void setConstitution(int constitution) {
			this.constitution = constitution;
		}

		public int getFloor() {
			return floor;
		}

		public void setFloor(int floor) {
			this.floor = floor;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}