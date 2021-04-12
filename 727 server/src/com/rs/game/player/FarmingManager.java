package com.rs.game.player;

import java.util.ArrayList;
import java.util.List;

import com.rs.utils.Utils;

public class FarmingManager {

	private static final int ALLOTMENT = 0, HERBS = 6, RAKE = 5342;

	private List<FarmingSpot> spots;
	private transient Player player;

	public FarmingManager() {
		spots = new ArrayList<FarmingSpot>();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		for (FarmingSpot spot : spots)
			spot.refresh();
	}

	private static enum ProductInfo {
		Potato(5318, 1, 1942, 0, ALLOTMENT), Onion(5319, 5, 1957, 1, ALLOTMENT), Cabbage(5324, 7, 1965, 2,
				ALLOTMENT), Tomato(5322, 12, 1982, 3, ALLOTMENT), Sweetcorn(5320, 20, 5986, 4,
						ALLOTMENT), Strawberry(5323, 31, 5504, 5, ALLOTMENT), Watermelon(5321, 47, 5982, 6, ALLOTMENT);

		private int seedId;
		private int level;
		private int productId;
		private int configIndex;
		private int type;

		private ProductInfo(int seedId, int level, int productId, int configIndex, int type) {
			this.setSeedId(seedId);
			this.setLevel(level);
			this.setProductId(productId);
			this.configIndex = configIndex;
			this.type = type;
		}

		@SuppressWarnings("unused")
		public int getSeedId() {
			return seedId;
		}

		public void setSeedId(int seedId) {
			this.seedId = seedId;
		}

		@SuppressWarnings("unused")
		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		@SuppressWarnings("unused")
		public int getProductId() {
			return productId;
		}

		public void setProductId(int productId) {
			this.productId = productId;
		}
	}

	private static enum SpotInfo {
		Falador_Herb_patch(8550, 780, HERBS), Falador_Allotment_North(8550, 708,
				ALLOTMENT), Falador_Allotment_South(8551, 709, ALLOTMENT);

		private int objectId;
		private int configFileId;
		private int type;

		private SpotInfo(int objectId, int configFileId, int type) {
			this.objectId = objectId;
			this.configFileId = configFileId;
			this.type = type;
		}
	}

	public boolean isFarming(int objectId, int optionId) {
		for (SpotInfo info : SpotInfo.values()) {
			if (info.objectId == objectId) {
				handleFarming(info, optionId);
				return true;
			}
		}
		return false;
	}

	private FarmingSpot getSpot(SpotInfo info) {
		for (FarmingSpot spot : spots)
			if (spot.spotInfo.equals(info))
				return spot;
		return null;
	}

	public void handleFarming(SpotInfo info, int optionId) {
		FarmingSpot spot = getSpot(info);
		if (spot == null) {
			switch (optionId) {
			case 0: // rake
				useRake(info); // creates spot
				break;
			case 1: // inspect
				sendNeedsWeeding();
				break;
			case 2: // guide
				sendGuide();
				break;
			}
		} else {
			switch (info.type) {
			case ALLOTMENT:

				break;
			}
		}
	}

	@SuppressWarnings("unused")
	private void useRake(FarmingSpot spot) {
		useRake(spot, null, false);
	}

	private void useRake(SpotInfo info) {
		useRake(null, info, true);
	}

	private void useRake(FarmingSpot spot, SpotInfo info, boolean create) {
		if (!player.getInventory().containsItem(RAKE, 1)) {
			player.getPackets().sendGameMessage("You'll need a rake to get rid of the weeds.");
			return;
		}
	}

	private void sendGuide() {
		player.getTemporaryAttributtes().put("skillMenu", 21);
		player.getPackets().sendConfig(965, 21);
		player.getInterfaceManager().sendInterface(499);
	}

	public void sendNeedsWeeding() {
		player.getPackets().sendGameMessage("The patch needs weeding.");
	}

	private class FarmingSpot {

		private SpotInfo spotInfo;
		private ProductInfo productInfo;
		private int stage;
		private long cycleTime;
		@SuppressWarnings("unused")
		private boolean watered;

		@SuppressWarnings("unused")
		public FarmingSpot(SpotInfo spotInfo, ProductInfo productInfo) {
			this.spotInfo = spotInfo;
			this.productInfo = productInfo;
			cycleTime = Utils.currentTimeMillis();
			stage = 1; // stage 0 is default null
			renewCycle();
		}

		public int getConfigValue() {
			if (productInfo != null) {
				if (productInfo.type == ALLOTMENT)
					return 6 + (productInfo.configIndex * 7);
			}
			return 0;
		}

		@SuppressWarnings("unused")
		public void process() {
			if (cycleTime == 0)
				return;
			while (cycleTime < Utils.currentTimeMillis()) {
				if (productInfo != null) {
					increaseStage();
					if (reachedMaxStage()) {
						cycleTime = 0;
						break;
					}
				} else {
					desecreaseStage();
					if (stage == 0) {
						remove();
						break;
					}
				}
				renewCycle();
			}

		}

		public void increaseStage() {
			stage++;
			refresh();
		}

		public void desecreaseStage() {
			stage--;
			refresh();
		}

		public void renewCycle() {
			cycleTime += 5000; // 5sec atm
		}

		public boolean reachedMaxStage() {
			return spotInfo.type == ALLOTMENT && stage == 4; // max stage ready
		}

		private void refresh() {
			player.getPackets().sendConfigByFile(spotInfo.configFileId, getConfigValue() + stage);
		}

		private void remove() {
			spots.remove(this);
		}

	}
}
