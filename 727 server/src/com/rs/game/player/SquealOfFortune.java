package com.rs.game.player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.utils.Utils;




public class SquealOfFortune {

    public static int SOF_INTERFACE_ID = 1253;
    public static int TAB_INTERFACE_ID = 0;
    private transient  Player player;
    private int prizeId;
    private boolean isDiscarded = false;

    private static List<Item> COMMON = Arrays.asList(new Item[] {
            //coins, bones, feathers, bait,
            new Item(995, 4_500), new Item(527, 10), new Item(314, 25), new Item(313, 25),

            //seeds: guam marrentill tarromin harralander
            new Item(3291, 3), new Item(5292, 3), new Item(5293, 3), new Item(5294, 3),

            //Leather:Cowhide snake hide, molten glass, gold ore, iron ore
            new Item(1740, 3), new Item(6288, 3), new Item(1776, 25), new Item(445, 3), new Item(441, 3),

            //Runes: Fire, water, air, earth, mind, body
            new Item(554, 80), new Item(555, 80), new Item(556, 80), new Item(557, 80), new Item(558, 80),
            new Item(559, 80)

            //Lamps
//            new Item(23717), new Item(23721), new Item(23725), new Item(23729),
//            new Item(23733), new Item(23737), new Item(23741), new Item(23745), new Item(23749), new Item(23753),
//            new Item(23757), new Item(23761), new Item(23765), new Item(23769), new Item(23774), new Item(23778),
//            new Item(23782), new Item(23786), new Item(23790), new Item(23794), new Item(23798), new Item(23802),
//            new Item(23806), new Item(23810), new Item(23814)
            });

    private static List<Item> UNCOMMON = Arrays.asList(new Item[] {
            //coins, wolf bones, big bones, baby dragon bones, snape grass
            new Item(995, 25_000), new Item(2860, 10), new Item(533, 10), new Item(535, 10),

            //seeds: Ranarr, Toadflax, irit, avantoe
            new Item(5295, 3), new Item(5296, 3), new Item(5297, 3), new Item(5298, 3),

            //Dragon Hide
            new Item(1754, 3), new Item(1752, 3), new Item(1750, 3),

            //Runes: Death, Nature, Chaos, astral, mud, lava
            new Item(560, 40), new Item(561, 40), new Item(562, 40), new Item(9075, 40),
            new Item(4698, 40), new Item(4699, 40),

            //Pure essence
            new Item(7937, 100)
            //Lamps
//            new Item(23718), new Item(23722), new Item(23726), new Item(23730),
//            new Item(23734),	new Item(23738), new Item(23742), new Item(23746), new Item(23750), new Item(23754),
//            new Item(23758), new Item(23762), new Item(23766), new Item(23770), new Item(23775), new Item(23779),
//            new Item(23783), new Item(23787), new Item(23791), new Item(23795), new Item(23799), new Item(23803),
//            new Item(23807),	new Item(23811), new Item(23815)
    });
    private static List<Item> RARE = Arrays.asList(new Item[] {
            //coins, living minerals, dragon bones, ourg bones, black dragonhide
            new Item(995, 75_000), new Item(15263, 25), new Item(537, 10), new Item(4835, 10), new Item(2492, 6),

            //Seeds: Kwuarm Snapdragon Cadantine lantadyme dwarfweed
            new Item(5299, 3), new Item(5300, 3), new Item(5301, 3), new Item(5302, 3), new Item(5303, 3),

            //Runes: Law, Cosmic, blood, soul
            new Item(563, 40), new Item(564, 40), new Item(565, 40), new Item(566, 40),

            //Pure ess
            new Item(7937, 250)
    });

    private static List<Item> SUPER_RARE = Arrays.asList(new Item[] {
            //coins, christmas cracker, pumpkin, easter egg, green h'ween, blue h'ween, red h'ween, santa hat
            new Item(995, 300_000), new Item(962), new Item(1960, 3), new Item(1961, 3), new Item(1053), new Item(1055), new Item(1057), new Item(1050),

            //frost bones, torstol seeds
            new Item(18831, 80), new Item(5304, 5)//,
    });

    public Item chooseSOFItem(List<Item> rewards) {
        Item reward = rewards.get((int) (Math.random() * rewards.size()));
        return reward;
    }

    private ItemsContainer<Item> items;
    private int[] superRare;
    private int[] rares;
    private int[] common;
    private int[] uncommon;

    public SquealOfFortune(Player player) {
        items = new ItemsContainer<Item>(13, false);
        setPlayer(player);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Starts the fortune. RARE - 0, 4, 8 COMMON - 1, 5, 7, 10, 12 UNCOMMON - 2,
     * 6, 9, 11
     */
    public void start() {
        items.clear();
        player.getPackets().sendConfigByFile(11026, player.getPlayerDetails().getSpins());
        player.getPackets().sendConfigByFile(11155, 1);
        player.getPackets().sendGlobalConfig(1928, 1);
        for (int slotLocation = 0; slotLocation < 14; slotLocation++) {
            if (slotLocation == 0) {
                addSOFItemToItemContainer(chooseSOFItem(SUPER_RARE));
            } else if (slotLocation == 8 || slotLocation == 4) {
                addSOFItemToItemContainer(chooseSOFItem(RARE));
            } else if (slotLocation == 2 || slotLocation == 6 || slotLocation == 9 || slotLocation == 11) {
                addSOFItemToItemContainer(chooseSOFItem(UNCOMMON));
            } else if (slotLocation == 1 || slotLocation == 5 || slotLocation == 7 || slotLocation == 10 || slotLocation == 12) {
                addSOFItemToItemContainer(chooseSOFItem(COMMON));
            } else {
                addSOFItemToItemContainer(chooseSOFItem(COMMON));
            }
        }
        setupSOFSpinInterface();
    }

    public boolean addSOFItemToItemContainer(Item item) {
        int slot = items.findAvailableSlot();
        if (slot == -1) {
            return false;
        }

        if(item.getDefinitions().isStackable() || item.getDefinitions().isNoted()){
            items.set(slot, item);
        }
        else {
            item.setAmount(1);
            items.set(slot, item);
        }
        return true;
    }

    public Item setupTotalLevelScaling(Item item) {
        if(item.getAmount() > 1) {
            int totalLevel = 0;
            int totalXP = 0;
            for(short level : player.getSkills().getLevels())
                totalLevel += level;
            for(double xp : player.getSkills().getXp())
                totalXP += (int)xp;

            int levelModifier = totalLevel/(300*Settings.XP_RATE);
            int xpModifier = totalXP/(1_000_000 * Settings.XP_RATE);
            int totalModifier = levelModifier + xpModifier;

            if(totalModifier <= 0)
                totalModifier = 1;
            item.setAmount(item.getAmount()*(totalModifier));
        }
        return item;
    }

    private void setupSOFSpinInterface() {
        player.getPackets().sendWindowsPane(SOF_INTERFACE_ID, 0);

        //Hide button top right
        player.getPackets().sendIComponentText(SOF_INTERFACE_ID, 107, "Exit");

        //set spin amount
        player.getPackets().sendIComponentText(SOF_INTERFACE_ID, 95, ""+ player.getPlayerDetails().getSpins());

        //Hide Buy Spins1
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 0, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 1, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 2, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 3, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 4, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 5, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 6, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 12, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 14, true);
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 15, true);

        rollChance();
    }

    /**
     * Contains the chances of receiving items.
     * Sends the Items in the reward container.
     */
    private void rollChance() {
        player.getPackets().sendItems(665, items);
        int random = Utils.random(100_000);

        if (random < 10) {// super rare
            superRare = new int[] { 0, 4, 8 };
            prizeId = superRare[(int) (Math.random() * superRare.length)];
        } else if (random < 3_000) {// rare
            rares = new int[] { 0, 4, 8 };
            prizeId = rares[(int) (Math.random() * rares.length)];
        } else if (random < 30_000) {// uncommon
            uncommon = new int[] { 2, 6, 9, 11 };
            prizeId = uncommon[(int) (Math.random() * uncommon.length)];
        } else {// common
            common = new int[] { 1, 3, 5, 7, 10, 12 };
            prizeId = common[(int) (Math.random() * common.length)];
        }
    }

    private void setVisibleExitButton(boolean isVisible) {
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 246, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 247, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 248, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 249, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 250, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 255, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 256, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 257, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 258, !isVisible);
//        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 260, !isVisible);
    }

    private void setupSOFRewardInterface() {
        player.getPackets().sendIComponentText(SOF_INTERFACE_ID, 161, "Spins remaining: "+ Integer.toString(player.getPlayerDetails().getSpins()));
        player.getPackets().sendIComponentText(SOF_INTERFACE_ID, 43, "For every 12 hours of play time, you get a spin ticket!");
        player.getPackets().sendHideIComponent(SOF_INTERFACE_ID, 305, true);//Hide Buy Spins text image
    }

    public void handleSOFButtons(Player player, int buttonId) {
        long currentTime = Utils.currentTimeMillis();

        if (buttonId == 92) {//red button next screen
            if (player.getPlayerDetails().getSpins() == 0) {
                items.clear();
                player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746	: 548, 0);
                player.getPackets().sendGlobalConfig(1790, 0);
                player.getPackets().sendRunScript(5906);
                isDiscarded = false;
                player.getPackets().sendGameMessage("You don't have any spins.");
                return;
            }  //damx
            if (player.getLockDelay() >= currentTime) {
                return;
            }
            player.lock(11);
            player.getPackets().sendGlobalConfig(1781, Utils.getRandom(13));
            player.getPackets().sendConfigByFile(10860, prizeId);
            player.getPackets().sendGlobalConfig(1790, 1);
            player.getPackets().sendConfigByFile(10861, prizeId);
            player.getPlayerDetails().setSpins(player.getPlayerDetails().getSpins() - 1);

            final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    setupSOFRewardInterface();
                    setVisibleExitButton(false);
                }
            }, 7, TimeUnit.SECONDS);
        }
        else if (buttonId == 105) {
            player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746	: 548, 0);
            items.clear();
        } else if (buttonId == 258) {//play again
            start();
        } else if (buttonId == 177) {//claim item
            if(!isDiscarded) {
                setupSOFRewardInterface();
                setVisibleExitButton(true);
                if (player.getInventory().hasFreeSlots())
                    player.getInventory().addItem(new Item(items.get(prizeId).getId(), items.get(prizeId).getAmount()));
                else {
                    Item[] itemsToBank = new Item[]{new Item(items.get(prizeId).getId())};
                    player.getBank().addItems(itemsToBank, true);
                }
            }
            player.getPackets().sendConfigByFile(10861, 0);
            player.getPackets().sendGlobalConfig(1790, 0);
            player.getPackets().sendHideIComponent(1253, 240, false);
            player.getPackets().sendHideIComponent(1253, 178, false);
            player.getPackets().sendHideIComponent(1253, 225, false);
            player.getPackets().sendRunScript(5906);
            items.clear();
            player.getPackets().sendIComponentText(SOF_INTERFACE_ID, 161, "Spins remaining: "+ Integer.toString(player.getPlayerDetails().getSpins()));
        } else if (buttonId == 243) {//done
            items.clear();
            player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, 0);
            player.getPackets().sendGlobalConfig(1790, 0);
            player.getPackets().sendRunScript(5906);
        } else if(buttonId == 224) {//discard
            isDiscarded = true;
            setupSOFRewardInterface();
            setVisibleExitButton(true);

            player.getPackets().sendConfigByFile(10861, 0);
            player.getPackets().sendGlobalConfig(1790, 0);
            player.getPackets().sendHideIComponent(1253, 240, false);
            player.getPackets().sendHideIComponent(1253, 178, false);
            player.getPackets().sendHideIComponent(1253, 225, false);
            player.getPackets().sendRunScript(5906);
            items.clear();
            player.getPackets().sendIComponentText(SOF_INTERFACE_ID, 162, "Spins remaining: "+ player.getPlayerDetails().getSpins());
        }
    }

}
