package com.rs.game.player;

import com.rs.utils.Utils;

/**
 * All personal variables of the Player shall be stored here for easier access.
 * 
 * @author Dennis
 *
 */
public final class PlayerDetails {

	/**
	 * Constructs a new Player's details
	 */
	public PlayerDetails() {
		pouches = new byte[4];
		maxedCapeCustomized = new int[4];
		completionistCapeCustomized = new int[4];
	}

	/**
	 * An array of Runecrafting pouches that possibly contain values.
	 */
	private byte[] pouches;

	/**
	 * Gets the Runecrafting pouches
	 * 
	 * @return pouches
	 */
	public byte[] getPouches() {
		return pouches;
	}

	private long displayTime;

	/**
	 * The length of a Player being Muted (Unable to chat)
	 */
	private long muted;

	/**
	 * Length of the Player being Jailed (stuck in a remote area)
	 */
	private long jailed;

	/**
	 * Gets the Muted length
	 * 
	 * @return length
	 */
	public long getMuted() {
		return muted;
	}

	/**
	 * Sets the Players Mute time
	 * 
	 * @param muted
	 */
	public void setMuted(long muted) {
		this.muted = muted;
	}

	/**
	 * Gets the Jailed length
	 * 
	 * @return
	 */
	public long getJailed() {
		return jailed;
	}

	/**
	 * Sets the Players Jail length
	 * 
	 * @param jailed
	 */
	public void setJailed(long jailed) {
		this.jailed = jailed;
	}

	/**
	 * Adds Players display time
	 * 
	 * @param i
	 */
	public void addDisplayTime(long i) {
		this.displayTime = i + Utils.currentTimeMillis();
	}

	/**
	 * Gets the Players display time
	 * 
	 * @return
	 */
	public long getDisplayTime() {
		return displayTime;
	}

	/**
	 * Represents the last known IP from the Player
	 */
	private String lastIP;

	/**
	 * Sets the last known IP to the Player
	 * 
	 * @param lastIP
	 */
	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	/**
	 * Gets the last known IP
	 * 
	 * @return ip
	 */
	public String getLastIP() {
		return lastIP;
	}

	/**
	 * Represents the amount of SOF spins a Player has
	 */
	private int spins;

	/**
	 * Sets the SOF spin amount for a Player
	 * 
	 * @param spins
	 */
	public void setSpins(int spins) {
		this.spins = spins;
	}

	/**
	 * Gets the amount of SOF spins from a Player
	 * 
	 * @return
	 */
	public int getSpins() {
		return this.spins;
	}

	/**
	 * Represents if a Player is filtering out their chatbox messages
	 */
	private boolean filterGame;
	/**
	 * Represents if the Player has their experience locked
	 */
	private boolean xpLocked;

	/**
	 * Gets the Filter game status from a Player
	 * 
	 * @return filter
	 */
	public boolean isFilterGame() {
		return filterGame;
	}

	/**
	 * Sets the Filter game status for a Player
	 * 
	 * @param filterGame
	 */
	public void setFilterGame(boolean filterGame) {
		this.filterGame = filterGame;
	}

	/**
	 * Gets the experience locked status for a Player
	 * 
	 * @return
	 */
	public boolean isXpLocked() {
		return xpLocked;
	}

	/**
	 * Sets the experienced lock status for a Player
	 * 
	 * @param locked
	 */
	public void setXpLocked(boolean locked) {
		this.xpLocked = locked;
	}

	/**
	 * An array of possible changes to the Max Cape customization
	 */
	private int[] maxedCapeCustomized;
	/**
	 * An array of possible changes to the Completionist Cape customization
	 */
	private int[] completionistCapeCustomized;

	/**
	 * Gets the Completionist Cape customization array
	 * 
	 * @return completionistCapeCustomized
	 */
	public int[] getCompletionistCapeCustomized() {
		return completionistCapeCustomized;
	}

	/**
	 * Sets the Completionist Cape array
	 * 
	 * @param skillcapeCustomized
	 */
	public void setCompletionistCapeCustomized(int[] skillcapeCustomized) {
		this.completionistCapeCustomized = skillcapeCustomized;
	}

	/**
	 * Gets the Max Cape customization array
	 * 
	 * @return
	 */
	public int[] getMaxedCapeCustomized() {
		return maxedCapeCustomized;
	}

	/**
	 * Sets the Completionist Cape array
	 * 
	 * @param maxedCapeCustomized
	 */
	public void setMaxedCapeCustomized(int[] maxedCapeCustomized) {
		this.maxedCapeCustomized = maxedCapeCustomized;
	}

	/**
	 * Represents if the Player should be using older item models to display
	 */
	public boolean oldItemsLook;

	/**
	 * Gets the
	 * 
	 * @return
	 */
	public boolean isOldItemsLook() {
		return oldItemsLook;
	}
	
	/**
	 * Represents the default Yell color for a Player
	 */
	public String yellColor = "ff0000";
	
	/**
	 * Gets the Yell color of a Player
	 * @return
	 */
	public String getYellColor() {
		return yellColor;
	}

	/**
	 * Sets the Yell color for a Player
	 * @param yellColor
	 */
	public void setYellColor(String yellColor) {
		this.yellColor = yellColor;
	}
	
	public int skullId;
	
	public void setSkullId(int skullId) {
		this.skullId = skullId;
	}

	public int getSkullId() {
		return skullId;
	}
}