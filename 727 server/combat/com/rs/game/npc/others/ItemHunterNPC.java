package com.rs.game.npc.others;

import java.util.List;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.OwnedObjectManager.ConvertEvent;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

import skills.hunter.Hunter;
import skills.hunter.BoxAction.HunterNPC;
import skills.hunter.Hunter.DynamicFormula;

@SuppressWarnings("serial")
public class ItemHunterNPC extends NPC {

	public ItemHunterNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		List<WorldObject> objects = World.getRegion(getRegionId()).getSpawnedObjects();
		if (objects != null) {
			final HunterNPC info = HunterNPC.forId(getId());
			int objectId = info.getEquipment().getObjectId();
			for (final WorldObject object : objects) {
				if (object.getId() == objectId) {
					if (OwnedObjectManager.convertIntoObject(object,
							new WorldObject(info.getSuccessfulTransformObjectId(), 10, 0, this.getX(), this.getY(),
									this.getPlane()),
							new ConvertEvent() {
								@Override
								public boolean canConvert(Player player) {
									if (player == null || player.getLockDelay() > Utils.currentTimeMillis())
										return false;
									if (Hunter.isSuccessful(player, info.getLevel(), new DynamicFormula() {
										@Override
										public int getExtraProbablity(Player player) {
											// bait here
											return 1;
										}
									})) {
										failedAttempt(object, info);
										return false;
									} else {
										setNextAnimation(info.getSuccessCatchAnim());
										return true;
									}
								}
							})) {
						setRespawnTask(); // auto finishes
					}
				}
			}
		}
	}

	private void failedAttempt(WorldObject object, HunterNPC info) {
		setNextAnimation(info.getFailCatchAnim());
		if (OwnedObjectManager.convertIntoObject(object,
				new WorldObject(info.getFailedTransformObjectId(), 10, 0, this.getX(), this.getY(), this.getPlane()),
				new ConvertEvent() {
					@Override
					public boolean canConvert(Player player) {
						// if(larupia)
						// blablabla
						return true;
					}
				})) {
		}
	}
}