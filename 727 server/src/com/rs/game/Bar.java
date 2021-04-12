package com.rs.game;
 
/**
 * @author _Jordan <citellumrsps@gmail.com>
 * @date Jan 24, 2015
 */
public enum Bar {
 
    HITPOINTS(0);
 
    /**
     * Represents the type if for each {@code Bar}.
     */
    private final int barType;
 
    /**
     * Constructs a new {@code Bar} {@code Object}.
     * @param barType Constructs the type id for each {@code Bar}.
     */
    private Bar(int barType) {
        this.barType = barType;
    }
 
    /**
     * Gets the percentage of a specified bar.
     * 
     * @param p The player to get for.
     * @return The percentage of the bar.
     */
    public int getPercentage(Entity e) {
        int percentage = 0;
        int hitpoints = e.getHitpoints();
        int maxHp = e.getMaxHitpoints();
        percentage = hitpoints > maxHp ? getMaxPercentage() : maxHp == 0 ? 0 : (hitpoints * 255) / maxHp;
        return percentage;
    }
     
    /**
     * Gets the max percentage of the bar.
     * 
     * @return The max percent.
     */
    public int getMaxPercentage() {
        return 255;
    }
 
    /**
     * Gets the barType.
     * @return The barType.
     */
    public int getBarType() {
        return barType;
    }
 
}