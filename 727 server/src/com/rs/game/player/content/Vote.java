package com.rs.game.player.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Vote {

	public static void checkVote(final Player player, final String auth) {
		if (player.getTemporaryAttributtes().get("CheckingVote") != null)
			return;
		if (Utils.invalidAuthId(auth)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"The authentication id you specified couldn't be found in our database,",
					"check your authentication id on invalid characters.");
			return;
		}
		player.getTemporaryAttributtes().put("CheckingVote", Boolean.TRUE);
		player.getDialogueManager().startDialogue("SimpleMessage", "Verifying your authentication id...");
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(
							"http://www.matrixftw.com/includes/modules/vote/check_vote.php?authentication=apacheownsyou&auth="
									+ auth.toLowerCase());
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					final String line = reader.readLine();
					reader.close();
					if (line.equals("SUCCESS_AUTH_" + auth)) {
						player.setVoted(player.isDonator() || player.isExtremeDonator() ? 172800000 : 86400000);
						player.getDialogueManager().startDialogue("SimpleMessage",
								"Thank you for voting, you've received the availability to wear",
								"prod items for 24 hours.");
					} else if (line.startsWith("SUCCESS_GIFT_" + auth)) {
						String[] weekly_gift = line.split("_");
						player.setVoted(player.isDonator() || player.isExtremeDonator() ? 172800000 : 86400000);
						player.getBank().addItem(Integer.parseInt(weekly_gift[3]), Integer.parseInt(weekly_gift[4]),
								false);
						player.getDialogueManager().startDialogue("SimpleMessage",
								"Thank you for voting, you've received the availability to wear",
								"prod items for 24 hours and a weekly gift on your bank.");
						player.getPackets()
								.sendGameMessage("You have recieved: "
										+ ItemDefinitions.getItemDefinitions(Integer.parseInt(weekly_gift[3])).getName()
										+ ", in your bank.");
					} else
						player.getDialogueManager().startDialogue("SimpleMessage",
								"The authentication id you specified couldn't be found in our database,",
								"do ::vote for a vote authentication id.");
				} catch (IOException e) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"The authentication id you specified couldn't be found in our database,",
							"do ::vote for a vote authentication id.");
					e.printStackTrace();
				}
				player.getTemporaryAttributtes().remove("CheckingVote");
			}
		});
	}
}
