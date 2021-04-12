package com.rs.game.rsinter;

public class RSInterfaceEvent {
	
	
	public RSInterfaceEvent(int interfaceId, int componentId, int slotId, int slotId2, int packetOpCode) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.slotId = slotId;
		this.slotId2 = slotId2;
		this.packetOpCode = packetOpCode;
	}

	private int interfaceId, componentId, slotId, slotId2, packetOpCode;

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getComponentId() {
		return componentId;
	}

	public int getSlotId() {
		return slotId;
	}

	public int getSlotId2() {
		return slotId2;
	}
	
	public int getPacketOpCode(){
		return packetOpCode;
	}

}
