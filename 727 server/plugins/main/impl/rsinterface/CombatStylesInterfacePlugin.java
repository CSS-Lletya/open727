package main.impl.rsinterface;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;
import player.PlayerCombat;

@RSInterfaceSignature(interfaceId = {884})
public class CombatStylesInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if (componentId == 4) {
			int weaponId = player.getEquipment().getWeaponId();
			if (PlayerCombat.hasInstantSpecial(weaponId)) {
				PlayerCombat.performInstantSpecial(player, weaponId);
				return;
			}
			World.get().submit(new Task(0) {
				@Override
				protected void execute() {
					player.getCombatDefinitions().switchUsingSpecialAttack();
					this.cancel();
				}
			});
		} else if (componentId >= 7 && componentId <= 10)
			player.getCombatDefinitions().setAttackStyle(componentId - 7);
		else if (componentId == 11)
			player.getCombatDefinitions().switchAutoRelatie();
	}
}