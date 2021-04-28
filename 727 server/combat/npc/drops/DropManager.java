package npc.drops;

import java.util.HashMap;
import java.util.List;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import npc.NPC;

/**
 * The static-utility class that manages all of the {@link DropTable}s
 * including the process of dropping the items when an {@link Mob} is killed.
 * @author lare96 <http://github.org/lare96>
 */
public final class DropManager {
	
	/**
	 * The {@link HashMap} that consists of the drops for {@link Mob}s.
	 */
	private final static Int2ObjectOpenHashMap<DropTable> TABLES = new Int2ObjectOpenHashMap<>();
	
	/**
	 * Mob sharing the same table drop redirects.
	 */
	public final static Int2IntArrayMap REDIRECTS = new Int2IntArrayMap();
	
	/**
	 * Drops the items in {@code victim}s drop table for {@code killer}. If the
	 * killer doesn't exist, the items are dropped for everyone to see.
	 * @param killer the killer, may or may not exist.
	 * @param victim the victim that was killed.
	 */
	public static void dropItems(Player killer, NPC victim) {
		DropTable table = TABLES.get(victim.getId());
		if(table == null) {
			return;
		}
		WorldTile pos = victim.getLastWorldTile();
		final WorldTile lastMobLocation = pos;
		List<Item> dropItems = table.toItems(killer, victim);
		for (Item drop : dropItems) {
			if (drop == null)
				continue;
			World.addGroundItem(drop, lastMobLocation, killer, false, 180, true);
		}
	}
	
	public static Int2ObjectOpenHashMap<DropTable> getTables() {
		return TABLES;
	}
	
	public static Int2IntArrayMap getRedirects() {
		return REDIRECTS;
	}
}