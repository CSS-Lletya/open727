package npc.godwars.zammorak;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import npc.NPC;
import npc.combat.NPCCombatDefinitions;

public class GodwarsZammorakFaction extends NPC {

	public GodwarsZammorakFaction(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public boolean checkAgressivity() {
		NPCCombatDefinitions defs = getCombatDefinitions();
		if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
			return false;
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null || player.isDead() || !player.isActive() || !player.withinDistance(this,
							getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
									: getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16 : 8)
							|| ((!isAtMultiArea() || !player.isAtMultiArea()) && player.getAttackedBy() != this
									&& player.getAttackedByDelay() > Utils.currentTimeMillis())
							|| !clipedProjectile(player, false) || !hasGodItem(player))
						continue;
					possibleTarget.add(player);
				}
			}
			List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
			if (npcsIndexes != null) {
				for (int npcIndex : npcsIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null || npc == this || npc instanceof GodwarsZammorakFaction || npc.isDead()
							|| npc.hasFinished()
							|| !npc.withinDistance(this,
									getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
											: getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.SPECIAL
													? 16
													: 8)
							|| !npc.getDefinitions().hasAttackOption()
							|| ((!isAtMultiArea() || !npc.isAtMultiArea()) && npc.getAttackedBy() != this
									&& npc.getAttackedByDelay() > Utils.currentTimeMillis())
							|| !clipedProjectile(npc, false))
						continue;
					possibleTarget.add(npc);
				}
			}
		}
		if (!possibleTarget.isEmpty()) {
			setTarget(possibleTarget.get(Utils.getRandom(possibleTarget.size() - 1)));
			return true;
		}
		return false;
	}

	private boolean hasGodItem(Player player) {
		for (Item item : player.getEquipment().getItems().getItems()) {
			if (item == null)
				continue; // shouldn't happen
			String name = item.getDefinitions().getName();
			if (name.contains("Zamorak coif") || name.contains("Zamorak mitre") || name.contains("Zamorak full helm")
					|| name.contains("Zamorak halo") || name.contains("Torva full helm") || name.contains("Pernix cowl")
					|| name.contains("Virtus mask"))
				return true;
			else if (name.contains("Zamorak cape") || name.contains("Zamorak cloak"))
				return true;
			else if (name.contains("Unholy symbol") || name.contains("Zamorak stole"))
				return true;
			else if (name.contains("Illuminated unholy book") || name.contains("Unholy book")
					|| name.contains("Zamorak kiteshield"))
				return true;
			else if (name.contains("Zamorak arrows"))
				return true;
			else if (name.contains("Zamorak godsword") || name.contains("Zamorakian spear")
					|| name.contains("Zamorak staff") || name.contains("Zamorak crozier")
					|| name.contains("Zaryte Bow"))
				return true;
			else if (name.contains("Zamorak robe top") || name.contains("Zamorak d'hide")
					|| name.contains("Zamorak platebody") || name.contains("Torva platebody")
					|| name.contains("Pernix body") || name.contains("Virtus robe top"))
				return true;
			else if (name.contains("Zamorak robe legs") || name.contains("Zamorak robe bottom ")
					|| name.contains("Zamorak chaps") || name.contains("Zamorak platelegs")
					|| name.contains("Zamorak plateskirt") || name.contains("Torva platelegs")
					|| name.contains("Pernix chaps") || name.contains("Virtus robe legs"))
				return true;
			else if (name.contains("Zamorak vambraces"))
				return true;
		}
		return false;
	}
}
