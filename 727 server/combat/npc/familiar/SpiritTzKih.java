package npc.familiar;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import skills.summoning.Summoning.Pouches;

public class SpiritTzKih extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8469842707500116693L;

	public SpiritTzKih(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Fireball Assault";
	}

	@Override
	public String getSpecialDescription() {
		return "Has the potential of hitting up to two nearby targets with up to 70 points of damage";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}
}
