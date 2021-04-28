package npc.familiar;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import skills.Skills;
import skills.summoning.Summoning.Pouches;

public class Icetitan extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8361882652135063488L;

	public Icetitan(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Titan's Constitution ";
	}

	@Override
	public String getSpecialDescription() {
		return "Defence by 12.5%, and it can also increase a player's Life Points 80 points higher than their max Life Points.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		int newLevel = getOwner().getSkills().getLevel(Skills.DEFENCE)
				+ (getOwner().getSkills().getLevelForXp(Skills.DEFENCE) / (int) 12.5);
		if (newLevel > getOwner().getSkills().getLevelForXp(Skills.DEFENCE) + (int) 12.5)
			newLevel = getOwner().getSkills().getLevelForXp(Skills.DEFENCE) + (int) 12.5;
		getOwner().setNextGraphics(new Graphics(2011));
		getOwner().setNextAnimation(new Animation(7660));
		getOwner().getSkills().set(Skills.DEFENCE, newLevel);
		return true;
	}
}
