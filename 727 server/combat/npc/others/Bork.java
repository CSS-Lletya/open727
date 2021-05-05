package npc.others;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import npc.NPC;

public class Bork extends NPC {

	public static long deadTime;

	public Bork(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(Entity source) {
		deadTime = System.currentTimeMillis() + (1000 * 60 * 60);
		resetWalkSteps();
		for (Entity e : getPossibleTargets()) {
			if (e instanceof Player) {
				final Player player = (Player) e;
				player.getInterfaceManager().sendInterface(693);
//				player.getDialogueManager().startDialogue("DagonHai", 7137, player, 1);
				//TODO: Dialogue
				World.get().submit(new Task(8) {
					@Override
					protected void execute() {
						player.stopAll();
						this.cancel();
					}
				});
			}
		}
		getCombat().removeTarget();
		setNextAnimation(new Animation(getCombatDefinitions().getDeathEmote()));
		World.get().submit(new Task(4) {
			@Override
			protected void execute() {
				drop();
				reset();
				setLocation(getRespawnTile());
				finish();
				if (!isSpawned())
					setRespawnTask();
				this.cancel();
			}
		});
	}

	@Override
	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setLocation(getRespawnTile());
			finish();
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawn();
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Error e) {
					e.printStackTrace();
				}
			}
		}, 1, TimeUnit.HOURS);
	}

	public static String convertToTime() {
		String time = "You have to wait " + (getTime() == 0 ? "few more seconds" : getTime() + " mins")
				+ " to kill bork again.";
		return time;
	}

	public static int getTime() {
		return (int) (deadTime - System.currentTimeMillis() / 60000);
	}

	public static boolean atBork(WorldTile tile) {
		if ((tile.getX() >= 3083 && tile.getX() <= 3120) && (tile.getY() >= 5522 && tile.getY() <= 5550))
			return true;
		return false;
	}
}
