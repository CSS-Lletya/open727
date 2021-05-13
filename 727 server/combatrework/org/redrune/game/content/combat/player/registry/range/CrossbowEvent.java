package org.redrune.game.content.combat.player.registry.range;

import java.util.Optional;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.core.system.SystemManager;
import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.EquipConstants;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.registry.wrapper.BowFireEvent;
import org.redrune.game.content.combat.player.registry.wrapper.CombatSwingDetail;
import org.redrune.game.content.combat.player.swing.RangeCombatSwing;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.utility.tool.RandomFunction;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.player.Player;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class CrossbowEvent implements BowFireEvent {
	
	@Override
	public String[] bowNames() {
		return arguments("* crossbow");
	}
	
	@Override
	public void fire(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int ammoId) {
		String name = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(attacker, target, 27, 38, 36, 41, 5, 0));
		swing.dropAmmo(attacker, target.getLocation(), EquipConstants.SLOT_ARROWS, ammoId, name.contains("karil"));
		Optional<BoltSpecial> optional = BoltSpecial.getBoltSpecial(ammoId);
		// found a possible bolt
		if (optional.isPresent()) {
			BoltSpecial special = optional.get();
			// the bolt was fired so we don't need to send another hit
			if (special.canFire(attacker, target)) {
				special.onSwing(attacker, target, this, swing, weaponId);
				return;
			}
		}
		// we didn't send the damage from the bolt, we must still send it here
		sendDamage(attacker, target, swing, weaponId);
	}
	
	private enum BoltSpecial {
		
		JADE_BOLT(9237, 755) {
			@Override
			public double getDamageModifier() {
				return 1;
			}
			
			@Override
			public CombatSwingDetail onSwing(Player source, Entity target, BowFireEvent event, RangeCombatSwing swing, int weaponId) {
				if (target.isNPC()) {
					target.toNPC().getCombatManager().getCombat().setTarget(null);
				} else {
					target.toPlayer().stop(true, true, true, false);
				}
				return super.onSwing(source, target, event, swing, weaponId);
			}
		},
		
		RUBY_BOLT(9242, 754) {
			@Override
			public double getDamageModifier() {
				return 1;
			}
			
			@Override
			public CombatSwingDetail onSwing(Player source, Entity target, BowFireEvent event, RangeCombatSwing swing, int weaponId) {
				target.sendGraphics(getGraphicsId(), getGraphicsHeight(), 0);
				source.getHitMap().applyHit(new Hit(target, source.getHealthPoints() > 20 ? (int) (source.getHealthPoints() * 0.10) : 1, HitSplat.REFLECTED_DAMAGE));
				return event.sendDamage(source, target, swing, weaponId, (int) (target.getHealthPoints() * 0.20));
			}
		},
		
		DIAMOND_BOLT(9243, 758) {
			@Override
			public double getDamageModifier() {
				return 1.05;
			}
		},
		
		DRAGON_BOLT(9244, 756) {
			@Override
			public double getDamageModifier() {
				return 1.45;
			}
			
			@Override
			public boolean canFire(Player source, Entity target) {
				return !StaticCombatFormulae.hasAntiDragProtection(target) && super.canFire(source, target);
			}
		},
		
		ONYX_BOLT(9245, 753) {
			@Override
			public double getDamageModifier() {
				return 1.25;
			}
			
			@Override
			public boolean canFire(Player source, Entity target) {
				return source.getAttribute("onyx-effect", 0L) <= SystemManager.getUpdateWorker().getTicks() && super.canFire(source, target);
			}
			
			@Override
			public CombatSwingDetail onSwing(Player source, Entity target, BowFireEvent event, RangeCombatSwing swing, int weaponId) {
				return super.onSwing(source, target, event, swing, weaponId).consume(detail -> {
					source.putAttribute("onyx-effect", SystemManager.getUpdateWorker().getTicks() + 12);
					source.heal((int) (detail.getHit().getDamage() * 0.25));
				});
			}
		},
		;
		
		/**
		 * The id of the bolt used for this special
		 */
		@Getter
		private final int boltId;
		
		/**
		 * The id of the graphics
		 */
		@Getter
		private final int graphicsId;
		
		/**
		 * The height of the graphics
		 */
		@Getter
		private final int graphicsHeight;
		
		BoltSpecial(int boltId, int graphicsId) {
			this(boltId, graphicsId, 0);
		}
		
		BoltSpecial(int boltId, int graphicsId, int graphicsHeight) {
			this.boltId = boltId;
			this.graphicsId = graphicsId;
			this.graphicsHeight = graphicsHeight;
		}
		
		/**
		 * The damage modifier of the bolt special
		 */
		public abstract double getDamageModifier();
		
		/**
		 * Checks if the bolt special can be fired
		 *
		 * @param source
		 * 		The source of the special
		 * @param target
		 * 		The target of the special
		 */
		public boolean canFire(Player source, Entity target) {
			return RandomFunction.random(13) == 5;
		}
		
		/**
		 * When the swing is called, this method is too
		 *
		 * @param source
		 * 		The player firing the bolt
		 * @param target
		 * 		The target of the bolt
		 */
		public CombatSwingDetail onSwing(Player source, Entity target, BowFireEvent event, RangeCombatSwing swing, int weaponId) {
			target.sendGraphics(graphicsId, graphicsHeight, 0);
			return event.sendDamage(source, target, swing, weaponId, getDamageModifier());
		}
		
		/**
		 * Gets the bolt special by the id of the bolt we're using
		 *
		 * @param boltId
		 * 		The id of the bolt
		 */
		public static Optional<BoltSpecial> getBoltSpecial(int boltId) {
			for (BoltSpecial special : values()) {
				if (special.getBoltId() == boltId) {
					return Optional.of(special);
				}
			}
			return Optional.empty();
		}
		
	}
	
}
