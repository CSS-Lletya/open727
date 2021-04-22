package com.rs.game.discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.ExceptionLogger;

import com.rs.Launcher;
import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.player.Player;

public class DiscordBot {
	
	private final String token = "YOUR_TOKEN_HERE";
	private final DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
	Player player;
	
	public DiscordBot() {
        api.addMessageCreateListener(event -> {
        	if (event.getMessageContent().isEmpty()) {
        		return;
        	}
        	String[] command = event.getMessageContent().substring(1).split(" ");
        	switch(command[0]) {
        	case "cache":
        		StringBuilder cacheLinkStringBuilder = new StringBuilder();
        		cacheLinkStringBuilder.append("Download the Cache contents and place it in your Server/data/cache/ \n")
        		.append("```https://mega.nz/file/ItUHlQhY#MSrZ7s4jJQDVo6LrsHYubczAgQ3HA91qPBu3ID7vZQQ```");
        		
        		Launcher.getDiscordBot().getAPI().getTextChannelById(DiscordBot.SERVER_RELAY).ifPresent(channel -> {
					  channel.sendMessage(cacheLinkStringBuilder.toString()).exceptionally(ExceptionLogger.get());
				});
        		break;
        	case "client":
        		StringBuilder clientLinkStringBuilder = new StringBuilder();
        		clientLinkStringBuilder.append("Due to copyright concerns the Client was removed from the repository.\n")
        		.append("Standard 727 Deob Download: \n")
        		.append("```https://mega.nz/#!IplRFBhB!UkDvOtyI2Q0XL3GLt-D7hO0YTgVv6W8FwMsSNyeyJ3k```\n")
        		.append("Partly Renamed 727 Deob Download: \n")
        		.append("```https://github.com/titandino/darkan-game-client```\n")
        		.append("If the Mega link dies, contact Zed on Discord!");
        		
        		Launcher.getDiscordBot().getAPI().getTextChannelById(DiscordBot.SERVER_RELAY).ifPresent(channel -> {
					  channel.sendMessage(clientLinkStringBuilder.toString()).exceptionally(ExceptionLogger.get());
				});
        		break;
        	case "sqlsetup":
        		StringBuilder sqlSetupStringBuilder = new StringBuilder();
        		sqlSetupStringBuilder.append("To get started with setting up the MYSQL Database you must first setup your web server. There are alternative methods as well, please research for any possible options for you.")
        		.append("```\nhttps://www.rose-hulman.edu/class/se/csse290-WebProgramming/201520/SupportCode/SQL-setup.html")
        		.append("\n```");

        		Launcher.getDiscordBot().getAPI().getTextChannelById(DiscordBot.SERVER_RELAY).ifPresent(channel -> {
					  channel.sendMessage(sqlSetupStringBuilder.toString()).exceptionally(ExceptionLogger.get());
				});
        		break;
        	case "dialoguesetup":
        		StringBuilder dialogueStringBuilder = new StringBuilder();
        		dialogueStringBuilder.append("To get started with using the new Dialogue system visit:\n")
        		.append("```")
        		.append("https://github.com/CSS-Lletya/open727/blob/37fcc8264715980d67e9a9a7a832a75479ba518d/727%20server/plugins/main/impl/npcs/TestNPC.java")
        		.append("\n```");
        		
        		Launcher.getDiscordBot().getAPI().getTextChannelById(DiscordBot.SERVER_RELAY).ifPresent(channel -> {
					  channel.sendMessage(dialogueStringBuilder.toString()).exceptionally(ExceptionLogger.get());
				});
        		break;
        	case "tasksetup":
        		StringBuilder taskStringBuilder = new StringBuilder();
        		taskStringBuilder.append("To get started with using the new Task handler system visit:\n")
        		.append("```")
        		.append("https://github.com/CSS-Lletya/open727/tree/main/727%20server/src/com/rs/game/task")
        		.append("\n```");
        		
        		Launcher.getDiscordBot().getAPI().getTextChannelById(DiscordBot.SERVER_RELAY).ifPresent(channel -> {
					  channel.sendMessage(taskStringBuilder.toString()).exceptionally(ExceptionLogger.get());
				});
        		break;
        	case "commands":
        		StringBuilder commandsStringBuilder = new StringBuilder();
        		commandsStringBuilder.append("Here's a full list of the Discord Bot commands.```")
        		.append("!tasksetup - Learn how to use the new Task handler system\n")
        		.append("!sqlsetup - Learn how to setup your MYSQL database\n")
        		.append("!client - Get the download link for the Client(s)\n")
        		.append("!cache - Get the download link for the official open727 cache.")
        		.append("```");
        		
        		Launcher.getDiscordBot().getAPI().getTextChannelById(DiscordBot.SERVER_RELAY).ifPresent(channel -> {
					  channel.sendMessage(commandsStringBuilder.toString()).exceptionally(ExceptionLogger.get());
				});
        		break;
        	case "players":
				StringBuilder players = new StringBuilder();
				players.append("```There " + (World.getPlayers().size() == 1 ? "is" : "are") + " currently "
						+ World.getPlayers().size() + " "
						+ (World.getPlayers().size() == 1 ? "person" : "people") + " playing "+ Settings.SERVER_NAME + "!"
						+ "\n");
				for (Player player : World.getPlayers()) {
					if (player == null)
						continue;
					players.append(player.getDisplayName() + ", ");
				}
				Launcher.getDiscordBot().getAPI().getTextChannelById(DiscordBot.SERVER_RELAY).ifPresent(channel -> {
					  channel.sendMessage("[SERVER] " + players.replace(players.length() - 2, players.length(), "!") + "```").exceptionally(ExceptionLogger.get());
					});
				break;
        	}
        });
	}
	
	public DiscordApi getAPI() {
		return api;
	}
	
	public static Long SERVER_RELAY = 831519484208087100L;
}