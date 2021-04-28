package player.type.impl;

import com.rs.game.Entity;
import com.rs.game.player.Player;

import player.type.CombatEffect;

/**
 * The combat effect applied when a player needs to be skulled.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatSkullEffect extends CombatEffect {
	
	/**
	 * Creates a new {@link CombatSkullEffect}.
	 */
	public CombatSkullEffect() {
		super(50);
	}
	
	@Override
	public boolean apply(Entity entity) {
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if(player.getSkullTimer().get() > 0) {
				return false;
			}
			player.getSkullTimer().set(3000);
			player.getPlayerDetails().setSkullId(WHITE_SKULL);
			player.getAppearance().getAppeareanceBlocks();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeOn(Entity entity) {
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if(player.getSkullTimer().get() <= 0) {
				player.getPlayerDetails().setSkullId(NO_SKULL);
				player.getAppearance().getAppeareanceBlocks();
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void process(Entity entity) {
		if(entity instanceof Player) {
			Player player = (Player) entity;
			player.getSkullTimer().decrementAndGet(50, 0);
		}
	}
	
	@Override
	public boolean onLogin(Entity entity) {
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if(player.getSkullTimer().get() > 0) {
				player.getPlayerDetails().setSkullId(WHITE_SKULL);
				return true;
			}
		}
		return false;
	}
	
	private final byte WHITE_SKULL = 0, NO_SKULL = -1;
}