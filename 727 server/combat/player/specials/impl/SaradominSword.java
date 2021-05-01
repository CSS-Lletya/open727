package player.specials.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.item.ItemNames;
import com.rs.game.player.Player;
import player.PlayerCombat;
import player.specials.WeaponSpecialSignature;
import player.specials.WeaponSpecials;

import java.util.Optional;

@WeaponSpecialSignature(weapons = { ItemNames.SARADOMIN_SWORD, 23690 }, specAmount = 50)
public class SaradominSword implements WeaponSpecials {
    @Override
    public void execute(Player player, Entity target, PlayerCombat combat) throws Exception {
        target.setNextGraphics(new Graphics(1194, 0, 100));
        player.getPackets().sendGameMessage(this.getClass().getName() + " Unfinished special!");
    }

    @Override
    public Optional<Animation> getAnimation() {
        return Optional.of(new Animation(11993));
    }

    @Override
    public Optional<Graphics> getGraphics() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getSound() {
        return Optional.of(3853);
    }
}