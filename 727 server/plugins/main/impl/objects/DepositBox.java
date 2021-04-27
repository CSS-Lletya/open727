package main.impl.objects;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;

import main.listener.ObjectType;
import main.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Bank deposit box"})
public class DepositBox implements ObjectType {

	@Override
	public void execute(Player player, WorldObject object, int optionId) throws Exception {
		System.out.println("General response");
		
		if (optionId == 1)
			player.getBank().openDepositBox();
		
		if (object.getDefinitions().getNameContaining("Bank deposit box"))
			System.out.println("Yeah it's a deposit box for sure.");
		
		if (object.getDefinitions().containsOption("Deposit"))
			System.out.println("Respnding to your Bank option");
	}
}