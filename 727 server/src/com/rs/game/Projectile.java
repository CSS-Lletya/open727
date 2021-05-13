package com.rs.game;

/**
 * Represents a projectile to send.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class Projectile {
	
	/**
	 * The source node.
	 */
	private Entity source;
	
	/**
	 * The source's centered location.
	 */
	private WorldTile sourceLocation;
	
	public WorldTile getSourceLocation() {
		return sourceLocation;
	}
	
	/**
	 * The victim.
	 */
	private Entity victim;
	
	/**
	 * The projectile's gfx id.
	 */
	private int projectileId;
	
	/**
	 * The start height.
	 */
	private int startHeight;
	
	/**
	 * The ending height.
	 */
	private int endHeight;
	
	/**
	 * The delay.
	 */
	private int delay;
	
	/**
	 * The speed.
	 */
	private int speed;
	
	/**
	 * The angle.
	 */
	private int angle;
	
	/**
	 * The size of the creator
	 */
	private int creatorSize = 1;
	
	public int setCreatorSize(int size) {
		return creatorSize = size;
	}
	
	/**
	 * The distance to start.
	 */
	private int startDistanceOffset;
	
	/**
	 * The end location (used for location based projectiles).
	 */
	private WorldTile endLocation;
	
	/**
	 * Constructs a new projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The entity victim.
	 * @param projectileId
	 * 		The projectile gfx id.
	 * @param startHeight
	 * 		The start height.
	 * @param endHeight
	 * 		The end height.
	 * @param delay
	 * 		The type of the projectile.
	 * @param speed
	 * 		The projectile speed.
	 * @param angle
	 * 		The projectile angle.
	 * @param startDistanceOffset
	 * 		The distance offset.
	 */
	public Projectile(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int delay, int speed, int angle, int startDistanceOffset) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.victim = victim;
		this.projectileId = projectileId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.delay = delay;
		this.speed = speed;
		this.angle = angle;
		this.setCreatorSize(source.getSize());
		this.startDistanceOffset = startDistanceOffset;
//		System.out.println("projectileId = [" + projectileId + "], startHeight = [" + startHeight + "], endHeight = [" + endHeight + "], delay = [" + delay + "], speed = [" + speed + "], angle = [" + angle + "], startDistanceOffset = [" + startDistanceOffset + "], sourceLocation=[" + sourceLocation + "]");
	}
	
	/**
	 * Gets the source location on construction.
	 *
	 * @param entity
	 * 		The node.
	 * @return The centered location.
	 */
	public static WorldTile getLocation(Entity entity) {
		if (entity == null) {
			return null;
		}
		if (entity.isNPC()) {
			int size = entity.getSize() >> 1;
			return entity.toNPC().getLastWorldTile().transform(size, size, 0);
		}
		return entity.getLastWorldTile();
	}
	
	@Override
	public String toString() {
		return "Projectile{" + "source=" + source + ", sourceLocation=" + sourceLocation + ", victim=" + victim + ", projectileId=" + projectileId + ", startHeight=" + startHeight + ", endHeight=" + endHeight + ", delay=" + delay + ", speed=" + speed + ", angle=" + angle + ", creatorSize=" + creatorSize + ", startDistanceOffset=" + startDistanceOffset + ", endLocation=" + endLocation + '}';
	}
	
	/**
	 * Checks if the projectile is location based.
	 *
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isLocationBased() {
		return endLocation != null;
	}
}