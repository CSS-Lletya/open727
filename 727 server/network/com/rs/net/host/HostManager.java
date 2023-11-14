package com.rs.net.host;

/**
 * The net security that handles and validates all incoming connections
 * received by the server to ensure that the server does not fall victim to
 * attacks by a socket flooder or a connection from a banned host.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class HostManager {
	
	/**
	 * The synchronized set of banned hosts.
	 */
	private static final HostList[] lists = {new HostList(HostListType.BANNED_IP), new HostList(HostListType.MUTED_IP), new HostList(HostListType.STARTER_RECEIVED),};
	
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private HostManager() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
	
	/**
	 * Determines if two players are connected from the same network.
	 * @param player the player to determine for.
	 * @param other the other player to determine for.
	 * @return {@code true} if the two players are connected from same network, {@code false} otherwise.
	 */

	
	/**
	 * Determines if the {@code text} is contained in the list.
	 */
	public static boolean contains(String text, HostListType type) {
		HostList list = lists[type.getIndex()];
		return list.contains(text);
	}
	
	/**
	 * Adds a blocked entry to the desired type.
	 */

	
	/**
	 * Removes a blocked entry to the desired type.
	 */
	public static boolean remove(String text, HostListType type, boolean save) {
		HostList list = lists[type.getIndex()];
		list.remove(text);
		if (save) {
			HostManager.serialize(type);
		}
		return true;
	}
	
	/**
	 * Serializes the desired type.
	 */
	public static void serialize(HostListType type) {
		HostList list = lists[type.getIndex()];
		list.serialize();
	}
	
	/**
	 * Deserializes the desired type.
	 */
	public static void deserialize(HostListType type) {
		HostList list = lists[type.getIndex()];
		list.deserialize();
	}
}
