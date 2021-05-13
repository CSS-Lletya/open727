package org.redrune.game.content.combat.player.registry.wrapper.context;

import org.redrune.game.content.combat.player.swing.MagicCombatSwing;

import com.rs.game.Entity;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class CombatSpellContext extends MagicSpellContext {
	
	/**
	 * The target of the spell
	 */
	private final Entity target;
	
	public Entity getTarget() {
		return target;
	}
	
	/**
	 * The combat swing of the spell
	 */
	private final MagicCombatSwing swing;
	
	public MagicCombatSwing getSwing() {
		return swing;
	}
	
	public CombatSpellContext(Entity target, MagicCombatSwing swing) {
		this.target = target;
		this.swing = swing;
	}
}
