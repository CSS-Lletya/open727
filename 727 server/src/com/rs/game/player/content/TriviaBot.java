package com.rs.game.player.content;

import com.rs.game.World;
import com.rs.game.player.Player;

import java.util.Random;

/**
 * @Author: Apache Ah64
 */
public class TriviaBot {

	private static String questions[][] = { { "What is Santa Claus's real name?", "Nick" },
			{ "In what year did Jagex release dungeoneering?", "2010" },
			{ "What was the first obsidian weapon Jagex released?", "Dark dagger" },
			{ "Who is the co founder of Jagex Studios?", "Andrew Gower" },
			{ "Who is the creator of RuneScape?", "Andrew Gower" },
			{ "What was the first minigame in RuneScape?", "Clan wars" }, { "What gaming genre is Matrix?", "MMORPG" },
			{ "In what year was RuneScape established?", "2001" },
			{ "In what year was RuneScape HD released?", "2008" },
			{ "What is the Matrix game playing mode? (Multiplayer/Solo)", "Multiplayer" },
			{ "What is maximum combat level in Matrix?", "138" }, { "Is a tomato a fruit or a vegetable?", "Fruit" },
			{ "How many legs does a spider have?", "8" }, { "Why is activity Monte Carlo mostly known for?", "Casino" },
			{ "Who sings the song 'I wanna be a billionaire'?", "Travy Mccoy" },
			{ "Until what year did the beatles exist?", "1970" },
			{ "Did RuneScape ever obtain the item Life rune? (Yes/No)", "Yes" },
			{ "What was the Falador Masaacre?", "A glitch" },
			{ "In what year was the Duplication Glitch in RuneScape?", "2003" },
			{ "What month of 2011 was Matrix released to the public", "October" },
			{ "Who created the Trivia Bot?", "Apache Ah64" },
			{ "What is the attack requirement for Goliath Gloves?", "80" },
			{ "Where is the Wise Old Man located at?", "Draynor Manor" },
			{ "What is the most powerful curse?", "Turmoil" },
			{ "How much of a percentage does a dragon dagger special requires?", "25%" },
			{ "What color does a donator sign have?", "Green" }, { "What's the name of the dungeon master?", "Thok" },
			{ "What is the best free to play armour?", "Rune" }, { "Where do you get Zeals at?", "Soul wars" },
			{ "What amulet does Imp Catcher give?", "Amulet of Accuracy" },
			{ "Fill out the good part of the name in the omitted part, `...... the mad�?", "Melzar" },
			{ "Which Non Player Character drops sigils?", "Corporeal beast" },
			{ "What do you receive when a fire disappears?", "Ashes" },
			{ "What is the name of the new firecape?", "TokHaar-Kal" } };

	public static int questionid = -1;
	public static int round = 0;
	public static boolean victory = false;

	public TriviaBot() {
		// TODO
	}

	public static void Run() {
		int rand = RandomQuestion();
		questionid = rand;
		victory = false;
		for (Player participant : World.getPlayers()) {
			if (participant == null)
				continue;
			if (TriviaArea(participant)) {
				participant.getPackets().sendGameMessage("[Trivia]" + questions[rand][0]);
			}
		}
	}

	public static void sendRoundWinner(String winner) {
		for (Player participant : World.getPlayers()) {
			if (participant == null)
				continue;
			if (TriviaArea(participant)) {
				victory = true;
				participant.getPackets()
						.sendGameMessage("[Trivia]Congratulations, " + winner + " won round " + round + ".");
			}
		}
	}

	public static void verifyAnswer(final Player player, String answer) {
		if (victory) {
			player.getPackets().sendGameMessage("That round has already been won, wait for the next round.");
		} else if (questions[questionid][1].equalsIgnoreCase(answer)) {
			round++;
			sendRoundWinner(player.getDisplayName());
		} else {
			player.getPackets().sendGameMessage("That answer wasn't correct, please try it again.");
		}
	}

	public static int RandomQuestion() {
		int random = 0;
		Random rand = new Random();
		random = rand.nextInt(questions.length);
		return random;
	}

	public static boolean TriviaArea(final Player participant) {
		if (participant.getX() >= 2630 && participant.getX() <= 2660 && participant.getY() >= 9377
				&& participant.getY() <= 9400) {
			return true;
		}
		return false;
	}
}
