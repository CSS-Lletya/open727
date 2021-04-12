package com.rs.game.rsinter;

import com.rs.game.player.Player;
import com.rs.game.rsinter.container.EquipBonusesRSInterface;

public class InteractRSInterface {
	
	private static final RSInterfaceEventListener[] RS_INTERFACES = new RSInterfaceEventListener[1400];
	
	static {
		
		RS_INTERFACES[667] = new EquipBonusesRSInterface();
		RS_INTERFACES[670] = new EquipBonusesRSInterface();
		
	}
	
	public static RSInterfaceEventListener getListener(int interfaceId){
		return RS_INTERFACES[interfaceId];
	}

	public static boolean performInteraction(int interfaceId, int componentId, int slotId, int slotId2, int op_code, Player player){
		
		RSInterfaceEventListener listener = getListener(interfaceId);
		
		if (listener == null){
			//System.out.println("Listener does not exist!");
			return false;
		}
		
		listener.setRSInterface(new RSInterfaceEvent(interfaceId, componentId, slotId, slotId2, op_code)).listenAndPerform(player);
		
		return true;
	}
}
