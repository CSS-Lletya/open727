package com.rs.game;

import com.rs.game.player.Player;

public class HintIconsManager {

	private transient Player player;
	private HintIcon[] loadedIcons;

	public HintIconsManager(Player p) {
		this.player = p;
		loadedIcons = new HintIcon[7];
	}

	public int addHintIcon(Entity target, int arrowType, int modelId, boolean saveIcon) {
		int index = saveIcon ? getFreeIndex() : 7;
		if (index != -1) {
			HintIcon icon = new HintIcon(target.getIndex(), target instanceof Player ? 10 : 1, arrowType, modelId,
					index);
			player.getPackets().sendHintIcon(icon);
			if (saveIcon)
				loadedIcons[index] = icon;
		}
		return index;
	}

	/*
	 * dirs 2 - center 3 - west 4 - east 5 - south 6 - north
	 */
	public int addHintIcon(int coordX, int coordY, int height, int distanceFromFloor, int direction, int arrowType,
			int modelId, boolean saveIcon) {
		int index = saveIcon ? getFreeIndex() : 7;
		if (index != -1) {
			if (direction < 2 || direction > 6)
				direction = 2;
			HintIcon icon = new HintIcon(coordX, coordY, height, distanceFromFloor, direction, arrowType, modelId,
					index);
			player.getPackets().sendHintIcon(icon);
			if (saveIcon)
				loadedIcons[index] = icon;
		}
		return index;
	}

	public int addHintIcon(int modelId, boolean saveIcon) {
		int index = saveIcon ? getFreeIndex() : 7;
		if (index != -1) {
			HintIcon icon = new HintIcon(8, modelId, index);
			player.getPackets().sendHintIcon(icon);
			if (saveIcon)
				loadedIcons[index] = icon;
		}
		return index;
	}

	public void removeUnsavedHintIcon() {
		player.getPackets().sendHintIcon(new HintIcon());
	}

	public boolean reloadHintIcon(int index) {
		if (index >= loadedIcons.length)
			return false;
		if (loadedIcons[index] == null)
			return false;
		player.getPackets().sendHintIcon(loadedIcons[index]);
		return true;
	}

	public boolean removeHintIcon(int index) {
		if (index == 7) {
			removeUnsavedHintIcon();
			return true;
		}
		if (index >= loadedIcons.length)
			return false;
		if (loadedIcons[index] == null)
			return false;
		loadedIcons[index].setTargetType(0);
		player.getPackets().sendHintIcon(loadedIcons[index]);
		loadedIcons[index] = null;
		return true;
	}

	public void removeAll() {
		for (int index = 0; index < loadedIcons.length; index++) {
			if (loadedIcons[index] != null) {
				loadedIcons[index].setTargetType(0);
				player.getPackets().sendHintIcon(loadedIcons[index]);
				loadedIcons[index] = null;
			}
		}
	}

	public boolean isEmpty() {
		for (int index = 0; index < loadedIcons.length; index++)
			if (loadedIcons[index] != null)
				return false;
		return true;
	}

	private int getFreeIndex() {
		for (int index = 0; index < loadedIcons.length; index++)
			if (loadedIcons[index] == null)
				return index;
		return -1;
	}
}