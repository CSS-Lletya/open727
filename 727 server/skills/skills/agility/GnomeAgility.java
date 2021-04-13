package skills.agility;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

import skills.Skills;

public class GnomeAgility {

	// gnome course

	public static void walkGnomeLog(final Player player) {
		if (player.getX() != 2474 || player.getY() != 3436)
			return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(2474, 3429, -1, false);
		player.getPackets().sendGameMessage("You walk carefully across the slippery log...", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					setGnomeStage(player, 0);
					player.getSkills().addXp(Skills.AGILITY, 7.5);
					player.getPackets().sendGameMessage("... and make it safely to the other side.", true);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void climbGnomeObstacleNet(final Player player) {
		if (player.getY() != 3426)
			return;
		player.getPackets().sendGameMessage("You climb the netting.", true);
		player.useStairs(828, new WorldTile(player.getX(), 3423, 1), 1, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 0)
					setGnomeStage(player, 1);
				player.getSkills().addXp(Skills.AGILITY, 7.5);
			}
		}, 1);
	}

	public static void climbUpGnomeTreeBranch(final Player player) {
		player.getPackets().sendGameMessage("You climb the tree...", true);
		player.useStairs(828, new WorldTile(2473, 3420, 2), 1, 2, "... to the plantaform above.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 1)
					setGnomeStage(player, 2);
				player.getSkills().addXp(Skills.AGILITY, 5);
			}
		}, 1);
	}

	public static void walkBackGnomeRope(final Player player) {
		if (player.getX() != 2483 || player.getY() != 3420 || player.getPlane() != 2)
			return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(7);
		player.addWalkSteps(2477, 3420, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 7);
					player.getPackets().sendGameMessage("You passed the obstacle succesfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

	public static void walkGnomeRope(final Player player) {
		if (player.getX() != 2477 || player.getY() != 3420 || player.getPlane() != 2)
			return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(7);
		player.addWalkSteps(2483, 3420, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					if (getGnomeStage(player) == 2)
						setGnomeStage(player, 3);
					player.getSkills().addXp(Skills.AGILITY, 7);
					player.getPackets().sendGameMessage("You passed the obstacle succesfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

	public static void climbDownGnomeTreeBranch(final Player player) {
		player.useStairs(828, new WorldTile(2487, 3421, 0), 1, 2, "You climbed the tree branch succesfully.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 3)
					setGnomeStage(player, 4);
				player.getSkills().addXp(Skills.AGILITY, 5);
			}
		}, 1);
	}

	public static void climbGnomeObstacleNet2(final Player player) {
		if (player.getY() != 3425)
			return;
		player.getPackets().sendGameMessage("You climb the netting.", true);
		player.useStairs(828, new WorldTile(player.getX(), player.getY() == 3425 ? 3428 : 3425, 0), 1, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 4)
					setGnomeStage(player, 5);
				player.getSkills().addXp(Skills.AGILITY, 8);
			}
		}, 1);
	}

	public static void enterGnomePipe(final Player player, int objectX, int objectY) {
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(objectX, objectY == 3431 ? 3437 : 3430, -1, false);
		player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(295);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 7);
					if (getGnomeStage(player) == 5) {
						removeGnomeStage(player);
						player.getSkills().addXp(Skills.AGILITY, 39.5);

					}
					stop();
				}
			}
		}, 0, 6);
	}

	public static void removeGnomeStage(Player player) {
		player.getTemporaryAttributtes().remove("GnomeCourse");
	}

	public static void setGnomeStage(Player player, int stage) {
		player.getTemporaryAttributtes().put("GnomeCourse", stage);
	}

	public static int getGnomeStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributtes().get("GnomeCourse");
		if (stage == null)
			return -1;
		return stage;
	}
}
