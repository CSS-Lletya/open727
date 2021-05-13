package main.impl.objects;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Ladder", "Staircase"})
public class Ladder implements ObjectType {

	private final short LUMBRIDGE_CELLAR_LADDER = 29355;

	@Override
	public void execute(Player player, WorldObject object, int optionId) throws Exception {
		player.setNextAnimation(new Animation(object.getDefinitions().getNameContaining("ladder") ? 828 : -1));
		if (object.getId() == LUMBRIDGE_CELLAR_LADDER)
			player.task(0, p -> p.setNextWorldTile(new WorldTile(3210, 3216, 0)));
		else
			player.task(0, p -> {
				if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb")) {
					player.dialog(new DialogueEventListener(player) {
						
						@Override
						public void start() {
							option("Climb where to",
									"Climb-Up", () -> {
										p.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getHeight() + 1));
									},
									"Climb-Down", () -> {
										player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getHeight() - 1));
									}
							);
						}
					});
				} else if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-up"))
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getHeight() + 1));
				else if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-down"))
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getHeight() - 1));
			});
	}
}