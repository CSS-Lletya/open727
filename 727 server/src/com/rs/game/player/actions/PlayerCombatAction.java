package com.rs.game.player.actions;

import java.util.Optional;

import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.combat.player.CombatType;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.tool.Misc;

import com.rs.game.Entity;
import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public final class PlayerCombatAction extends Action {
	
	/**
	 * The target we are in combat with
	 */
	private final Entity target;
	
	/**
	 * The type of combat we're engaging in.
	 */
	private CombatType type;
	
	public PlayerCombatAction(Entity target) {
		this.target = target;
	}
	
	@Override
	public boolean start(Player player) {
		type = StaticCombatFormulae.getCombatType(player);
		player.resetWalkSteps();
		if (!verifyContinuation(player)) {
			return false;
		}
		player.faceEntity(target);
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		type = StaticCombatFormulae.getCombatType(player);
		if (!verifyContinuation(player)) {
			return false;
		}
		if (target != null) {
			target.putAttribute("last_target", player);
			target.putAttribute("last_time_combatted", System.currentTimeMillis());
		}
		checkSpecials(player);
		player.putAttribute("combat_target", target);
		player.putAttribute("last_time_combatted", System.currentTimeMillis());
		return true;
	}
	
	/**
	 * Checks the special attacks, this sends instant specs as well as toggles the queued special attack on before we
	 * use the weapon.
	 *
	 * @param player
	 * 		The player.
	 */
	private void checkSpecials(Player player) {
		// if the special attack was toggled on and is queued.
		StaticCombatFormulae.checkSpecialToggle(player, 0);
	}
	
	/**
	 * Gets the special attack event
	 *
	 * @param player
	 * 		The player
	 * @param weaponId
	 * 		The id of the weapon we're using
	 * @param usingSpecial
	 * 		If we're using the special attack
	 */
	private Optional<SpecialAttackEvent> getSpecialAttackEvent(Player player, int weaponId, boolean usingSpecial) {
		Optional<SpecialAttackEvent> specialOptional = Optional.empty();
		if (usingSpecial) {
			specialOptional = CombatRegistry.getSpecial(weaponId);
			if (!specialOptional.isPresent()) {
				player.getCombatDefinitions().setUsuingSpecilAttack(false);
				player.getTransmitter().sendMessage("Unregistered special attack event, please report this on the forum.");
			}
		}
		return specialOptional;
	}
	
	@Override
	public void stop(Player player) {
		// otherwise we don't face when casting magic
		if (type != null && type != CombatType.MAGIC) {
			player.turnTo(null);
		}
	}
	
	/**
	 * Verifies that combat can continue by checking multiple states
	 *
	 * @param player
	 * 		The player in combat
	 */
	private boolean verifyContinuation(Player player) {
		Entity target = this.target;
		// we couldn't find a combat type
		if (type == null) {
			return false;
		}
		// if we are invalid to fight
		if (!StaticCombatFormulae.canFight(player, target)) {
			return false;
		}
		// if player is frozen and under, stops attacking, else stands waiting
		if (player.isFrozen()) { // TODO stunned [should it be same flag as frozen?]
			return !Misc.colides(player, target);
		}
		// if we are on the same position
		if (Misc.colides(player, target)) {
			player.getMovement().resetWalkSteps();
			// if the target is moving [must be going from the collision tile]
			if (target.getMovement().isMoving()) {
				return true;
			}
			player.getMovement().addEntityPath(target, true);
			return true;
		}
		// we're too far away or we can't clip to the target
		if (!Misc.isOnRange(player, target, StaticCombatFormulae.getMinimumDistance(player, type)) || !player.getMovement().clippedProjectileToNode(target, type == CombatType.MELEE && !StaticCombatFormulae.checkAttackPathAsRange(target))) {
			if (!player.getMovement().isMoving() || target.getMovement().isMoving()) {
				player.getMovement().resetWalkSteps();
				player.getMovement().addEntityPath(target, 25, true);
			}
		} else {
			player.getMovement().resetWalkSteps();
		}
		// diagonal check
		if (type == CombatType.MELEE && Math.abs(player.getLocation().getX() - target.getLocation().getX()) == 1 && Math.abs(player.getLocation().getY() - target.getLocation().getY()) == 1 && !target.getMovement().hasWalkSteps() && target.getSize() == 1) {
			player.getMovement().resetWalkSteps();
			if (!player.getMovement().addWalkSteps(target.getLocation().getX(), player.getLocation().getY(), 1, true)) {
				player.getMovement().resetWalkSteps();
				player.getMovement().addWalkSteps(player.getLocation().getX(), target.getLocation().getY(), 1, true);
			}
			return true;
		}
		if (!(target.isNPC() && target.toNPC().getCombatManager().isForceMultiAttacked())) {
			if (!target.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != target && player.getAttackedByDelay() > System.currentTimeMillis()) {
					player.getTransmitter().sendMessage("You are already in combat");
					return false;
				}
				if (target.getAttackedBy() != player && target.getAttackedByDelay() > System.currentTimeMillis()) {
					player.getTransmitter().sendMessage((target.isPlayer() ? "That player is" : "This npc is") + " already in combat");
					return false;
				}
			}
		}
		// we can't continue fighting in the activity
		if (player.getManager().getActivities().handlesNodeInteraction(target, InteractionOption.ATTACK_OPTION) || !player.getManager().getActivities().combatAcceptable(target)) {
			return false;
		}
		// anything else ?
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (!StaticCombatFormulae.isWithinDistance(player, target, type)) {
			return 0;
		}
		player.turnTo(target);
		// the id of the weapon equipped
		final int weaponId = player.getEquipment().getWeaponId();
		// the spell we're casting
		final int spellId = player.getAttribute("spell_cast_id", player.getCombatDefinitions().getAutocastId());
		// the id of the combat flag [weapon or spell id]
		final int id = type == CombatType.MAGIC ? spellId : weaponId;
		// the delay we will have
		final int delay = type.getDelay(player, id);
		// the multiplier on the speed of the combat action
		double multiplier = 1.0;
		// the delay wasn't found [this is only possible when we don't have a magic spell
		// otherwise, delays are calculated in the swing
		if (delay == -1) {
			player.getTransmitter().sendMessage((type == CombatType.MAGIC ? "Spell #" + id + "" : "Weapon #" + weaponId) + " has not yet been added, please report this on the forums.");
			player.getCombatDefinitions().resetSpells(true);
			return -1;
		}
		
		if (player.getAttribute(AttributeKey.MIASMIC_EFFECT) == Boolean.TRUE) {
			multiplier = 1.5;
		}
		// if we're using special
		final boolean usingSpecial = player.getCombatDefinitions().isSpecialActivated();
		// the special attack event
		Optional<SpecialAttackEvent> specialOptional = getSpecialAttackEvent(player, weaponId, usingSpecial);
		// sends the swing
		if (!type.getSwing().run(player, target, id, player.getCombatDefinitions().getAttackStyle(), specialOptional.orElse(null))) {
			return -1;
		}
		// after combat has been sent, we must send listeners
		StaticCombatFormulae.fireCombatListeners(player, target);
		return (int) (delay * multiplier);
	}
	
}
