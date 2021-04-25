package com.rs.game.npc.combat.rework;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

/**
 * TODO: Redo Drops
 * @author Dennis
 */
public final class NPCCombatDispatcher {
	
	/**
	 * The object map which contains all the mob on the world.
	 */
	private static final Object2ObjectArrayMap<MobCombatSignature, MobCombatInterface> COMBATANTS = new Object2ObjectArrayMap<>();
	
	private static int mobValue = 0;
	
	/**
	 * Executes the specified mob if it's registered.
	 * @param player the player executing the mob.
	 * @param parts the string which represents a mob.
	 * @throws Exception 
	 */
	public static int execute(Player player, NPC npc) {
		Optional<MobCombatInterface> mobCombat = getMobCombatant(npc.getId());
		mobCombat.ifPresent(value -> {
			try {
				mobValue = value.execute(player, npc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return mobValue;
	}

	/**
	 * Gets a mob which matches the {@code identifier}.
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<MobCombatInterface> getMobCombatant(int interfaceId) {
		for(Entry<MobCombatSignature, MobCombatInterface> MobCombatInterface : COMBATANTS.entrySet()) {
			if (isMobId(MobCombatInterface.getValue(), interfaceId) || isMobNamed(null, null)) {
				return Optional.of(MobCombatInterface.getValue());
			}
		}
		return Optional.empty();
	}
	
	private static boolean isMobId(MobCombatInterface MobCombatInterface, int mobId) {
		Annotation annotation = MobCombatInterface.getClass().getAnnotation(MobCombatSignature.class);
		MobCombatSignature signature = (MobCombatSignature) annotation;
		return Arrays.stream(signature.mobId()).anyMatch(id -> mobId == id);
	}
	
	/**
	 * Checks if the the NPC Name matches the signature
	 * @param mobType
	 * @param objectId
	 * @return
	 */
	private static boolean isMobNamed(MobCombatInterface mobType, NPC mob) {
		Annotation annotation = mobType.getClass().getAnnotation(MobCombatSignature.class);
		MobCombatSignature signature = (MobCombatSignature) annotation;
		return Arrays.stream(signature.mobName()).anyMatch(mobName -> mob.getName().contains(mobName));
	}
	
	/**
	 * Loads all the mob into the {@link #COMBATANTS} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<MobCombatInterface> interfaces = Utils.getClassesInDirectory("com.rs.game.npc.combat.rework.impl").stream().map(clazz -> (MobCombatInterface) clazz).collect(Collectors.toList());
		
		for(MobCombatInterface MobCombatInterface : interfaces) {
			if(MobCombatInterface.getClass().getAnnotation(MobCombatSignature.class) == null) {
				throw new IncompleteAnnotationException(MobCombatSignature.class, MobCombatInterface.getClass().getName() + " has no annotation.");
			}
			COMBATANTS.put(MobCombatInterface.getClass().getAnnotation(MobCombatSignature.class), MobCombatInterface);
		}
	}
	
	/**
	 * Reloads all the mob into the {@link #COMBATANTS} list.
	 * <p></p>
	 * <b>This method can be invoked on run-time to clear all the commands in the list
	 * and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		COMBATANTS.clear();
		load();
	}
	
}