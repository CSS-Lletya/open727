package org.redrune.game.content.combat.player.swing;

import java.util.Optional;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.EquipConstants;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.calc.RangeCombatCalculator;
import org.redrune.game.content.combat.player.registry.wrapper.BowFireEvent;
import org.redrune.game.content.combat.player.registry.wrapper.CombatSwingDetail;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.rs.Hit.HitAttributes;
import org.redrune.utility.rs.Hit.HitSplat;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import skills.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class RangeCombatSwing extends CombatTypeSwing {
	
	public RangeCombatSwing() {
		super(new RangeCombatCalculator());
	}
	
	@Override
	public boolean run(Player player, Entity target, int weaponId, int combatStyle, SpecialAttackEvent special) {
		int response = StaticCombatFormulae.getRangeResponse(player);
		if (response == 3) {
			player.getPackets().sendGameMessage("You don't have any more ammo left to use.");
			return false;
		} else if (response == 1) {
			player.getPackets().sendGameMessage("The ammo you're using is ineffective with your bow.");
			return false;
		}
		// we check the special attacks
		boolean usingSpecial = special != null;
		// the energy required
		final int energyRequired = ItemConstants.getSpecialEnergy(weaponId);
		if (usingSpecial) {
			// set the special attack off now...
			player.getCombatDefinitions().setSpecialActivated(false);
			if (player.getCombatDefinitions().getSpecialEnergy() < energyRequired) {
				player.getPackets().sendGameMessage("You don't have enough special attack energy.");
				usingSpecial = false;
			}
		}
		// custom attack send
		if (usingSpecial) {
			special.fire(player, target, this, combatStyle);
			player.getCombatDefinitions().decreaseSpecialAttack(energyRequired);
		} else {
			Optional<BowFireEvent> optional = CombatRegistry.getBow(weaponId);
			if (!optional.isPresent()) {
				player.getPackets().sendGameMessage("This bow has not yet been configured, please report it on the forums.");
				return false;
			}
			BowFireEvent event = optional.get();
			player.setNextAnimation(new Animation(StaticCombatFormulae.getWeaponAttackEmote(weaponId, player.getCombatDefinitions().getAttackStyle())));
			event.fire(player, target, this, weaponId, player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS));
		}
		return true;
	}
	
	@Override
	public double getAttackBonus(Player player, int weaponId, int combatStyle, boolean specialAttack) {
		return calculator.totalAggressiveBoost(player, combatStyle, specialAttack);
	}
	
	@Override
	public double getDefenceBonus(Entity entity, int weaponId, int combatStyle) {
		return calculator.totalDefensiveBoost(entity, weaponId, combatStyle);
	}
	
	@Override
	public double getMaxHit(Player player, int weaponId, int combatStyle, double multiplier) {
		return calculator.maximumDamageAppendable(player, weaponId, combatStyle, multiplier);
	}
	
	@Override
	public void applyHit(Player attacker, Entity receiver, Hit hit, int itemId, int combatStyle, int delay) {
		appendExperience(attacker, receiver, hit.getDamage());
		// handles the leeches aspect of the hit
		if (receiver.isPlayer()) {
			receiver.toPlayer().getManager().getPrayers().handlePrayerEffects(hit);
		} else {
			receiver.toNPC().handlePrayerEffects(hit);
		}
		SystemManager.getScheduler().schedule(new ScheduledTask(delay) {
			@Override
			public void run() {
				// the attribute is put when the hit actually appears
				hit.getAttributes().put(HitAttributes.WEAPON_USED, itemId);
				// and the hit is applied to the receiver
				receiver.getHitMap().applyHit(hit);
			}
		});
	}
	
	@Override
	public void appendExperience(Player player, Entity target, Object... params) {
		int damage = (int) params[0];
		
		if (damage > 0) {
			double combatXp = damage / 2.5;
			int attackStyle = player.getCombatDefinitions().getAttackStyle();
			if (attackStyle == 2) {
				if (target.isPlayer()) {
					player.getSkills().addExperienceNoMultiplier(SkillConstants.RANGE, combatXp / 2);
					player.getSkills().addExperienceNoMultiplier(SkillConstants.DEFENCE, combatXp / 2);
				} else {
					player.getSkills().addExperienceWithMultiplier(SkillConstants.RANGE, combatXp / 2);
					player.getSkills().addExperienceWithMultiplier(SkillConstants.DEFENCE, combatXp / 2);
				}
			} else {
				if (target.isPlayer()) {
					player.getSkills().addExperienceNoMultiplier(SkillConstants.RANGE, combatXp);
				} else {
					player.getSkills().addExperienceWithMultiplier(SkillConstants.RANGE, combatXp);
				}
			}
		}
	}
	
	/**
	 * Drops ammo on the ground
	 *
	 * @param player
	 * 		The player to drop ammo for
	 * @param location
	 * 		The location of the target
	 * @param ammoSlot
	 * 		The slot of the ammo
	 * @param ammoId
	 * 		The id of the ammo to use
	 */
	public void dropAmmo(Player player, WorldTile location, int ammoSlot, int ammoId, boolean delete) {
		Item ammo = player.getEquipment().getItem(ammoSlot);
		// no ammo here, safe check [this shouldn't happen anyway]
		if (ammo == null || ammo.getId() != ammoId) {
			return;
		}
		// the player doesn't lose ammo in the case they're lucky with a cape that saves ammo
		if (player.getEquipment().capeSavesAmmo() && Utils.getRandom(3) == 2) {
			return;
		}
		// the new amount to set
		int newAmount = ammo.getAmount() - 1;
		// if we should remove the item from the equipment
		final boolean removed = newAmount <= 0;
		// removes the ammo from the equipment
		player.getEquipment().getItems().set(ammoSlot, removed ? null : new Item(ammoId, newAmount));
		// we aren't deleting so it must be dropped on ground...
		if (!delete) {
			RegionManager.addStackableFloorItem(ammoId, 1, 180, location, player.getDetails().getUsername());
		}
		player.getEquipment().refresh(ammoSlot);
		// if the item is removed
		if (removed) {
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		}
	}
	
	/**
	 * Sends the damage to the target
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id
	 * @param modifier
	 * 		The max hit modifier
	 * @param specialAttack
	 * 		If we are using a special attack
	 */
	public static CombatSwingDetail sendDamage(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, double modifier, boolean specialAttack) {
		final int style = attacker.getCombatDefinitions().getAttackStyle();
		final int delay = ProjectileManager.getProjectileDelay(attacker, target);
		final double maxHit = swing.getMaxHit(attacker, weaponId, style, modifier);
		final int damage = swing.randomizeHit(maxHit, swing.getAttackBonus(attacker, weaponId, style, specialAttack), swing.getDefenceBonus(target, weaponId, style));
		return sendDamage(attacker, target, swing, weaponId, style, delay, maxHit, damage, null);
	}
	
	/**
	 * Sends the damage to the target with predefined damage
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id
	 * @param damage
	 * 		The damage to append
	 */
	public static CombatSwingDetail sendDamage(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int damage) {
		final int style = attacker.getCombatDefinitions().getAttackStyle();
		final int delay = ProjectileManager.getProjectileDelay(attacker, target);
		return sendDamage(attacker, target, swing, weaponId, style, delay, damage, damage, null);
	}
	
	/**
	 * Sends damage to the target
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The id of the weapon
	 * @param style
	 * 		The style we're using
	 * @param delay
	 * 		The delay to use
	 * @param maxHit
	 * 		The max hit
	 * @param damage
	 * 		The damage
	 */
	public static CombatSwingDetail sendDamage(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int style, int delay, double maxHit, int damage, Runnable landTask) {
		// hit, ammo, defend
		final Hit hit = new Hit(attacker, damage, HitSplat.RANGE_DAMAGE).setMaxHit(maxHit);
		swing.applyHit(attacker, target, hit, weaponId, style, delay);
		sendBlockEmote(target, delay);
		if (landTask != null) {
			SystemManager.getScheduler().schedule(new ScheduledTask(delay) {
				@Override
				public void run() {
					landTask.run();
				}
			});
		}
		return new CombatSwingDetail(attacker, target, hit);
	}
	
	/**
	 * Makes the target do their block emote a tick before the delay
	 *
	 * @param target
	 * 		The target
	 * @param delay
	 * 		Their block emote
	 */
	private static void sendBlockEmote(Entity target, int delay) {
		SystemManager.getScheduler().schedule(new ScheduledTask(delay - 1) {
			@Override
			public void run() {
				target.sendAwaitedAnimation(StaticCombatFormulae.getDefenceEmote(target));
			}
		});
	}
	
}
