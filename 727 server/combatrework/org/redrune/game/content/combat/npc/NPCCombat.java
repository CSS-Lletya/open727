package org.redrune.game.content.combat.npc;

import org.redrune.game.content.combat.NPCConstants;
import org.redrune.utility.repository.npc.combat.NPCCombatSwingRepository;
import org.redrune.utility.tool.Misc;

import com.rs.game.Entity;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public final class NPCCombat {
	
	/**
	 * The npc who owns this combat
	 */
	private final NPC npc;
	
	/**
	 * The delay until the next swing
	 */
	private int delay;
	
	/**
	 * The target
	 */
	private Entity target;
	
	/**
	 * Constructs a new npc combat instance
	 *
	 * @param npc
	 * 		The npc who owns this instance
	 */
	public NPCCombat(NPC npc) {
		this.npc = npc;
	}
	
	/**
	 * Handles the processing of combat
	 */
	public boolean process() {
		if (delay > 0) {
			delay--;
		}
		if (target != null) {
			npc.faceEntity(target);
			if (!verifyContinuation()) {
				removeTarget();
				return false;
			}
			if (delay <= 0) {
				delay = fireNPCSwing();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Fires the combat swing
	 */
	private int fireNPCSwing() {
		Entity target = this.target;
		if (target == null) {
			return 0;
		}
		// the definitions
		NPCCombatDefinitions definitions = npc.getCombatDefinitions();
		// gets the default attack style to use
		int attackStyle = definitions.getAttackStyle();
		// if we are frozen we can't keep fighting
		if (npc.isLocked()) {
			if (attackStyle == NPCConstants.MELEE_COMBAT_STYLE) {
				return 0;
			}
		}
		// TODO:
		/*
		if (target instanceof Familiar && (npc.getLureDelay() == 0 || Utils.random(3) == 0)) {
			Familiar familiar = (Familiar) target;
			Player player = familiar.getOwner();
			if (player != null) {
				target = player;
				npc.setTarget(target);
				addAttackedByDelay(target);
			}
		} else if (target instanceof Dreadnip) {
			Dreadnip dreadnip = (Dreadnip) target;
			Player player = dreadnip.getOwner();
			if (player != null) {
				target = player;
				npc.setTarget(player);
			}
		} */
		// the distance we must be at
		int maxDistance = attackStyle == NPCConstants.MELEE_COMBAT_STYLE ? 0 : 9;
		// if we are distanced
		boolean distanced = !Misc.isOnRange(npc, target, maxDistance + (npc.getMovement().hasWalkSteps() && target.getMovement().hasWalkSteps() ? (target.getMovement().isRunning() ? 2 : 1) : 0));
		// if the unfollowable state is set
		boolean unfollowable = !npc.getCombatManager().isCantFollowDuringCombat() && Misc.colides(npc, target);
		if ((!npc.getMovement().clippedProjectileToNode(target, maxDistance == 0 && !forceCheckClipAsRange(target))) || distanced || unfollowable) {
			return 0;
		}
		return NPCCombatSwingRepository.fire(npc, target);
	}
	
	/**
	 * Verifys that combat can continue
	 */
	private boolean verifyContinuation() {
		Entity target = this.target;
		// make sure we have a target
		if (target == null) {
			return false;
		}
		// if we are invalid to fight
		if (npc.isDead() || !npc.isRenderable() || npc.isForceWalking() || target.isDead() || !target.isRenderable() || npc.getLocation().getPlane() != target.getLocation().getPlane()) {
			return false;
		}
		// cant move if frozen
		if (npc.isFrozen()) {
			return true;
		}
		int distanceX = npc.getLocation().getX() - npc.getSpawnLocation().getX();
		int distanceY = npc.getLocation().getY() - npc.getSpawnLocation().getY();
		int size = npc.getSize();
		int maxDistance;
		if (!npc.getCombatManager().isNoDistanceCheck() && !npc.getCombatManager().isCantFollowDuringCombat()) {
			maxDistance = 16;
			if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
				npc.traverseOriginalTile();
				return false;
			}
			distanceX = target.getLocation().getX() - npc.getLocation().getX();
			distanceY = target.getLocation().getY() - npc.getLocation().getY();
			if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
				return false; // if target distance higher 16
			}
		} else {
			distanceX = target.getLocation().getX() - npc.getLocation().getX();
			distanceY = target.getLocation().getY() - npc.getLocation().getY();
		}
		if (!npc.getCombatManager().isForceMultiAttacked()) {
			if (!target.isAtMultiArea() || !npc.isAtMultiArea()) {
				if (npc.getAttackedBy() != target && npc.getAttackedByDelay() > System.currentTimeMillis()) {
					return false;
				}
				if (target.getAttackedBy() != npc && target.getAttackedByDelay() > System.currentTimeMillis()) {
					return false;
				}
			}
		}
		
		if (!npc.getCombatManager().isCantFollowDuringCombat()) {
			// if is under
			int targetSize = target.getSize();
			if (distanceX < size && distanceX > -targetSize && distanceY < size && distanceY > -targetSize && !target.getMovement().hasWalkSteps()) {
				npc.getMovement().resetWalkSteps();
				if (!npc.getMovement().addWalkSteps(target.getLocation().getX() + 1, npc.getLocation().getY())) {
					npc.getMovement().resetWalkSteps();
					if (!npc.getMovement().addWalkSteps(target.getLocation().getX() - size, npc.getLocation().getY())) {
						npc.getMovement().resetWalkSteps();
						if (!npc.getMovement().addWalkSteps(npc.getLocation().getX(), target.getLocation().getY() + 1)) {
							npc.getMovement().resetWalkSteps();
							if (!npc.getMovement().addWalkSteps(npc.getLocation().getX(), target.getLocation().getY() - size)) {
								return true;
							}
						}
					}
				}
				return true;
			}
			if (npc.getCombatDefinitions().getAttackStyle() == NPCConstants.MELEE_COMBAT_STYLE && targetSize == 1 && size == 1 && Math.abs(npc.getLocation().getX() - target.getLocation().getX()) == 1 && Math.abs(npc.getLocation().getY() - target.getLocation().getY()) == 1 && !target.getMovement().hasWalkSteps()) {
				
				if (!npc.getMovement().addWalkSteps(target.getLocation().getX(), npc.getLocation().getY())) {
					npc.getMovement().addWalkSteps(npc.getLocation().getX(), target.getLocation().getY());
				}
				return true;
			}
			
			int attackStyle = npc.getCombatDefinitions().getAttackStyle();
			// the maximum distance between the npc and target, based on the combat type.
			maxDistance = npc.getCombatManager().isForceFollowClose() ? 0 : (attackStyle == NPCConstants.MELEE_COMBAT_STYLE) ? 0 : 9;
			// reset the walk steps
			npc.getMovement().resetWalkSteps();
			boolean clippedProjectileToNode = npc.getMovement().clippedProjectileToNode(target, maxDistance == 0 && !forceCheckClipAsRange(target));
			boolean onRange = !Misc.isOnRange(npc.getLocation().getX(), npc.getLocation().getY(), size, target.getLocation().getX(), target.getLocation().getY(), targetSize, maxDistance);
			// check if we need to walk to the target
			if ((!clippedProjectileToNode) || onRange) {
				npc.getMovement().addEntityPath(target, 2, npc.getCombatManager().isIntelligentRouteFinder());
				return true;
			}
		}
		return true;
	}
	
	/**
	 * If we are fighting somebody
	 */
	public boolean fighting() {
		return target != null;
	}
	
	/**
	 * Removes the target
	 */
	public void removeTarget() {
		this.target = null;
		npc.turnTo(null);
	}
	
	/**
	 * Resets the combat
	 */
	public void reset() {
		removeTarget();
		delay = 0;
	}
	
	/**
	 * If we should check the clipping flags as range
	 *
	 * @param target
	 * 		The target
	 */
	private boolean forceCheckClipAsRange(Entity target) {
		//		return target instanceof PestPortal;
		return false;
	}
	
	/**
	 * Handles the setting of a target
	 *
	 * @param target
	 * 		The target
	 */
	public void setTarget(Entity target) {
		this.target = target;
		if (target != null) {
			npc.turnTo(target);
		}
		if (!verifyContinuation()) {
			removeTarget();
		}
	}
	
}
