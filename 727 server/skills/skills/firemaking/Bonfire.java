package skills.firemaking;

import java.util.ArrayList;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.others.FireSpirit;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

import skills.Skills;

public class Bonfire extends Action {

	public static enum Log {

		LOG(1511, 3098, 1, 50, 6), OAK(1521, 3099, 15, 75, 12), WILLOW(1519, 3101, 30, 112.5, 18), MAPLE(1517, 3100, 45,
				157, 36), EUCALYPTUS(12581, 3112, 58, 241, 48), YEWS(1515, 3111, 60, 252, 54), MAGIC(1513, 3135, 75,
						378, 60), BLISTERWOOD(21600, 3113, 76, 378, 60), CURSED_MAGIC(13567, 3116, 82, 378, 60);
		private int logId, gfxId, level, boostTime;
		private double xp;

		private Log(int logId, int gfxId, int level, double xp, int boostTime) {
			this.logId = logId;
			this.gfxId = gfxId;
			this.level = level;
			this.xp = xp;
			this.boostTime = boostTime;
		}

		public int getLogId() {
			return logId;
		}

	}

	private Log log;
	private WorldObject object;
	private int count;

	public Bonfire(Log log, WorldObject object) {
		this.log = log;
		this.object = object;
	}

	private boolean checkAll(Player player) {
		if (!World.getRegion(object.getRegionId()).containsObject(object.getId(), object))
			return false;
		if (!player.getInventory().containsItem(log.logId, 1))
			return false;
		if (player.getSkills().getLevel(Skills.FIREMAKING) < log.level) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need level " + log.level + " Firemaking to add these logs to a bonfire.");
			return false;
		}
		return true;
	}

	public static boolean addLog(Player player, WorldObject object, Item item) {
		for (Log log : Log.values())
			if (log.logId == item.getId()) {
				player.getActionManager().setAction(new Bonfire(log, object));
				return true;
			}
		return false;
	}

	public static void addLogs(Player player, WorldObject object) {

		ArrayList<Log> possiblities = new ArrayList<Log>();
		for (Log log : Log.values())
			if (player.getInventory().containsItem(log.logId, 1))
				possiblities.add(log);
		Log[] logs = possiblities.toArray(new Log[possiblities.size()]);
		if (logs.length == 0)
			player.getPackets().sendGameMessage("You do not have any logs to add to this fire.");
		else if (logs.length == 1)
			player.getActionManager().setAction(new Bonfire(logs[0], object));
		else
			player.getDialogueManager().startDialogue("BonfireD", logs, object);
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			player.getAppearance().setRenderEmote(2498);
			return true;
		}
		return false;

	}

	@Override
	public boolean process(Player player) {
		if (checkAll(player)) {
			if (Utils.random(500) == 0) {
				new FireSpirit(new WorldTile(object, 1), player);
				player.getPackets().sendGameMessage("<col=ff0000>A fire spirit emerges from the bonfire.");
			}
			return true;
		}
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		player.getInventory().deleteItem(log.logId, 1);
		player.getSkills().addXp(Skills.FIREMAKING, Firemaking.increasedExperience(player, log.xp));
		player.setNextAnimation(new Animation(16703));
		player.setNextGraphics(new Graphics(log.gfxId));
		player.getPackets().sendGameMessage("You add a log to the fire.", true);
		if (count++ == 4 && player.getLastBonfire() == 0) {
			player.setLastBonfire(log.boostTime * 100);
			int percentage = (int) (getBonfireBoostMultiplier(player) * 100 - 100);
			player.getPackets().sendGameMessage("<col=00ff00>The bonfire's warmth increases your maximum health by "
					+ percentage + "%. This will last " + log.boostTime + " minutes.");
			player.getEquipment().refreshConfigs(false);
		}
		return 6;
	}

	public static double getBonfireBoostMultiplier(Player player) {
		int fmLvl = player.getSkills().getLevel(Skills.FIREMAKING);
		if (fmLvl >= 90)
			return 1.1;
		if (fmLvl >= 80)
			return 1.09;
		if (fmLvl >= 70)
			return 1.08;
		if (fmLvl >= 60)
			return 1.07;
		if (fmLvl >= 50)
			return 1.06;
		if (fmLvl >= 40)
			return 1.05;
		if (fmLvl >= 30)
			return 1.04;
		if (fmLvl >= 20)
			return 1.03;
		if (fmLvl >= 10)
			return 1.02;
		return 1.01;

	}

	@Override
	public void stop(final Player player) {
		player.getEmotesManager().setNextEmoteEnd(2400);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextAnimation(new Animation(16702));
				player.getAppearance().setRenderEmote(-1);

			}

		}, 3);
	}

}
