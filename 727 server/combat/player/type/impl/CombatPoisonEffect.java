package player.type.impl;

import java.util.Optional;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import player.type.CombatEffect;
import player.type.PoisonType;

/**
 * The combat effect applied when a character needs to be poisoned.
 * 
 * @author lare96 <http://github.com/lare96>
 * @author Dennis
 */
public final class CombatPoisonEffect extends CombatEffect {

	/**
	 * The collection of weapons mapped to their respective poison types.
	 */
	public static final Int2ObjectArrayMap<PoisonType> TYPES = new Int2ObjectArrayMap<>();

	/**
	 * Creates a new {@link CombatPoisonEffect}.
	 */
	public CombatPoisonEffect() {
		super(30);
	}

	@Override
	public boolean apply(Entity entity) {
		if (entity.isPoisoned() || entity.getPoisonType() == null)
			return false;
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (player.getPoisonImmunity().get() > 0 || entity.isDead())
				return false;
			player.getPackets().sendConfig(102, 1);
		}
		entity.getPoisonDamage().set(entity.getPoisonType().getDamage());
		return true;
	}
	
	@Override
	public boolean removeOn(Entity entity) {
		return !entity.isPoisoned() || entity.isDead();
	}
	
	@Override
	public void process(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (entity.getPoisonDamage().get() == 0) {
				player.getPackets().sendGameMessage("Your poison effects has worn off.");
				player.getPackets().sendConfig(102, 0);
			}
		}
		if(entity.getPoisonDamage().get() <= 0) {
			if(entity instanceof Player) {
				Player player = (Player) entity;
				player.getPackets().sendConfig(102, 1);
			}
			this.removeOn(entity);
			return;
		}
		entity.applyHit(new Hit(entity, entity.getPoisonDamage().get() * 10, HitLook.POISON_DAMAGE));
		entity.getPoisonDamage().decrementAndGet();
	}
	
	@Override
	public boolean onLogin(Entity entity) {
		return entity.isPoisoned();
	}

	/**
	 * Gets the {@link PoisonType} for {@code item} wrapped in an optional. If a
	 * poison type doesn't exist for the item then an empty optional is returned.
	 * 
	 * @param item the item to get the poison type for.
	 * @return the poison type for this item wrapped in an optional, or an empty
	 *         optional if no poison type exists.
	 */
	public static Optional<PoisonType> getPoisonType(Item item) {
		return item == null || item.getId() < 1 || item.getAmount() < 1 ? Optional.empty() : Optional.ofNullable(TYPES.get(item.getId()));
	}

	/**
	 * Gets the {@link PoisonType} for {@code npc} wrapped in an optional. If a
	 * poison type doesn't exist for the NPC then an empty optional is returned.
	 * 
	 * @param npc the NPC to get the poison type for.
	 * @return the poison type for this NPC wrapped in an optional, or an empty
	 *         optional if no poison type exists.
	 */
	public static Optional<PoisonType> getPoisonType(int npc) {
		NPCDefinitions def = NPCDefinitions.getNPCDefinitions(npc);
		if(def == null || !def.hasAttackOption())
			return Optional.empty();
		if (def.getCombatLevel() < 75)
			return Optional.of(PoisonType.DEFAULT_NPC);
		if (def.getCombatLevel() < 200)
			return Optional.of(PoisonType.STRONG_NPC);
		return Optional.of(PoisonType.SUPER_NPC);
	}
}
