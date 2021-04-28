package npc.familiar;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import skills.Skills;
import skills.summoning.Summoning.Pouches;

public class Wartortoise extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5092434230714486203L;

	public Wartortoise(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Testudo";
	}

	@Override
	public String getSpecialDescription() {
		return "Increases defence by nine points.";
	}

	@Override
	public int getBOBSize() {
		return 18;
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
		Player player = (Player) object;
		int newLevel = player.getSkills().getLevel(Skills.DEFENCE) + 9;
		if (newLevel > player.getSkills().getLevelForXp(Skills.DEFENCE) + 9)
			newLevel = player.getSkills().getLevelForXp(Skills.DEFENCE) + 9;
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		player.getSkills().set(Skills.DEFENCE, newLevel);
		return true;
	}
}
