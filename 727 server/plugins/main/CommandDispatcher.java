package main;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import main.listener.Command;
import main.wrapper.CommandSignature;

/**
 * The manager class of commands which will dispatch executable commands.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CommandDispatcher {
	
	/**
	 * The object map which contains all the commands on the world.
	 */
	private static final Object2ObjectArrayMap<CommandSignature, Command> COMMANDS = new Object2ObjectArrayMap<>();
	
	/**
	 * Executes the specified {@code string} if it's a command.
	 * @param player the player executing the command.
	 * @param parts the string which represents a command.
	 */
	public static void execute(Player player, String[] parts, String command) {
		Optional<Command> cmd = getCommand(parts[0]);
		
		if(!cmd.isPresent()) {
			player.getPackets().sendGameMessage("Command [;;" + parts[0] + "] does not exist!");
			return;
		}
		
		if(!hasPrivileges(player, cmd.get())) {
			player.getPackets().sendGameMessage("You don't have the privileges required to use this command.");
			return;
		}
		
		try {
			cmd.get().execute(player, parts, command);
		} catch(Exception e) {
			e.printStackTrace();
			sendSyntax(player, cmd.get());
		}
	}
	
	/**
	 * Gets a command which matches the {@code identifier}.
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<Command> getCommand(String identifier) {
		for(Entry<CommandSignature, Command> command : COMMANDS.entrySet()) {
			for(String s : command.getKey().alias()) {
				if(s.equalsIgnoreCase(identifier)) {
					return Optional.of(command.getValue());
				}
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Sends the correct syntax of usage to the player for the
	 * specified {@code command}.
	 * @param player the player to send this syntax for.
	 * @param command the command that was misinterpreted.
	 */
	private static void sendSyntax(Player player, Command command) {
		Annotation annotation = command.getClass().getAnnotation(CommandSignature.class);
		CommandSignature sig = (CommandSignature) annotation;
		player.getPackets().sendGameMessage("[COMMAND SYNTAX] " + sig.syntax());
	}
	
	/**
	 * Checks if the player has the privileges to execute this command.
	 * @param player the player executing this command.
	 * @param command the command that was executed.
	 * @return <true> if the command was executed, <false> otherwise.
	 */
	private static boolean hasPrivileges(Player player, Command command) {
		Annotation annotation = command.getClass().getAnnotation(CommandSignature.class);
		CommandSignature sig = (CommandSignature) annotation;
		if (player.getRights().isStaff()) {
			return true;
		}
		return Arrays.stream(sig.rights()).anyMatch(right -> player.getRights().equals(right));
	}
	
	/**
	 * Loads all the commands into the {@link #COMMANDS} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		
		List<Command> commands = Utils.getClassesInDirectory("main.impl.commands").stream().map(clazz -> (Command) clazz).collect(Collectors.toList());
		
		for(Command command : commands) {
			if(command.getClass().getAnnotation(CommandSignature.class) == null) {
				throw new IncompleteAnnotationException(CommandSignature.class, command.getClass().getName() + " has no annotation.");
			}
			COMMANDS.put(command.getClass().getAnnotation(CommandSignature.class), command);
		}
	}
	
	/**
	 * Reloads all the commands into the {@link #COMMANDS} list.
	 * <p></p>
	 * <b>This method can be invoked on run-time to clear all the commands in the list
	 * and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		COMMANDS.clear();
		load();
	}

	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) {
		if (command.length() == 0)
			return false;
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0)
			return false;
		if (clientCommand) {
			switch (cmd[0]) {
			//NOTE: There's a bug where in World Map if you shift click it crashes client, idk why.
			case "tele":
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextWorldTile(new WorldTile(x, y, plane));
				return true;
			}
		}
		CommandDispatcher.execute(player, cmd, command);
		return false;
	}
	
}