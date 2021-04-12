package com.rs.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.rs.cores.CoresManager;
import com.rs.game.player.Player;

public class Donations {

	public static final void checkDonation(final Player player) {
		if (player.isExtremeDonator())
			return;
		if (player.getTemporaryAttributtes().get("CheckingDonation") != null)
			return;
		player.getTemporaryAttributtes().put("CheckingDonation", Boolean.TRUE);
		player.getPackets().sendGameMessage("Checking donation...");
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL("http://matrixftw.com/donate/checkdonate.php?username="
							+ player.getUsername().toLowerCase());
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					String string = reader.readLine();
					boolean noresult = string == null || string.length() <= 0 || string.equalsIgnoreCase("false");
					reader.close();
					if (noresult)
						player.getPackets().sendGameMessage(
								"<col=ff0000>We were unable to locate your donation, please try again later.");
					else {
						if (string.equalsIgnoreCase("1"))
							player.setExtremeDonator(true);
						else
							player.setDonator(true);
						SerializableFilesManager.savePlayer(player);
						player.getPackets().sendGameMessage("<col=00ff00>Congratulations! You are now a "
								+ (string == "1" ? "extreme " : "") + "donator.");
					}
				} catch (Throwable e) {
					player.getPackets().sendGameMessage(
							"<col=ff0000>We were unable to verify your donation, please try again later.");
				}
				player.getTemporaryAttributtes().remove("CheckingDonation");
			}
		});
	}
}

/*
 * package com.rs.utils;
 * 
 * import java.io.BufferedReader; import java.io.ByteArrayInputStream; import java.io.ByteArrayOutputStream; import java.io.InputStreamReader; import java.util.concurrent.TimeUnit;
 * 
 * import org.apache.commons.net.ftp.FTPClient;
 * 
 * import com.rs.cores.CoresManager; import com.rs.game.World; import com.rs.game.player.Player;
 * 
 * public class Donations {
 * 
 * private static final String HOST = "matrixrsps.com"; private static final String LOGIN = "donations@matrixrsps.com"; private static final String PASSWORD = "vEDh*SIeW;+K"; private static FTPClient client;
 * 
 * public static void init() { if(Settings.HOSTED) { client = new FTPClient(); addDonationsCheckTask(); } }
 * 
 * @SuppressWarnings("unused") private static void addDonationsCheckTask() { CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
 * 
 * @Override public void run() { try { checkDonations(); }catch(Throwable e) { e.printStackTrace(); } } }, 0, 3, TimeUnit.MINUTES); }
 * 
 * private static void checkDonations() { try { if(!client.isConnected()) { client.connect(HOST); client.login(LOGIN, PASSWORD); client.setTcpNoDelay(true); client.setConnectTimeout(5000); } ByteArrayOutputStream stream = new ByteArrayOutputStream(); if(!client.retrieveFile("Plimus_TEMP.log", stream)) return; client.deleteFile("Plimus_TEMP.log"); BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(stream.toByteArray()))); String line; while((line = in.readLine()) != null) { String[] details = line.split(" - ", 3); if(details.length != 3) continue; try { int productId = Integer.valueOf(details[0]); int quantity = Integer.valueOf(details[1]); String username = details[2]; processProduct(productId, quantity, username); }catch(Throwable e) { e.printStackTrace(); } } }catch(Throwable e) { e.printStackTrace(); } }
 * 
 * private static void processProduct(int productId, int quantity, String username) { if(productId == 852182) { Player target = World.getPlayerByDisplayName(username); boolean online = true; if(target == null) { online = false; String formatedUsername = Utils.formatPlayerNameForProtocol(username); target = SerializableFilesManager.loadPlayer(formatedUsername); if(target != null) target.setUsername(formatedUsername); } if(target == null) return; target.makeDonator(quantity); if(!online) SerializableFilesManager.savePlayer(target); else target.getPackets().sendGameMessage ("You've been promoted to donator till "+target.getDonatorTill()); } }
 * 
 * private Donations() {
 * 
 * } }
 */
