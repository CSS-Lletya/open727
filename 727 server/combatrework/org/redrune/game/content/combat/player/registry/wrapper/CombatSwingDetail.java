package org.redrune.game.content.combat.player.registry.wrapper;

import java.util.function.Consumer;

import com.rs.game.Entity;
import com.rs.game.Hit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class CombatSwingDetail {
	
	/**
	 * The source of the swing
	 */
	private final Entity source;
	
	/**
	 * The target of the swing
	 */
	private final Entity target;
	
	/**
	 * The hit of the swing
	 */
	private final Hit hit;
	
	public CombatSwingDetail(Entity source, Entity target, Hit hit) {
		this.source = source;
		this.target = target;
		this.hit = hit;
	}
	
	/**
	 * Accepts the swing to the consumer
	 *
	 * @param consumer
	 * 		The consumer
	 */
	public CombatSwingDetail consume(Consumer<CombatSwingDetail> consumer) {
		consumer.accept(this);
		return this;
	}
}
