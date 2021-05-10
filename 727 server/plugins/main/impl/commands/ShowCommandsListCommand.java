package main.impl.commands;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.utils.Utils;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"commandslist"}, rights = {Rights.ADMINISTRATOR}, syntax = "Lists all commands")
public final class ShowCommandsListCommand implements Command {

    @Override
    public void execute(Player player, String[] cmd, String command) throws Exception {
        player.getInterfaceManager().sendInterface(275);

        List<Command> commands = Utils.getClassesInDirectory("main.impl.commands").stream().map(clazz -> (Command) clazz).collect(Collectors.toList());

        List<String> text = new ArrayList<>();
        text.add("~~Commands~~");
        for(Command commandSig : commands) {
            if (commandSig.getClass().getAnnotation(CommandSignature.class) == null) {
                throw new IncompleteAnnotationException(CommandSignature.class, commandSig.getClass().getName() + " has no annotation.");
            }

            String commandAlias = commandSig.getClass().getAnnotation(CommandSignature.class).alias()[0];
            String commandSyntax = commandSig.getClass().getAnnotation(CommandSignature.class).syntax();
            text.add("<col=a40000>" + commandAlias + "<col=000000>: " + commandSyntax);
        }

        player.getInterfaceManager().sendInterface(275);
        int messageRow = 0;
        for(int componentID = 1; componentID <= 150; componentID++) {
            if (componentID >= 2 && componentID <= 9)//Skip container components
                continue;
            if (messageRow < text.size())
                player.getPackets().sendIComponentText(275, componentID, text.get(messageRow));
            else
                player.getPackets().sendIComponentText(275, componentID, "");
            messageRow++;
        }
    }
}
