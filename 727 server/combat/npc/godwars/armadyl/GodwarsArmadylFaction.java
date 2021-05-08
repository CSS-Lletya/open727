package npc.godwars.armadyl;

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
import npc.godwars.zammorak.GodwarsZammorakFaction;

public class GodwarsArmadylFaction extends NPC {

	public GodwarsArmadylFaction(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
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
			// using else as only one item should count
			if (name.contains("Armadyl Helmet") || name.contains("Armadyl mitre") || name.contains("Armadyl full helm")
					|| name.contains("Armadyl coif") || name.contains("Torva full helm") || name.contains("Pernix cowl")
					|| name.contains("Virtus mask"))
				return true;
			else if (name.contains("Armadyl cloak"))
				return true;
			else if (name.contains("Armadyl pendant") || name.contains("Armadyl stole"))
				return true;
			else if (name.contains("Armadyl godsword") || name.contains("Armadyl crozier")
					|| name.contains("Zaryte Bow"))
				return true;
			else if (name.contains("Armadyl body") || name.contains("Armadyl robe top")
					|| name.contains("Armadyl chestplate") || name.contains("Armadyl platebody")
					|| name.contains("Torva platebody") || name.contains("Pernix body")
					|| name.contains("Virtus robe top"))
				return true;
			else if (name.contains("Illuminated book of law") || name.contains("Book of law")
					|| name.contains("Armadyl kiteshield"))
				return true;
			else if (name.contains("Armadyl robe legs") || name.contains("Armadyl plateskirt")
					|| name.contains("Armadyl chaps") || name.contains("Armadyl platelegs")
					|| name.contains("Armadyl Chainskirt") || name.contains("Torva platelegs")
					|| name.contains("Pernix chaps") || name.contains("Virtus robe legs"))
				return true;
			else if (name.contains("Armadyl vambraces"))
				return true;
		}
		return false;
	}
}
