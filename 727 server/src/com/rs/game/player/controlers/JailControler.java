package com.rs.game.player.controlers;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.utils.Utils;

public class JailControler extends Controler {

	@Override
	public void start() {
		if (player.getPlayerDetails().getJailed() > Utils.currentTimeMillis())
			player.sendRandomJail(player);
	}

	@Override
	public void process() {
		if (player.getPlayerDetails().getJailed() <= Utils.currentTimeMillis()) {
			player.getControlerManager().getControler().removeControler();
			player.getPackets().sendGameMessage("Your account has been unjailed.", true);
			player.setNextWorldTile(new WorldTile(2677, 10379, 0));
		}
	}

	public static void stopControler(Player p) {
		p.getControlerManager().getControler().removeControler();
	}

	@Override
	public boolean sendDeath() {
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(1, () -> player.setNextAnimation(new Animation(836)));
		seq.connect(3, () -> {
			player.getPackets().sendGameMessage("Oh dear, you have died.");
			player.setNextAnimation(new Animation(-1));
			player.reset();
			player.setCanPvp(false);
			player.sendRandomJail(player);
			player.unlock();
		});
		seq.start();
		return false;
	}

	@Override
	public boolean login() {

		return false;
	}

	@Override
	public boolean logout() {

		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getPackets().sendGameMessage("You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getPackets().sendGameMessage("You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		player.getPackets().sendGameMessage("You cannot do any activities while being jailed.");
		return false;
	}

}
