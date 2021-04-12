package com.rs.game.player.controlers.fightpits;

import com.rs.game.WorldObject;
import com.rs.game.minigames.FightPits;
import com.rs.game.player.actions.FightPitsViewingOrb;
import com.rs.game.player.controlers.Controler;

public class FightPitsLobby extends Controler {

	@Override
	public void start() {

	}

	@Override
	public boolean login() {
		FightPits.enterLobby(player, true);
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		FightPits.leaveLobby(player, 2);
	}

	// fuck it dont dare touching here again or dragonkk(me) kills u irl :D btw nice code it keeps nulling, fixed

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 68223) {
			FightPits.leaveLobby(player, 1);
			return false;
		} else if (object.getId() == 68222) {
			player.getPackets().sendGameMessage("The heat prevents you passing through.");
			return false;
		} else if (object.getId() == 68220) {
			player.getActionManager().setAction(new FightPitsViewingOrb());
			return false;
		}
		return true;
	}

	/**
	 * return let default death
	 */
	@Override
	public boolean sendDeath() {
		// if somehow dies on lobby example poisoned
		FightPits.leaveLobby(player, 2);
		return true;
	}

	@Override
	public boolean logout() {
		FightPits.leaveLobby(player, 0);
		return false;
	}

	@Override
	public void forceClose() {
		FightPits.leaveLobby(player, 2);
	}

}
