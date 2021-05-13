package main.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import main.listener.Command;
import main.wrapper.CommandSignature;

@CommandSignature(alias = {"Bank"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens Bank")
public final class BankCommand implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
        player.getBank().openBank();
    }
}