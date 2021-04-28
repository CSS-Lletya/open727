package npc.godwars;

public final class GodWarsBosses {

	public static final GodWarMinion[] graardorMinions = new GodWarMinion[3];
	public static final GodWarMinion[] commanderMinions = new GodWarMinion[3];
	public static final GodWarMinion[] zamorakMinions = new GodWarMinion[3];
	public static final GodWarMinion[] armadylMinions = new GodWarMinion[3];

	public static void respawnBandosMinions() {
		for (GodWarMinion minion : graardorMinions) {
			if (minion.hasFinished() || minion.isDead())
				minion.respawn();
		}
	}

	public static void respawnSaradominMinions() {
		for (GodWarMinion minion : commanderMinions) {
			if (minion.hasFinished() || minion.isDead())
				minion.respawn();
		}
	}

	public static void respawnZammyMinions() {
		for (GodWarMinion minion : zamorakMinions) {
			if (minion.hasFinished() || minion.isDead())
				minion.respawn();
		}
	}

	public static void respawnArmadylMinions() {
		for (GodWarMinion minion : armadylMinions) {
			if (minion.hasFinished() || minion.isDead())
				minion.respawn();
		}
	}

	private GodWarsBosses() {

	}
}
