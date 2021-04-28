package player.specials;

import com.rs.game.Entity;
import com.rs.game.player.Player;

public interface WeaponSpecials {
	
	public void execute(Player player, Entity target) throws Exception;
}
