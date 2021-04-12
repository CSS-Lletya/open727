package com.rs.game.player.content;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.utils.Utils;

public final class LivingRockCavern {

	private static enum Rocks {
		COAL_ROCK_1(new WorldObject(5999, 10, 1, 3690, 5146, 0)), COAL_ROCK_2(new WorldObject(5999, 10, 2, 3690, 5125,
				0)), COAL_ROCK_3(new WorldObject(5999, 10, 0, 3687, 5107, 0)), COAL_ROCK_4(
						new WorldObject(5999, 10, 1, 3674, 5098, 0)), COAL_ROCK_5(new WorldObject(5999, 10, 2, 3664,
								5090, 0)), COAL_ROCK_6(new WorldObject(5999, 10, 3, 3615, 5090, 0)), COAL_ROCK_7(
										new WorldObject(5999, 10, 1, 3625, 5107, 0)), COAL_ROCK_8(
												new WorldObject(5999, 10, 3, 3647, 5142, 0)), GOLD_ROCK_1(
														new WorldObject(45076, 10, 1, 3667, 5075, 0)), GOLD_ROCK_2(
																new WorldObject(45076, 10, 0, 3637, 5094,
																		0)), GOLD_ROCK_3(
																				new WorldObject(45076, 10, 0, 3677,
																						5160, 0)), GOLD_ROCK_4(
																								new WorldObject(45076,
																										10, 1, 3629,
																										5148, 0));

		private Rocks(WorldObject rock) {
			this.rock = rock;
		}

		private WorldObject rock;

	}

	private LivingRockCavern() {

	}

	private static void respawnRock(final Rocks rock) {
		World.spawnObject(rock.rock, false);
		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				removeRock(rock);
			}
		}, Utils.random(8) + 3, TimeUnit.MINUTES);
	}

	private static void removeRock(final Rocks rock) {
		World.destroySpawnedObject(rock.rock, false);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				respawnRock(rock);
			}

		}, 3, TimeUnit.MINUTES);
	}

	public static void init() {
		for (Rocks rock : Rocks.values())
			respawnRock(rock);
	}
}
