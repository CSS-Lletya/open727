package org.redrune.game.content.combat.player.registry.wrapper.magic;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.activity.ActivitySystem;
import org.redrune.game.content.activity.impl.pvp.PvPLocation;
import org.redrune.game.content.combat.MagicConstants;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.combat.player.registry.CombatRegistryEvent;
import org.redrune.game.content.combat.player.registry.wrapper.context.MagicSpellContext;
import org.redrune.utility.AttributeKey;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import skills.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public interface TeleportationSpellEvent extends MagicSpellEvent, CombatRegistryEvent, MagicConstants {
	
	/**
	 * The level required to use the spell
	 */
	int levelRequired();
	
	/**
	 * The runes that are required
	 */
	int[] runesRequired();
	
	/**
	 * The destination of the teleportation
	 */
	WorldTile destination();
	
	/**
	 * If the teleport spell should randomize the coordinates to arrive at
	 */
	default boolean randomize() {
		return true;
	}
	
	@Override
	default void cast(Player player, MagicSpellContext context) {
		switch (book()) {
			case REGULAR:
				sendModernTeleport(player, destination(), levelRequired(), exp(), randomize(), runesRequired());
				break;
			case ANCIENTS:
				sendAncientsTeleport(player, destination(), levelRequired(), exp(), randomize(), runesRequired());
				break;
			case LUNARS:
				sendLunarTeleport(player, destination(), levelRequired(), exp(), randomize(), runesRequired());
				break;
		}
	}
	
	/**
	 * Sends a modern teleport spell
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination
	 */
	static void sendModernTeleport(Player player, WorldTile destination, int level, double xp, boolean randomize, int... runes) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, destination, 3, randomize, TeleportType.SPELL, runes);
	}
	
	/**
	 * Sends an ancients teleport spell
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination
	 */
	static void sendAncientsTeleport(Player player, WorldTile destination, int level, double xp, boolean randomize, int... runes) {
		sendTeleportSpell(player, 1979, -1, 1681, -1, level, xp, destination, 5, randomize, TeleportType.SPELL, runes);
	}
	
	/**
	 * Sends a lever teleportation
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination of the teleportation
	 */
	static void sendLeverTeleport(Player player, WorldTile destination) {
		if (!player.getManager().getActivities().teleportationAllowed(TeleportType.OBJECT)) {
			return;
		}
		player.getManager().getLocks().lockAll();
		player.setNextAnimation(new Animation(2140));
		SystemManager.getScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void run() {
				player.getManager().getLocks().unlockAll();
				sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, destination, 3, false, TeleportType.OBJECT);
			}
		});
	}
	
	/**
	 * Sends a lunar teleport spell
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination
	 */
	static void sendLunarTeleport(Player player, WorldTile destination, int level, double xp, boolean randomize, int... runes) {
		sendTeleportSpell(player, 9606, -1, 1685, -1, level, xp, destination, 6, randomize, TeleportType.SPELL, runes);
	}
	
	/**
	 * Sends the teleportation spell
	 *
	 * @param player
	 * 		The player
	 * @param upEmoteId
	 * 		The up animation  id
	 * @param downEmoteId
	 * 		The down animation id
	 * @param upGraphicId
	 * 		The up graphics id
	 * @param downGraphicId
	 * 		The down graphics id
	 * @param level
	 * 		The level required to cast the spell
	 * @param xp
	 * 		The experience rewarded for casting the spell
	 * @param destination
	 * 		The destination of the spell
	 * @param delay
	 * 		The delay before the down is cast
	 * @param randomize
	 * 		If we should randomize the destination
	 * @param type
	 * 		The type of teleport
	 * @param runes
	 * 		The runes required
	 */
	static boolean sendTeleportSpell(final Player player, int upEmoteId, final int downEmoteId, int upGraphicId, final int downGraphicId, int level, final double xp, final WorldTile destination, int delay, final boolean randomize, final TeleportType type, int... runes) {
		if (player.getManager().getLocks().isAnyLocked()) {
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.MAGIC) < level) {
			player.getTransmitter().sendMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		if (!CombatRegistry.checkRunes(player, false, runes)) {
			return false;
		}
		if (!player.getManager().getActivities().teleportationAllowed(type)) {
			return false;
		}
		if (player.getVariables().getAttribute(AttributeKey.TELEBLOCKED_UNTIL, -1L) >= System.currentTimeMillis()) {
			player.getTransmitter().sendMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		CombatRegistry.checkRunes(player, true, runes);
		player.stop(true, true, true, true);
		if (upEmoteId != -1) {
			player.sendAnimation(upEmoteId);
		}
		if (upGraphicId != -1) {
			player.sendGraphics(upGraphicId);
		}
		player.getManager().getLocks().lockAll();
		player.putAttribute("teleporting", true);
		final int goalTicks = delay + 1;
		SystemManager.getScheduler().schedule(new ScheduledTask(1, goalTicks) {
			@Override
			public void run() {
				int ticksPassed = getTicksPassed();
				if (ticksPassed == goalTicks - 1) {
					WorldTile location = destination;
					if (randomize) {
						// attempts to randomize tile by 4x4 area
						for (int attempt = 0; attempt < 10; attempt++) {
							location = new WorldTile(destination, 2);
							if (World.isTileFree(location.getHeight(), location.getX(), location.getY(), player.getSize())) {
								break;
							} else {
								location = destination;
							}
						}
					}
					if (location != null) {
						player.teleport(location);
						if (!PvPLocation.isAtWild(location)) {
							player.unfreeze();
						}
						//player.getPackets().sendSound(5524, 0, 2);
						player.turnToLocation(Location.create(location.getX(), location.getY() - 1, location.getPlane()));
						ActivitySystem.fireLocationUpdate(player, location);
					}
					if (xp != 0) {
						player.getSkills().addExperienceWithMultiplier(SkillConstants.MAGIC, xp);
					}
					if (downEmoteId != -1) {
						player.sendAnimation(downEmoteId);
					}
					if (downGraphicId != -1) {
						player.sendGraphics(downGraphicId);
					}
				} else if (ticksPassed == goalTicks) {
					player.getManager().getLocks().unlockAll();
					player.removeAttribute("teleporting");
					player.getHitMap().clear();
				}
			}
		});
		return true;
	}
	
}
