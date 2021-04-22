package skills;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

/**
 * The skill action that represents an action where one item is removed from an
 * inventory and lost forever. This type of skill action is very basic and only
 * requires that a player have the item to destruct in their inventory.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code PRAYER}.
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see HarvestingSkillAction
 * @see ProducingSkillAction
 */
public abstract class DestructionSkillAction extends SkillHandler {
	
	/**
	 * Creates a new {@link DestructionSkillAction}.
	 * @param player the player this skill action is for.
	 * @param position the position the player should face.
	 */
	public DestructionSkillAction(Player player, WorldTile position) {
		super(player, position);
	}
	
	@Override
	public boolean canRun(Task t) {
		String name = ItemDefinitions.forId(destructItem().getId()).getName();
		if(!getPlayer().getInventory().contains(new Item(destructItem().getId()))) {
			getPlayer().getPackets().sendGameMessage("You do not have any " + name + " in your inventory.");
			return false;
		}
		return true;
	}
	
	@Override
	public final void execute(Task t) {
		if(getPlayer().getInventory().getNumerOf(destructItem().getAmount()) >= 0) {
			onDestruct(t, true);
			player.getSkills().addXp(getSkillId(), experience());
			return;
		}
		onDestruct(t, false);
		t.cancel();
	}
	
	/**
	 * The method executed upon destruction of the item.
	 * @param t the task executing this method.
	 * @param success determines if the destruction was successful or not.
	 */
	public void onDestruct(Task t, boolean success) {

	}
	
	/**
	 * The item that will be removed upon destruction.
	 * @return the item that will be removed.
	 */
	public abstract Item destructItem();
	
	@Override
	public boolean isPrioritized() {
		return false;
	}
}
