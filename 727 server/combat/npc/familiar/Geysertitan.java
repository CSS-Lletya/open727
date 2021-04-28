package npc.familiar;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import skills.summoning.Summoning.Pouches;

public class Geysertitan extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -707448797034175432L;

	public Geysertitan(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Boil";
	}

	@Override
	public String getSpecialDescription() {
		return "Increases the titan's combat by 60 in the next combat tick.";
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
