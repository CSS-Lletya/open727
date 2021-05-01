package player.specials;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.Entity;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import player.PlayerCombat;

/**
 * Handles all the special attack events that a Weapon can perform
 * 
 * @author Dennis
 */
public final class WeaponSpecialDispatcher {

	/**
	 * The object map which contains all the Weapon specials in the world.
	 */
	private static final Object2ObjectArrayMap<WeaponSpecialSignature, WeaponSpecials> SPECIALS = new Object2ObjectArrayMap<>();

	/**
	 * Executes the specified weapon if it's registered.
	 * 
	 * @param player the player executing the weapon.
	 * @param parts  the string which represents a weapon.
	 */
	public static boolean execute(Player player, Entity entity, int weaponId, PlayerCombat combat) {
		Optional<WeaponSpecials> specials = getRSInterface(weaponId);
		if (!specials.isPresent()) {
			player.getPackets().sendGameMessage("Weapon ID: " + weaponId + " is not handled yet.");
			return false;
		}
		try {
			getSpecAmount(weaponId).ifPresent(weapon -> {
				if (player.getCombatDefinitions().getSpecialAttackPercentage() < getWeaponSpecialAmount(weapon)) {
					player.getPackets().sendGameMessage("You don't have enough power left.");
					player.getCombatDefinitions().decreaseSpecialAttack(0);
					return;
				}
				player.getCombatDefinitions().decreaseSpecialAttack(getWeaponSpecialAmount(weapon));
			});
			player.faceEntity(entity);
			specials.get().getAnimation().ifPresent(player::setNextAnimation);
			specials.get().getGraphics().ifPresent(player::setNextGraphics);
			specials.get().getSound().ifPresent(player::sendSound);
			specials.get().execute(player, entity, combat);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets a weapon which matches the {@code identifier}.
	 * 
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<WeaponSpecials> getRSInterface(int interfaceId) {
		for (Entry<WeaponSpecialSignature, WeaponSpecials> WeaponSpecials : SPECIALS.entrySet()) {
			if (getWeaponId(WeaponSpecials.getValue(), interfaceId)) {
				return Optional.of(WeaponSpecials.getValue());
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets the Weapon id for the special event to take place with
	 * 
	 * @param WeaponSpecials
	 * @param weaponId
	 * @return weaponId
	 */
	private static boolean getWeaponId(WeaponSpecials WeaponSpecials, int weaponId) {
		Annotation annotation = WeaponSpecials.getClass().getAnnotation(WeaponSpecialSignature.class);
		WeaponSpecialSignature signature = (WeaponSpecialSignature) annotation;
		return Arrays.stream(signature.weapons()).anyMatch(id -> weaponId == id);
	}

	/**
	 * Returns the Special amount for a weapon
	 * 
	 * @param interfaceId
	 * @return
	 */
	private static Optional<WeaponSpecials> getSpecAmount(int interfaceId) {
		for (Entry<WeaponSpecialSignature, WeaponSpecials> WeaponSpecials : SPECIALS.entrySet()) {
			if (getWeaponSpecialAmount(WeaponSpecials.getValue()) > 0) {
				return Optional.of(WeaponSpecials.getValue());
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets the weapon special amount
	 * 
	 * @param weaponSpecials
	 * @return special amount
	 */
	private static int getWeaponSpecialAmount(WeaponSpecials weaponSpecials) {
		Annotation annotation = weaponSpecials.getClass().getAnnotation(WeaponSpecialSignature.class);
		WeaponSpecialSignature signature = (WeaponSpecialSignature) annotation;
		return signature.specAmount();
	}

	/**
	 * Loads all the weapon into the {@link #SPECIALS} list.
	 * <p>
	 * </p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<WeaponSpecials> weapons = Utils.getClassesInDirectory("player.specials.impl").stream()
				.map(clazz -> (WeaponSpecials) clazz).collect(Collectors.toList());
		for (WeaponSpecials WeaponSpecials : weapons) {
			if (WeaponSpecials.getClass().getAnnotation(WeaponSpecialSignature.class) == null) {
				throw new IncompleteAnnotationException(WeaponSpecialSignature.class,
						WeaponSpecials.getClass().getName() + " has no annotation.");
			}
			SPECIALS.put(WeaponSpecials.getClass().getAnnotation(WeaponSpecialSignature.class), WeaponSpecials);
		}
	}

	/**
	 * Reloads all the weapon into the {@link #SPECIALS} list.
	 * <p>
	 * </p>
	 * <b>This method can be invoked on run-time to clear all the weapons in the
	 * list and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		SPECIALS.clear();
		load();
	}
}