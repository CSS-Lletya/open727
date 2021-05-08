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
import com.rs.game.player.Player;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

/**
 * @author Dennis
 */
public final class RSInterfaceDispatcher {
	
	/**
	 * The object map which contains all the interface on the world.
	 */
	private static final Object2ObjectArrayMap<RSInterfaceSignature, RSInterface> INTERFACES = new Object2ObjectArrayMap<>();
	
	/**
	 * Executes the specified interface if it's registered.
	 * @param player the player executing the interface.
	 * @param parts the string which represents a interface.
	 */
	public static void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		Optional<RSInterface> rsInterface = getRSInterface(interfaceId);
		if(!rsInterface.isPresent()) {
			player.getPackets().sendGameMessage(interfaceId + " is not handled yet.");
			return;
		}
	
		try {
			rsInterface.get().execute(player, interfaceId, componentId, packetId, slotId, slotId2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a interface which matches the {@code identifier}.
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<RSInterface> getRSInterface(int interfaceId) {
		for(Entry<RSInterfaceSignature, RSInterface> rsInterface : INTERFACES.entrySet()) {
			if (isInterface(rsInterface.getValue(), interfaceId)) {
				return Optional.of(rsInterface.getValue());
			}
		}
		return Optional.empty();
	}
	
	private static boolean isInterface(RSInterface rsInterface, int interfaceId) {
		Annotation annotation = rsInterface.getClass().getAnnotation(RSInterfaceSignature.class);
		RSInterfaceSignature signature = (RSInterfaceSignature) annotation;
		return Arrays.stream(signature.interfaceId()).anyMatch(right -> interfaceId == right);
	}
	
	/**
	 * Loads all the interface into the {@link #INTERFACES} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<RSInterface> interfaces = Utils.getClassesInDirectory("main.impl.rsinterface").stream().map(clazz -> (RSInterface) clazz).collect(Collectors.toList());
		
		for(RSInterface rsInterface : interfaces) {
			if(rsInterface.getClass().getAnnotation(RSInterfaceSignature.class) == null) {
				throw new IncompleteAnnotationException(RSInterfaceSignature.class, rsInterface.getClass().getName() + " has no annotation.");
			}
			INTERFACES.put(rsInterface.getClass().getAnnotation(RSInterfaceSignature.class), rsInterface);
		}
	}
	
	/**
	 * Reloads all the interface into the {@link #INTERFACES} list.
	 * <p></p>
	 * <b>This method can be invoked on run-time to clear all the commands in the list
	 * and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		INTERFACES.clear();
		load();
	}
	
	@SuppressWarnings("unused")
	public static void handleButtons(final Player player, InputStream stream, int packetId) {
		int interfaceHash = stream.readIntLE();
		int interfaceId = interfaceHash >> 16;
		if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			return;
		}
		if (player.isDead() || player.isLocked()) {
			return;
		}
		if (!player.getInterfaceManager().containsInterface(interfaceId)) {
			return;
		}
		final int componentId = interfaceHash - (interfaceId << 16);
		if (componentId != 65535 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			return;
		}
		final int slotId2 = stream.readUnsignedShort();
		final byte slotId = (byte) stream.readUnsignedShort128();
		final int itemId = stream.readUnsignedShortLE128();
		RSInterfaceDispatcher.execute(player, interfaceId, componentId, packetId, slotId, slotId2);
		
		if (Settings.DEBUG)
			Logger.log("ButtonHandler", "Interface ID: " + interfaceId + " - Comonent: " + componentId + " - PacketId: " + packetId);
	}
}