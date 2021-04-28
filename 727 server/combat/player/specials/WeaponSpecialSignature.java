package player.specials;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WeaponSpecialSignature {

	/**
	 * The array of possible weapons for this event
	 * 
	 * @return
	 */
	int[] weapons();

	/**
	 * The amount of special the possible weapons will use in order to execute
	 * 
	 * @return
	 */
	int specAmount();
}