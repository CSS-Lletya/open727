package main.impl.npcs;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.dialogue.impl.BankerD;
import com.rs.game.player.Player;

import com.rs.game.route.CoordsEvent;
import com.rs.game.route.strategy.RouteEvent;
import com.rs.utils.Utils;
import main.NPCDispatcher;
import main.listener.NPCType;
import main.wrapper.NPCSignature;
import npc.NPC;

@NPCSignature(name = {"Banker"}, npcId = {})
public class Banker implements NPCType {
	Player player;
	NPC npc;
	int option;

	WorldTile NEEdgeTile = new WorldTile(3099, 3494, 0);
	WorldTile SouthEdgeTile = new WorldTile(3096, 3487, 0);
	WorldTile[] badBankerTiles = new WorldTile[] {
			NEEdgeTile,
			SouthEdgeTile,
	};

	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		this.player = player;
		this.npc = npc;
		this.option = option;
		if(isBadBankerTile()) {
			goCorrectPath();
			return;
		}
		player.dialog(new BankerD(player, npc));
	}

	private boolean isBadBankerTile() {
		for(WorldTile badTile : badBankerTiles)
			if(player.matches(badTile))
				return true;
		return false;
	}

	private void goCorrectPath() {
		if(player.matches(NEEdgeTile)) {
			player.addWalkSteps(3099, 3497);
			Utils.runLater(runToBanker(), 800);
		} else if(player.matches(SouthEdgeTile)) {
			player.addWalkSteps(3090, 3487);
			Utils.runLater(runSouthDoor(), 3000);
			Utils.runLater(runToBanker(), 4000);
		}
	}

	private Runnable runToBanker() {
		return new Runnable() {
			@Override
			public void run() {
				player.setRouteEvent(new RouteEvent(npc, new Runnable() {
					@Override
					public void run() {
						npc.resetWalkSteps();
						player.faceEntity(npc);
						NPCDispatcher.execute(player, npc, option);
					}
				}, true));
			}
		};
	}

	private Runnable runSouthDoor() {
		return new Runnable() {
			@Override
			public void run() {
				player.addWalkSteps(3090, 3490);
			}
		};
	}
}