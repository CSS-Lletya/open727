package com.rs.game.player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.rs.game.item.Item;

/**
 * Hold information for toolbelt
 */
public class Toolbelt {

    private final int[] TOOLBELT_ITEMS = new int[] { 946, 1735, 1595, 1755, 1599, 1597, 1733, 1592, 5523, 13431, 307, 309, 311, 301, 303, 1265, 2347, 1351, 590, -1, 8794, 4, 9434, 11065, 1785, 2976, 1594, 5343, 5325, 5341, 5329, 233, 952, 305, 975, 11323, 2575, 2576, 13153, 10150, 6739 };
    private static final short[] CONFIG_IDS = new short[] { 2438, 2439 };
    private boolean[] TOOLBELT_ITEMS_TAKEN;//Each index represents the same index as TOOLBELT_ITEMS
    private boolean[] tools;
    private transient Player player;

    public Toolbelt() {
        TOOLBELT_ITEMS_TAKEN = new boolean[TOOLBELT_ITEMS.length];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void init() {
        refreshConfigs();
    }

    public void setToolbeltItemsTaken(boolean[] toolbeltItemsTaken) {
        this.TOOLBELT_ITEMS_TAKEN = toolbeltItemsTaken;
    }

    private int getConfigIndex(int i) {
        return i / 20;
    }

    private void refreshConfigs() {
        int[] configValues = new int[CONFIG_IDS.length];
        for (int slot = 0; slot < TOOLBELT_ITEMS_TAKEN.length; slot++) {
            if (TOOLBELT_ITEMS_TAKEN[slot]) {
                int index = getConfigIndex(slot);
                configValues[index] |= 1 << (slot - (index * 20));
            }
        }
        for (int i = 0; i < CONFIG_IDS.length; i++) {
            player.getPackets().sendConfig(CONFIG_IDS[i], configValues[i]);
        }
    }

    private int getItemSlot(int itemId) {
        for (int slot = 0; slot < TOOLBELT_ITEMS.length; slot++)
            if (TOOLBELT_ITEMS[slot] == itemId)
                return slot;
        return -1;
    }

    /**
     * Returns a boolean array which is used to represent
     * each item as existant in the toolbelt. The position
     * of the item in the toolbelt is static.
     * @return boolean array
     */
    public boolean[] getItemsDeposited() {
        return TOOLBELT_ITEMS_TAKEN;
    }

    public void addItemForce(Item item) {
        int slot = getItemSlot(item.getId());
        if (slot == -1)
            return;
        if (TOOLBELT_ITEMS_TAKEN[slot])
            return;
        else {
            TOOLBELT_ITEMS_TAKEN[slot] = true;
            refreshConfigs();
        }
    }

    /**
     * Checks if a tool is in the toolbelt. It inserts the tool that is not inside and warns the player if it is inside the toolbelt
     * @param invSlot
     * @param item
     * @return
     */
    public boolean addItem(int invSlot, Item item) {
        int slot = getItemSlot(item.getId());
        if (slot == -1)
            return false;
        if (TOOLBELT_ITEMS_TAKEN[slot])
            player.getPackets().sendInventoryMessage(0, invSlot, "That is already on your tool belt.");
        else {
            TOOLBELT_ITEMS_TAKEN[slot] = true;
            player.getInventory().deleteItem(invSlot, item);
            refreshConfigs();
            player.getPackets().sendGameMessage("You add the " + item.getDefinitions().getName() + " to your tool belt.");
        }
        return true;
    }

    public boolean containsItem(int id) {
        int slot = getItemSlot(id);
        return slot == -1 ? false : TOOLBELT_ITEMS_TAKEN[slot];
    }
    
    public boolean hasTool(int toolId) {
        return tools[toolId] || player.getInventory().containsItem(toolId, 1) || player.getEquipment().getWeaponId() == toolId;
    }

    /**
     * Returns a list of acceptable toolbel items
     * @return List of Integer
     */
    public List<Integer> getToolbeltItems() {
        return Arrays.stream(TOOLBELT_ITEMS).boxed().collect(Collectors.toList());
    }
}