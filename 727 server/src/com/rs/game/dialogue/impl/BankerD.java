package com.rs.game.dialogue.impl;

import com.rs.Settings;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import npc.NPC;

public class BankerD extends DialogueEventListener {
    public BankerD(Player player, NPC npc){
        super(player, npc);
    }

    @Override
    public void start() {
        npc(happy_plain, "Good day, How may I help you?");
        option("What would you like to say?",
                "I'd like to access my bank account, please.",
                () -> {
                    player.getBank().openBank();
                },
                "I'd like to check my PIN settings.",
                () -> {
                    player.getBankPin().openPinSettings();
                },
                "I'd like to see my collection box.",
                () -> {
                    ;
                },
                "What is this place?",
                () -> {
                    player(question, "What is this place?");
                    npc(talks_looksatplayer_looksback, "This is a branch of the Bank of " + Settings.SERVER_NAME + ". We have branches in many towns." );
                });
    }
}
