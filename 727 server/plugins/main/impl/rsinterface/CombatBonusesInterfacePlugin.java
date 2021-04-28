package main.impl.rsinterface;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.ItemExamines;

import main.listener.RSInterface;
import main.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 667 })
public class CombatBonusesInterfacePlugin implements RSInterface {
	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, int slotId, int slotId2) throws Exception {
		Item item2 = player.getEquipment().getItem(slotId);
		System.out.println("Item : " + item2.getName());
		if (componentId == 9) {
			if (slotId >= 14)
				return;
			player.getEquipment().sendRemoveEquipment(slotId);
			refreshEquipBonuses(player);
		}
	}
	private static String names[] = { "Stab", "Slash", "Crush", "Magic", "Ranged", "Summoning", "Absorb Melee",
			"Absorb Magic", "Absorb Ranged", "Strength", "Ranged Strength", "Prayer", "Magic Damage" };

	public static void refreshEquipBonuses(Player player) {
		player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
		for (int i = 0; i < 18; i++) {
			String bonusName = (new StringBuilder(String.valueOf(names[i <= 4 ? i : i - 5]))).append(": ").toString();
			int bonus = player.getCombatDefinitions().getBonuses()[i];
			bonusName = (new StringBuilder(String.valueOf(bonusName))).append(bonus >= 0 ? "+" : "").append(bonus)
					.toString();
			if (i == 17 || i > 10 && i < 14)
				bonusName = (new StringBuilder(String.valueOf(bonusName))).append("%").toString();

			// Weights.calculateWeight(player);
			player.getPackets().sendIComponentText(667, 28 + i, bonusName);
		}

	}

}