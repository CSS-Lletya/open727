package npc.drops;

import java.util.concurrent.ThreadLocalRandom;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.utils.Chance;
import com.rs.utils.Utils;

/**
 * A model representing an item within a rational item table that can be dropped.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Drop {
	
	/**
	 * The identification of this {@code Drop}.
	 */
	public int id;
	
	/**
	 * The minimum amount that will be dropped.
	 */
	public int minimum;
	
	/**
	 * The maximum amount that will be dropped.
	 */
	public int maximum;
	
	/**
	 * The chance of this item being dropped.
	 */
	private final Chance chance;
	
	/**
	 * Creates a new {@link Drop}.
	 * @param id the identification of this {@code Drop}.
	 * @param minimum the minimum amount that will be dropped.
	 * @param maximum the maximum amount that will be dropped.
	 * @param chance the chance of this item being dropped.
	 */
	public Drop(int id, int minimum, int maximum, Chance chance) {
		this.id = id;
		this.minimum = minimum;
		this.maximum = maximum;
		this.chance = chance;
	}
	
	@Override
	public String toString() {
		return "ITEM[id= " + getId() + ", min= " + getMinimum() + ", max= " + getMaximum() + ", chance= " + getChance() + "]";
	}
	
	/**
	 * Converts this {@code Drop} into an {@link Item} Object.
	 * @return the converted drop.
	 */
	public Item toItem() {
		return new Item(getId(), Utils.inclusive(getMinimum(), getMaximum()));
	}
	
	/**
	 * Gets the identification of this {@code Drop}.
	 * @return the identification.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the minimum amount that will be dropped.
	 * @return the minimum amount.
	 */
	public int getMinimum() {
		return minimum;
	}
	
	/**
	 * Gets the maximum amount that will be dropped.
	 * @return the maximum amount.
	 */
	public int getMaximum() {
		return maximum;
	}
	
	/**
	 * Gets the chance of this item being dropped.
	 * @return the drop chance.
	 */
	public Chance getChance() {
		return chance;
	}
	
	/**
	 * Returns the condition if this item is rare.
	 * @return value.
	 */
	public boolean isRare() {
		return chance == Chance.RARE || chance == Chance.VERY_RARE || chance == Chance.EXTREMELY_RARE;
	}
	
	/**
	 * Gets the pricing value of this drop.
	 * @return value.
	 */
	public int value() {
		return ItemDefinitions.getItemDefinitions(id).getValue() * maximum;
	}
	
	/**
	 * Tries to roll this item.
	 * @param rand random gen.
	 * @return condition if successful.
	 */
	public boolean roll(ThreadLocalRandom rand) {
		return chance.getRoll() >= rand.nextDouble();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Drop)) {
			return false;
		}
		Drop drop = (Drop) o;
		return drop.getId() == getId() && drop.getMinimum() == getMinimum() && drop.getMaximum() == getMaximum() && drop.getChance() == getChance();
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + getId();
		result = 31 * result + getMinimum();
		result = 31 * result + getMaximum();
		result = 31 * result + getChance().hashCode();
		return result;
	}
	
}