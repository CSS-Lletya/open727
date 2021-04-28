package npc.pet;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.pet.PetDetails;
import com.rs.game.player.content.pet.Pets;
import com.rs.utils.Utils;

import npc.NPC;
import npc.familiar.Familiar;

/**
 * Represents a pet.
 * @author Emperor
 *
 */
public final class Pet extends NPC {

	/**
	 * The owner.
	 */
	private transient final Player owner;

	/**
	 * The "near" directions.
	 */
	private final int[][] checkNearDirs;

	/**
	 * The item id.
	 */
	private final int itemId;

	/**
	 * The pet details.
	 */
	private final PetDetails details;

	/**
	 * The growth rate of the pet.
	 */
	private double growthRate;

	/**
	 * The pets type.
	 */
	private final Pets pet;

	/**
	 * Constructs a new {@code Pet} {@code Object}.
	 * @param id The NPC id.
	 * @param itemId The item id.
	 * @param owner The owner.
	 * @param tile The world tile.
	 */
	public Pet(int id, int itemId, Player owner, WorldTile tile, PetDetails details) {
		super(id, tile, -1, false);
		this.owner = owner;
		this.itemId = itemId;
		this.checkNearDirs = Utils.getCoordOffsetsNear(super.getSize());
		this.details = details;
		this.pet = Pets.forId(itemId);
		if (pet == Pets.TROLL_BABY && owner.getPetManager().getTrollBabyName() != null) {
			setName(owner.getPetManager().getTrollBabyName());
		}
		sendMainConfigurations();
		sendFollowerDetails();
	}

	@Override
	public void processNPC() {
		unlockOrb();
		if (pet == Pets.TROLL_BABY || pet.getFood().length > 0) {
			details.updateHunger(0.025);
			owner.getPackets().sendConfigByFile(4286, (int) details.getHunger());
		}
		if (details.getHunger() >= 90.0 && details.getHunger() < 90.025) {
			owner.getPackets().sendGameMessage("<col=ff0000>Your pet is starving, feed it before it runs off.</col>");
		} else if (details.getHunger() == 100.0) {
			owner.getPetManager().setNpcId(-1);
			owner.getPetManager().setItemId(-1);
			owner.setPet(null);
			owner.getPetManager().removeDetails(itemId);
			owner.getPackets().sendGameMessage("Your pet has ran away to find some food!");
			switchOrb(false);
			owner.getPackets().closeInterface(owner.getInterfaceManager().hasRezizableScreen() ? 98 : 212);
			owner.getPackets().sendAccessMask(747, 17, 0, 0, 0);
			finish();
			return;
		}
		if (growthRate > 0.000) {
			details.updateGrowth(growthRate);
			owner.getPackets().sendConfigByFile(4285, (int) details.getGrowth());
			if (details.getGrowth() == 100.0) {
				growNextStage();
			}
		}
		if (!withinDistance(owner, 12)) {
			call();
			return;
		}
		sendFollow();
	}

	/**
	 * Grows into the next stage of this pet (if any).
	 */
	public void growNextStage() {
		if (details.getStage() == 3) {
			return;
		}
		if (pet == null) {
			return;
		}
		int npcId = pet.getNpcId(details.getStage() + 1);
		if (npcId < 1) {
			return;
		}
		details.setStage(details.getStage() + 1);
		int itemId = pet.getItemId(details.getStage());
		if (pet.getNpcId(details.getStage() + 1) > 0) {
			details.updateGrowth(-100.0);
		}
		owner.getPetManager().setItemId(itemId);
		owner.getPetManager().setNpcId(npcId);
		finish();
		Pet newPet = new Pet(npcId, itemId, owner, owner, details);
		newPet.growthRate = growthRate;
		owner.setPet(newPet);
		owner.getPackets().sendGameMessage("<col=ff0000>Your pet has grown larger.</col>");
	}

	/**
	 * Picks up the pet.
	 */
	public void pickup() {
		owner.getInventory().addItem(itemId, 1);
		owner.setPet(null);
		owner.getPetManager().setNpcId(-1);
		owner.getPetManager().setItemId(-1);
		switchOrb(false);
		owner.getPackets().closeInterface(owner.getInterfaceManager().hasRezizableScreen() ? 98 : 212);
		owner.getPackets().sendAccessMask(747, 17, 0, 0, 0);
		finish();
	}

	/**
	 * Calls the pet.
	 */
	public void call() {
		int size = getSize();
		WorldTile teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final WorldTile tile = new WorldTile(new WorldTile(owner.getX() + checkNearDirs[0][dir],
					owner.getY() + checkNearDirs[1][dir], owner.getHeight()));
			if (World.canMoveNPC(tile.getHeight(), tile.getX(), tile.getY(), size)) {
				teleTile = tile;
				break;
			}
		}
		if (teleTile == null) {
			return;
		}
		setNextWorldTile(teleTile);
	}

	/**
	 * Follows the owner.
	 */
	protected void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex()) {
			setNextFaceEntity(owner);
		}
		if (getFreezeDelay() >= Utils.currentTimeMillis()) {
			return;
		}
		int size = getSize();
		int distanceX = owner.getX() - getX();
		int distanceY = owner.getY() - getY();
		if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1 && !owner.hasWalkSteps()
				&& !hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + 1, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), owner.getY() + 1)) {
						resetWalkSteps();
						addWalkSteps(getX(), owner.getY() - size);
					}
				}
			}
			return;
		}
		if ((!clipedProjectile(owner, true)) || distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(owner.getX(), owner.getY(), getRun() ? 2 : 1, size, true);
			return;
		}
		resetWalkSteps();
	}

	/**
	 * Sends the main configurations for the Pet interface (+ summoning orb).
	 */
	public void sendMainConfigurations() {
		switchOrb(true);
		owner.getPackets().sendConfig(448, itemId);// configures
		owner.getPackets().sendConfig(1160, 243269632); // sets npc emote
		owner.getPackets().sendGlobalConfig(1436, 0);
		unlockOrb(); // temporary
	}

	/**
	 * Sends the follower details.
	 */
	public void sendFollowerDetails() {
		owner.getPackets().sendConfigByFile(4285, (int) details.getGrowth());
		owner.getPackets().sendConfigByFile(4286, (int) details.getHunger());
		boolean res = owner.getInterfaceManager().hasRezizableScreen();
		owner.getPackets().sendInterface(true, res ? 746 : 548, res ? 119 : 179, 662);
		unlock();
		owner.getPackets().sendGlobalConfig(168, 8);// tab id
	}

	/**
	 * Switch the Summoning orb state.
	 * @param enable If the orb should be enabled.
	 */
	public void switchOrb(boolean enable) {
		owner.getPackets().sendConfig(1174, enable ? getId() : 0);
		if (enable) {
			unlock();
			return;
		}
		lockOrb();
	}

	/**
	 * Unlocks the orb.
	 */
	public void unlockOrb() {
		owner.getPackets().sendHideIComponent(747, 9, false);
		Familiar.sendLeftClickOption(owner);
	}

	/**
	 * Unlocks the interfaces.
	 */
	public void unlock() {
		owner.getPackets().sendHideIComponent(747, 9, false);
	}

	/**
	 * Locks the orb.
	 */
	public void lockOrb() {
		owner.getPackets().sendHideIComponent(747, 9, true);
	}

	/**
	 * Gets the details.
	 * @return The details.
	 */
	public PetDetails getDetails() {
		return details;
	}

	/**
	 * Gets the growthRate.
	 * @return The growthRate.
	 */
	public double getGrowthRate() {
		return growthRate;
	}

	/**
	 * Sets the growthRate.
	 * @param growthRate The growthRate to set.
	 */
	public void setGrowthRate(double growthRate) {
		this.growthRate = growthRate;
	}

	/**
	 * Gets the item id of the pet.
	 * @return The item id.
	 */
	public int getItemId() {
		return itemId;
	}

}