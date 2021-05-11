package com.rs.game.player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.HintIconsManager;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.player.actions.ActionManager;
import com.rs.game.player.content.*;
import com.rs.game.player.content.pet.PetManager;
import com.rs.game.player.controlers.ControlerManager;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.route.CoordsEvent;
import com.rs.game.route.strategy.RouteEvent;
import com.rs.game.task.Task;
import com.rs.game.task.impl.CombatEffectTask;
import com.rs.game.task.impl.SkillActionTask;
import com.rs.net.Session;
import com.rs.net.decoders.LogicPacket;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.net.encoders.WorldPacketsEncoder;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.utils.IntegerInputAction;
import com.rs.utils.IsaacKeyPair;
import com.rs.utils.Logger;
import com.rs.utils.MutableNumber;
import com.rs.utils.Stopwatch;
import com.rs.utils.Utils;

import mysql.impl.SendStarter;
import npc.corp.CorpBeastControler;
import npc.familiar.Familiar;
import npc.pet.Pet;
import player.CombatDefinitions;
import player.PlayerCombat;
import player.type.AntifireDetails;
import player.type.CombatEffect;
import skills.Skills;

public class Player extends Entity {

	public static final byte TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1, RUN_MOVE_TYPE = 2;

	// transient stuff
	private transient String username;
	private transient Session session;
	private transient boolean clientLoadedMapRegion;
	private transient byte displayMode;
	private transient short screenWidth;
	private transient short screenHeight;
	private transient InterfaceManager interfaceManager;
	private transient HintIconsManager hintIconsManager;
	private transient ActionManager actionManager;
	private transient PriceCheckManager priceCheckManager;
	private transient CoordsEvent coordsEvent;
	private transient FriendChatsManager currentFriendChat;
	private transient Trade trade;
	private transient IsaacKeyPair isaacKeyPair;
	private transient Pet pet;

	//Stones
	private transient boolean[] activatedLodestones;
	private transient LodeStone lodeStone;

	//Pins
	private transient BankPin pin;
	public transient String lastIPBankWasOpened;
	public transient boolean bypass;
	private transient int pinpinpin;
	public transient boolean hasPinOpenedToday = false;
	public transient long lastOpenedWithPin = -1;
	public transient boolean setPin = false;
	public transient boolean openPin = false;
	public transient boolean startpin = false;
	private transient int[] bankpins = new int[] { 0, 0, 0, 0 };
	private transient int[] confirmpin = new int[] { 0, 0, 0, 0 };
	private transient int[] openBankPin = new int[] { 0, 0, 0, 0 };
	private transient int[] changeBankPin = new int[] { 0, 0, 0, 0 };

	// used for packets logic
	private transient ConcurrentLinkedQueue<LogicPacket> logicPackets;

	// used for update
	private transient LocalPlayerUpdate localPlayerUpdate;
	private transient LocalNPCUpdate localNPCUpdate;

	private transient byte temporaryMovementType;
	private transient boolean updateMovementType;

	// player stages - not personal
	private transient boolean started;
	private transient boolean isActive;
	public void isactive(boolean active) {
		this.isActive = active;
	}

	private transient long packetsDecoderPing;
	private transient boolean resting;
	private transient boolean canPvp;
	private transient boolean cantTrade;
	private transient long lockDelay; // used for doors and stuff like that
	private transient Runnable closeInterfacesEvent;
	private transient long lastPublicMessage;
	private transient List<Byte> switchItemCache;
	private transient boolean disableEquip;
	private transient boolean castedVeng;
	private transient double hpBoostMultiplier;
	private transient boolean largeSceneView;
	private transient RouteEvent routeEvent;

	// interface

	// saving stuff
	private String password;
	private String displayName;
	
	/**
	 * The amount of authority this player has over others.
	 */
	public Rights rights = Rights.PLAYER;
	
	/**
	 * Creates a new instance of a Players details
	 */
	public PlayerDetails details = new PlayerDetails();
	
	/**
	 * Gets the amount of authority this player has over others.
	 * @return the authority this player has.
	 */
	public Rights getRights() {
		return rights;
	}
	
	/**
	 * Sets the value for {@link Player#rights}.
	 * @param rights the new value to set.
	 */
	public void setRights(Rights rights) {
		this.rights = rights;
	}
	
	private Appearance appearence;
	private Inventory inventory;
	private Equipment equipment;
	private Skills skills;
	private CombatDefinitions combatDefinitions;
	private Prayer prayer;
	private Bank bank;
	private ControlerManager controlerManager;
	private MusicsManager musicsManager;
	private EmotesManager emotesManager;
	private FriendsIgnores friendsIgnores;
	private Familiar familiar;
	private AuraManager auraManager;
	private PetManager petManager;
	private double runEnergy;
	private boolean allowChatEffects;
	private boolean mouseButtons;
	private byte privateChatSetup;
	private byte friendChatSetup;
	
	private boolean forceNextMapLoadRefresh;

	// game bar status
	private byte publicStatus;
	private byte clanStatus;
	private byte tradeStatus;

	// Used for storing recent ips and password
	private transient ArrayList<String> passwordList = new ArrayList<String>();
	private transient ArrayList<String> ipList = new ArrayList<String>();

	private ChargesManager charges;

	private String currentFriendChatOwner;
	private byte summoningLeftClickOption;
	private List<String> ownedObjectsManagerKeys;
	
	private SquealOfFortune sof;

	// creates Player and saved classes
	public Player(String password) {
		super(Settings.START_PLAYER_LOCATION);
		setHitpoints(Settings.START_PLAYER_HITPOINTS);
		this.password = password;
		this.toolbelt = new Toolbelt();
		appearence = new Appearance();
		inventory = new Inventory();
		equipment = new Equipment();
		skills = new Skills();
		combatDefinitions = new CombatDefinitions();
		prayer = new Prayer();
		bank = new Bank();
		details = new PlayerDetails();
		controlerManager = new ControlerManager();
		musicsManager = new MusicsManager();
		emotesManager = new EmotesManager();
		friendsIgnores = new FriendsIgnores();
		charges = new ChargesManager();
		auraManager = new AuraManager();
		petManager = new PetManager();
		pin = new BankPin();
		lodeStone = new LodeStone();
		runEnergy = 100D;
		allowChatEffects = true;
		mouseButtons = true;
		ownedObjectsManagerKeys = new LinkedList<String>();
		passwordList = new ArrayList<String>();
		ipList = new ArrayList<String>();
	}

	public void init(Session session, String username, byte displayMode, short screenWidth, short screenHeight, IsaacKeyPair isaacKeyPair) {
		// temporary deleted after reset all chars
		if (auraManager == null)
			auraManager = new AuraManager();
		if (petManager == null)
			petManager = new PetManager();
		if (details == null)
			details = new PlayerDetails();
		if(toolbelt == null)
			this.toolbelt = new Toolbelt();
		if (lodeStone == null)
			lodeStone = new LodeStone();
		if (activatedLodestones == null)
			activatedLodestones = new boolean[16];
		if (pin == null)
			pin = new BankPin();

		if (pinpinpin != 1) {
			pinpinpin = 1;
			bankpins = new int[] { 0, 0, 0, 0 };
			confirmpin = new int[] { 0, 0, 0, 0 };
			openBankPin = new int[] { 0, 0, 0, 0 };
			changeBankPin = new int[] { 0, 0, 0, 0 };
		}

		this.session = session;
		this.username = username;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.isaacKeyPair = isaacKeyPair;
		interfaceManager = new InterfaceManager(this);
		hintIconsManager = new HintIconsManager(this);
		priceCheckManager = new PriceCheckManager(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		actionManager = new ActionManager(this);
		sof = new SquealOfFortune(this);
		trade = new Trade(this);
		lodeStone.setPlayer(this);
		pin.setPlayer(this);
		// loads player on saved instances
		appearence.setPlayer(this);
		inventory.setPlayer(this);
		equipment.setPlayer(this);
		skills.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		bank.setPlayer(this);
		controlerManager.setPlayer(this);
		musicsManager.setPlayer(this);
		emotesManager.setPlayer(this);
		friendsIgnores.setPlayer(this);
		auraManager.setPlayer(this);
		charges.setPlayer(this);
		petManager.setPlayer(this);
		temporaryMovementType = -1;
		logicPackets = new ConcurrentLinkedQueue<LogicPacket>();
		switchItemCache = Collections.synchronizedList(new ArrayList<Byte>());
		initEntity();
		packetsDecoderPing = Utils.currentTimeMillis();
		World.addPlayer(this);
		updateEntityRegion(this);
		if (Settings.DEBUG)
			Logger.log(this, "Initiated player: " + username + ", pass: " + password);

		// Do not delete >.>, useful for security purpose. this wont waste that much space..
		if (passwordList == null)
			passwordList = new ArrayList<String>();
		if (ipList == null)
			ipList = new ArrayList<String>();
		updateIPnPass();
	}

	public SquealOfFortune getSquealOfFortune() {
		return sof;
	}

	public boolean hasSkull() {
		return getSkullTimer().get() > 0;
	}

	public void refreshSpawnedItems() {
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId).getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave()) && this != item.getOwner()
						|| item.getTile().getHeight() != getHeight())
					continue;
				getPackets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId).getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave()) && this != item.getOwner()
						|| item.getTile().getHeight() != getHeight())
					continue;
				getPackets().sendGroundItem(item);
			}
		}
	}

	public void refreshSpawnedObjects() {
		for (int regionId : getMapRegionsIds()) {
			List<WorldObject> spawnedObjects = World.getRegion(regionId).getSpawnedObjects();
			if (spawnedObjects != null) {
				for (WorldObject object : spawnedObjects)
					if (object.getHeight() == getHeight())
						getPackets().sendSpawnedObject(object);
			}
			List<WorldObject> removedObjects = World.getRegion(regionId).getRemovedObjects();
			if (removedObjects != null) {
				for (WorldObject object : removedObjects)
					if (object.getHeight() == getHeight())
						getPackets().sendDestroyObject(object);
			}
		}
	}

	public BankPin getBankPin() {
		return pin;
	}

	public boolean getSetPin() {
		return setPin;
	}

	public boolean getOpenedPin() {
		return openPin;
	}

	public int[] getPin() {
		return bankpins;
	}

	public int[] getConfirmPin() {
		return confirmpin;
	}

	public int[] getOpenBankPin() {
		return openBankPin;
	}

	public int[] getChangeBankPin() {
		return changeBankPin;
	}

	// now that we inited we can start showing game
	public void start() {
		loadMapRegions();
		started = true;
		run();

		if (isDead())
			sendDeath(null);
	}

	public void stopAll() {
		stopAll(true);
	}

	public void stopAll(boolean stopWalk) {
		stopAll(stopWalk, true);
	}

	public void stopAll(boolean stopWalk, boolean stopInterface) {
		stopAll(stopWalk, stopInterface, true);
	}

	// as walk done clientsided - not anymore buddy
	public void stopAll(boolean stopWalk, boolean stopInterfaces, boolean stopActions) {
		if (stopInterfaces)
			closeInterfaces();
		if (stopWalk){
			coordsEvent = null;
			routeEvent = null;
			resetWalkSteps();
			getPackets().sendResetMinimapFlag();
		}
		if (stopActions)
			actionManager.forceStop();
		combatDefinitions.resetSpells(false);
	}

	@Override
	public void reset(boolean attributes) {
		super.reset(attributes);
		refreshHitPoints();
		hintIconsManager.removeAll();
		skills.restoreSkills();
		combatDefinitions.resetSpecialAttack();
		prayer.reset();
		combatDefinitions.resetSpells(true);
		resting = false;
		getPoisonDamage().set(0);
		castedVeng = false;
		setRunEnergy(100);
		appearence.getAppeareanceBlocks();
	}

	@Override
	public void reset() {
		reset(true);
	}

	public void closeInterfaces() {
		if (interfaceManager.containsScreenInter())
			interfaceManager.closeScreenInterface();
		if (interfaceManager.containsInventoryInter())
			interfaceManager.closeInventoryInterface();
		
		getInterfaceManager().closeChatBoxInterface();
		getTemporaryAttributtes().remove("dialogue_event");
		
		if (closeInterfacesEvent != null) {
			closeInterfacesEvent.run();
			closeInterfacesEvent = null;
		}
	}

	public void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = false;
	}

	@Override
	public void loadMapRegions() {
		boolean wasAtDynamicRegion = isAtDynamicRegion();
		super.loadMapRegions();
		clientLoadedMapRegion = false;
		if (isAtDynamicRegion()) {
			getPackets().sendDynamicMapRegion(!started);
			if (!wasAtDynamicRegion)
				localNPCUpdate.reset();
		} else {
			getPackets().sendMapRegion(!started);
			if (wasAtDynamicRegion)
				localNPCUpdate.reset();
		}
		forceNextMapLoadRefresh = false;
	}

	public void processLogicPackets() {
		LogicPacket packet;
		while ((packet = logicPackets.poll()) != null)
			WorldPacketsDecoder.decodeLogicPacket(this, packet);
	}

	private transient byte miscTick = 0;
	private transient byte healTick = 0;
	
	@Override
	public void processEntity() {
		if (!isActive())
			return;
		super.processEntity();
		processLogicPackets();
		if (coordsEvent != null && coordsEvent.processEvent(this))
			coordsEvent = null;
		if (routeEvent != null && routeEvent.processEvent(this))
			routeEvent = null;
		if (musicsManager.musicEnded())
			musicsManager.replayMusic();
		
		if (!(getControlerManager().getControler() instanceof Wilderness) && isAtWild()
				&& !Wilderness.isAtWildSafe(this)) {
			getControlerManager().startControler("Wilderness");
		}
		
		miscTick++;
		boolean usingBerserk = Prayer.usingBerserker(this);
		if (miscTick % (usingBerserk ? 110 : 96) == 0)
			drainSkills();
		boolean usingRapidRestore = Prayer.usingRapidRestore(this);
		if (miscTick % (usingRapidRestore ? 48 : 96) == 0)
			restoreSkills();
		healTick++;
        boolean usingRenewal = Prayer.usingRapidRenewal(this);
        boolean usingRapidHeal = Prayer.usingRapidHeal(this);
        if (healTick % (usingRenewal ? 2 : isResting() ? 2 : usingRapidHeal ? 5 : 10) == 0)
            restoreHitPoints();

		charges.process();
		auraManager.process();
		actionManager.process();
		controlerManager.process();
	}
	
	public final boolean isAtWild() {
		return (getX() >= 3011 && getX() <= 3132 && getY() >= 10052 && getY() <= 10175)
				|| (getX() >= 2940 && getX() <= 3395 && getY() >= 3525 && getY() <= 4000)
				|| (getX() >= 3264 && getX() <= 3279 && getY() >= 3279 && getY() <= 3672)
				|| (getX() >= 3158 && getX() <= 3181 && getY() >= 3679 && getY() <= 3697)
				|| (getX() >= 3280 && getX() <= 3183 && getY() >= 3885 && getY() <= 3888)
				|| (getX() >= 3012 && getX() <= 3059 && getY() >= 10303 && getY() <= 10351)
				|| (getX() >= 3060 && getX() <= 3072 && getY() >= 10251 && getY() <= 10263);
	}


	public void restoreSkills() {
        for (int skill = 0; skill < 25; skill++) {
            if (skill == Skills.HITPOINTS || skill == Skills.SUMMONING || skill == Skills.PRAYER)
                continue;
            int currentLevel = getSkills().getLevel(skill);
            int normalLevel = getSkills().getLevelForXp(skill);
            if (currentLevel < normalLevel) {
                getSkills().set(skill, currentLevel + 1);

            }
        }
    }
    
    private void drainSkills() {
        for (int skill = 0; skill < 25; skill++) {
            if (skill == Skills.HITPOINTS)
                continue;
            int currentLevel = getSkills().getLevel(skill);
            int normalLevel = getSkills().getLevelForXp(skill);
            if (currentLevel > normalLevel) {
                getSkills().set(skill, currentLevel - 1);

            }
        }
    }
    

	@Override
	public void processReceivedHits() {
		if (lockDelay > Utils.currentTimeMillis())
			return;
		super.processReceivedHits();
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || temporaryMovementType != -1 || updateMovementType;
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		temporaryMovementType = -1;
		updateMovementType = false;
		if (!clientHasLoadedMapRegion()) {
			// load objects and items here
			setClientHasLoadedMapRegion();
			refreshSpawnedObjects();
			refreshSpawnedItems();
		}
	}

	public void toogleRun(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if (update)
			sendRunButtonConfig();
	}

	public void setRunHidden(boolean run) {
		super.setRun(run);
		updateMovementType = true;
	}

	@Override
	public void setRun(boolean run) {
		if (run != getRun()) {
			super.setRun(run);
			updateMovementType = true;
			sendRunButtonConfig();
		}
	}

	public void sendRunButtonConfig() {
		getPackets().sendConfig(173, resting ? 3 : getRun() ? 1 : 0);
	}

	public void run() {
		if (World.exiting_start != 0) {
			int delayPassed = (int) ((Utils.currentTimeMillis() - World.exiting_start) / 1000);
			getPackets().sendSystemUpdate(World.exiting_delay - delayPassed);
		}
		getAppearance().generateAppearenceData();
		getPlayerDetails().setLastIP(getSession().getIP());
		getInterfaceManager().sendInterfaces();
		if (getRights().isStaff() && Settings.PIN_ACTIVE) {
			lock();
			getPackets().sendInputIntegerScript("Whats the keycode?", new IntegerInputAction() {
				@Override
				public void handle(int input) {
					System.out.println(input);
					if (input != Settings.STAFF_PIN)
						logout(false);
					else 
						unlock();
				}
			});
		}
		getPackets().sendRunEnergy();
		getPackets().sendItemsLook();
		refreshAllowChatEffects();
		refreshMouseButtons();
		refreshPrivateChatSetup();
		refreshOtherChatsSetup();
		CombatEffect.values().forEach($it -> {
			if($it.onLogin(this))
				World.get().submit(new CombatEffectTask(this, $it));
		});
		Settings.STAFF.entrySet().forEach(staff -> {
			if (getUsername().equalsIgnoreCase(staff.getKey()))
				setRights(staff.getValue());
		});
		sendRunButtonConfig();
		getPackets().sendGameMessage("Welcome to " + Settings.SERVER_NAME + ".");
		getPackets().sendGameMessage(Settings.LASTEST_UPDATE);

		toolbelt.setPlayer(this);
		toolbelt.init();

		sendDefaultPlayersOptions();
		checkMultiArea();
		getInventory().init();
		getEquipment().init();
		getSkills().init();
		getCombatDefinitions().init();
		getPrayer().init();
		getFriendsIgnores().init();
		refreshHitPoints();
		getPrayer().refreshPrayerPoints();
		getPackets().sendGlobalConfig(823, 1);
		getPackets().sendConfig(281, 1000); // unlock can't do this on tutorial
		getPackets().sendConfig(1160, -1); // unlock summoning orb
		getPackets().sendConfig(1159, 1);
		getCombatDefinitions().sendUnlockAttackStylesButtons();
		getPackets().sendGameBarStages();
		getMusicsManager().init();
		getEmotesManager().refreshListConfigs();
		if(this.rights == Rights.ADMINISTRATOR)
			lodeStone.unlockAllLodestones();
		if (getCurrentFriendChatOwner() != null) {
			FriendChatsManager.joinChat(getCurrentFriendChatOwner(), this);
			if (currentFriendChat == null) // failed
				currentFriendChatOwner = null;
		}
		if (getFamiliar() != null) {
			getFamiliar().respawnFamiliar(this);
		} else {
			getPetManager().init();
		}
		isActive = true;
		updateMovementType = true;
		getAppearance().getAppeareanceBlocks();
		getControlerManager().login(); // checks what to do on login after welcome
		OwnedObjectManager.linkKeys(this);
		
		if (!HostManager.contains(getUsername(), HostListType.STARTER_RECEIVED)) {
			new SendStarter(World.getSQLPool(), this).submit();
			HostManager.add(this, HostListType.STARTER_RECEIVED, true);
		}
		if (!getRun())
			toogleRun(true);
	}

	public Toolbelt getToolbelt() {
		return toolbelt;
	}

	@SuppressWarnings("unused")
	private void sendUnlockedObjectConfigs() {
		refreshLodestoneNetwork();
	}

	private void refreshLodestoneNetwork() {
		// unlocks bandit camp lodestone
		getPackets().sendConfigByFile(358, 15);
		// unlocks lunar isle lodestone
		getPackets().sendConfigByFile(2448, 190);
		// unlocks alkarid lodestone
		getPackets().sendConfigByFile(10900, 1);
		// unlocks ardougne lodestone
		getPackets().sendConfigByFile(10901, 1);
		// unlocks burthorpe lodestone
		getPackets().sendConfigByFile(10902, 1);
		// unlocks catherbay lodestone
		getPackets().sendConfigByFile(10903, 1);
		// unlocks draynor lodestone
		getPackets().sendConfigByFile(10904, 1);
		// unlocks edgeville lodestone
		getPackets().sendConfigByFile(10905, 1);
		// unlocks falador lodestone
		getPackets().sendConfigByFile(10906, 1);
		// unlocks lumbridge lodestone
		getPackets().sendConfigByFile(10907, 1);
		// unlocks port sarim lodestone
		getPackets().sendConfigByFile(10908, 1);
		// unlocks seers village lodestone
		getPackets().sendConfigByFile(10909, 1);
		// unlocks taverley lodestone
		getPackets().sendConfigByFile(10910, 1);
		// unlocks varrock lodestone
		getPackets().sendConfigByFile(10911, 1);
		// unlocks yanille lodestone
		getPackets().sendConfigByFile(10912, 1);
	}

	public void updateIPnPass() {
		if (getPasswordList().size() > 25)
			getPasswordList().clear();
		if (getIPList().size() > 50)
			getIPList().clear();
		if (!getPasswordList().contains(getPassword()))
			getPasswordList().add(getPassword());
		if (!getIPList().contains(getPlayerDetails().getLastIP()))
			getIPList().add(getPlayerDetails().getLastIP());
		return;
	}

	public void sendDefaultPlayersOptions() {
		// To Debug full list of options possible
//		for (int i = 1; i < 10; i++)
//		getPackets().sendPlayerOption("Option-"+i, i, i == 1);
		getPackets().sendPlayerOption("Follow", 2, false);
		getPackets().sendPlayerOption("Trade with", 4, false);
		getPackets().sendPlayerOption("Req Assist", 5, false);
	}

	@Override
	public void checkMultiArea() {
		if (!hasStarted())
			return;
		boolean isAtMultiArea = isForceMultiArea() ? true : World.isMultiArea(this);
		if (isAtMultiArea && !isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendGlobalConfig(616, 1);
		} else if (!isAtMultiArea && isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendGlobalConfig(616, 0);
		}
	}

	/**
	 * Logs the player out.
	 * @param lobby If we're logging out to the lobby.
	 */
	public void logout(boolean lobby) {
		World.get().queueLogout(this);
	}

	public void forceLogout() {
		getPackets().sendLogout(false);
		isActive = false;
		realFinish();
	}

	private transient boolean finishing;

	@Override
	public void finish() {
		finish(0);
	}

	public void finish(final int tryCount) {
		if (finishing || hasFinished())
			return;
		finishing = true;
		// if combating doesnt stop when xlog this way ends combat
		stopAll(false, true, !(actionManager.getAction() instanceof PlayerCombat));
		long currentTime = Utils.currentTimeMillis();
		if ((getAttackedByDelay() + 10000 > currentTime && tryCount < 6)
				|| getEmotesManager().getNextEmoteEnd() >= currentTime || lockDelay >= currentTime) {
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						packetsDecoderPing = Utils.currentTimeMillis();
						finishing = false;
						finish(tryCount + 1);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 10, TimeUnit.SECONDS);
			return;
		}
		realFinish();
	}

	public void realFinish() {
		if (hasFinished())
			return;
		stopAll();
		controlerManager.logout(); // checks what to do on before logout for
		// login
		isActive = false;
		friendsIgnores.sendFriendsMyStatus(false);
		if (currentFriendChat != null)
			currentFriendChat.leaveChat(this, true);
		if (familiar != null && !familiar.isFinished())
			familiar.dissmissFamiliar(true);
		else if (pet != null)
			pet.finish();
		World.get().getTask().cancel(this);
		setSkillAction(Optional.empty());
		setFinished(true);
		session.setDecoder(-1);
		AccountCreation.savePlayer(this);
		updateEntityRegion(this);
		World.removePlayer(this);
		if (Settings.DEBUG)
			Logger.log(this, "Finished Player: " + username + ", pass: " + password);
	}

	@Override
	public boolean restoreHitPoints() {
		if (isDead()) {
			return false;
		}
		boolean update = super.restoreHitPoints();
		if (update) {
			if (prayer.usingPrayer(0, 9))
				super.restoreHitPoints();
			if (resting)
				super.restoreHitPoints();
			refreshHitPoints();
		}
		return update;
	}

	public void refreshHitPoints() {
		getPackets().sendConfigByFile(7198, getHitpoints());
	}

	@Override
	public void removeHitpoints(Hit hit) {
		super.removeHitpoints(hit);
		refreshHitPoints();
	}

	@Override
	public int getMaxHitpoints() {
		return skills.getLevel(Skills.HITPOINTS) * 10 + equipment.getEquipmentHpIncrease();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<String> getPasswordList() {
		return passwordList;
	}

	public ArrayList<String> getIPList() {
		return ipList;
	}
	
	public int getMessageIcon() {
		return getRights() == Rights.ADMINISTRATOR ? 2 : getRights() == Rights.MODERATOR ? 1 : 0;
	}

	public WorldPacketsEncoder getPackets() {
		return session.getWorldPackets();
	}

	public boolean[] getActivatedLodestones() {
		return activatedLodestones;
	}

	public LodeStone getLodeStones() {
		return lodeStone;
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getDisplayName() {
		if (displayName != null)
			return displayName;
		return Utils.formatPlayerNameForDisplay(username);
	}

	public boolean hasDisplayName() {
		return displayName != null;
	}

	public Appearance getAppearance() {
		return appearence;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public byte getTemporaryMoveType() {
		return temporaryMovementType;
	}

	public void setTemporaryMoveType(byte temporaryMovementType) {
		this.temporaryMovementType = temporaryMovementType;
	}

	public LocalPlayerUpdate getLocalPlayerUpdate() {
		return localPlayerUpdate;
	}

	public LocalNPCUpdate getLocalNPCUpdate() {
		return localNPCUpdate;
	}

	public byte getDisplayMode() {
		return displayMode;
	}

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public void setPacketsDecoderPing(long packetsDecoderPing) {
		this.packetsDecoderPing = packetsDecoderPing;
	}

	public long getPacketsDecoderPing() {
		return packetsDecoderPing;
	}

	public Session getSession() {
		return session;
	}

	public void setScreenWidth(short screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenHeight(short screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion;
	}

	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = true;
	}

	public void setDisplayMode(byte displayMode) {
		this.displayMode = displayMode;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Skills getSkills() {
		return skills;
	}

	public double getRunEnergy() {
		return runEnergy;
	}

	public void drainRunEnergy() {
		setRunEnergy(runEnergy - 1);
	}

	public void setRunEnergy(double runEnergy) {
		this.runEnergy = runEnergy;
		getPackets().sendRunEnergy();
	}

	public boolean isResting() {
		return resting;
	}

	public void setResting(boolean resting) {
		this.resting = resting;
		sendRunButtonConfig();
	}

	public ActionManager getActionManager() {
		return actionManager;
	}

	public void setCoordsEvent(CoordsEvent coordsEvent) {
		this.coordsEvent = coordsEvent;
	}

	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	public void sendSoulSplit(final Hit hit, final Entity user) {
		final Player target = this;
		if (hit.getDamage() > 0)
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
		prayer.drainPrayer(hit.getDamage() / 5);
		World.get().submit(new Task(0) {
			@Override
			protected void execute() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0)
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
				this.cancel();
			}
		});
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		PlayerCombat.handleIncomingHit(this, hit);
	}

	@Override
	public void sendDeath(final Entity source) {
		World.get().submit(new PlayerDeath(this));
	}

	public void sendItemsOnDeath(Player killer) {
//		if (getRights().isStaff())
//			return;
		getCharges().die();
		getAuraManager().removeAura();
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<Item>();
		for (int i = 0; i < 14; i++) {
			if (getEquipment().getItem(i) != null && getEquipment().getItem(i).getId() != -1
					&& getEquipment().getItem(i).getAmount() != -1)
				containedItems.add(new Item(getEquipment().getItem(i).getId(), getEquipment().getItem(i).getAmount()));
		}
		for (int i = 0; i < 28; i++) {
			if (getInventory().getItem(i) != null && getInventory().getItem(i).getId() != -1
					&& getInventory().getItem(i).getAmount() != -1)
				containedItems.add(new Item(getInventory().getItem(i).getId(), getInventory().getItem(i).getAmount()));
		}
		if (containedItems.isEmpty())
			return;
		int keptAmount = 0;
		if (!(getControlerManager().getControler() instanceof CorpBeastControler)) {
			keptAmount = hasSkull() ? 0 : 3;
			if (getPrayer().usingPrayer(0, 10) || getPrayer().usingPrayer(1, 0))
				keptAmount++;
		}
		CopyOnWriteArrayList<Item> keptItems = new CopyOnWriteArrayList<Item>();
		Item lastItem = new Item(1, 1);
		for (int i = 0; i < keptAmount; i++) {
			for (Item item : containedItems) {
				int price = item.getDefinitions().getValue();
				if (price >= lastItem.getDefinitions().getValue()) {
					lastItem = item;
				}
			}
			keptItems.add(lastItem);
			containedItems.remove(lastItem);
			lastItem = new Item(1, 1);
		}
		getInventory().reset();
		getEquipment().reset();
		for (Item item : keptItems) {
			getInventory().addItem(item);
		}
		/** This Checks which items that is listed in the 'PROTECT_ON_DEATH' **/
		for (Item item : containedItems) {	// This checks the items you had in your inventory or equipped
			for (String string : Settings.PROTECT_ON_DEATH) {	//	This checks the matched items from the list 'PROTECT_ON_DEATH'
				if (item.getDefinitions().getName().toLowerCase().contains(string) || item.getDefinitions().exchangableItem) {
					getInventory().addItem(item);	//	This adds the items that is matched and listed in 'PROTECT_ON_DEATH'
					containedItems.remove(item);	//	This remove the whole list of the contained items that is matched
				}
			}
		}

		/** This to avoid items to be dropped in the list 'PROTECT_ON_DEATH' **/
		for (Item item : containedItems) {	//	This checks the items you had in your inventory or equipped
			for (String string : Settings.PROTECT_ON_DEATH) {	//	This checks the matched items from the list 'PROTECT_ON_DEATH'
				if (item.getDefinitions().getName().toLowerCase().contains(string)) {
					containedItems.remove(item);	//	This remove the whole list of the contained items that is matched
				}
			}
			FloorItem.createGroundItem(item, getLastWorldTile(), killer == null ? this : killer, false, 180, true, true);	//	This dropps the items to the killer, and is showed for 180 seconds
		}
		for (Item item : containedItems) {
			FloorItem.createGroundItem(item, getLastWorldTile(), killer == null ? this : killer, false, 180, true, true);
		}
	}

	public void sendRandomJail(Player p) {
		p.resetWalkSteps();
		switch (Utils.getRandom(6)) {
		case 0:
			p.setNextWorldTile(new WorldTile(2669, 10387, 0));
			break;
		case 1:
			p.setNextWorldTile(new WorldTile(2669, 10383, 0));
			break;
		case 2:
			p.setNextWorldTile(new WorldTile(2669, 10379, 0));
			break;
		case 3:
			p.setNextWorldTile(new WorldTile(2673, 10379, 0));
			break;
		case 4:
			p.setNextWorldTile(new WorldTile(2673, 10385, 0));
			break;
		case 5:
			p.setNextWorldTile(new WorldTile(2677, 10387, 0));
			break;
		case 6:
			p.setNextWorldTile(new WorldTile(2677, 10383, 0));
			break;
		}
	}

	@Override
	public int getSize() {
		return appearence.getSize();
	}

	public boolean isCanPvp() {
		return canPvp;
	}

	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		appearence.getAppeareanceBlocks();
		getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
		getPackets().sendPlayerUnderNPCPriority(canPvp);
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public long getLockDelay() {
		return lockDelay;
	}

	public boolean isLocked() {
		return lockDelay >= Utils.currentTimeMillis();
	}

	public void lock() {
		lockDelay = Long.MAX_VALUE;
	}

	public void lock(long time) {
		lockDelay = Utils.currentTimeMillis() + (time * 600);
	}

	public void unlock() {
		lockDelay = 0;
	}

	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay) {
		useStairs(emoteId, dest, useDelay, totalDelay, null);
	}

	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay, final String message) {
		stopAll();
		lock(totalDelay);
		if (emoteId != -1)
			setNextAnimation(new Animation(emoteId));
		if (useDelay == 0)
			setNextWorldTile(dest);
		else {
			World.get().submit(new Task(useDelay - 1) {
				@Override
				protected void execute() {
					if (isDead())
						return;
					setNextWorldTile(dest);
					if (message != null)
						getPackets().sendGameMessage(message);
					this.cancel();
				}
			});
		}
	}

	public Bank getBank() {
		return bank;
	}

	public ControlerManager getControlerManager() {
		return controlerManager;
	}

	public void switchMouseButtons() {
		mouseButtons = !mouseButtons;
		refreshMouseButtons();
	}

	public void switchAllowChatEffects() {
		allowChatEffects = !allowChatEffects;
		refreshAllowChatEffects();
	}

	public void refreshAllowChatEffects() {
		getPackets().sendConfig(171, allowChatEffects ? 0 : 1);
	}

	public void refreshMouseButtons() {
		getPackets().sendConfig(170, mouseButtons ? 0 : 1);
	}

	public void refreshPrivateChatSetup() {
		getPackets().sendConfig(287, privateChatSetup);
	}

	public void refreshOtherChatsSetup() {
		int value = friendChatSetup << 6;
		getPackets().sendConfig(1438, value);
	}

	public void setPrivateChatSetup(byte privateChatSetup) {
		this.privateChatSetup = privateChatSetup;
	}

	public void setFriendChatSetup(byte friendChatSetup) {
		this.friendChatSetup = friendChatSetup;
	}

	public byte getPrivateChatSetup() {
		return privateChatSetup;
	}

	public boolean isForceNextMapLoadRefresh() {
		return forceNextMapLoadRefresh;
	}

	public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
		this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
	}

	public FriendsIgnores getFriendsIgnores() {
		return friendsIgnores;
	}

	/*
	 * do not use this, only used by pm
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public void heal(int ammount, int extra) {
		super.heal(ammount, extra);
		refreshHitPoints();
	}

	public MusicsManager getMusicsManager() {
		return musicsManager;
	}

	public HintIconsManager getHintIconsManager() {
		return hintIconsManager;
	}

	public boolean isCastVeng() {
		return castedVeng;
	}

	public void setCastVeng(boolean castVeng) {
		this.castedVeng = castVeng;
	}

	public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
		this.closeInterfacesEvent = closeInterfacesEvent;
	}

	public ChargesManager getCharges() {
		return charges;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public EmotesManager getEmotesManager() {
		return emotesManager;
	}

	public String getLastHostname() {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(getPlayerDetails().getLastIP());
			String hostname = addr.getHostName();
			return hostname;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PriceCheckManager getPriceCheckManager() {
		return priceCheckManager;
	}

	public boolean isUpdateMovementType() {
		return updateMovementType;
	}

	public long getLastPublicMessage() {
		return lastPublicMessage;
	}

	public void setLastPublicMessage(long lastPublicMessage) {
		this.lastPublicMessage = lastPublicMessage;
	}

	public void kickPlayerFromFriendsChannel(String name) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.kickPlayerFromChat(this, name);
	}

	public void sendFriendsChannelMessage(String message) {
		if (currentFriendChat == null)
			return;
		currentFriendChat.sendMessage(this, message);
	}

	public void sendPublicChatMessage(PublicChatMessage message) {
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null)
				continue;
			for (Integer playerIndex : playersIndexes) {
				Player p = World.getPlayers().get(playerIndex);
				if (p == null || !p.hasStarted() || p.hasFinished()
						|| p.getLocalPlayerUpdate().getLocalPlayers()[getIndex()] == null)
					continue;
				p.getPackets().sendPublicMessage(this, message);
			}
		}
	}

	public void addLogicPacketToQueue(LogicPacket packet) {
		for (LogicPacket p : logicPackets) {
			if (p.getId() == packet.getId()) {
				logicPackets.remove(p);
				break;
			}
		}
		logicPackets.add(packet);
	}
	
	public void setRouteEvent(RouteEvent routeEvent) {
		this.routeEvent = routeEvent;
	}
	
	public Trade getTrade() {
		return trade;
	}

	public void setTeleBlockDelay(long teleDelay) {
		getTemporaryAttributtes().put("TeleBlocked", teleDelay + Utils.currentTimeMillis());
	}

	public long getTeleBlockDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("TeleBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public void setPrayerDelay(long teleDelay) {
		getTemporaryAttributtes().put("PrayerBlocked", teleDelay + Utils.currentTimeMillis());
		prayer.closeAllPrayers();
	}

	public long getPrayerDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("PrayerBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public FriendChatsManager getCurrentFriendChat() {
		return currentFriendChat;
	}

	public void setCurrentFriendChat(FriendChatsManager currentFriendChat) {
		this.currentFriendChat = currentFriendChat;
	}

	public String getCurrentFriendChatOwner() {
		return currentFriendChatOwner;
	}

	public void setCurrentFriendChatOwner(String currentFriendChatOwner) {
		this.currentFriendChatOwner = currentFriendChatOwner;
	}

	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}

	public void setSummoningLeftClickOption(byte summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}

	public List<Byte> getSwitchItemCache() {
		return switchItemCache;
	}

	public AuraManager getAuraManager() {
		return auraManager;
	}

	public byte getMovementType() {
		if (getTemporaryMoveType() != -1)
			return getTemporaryMoveType();
		return getRun() ? RUN_MOVE_TYPE : WALK_MOVE_TYPE;
	}

	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) // temporary
			ownedObjectsManagerKeys = new LinkedList<String>();
		return ownedObjectsManagerKeys;
	}

	public void setDisableEquip(boolean equip) {
		disableEquip = equip;
	}

	public boolean isEquipDisabled() {
		return disableEquip;
	}

	public byte getPublicStatus() {
		return publicStatus;
	}

	public void setPublicStatus(byte publicStatus) {
		this.publicStatus = publicStatus;
	}

	public byte getClanStatus() {
		return clanStatus;
	}

	public void setClanStatus(byte clanStatus) {
		this.clanStatus = clanStatus;
	}

	public byte getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(byte tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	
	public IsaacKeyPair getIsaacKeyPair() {
		return isaacKeyPair;
	}

	public boolean isCantTrade() {
		return cantTrade;
	}

	public void setCantTrade(boolean canTrade) {
		this.cantTrade = canTrade;
	}

	/**
	 * Gets the pet.
	 * @return The pet.
	 */
	public Pet getPet() {
		return pet;
	}

	/**
	 * Sets the pet.
	 * @param pet The pet to set.
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}
	
	/**
	 * Gets the petManager.
	 * @return The petManager.
	 */
	public PetManager getPetManager() {
		return petManager;
	}

	/**
	 * Sets the petManager.
	 * @param petManager The petManager to set.
	 */
	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	public double getHpBoostMultiplier() {
		return hpBoostMultiplier;
	}

	public void setHpBoostMultiplier(double hpBoostMultiplier) {
		this.hpBoostMultiplier = hpBoostMultiplier;
	}

	public boolean hasLargeSceneView() {
		return largeSceneView;
	}

	public void setLargeSceneView(boolean largeSceneView) {
		this.largeSceneView = largeSceneView;
	}

	public void switchItemsLook() {
		getPlayerDetails().oldItemsLook = !getPlayerDetails().oldItemsLook;
		getPackets().sendItemsLook();
	}

	public void dialog(DialogueEventListener listener){ //temp
		getTemporaryAttributtes().put("dialogue_event", listener.begin());
	}
	
	public DialogueEventListener dialog(){
		DialogueEventListener listener = (DialogueEventListener) getTemporaryAttributtes().get("dialogue_event");
		return listener;
	}
	
	/**
	 * The current skill action that is going on for this player.
	 */
	private Optional<SkillActionTask> action = Optional.empty();
	
	/**
	 * The current skill this player is training.
	 * @return {@link #action}.
	 */
	public Optional<SkillActionTask> getSkillActionTask() {
		return action;
	}
	
	/**
	 * Sets the skill action.
	 * @param action the action to set this skill action to.
	 */
	public void setSkillAction(SkillActionTask action) {
		this.action = Optional.of(action);
	}
	
	/**
	 * Sets the skill action.
	 * @param action the action to set this skill action to.
	 */
	public void setSkillAction(Optional<SkillActionTask> action) {
		this.action = action;
	}
	
	/**
	 * Sends a delayed task for this player.
	 */
	public void task(int delay, Consumer<Player> action) {
		Player p = this;
		new Task(delay, false) {
			@Override
			protected void execute() {
				action.accept(p);
				cancel();
			}
		}.submit();
	}

	private final MutableNumber poisonImmunity = new MutableNumber(), skullTimer = new MutableNumber();
	
	/**
	 * Gets the poison immunity counter value.
	 * @return the poison immunity counter.
	 */
	public MutableNumber getPoisonImmunity() {
		return poisonImmunity;
	}
	
	/**
	 * Gets the skull timer counter value.
	 * @return the skull timer counter.
	 */
	public MutableNumber getSkullTimer() {
		return skullTimer;
	}

	/**
	 * Holds an optional wrapped inside the Antifire details.
	 */
	private Optional<AntifireDetails> antifireDetails = Optional.empty();
	
	/**
	 * Gets the anti-fire details instance for this player.
	 * @return the {@link AntifireDetails} as an optional.
	 */
	public Optional<AntifireDetails> getAntifireDetails() {
		return antifireDetails;
	}
	
	/**
	 * Sets a new anti-fire instance for this class.
	 * @param details the anti-fire instance to set.
	 */
	public void setAntifireDetail(Optional<AntifireDetails> details) {
		this.antifireDetails = details;
	}
	
	/**
	 * Sets the new anti-fire instance for this class directly.
	 * @param details the anti-fire instance to set.
	 */
	public void setAntifireDetail(AntifireDetails details) {
		setAntifireDetail(details == null ? Optional.empty() : Optional.of(details));
	}
	
	/**
	 * A collection of Stopwatches
	 */
	public HashMap<String, Stopwatch> watchMap = new HashMap<>();
	
	/**
	 * Gets the collection of Stopwatches
	 * @return stopwatch
	 */
	public HashMap<String, Stopwatch> getWatchMap() {
		return watchMap;
	}
	
	/**
	 * Populates the {@link #watchMap}
	 */
	{
		watchMap.put("FOOD", new Stopwatch());
		watchMap.put("DRINKS", new Stopwatch());
		watchMap.put("TOLERANCE", new Stopwatch());
		watchMap.put("EMOTE", new Stopwatch());
	}
	
	public PlayerDetails getPlayerDetails() {
		return details;
	}
	
	public void sendSound(int id) {
		getPackets().sendSound(id, 0, 1);
	}
	
	private Toolbelt toolbelt;
}