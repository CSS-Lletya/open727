package com.rs.game.rsinter;

import com.rs.game.player.Player;

public abstract class RSInterfaceEventListener {
	
	public static final int[] ORDINAL_BUTTON = { 96, 27, 68, 9, 72, 19, 23, 21, 22, 81 };
	
	private RSInterfaceEvent rsinter;
	
	public RSInterfaceEventListener setRSInterface(RSInterfaceEvent rsinter){
		this.rsinter = rsinter;
		return this;
	}

	public abstract boolean start(Player player);
	
	public abstract boolean listenAndPerform(Player player);
	
	public int getOption(){
		for (int button = 0; button < ORDINAL_BUTTON.length; button++)
			if (rsinter.getPacketOpCode() == ORDINAL_BUTTON[button])
				return button + 1;
		return -1;
	}
	
	public int interfaceId() {
		return rsinter.getInterfaceId();
	}

	public int componentId() {
		return rsinter.getComponentId();
	}

	public int slotId() {
		return rsinter.getSlotId();
	}

	public int slotId2() {
		return rsinter.getSlotId2();
	}
}
