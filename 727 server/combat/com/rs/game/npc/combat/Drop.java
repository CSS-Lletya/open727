package com.rs.game.npc.combat;

public class Drop {

	private int itemId, minAmount, maxAmount;
	private double rate;
	@SuppressWarnings("unused")
	private boolean rare;

	public Drop(int itemId, double rate, int minAmount, int maxAmount, boolean rare) {
		this.itemId = itemId;
		this.rate = rate;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.rare = rare;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getExtraAmount() {
		return maxAmount - minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public int getItemId() {
		return itemId;
	}

	public double getRate() {
		return rate;
	}

}
