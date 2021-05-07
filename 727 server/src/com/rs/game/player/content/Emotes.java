package com.rs.game.player.content;

import java.util.Optional;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;

import npc.NPC;

/**
 * A new way of handling Emotes & Special Emotes such as Linked Queue emotes &
 * Skillcapes.
 * 
 * @author Dennis
 *
 */
public final class Emotes {
	
	/**
	 * A list of Emotes for the Player to perform from the Emotes tab
	 * 
	 * @author Dennis
	 *
	 */
	public enum Emote {
		YES((byte) 0, Optional.of(new Animation(855)), Optional.empty(), Optional.empty()),
		NO((byte) 1, Optional.of(new Animation(856)), Optional.empty(), Optional.empty()),
		BOW((byte) 2, Optional.of(new Animation(858)), Optional.empty(), Optional.empty()),
		ANGRY((byte) 3, Optional.of(new Animation(859)), Optional.empty(), Optional.empty()),
		THINKING((byte) 4, Optional.of(new Animation(857)), Optional.empty(), Optional.empty()),
		WAVE((byte) 5, Optional.of(new Animation(863)), Optional.empty(), Optional.empty()),
		SHRUG((byte) 6, Optional.of(new Animation(2113)), Optional.empty(), Optional.empty()),
		CHEER((byte) 7, Optional.of(new Animation(862)), Optional.empty(), Optional.empty()),
		BECKON((byte) 8, Optional.of(new Animation(864)), Optional.empty(), Optional.empty()),
		LAUGH((byte) 9, Optional.of(new Animation(861)), Optional.empty(), Optional.empty()),
		JUMP_FOR_JOY((byte) 10, Optional.of(new Animation(2109)), Optional.empty(), Optional.empty()),
		YAWN((byte) 11, Optional.of(new Animation(2111)), Optional.empty(), Optional.empty()),
		DANCE((byte) 12, Optional.of(new Animation(866)), Optional.empty(), Optional.empty()),
		JIG((byte) 13, Optional.of(new Animation(2106)), Optional.empty(), Optional.empty()),
		TWIRL((byte) 14, Optional.of(new Animation(2107)), Optional.empty(), Optional.empty()),
		HEADBANG((byte) 15, Optional.of(new Animation(2108)), Optional.empty(), Optional.empty()),
		CRY((byte) 16, Optional.of(new Animation(860)), Optional.empty(), Optional.empty()),
		BLOW_KISS((byte) 17, Optional.of(new Animation(1374)), Optional.of(new Graphics(1702)), Optional.empty()),
		PANIC((byte) 18, Optional.of(new Animation(2105)), Optional.empty(), Optional.empty()),
		RASPBERRY((byte) 19, Optional.of(new Animation(2110)), Optional.empty(), Optional.empty()),
		CLAP((byte) 20, Optional.of(new Animation(865)), Optional.empty(), Optional.empty()),
		SALUTE((byte) 21, Optional.of(new Animation(2112)), Optional.empty(), Optional.empty()),
		GOBLIN_BOW((byte) 22, Optional.of(new Animation(0x84F)), Optional.empty(), Optional.empty()),
		GOBLIN_SALUTE((byte) 23, Optional.of(new Animation(0x850)), Optional.empty(), Optional.empty()),
		GLASS_BOX((byte) 24, Optional.of(new Animation(1131)), Optional.empty(), Optional.empty()),
		CLIMB_ROPE((byte) 25, Optional.of(new Animation(1130)), Optional.empty(), Optional.empty()),
		LEAN((byte) 26, Optional.of(new Animation(1129)), Optional.empty(), Optional.empty()),
		GLASS_WALL((byte) 27, Optional.of(new Animation(1128)), Optional.empty(), Optional.empty()),
		IDEA((byte) 28, Optional.of(new Animation(4275)), Optional.empty(), Optional.empty()),
		STOMP((byte) 29, Optional.of(new Animation(1745)), Optional.empty(), Optional.empty()),
		FLAP((byte) 30, Optional.of(new Animation(4280)), Optional.empty(), Optional.empty()),
		SLAP_HEAD((byte) 31, Optional.of(new Animation(4276)), Optional.empty(), Optional.empty()),
		ZOMBIE_WALK((byte) 32, Optional.of(new Animation(3544)), Optional.empty(), Optional.empty()),
		ZOMBIE_DANCE((byte) 33, Optional.of(new Animation(3543)), Optional.empty(), Optional.empty()),
		ZOMBIE_HAND((byte) 34, Optional.of(new Animation(7272)), Optional.of(new Graphics(1244)), Optional.empty()),
		SCARED((byte) 35, Optional.of(new Animation(2836)), Optional.empty(), Optional.empty()),
		BUNNY_HOP((byte) 36, Optional.of(new Animation(6111)), Optional.empty(), Optional.empty()),
		//skillcape is 37
		SNOWMAN_DANCE((byte) 38, Optional.of(new Animation(7531)), Optional.empty(), Optional.empty()),
		AIR_GUITAR((byte) 39, Optional.of(new Animation(2414)), Optional.of(new Graphics(1537)), Optional.of(SpecialEmote.AIR_GUITAR)),
		SAFETY_FIRST((byte) 40, Optional.of(new Animation(8770)), Optional.of(new Graphics(1553)), Optional.empty()),
		EXPLORE((byte) 41, Optional.of(new Animation(9990)), Optional.of(new Graphics(1734)), Optional.empty()),
		TRICK((byte) 42, Optional.of(new Animation(10530)), Optional.of(new Graphics(1864)), Optional.empty()),
		FREEZE((byte) 43, Optional.of(new Animation(11044)), Optional.of(new Graphics(1973)), Optional.empty()),
		TURKEY((byte) 44, Optional.empty(),  Optional.empty(), Optional.of(SpecialEmote.TURKEY)),
		AROUND_THE_WORLD_IN_EGGTY_DAYS((byte) 45, Optional.of(new Animation(11542)), Optional.of(new Graphics(2037)), Optional.empty()),
		DRAMATIC_POINT((byte) 46, Optional.of(new Animation(11542)), Optional.of(new Graphics(2037)), Optional.empty()),
		FAINT((byte) 47, Optional.of(new Animation(14869)), Optional.of(new Graphics(2837)), Optional.empty()),
		PUPPET_MASTER((byte) 48, Optional.of(new Animation(14869)), Optional.of(new Graphics(2837)), Optional.empty()),
		TASK_MASTER((byte) 49, Optional.of(new Animation(15033)), Optional.of(new Graphics(2930)), Optional.empty()),
		SEAL_OF_APPROVAL((byte) 50, Optional.empty(),  Optional.empty(), Optional.of(SpecialEmote.SEAL_OF_APPROVAL)),
		CAT_FIGHT((byte) 51, Optional.of(new Animation(2252)), Optional.empty(), Optional.empty()),
		TALK_TO_THE_HAND((byte) 52, Optional.of(new Animation(2416)), Optional.empty(), Optional.empty()),
		SHAKE_HANDS((byte) 53, Optional.of(new Animation(2303)), Optional.empty(), Optional.empty()),
		HIGH_FIVE((byte) 54, Optional.of(new Animation(2312)), Optional.empty(), Optional.empty()),
		FACE_PALM((byte) 55, Optional.of(new Animation(2254)), Optional.empty(), Optional.empty()),
		SURRENDER((byte) 56, Optional.of(new Animation(2360)), Optional.empty(), Optional.empty()),
		LEVITATE((byte) 57, Optional.of(new Animation(2327)), Optional.empty(), Optional.empty()),
		MUSCLE_MAN_POSE((byte) 58, Optional.of(new Animation(2566)), Optional.empty(), Optional.empty()),
		ROFL((byte) 59, Optional.of(new Animation(2347)), Optional.empty(), Optional.empty()),
		BREATHE_FIRE((byte) 60, Optional.of(new Animation(2238)), Optional.of(new Graphics(358)), Optional.empty()),
		STORM((byte) 61, Optional.of(new Animation(2563)), Optional.of(new Graphics(365)), Optional.empty()),
		SNOW((byte) 62, Optional.of(new Animation(2417)), Optional.of(new Graphics(364)), Optional.empty()),
		INVOKE_SPRING((byte) 63, Optional.of(new Animation(15357)), Optional.of(new Graphics(1391)), Optional.empty()),
		HEAD_IN_SAND((byte) 64, Optional.of(new Animation(12926)), Optional.of(new Graphics(1761)), Optional.empty()),
		HULA_HOOP((byte) 65, Optional.of(new Animation(12928)), Optional.empty(), Optional.empty()),
		DISAPPEAR((byte) 66, Optional.of(new Animation(12929)), Optional.of(new Graphics(1760)), Optional.empty()),
		GHOST((byte) 67, Optional.of(new Animation(12932)), Optional.of(new Graphics(1762)), Optional.empty()),
		BRING_IT((byte) 68, Optional.of(new Animation(12934)), Optional.empty(), Optional.empty()),
		PALM_FIST((byte) 69, Optional.of(new Animation(12931)), Optional.empty(), Optional.empty()),
		LIVIMG_ON_BORROWED_TIME((byte) 93, Optional.empty(),  Optional.empty(), Optional.of(SpecialEmote.BORROWED_TIME)),
		TROUBADOUR_DANCE((byte) 94, Optional.of(new Animation(15399 )), Optional.empty(), Optional.empty()),
		EVIL_LAUGH((byte) 95, Optional.of(new Animation(15535)), Optional.empty(), Optional.empty()), //female: 15536 
		GOLF_CLAP((byte) 96, Optional.of(new Animation(15520)), Optional.empty(), Optional.empty()),
		LOL_CANO((byte) 97, Optional.of(new Animation(15532)), Optional.of(new Graphics(2191)), Optional.empty()),
		INFERNAL_POWER((byte) 98, Optional.of(new Animation(15529)), Optional.of(new Graphics(2197)), Optional.empty()),
		DIVINE_POWER((byte) 99, Optional.of(new Animation(15524)), Optional.of(new Graphics(2195)), Optional.empty()),
		YOUR_DEAD((byte) 100, Optional.of(new Animation(14195)), Optional.empty(), Optional.empty()),
		SCREAM((byte) 101, Optional.of(new Animation(15526)), Optional.empty(), Optional.empty()), //female 15527
		TORNADO((byte) 102, Optional.of(new Animation(15530)), Optional.of(new Graphics(2196)), Optional.empty()),
		CHAOTIC_COOKERY((byte) 103, Optional.of(new Animation(15604)), Optional.of(new Graphics(2239)), Optional.empty()),
		ROFL_COPTER((byte) 104, Optional.of(new Animation(16373)), Optional.of(new Graphics(3009)), Optional.empty()), //female 163734
		NATURES_MIGHT((byte) 105, Optional.of(new Animation(16376)), Optional.of(new Graphics(3011)), Optional.empty()),
		INNER_POWER((byte) 106, Optional.of(new Animation(16382)), Optional.of(new Graphics(3014)), Optional.empty()),
		WEREWOLF((byte) 107, Optional.empty(),  Optional.empty(), Optional.of(SpecialEmote.WEREWOLF)),
		CELEBRATE((byte) 108, Optional.of(new Animation(16913)), Optional.of(new Graphics(3175)), Optional.empty()),
		BREAKDANCE((byte) 109, Optional.of(new Animation(17079)), Optional.empty(), Optional.empty()),
		MAHJARRAT_TRANSFORMATION((byte) 110, Optional.of(new Animation(17103)), Optional.of(new Graphics(3222)), Optional.empty()),
		BREAK_WIND((byte) 111, Optional.of(new Animation(17076)), Optional.of(new Graphics(3226)), Optional.empty()),
		BACKFLIP((byte) 112, Optional.of(new Animation(17101)), Optional.of(new Graphics(3221)), Optional.empty()),
		GRAVE_DIGGER((byte) 113, Optional.of(new Animation(17077)), Optional.of(new Graphics(3219)), Optional.empty()),
		FROG_TRANSFORMATION((byte) 114, Optional.of(new Animation(17080)), Optional.of(new Graphics(3220)), Optional.empty()),
		MEXICAN_WAVE((byte) 115, Optional.of(new Animation(17163)), Optional.empty(), Optional.empty()),
		SPORTSMAN((byte) 116, Optional.of(new Animation(17166)), Optional.empty(), Optional.empty()),
		;

		/**
		 * Gets the Button Id (slot id in this specific case).
		 * 
		 * @return buttonId
		 */
		private final byte getButtonId() {
			return buttonId;
		}

		/**
		 * Gets the Animation for this emote.
		 * 
		 * @return animation
		 */
		private final Optional<Animation> getAnimation() {
			return animation;
		}

		/**
		 * Gets the Graphics for this emote.
		 * 
		 * @return graphis
		 */
		private final Optional<Graphics> getGraphics() {
			return graphics;
		}

		/**
		 * Gets the special emote event (used for Skillcapes, Linked Queue tasks,
		 * etc..).
		 * 
		 * @return specialEmote
		 */
		private final Optional<SpecialEmote> getSpecialEmote() {
			return specialEmote;
		}

		/**
		 * The button Id (slot id).
		 */
		private final byte buttonId;
		
		/**
		 * The Animation being performed.
		 */
		private final Optional<Animation> animation;
		
		/**
		 * The Graphics being performed.
		 */
		private final Optional<Graphics> graphics;
		
		/**
		 * The Special Emote being performed.
		 */
		private final Optional<SpecialEmote> specialEmote;

		/**
		 * Constructs a new Player Emote.
		 * 
		 * @param buttonId
		 * @param animation
		 * @param graphics
		 * @param specialEmote
		 */
		private Emote(byte buttonId, Optional<Animation> animation, Optional<Graphics> graphics,
				Optional<SpecialEmote> specialEmote) {
			this.buttonId = buttonId;
			this.animation = animation;
			this.graphics = graphics;
			this.specialEmote = specialEmote;
		}

		/**
		 * Executes the Emote.
		 * 
		 * @param player
		 * @param buttonId
		 */
		public static void executeEmote(Player player, byte buttonId) {
			if (!player.getWatchMap().get("EMOTE").elapsed(1600)) {
				return;
			}
			for (Emote emote : Emote.values()) {
				if (buttonId == emote.getButtonId()) {
					player.getWatchMap().get("EMOTE").reset();
					emote.getSpecialEmote().ifPresent(user -> user.handleSpecialEmote(player));

					emote.getAnimation().ifPresent(player::setNextAnimation);
					emote.getGraphics().ifPresent(player::setNextGraphics);
				}
			}
		}
	}

	/**
	 * A list of Special Emote events to take place (Skillcapes, Linked Queue events, etc..).
	 * @author Dennis
	 *
	 */
	private enum SpecialEmote {
		AIR_GUITAR {
			@Override
			protected boolean handleSpecialEmote(Player player) {
				player.getPackets().sendMusicEffect(302);
				return true;
			}
		},
		TURKEY {
			@Override
			protected boolean handleSpecialEmote(Player player) {
				LinkedTaskSequence turkeySeq = new LinkedTaskSequence();
				turkeySeq.connect(1, () -> {
					player.setNextAnimation(new Animation(10994));
					player.setNextGraphics(new Graphics(86));
				});
				turkeySeq.connect(2, () -> {
					player.setNextAnimation(new Animation(10996));
					player.getAppearance().transformIntoNPC(8499);
				});
				turkeySeq.connect(6, () -> {
					player.setNextAnimation(new Animation(10995));
					player.setNextGraphics(new Graphics(86));
					player.getAppearance().transformIntoNPC(-1);
				});
				turkeySeq.start();
				return true;
			}
		},
		SEAL_OF_APPROVAL {
			@Override
			protected boolean handleSpecialEmote(Player player) {
				LinkedTaskSequence soaSeq = new LinkedTaskSequence();
				soaSeq.connect(1, () -> {
					player.setNextAnimation(new Animation(15104));
					player.setNextGraphics(new Graphics(1287));
				});
				soaSeq.connect(2, () -> {
					int random = (int) (Math.random() * (2 + 1));
					player.setNextAnimation(new Animation(15106));
					player.getAppearance()
							.transformIntoNPC(random == 0 ? 13255 : (random == 1 ? 13256 : 13257));
				});
				soaSeq.connect(3, () -> player.setNextAnimation(new Animation(15108)));
				soaSeq.connect(4, () -> {
					player.setNextAnimation(new Animation(15105));
					player.setNextGraphics(new Graphics(1287));
					player.getAppearance().transformIntoNPC(-1);
				});
				soaSeq.start();
				return true;
			}
		},
		WEREWOLF {
			@Override
			protected boolean handleSpecialEmote(Player player) {
				 player.setNextAnimation(new Animation(16380));
				 player.setNextGraphics(new Graphics(3013));
				 player.setNextGraphics(new Graphics(3016));
				return true;
			}
		},
		BORROWED_TIME {
			@Override
			protected boolean handleSpecialEmote(Player player) {
				if (!World.isTileFree(player.getHeight(), player.getX(), player.getY(), 3)) {
					player.getPackets().sendGameMessage("You need clear space in order to perform this emote.", true);
					return false;
				} else if (player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You can't do this here.", true);
					return false;
				}
				final NPC reaper = new NPC(14388, new WorldTile(player.getX(), player.getY() - 2, player.getHeight()), 0, false);
				reaper.setLocation(reaper);
				reaper.setNextFaceEntity(player);
				player.setNextFaceEntity(reaper);
				
				LinkedTaskSequence timeSeq = new LinkedTaskSequence();
				timeSeq.connect(1, () -> {
					reaper.setNextAnimation(new Animation(13964));
					player.setNextGraphics(new Graphics(1766));
					player.setNextAnimation(new Animation(13965));
				});
				timeSeq.connect(10, () -> {
					reaper.setFinished(true);
					World.removeNPC(reaper);
					reaper.setNextFaceEntity(null);
				});
				timeSeq.connect(11, () -> {
					player.setNextForceTalk(new ForceTalk(
							"Phew! Close call."));
						player.setNextFaceEntity(null);
				});
				timeSeq.start();
				return true;
			}
		};

		/**
		 * The execution method for the Special Emote.
		 * @param player
		 * @return state
		 */
		protected boolean handleSpecialEmote(Player player) {
			return false;
		}
	}
}