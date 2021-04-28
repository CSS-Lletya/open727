package npc.drops;

import com.rs.utils.Chance;

/**
 * A suggested drop from a player.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class SuggestedDrop extends Drop {
	
	/**
	 * The npc the drop may concern.
	 */
	private final int npc;
	
	/**
	 * Creates a new {@link SuggestedDrop}.
	 * @param npc the npc concerning.
	 * @param id the identification of this {@code Drop}.
	 * @param minimum the minimum amount that will be dropped.
	 * @param maximum the maximum amount that will be dropped.
	 * @param chance the chance of this item being dropped.
	 */
	public SuggestedDrop(int npc, int id, int minimum, int maximum, Chance chance) {
		super(id, minimum, maximum, chance);
		this.npc = npc;
	}
	
	/**
	 * The npc this suggestion is concerning.
	 * @return npc id.
	 */
	public int getNpc() {
		return npc;
	}
	
	/**
	 * Down casts to a drop.
	 */
	public Drop toDrop() {
		return new Drop(getId(), getMinimum(), getMaximum(), getChance());
	}
	
	@Override
	public String toString() {
		return "DROP[npc= " + npc + ", id= " + getId() + ", min= " + getMinimum() + ", max= " + getMaximum() + ", chance= " + getChance() + "]";
	}
	
}
