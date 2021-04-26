package player.type.impl;

import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.player.Player;

import player.type.AntifireDetails;
import player.type.AntifireDetails.AntifireType;
import player.type.CombatEffect;

/**
 * The class which is responsible for the effect when you drink an anti-fire potion.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CombatAntifireEffect extends CombatEffect {

	/**
	 * The type of antifire this player has drunk.
	 */
	private final AntifireType type;

	/**
	 * Constructs a new {@link CombatAntifireEffect}.
	 * @param type {@link #type}.
	 */
	public CombatAntifireEffect(AntifireType type) {
		super(1);
		this.type = type;
	}

	@Override
	public boolean apply(Entity c) {
		if(!(c instanceof Player)) {
			return false;
		}
		System.out.println("hey");
		Player player = (Player) c;
		if(player.getAntifireDetails().isPresent()) {
			player.setAntifireDetail(new AntifireDetails(type));
			return false;
		}
		player.setAntifireDetail(new AntifireDetails(type));
		return true;
	}

	@Override
	public boolean removeOn(Entity c) {
		if(c instanceof Player) {
			Player player = (Player) c;
			return !player.getAntifireDetails().isPresent() ? true : false;
		}
		return true;
	}

	@Override
	public void process(Entity c) {
		if (c instanceof Player) {
			Player player = (Player) c;
			
			if (player.getAntifireDetails().isPresent()) {
				AntifireDetails detail = player.getAntifireDetails().get();
				int count = detail.getAntifireDelay().decrementAndGet();
				if (count == 30) {
					player.getPackets().sendGameMessage("Your resistance to dragon fire is about to wear off!");
				}
				System.out.println(count);
				if (count < 1) {
					player.setAntifireDetail(Optional.empty());
					player.getPackets().sendGameMessage("Your resistance to dragon fire has worn off!");
				}
			}
		}
	}

	@Override
	public boolean onLogin(Entity c) {
		if (c instanceof Player) {
			Player player = (Player) c;
			return player.getAntifireDetails().isPresent() ? true : false;
		}
		return false;
	}
}