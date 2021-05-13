
package com.rs.game.item;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

public class ItemConstants {

	public static int getDegradeItemWhenWear(int id) {
		// pvp armors
		if (id == 13958 || id == 13961 || id == 13964 || id == 13967 || id == 13970 || id == 13973 || id == 13858
				|| id == 13861 || id == 13864 || id == 13867 || id == 13870 || id == 13873 || id == 13876 || id == 13884
				|| id == 13887 || id == 13890 || id == 13893 || id == 13896 || id == 13899 || id == 13902 || id == 13905
				|| id == 13908 || id == 13911 || id == 13914 || id == 13917 || id == 13920 || id == 13923 || id == 13926
				|| id == 13929 || id == 13932 || id == 13935 || id == 13938 || id == 13941 || id == 13944 || id == 13947
				|| id == 13950 || id == 13958)
			return id + 2; // if you wear it it becomes corrupted LOL
		return -1;
	}

	// return amt of charges
	public static int getItemDefaultCharges(int id) {
		// pvp armors
		if (id == 13910 || id == 13913 || id == 13916 || id == 13919 || id == 13922 || id == 13925 || id == 13928
				|| id == 13931 || id == 13934 || id == 13937 || id == 13940 || id == 13943 || id == 13946 || id == 13949
				|| id == 13952)
			return 1500;
		if (id == 13960 || id == 13963 || id == 13966 || id == 13969 || id == 13972 || id == 13975)
			return 3000;
		if (id == 13860 || id == 13863 || id == 13866 || id == 13869 || id == 13872 || id == 13875 || id == 13878
				|| id == 13886 || id == 13889 || id == 13892 || id == 13895 || id == 13898 || id == 13901 || id == 13904
				|| id == 13907 || id == 13960)
			return 6000; // 1hour
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149 || id == 20153 || id == 20157 || id == 20161
				|| id == 20165 || id == 20169 || id == 20173)
			return 60000;
		return -1;
	}

	// return what id it degrades to, -1 for disapear which is default so we
	// dont add -1
	public static int getItemDegrade(int id) {
		if (id == 11285) // DFS
			return 11283;
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149 || id == 20153 || id == 20157 || id == 20161
				|| id == 20165 || id == 20169 || id == 20173)
			return id + 1;
		return -1;
	}

	public static int getDegradeItemWhenCombating(int id) {
		// nex armors
		if (id == 20135 || id == 20139 || id == 20143 || id == 20147 || id == 20151 || id == 20155 || id == 20159
				|| id == 20163 || id == 20167 || id == 20171)
			return id + 2;
		return -1;
	}

	public static boolean itemDegradesWhileHit(int id) {
		if (id == 2550)
			return true;
		return false;
	}

	public static boolean itemDegradesWhileWearing(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		if (name.contains("c. dragon") || name.contains("corrupt dragon") || name.contains("vesta's")
				|| name.contains("statius'") || name.contains("morrigan's") || name.contains("zuriel's"))
			return true;
		return false;
	}

	public static boolean itemDegradesWhileCombating(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		// nex armors
		if (name.contains("torva") || name.contains("pernix") || name.contains("virtux") || name.contains("zaryte"))
			return true;
		return false;
	}

	public static boolean canWear(Item item, Player player) {
		if (player.getRights() == Rights.ADMINISTRATOR)
			return true;
		/**
		 * Specific condition checks can be execute here.
		 */
		return true;
	}

	public static boolean isTradeable(Item item) {
		if (item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended()
				|| ItemConstants.getItemDefaultCharges(item.getId()) != -1)
			return false;
		
		if (!item.getDefinitions().exchangableItem) {
			return false;
		}
		if (item.getDefinitions().getName().toLowerCase().contains("flaming skull"))
			return false;
		switch (item.getId()) {
		case 6570: // firecape
		case 6529: // tokkul
		case 7462: // barrow gloves
		case 23659: // tookhaar-kal
			return false;
		default:
			return true;
		}
	}
	
	public static boolean turnCoins(Item item) {
		if (item.getDefinitions().getName().toLowerCase().contains("(deg)"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("strength cape"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("max cape"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("max hood"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("completionist cape"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("completionist hood"))
			return true;
		switch (item.getId()) {
		case 10887:
		case 7462:
		case 7461:
		case 18349:
		case 18351:
		case 18353:
		case 18355:
		case 18357:
		case 18359:
		case 18361:
		case 18363:
		case 18335:
		case 18334:
		case 18333:
			return true;
		default:
			return false;
		}
	}
	
	public static int getSpecialEnergy(int weaponId) {
		if (weaponId == -1) {
			return 0;
		}
		ItemDefinitions definition = ItemDefinitions.forId(weaponId);
		if (definition.isLended()) {
			weaponId = definition.getLendId();
		}
		switch (weaponId) {
			case 4587: // dragon sci
			case 859: // magic longbow
			case 861: // magic shortbow
			case 10284: // Magic composite bow
			case 18332: // Magic longbow (sighted)
			case 19149:// zamorak bow
			case 19151:
			case 19143:// saradomin bow
			case 19145:
			case 19146:
			case 19148:// guthix bow
				return 55;
			case 11235: // dark bows
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				return 65;
			case 13899: // vls
			case 13901:
			case 1305: // dragon long
			case 1215: // dragon dagger
			case 5698: // dds
			case 1434: // dragon mace
			case 1249:// d spear
			case 1263:
			case 3176:
			case 5716:
			case 5730:
			case 13770:
			case 13772:
			case 13774:
			case 13776:
			case 11716:
				return 25;
			case 15442:// whip start
			case 15443:
			case 15444:
			case 15441:
			case 4151:
			case 23691:
			case 11698: // sgs
			case 23681:
			case 11694: // ags
			case 23679:
			case 13904:
			case 13905: // vesta spear
			case 13907:
			case 14484: // d claws
			case 23695:
			case 10887: // anchor
			case 3204: // d hally
			case 4153: // granite maul
			case 15241: // hand cannon
			case 13908:
			case 13954:// morrigan javelin
			case 13955:
			case 13956:
			case 13879:
			case 13880:
			case 13881:
			case 13882:
			case 13883:// morigan thrown axe
			case 13957:
				return 50;
			case 11730: // ss
			case 23690:
			case 11696: // bgs
			case 23680:
			case 11700: // zgs
			case 23682:
			case 35:// Excalibur
			case 8280:
			case 14632:
			case 1377:// dragon battle axe
			case 13472:
			case 15486:// staff of lights
			case 22207:
			case 22209:
			case 22211:
			case 22213:
				return 100;
			case 19784: // korasi sword
			case 7158: // d2h
			case 21371: // vine whip
				return 60;
			case 14684: // zanik cbow
				return 50;
			case 13902: // statius hammer
				return 35;
			default:
				return 0;
		}
	}
}
