package com.rs.game.item;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemsEquipIds;

/**
 * Represents a single item.
 * <p/>
 * 
 * @author Graham / edited by Dragonkk(Alex)
 */
public class Item {

	private short id;
	protected int amount;

	public int getId() {
		return id;
	}

	@Override
	public Item clone() {
		return new Item(id, amount);
	}

	public Item(int id) {
		this(id, 1);
	}

	public Item(int id, int amount) {
		this(id, amount, false);
	}

	public Item(int id, int amount, boolean amt0) {
		this.id = (short) id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}

	public ItemDefinitions getDefinitions() {
		return ItemDefinitions.getItemDefinitions(id);
	}

	public int getEquipId() {
		return ItemsEquipIds.getEquipId(id);
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setId(int id) {
		this.id = (short) id;
	}

	public int getAmount() {
		return amount;
	}

	public String getName() {
		return getDefinitions().getName();
	}

	public long getFixedUniqueId() {
		return id * 234111 +  amount * 23911;
	}
}