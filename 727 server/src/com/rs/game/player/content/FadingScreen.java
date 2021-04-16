package com.rs.game.player.content;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public final class FadingScreen {

	@Deprecated
	public static void fade(final Player player, long fadeTime, final Runnable event) {
		unfade(player, fade(player, fadeTime), event);
	}

	public static void fade(final Player player, final Runnable event) {
		player.lock();
		unfade(player, fade(player), event);
	}

	public static void unfade(final Player player, long startTime, final Runnable event) {
		player.lock();
		unfade(player, 2500, startTime, event);
	}

	public static void unfade(final Player player, long endTime, long startTime, final Runnable event) {
		long leftTime = endTime - (Utils.currentTimeMillis() - startTime);
		if (leftTime > 0) {
			CoresManager.slowExecutor.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						unfade(player, event);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}

			}, leftTime, TimeUnit.MILLISECONDS);
		} else
			unfade(player, event);
	}

	public static void unfade(final Player player, Runnable event) {
		event.run();
		World.get().submit(new Task(0) {
			@Override
			protected void execute() {
				player.getInterfaceManager().sendFadingInterface(170);
				CoresManager.slowExecutor.schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							player.getInterfaceManager().closeFadingInterface();
							player.unlock();
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}
				}, 2, TimeUnit.SECONDS);
				this.cancel();
			}
		});
	}

	public static long fade(Player player, long fadeTime) {
		player.getInterfaceManager().sendFadingInterface(115);
		return Utils.currentTimeMillis() + fadeTime;
	}

	public static long fade(Player player) {
		return fade(player, 0);
	}
}