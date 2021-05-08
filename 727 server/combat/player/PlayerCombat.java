package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.task.Task;
import com.rs.utils.MapAreas;
import com.rs.utils.Utils;

import npc.NPC;
import npc.familiar.Familiar;
import npc.familiar.Steeltitan;
import npc.godwars.zaros.Nex;
import npc.godwars.zaros.NexMinion;
import npc.qbd.QueenBlackDragon;
import player.specials.WeaponSpecialDispatcher;
import skills.Skills;
import skills.magic.Magic;

public class PlayerCombat extends Action {

	private Entity target;
	public int max_hit; // temporary constant
	private double base_mage_xp; // temporary constant
	private int mage_hit_gfx; // temporary constant
	private int magic_sound; // temporary constant
	private int max_poison_hit; // temporary constant
	private int freeze_time; // temporary constant
	private boolean reduceAttack; // temporary constant
	private boolean blood_spell; // temporary constant
	private boolean block_tele;
	private int spellcasterGloves;

	public PlayerCombat(Entity target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		player.setNextFaceEntity(target);
		if (checkAll(player)) {
			return true;
		}
		player.setNextFaceEntity(null);
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	private boolean forceCheckClipAsRange(Entity target) {
		return target instanceof NexMinion/* || target instanceof HarAken || target instanceof HarAkenTentacle */
				|| target instanceof QueenBlackDragon;
	}

	@Override
	public int processWithDelay(Player player) {
		int isRanging = isRanging(player);
		int spellId = player.getCombatDefinitions().getSpellId();
		if (spellId < 1 && hasPolyporeStaff(player)) {
			spellId = 65535;
		}
		int maxDistance = isRanging != 0 || spellId > 0 ? 7 : 0;
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		double multiplier = 1.0;
		if (player.getTemporaryAttributtes().get("miasmic_effect") == Boolean.TRUE)
			multiplier = 1.5;
		int size = target.getSize();
		if (!player.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target)))
			return 0;
		if (player.hasWalkSteps())
			maxDistance += 1;
		if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return 0;
		if (!player.getControlerManager().keepCombating(target))
			return -1;
		addAttackedByDelay(player);
		if (spellId > 0) {
			boolean manualCast = spellId != 65535 && spellId >= 256;
			Item gloves = player.getEquipment().getItem(Equipment.SLOT_HANDS);
			spellcasterGloves = gloves != null && gloves.getDefinitions().getName().contains("Spellcaster glove")
					&& player.getEquipment().getWeaponId() == -1 && new Random().nextInt(30) == 0 ? spellId : -1;
			int delay = mageAttack(player, manualCast ? spellId - 256 : spellId, !manualCast);
			if (player.getNextAnimation() != null && spellcasterGloves > 0) {
				player.setNextAnimation(new Animation(14339));
				spellcasterGloves = -1;
			}
			return delay;
		} else {
			if (isRanging == 0) {
				return (int) (meleeAttack(player) * multiplier);
			} else if (isRanging == 1) {
				player.getPackets().sendGameMessage("This ammo is not very effective with this weapon.");
				return -1;
			} else if (isRanging == 3) {
				player.getPackets().sendGameMessage("You dont have any ammo in your backpack.");
				return -1;
			} else {
				return (int) (rangeAttack(player) * multiplier);
			}
		}
	}

	private void addAttackedByDelay(Entity player) {
		target.setAttackedBy(player);
		target.setAttackedByDelay(Utils.currentTimeMillis() + 8000); // 8seconds
	}

	private int getRangeCombatDelay(int weaponId, int attackStyle) {
		int delay = 6;
		if (weaponId != -1) {
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName.contains("shortbow") || weaponName.contains("karil's crossbow"))
				delay = 3;
			else if (weaponName.contains("crossbow"))
				delay = 5;
			else if (weaponName.contains("dart") || weaponName.contains("knife") || weaponName.contains("chinchompa"))
				delay = 2;
			else {
				switch (weaponId) {
				case 15241:
					delay = 7;
					break;
				case 11235: // dark bows
				case 15701:
				case 15702:
				case 15703:
				case 15704:
					delay = 9;
					break;
				default:
					delay = 6;
					break;
				}
			}
		}
		if (attackStyle == 1)
			delay--;
		else if (attackStyle == 2)
			delay++;
		return delay;
	}

	public Entity[] getMultiAttackTargets(Player player) {
		return getMultiAttackTargets(player, 1, 9);
	}

	public Entity[] getMultiAttackTargets(Player player, int maxDistance, int maxAmtTargets) {
		List<Entity> possibleTargets = new ArrayList<Entity>();
		possibleTargets.add(target);
		if (target.isAtMultiArea()) {
			y: for (int regionId : target.getMapRegionsIds()) {
				Region region = World.getRegion(regionId);
				if (target instanceof Player) {
					List<Integer> playerIndexes = region.getPlayerIndexes();
					if (playerIndexes == null)
						continue;
					for (int playerIndex : playerIndexes) {
						Player p2 = World.getPlayers().get(playerIndex);
						if (p2 == null || p2 == player || p2 == target || p2.isDead() || !p2.hasStarted()
								|| p2.hasFinished() || !p2.isCanPvp() || !p2.isAtMultiArea()
								|| !p2.withinDistance(target, maxDistance) || !player.getControlerManager().canHit(p2))
							continue;
						possibleTargets.add(p2);
						if (possibleTargets.size() == maxAmtTargets)
							break y;
					}
				} else {
					List<Integer> npcIndexes = region.getNPCsIndexes();
					if (npcIndexes == null)
						continue;
					for (int npcIndex : npcIndexes) {
						NPC n = World.getNPCs().get(npcIndex);
						if (n == null || n == target || n == player.getFamiliar() || n.isDead() || n.hasFinished()
								|| !n.isAtMultiArea() || !n.withinDistance(target, maxDistance)
								|| !n.getDefinitions().hasAttackOption() || !player.getControlerManager().canHit(n))
							continue;
						possibleTargets.add(n);
						if (possibleTargets.size() == maxAmtTargets)
							break y;
					}
				}
			}
		}
		return possibleTargets.toArray(new Entity[possibleTargets.size()]);
	}

	public int mageAttack(final Player player, int spellId, boolean autocast) {
		if (!autocast) {
			player.getCombatDefinitions().resetSpells(false);
			player.getActionManager().forceStop();
		}
		if (!Magic.checkCombatSpell(player, spellId, -1, true)) {
			if (autocast)
				player.getCombatDefinitions().resetSpells(true);
			return -1; // stops
		}
		if (spellId == 65535) {
			player.setNextFaceEntity(target);
			player.setNextGraphics(new Graphics(2034));
			player.setNextAnimation(new Animation(15448));
			mage_hit_gfx = 2036;
			delayMagicHit(2, getMagicHit(player,
					getRandomMagicMaxHit(player, (5 * player.getSkills().getLevel(Skills.MAGIC)) - 180)));
			World.sendProjectile(player, target, 2035, 60, 32, 50, 50, 0, 0);
			return 4;
		}
		if (player.getCombatDefinitions().getSpellBook() == 192) {
			switch (spellId) {
			case 25: // air strike
				player.setNextAnimation(new Animation(14221));
				mage_hit_gfx = 2700;
				base_mage_xp = 5.5;
				int baseDamage = 20;
				if (player.getEquipment().getGlovesId() == 205) {
					baseDamage = 90;
				}
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendProjectile(player, target, 2699, 18, 18, 50, 50, 0, 0);
				return 5;
			case 28: // water strike
				player.setNextGraphics(new Graphics(2701));
				player.setNextAnimation(new Animation(14221));
				mage_hit_gfx = 2708;
				base_mage_xp = 7.5;
				baseDamage = 40;
				if (player.getEquipment().getGlovesId() == 205) {
					baseDamage = 100;
				}
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendProjectile(player, target, 2703, 18, 18, 50, 50, 0, 0);
				return 5;
			case 36:// bind
				player.setNextGraphics(new Graphics(177));
				player.setNextAnimation(new Animation(710));
				mage_hit_gfx = 181;
				base_mage_xp = 60.5;
				Hit magicHit = getMagicHit(player, getRandomMagicMaxHit(player, 20));
				delayMagicHit(2, magicHit);
				World.sendProjectile(player, target, 178, 18, 18, 50, 50, 0, 0);
				long currentTime = Utils.currentTimeMillis();
				if (magicHit.getDamage() > 0 && target.getFrozenBlockedDelay() < currentTime)
					target.addFreezeDelay(5000, true);
				return 5;
			case 55:// snare
				player.setNextGraphics(new Graphics(177));
				player.setNextAnimation(new Animation(710));
				mage_hit_gfx = 180;
				base_mage_xp = 91.1;
				Hit snareHit = getMagicHit(player, getRandomMagicMaxHit(player, 30));
				delayMagicHit(2, snareHit);
				if (snareHit.getDamage() > 0 && target.getFrozenBlockedDelay() < Utils.currentTimeMillis())
					target.addFreezeDelay(10000, true);
				World.sendProjectile(player, target, 178, 18, 18, 50, 50, 0, 0);
				return 5;
			case 81:// entangle
				player.setNextGraphics(new Graphics(177));
				player.setNextAnimation(new Animation(710));
				mage_hit_gfx = 179;
				base_mage_xp = 91.1;
				Hit entangleHit = getMagicHit(player, getRandomMagicMaxHit(player, 50));
				delayMagicHit(2, entangleHit);
				if (entangleHit.getDamage() > 0 && target.getFrozenBlockedDelay() < Utils.currentTimeMillis())
					target.addFreezeDelay(20000, true);
				World.sendProjectile(player, target, 178, 18, 18, 50, 50, 0, 0);
				return 5;
			case 30: // earth strike
				player.setNextGraphics(new Graphics(2713));
				player.setNextAnimation(new Animation(14221));
				mage_hit_gfx = 2723;
				base_mage_xp = 9.5;
				baseDamage = 60;
				if (player.getEquipment().getGlovesId() == 205) {
					baseDamage = 110;
				}
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendProjectile(player, target, 2718, 18, 18, 50, 50, 0, 0);
				return 5;
			case 32: // fire strike
				player.setNextGraphics(new Graphics(2728));
				player.setNextAnimation(new Animation(14221));
				mage_hit_gfx = 2737;
				base_mage_xp = 11.5;
				baseDamage = 80;
				if (player.getEquipment().getGlovesId() == 205) {
					baseDamage = 120;
				}
				int damage = getRandomMagicMaxHit(player, baseDamage);
				if (target instanceof NPC) {
					NPC n = (NPC) target;
					if (n.getId() == 9463) // ice verm
						damage *= 2;
				}
				delayMagicHit(2, getMagicHit(player, damage));
				World.sendProjectile(player, target, 2729, 18, 18, 50, 50, 0, 0);
				return 5;
			case 34: // air bolt
				player.setNextAnimation(new Animation(14220));
				mage_hit_gfx = 2700;
				base_mage_xp = 13.5;
				baseDamage = 90;
				if (player.getEquipment().getGlovesId() == 777) {
					baseDamage = 120;
				}
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendProjectile(player, target, 2699, 18, 18, 50, 50, 0, 0);
				return 5;
			case 39: // water bolt
				player.setNextGraphics(new Graphics(2707, 0, 100));
				player.setNextAnimation(new Animation(14220));
				mage_hit_gfx = 2709;
				base_mage_xp = 16.5;
				baseDamage = 100;
				if (player.getEquipment().getGlovesId() == 777) {
					baseDamage = 130;
				}
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendProjectile(player, target, 2704, 18, 18, 50, 50, 0, 0);
				return 5;
			case 42: // earth bolt
				player.setNextGraphics(new Graphics(2714));
				player.setNextAnimation(new Animation(14222));
				mage_hit_gfx = 2724;
				base_mage_xp = 19.5;
				baseDamage = 110;
				if (player.getEquipment().getGlovesId() == 777) {
					baseDamage = 140;
				}
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendProjectile(player, target, 2719, 18, 18, 50, 50, 0, 0);
				return 5;
			case 45: // fire bolt
				player.setNextGraphics(new Graphics(2728));
				player.setNextAnimation(new Animation(14223));
				mage_hit_gfx = 2738;
				base_mage_xp = 22.5;
				baseDamage = 120;
				if (player.getEquipment().getGlovesId() == 777) {
					baseDamage = 150;
				}
				damage = getRandomMagicMaxHit(player, baseDamage);
				if (target instanceof NPC) {
					NPC n = (NPC) target;
					if (n.getId() == 9463) // ice verm
						damage *= 2;
				}
				delayMagicHit(2, getMagicHit(player, damage));
				World.sendProjectile(player, target, 2731, 18, 18, 50, 50, 0, 0);
				return 5;
			case 49: // air blast
				player.setNextAnimation(new Animation(14221));
				mage_hit_gfx = 2700;
				base_mage_xp = 25.5;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 130)));
				World.sendProjectile(player, target, 2699, 18, 18, 50, 50, 0, 0);
				return 5;
			case 52: // water blast
				player.setNextGraphics(new Graphics(2701));
				player.setNextAnimation(new Animation(14220));
				mage_hit_gfx = 2710;
				base_mage_xp = 31.5;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 140)));
				World.sendProjectile(player, target, 2705, 18, 18, 50, 50, 0, 0);
				return 5;
			case 58: // earth blast
				player.setNextGraphics(new Graphics(2715));
				player.setNextAnimation(new Animation(14222));
				mage_hit_gfx = 2725;
				base_mage_xp = 31.5;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 150)));
				World.sendProjectile(player, target, 2720, 18, 18, 50, 50, 0, 0);
				return 5;
			case 63: // fire blast
				player.setNextGraphics(new Graphics(2728));
				player.setNextAnimation(new Animation(14223));
				mage_hit_gfx = 2739;
				base_mage_xp = 34.5;
				damage = getRandomMagicMaxHit(player, 160);
				if (target instanceof NPC) {
					NPC n = (NPC) target;
					if (n.getId() == 9463) // ice verm
						damage *= 2;
				}
				delayMagicHit(2, getMagicHit(player, damage));
				World.sendProjectile(player, target, 2733, 18, 18, 50, 50, 0, 0);
				return 5;
			case 66:// Saradomin Strike
				player.setNextAnimation(new Animation(811));
				mage_hit_gfx = 76;
				base_mage_xp = 34.5;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 300)));
				return 5;
			case 67: // Claws of Guthix
				player.setNextAnimation(new Animation(811));
				mage_hit_gfx = 77;
				base_mage_xp = 34.5;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 300)));
				return 5;
			case 68: // Flames of Zamorak
				player.setNextAnimation(new Animation(811));
				mage_hit_gfx = 78;
				base_mage_xp = 34.5;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 300)));
				return 5;
			case 70: // air wave
				player.setNextAnimation(new Animation(14221));
				mage_hit_gfx = 2700;
				base_mage_xp = 36;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 170)));
				World.sendProjectile(player, target, 2699, 18, 18, 50, 50, 0, 0);
				return 5;
			case 73: // water wave
				player.setNextGraphics(new Graphics(2702));
				player.setNextAnimation(new Animation(14220));
				mage_hit_gfx = 2710;
				base_mage_xp = 37.5;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 180)));
				World.sendProjectile(player, target, 2706, 18, 18, 50, 50, 0, 0);
				return 5;
			case 77: // earth wave
				player.setNextGraphics(new Graphics(2716));
				player.setNextAnimation(new Animation(14222));
				mage_hit_gfx = 2726;
				base_mage_xp = 42.5;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 190)));
				World.sendProjectile(player, target, 2721, 18, 18, 50, 50, 0, 0);
				return 5;
			case 80: // fire wave
				player.setNextGraphics(new Graphics(2728));
				player.setNextAnimation(new Animation(14223));
				mage_hit_gfx = 2740;
				base_mage_xp = 42.5;
				damage = getRandomMagicMaxHit(player, 200);
				if (target instanceof NPC) {
					NPC n = (NPC) target;
					if (n.getId() == 9463) // ice verm
						damage *= 2;
				}
				delayMagicHit(2, getMagicHit(player, damage));
				World.sendProjectile(player, target, 2735, 18, 18, 50, 50, 0, 0);
				return 5;
			case 86: // teleblock
				if (target instanceof Player && ((Player) target).getTeleBlockDelay() <= Utils.currentTimeMillis()) {
					player.setNextGraphics(new Graphics(1841));
					player.setNextAnimation(new Animation(10503));
					mage_hit_gfx = 1843;
					base_mage_xp = 80;
					block_tele = true;
					Hit hit = getMagicHit(player, getRandomMagicMaxHit(player, 30));
					delayMagicHit(2, hit);
					World.sendProjectile(player, target, 1842, 18, 18, 50, 50, 0, 0);
				} else {
					player.getPackets().sendGameMessage("This player is already effected by this spell.", true);
				}
				return 5;
			case 84:// air surge
				player.setNextGraphics(new Graphics(457));
				player.setNextAnimation(new Animation(10546));
				mage_hit_gfx = 2700;
				base_mage_xp = 80;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 220)));
				World.sendProjectile(player, target, 462, 18, 18, 50, 50, 0, 0);
				return 5;
			case 87:// water surge
				player.setNextGraphics(new Graphics(2701));
				player.setNextAnimation(new Animation(10542));
				mage_hit_gfx = 2712;
				base_mage_xp = 80;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 240)));
				World.sendProjectile(player, target, 2707, 18, 18, 50, 50, 3, 0);
				return 5;
			case 89:// earth surge
				player.setNextGraphics(new Graphics(2717));
				player.setNextAnimation(new Animation(14209));
				mage_hit_gfx = 2727;
				base_mage_xp = 80;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 260)));
				World.sendProjectile(player, target, 2722, 18, 18, 50, 50, 0, 0);
				return 5;
			case 91:// fire surge
				player.setNextGraphics(new Graphics(2728));
				player.setNextAnimation(new Animation(2791));
				mage_hit_gfx = 2741;
				base_mage_xp = 80;
				damage = getRandomMagicMaxHit(player, 280);
				if (damage > 0 && target instanceof NPC) {
					NPC n = (NPC) target;
					if (n.getId() == 9463) // ice verm
						damage *= 2;
				}
				delayMagicHit(2, getMagicHit(player, damage));
				World.sendProjectile(player, target, 2735, 18, 18, 50, 50, 3, 0);
				World.sendProjectile(player, target, 2736, 18, 18, 50, 50, 20, 0);
				World.sendProjectile(player, target, 2736, 18, 18, 50, 50, 110, 0);
				return 5;
			case 99: // Storm of armadyl //Sonic and Tyler dumped
				player.setNextGraphics(new Graphics(457));
				player.setNextAnimation(new Animation(10546));
				mage_hit_gfx = 1019;
				base_mage_xp = 70;
				int boost = (player.getSkills().getLevelForXp(Skills.MAGIC) - 77) * 5;
				int hit = getRandomMagicMaxHit(player, 160 + boost);
				if (hit > 0 && hit < boost)
					hit += boost;
				delayMagicHit(2, getMagicHit(player, hit));
				World.sendProjectile(player, target, 1019, 18, 18, 50, 30, 0, 0);
				return player.getEquipment().getWeaponId() == 21777 ? 4 : 5;
			}
		} else if (player.getCombatDefinitions().getSpellBook() == 193) {
			switch (spellId) {
			case 28:// Smoke Rush
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 385;
				base_mage_xp = 30;
				max_poison_hit = 20;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 150)));
				World.sendProjectile(player, target, 386, 18, 18, 50, 50, 0, 0);
				return 4;
			case 32:// Shadow Rush
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 379;
				base_mage_xp = 31;
				reduceAttack = true;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 160)));
				World.sendProjectile(player, target, 380, 18, 18, 50, 50, 0, 0);
				return 4;
			case 36: // Miasmic rush
				player.setNextAnimation(new Animation(10513));
				player.setNextGraphics(new Graphics(1845));
				mage_hit_gfx = 1847;
				base_mage_xp = 35;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 200)));
				World.sendProjectile(player, target, 1846, 43, 22, 51, 50, 16, 0);
				if (target.getTemporaryAttributtes().get("miasmic_immunity") == Boolean.TRUE) {
					return 4;
				}
				if (target instanceof Player) {
					((Player) target).getPackets().sendGameMessage("You feel slowed down.");
				}
				target.getTemporaryAttributtes().put("miasmic_immunity", Boolean.TRUE);
				target.getTemporaryAttributtes().put("miasmic_effect", Boolean.TRUE);
				final Entity t = target;
				World.get().submit(new Task(20) {
					@Override
					protected void execute() {
						t.getTemporaryAttributtes().remove("miasmic_effect");
						World.get().submit(new Task(15) {
							@Override
							protected void execute() {
								t.getTemporaryAttributtes().remove("miasmic_immunity");
								this.cancel();
							}
						});
						this.cancel();
					}
				});
				return 4;
			case 37: // Miasmic blitz
				player.setNextAnimation(new Animation(10524));
				player.setNextGraphics(new Graphics(1850));
				mage_hit_gfx = 1851;
				base_mage_xp = 48;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 280)));
				World.sendProjectile(player, target, 1852, 43, 22, 51, 50, 16, 0);
				if (target.getTemporaryAttributtes().get("miasmic_immunity") == Boolean.TRUE) {
					return 4;
				}
				if (target instanceof Player) {
					((Player) target).getPackets().sendGameMessage("You feel slowed down.");
				}
				target.getTemporaryAttributtes().put("miasmic_immunity", Boolean.TRUE);
				target.getTemporaryAttributtes().put("miasmic_effect", Boolean.TRUE);
				final Entity t0 = target;
				World.get().submit(new Task(60) {
					@Override
					protected void execute() {
						t0.getTemporaryAttributtes().remove("miasmic_effect");
						World.get().submit(new Task(15) {
							@Override
							protected void execute() {
								t0.getTemporaryAttributtes().remove("miasmic_immunity");
								this.cancel();
							}
						});
						this.cancel();
					}
				});
				return 4;
			case 38: // Miasmic burst
				player.setNextAnimation(new Animation(10516));
				player.setNextGraphics(new Graphics(1848));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {
					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 1849;
						base_mage_xp = 42;
						int damage = getRandomMagicMaxHit(player, 240);
						delayMagicHit(2, getMagicHit(player, damage));
						if (target.getTemporaryAttributtes().get("miasmic_immunity") != Boolean.TRUE) {
							if (target instanceof Player) {
								((Player) target).getPackets().sendGameMessage("You feel slowed down.");
							}
							target.getTemporaryAttributtes().put("miasmic_immunity", Boolean.TRUE);
							target.getTemporaryAttributtes().put("miasmic_effect", Boolean.TRUE);
							final Entity t = target;
							World.get().submit(new Task(40) {
								@Override
								protected void execute() {
									t.getTemporaryAttributtes().remove("miasmic_effect");
									World.get().submit(new Task(15) {
										@Override
										protected void execute() {
											t.getTemporaryAttributtes().remove("miasmic_immunity");
											this.cancel();
										}
									});
									this.cancel();
								}
							});
						}
						if (!nextTarget) {
							if (damage == -1) {
								return false;
							}
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 39: // Miasmic barrage
				player.setNextAnimation(new Animation(10518));
				player.setNextGraphics(new Graphics(1853));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {
					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 1854;
						base_mage_xp = 54;
						int damage = getRandomMagicMaxHit(player, 320);
						delayMagicHit(2, getMagicHit(player, damage));
						if (target.getTemporaryAttributtes().get("miasmic_immunity") != Boolean.TRUE) {
							if (target instanceof Player) {
								((Player) target).getPackets().sendGameMessage("You feel slowed down.");
							}
							target.getTemporaryAttributtes().put("miasmic_immunity", Boolean.TRUE);
							target.getTemporaryAttributtes().put("miasmic_effect", Boolean.TRUE);
							final Entity t = target;
							World.get().submit(new Task(80) {
								@Override
								protected void execute() {
									t.getTemporaryAttributtes().remove("miasmic_effect");
									World.get().submit(new Task(15) {
										protected void execute() {
											t.getTemporaryAttributtes().remove("miasmic_immunity");
											this.cancel();
										}
									});
									this.cancel();
								}
							});
						}
						if (!nextTarget) {
							if (damage == -1) {
								return false;
							}
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 24:// Blood rush
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 373;
				base_mage_xp = 33;
				blood_spell = true;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 170)));
				World.sendProjectile(player, target, 374, 18, 18, 50, 50, 0, 0);
				return 4;
			case 20:// Ice rush
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 361;
				base_mage_xp = 34;
				freeze_time = 5000;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 180)));
				World.sendProjectile(player, target, 362, 18, 18, 50, 50, 0, 0);
				return 4;
			// TODO burst
			case 30:// Smoke burst
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						mage_hit_gfx = 389;
						base_mage_xp = 36;
						max_poison_hit = 20;
						int damage = getRandomMagicMaxHit(player, 190);
						delayMagicHit(2, getMagicHit(player, damage));
						World.sendProjectile(player, target, 388, 18, 18, 50, 50, 0, 0);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 34:// Shadow burst
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						mage_hit_gfx = 382;
						base_mage_xp = 37;
						reduceAttack = true;
						int damage = getRandomMagicMaxHit(player, 200);
						delayMagicHit(2, getMagicHit(player, damage));
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 26:// Blood burst
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						mage_hit_gfx = 376;
						base_mage_xp = 39;
						blood_spell = true;
						int damage = getRandomMagicMaxHit(player, 210);
						delayMagicHit(2, getMagicHit(player, damage));
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 22:// Ice burst
				player.setNextGraphics(new Graphics(366));
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						mage_hit_gfx = 367;
						base_mage_xp = 46;
						freeze_time = 15000;
						magic_sound = 169;
						int damage = getRandomMagicMaxHit(player, 220);
						delayMagicHit(4, getMagicHit(player, damage));
						World.sendProjectile(player, target, 366, 43, 0, 120, 0, 50, 64);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 29:// Smoke Blitz
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 387;
				base_mage_xp = 42;
				max_poison_hit = 40;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 230)));
				World.sendProjectile(player, target, 386, 18, 18, 50, 50, 0, 0);
				return 4;
			case 33:// Shadow Blitz
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 381;
				base_mage_xp = 43;
				reduceAttack = true;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 240)));
				World.sendProjectile(player, target, 380, 18, 18, 50, 50, 0, 0);
				return 4;
			case 25:// Blood Blitz
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 375;
				base_mage_xp = 45;
				blood_spell = true;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 250)));
				World.sendProjectile(player, target, 374, 18, 18, 50, 50, 0, 0);
				return 4;
			case 21:// Ice Blitz
				player.setNextGraphics(new Graphics(366));
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 367;
				base_mage_xp = 46;
				freeze_time = 15000;
				magic_sound = 169;
				delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 260)));
				World.sendProjectile(player, target, 368, 60, 32, 50, 50, 0, 0);
				return 4;
			case 31:// Smoke barrage
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						mage_hit_gfx = 391;
						base_mage_xp = 48;
						max_poison_hit = 40;
						int damage = getRandomMagicMaxHit(player, 270);
						delayMagicHit(2, getMagicHit(player, damage));
						World.sendProjectile(player, target, 390, 18, 18, 50, 50, 0, 0);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 35:// shadow barrage
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						mage_hit_gfx = 383;
						base_mage_xp = 49;
						reduceAttack = true;
						int damage = getRandomMagicMaxHit(player, 280);
						delayMagicHit(2, getMagicHit(player, damage));
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 27:// blood barrage
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						mage_hit_gfx = 377;
						base_mage_xp = 51;
						max_poison_hit = 40;
						int damage = getRandomMagicMaxHit(player, 290);
						delayMagicHit(2, getMagicHit(player, damage));
						World.sendProjectile(player, target, 390, 18, 18, 50, 50, 0, 0);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 23: // ice barrage
				player.setNextAnimation(new Animation(1979));
				playSound(171, player, target);
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						magic_sound = 168;
						long currentTime = Utils.currentTimeMillis();
						if (target.getSize() >= 2 || target.getFreezeDelay() >= currentTime
								|| target.getFrozenBlockedDelay() >= currentTime) {
							mage_hit_gfx = 1677;
						} else {
							mage_hit_gfx = 369;
							freeze_time = 20000;
						}
						base_mage_xp = 52;
						int damage = getRandomMagicMaxHit(player, 300);
						Hit hit = getMagicHit(player, damage);
						delayMagicHit(Utils.getDistance(player, target) > 3 ? 4 : 2, hit);
						World.sendProjectile(player, target, 368, 60, 32, 50, 50, 0, 0);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			}

		}
		return -1; // stops atm
	}

	public interface MultiAttack {

		public boolean attack();

	}

	public void attackTarget(Entity[] targets, MultiAttack perform) {
		Entity realTarget = target;
		for (Entity t : targets) {
			target = t;
			if (!perform.attack())
				break;
		}
		target = realTarget;
	}

	public int getRandomMagicMaxHit(Player player, int baseDamage) {
		int current = getMagicMaxHit(player, baseDamage);
		if (current <= 0) // Splash.
			return -1;
		int hit = Utils.random(current + 1);
		if (hit > 0) {
			if (target instanceof NPC) {
				NPC n = (NPC) target;
				if (n.getId() == 9463 && hasFireCape(player)) // ice verm
					hit += 40;
			}
		}
		return hit;
	}

	/**
	 * Gets the current maximum magic hit (depending if the hit was accurate or not).
	 * @param player The player.
	 * @param baseDamage The base damage of the spell.
	 * @return The current maximum hit.
	 */
	/*
	 * public int getMagicMaxHit(Player player, int baseDamage) { //older formula double effectiveAttack = (Math.round((player.getSkills().getLevel(Skills.MAGIC) * player.getPrayer().getMageMultiplier())) + 8) + player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK]; if (fullVoidEquipped(player, new int[] { 11663, 11674 })) effectiveAttack *= 1.3; effectiveAttack *= player.getAuraManager().getMagicAccurayMultiplier(); double totalDefence = 0; if (target instanceof Player) { Player p2 = (Player) target; double effectiveDefence = (Math.round(p2.getSkills().getLevel(Skills.DEFENCE) + (p2.getPrayer().getDefenceMultiplier() + Utils.random(0, 14))) + 8) + p2.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF]; double effectiveMagicDefence = p2.getSkills().getLevel(Skills.MAGIC) + p2.getPrayer().getMageMultiplier(); effectiveMagicDefence *= 0.7; effectiveDefence *= 0.3; totalDefence = Math.round(effectiveMagicDefence + effectiveDefence); } else { NPC n = (NPC) target; totalDefence = n.getBonuses() != null ? n.getBonuses()[CombatDefinitions.MAGIC_DEF] : 0; } double attack = Math.round((effectiveAttack * (64 + player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK])) / 10); double defence = Math.round(((totalDefence * (64 + player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF])) / 10)); double accuracy = 0.5; if (attack < defence) accuracy = (attack - 1) / (2 * defence); else if (attack > defence) accuracy = 1 - (defence + 1) / (2 * attack); if (accuracy > 0.90) accuracy = 0.90; else if (accuracy < 0.01) accuracy = 0.01; if (accuracy < Math.random()) { return 0; } max_hit = baseDamage; double boost = 1 + ((player.getSkills().getLevel(Skills.MAGIC) - player .getSkills().getLevelForXp(Skills.MAGIC)) * 0.03); if (boost > 1) max_hit *= boost; double magicPerc = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DAMAGE]; if (spellcasterGloves > 0) { if (baseDamage > 60 || spellcasterGloves == 28 || spellcasterGloves == 25) { magicPerc += 17; if (target instanceof Player) { Player p = (Player) target; p.getSkills().drainLevel(0, p.getSkills().getLevel(0) / 10); p.getSkills().drainLevel(1, p.getSkills().getLevel(1) / 10); p.getSkills().drainLevel(2, p.getSkills().getLevel(2) / 10); p.getPackets().sendGameMessage("Your melee skills have been drained."); player.getPackets().sendGameMessage("Your spell weakened your enemy."); } player.getPackets().sendGameMessage("Your magic surged with extra power."); } } boost = magicPerc / 100 + 1; max_hit *= boost; return (int) Math.floor(max_hit); }
	 */

	/*
	 * public int getMagicMaxHit(Player player, int baseDamage) { double EA = (Math.round(player.getSkills().getLevel(Skills.MAGIC) * player.getPrayer().getMageMultiplier()) + 8); if (fullVoidEquipped(player, new int[] { 11663, 11674 })) EA *= 1.3; EA *= player.getAuraManager().getMagicAccurayMultiplier(); double ED = 0, DB; if (target instanceof Player) { Player p2 = (Player) target; double EMD = p2.getSkills().getLevel(Skills.MAGIC) * p2.getPrayer().getMageMultiplier(); if (p2.getFamiliar() != null && (p2.getFamiliar().getId() == 6870 || p2.getFamiliar().getId() == 6871)) EMD *= 1.05; Math.round(EMD); double ERD = (Math.round(p2.getSkills().getLevel(Skills.DEFENCE) * p2.getPrayer().getDefenceMultiplier()) + 8) + (player.getCombatDefinitions().isDefensiveCasting() ? 3 : 0); EMD *= 0.7; ERD *= 0.3; ED = (EMD + ERD); DB = p2.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF]; } else { NPC n = (NPC) target; DB = ED = n.getBonuses() != null ? n.getBonuses()[CombatDefinitions.MAGIC_DEF] : 0; } double A = (EA * (64 + player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK])) / 10; double D = (ED * (64 + DB)) / 10; double accuracy = 0.05; //so a level 3 can hit a level 138 if (A < D) accuracy = (A-1) / (2 * D); else if (A > D) accuracy = 1 - (D + 1) / (2 * A); if (accuracy < Math.random()) { return 0; } max_hit = baseDamage; double boost = 1 + ((player.getSkills().getLevel(Skills.MAGIC) - player .getSkills().getLevelForXp(Skills.MAGIC)) * 0.03); if (boost > 1) max_hit *= boost; double magicPerc = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DAMAGE]; if (spellcasterGloves > 0) { if (baseDamage > 60 || spellcasterGloves == 28 || spellcasterGloves == 25) { magicPerc += 17; if (target instanceof Player) { Player p = (Player) target; p.getSkills().drainLevel(0, p.getSkills().getLevel(0) / 10); p.getSkills().drainLevel(1, p.getSkills().getLevel(1) / 10); p.getSkills().drainLevel(2, p.getSkills().getLevel(2) / 10); p.getPackets().sendGameMessage("Your melee skills have been drained."); player.getPackets().sendGameMessage("Your spell weakened your enemy."); } player.getPackets().sendGameMessage("Your magic surged with extra power."); } } boost = magicPerc / 100 + 1; max_hit *= boost; return (int) Math.floor(max_hit); }
	 */

	private int getMagicMaxHit(Player player, int baseDamage) {
		double EA = (Math.round(player.getSkills().getLevel(Skills.MAGIC) * player.getPrayer().getMageMultiplier())
				+ 11);
		if (fullVoidEquipped(player, new int[] { 11663, 11674 }))
			EA *= 1.3;
		EA *= player.getAuraManager().getMagicAccurayMultiplier();
		double ED = 0, DB;
		if (target instanceof Player) {
			Player p2 = (Player) target;
			double EMD = ((Math.round(p2.getSkills().getLevel(Skills.MAGIC) * p2.getPrayer().getMageMultiplier()) + 8)
					+ (player.getCombatDefinitions().isDefensiveCasting() ? 3 : 0));
			if (p2.getFamiliar() != null && (p2.getFamiliar().getId() == 6870 || p2.getFamiliar().getId() == 6871))
				EMD *= 1.05;
			Math.round(EMD);
			double ERD = (Math.round(p2.getSkills().getLevel(Skills.DEFENCE) * p2.getPrayer().getDefenceMultiplier())
					+ 8);
			EMD *= 0.7;
			ERD *= 0.3;
			ED = (EMD + ERD);
			DB = p2.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF];
		} else {
			NPC n = (NPC) target;
			DB = ED = n.getBonuses() != null ? n.getBonuses()[CombatDefinitions.MAGIC_DEF] / 1.6 : 0;
		}
		double A = (EA * (1 + player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK]) / 64);
		double D = (ED * (1 + DB)) / 64;
		double accuracy = 0.05; // so a level 3 can hit a level 138
		if (A < D)
			accuracy = (A - 1) / (2 * D);
		else if (A > D)
			accuracy = 1 - (D + 1) / (2 * A);
		if (accuracy < Math.random()) {
			return 0;
		}
		max_hit = baseDamage;
		double boost = 1
				+ ((player.getSkills().getLevel(Skills.MAGIC) - player.getSkills().getLevelForXp(Skills.MAGIC)) * 0.03);
		if (boost > 1)
			max_hit *= boost;
		double magicPerc = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DAMAGE];
		if (spellcasterGloves > 0) {
			if (baseDamage > 60 || spellcasterGloves == 28 || spellcasterGloves == 25) {
				magicPerc += 17;
				if (target instanceof Player) {
					Player p = (Player) target;
					p.getSkills().drainLevel(0, p.getSkills().getLevel(0) / 10);
					p.getSkills().drainLevel(1, p.getSkills().getLevel(1) / 10);
					p.getSkills().drainLevel(2, p.getSkills().getLevel(2) / 10);
					p.getPackets().sendGameMessage("Your melee skills have been drained.");
					player.getPackets().sendGameMessage("Your spell weakened your enemy.");
				}
				player.getPackets().sendGameMessage("Your magic surged with extra power.");
			}
		}
		boost = magicPerc / 100 + 1;
		max_hit *= boost;
		return (int) Math.floor(max_hit);
	}

	/**
	 * Gets the magic accuracy.
	 * @param e The player.
	 * @param baseDamage The base damage.
	 * @param extraDamage The extra damage.
	 * @return The magic accuracy value.
	 */
	@SuppressWarnings("unused")
	private double getMagicAccuracy(Player e, int baseDamage, int extraDamage) {
		int magicLevel = e.getSkills().getLevel(Skills.MAGIC) + 1;
		int magicBonus = e.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK];
		if ((baseDamage << 2) < 60 && spellcasterGloves > 0 && spellcasterGloves != 28 && spellcasterGloves != 25) {
			magicBonus += 20;
		}
		double prayer = 1.0 * e.getPrayer().getMageMultiplier();
		double accuracy = ((magicLevel + (magicBonus * 4))) * prayer;
		accuracy += (extraDamage + baseDamage) >> 1;
		if (fullVoidEquipped(e, 11663, 11674)) {
			accuracy *= 1.106;
		}
		return accuracy < 1 ? 1 : accuracy;
	}

	/**
	 * Gets the magic defence of an entity.
	 * @param e The entity.
	 * @return The magic defence value.
	 */
	/*
	 * private double getMagicDefence(Entity e) { Player p = e instanceof Player ? (Player) e : null; int style = p != null ? 0 : 1; if (p != null) { int type = CombatDefinitions.getXpStyle(p.getEquipment().getWeaponId(), p.getCombatDefinitions().getAttackStyle()); style += type == Skills.DEFENCE ? 3 : type == CombatDefinitions.SHARED ? 1 : 0; } double defLvl = (p != null ? p.getSkills().getLevel(Skills.DEFENCE) : ((NPC) e).getCombatLevel() * 0.7) * 0.3; defLvl += (p != null ? p.getSkills().getLevel(Skills.MAGIC) : ((NPC) e).getCombatLevel() * 0.7) * 0.7; int defBonus = p != null ? p.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF] : ((NPC) e).getBonuses() != null ? ((NPC) e).getBonuses()[CombatDefinitions.MAGIC_DEF] : 5; double defMult = 1.0; if (p != null) { defMult += p.getPrayer().getDefenceMultiplier(); } double defence = ((defLvl + (defBonus << 2)) + style) * defMult; return defence < 1 ? 1 : defence; }
	 */

	private int rangeAttack(final Player player) {
		final int weaponId = player.getEquipment().getWeaponId();
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int combatDelay = getRangeCombatDelay(weaponId, attackStyle);
		int soundId = getSoundId(weaponId, attackStyle);
		if (player.getCombatDefinitions().isUsingSpecialAttack()) {
			int specAmt = getSpecialAmmount(weaponId);
			if (specAmt == 0) {
				player.getPackets().sendGameMessage(
						"This weapon has no special Attack, if you still see special bar please relogin.");
				player.getCombatDefinitions().decreaseSpecialAttack(0);
				return combatDelay;
			}
			if (player.getCombatDefinitions().hasRingOfVigour())
				specAmt *= 0.9;
			if (player.getCombatDefinitions().getSpecialAttackPercentage() < specAmt) {
				player.getPackets().sendGameMessage("You don't have enough power left.");
				player.getCombatDefinitions().decreaseSpecialAttack(0);
				return combatDelay;
			}
			player.getCombatDefinitions().decreaseSpecialAttack(specAmt);
			switch (weaponId) {
			case 19149:// zamorak bow
			case 19151:
				player.setNextAnimation(new Animation(426));
				player.setNextGraphics(new Graphics(97));
				World.sendProjectile(player, target, 100, 41, 16, 25, 35, 16, 0);
				delayHit(1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, 1);
				break;
			case 19146:
			case 19148:// guthix bow
				player.setNextAnimation(new Animation(426));
				player.setNextGraphics(new Graphics(95));
				World.sendProjectile(player, target, 98, 41, 16, 25, 35, 16, 0);
				delayHit(1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, 1);
				break;
			case 19143:// saradomin bow
			case 19145:
				player.setNextAnimation(new Animation(426));
				player.setNextGraphics(new Graphics(96));
				World.sendProjectile(player, target, 99, 41, 16, 25, 35, 16, 0);
				delayHit(1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, 1);
				break;
			case 10034:
			case 10033:
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player on array

					@Override
					public boolean attack() {
						int damage = getRandomMaxHit(player, weaponId, attackStyle, true, true,
								weaponId == 10034 ? 1.2 : 1.0, true);
						player.setNextAnimation(new Animation(2779));
						World.sendProjectile(player, target, weaponId == 10034 ? 909 : 908, 41, 16, 31, 35, 16, 0);
						delayHit(1, weaponId, attackStyle, getRangeHit(player, damage));
						World.get().submit(new Task(2) {
							@Override
							protected void execute() {
								player.setNextGraphics(new Graphics(2739, 0, 96 << 16));
							}
						});
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				break;
			case 859: // magic longbow
			case 861: // magic shortbow
			case 10284: // Magic composite bow
			case 18332: // Magic longbow (sighted)
				player.setNextAnimation(new Animation(1074));
				player.setNextGraphics(new Graphics(249, 0, 100));
				World.sendProjectile(player, target, 249, 41, 16, 31, 35, 16, 0);
				World.sendProjectile(player, target, 249, 41, 16, 25, 35, 21, 0);
				delayHit(2, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				delayHit(3, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, 2);
				break;
			case 15241: // Hand cannon
				World.get().submit(new Task((int) 0.25) {
					int loop = 0;
					@Override
					protected void execute() {
						if ((target.isDead() || player.isDead() || loop > 1) && !World.getNPCs().contains(target)) {
							this.cancel();
							return;
						}
						if (loop == 0) {
							player.setNextAnimation(new Animation(12174));
							player.setNextGraphics(new Graphics(2138));
							World.sendProjectile(player, target, 2143, 18, 18, 50, 50, 0, 0);
							delayHit(1, weaponId, attackStyle, getRangeHit(player,
									getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
						} else if (loop == 1) {
							player.setNextAnimation(new Animation(12174));
							player.setNextGraphics(new Graphics(2138));
							World.sendProjectile(player, target, 2143, 18, 18, 50, 50, 0, 0);
							delayHit(1, weaponId, attackStyle, getRangeHit(player,
									getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
							this.cancel();
						}
						loop++;
					}
				});
				combatDelay = 9;
				break;
			case 11235: // dark bows
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				int ammoId = player.getEquipment().getAmmoId();
				player.setNextAnimation(new Animation(getWeaponAttackEmote(weaponId, attackStyle)));
				player.setNextGraphics(new Graphics(getArrowThrowGfxId(weaponId, ammoId), 0, 100));
				if (ammoId == 11212) {
					int damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.5, true);
					if (damage < 80)
						damage = 80;
					int damage2 = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.5, true);
					if (damage2 < 80)
						damage2 = 80;
					World.sendProjectile(player, target, 1099, 41, 16, 31, 35, 16, 0);
					World.sendProjectile(player, target, 1099, 41, 16, 25, 35, 21, 0);
					delayHit(2, weaponId, attackStyle, getRangeHit(player, damage));
					delayHit(3, weaponId, attackStyle, getRangeHit(player, damage2));
					World.get().submit(new Task(2) {
						@Override
						protected void execute() {
							target.setNextGraphics(new Graphics(1100, 0, 100));
						}
					});
				} else {
					int damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.3, true);
					if (damage < 50)
						damage = 50;
					int damage2 = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.3, true);
					if (damage2 < 50)
						damage2 = 50;
					World.sendProjectile(player, target, 1101, 41, 16, 31, 35, 16, 0);
					World.sendProjectile(player, target, 1101, 41, 16, 25, 35, 21, 0);
					delayHit(2, weaponId, attackStyle, getRangeHit(player, damage));
					delayHit(3, weaponId, attackStyle, getRangeHit(player, damage2));
				}
				dropAmmo(player, 2);

				break;
			case 14684: // zanik cbow
				player.setNextAnimation(new Animation(getWeaponAttackEmote(weaponId, attackStyle)));
				player.setNextGraphics(new Graphics(1714));
				World.sendProjectile(player, target, 2001, 41, 41, 41, 35, 0, 0);
				delayHit(2, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true) + 30
								+ Utils.getRandom(120)));
				dropAmmo(player);
				break;
			case 13954:// morrigan javelin
			case 12955:
			case 13956:
			case 13879:
			case 13880:
			case 13881:
			case 13882:
				player.setNextGraphics(new Graphics(1836));
				player.setNextAnimation(new Animation(10501));
				World.sendProjectile(player, target, 1837, 41, 41, 41, 35, 0, 0);
				final int hit = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true);
				delayHit(2, weaponId, attackStyle, getRangeHit(player, hit));
				if (hit > 0) {
					final Entity finalTarget = target;
					World.get().submit(new Task(4) {
						int damage = hit;
						@Override
						protected void execute() {
							if (finalTarget.isDead() || finalTarget.hasFinished()) {
								this.cancel();
								return;
							}
							if (damage > 50) {
								damage -= 50;
								finalTarget.applyHit(new Hit(player, 50, HitLook.REGULAR_DAMAGE));
							} else {
								finalTarget.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
								this.cancel();
							}
						}
					});
				}
				dropAmmo(player, -1);
				break;
			case 13883:
			case 13957:// morigan thrown axe
				player.setNextGraphics(new Graphics(1838));
				player.setNextAnimation(new Animation(10504));
				World.sendProjectile(player, target, 1839, 41, 41, 41, 35, 0, 0);
				delayHit(2, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, -1);
				break;
			default:
				player.getPackets().sendGameMessage(
						"This weapon has no special Attack, if you still see special bar please relogin.");
				return combatDelay;
			}
		} else {
			if (weaponId != -1) {
				String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
				if (weaponName.contains("throwing axe") || weaponName.contains("knife") || weaponName.contains("dart")
						|| weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
					if (!weaponName.contains("javelin") && !weaponName.contains("thrownaxe"))
						player.setNextGraphics(new Graphics(getKnifeThrowGfxId(weaponId), 0, 100));
					World.sendProjectile(player, target, getKnifeThrowGfxId(weaponId), 41, 36, 41, 32, 15, 0);
					int hit = getRandomMaxHit(player, weaponId, attackStyle, true);
					delayHit(1, weaponId, attackStyle, getRangeHit(player, hit));
					checkSwiftGlovesEffect(player, 1, attackStyle, weaponId, hit, getKnifeThrowGfxId(weaponId), 41, 36,
							41, 32, 15, 0);
					dropAmmo(player, -1);
				} else if (weaponName.contains("crossbow")) {
					int damage = 0;
					int ammoId = player.getEquipment().getAmmoId();
					if (ammoId != -1 && Utils.getRandom(10) == 5) {
						switch (ammoId) {
						case 9237:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true);
							target.setNextGraphics(new Graphics(755));
							if (target instanceof Player) {
								Player p2 = (Player) target;
								p2.stopAll();
							} else {
								NPC n = (NPC) target;
								n.setTarget(null);
							}
							soundId = 2914;
							break;
						case 9242:
							max_hit = Short.MAX_VALUE;
							damage = (int) (target.getHitpoints() * 0.2);
							target.setNextGraphics(new Graphics(754));
							player.applyHit(new Hit(target,
									player.getHitpoints() > 20 ? (int) (player.getHitpoints() * 0.1) : 1,
									HitLook.REFLECTED_DAMAGE));
							soundId = 2912;
							break;
						case 9243:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, false, 1.15, true);
							target.setNextGraphics(new Graphics(751));
							soundId = 2913;
							break;
						case 9244:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, false,
									!Combat.hasAntiDragProtection(target) ? 1.45 : 1.0, true);
							target.setNextGraphics(new Graphics(756));
							soundId = 2915;
							break;
						case 9245:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, false, 1.15, true);
							target.setNextGraphics(new Graphics(753));
							player.heal((int) (player.getMaxHitpoints() * 0.25));
							soundId = 2917;
							break;
						default:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true);
						}
					} else {
						damage = getRandomMaxHit(player, weaponId, attackStyle, true);
						checkSwiftGlovesEffect(player, 2, attackStyle, weaponId, damage, 27, 38, 36, 41, 32, 5, 0);
					}
					World.sendProjectile(player, target, 27, 38, 36, 41, 32, 5, 0);
					delayHit(2, weaponId, attackStyle, getRangeHit(player, damage));
					if (weaponId != 4740)
						dropAmmo(player);
					else
						player.getEquipment().removeAmmo(ammoId, 1);
				} else if (weaponId == 15241) {// handcannon
					if (Utils.getRandom(player.getSkills().getLevel(Skills.FIREMAKING) << 1) == 0) {
						// explode
						player.setNextGraphics(new Graphics(2140));
						player.getEquipment().getItems().set(3, null);
						player.getEquipment().refresh((byte) 3);
						player.getAppearance().generateAppearenceData();
						player.applyHit(new Hit(player, Utils.getRandom(150) + 10, HitLook.REGULAR_DAMAGE));
						player.setNextAnimation(new Animation(12175));
						return combatDelay;
					} else {
						player.setNextGraphics(new Graphics(2138));
						World.sendProjectile(player, target, 2143, 18, 18, 60, 30, 0, 0);
						delayHit(1, weaponId, attackStyle,
								getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
						dropAmmo(player, -2);
					}
				} else if (weaponName.contains("crystal bow")) {
					player.setNextAnimation(new Animation(getWeaponAttackEmote(weaponId, attackStyle)));
					player.setNextGraphics(new Graphics(250));
					World.sendProjectile(player, target, 249, 41, 36, 41, 35, 0, 0);
					int hit = getRandomMaxHit(player, weaponId, attackStyle, true);
					delayHit(2, weaponId, attackStyle, getRangeHit(player, hit));
					checkSwiftGlovesEffect(player, 2, attackStyle, weaponId, hit, 249, 41, 36, 41, 35, 0, 0);
				} else if (weaponId == 21365) { // Bolas
					dropAmmo(player, -3);
					player.setNextAnimation(new Animation(3128));
					World.sendProjectile(player, target, 468, 41, 41, 41, 35, 0, 0);
					int delay = 15000;
					if (target instanceof Player) {
						Player p = (Player) target;
						Item weapon = p.getEquipment().getItem(3);
						boolean slashBased = weapon != null;
						if (weapon != null) {
							int slash = p.getCombatDefinitions().getBonuses()[CombatDefinitions.SLASH_ATTACK];
							for (int i = 0; i < 5; i++) {
								if (p.getCombatDefinitions().getBonuses()[i] > slash) {
									slashBased = false;
									break;
								}
							}
						}
						if (p.getInventory().containsItem(946, 1) || slashBased) {
							delay /= 2;
						}
						if (p.getPrayer().usingPrayer(0, 18) || p.getPrayer().usingPrayer(1, 8)) {
							delay /= 2;
						}
						if (delay < 5000) {
							delay = 5000;
						}
					}
					long currentTime = Utils.currentTimeMillis();
					if (getRandomMaxHit(player, weaponId, attackStyle, true) > 0
							&& target.getFrozenBlockedDelay() < currentTime) {
						target.addFreezeDelay(delay, true);
						World.get().submit(new Task(2) {
							@Override
							protected void execute() {
								target.setNextGraphics(new Graphics(469, 0, 96));
								this.cancel();
							}
						});
					}
					playSound(soundId, player, target);
					return combatDelay;
				} else { // bow/default
					final int ammoId = player.getEquipment().getAmmoId();
					player.setNextGraphics(new Graphics(getArrowThrowGfxId(weaponId, ammoId), 0, 100));
					World.sendProjectile(player, target, getArrowProjectileGfxId(weaponId, ammoId), 41, 36, 20, 35, 16,
							0);
					int hit = getRandomMaxHit(player, weaponId, attackStyle, true);
					delayHit(2, weaponId, attackStyle, getRangeHit(player, hit));
					checkSwiftGlovesEffect(player, 2, attackStyle, weaponId, hit,
							getArrowProjectileGfxId(weaponId, ammoId), 41, 36, 20, 35, 16, 0);
					if (weaponId == 11235 || weaponId == 15701 || weaponId == 15702 || weaponId == 15703
							|| weaponId == 15704) { // dbows
						World.sendProjectile(player, target, getArrowProjectileGfxId(weaponId, ammoId), 41, 35, 36, 35,
								21, 0);

						delayHit(3, weaponId, attackStyle,
								getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
						dropAmmo(player, 2);
					} else {
						if (weaponId != -1) {
							if (!weaponName.endsWith("bow full") && !weaponName.equals("zaryte bow"))
								dropAmmo(player);
						}
					}
				}

				player.setNextAnimation(new Animation(getWeaponAttackEmote(weaponId, attackStyle)));
			}
		}
		playSound(soundId, player, target);
		return combatDelay;
	}

	/**
	 * Handles the swift gloves effect.
	 * @param player The player.
	 * @param hitDelay The delay before hitting the target.
	 * @param attackStyle The attack style used.
	 * @param weaponId The weapon id.
	 * @param hit The hit done.
	 * @param gfxId The gfx id.
	 * @param startHeight The start height of the original projectile.
	 * @param endHeight The end height of the original projectile.
	 * @param speed The speed of the original projectile.
	 * @param delay The delay of the original projectile.
	 * @param curve The curve of the original projectile.
	 * @param startDistanceOffset The start distance offset of the original projectile.
	 */
	private void checkSwiftGlovesEffect(Player player, int hitDelay, int attackStyle, int weaponId, int hit, int gfxId,
			int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		Item gloves = player.getEquipment().getItem(Equipment.SLOT_HANDS);
		if (gloves == null || !gloves.getDefinitions().getName().contains("Swift glove")) {
			return;
		}
		if (hit != 0 && hit < ((max_hit / 3) * 2) || new Random().nextInt(3) != 0) {
			return;
		}
		player.getPackets().sendGameMessage("You fired an extra shot.");
		World.sendProjectile(player, target, gfxId, startHeight - 5, endHeight - 5, speed, delay,
				curve - 5 < 0 ? 0 : curve - 5, startDistanceOffset);
		delayHit(hitDelay, weaponId, attackStyle,
				getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
		if (hit > (max_hit - 10)) {
			target.addFreezeDelay(10000, false);
			target.setNextGraphics(new Graphics(181, 0, 96));
		}

	}

	public void dropAmmo(Player player, int quantity) {
		if (quantity == -2) {
			final int ammoId = player.getEquipment().getAmmoId();
			player.getEquipment().removeAmmo(ammoId, 1);
		} else if (quantity == -1 || quantity == -3) {
			final int weaponId = player.getEquipment().getWeaponId();
			if (weaponId != -1) {
				if ((quantity == -3 && Utils.getRandom(10) < 2) || (quantity != -3 && Utils.getRandom(3) > 0)) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId != -1 && ItemDefinitions.getItemDefinitions(capeId).getName().contains("Ava's"))
						return; // nothing happens
				} else {
					player.getEquipment().removeAmmo(weaponId, quantity);
					return;
				}
				player.getEquipment().removeAmmo(weaponId, quantity);
				FloorItem.updateGroundItem(new Item(weaponId, quantity),
						new WorldTile(target.getCoordFaceX(target.getSize()), target.getCoordFaceY(target.getSize()),
								target.getHeight()),
						player);
			}
		} else {
			final int ammoId = player.getEquipment().getAmmoId();
			if (Utils.getRandom(3) > 0) {
				int capeId = player.getEquipment().getCapeId();
				if (capeId != -1 && ItemDefinitions.getItemDefinitions(capeId).getName().contains("Ava's"))
					return; // nothing happens
			} else {
				player.getEquipment().removeAmmo(ammoId, quantity);
				return;
			}
			if (ammoId != -1) {
				player.getEquipment().removeAmmo(ammoId, quantity);
				FloorItem.updateGroundItem(new Item(ammoId, quantity), new WorldTile(target.getCoordFaceX(target.getSize()),
						target.getCoordFaceY(target.getSize()), target.getHeight()), player);
			}
		}
	}

	public void dropAmmo(Player player) {
		dropAmmo(player, 1);
	}

	public int getArrowThrowGfxId(int weaponId, int arrowId) {
		if (arrowId == 884) {
			return 18;
		} else if (arrowId == 886) {
			return 20;
		} else if (arrowId == 888) {
			return 21;
		} else if (arrowId == 890) {
			return 22;
		} else if (arrowId == 892)
			return 24;
		return 19; // bronze default
	}

	public int getArrowProjectileGfxId(int weaponId, int arrowId) {
		if (arrowId == 884) {
			return 11;
		} else if (arrowId == 886) {
			return 12;
		} else if (arrowId == 888) {
			return 13;
		} else if (arrowId == 890) {
			return 14;
		} else if (arrowId == 892)
			return 15;
		else if (arrowId == 11212)
			return 1120;
		else if (weaponId == 20171)
			return 1066;
		return 10;// bronze default
	}

	public static int getKnifeThrowGfxId(int weaponId) {
		// knives TODO ALL
		if (weaponId == 868) {
			return 225;
		} else if (weaponId == 867) {
			return 224;
		} else if (weaponId == 866) {
			return 223;
		} else if (weaponId == 865) {
			return 221;
		} else if (weaponId == 864) {
			return 219;
		} else if (weaponId == 863) {
			return 220;
		}
		// darts
		if (weaponId == 806) {
			return 232;
		} else if (weaponId == 807) {
			return 233;
		} else if (weaponId == 808) {
			return 234;
		} else if (weaponId == 3093) {
			return 273;
		} else if (weaponId == 809) {
			return 235;
		} else if (weaponId == 810) {
			return 236;
		} else if (weaponId == 811) {
			return 237;
		} else if (weaponId == 11230) {
			return 1123;
		}
		// javelins
		if (weaponId >= 13954 && weaponId <= 13956 || weaponId >= 13879 && weaponId <= 13882)
			return 1837;
		// thrownaxe
		if (weaponId == 13883 || weaponId == 13957)
			return 1839;
		if (weaponId == 800)
			return 43;
		else if (weaponId == 13883 || weaponId == 13957)
			return 1839;
		else if (weaponId == 13954 || weaponId == 13955 || weaponId == 13956 || weaponId == 13879 || weaponId == 13880
				|| weaponId == 13881 || weaponId == 13882)
			return 1837;
		return 219;
	}

	@SuppressWarnings("unused")
	private int getRangeHitDelay(Player player) {
		return Utils.getDistance(player.getX(), player.getY(), target.getX(), target.getY()) >= 5 ? 2 : 1;
	}

	private int meleeAttack(final Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int combatDelay = getMeleeCombatDelay(player, weaponId);
		int soundId = getSoundId(weaponId, attackStyle);
		if (weaponId == -1) {
			Item gloves = player.getEquipment().getItem(Equipment.SLOT_HANDS);
			if (gloves != null && gloves.getDefinitions().getName().contains("Goliath gloves")) {
				weaponId = -2;
			}
		}
		
		if (player.getCombatDefinitions().isUsingSpecialAttack()) {
			if (!specialExecute(player))
				return combatDelay;
			switch (weaponId) {
			case 11730: // sara sword
			case 23690:
				player.setNextAnimation(new Animation(11993));
				target.setNextGraphics(new Graphics(1194));
				delayNormalHit(weaponId, attackStyle, getMeleeHit(player, 50 + Utils.getRandom(100)),
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
				soundId = 3853;
				break;
			case 1249:// d spear
			case 1263:
			case 3176:
			case 5716:
			case 5730:
			case 13770:
			case 13772:
			case 13774:
			case 13776:
				player.setNextAnimation(new Animation(12017));
				player.stopAll();
				target.setNextGraphics(new Graphics(80, 5, 60));

				if (!target.addWalkSteps(target.getX() - player.getX() + target.getX(),
						target.getY() - player.getY() + target.getY(), 1))
					player.setNextFaceEntity(target);
				target.setNextFaceEntity(player);
				World.get().submit(new Task(1) {
					@Override
					protected void execute() {
						target.setNextFaceEntity(null);
						player.setNextFaceEntity(null);
					}
				});
				if (target instanceof Player) {
					final Player other = (Player) target;
					other.lock();
					other.getWatchMap().get("FOOD").reset();
					other.setDisableEquip(true);
					World.get().submit(new Task(5) {
						@Override
						protected void execute() {
							other.setDisableEquip(false);
							other.unlock();
						}
					});
				} else {
					NPC n = (NPC) target;
					n.setFreezeDelay(3000);
					n.resetCombat();
					n.setRandomWalk(false);
				}
				break;
			case 11698: // sgs
			case 23681:
				player.setNextAnimation(new Animation(7071));
				player.setNextGraphics(new Graphics(2109));
				int sgsdamage = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true);
				player.heal(sgsdamage / 2);
				player.getPrayer().restorePrayer((sgsdamage / 4) * 10);
				delayNormalHit(weaponId, attackStyle, getMeleeHit(player, sgsdamage));
				break;
			case 11696: // bgs
			case 23680:
				player.setNextAnimation(new Animation(11991));
				player.setNextGraphics(new Graphics(2114));
				int damage = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.2, true);
				delayNormalHit(weaponId, attackStyle, getMeleeHit(player, damage));
				if (target instanceof Player) {
					Player targetPlayer = ((Player) target);
					int amountLeft;
					if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.DEFENCE, damage / 10)) > 0) {
						if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.STRENGTH, amountLeft)) > 0) {
							if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.PRAYER, amountLeft)) > 0) {
								if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.ATTACK, amountLeft)) > 0) {
									if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.MAGIC,
											amountLeft)) > 0) {
										if (targetPlayer.getSkills().drainLevel(Skills.RANGE, amountLeft) > 0) {
											break;
										}
									}
								}
							}
						}
					}
				}
				break;
			case 11694: // ags
			case 23679:
				player.setNextAnimation(new Animation(11989));
				player.setNextGraphics(new Graphics(2113));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.375, true)));
				break;
			case 13899: // vls
			case 13901:
				player.setNextAnimation(new Animation(10502));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.20, true)));
				break;
			case 13902: // statius hammer
			case 13904:
				player.setNextAnimation(new Animation(10505));
				player.setNextGraphics(new Graphics(1840));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.25, true)));
				break;
			case 13905: // vesta spear
			case 13907:
				player.setNextAnimation(new Animation(10499));
				player.setNextGraphics(new Graphics(1835));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
				break;
			case 19784: // korasi sword
			case 18786:
				player.setNextAnimation(new Animation(14788));
				player.setNextGraphics(new Graphics(1729));
				int korasiDamage = getMaxHit(player, weaponId, attackStyle, false, true, 1);
				double multiplier = 0.5 + Math.random();
				max_hit = (int) (korasiDamage * 1.5);
				korasiDamage *= multiplier;
				delayNormalHit(weaponId, attackStyle, getMagicHit(player, korasiDamage));
				break;
			case 11700:
				int zgsdamage = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
				player.setNextAnimation(new Animation(7070));
				player.setNextGraphics(new Graphics(1221));
				if (zgsdamage != 0 && target.getSize() <= 1) { // freezes small
					// npcs
					target.setNextGraphics(new Graphics(2104));
					target.addFreezeDelay(18000); // 18seconds
				}
				delayNormalHit(weaponId, attackStyle, getMeleeHit(player, zgsdamage));
				break;
			case 14484: // d claws
			case 23695:
				player.setNextAnimation(new Animation(10961));
				player.setNextGraphics(new Graphics(1950));
				int[] hits = new int[] { 0, 1 };
				int hit = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
				if (hit > 0) {
					hits = new int[] { hit, hit / 2, (hit / 2) / 2, (hit / 2) - ((hit / 2) / 2) };
				} else {
					hit = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
					if (hit > 0) {
						hits = new int[] { 0, hit, hit / 2, hit - (hit / 2) };
					} else {
						hit = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
						if (hit > 0) {
							hits = new int[] { 0, 0, hit / 2, (hit / 2) + 10 };
						} else {
							hit = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
							if (hit > 0) {
								hits = new int[] { 0, 0, 0, (int) (hit * 1.5) };
							} else {
								hits = new int[] { 0, 0, 0, Utils.getRandom(7) };
							}
						}
					}
				}
				for (int i = 0; i < hits.length; i++) {
					if (i > 1) {
						delayHit(1, weaponId, attackStyle, getMeleeHit(player, hits[i]));
					} else {
						delayNormalHit(weaponId, attackStyle, getMeleeHit(player, hits[i]));
					}
				}
				break;
			case 10887: // anchor
				player.setNextAnimation(new Animation(5870));
				player.setNextGraphics(new Graphics(1027));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, false, 1.0, true)));
				break;
			case 1305: // dragon long
				player.setNextAnimation(new Animation(12033));
				player.setNextGraphics(new Graphics(2117));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.25, true)));
				break;
			case 3204: // d hally
				player.setNextAnimation(new Animation(1665));
				player.setNextGraphics(new Graphics(282));
				if (target.getSize() < 3) {// giant npcs wont get stuned cuz of
					// a stupid hit
					target.setNextGraphics(new Graphics(254, 0, 100));
					target.setNextGraphics(new Graphics(80));
				}
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
				if (target.getSize() > 1)
					delayHit(1, weaponId, attackStyle, getMeleeHit(player,
							getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
				break;
			case 4587: // dragon sci
				player.setNextAnimation(new Animation(12031));
				player.setNextGraphics(new Graphics(2118));
				Hit hit1 = getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					if (hit1.getDamage() > 0)
						p2.setPrayerDelay(5000);// 5 seconds
				}
				delayNormalHit(weaponId, attackStyle, hit1);
				soundId = 2540;
				break;
			case 1215: // dragon dagger
			case 5698: // dds
				player.setNextAnimation(new Animation(1062));
				player.setNextGraphics(new Graphics(252, 0, 100));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.15, true)),
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.15, true)));
				soundId = 2537;
				break;
			case 1434: // dragon mace
				player.setNextAnimation(new Animation(1060));
				player.setNextGraphics(new Graphics(251));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.45, true)));
				soundId = 2541;
				break;
			default:
				player.getPackets().sendGameMessage(
						"This weapon has no special Attack, if you still see special bar please relogin.");
				return combatDelay;
			}
		} else {
			if (weaponId == -2 && new Random().nextInt(25) == 0) {
				player.setNextAnimation(new Animation(14417));
				final int attack = attackStyle;
				attackTarget(getMultiAttackTargets(player, 5, Integer.MAX_VALUE), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						target.addFreezeDelay(10000, true);
						target.setNextGraphics(new Graphics(181, 0, 96));
						final Entity t = target;
						World.get().submit(new Task(1) {
							@Override
							protected void execute() {
								final int damage = getRandomMaxHit(player, -2, attack, false, false, 1.0, false);
								t.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
								this.cancel();
							}
						});
						if (target instanceof Player) {
							Player p = (Player) target;
							for (int i = 0; i < 7; i++) {
								if (i != 3 && i != 5) {
									p.getSkills().drainLevel(i, 7);
								}
							}
							p.getPackets().sendGameMessage("Your stats have been drained!");
						}
						if (!nextTarget) {
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return combatDelay;
			}
			delayNormalHit(weaponId, attackStyle,
					getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false)));
			player.setNextAnimation(new Animation(getWeaponAttackEmote(weaponId, attackStyle)));
		}
		playSound(soundId, player, target);
		return combatDelay;
	}

	public void playSound(int soundId, Player player, Entity target) {
		if (soundId == -1)
			return;
		player.getPackets().sendSound(soundId, 0, 1);
		if (target instanceof Player) {
			Player p2 = (Player) target;
			p2.getPackets().sendSound(soundId, 0, 1);
		}
	}

	public static int getSpecialAmmount(int weaponId) {
		switch (weaponId) {
		case 4587: // dragon sci
		case 859: // magic longbow
		case 861: // magic shortbow
		case 10284: // Magic composite bow
		case 18332: // Magic longbow (sighted)
		case 19149:// zamorak bow
		case 19151:
		case 19143:// saradomin bow
		case 19145:
		case 19146:
		case 19148:// guthix bow
			return 55;
		case 11235: // dark bows
		case 15701:
		case 15702:
		case 15703:
		case 15704:
			return 65;
		case 13899: // vls
		case 13901:
		case 1305: // dragon long
		case 1215: // dragon dagger
		case 5698: // dds
		case 1434: // dragon mace
		case 1249:// d spear
		case 1263:
		case 3176:
		case 5716:
		case 5730:
		case 13770:
		case 13772:
		case 13774:
		case 13776:
			return 25;
		case 15442:// whip start
		case 15443:
		case 15444:
		case 15441:
//		case 4151:
		case 23691:
		case 11698: // sgs
		case 23681:
		case 11694: // ags
		case 23679:
		case 13902: // statius hammer
		case 13904:
		case 13905: // vesta spear
		case 13907:
		case 14484: // d claws
		case 23695:
		case 10887: // anchor
		case 3204: // d hally
		case 4153: // granite maul
		case 14684: // zanik cbow
		case 15241: // hand cannon
		case 13908:
		case 13954:// morrigan javelin
		case 13955:
		case 13956:
		case 13879:
		case 13880:
		case 13881:
		case 13882:
		case 13883:// morigan thrown axe
		case 13957:
			return 50;
		case 11730: // ss
		case 23690:
		case 11696: // bgs
		case 23680:
		case 11700: // zgs
		case 23682:
		case 35:// Excalibur
		case 8280:
		case 14632:
		case 1377:// dragon battle axe
		case 13472:
		case 15486:// staff of lights
		case 22207:
		case 22209:
		case 22211:
		case 22213:
			return 100;
		case 19784: // korasi sword
			return 60;
		default:
			return 0;
		}
	}

	public int getRandomMaxHit(Player player, int weaponId, int attackStyle, boolean ranging) {
		return getRandomMaxHit(player, weaponId, attackStyle, ranging, true, 1.0D, false);
	}

	public int getRandomMaxHit(Player player, int weaponId, int attackStyle, boolean ranging, boolean defenceAffects,
			double specMultiplier, boolean usingSpec) {
		max_hit = getMaxHit(player, weaponId, attackStyle, ranging, usingSpec, specMultiplier);
		if (defenceAffects) {
			double att = player.getSkills().getLevel(ranging ? 4 : 0) + player.getCombatDefinitions()
					.getBonuses()[ranging ? 4 : CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle)];
			if (weaponId == -2) {
				att += 82;
			}
			att *= ranging ? player.getPrayer().getRangeMultiplier() : player.getPrayer().getAttackMultiplier();
			if (fullVoidEquipped(player, ranging ? (new int[] { 11664, 11675 }) : (new int[] { 11665, 11676 })))
				att *= 1.1;
			if (ranging)
				att *= player.getAuraManager().getRangeAccurayMultiplier();
			double def = 0;
			if (target instanceof Player) {
				Player p2 = (Player) target;
				def = (double) p2.getSkills().getLevel(Skills.DEFENCE)
						+ (2 * p2.getCombatDefinitions().getBonuses()[ranging ? 9
								: CombatDefinitions.getMeleeDefenceBonus(
										CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle))]);

				def *= p2.getPrayer().getDefenceMultiplier();
				if (!ranging) {
					if (p2.getFamiliar() instanceof Steeltitan)
						def *= 1.15;
				}
			} else {
				NPC n = (NPC) target;
				def = n.getBonuses() != null ? n.getBonuses()[ranging ? 9
						: CombatDefinitions
								.getMeleeDefenceBonus(CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle))]
						: 0;
				def *= 2;
				if (n.getId() == 1160 && fullVeracsEquipped(player))
					def *= 0.6;
			}
			if (usingSpec) {
				double multiplier = /* 0.25 + */getSpecialAccuracyModifier(player);
				att *= multiplier;
			}
			if (def < 0) {
				def = -def;
				att += def;
			}
			double prob = att / def;

			if (prob > 0.90) // max, 90% prob hit so even lvl 138 can miss at
				// lvl 3
				prob = 0.90;
			else if (prob < 0.05) // minimun 5% so even lvl 3 can hit lvl 138
				prob = 0.05;
			if (prob < Math.random())
				return 0;
		}
		int hit = Utils.random(max_hit + 1);
		if (target instanceof NPC) {
			NPC n = (NPC) target;
			if (n.getId() == 9463 && hasFireCape(player))
				hit += 40;
		}
		if (player.getAuraManager().usingEquilibrium()) {
			int perc25MaxHit = (int) (max_hit * 0.25);
			hit -= perc25MaxHit;
			max_hit -= perc25MaxHit;
			if (hit < 0)
				hit = 0;
			if (hit < perc25MaxHit)
				hit += perc25MaxHit;

		}
		return hit;
	}

	public final int getMaxHit(Player player, int weaponId, int attackStyle, boolean ranging, boolean usingSpec,
                               double specMultiplier) {
		if (!ranging) {

			/*
			 * //whip hiting 450 no pot no pray? lmao nty int strLvl = player.getSkills().getLevel(Skills.STRENGTH); int strBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS]; double strMult = player.getPrayer().getStrengthMultiplier(); double xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle); double style = xpStyle == Skills.STRENGTH ? 3 : xpStyle == CombatDefinitions.SHARED ? 1 : 0; int dhp = 0; double dharokMod = 1.0; double hitMultiplier = 1.0; if (fullDharokEquipped(player)) { dhp = player.getMaxHitpoints() - player.getHitpoints(); dharokMod = (dhp * 0.001) + 1; } if (fullVoidEquipped(player, 11665, 11676)) { hitMultiplier *= 1.1; } hitMultiplier *= specMultiplier; double cumulativeStr = (strLvl * strMult + style) * dharokMod; return (int) ((14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * hitMultiplier);
			 */

			double strengthLvl = player.getSkills().getLevel(Skills.STRENGTH);
			int xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle);
			double styleBonus = xpStyle == Skills.STRENGTH ? 3 : xpStyle == CombatDefinitions.SHARED ? 1 : 0;
			double otherBonus = 1;
			if (fullDharokEquipped(player)) {
				double hp = player.getHitpoints();
				double maxhp = player.getMaxHitpoints();
				double d = hp / maxhp;
				otherBonus = 2 - d;
			}
			double effectiveStrength = 8
					+ Math.floor((strengthLvl * player.getPrayer().getStrengthMultiplier()) + styleBonus);
			if (fullVoidEquipped(player, 11665, 11676))
				effectiveStrength = Math.floor(effectiveStrength * 1.1);
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS];
			if (weaponId == -2) {
				strengthBonus += 82;
			}
			double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
			return (int) Math.floor(baseDamage * specMultiplier * otherBonus);
		} else {
			if (weaponId == 24338 && target instanceof Player) {
				player.getPackets()
						.sendGameMessage("The royal crossbow feels weak and unresponsive against other players.");
				return 60;
			}
			double rangedLvl = player.getSkills().getLevel(Skills.RANGE);
			double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
			double effectiveStrenght = Math.floor(rangedLvl * player.getPrayer().getRangeMultiplier()) + styleBonus;

			if (fullVoidEquipped(player, 11664, 11675))
				effectiveStrenght += Math.floor((player.getSkills().getLevelForXp(Skills.RANGE) / 5) + 1.6);
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGED_STR_BONUS];
			double baseDamage = 5 + (((effectiveStrenght + 8) * (strengthBonus + 64)) / 64);
			return (int) Math.floor(baseDamage * specMultiplier);
		}
	}
	/*
	 * public int getRandomMaxHit(Player player, int weaponId, int attackStyle, boolean ranging, boolean defenceAffects, double specMultiplier, boolean usingSpec) { Random r = new Random(); max_hit = getMaxHit(player, weaponId, attackStyle, ranging, usingSpec, specMultiplier); double accuracyMultiplier = 1.0; if (defenceAffects) { accuracyMultiplier = getSpecialAccuracyModifier(player); } if (ranging && defenceAffects) { double accuracy = accuracyMultiplier * getRangeAccuracy(player, attackStyle) + 1; double defence = getRangeDefence(target) + 1; if (r.nextInt((int) accuracy) < r.nextInt((int) defence)) { return 0; } } else if (defenceAffects) { double accuracy = accuracyMultiplier * getMeleeAccuracy(player, attackStyle, weaponId) + 1; double defence = getMeleeDefence(target, attackStyle, weaponId) + 1; if (r.nextInt((int) accuracy) < r.nextInt((int) defence)) { return 0; } } int hit = r.nextInt(max_hit + 1); if (target instanceof NPC) { NPC n = (NPC) target; if (n.getId() == 9463 && hasFireCape(player)) hit += 40; } if (player.getAuraManager().usingEquilibrium()) { int perc25MaxHit = (int) (max_hit * 0.25); hit -= perc25MaxHit; max_hit -= perc25MaxHit; if (hit < 0) hit = 0; if (hit < perc25MaxHit) hit += perc25MaxHit;
	 * 
	 * } return hit; }
	 * 
	 * /** Gets the melee accuracy of the player.
	 * 
	 * @param e The player.
	 * 
	 * @param attackStyle The attack style.
	 * 
	 * @param weaponId The weapon id.
	 * 
	 * @return The melee accuracy.
	 */
	/*
	 * public static double getMeleeAccuracy(Player e, int attackStyle, int weaponId) { int style = attackStyle == 0 ? 3 : attackStyle == 2 ? 1 : 0; int attLvl = e.getSkills().getLevel(Skills.ATTACK); int attBonus = e.getCombatDefinitions().getBonuses()[CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle)]; if (weaponId == -2) { attBonus += 82; } double attMult = 1.0 * e.getPrayer().getAttackMultiplier(); double accuracyMultiplier = 1.0; if (fullVoidEquipped(e, 11665, 11676)) { accuracyMultiplier *= 0.15; } double cumulativeAtt = attLvl * attMult + style; return (14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64)) * accuracyMultiplier; }
	 * 
	 * /** Gets the maximum melee hit.
	 * 
	 * @param e The player.
	 * 
	 * @param attackStyle The attack style.
	 * 
	 * @param weaponId The weapon id.
	 * 
	 * @return The maximum melee hit.
	 */
	/*
	 * public static double getMeleeMaximum(Player e, int attackStyle, int weaponId) { int strLvl = e.getSkills().getLevel(Skills.STRENGTH); int strBonus = e.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS]; if (weaponId == -2) { strBonus += 82; } double strMult = e.getPrayer().getStrengthMultiplier(); double xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle); double style = xpStyle == Skills.STRENGTH ? 3 : xpStyle == CombatDefinitions.SHARED ? 1 : 0; int dhp = 0; double dharokMod = 1.0; double hitMultiplier = 1.0; if (fullDharokEquipped(e)) { dhp = e.getMaxHitpoints() - e.getHitpoints(); dharokMod = (dhp * 0.001) + 1; } if (fullVoidEquipped(e, 11665, 11676)) { hitMultiplier *= 1.1; } double cumulativeStr = (strLvl * strMult + style) * dharokMod; return (int) ((14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * hitMultiplier); }
	 * 
	 * /** Gets the melee defence.
	 * 
	 * @param e The entity.
	 * 
	 * @param attackStyle The attack style.
	 * 
	 * @param weaponId The weapon id.
	 * 
	 * @return The maximum melee defence.
	 */
	/*
	 * public static double getMeleeDefence(Entity e, int attackStyle, int weaponId) { boolean player = e instanceof Player; int style = player ? ((Player) e).getCombatDefinitions().getAttackStyle() : 0; style = style == 2 ? 1 : style == 3 ? 3 : 0; int defLvl = player ? ((Player) e).getSkills().getLevel(Skills.DEFENCE) : (int) (((NPC) e).getCombatLevel() * 0.6); int defBonus = player ? ((Player) e).getCombatDefinitions().getBonuses()[ CombatDefinitions.getMeleeDefenceBonus(CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle))] : 0; if (!player) { defBonus = ((NPC) e).getBonuses() != null ? ((NPC) e).getBonuses()[ CombatDefinitions.getMeleeDefenceBonus(CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle))] : 0; } double defMult = 1.0 * (player ? ((Player) e).getPrayer().getDefenceMultiplier() : 1.0); double cumulativeDef = defLvl * defMult + style; return 14 + cumulativeDef + (defBonus / 8) + ((cumulativeDef * (defBonus)) / 64); }
	 * 
	 * /** Gets the range accuracy of the player.
	 * 
	 * @param e The player.
	 * 
	 * @param attackStyle The attack style.
	 * 
	 * @return The range accuracy.
	 */
	/*
	 * public static double getRangeAccuracy(Player e, int attackStyle) { int style = attackStyle == 0 ? 3 : attackStyle == 2 ? 1 : 0; int attLvl = e.getSkills().getLevel(Skills.RANGE); int attBonus = e.getCombatDefinitions().getBonuses()[4]; double attMult = 1.0 * e.getPrayer().getRangeMultiplier() * e.getAuraManager().getRangeAccurayMultiplier(); double accuracyMultiplier = 1.05; if (fullVoidEquipped(e, 11664, 11675)) { accuracyMultiplier += 0.10; } double cumulativeAtt = attLvl * attMult + style; return (14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64)) * accuracyMultiplier; }
	 * 
	 * /** Gets the maximum range hit.
	 * 
	 * @param e The player.
	 * 
	 * @param attackStyle The attack style.
	 * 
	 * @return The maximum range hit.
	 */
	/*
	 * public static double getRangeMaximum(Player e, int attackStyle) { int style = attackStyle == 0 ? 3 : attackStyle == 2 ? 1 : 0; int strLvl = e.getSkills().getLevel(Skills.RANGE); int strBonus = e.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGED_STR_BONUS]; double strMult = 1.0 * e.getPrayer().getRangeMultiplier(); double hitMultiplier = 1.0; if (fullVoidEquipped(e, 11664, 11675)) { hitMultiplier += 0.1; } double cumulativeStr = strLvl * strMult + style; return (14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * hitMultiplier; }
	 * 
	 * /** Gets the range defence.
	 * 
	 * @param e The entity.
	 * 
	 * @return The maximum range defence.
	 */
	/*
	 * public static double getRangeDefence(Entity e) { boolean player = e instanceof Player; int style = player ? ((Player) e).getCombatDefinitions().getAttackStyle() : 0; style = style == 2 ? 1 : style == 3 ? 3 : 0; int defLvl = player ? ((Player) e).getSkills().getLevel(Skills.DEFENCE) : (int) (((NPC) e).getCombatLevel() * 0.6); int defBonus = player ? ((Player) e).getCombatDefinitions().getBonuses()[CombatDefinitions.RANGE_DEF] : 0; if (!player) { defBonus = ((NPC) e).getBonuses() != null ? ((NPC) e).getBonuses()[9] : 0; } double defMult = 1.0 * (player ? ((Player) e).getPrayer().getDefenceMultiplier() : 1.0); double cumulativeDef = defLvl * defMult + style; return 14 + cumulativeDef + (defBonus / 8) + ((cumulativeDef * defBonus) / 64); }
	 */

	private double getSpecialAccuracyModifier(Player player) {
		Item weapon = player.getEquipment().getItem(Equipment.SLOT_WEAPON);
		if (weapon == null)
			return 1;
		String name = weapon.getDefinitions().getName().toLowerCase();
		if (name.contains("whip") || name.contains("dragon longsword") || name.contains("dragon scimitar")
				|| name.contains("dragon dagger"))
			return 1.1;
		if (name.contains("anchor") || name.contains("magic longbow"))
			return 2;
		if (name.contains("dragon claws") || name.contains("armadyl godsword"))
			return 2;
		return 1;
	}

	public boolean hasFireCape(Player player) {
		int capeId = player.getEquipment().getCapeId();
		return capeId == 6570 || capeId == 20769 || capeId == 20771;
	}

	/*
	 * public static final int getMaxHit(Player player, int weaponId, int attackStyle, boolean ranging, boolean usingSpec, double specMultiplier) { if (ranging) { return (int) (getRangeMaximum(player, attackStyle) * specMultiplier); } return (int) (getMeleeMaximum(player, weaponId, attackStyle) * specMultiplier); }
	 */

	public static final boolean fullVanguardEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		int bootsId = player.getEquipment().getBootsId();
		int glovesId = player.getEquipment().getGlovesId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1 || bootsId == -1 || glovesId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(bootsId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(glovesId).getName().contains("Vanguard");
	}

	public static final boolean usingGoliathGloves(Player player) {
		String name = player.getEquipment().getItem(Equipment.SLOT_SHIELD) != null
				? player.getEquipment().getItem(Equipment.SLOT_SHIELD).getDefinitions().getName().toLowerCase()
				: "";
		if (player.getEquipment().getItem((Equipment.SLOT_HANDS)) != null) {
			if (player.getEquipment().getItem(Equipment.SLOT_HANDS).getDefinitions().getName().toLowerCase()
					.contains("goliath") && player.getEquipment().getWeaponId() == -1) {
				if (name.contains("defender") && name.contains("dragonfire shield"))
					return true;
				return true;
			}
		}
		return false;
	}

	public static final boolean fullVeracsEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Verac's")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Verac's")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Verac's")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Verac's");
	}

	public static final boolean fullDharokEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Dharok's")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Dharok's")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Dharok's")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Dharok's");
	}

	public static final boolean fullVoidEquipped(Player player, int... helmid) {
		boolean hasDeflector = player.getEquipment().getShieldId() == 19712;
		if (player.getEquipment().getGlovesId() != 8842) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		int legsId = player.getEquipment().getLegsId();
		boolean hasLegs = legsId != -1 && (legsId == 8840 || legsId == 19786 || legsId == 19788 || legsId == 19790);
		if (!hasLegs) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		int torsoId = player.getEquipment().getChestId();
		boolean hasTorso = torsoId != -1
				&& (torsoId == 8839 || torsoId == 10611 || torsoId == 19785 || torsoId == 19787 || torsoId == 19789);
		if (!hasTorso) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		if (hasDeflector)
			return true;
		int helmId = player.getEquipment().getHatId();
		if (helmId == -1)
			return false;
		boolean hasHelm = false;
		for (int id : helmid) {
			if (helmId == id) {
				hasHelm = true;
				break;
			}
		}
		if (!hasHelm)
			return false;
		return true;
	}

	public void delayNormalHit(int weaponId, int attackStyle, Hit... hits) {
		delayHit(0, weaponId, attackStyle, hits);
	}

	public Hit getMeleeHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.MELEE_DAMAGE);
	}

	public Hit getRangeHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.RANGE_DAMAGE);
	}

	public Hit getMagicHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.MAGIC_DAMAGE);
	}

	private void delayMagicHit(int delay, final Hit... hits) {
		delayHit(delay, -1, -1, hits);
	}

	public void resetVariables() {
		base_mage_xp = 0;
		mage_hit_gfx = 0;
		magic_sound = 0;
		max_poison_hit = 0;
		freeze_time = 0;
		reduceAttack = false;
		blood_spell = false;
		block_tele = false;
	}

	public void delayHit(int delay, final int weaponId, final int attackStyle, final Hit... hits) {
		addAttackedByDelay(hits[0].getSource());

		final Entity target = this.target;
		final int max_hit = this.max_hit;
		final double base_mage_xp = this.base_mage_xp;
		final int mage_hit_gfx = this.mage_hit_gfx;
		final int magic_sound = this.magic_sound;
		@SuppressWarnings("unused")
		final int max_poison_hit = this.max_poison_hit;
		final int freeze_time = this.freeze_time;
		@SuppressWarnings("unused")
		final boolean reduceAttack = this.reduceAttack;
		final boolean blood_spell = this.blood_spell;
		final boolean block_tele = this.block_tele;
		resetVariables();

		for (Hit hit : hits) {
			Player player = (Player) hit.getSource();
			if (target instanceof Player) {
				Player p2 = (Player) target;
				if (player.getPrayer().usingPrayer(1, 18))
					p2.sendSoulSplit(hit, player);
			}
			int damage = hit.getDamage() > target.getHitpoints() ? target.getHitpoints() : hit.getDamage();
			if (hit.getLook() == HitLook.RANGE_DAMAGE || hit.getLook() == HitLook.MELEE_DAMAGE) {
				double combatXp = damage / 2.5;
				if (combatXp > 0) {
					player.getAuraManager().checkSuccefulHits(hit.getDamage());
					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
						if (attackStyle == 2) {
							player.getSkills().addXp(Skills.RANGE, combatXp / 2);
							player.getSkills().addXp(Skills.DEFENCE, combatXp / 2);
						} else
							player.getSkills().addXp(Skills.RANGE, combatXp);

					} else {
						int xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle);
						if (xpStyle != CombatDefinitions.SHARED)
							player.getSkills().addXp(xpStyle, combatXp);
						else {
							player.getSkills().addXp(Skills.ATTACK, combatXp / 3);
							player.getSkills().addXp(Skills.STRENGTH, combatXp / 3);
							player.getSkills().addXp(Skills.DEFENCE, combatXp / 3);
						}
					}
					double hpXp = damage / 7.5;
					if (hpXp > 0)
						player.getSkills().addXp(Skills.HITPOINTS, hpXp);
				}
			} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (mage_hit_gfx != 0 && damage > 0) {
					if (freeze_time > 0) {
						target.addFreezeDelay(freeze_time, freeze_time == 0);
						if (freeze_time > 0)
							if (target instanceof Player) {
								((Player) target).stopAll(false);
							}
						target.addFrozenBlockedDelay(freeze_time + (4 * 1000));// four seconds of no freeze
					}
				} else if (damage < 0) {
					damage = 0;
				}
				double combatXp = base_mage_xp * 1 + (damage / 5);

				if (combatXp > 0) {
					player.getAuraManager().checkSuccefulHits(hit.getDamage());
					if (player.getCombatDefinitions().isDefensiveCasting()
							|| (hasPolyporeStaff(player) && player.getCombatDefinitions().getAttackStyle() == 1)) {
						int defenceXp = (int) (damage / 7.5);
						if (defenceXp > 0) {
							combatXp -= defenceXp;
							player.getSkills().addXp(Skills.DEFENCE, defenceXp / 7.5);
						}
					}
					player.getSkills().addXp(Skills.MAGIC, combatXp);
					double hpXp = damage / 7.5;
					if (hpXp > 0)
						player.getSkills().addXp(Skills.HITPOINTS, hpXp);
				}
			}
		}
		
		World.get().submit(new Task(delay) {
			@Override
			protected void execute() {
				for (Hit hit : hits) {
					boolean splash = false;
					Player player = (Player) hit.getSource();
					if (player.isDead() || player.hasFinished() || target.isDead() || target.hasFinished())
						return;
					if (hit.getDamage() > -1) {
						target.applyHit(hit); // also reduces damage if needed, pray
						// and special items affect here
					} else {
						splash = true;
						hit.setDamage(0);
					}
					doDefenceEmote();
					int damage = hit.getDamage() > target.getHitpoints() ? target.getHitpoints() : hit.getDamage();
					if ((damage >= max_hit * 0.90) && (hit.getLook() == HitLook.MAGIC_DAMAGE
							|| hit.getLook() == HitLook.RANGE_DAMAGE || hit.getLook() == HitLook.MELEE_DAMAGE))
						hit.setCriticalMark();
					if (hit.getLook() == HitLook.RANGE_DAMAGE || hit.getLook() == HitLook.MELEE_DAMAGE) {
						double combatXp = damage / 2.5;
						if (combatXp > 0) {
							if (hit.getLook() == HitLook.RANGE_DAMAGE) {
								if (weaponId != -1) {
									@SuppressWarnings("unused")
									String name = ItemDefinitions.getItemDefinitions(weaponId).getName();
//									if (name.contains("(p++)")) {
//										if (Utils.getRandom(8) == 0)
//											target.getPoison().makePoisoned(48);
//									} else if (name.contains("(p+)")) {
//										if (Utils.getRandom(8) == 0)
//											target.getPoison().makePoisoned(38);
//									} else if (name.contains("(p)")) {
//										if (Utils.getRandom(8) == 0)
//											target.getPoison().makePoisoned(28);
//									}
								}
							} else {
								if (weaponId != -1) {
									@SuppressWarnings("unused")
									String name = ItemDefinitions.getItemDefinitions(weaponId).getName();
//									if (name.contains("(p++)")) {
//										if (Utils.getRandom(8) == 0)
//											target.getPoison().makePoisoned(68);
//									} else if (name.contains("(p+)")) {
//										if (Utils.getRandom(8) == 0)
//											target.getPoison().makePoisoned(58);
//									} else if (name.contains("(p)")) {
//										if (Utils.getRandom(8) == 0)
//											target.getPoison().makePoisoned(48);
//									}
									if (target instanceof Player) {
//										if (((Player) target).getPolDelay() >= Utils.currentTimeMillis())
//											target.setNextGraphics(new Graphics(2320));
									}
								}
							}
						}
					} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
						if (splash) {
							target.setNextGraphics(new Graphics(85, 0, 96));
							playSound(227, player, target);
						} else {
							if (mage_hit_gfx != 0) {
								target.setNextGraphics(
										new Graphics(mage_hit_gfx, 0, mage_hit_gfx == 369 || mage_hit_gfx == 1843
												|| (mage_hit_gfx > 1844 && mage_hit_gfx < 1855) ? 0 : 96));
								if (blood_spell)
									player.heal(damage / 4);
								if (block_tele) {
									if (target instanceof Player) {
										Player targetPlayer = (Player) target;
										targetPlayer.setTeleBlockDelay((targetPlayer.getPrayer().usingPrayer(0, 17)
												|| targetPlayer.getPrayer().usingPrayer(1, 7) ? 100000 : 300000));
										targetPlayer.getPackets().sendGameMessage("You have been teleblocked.", true);
									}
								}
							}
							if (magic_sound > 0)
								playSound(magic_sound, player, target);
						}
					}
//					if (max_poison_hit > 0 && Utils.getRandom(10) == 0) {
//						if (!target.getPoison().isPoisoned())
//							target.getPoison().makePoisoned(max_poison_hit);
//					}
					if (target instanceof Player) {
						Player p2 = (Player) target;
						p2.closeInterfaces();
						if (p2.getCombatDefinitions().isAutoRelatie() && !p2.getActionManager().hasSkillWorking()
								&& !p2.hasWalkSteps())
							p2.getActionManager().setAction(new PlayerCombat(player));
					} else {
						NPC n = (NPC) target;
						if (!n.isUnderCombat() || n.canBeAttackedByAutoRelatie())
							n.setTarget(player);
					}

				}
				this.cancel();
			}
		});
	}

	private int getSoundId(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName.contains("dart") || weaponName.contains("knife"))
				return 2707;
		}
		return -1;
	}

	public static int getWeaponAttackEmote(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			if (weaponId == -2) {
				// punch/block:14393 kick:14307 spec:14417
				switch (attackStyle) {
				case 1:
					return 14307;
				default:
					return 14393;
				}
			}
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName != null && !weaponName.equals("null")) {
				if (weaponName.contains("crossbow"))
					return weaponName.contains("karil's crossbow") ? 2075 : 4230;
				if (weaponName.contains("bow"))
					return 426;
				if (weaponName.contains("chinchompa"))
					return 2779;
				if (weaponName.contains("staff of light")) {
					switch (attackStyle) {
					case 0:
						return 15072;
					case 1:
						return 15071;
					case 2:
						return 414;
					}
				}
				if (weaponName.contains("staff") || weaponName.contains("wand"))
					return 419;
				if (weaponName.contains("dart"))
					return 6600;
				if (weaponName.contains("knife"))
					return 9055;
				if (weaponName.contains("scimitar") || weaponName.contains("korasi's sword")) {
					switch (attackStyle) {
					case 2:
						return 15072;
					default:
						return 15071;
					}
				}
				if (weaponName.contains("granite mace"))
					return 400;
				if (weaponName.contains("mace")) {
					switch (attackStyle) {
					case 2:
						return 400;
					default:
						return 401;
					}
				}
				if (weaponName.contains("hatchet")) {
					switch (attackStyle) {
					case 2:
						return 401;
					default:
						return 395;
					}
				}
				if (weaponName.contains("warhammer")) {
					switch (attackStyle) {
					default:
						return 401;
					}
				}
				if (weaponName.contains("claws")) {
					switch (attackStyle) {
					case 2:
						return 1067;
					default:
						return 393;
					}
				}
				if (weaponName.contains("whip")) {
					switch (attackStyle) {
					case 1:
						return 11969;
					case 2:
						return 11970;
					default:
						return 11968;
					}
				}
				if (weaponName.contains("anchor")) {
					switch (attackStyle) {
					default:
						return 5865;
					}
				}
				if (weaponName.contains("tzhaar-ket-em")) {
					switch (attackStyle) {
					default:
						return 401;
					}
				}
				if (weaponName.contains("tzhaar-ket-om")) {
					switch (attackStyle) {
					default:
						return 13691;
					}
				}
				if (weaponName.contains("halberd")) {
					switch (attackStyle) {
					case 1:
						return 440;
					default:
						return 428;
					}
				}
				if (weaponName.contains("zamorakian spear")) {
					switch (attackStyle) {
					case 1:
						return 12005;
					case 2:
						return 12009;
					default:
						return 12006;
					}
				}
				if (weaponName.contains("spear")) {
					switch (attackStyle) {
					case 1:
						return 440;
					case 2:
						return 429;
					default:
						return 428;
					}
				}
				if (weaponName.contains("flail")) {
					return 2062;
				}
				if (weaponName.contains("javelin")) {
					return 10501;
				}
				if (weaponName.contains("morrigan's throwing axe"))
					return 10504;
				if (weaponName.contains("pickaxe")) {
					switch (attackStyle) {
					case 2:
						return 400;
					default:
						return 401;
					}
				}
				if (weaponName.contains("dagger")) {
					switch (attackStyle) {
					case 2:
						return 377;
					default:
						return 376;
					}
				}
				if (weaponName.contains("2h sword") || weaponName.equals("dominion sword")
						|| weaponName.equals("thok's sword") || weaponName.equals("saradomin sword")) {
					switch (attackStyle) {
					case 2:
						return 7048;
					case 3:
						return 7049;
					default:
						return 7041;
					}
				}
				if (weaponName.contains(" sword") || weaponName.contains("saber") || weaponName.contains("longsword")
						|| weaponName.contains("light") || weaponName.contains("excalibur")) {
					switch (attackStyle) {
					case 2:
						return 12310;
					default:
						return 12311;
					}
				}
				if (weaponName.contains("rapier") || weaponName.contains("brackish")) {
					switch (attackStyle) {
					case 2:
						return 13048;
					default:
						return 13049;
					}
				}
				if (weaponName.contains("katana")) {
					switch (attackStyle) {
					case 2:
						return 1882;
					default:
						return 1884;
					}
				}
				if (weaponName.contains("godsword")) {
					switch (attackStyle) {
					case 2:
						return 11980;
					case 3:
						return 11981;
					default:
						return 11979;
					}
				}
				if (weaponName.contains("greataxe")) {
					switch (attackStyle) {
					case 2:
						return 12003;
					default:
						return 12002;
					}
				}
				if (weaponName.contains("granite maul")) {
					switch (attackStyle) {
					default:
						return 1665;
					}
				}

			}
		}
		switch (weaponId) {
		case 16405:// novite maul
		case 16407:// Bathus maul
		case 16409:// Maramaros maul
		case 16411:// Kratonite maul
		case 16413:// Fractite maul
		case 18353:// chaotic maul
		case 16415:// Zephyrium maul
		case 16417:// Argonite maul
		case 16419:// Katagon maul
		case 16421:// Gorgonite maul
		case 16423:// Promethium maul
		case 16425:// primal maul
			return 2661; // maul
		case 13883: // morrigan thrown axe
			return 10504;
		case 15241:
			return 12174;
		default:
			switch (attackStyle) {
			case 1:
				return 423;
			default:
				return 422; // todo default emote
			}
		}
	}

	private void doDefenceEmote() {
		target.setNextAnimationNoPriority(new Animation(Combat.getDefenceEmote(target)));
	}

	private int getMeleeCombatDelay(Player player, int weaponId) {
		if (weaponId != -1) {
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();

			// Interval 2.4
			if (weaponName.equals("zamorakian spear") || weaponName.equals("korasi's sword"))
				return 3;
			// Interval 3.6
			if (weaponName.contains("godsword") || weaponName.contains("warhammer") || weaponName.contains("battleaxe")
					|| weaponName.contains("maul") || weaponName.equals("dominion sword"))
				return 5;
			// Interval 4.2
			if (weaponName.contains("greataxe") || weaponName.contains("halberd") || weaponName.contains("2h sword")
					|| weaponName.contains("two handed sword") || weaponName.contains("katana")
					|| weaponName.equals("thok's sword"))
				return 6;
			// Interval 3.0
			if (weaponName.contains("spear") || weaponName.contains(" sword") || weaponName.contains("longsword")
					|| weaponName.contains("light") || weaponName.contains("hatchet") || weaponName.contains("pickaxe")
					|| weaponName.contains("mace") || weaponName.contains("hasta") || weaponName.contains("warspear")
					|| weaponName.contains("flail") || weaponName.contains("hammers"))
				return 4;
		}
		switch (weaponId) {
		case 6527:// tzhaar-ket-em
			return 4;
		case 10887:// barrelchest anchor
			return 5;
		case 15403:// balmung
		case 6528:// tzhaar-ket-om
			return 6;
		default:
			return 3;
		}
	}

	@Override
	public void stop(final Player player) {
		player.setNextFaceEntity(null);
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || player.hasFinished() || target.isDead() || target.hasFinished()) {
			return false;
		}
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = target.getSize();
		int maxDistance = 16;
		if (player.getHeight() != target.getHeight() || distanceX > size + maxDistance || distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
			return false;
		}
		if (player.getFreezeDelay() >= Utils.currentTimeMillis()) {
			if (player.withinDistance(target, 0))// done
				return false;
			return true;
		}
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (!player.isCanPvp() || !p2.isCanPvp())
				return false;
		} else {
			NPC n = (NPC) target;
			if (n.isCantInteract()) {
				return false;
			}
			if (n instanceof Familiar) {
				Familiar familiar = (Familiar) n;
				if (!familiar.canAttack(target))
					return false;
			} else {
				if (!n.canBeAttackFromOutOfArea() && !MapAreas.isAtArea(n.getMapAreaNameHash(), player)) {
					return false;
				}
				if (n.getId() == 14578) {
					if (player.getEquipment().getWeaponId() != 2402
							&& player.getCombatDefinitions().getAutoCastSpell() <= 0 && !hasPolyporeStaff(player)) {
						player.getPackets().sendGameMessage("I'd better wield Silverlight first.");
						return false;
					} else {
						int slayerLevel = Combat.getSlayerLevelForNPC(n.getId());
						if (slayerLevel > player.getSkills().getLevel(Skills.SLAYER)) {
							player.getPackets().sendGameMessage(
									"You need at least a slayer level of " + slayerLevel + " to fight this.");
							return false;
						}
					}
				} else if (n.getId() == 6222 || n.getId() == 6223 || n.getId() == 6225 || n.getId() == 6227) {
					if (isRanging(player) == 0) {
						player.getPackets().sendGameMessage("I can't reach that!");
						return false;
					}
				}
			}
		}
		if (!(target instanceof NPC && ((NPC) target).isForceMultiAttacked())) {

			if (!target.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != target && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
					return false;
				}
				if (target.getAttackedBy() != player && target.getAttackedByDelay() > Utils.currentTimeMillis()) {
					return false;
				}
			}
		}
		int isRanging = isRanging(player);
		if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1 && !target.hasWalkSteps()) {
			player.resetWalkSteps();
			if (!player.addWalkSteps(target.getX() + size, target.getY())) {
				player.resetWalkSteps();
				if (!player.addWalkSteps(target.getX() - 1, target.getY())) {
					player.resetWalkSteps();
					if (!player.addWalkSteps(target.getX(), target.getY() + size)) {
						player.resetWalkSteps();
						if (!player.addWalkSteps(target.getX(), target.getY() - 1)) {
							return false;
						}
					}
				}
			}
			return true;
		} else if (isRanging == 0 && target.getSize() == 1 && player.getCombatDefinitions().getSpellId() <= 0
				&& !hasPolyporeStaff(player) && Math.abs(player.getX() - target.getX()) == 1
				&& Math.abs(player.getY() - target.getY()) == 1 && !target.hasWalkSteps()) {
			if (!player.addWalkSteps(target.getX(), player.getY(), 1))
				player.addWalkSteps(player.getX(), target.getY(), 1);
			return true;
		}
		maxDistance = isRanging != 0 || player.getCombatDefinitions().getSpellId() > 0 || hasPolyporeStaff(player) ? 7
				: 0;
		if ((!player.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target)))
				|| distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) {
			if (!player.hasWalkSteps()) {
				player.resetWalkSteps();
				player.addWalkStepsInteract(target.getX(), target.getY(), player.getRun() ? 2 : 1, size, true);
			}
			return true;
		} else {
			player.resetWalkSteps();
		}
//		if (player.getPolDelay() >= Utils.currentTimeMillis() && !(player.getEquipment().getWeaponId() == 15486
//				|| player.getEquipment().getWeaponId() == 22207 || player.getEquipment().getWeaponId() == 22209
//				|| player.getEquipment().getWeaponId() == 22211 || player.getEquipment().getWeaponId() == 22213))
//			player.setPolDelay(0);
		player.getTemporaryAttributtes().put("last_target", target);
		if (target != null)
			target.getTemporaryAttributtes().put("last_attacker", player);
		if (player.getCombatDefinitions().isInstantAttack()) {
			player.getCombatDefinitions().setInstantAttack(false);
			if (player.getCombatDefinitions().getAutoCastSpell() > 0)
				return true;
			if (player.getCombatDefinitions().isUsingSpecialAttack()) {
				if (!specialExecute(player))
					return true;
				player.getActionManager().setActionDelay(0);
				int weaponId = player.getEquipment().getWeaponId();
				int attackStyle = player.getCombatDefinitions().getAttackStyle();
				switch (weaponId) {
				case 4153:
					player.setNextAnimation(new Animation(1667));
					player.setNextGraphics(new Graphics(340, 0, 96 << 16));
					delayNormalHit(weaponId, attackStyle, getMeleeHit(player,
							getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
					break;
				}
				player.getActionManager().setActionDelay(4);
			}
			return true;
		}
		return true;
	}

	public boolean specialExecute(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		player.getCombatDefinitions().switchUsingSpecialAttack();
		int specAmt = getSpecialAmmount(weaponId);
		if (player.getCombatDefinitions().hasRingOfVigour())
			specAmt *= 0.9;
		if (WeaponSpecialDispatcher.execute(player, target, weaponId, this)) {
			return false;
		} else {
			if (player.getCombatDefinitions().getSpecialAttackPercentage() < specAmt) {
				player.getPackets().sendGameMessage("You don't have enough power left.");
				player.getCombatDefinitions().decreaseSpecialAttack(0);
				return false;
			}
			if (specAmt == 0) {
				player.getPackets()
						.sendGameMessage("This weapon has no special Attack, if you still see special bar please relogin.");
				player.getCombatDefinitions().decreaseSpecialAttack(0);
				return false;
			}
			player.getCombatDefinitions().decreaseSpecialAttack(specAmt);
		}
		return true;
	}

	/*
	 * 0 not ranging, 1 invalid ammo so stops att, 2 can range, 3 no ammo
	 */
	public static final int isRanging(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1)
			return 0;
		String name = ItemDefinitions.getItemDefinitions(weaponId).getName();
		if (name != null) { // those dont need arrows
			if (name.contains("knife") || name.contains("dart") || name.contains("javelin")
					|| name.contains("thrownaxe") || name.contains("throwing axe") || name.contains("crystal bow")
					|| name.equalsIgnoreCase("zaryte bow") || name.contains("chinchompa") || name.contains("Bolas"))
				return 2;
		}
		int ammoId = player.getEquipment().getAmmoId();
		switch (weaponId) {
		case 15241: // Hand cannon
			switch (ammoId) {
			case -1:
				return 3;
			case 15243: // bronze arrow
				return 2;
			default:
				return 1;
			}
		case 839: // longbow
		case 841: // shortbow
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
				return 2;
			default:
				return 1;
			}
		case 843: // oak longbow
		case 845: // oak shortbow
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
				return 2;
			default:
				return 1;
			}
		case 847: // willow longbow
		case 849: // willow shortbow
		case 13541: // Willow composite bow
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
				return 2;
			default:
				return 1;
			}
		case 851: // maple longbow
		case 853: // maple shortbow
		case 18331: // Maple longbow (sighted)
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
				return 2;
			default:
				return 1;
			}
		case 2883:// ogre bow
			switch (ammoId) {
			case -1:
				return 3;
			case 2866: // ogre arrow
				return 2;
			default:
				return 1;
			}
		case 4827:// Comp ogre bow
			switch (ammoId) {
			case -1:
				return 3;
			case 2866: // ogre arrow
			case 4773: // bronze brutal
			case 4778: // iron brutal
			case 4783: // steel brutal
			case 4788: // black brutal
			case 4793: // mithril brutal
			case 4798: // adamant brutal
			case 4803: // rune brutal
				return 2;
			default:
				return 1;
			}
		case 855: // yew longbow
		case 857: // yew shortbow
		case 10281: // Yew composite bow
		case 14121: // Sacred clay bow
		case 859: // magic longbow
		case 861: // magic shortbow
		case 10284: // Magic composite bow
		case 18332: // Magic longbow (sighted)
		case 6724: // seercull
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
			case 892: // rune arrow
				return 2;
			default:
				return 1;
			}
		case 11235: // dark bows
		case 15701:
		case 15702:
		case 15703:
		case 15704:
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
			case 892: // rune arrow
			case 11212: // dragon arrow
				return 2;
			default:
				return 1;
			}
		case 19143: // saradomin bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19152: // saradomin arrow
				return 2;
			default:
				return 1;
			}
		case 19146: // guthix bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19157: // guthix arrow
				return 2;
			default:
				return 1;
			}
		case 19149: // zamorak bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19162: // zamorak arrow
				return 2;
			default:
				return 1;
			}
		case 24338: // Royal crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 24336: // Coral bolts
				return 2;
			default:
				return 1;
			}
		case 24303: // Coral crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 24304: // Coral bolts
				return 2;
			default:
				return 1;
			}
		case 4734: // karil crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 4740: // bolt rack
				return 2;
			default:
				return 1;
			}
		case 10156: // hunters crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 10158: // Kebbit bolts
			case 10159: // Long kebbit bolts
				return 2;
			default:
				return 1;
			}
		case 8880: // Dorgeshuun c'bow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 8882: // bone bolts
				return 2;
			default:
				return 1;
			}
		case 14684: // zanik crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9145: // silver bolts
			case 8882: // bone bolts
				return 2;
			default:
				return 1;
			}
		case 767: // phoenix crossbow
		case 837: // crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
				return 2;
			default:
				return 1;
			}
		case 9174: // bronze crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9236: // Opal bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9176: // blurite crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9139: // Blurite bolts
			case 9237: // Jade bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9177: // iron crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9179: // steel crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
				return 2;
			default:
				return 1;
			}
		case 13081: // black crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9181: // Mith crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9145: // silver bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9183: // adam c bow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9145: // silver bolts wtf
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
			case 9242: // Ruby bolts (e)
			case 9243: // Diamond bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9185: // rune c bow
		case 18357: // chaotic crossbow
		case 18358:
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9144: // rune bolts
			case 9145: // silver bolts wtf
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
			case 9242: // Ruby bolts (e)
			case 9243: // Diamond bolts (e)
			case 9244: // Dragon bolts (e)
			case 9245: // Onyx bolts (e)
			case 24116: // Bakriminel bolts
				return 2;
			default:
				return 1;
			}
		default:
			return 0;
		}
	}

	/**
	 * Checks if the player is wielding polypore staff.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	private static boolean hasPolyporeStaff(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		return weaponId == 22494 || weaponId == 22496;
	}

	public Entity getTarget() {
		return target;
	}
	
	public static boolean hasInstantSpecial(final int weaponId) {
		switch (weaponId) {
		case 4153:
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
		case 1377:
		case 13472:
		case 35:// Excalibur
		case 8280:
		case 14632:
			return true;
		default:
			return false;
		}
	}

	public static void performInstantSpecial(Player player, final int weaponId) {
		int specAmt = PlayerCombat.getSpecialAmmount(weaponId);
		if (player.getCombatDefinitions().hasRingOfVigour())
			specAmt *= 0.9;
		if (player.getCombatDefinitions().getSpecialAttackPercentage() < specAmt) {
			player.getPackets().sendGameMessage("You don't have enough power left.");
			player.getCombatDefinitions().decreaseSpecialAttack(0);
			return;
		}
		if (player.getSwitchItemCache().size() > 0) {
			World.get().submit(new Task(0) {
				@Override
				protected void execute() {
					player.getCombatDefinitions().switchUsingSpecialAttack();
					this.cancel();
				}
			});
			return;
		}
		switch (weaponId) {
		case 4153:
			player.getCombatDefinitions().setInstantAttack(true);
			player.getCombatDefinitions().switchUsingSpecialAttack();
			Entity target = (Entity) player.getTemporaryAttributtes().get("last_target");
			if (target != null && target.getTemporaryAttributtes().get("last_attacker") == player) {
				if (!(player.getActionManager().getAction() instanceof PlayerCombat)
						|| ((PlayerCombat) player.getActionManager().getAction()).getTarget() != target) {
					player.getActionManager().setAction(new PlayerCombat(target));
				}
			}
			break;
		case 1377:
		case 13472:
			player.setNextAnimation(new Animation(1056));
			player.setNextGraphics(new Graphics(246));
			player.setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
			int defence = (int) (player.getSkills().getLevelForXp(Skills.DEFENCE) * 0.90D);
			int attack = (int) (player.getSkills().getLevelForXp(Skills.ATTACK) * 0.90D);
			int range = (int) (player.getSkills().getLevelForXp(Skills.RANGE) * 0.90D);
			int magic = (int) (player.getSkills().getLevelForXp(Skills.MAGIC) * 0.90D);
			int strength = (int) (player.getSkills().getLevelForXp(Skills.STRENGTH) * 1.2D);
			player.getSkills().set(Skills.DEFENCE, defence);
			player.getSkills().set(Skills.ATTACK, attack);
			player.getSkills().set(Skills.RANGE, range);
			player.getSkills().set(Skills.MAGIC, magic);
			player.getSkills().set(Skills.STRENGTH, strength);
			player.getCombatDefinitions().decreaseSpecialAttack(specAmt);
			break;
		case 35:// Excalibur
		case 8280:
		case 14632:
			player.setNextAnimation(new Animation(1168));
			player.setNextGraphics(new Graphics(247));
			player.setNextForceTalk(new ForceTalk("For Matrix!"));
			final boolean enhanced = weaponId == 14632;
			player.getSkills().set(Skills.DEFENCE, enhanced ? (int) (player.getSkills().getLevelForXp(Skills.DEFENCE) * 1.15D)
					: (player.getSkills().getLevel(Skills.DEFENCE) + 8));
			World.get().submit(new Task(4) {
				int count;
				@Override
				protected void execute() {
					if (player.isDead() || player.hasFinished() || player.getHitpoints() >= player.getMaxHitpoints()) {
						return;
					}
					player.heal(enhanced ? 80 : 40);
					if (count-- == 0) {
						return;
					}
				}
			});
			player.getCombatDefinitions().decreaseSpecialAttack(specAmt);
			break;
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
			player.setNextAnimation(new Animation(12804));
			player.setNextGraphics(new Graphics(2319));// 2320
			player.setNextGraphics(new Graphics(2321));
			player.getWatchMap().get("DRINKS").elapsed(60000);
			player.getCombatDefinitions().decreaseSpecialAttack(specAmt);
			break;
		}
	}
	
	public static void handleIncomingHit(Player player, Hit hit) {
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (player.getAuraManager().usingPenance()) {
			int amount = (int) (hit.getDamage() * 0.2);
			if (amount > 0)
				player.getPrayer().restorePrayer(amount);
		}
		Entity source = hit.getSource();
		if (source == null) {
			player.resetReceivedDamage();
			return;
		}
		if (player.getPrayer().hasPrayersOn() && hit.getDamage() != 0) {
			if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (player.getPrayer().usingPrayer(0, 17))
					hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
				else if (player.getPrayer().usingPrayer(1, 7)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(player, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						player.setNextGraphics(new Graphics(2228));
						player.setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				if (player.getPrayer().usingPrayer(0, 18))
					hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
				else if (player.getPrayer().usingPrayer(1, 8)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(player, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						player.setNextGraphics(new Graphics(2229));
						player.setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (player.getPrayer().usingPrayer(0, 19))
					hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
				else if (player.getPrayer().usingPrayer(1, 9)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(player, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						player.setNextGraphics(new Graphics(2230));
						player.setNextAnimation(new Animation(12573));
					}
				}
			}
		}
		if (hit.getDamage() >= 200) {
			if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				int reducedDamage = hit.getDamage()
						* player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			}
		}
		int shieldId = player.getEquipment().getShieldId();
		if (shieldId == 13742) { // elsyian
			if (Utils.getRandom(100) <= 70)
				hit.setDamage((int) (hit.getDamage() * 0.75));
		} else if (shieldId == 13740) { // divine
			int drain = (int) (Math.ceil(hit.getDamage() * 0.3) / 2);
			if (player.getPrayer().getPrayerpoints() >= drain) {
				hit.setDamage((int) (hit.getDamage() * 0.70));
				player.getPrayer().drainPrayer(drain);
			}
		}
		//TODO: player details veng
//		if (player.castedVeng && hit.getDamage() >= 4) {
//			player.castedVeng = false;
//			player.setNextForceTalk(new ForceTalk("Taste vengeance!"));
//			source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75), HitLook.REGULAR_DAMAGE));
//		}
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.getPrayer().hasPrayersOn()) {
				if (p2.getPrayer().usingPrayer(0, 24)) { // smite
					int drain = hit.getDamage() / 4;
					if (drain > 0)
						player.getPrayer().drainPrayer(drain);
				} else {
					if (hit.getDamage() == 0)
						return;
					if (!p2.getPrayer().isBoostedLeech()) {
						if (hit.getLook() == HitLook.MELEE_DAMAGE) {
							if (p2.getPrayer().usingPrayer(1, 19)) {
								if (Utils.getRandom(4) == 0) {
									p2.getPrayer().increaseTurmoilBonus(player);
									p2.getPrayer().setBoostedLeech(true);
									return;
								}
							} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap att
								if (Utils.getRandom(4) == 0) {
									if (p2.getPrayer().reachedMax(0)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(0);
										p2.getPackets().sendGameMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2214));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, player, 2215, 35, 35, 20, 5, 0, 0);
									World.get().submit(new Task(1) {
										@Override
										protected void execute() {
											player.setNextGraphics(new Graphics(2216));
											this.cancel();
										}
									});
									return;
								}
							} else {
								if (p2.getPrayer().usingPrayer(1, 10)) {
									if (Utils.getRandom(7) == 0) {
										if (p2.getPrayer().reachedMax(3)) {
											p2.getPackets().sendGameMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
										} else {
											p2.getPrayer().increaseLeechBonus(3);
											p2.getPackets().sendGameMessage(
													"Your curse drains Attack from the enemy, boosting your Attack.",
													true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.getPrayer().setBoostedLeech(true);
										World.sendProjectile(p2, player, 2231, 35, 35, 20, 5, 0, 0);
										World.get().submit(new Task(1) {
											@Override
											protected void execute() {
												player.setNextGraphics(new Graphics(2232));
											}
										});
										return;
									}
								}
								if (p2.getPrayer().usingPrayer(1, 14)) {
									if (Utils.getRandom(7) == 0) {
										if (p2.getPrayer().reachedMax(7)) {
											p2.getPackets().sendGameMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
										} else {
											p2.getPrayer().increaseLeechBonus(7);
											p2.getPackets().sendGameMessage(
													"Your curse drains Strength from the enemy, boosting your Strength.",
													true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.getPrayer().setBoostedLeech(true);
										World.sendProjectile(p2, player, 2248, 35, 35, 20, 5, 0, 0);
										World.get().submit(new Task(1) {
											@Override
											protected void execute() {
												player.setNextGraphics(new Graphics(2250));
											}
										});
										return;
									}
								}

							}
						}
						if (hit.getLook() == HitLook.RANGE_DAMAGE) {
							if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
								if (Utils.getRandom(4) == 0) {
									if (p2.getPrayer().reachedMax(1)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(1);
										p2.getPackets().sendGameMessage(
												"Your curse drains Range from the enemy, boosting your Range.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2217));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, player, 2218, 35, 35, 20, 5, 0, 0);
									World.get().submit(new Task(1) {
										@Override
										protected void execute() {
											player.setNextGraphics(new Graphics(2219));
										}
									});
									return;
								}
							} else if (p2.getPrayer().usingPrayer(1, 11)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(4)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(4);
										p2.getPackets().sendGameMessage(
												"Your curse drains Range from the enemy, boosting your Range.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, player, 2236, 35, 35, 20, 5, 0, 0);
									World.get().submit(new Task(1) {
										@Override
										protected void execute() {
											player.setNextGraphics(new Graphics(2238));
										}
									});
									return;
								}
							}
						}
						if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
							if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
								if (Utils.getRandom(4) == 0) {
									if (p2.getPrayer().reachedMax(2)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your sap curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(2);
										p2.getPackets().sendGameMessage(
												"Your curse drains Magic from the enemy, boosting your Magic.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2220));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, player, 2221, 35, 35, 20, 5, 0, 0);
									World.get().submit(new Task(1) {
										@Override
										protected void execute() {
											player.setNextGraphics(new Graphics(2222));
										}
									});
									return;
								}
							} else if (p2.getPrayer().usingPrayer(1, 12)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(5)) {
										p2.getPackets().sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(5);
										p2.getPackets().sendGameMessage(
												"Your curse drains Magic from the enemy, boosting your Magic.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, player, 2240, 35, 35, 20, 5, 0, 0);
									World.get().submit(new Task(1) {
										@Override
										protected void execute() {
											player.setNextGraphics(new Graphics(2242));
										}
									});
									return;
								}
							}
						}

						// overall

						if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
							if (Utils.getRandom(10) == 0) {
								if (p2.getPrayer().reachedMax(6)) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(6);
									p2.getPackets().sendGameMessage(
											"Your curse drains Defence from the enemy, boosting your Defence.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, player, 2244, 35, 35, 20, 5, 0, 0);
								World.get().submit(new Task(1) {
									@Override
									protected void execute() {
										player.setNextGraphics(new Graphics(2246));
									}
								});
								return;
							}
						}

						if (p2.getPrayer().usingPrayer(1, 15)) {
							if (Utils.getRandom(10) == 0) {
								if (player.getRunEnergy() <= 0) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.setRunEnergy(p2.getRunEnergy() > 90 ? 100 : p2.getRunEnergy() + 10);
									player.setRunEnergy(p2.getRunEnergy() > 10 ? player.getRunEnergy() - 10 : 0);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, player, 2256, 35, 35, 20, 5, 0, 0);
								World.get().submit(new Task(1) {
									@Override
									protected void execute() {
										player.setNextGraphics(new Graphics(2258));
									}
								});
								return;
							}
						}

						if (p2.getPrayer().usingPrayer(1, 16)) {
							if (Utils.getRandom(10) == 0) {
								if (player.getCombatDefinitions().getSpecialAttackPercentage() <= 0) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getCombatDefinitions().restoreSpecialAttack();
									player.getCombatDefinitions().decreaseSpecialAttack(10);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, player, 2252, 35, 35, 20, 5, 0, 0);
								World.get().submit(new Task(1) {
									@Override
									protected void execute() {
										player.setNextGraphics(new Graphics(2254));
									}
								});
								return;
							}
						}

						if (p2.getPrayer().usingPrayer(1, 4)) { // sap spec
							if (Utils.getRandom(10) == 0) {
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2223));
								p2.getPrayer().setBoostedLeech(true);
								if (player.getCombatDefinitions().getSpecialAttackPercentage() <= 0) {
									p2.getPackets().sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									player.getCombatDefinitions().decreaseSpecialAttack(10);
								}
								World.sendProjectile(p2, player, 2224, 35, 35, 20, 5, 0, 0);
								World.get().submit(new Task(1) {
									@Override
									protected void execute() {
										player.setNextGraphics(new Graphics(2225));
									}
								});
								return;
							}
						}
					}
				}
			}
		} else {
			NPC n = (NPC) source;
			if (n.getId() == 13448)
				player.sendSoulSplit(hit, n);
		}
	}
}