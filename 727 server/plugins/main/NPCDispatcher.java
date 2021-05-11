package main;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.Settings;
import com.rs.cache.io.InputStream;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.route.strategy.RouteEvent;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import main.listener.NPCType;
import main.wrapper.NPCSignature;
import npc.NPC;

/**
 * @author Dennis
 */
public final class NPCDispatcher {
	
	/**
	 * The NPCS map which contains all the NPCS on the world.
	 */
	private static final Object2ObjectArrayMap<NPCSignature, NPCType> MOBS = new Object2ObjectArrayMap<>();
	
	/**
	 * Executes the specified NPCS if it's registered.
	 * @param player the player executing the NPCS.
	 * @param parts the string which represents a NPCS.
	 */
	public static void execute(Player player, NPC mob, int option) {
		Optional<NPCType> mobTypes = getMob(mob, mob.getId());
		
		if(!mobTypes.isPresent()) {
			player.getPackets().sendGameMessage("NPC: " + mob.getId() + " is not handled yet.");
			return;
		}
		try {
			mobTypes.get().execute(player, mob, option);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a NPCS which matches the {@code identifier}.
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<NPCType> getMob(NPC mob, int npcId) {
		for(Entry<NPCSignature, NPCType> mobType : MOBS.entrySet()) {
			if (isNPCId(mobType.getValue(), npcId) || isMobNamed(mobType.getValue(), mob)) {
				return Optional.of(mobType.getValue());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Checks if the the NPCS Id matches the signature
	 * @param mob
	 * @param npcId
	 * @return
	 */
	private static boolean isNPCId(NPCType mob, int npcId) {
		Annotation annotation = mob.getClass().getAnnotation(NPCSignature.class);
		NPCSignature signature = (NPCSignature) annotation;
		return Arrays.stream(signature.npcId()).anyMatch(id -> npcId == id);
	}
	
	/**
	 * Checks if the the NPC Name matches the signature
	 * @param mobType
	 * @param objectId
	 * @return
	 */
	private static boolean isMobNamed(NPCType mobType, NPC mob) {
		Annotation annotation = mobType.getClass().getAnnotation(NPCSignature.class);
		NPCSignature signature = (NPCSignature) annotation;
		return Arrays.stream(signature.name()).anyMatch(mobName -> mob.getName().contains(mobName));
	}
	
	/**
	 * Loads all the NPCS into the {@link #MOBS} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<NPCType> NPCTypes = Utils.getClassesInDirectory("main.impl.npcs").stream().map(clazz -> (NPCType) clazz).collect(Collectors.toList());
		
		for(NPCType mobTypes : NPCTypes) {
			if(mobTypes.getClass().getAnnotation(NPCSignature.class) == null) {
				throw new IncompleteAnnotationException(NPCSignature.class, mobTypes.getClass().getName() + " has no annotation.");
			}
			MOBS.put(mobTypes.getClass().getAnnotation(NPCSignature.class), mobTypes);
		}
	}
	
	/**
	 * Reloads all the NPCS into the {@link #MOBS} list.
	 * <p></p>
	 * <b>This method can be invoked on run-time to clear all the NPCS in the list
	 * and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		MOBS.clear();
		load();
	}
	
	public static void executeMobInteraction(final Player player, InputStream stream, int optionId) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(true);
		if (forceRun)
			player.setRun(forceRun);



		player.setRouteEvent(new RouteEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				player.faceEntity(npc);
				NPCDispatcher.execute(player, npc, optionId);
			}
		}, npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")));

	}
	
	public static void handleExamine(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort();//stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte() == 1;//stream.read128Byte() == 1;
		if (forceRun)
			player.setRun(forceRun);
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		if (player.getRights().isStaff()) {
			player.getPackets().sendGameMessage("NPC - [id=" + npc.getId() + ", loc=[" + npc.getX() + ", " + npc.getY()
					+ ", " + npc.getHeight() + "]].");
		}
		player.getPackets().sendNPCMessage(0, npc, "It's a " + npc.getDefinitions().name + ".");
		
		if (Settings.DEBUG)
			Logger.log("NPCHandler", "examined npc: " + npcIndex + ", " + npc.getId());
	}
}