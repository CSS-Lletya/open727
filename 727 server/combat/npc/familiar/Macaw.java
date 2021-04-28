package npc.familiar;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import skills.summoning.Summoning.Pouches;

public class Macaw extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7805271915467121215L;

	public Macaw(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Herbcall";
	}

	@Override
	public String getSpecialDescription() {
		return "Creates a random herb.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
//		Player player = (Player) object;
//		Herbs herb;
//		player.setNextGraphics(new Graphics(1300));
//		player.setNextAnimation(new Animation(7660));
//		// TODO too lazy to find anims and gfx
//		if (Utils.getRandom(100) == 0)
//			herb = Herbs.values()[Utils.random(Herbs.values().length)];
//		else
//			herb = Herbs.values()[Utils.getRandom(3)];
//		World.addGroundItem(new Item(herb.getHerbId(), 1), player);
		return true;
	}
}
