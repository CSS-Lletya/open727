package com.rs.game;

/**
 * A container class that represents an Entity Animation (NPC, Player, Object,
 * Item, such).
 * 
 * @author Dennis
 *
 */
public final class Animation {

	/**
	 * A list of possible animations Id's to execute.
	 */
	private int[] ids;

	/**
	 * The speed of the Animation.
	 */
	private short speed;

	/**
	 * Represents a negative Animation response (Example: Digging (831) is looped,
	 * so we use this to force reset the animation).
	 */
	public static final byte RESET_ANIMATION = -1;

	/**
	 * Creates a new Animation.
	 * 
	 * @param id
	 */
	public Animation(int id) {
		this(id, (short) 0);
	}

	/**
	 * Creates a new Animation.
	 * 
	 * @param id
	 * @param speed
	 */
	public Animation(int id, short speed) {
		this(id, id, id, id, speed);
	}

	/**
	 * Creates a new Animation.
	 * 
	 * @param id1
	 * @param id2
	 * @param id3
	 * @param id4
	 * @param speed
	 */
	public Animation(int id1, int id2, int id3, int id4, short speed) {
		this.ids = new int[] { id1, id2, id3, id4 };
		this.speed = speed;
	}

	/**
	 * Gets a list of possible Animations.
	 * 
	 * @return
	 */
	public int[] getIds() {
		return ids;
	}

	/**
	 * Gets the speed of the Animation.
	 * 
	 * @return
	 */
	public short getSpeed() {
		return speed;
	}
}