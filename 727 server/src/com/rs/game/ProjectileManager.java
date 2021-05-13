package com.rs.game;

import java.util.stream.Stream;

import com.rs.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class ProjectileManager {
	
	/**
	 * Sends a projectile
	 *
	 * @param projectile
	 * 		The projectile
	 */
	public static void sendProjectile(Projectile projectile) {
		WorldTile sourceLocation = projectile.getSourceLocation();
		Stream<Player> closeByPlayers = World.get().getPlayers().stream().filter(player -> player.getLastWorldTile().withinDistance(sourceLocation, 16));
		closeByPlayers.forEach(player -> player.getTransmitter().send(new ProjectilePacketBuilder(projectile).build(player)));
	}
	
	/**
	 * Creates a new projectile with the speed being calculated, based on the distance from each other [source-target]
	 *
	 * @param source
	 * 		The source of the projectile
	 * @param target
	 * 		The target of the projectile
	 * @param projectileId
	 * 		The id of the projectile
	 * @param startHeight
	 * 		The start height of the projectile
	 * @param endHeight
	 * 		The end height of the projectile
	 * @param delay
	 * 		The delay on the projectile
	 * @param angle
	 * 		The angle of the projectile
	 * @param offset
	 * 		The distance offset
	 */
	public static Projectile createSpeedDefinedProjectile(Entity source, Entity target, int projectileId, int startHeight, int endHeight, int delay, int angle, int offset) {
		return new Projectile(source, target, projectileId, startHeight, endHeight, delay, getSpeedModifier(source, target), angle, offset);
	}
	
	/**
	 * Gets the projectile speed modifier
	 *
	 * @param source
	 * 		The source
	 * @param target
	 * 		The target
	 */
	public static int getSpeedModifier(Entity source, Entity target) {
		return 46 + (getLocation(source).getDistance(target.getLastWorldTile()) * 5);
	}
	
	/**
	 * Gets the source location on construction.
	 *
	 * @param n
	 * 		The node.
	 * @return The centered location.
	 */
	public static WorldTile getLocation(Entity n) {
		if (n == null) {
			return null;
		}
		return n.getCenterLocation();
	}
	
	/**
	 * Gets the delay of a ranged/magic hit.
	 *
	 * @param attacker
	 * 		the attacking mob.
	 * @param victim
	 * 		the victim mob.
	 * @param delay
	 * 		the show delay of the projectile.
	 * @param speed
	 * 		the speed of the projectile (or slowness...the higher the speed the slower the delay).
	 * @return the delay of a hit.
	 */
	public static double getDelay(Entity attacker, Entity victim, int delay, int speed) {
		/* The distance between the entities. */
		double distance = attacker.getLastWorldTile().getDistance(victim.getLastWorldTile());
		
		/* The speed at which the projectile is traveling. */
		double projectileSpeed = (delay + speed + distance) * 5;
		
		/* The delay of the hit. */
		double hitDelay = (projectileSpeed * .02857);
		
		/* Returns the hit delay. */
		return hitDelay;
	}
	
	/**
	 * Gets the delay before a projectile can arrive at a target.
	 *
	 * @param source
	 * 		The source
	 * @param target
	 * 		The target
	 */
	public static int getProjectileDelay(Entity source, Entity target) {
		return 1 + (int) Math.ceil(source.getLastWorldTile().getDistance(target.getLastWorldTile()) * 0.3);
	}
}
