package com.rs.game.route.strategy;

import com.rs.game.Entity;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.player.Player;
import com.rs.game.route.RouteFinder;
import com.rs.game.route.RouteStrategy;
import com.rs.utils.Utils;

import npc.NPC;

public class RouteEvent {

	/**
	 * Object to which we are finding the route.
	 */
	private Object object;
	/**
	 * The event instance.
	 */
	private Runnable event;
	/**
	 * Whether we also run on alternative.
	 */
	private boolean alternative;
	/**
	 * Contains last route strategies.
	 */
	private RouteStrategy[] last;

	public RouteEvent(Object object, Runnable event) {
		this(object, event, false);
	}

	public RouteEvent(Object object, Runnable event, boolean alternative) {
		this.object = object;
		this.event = event;
		this.alternative = alternative;
	}

	public boolean instanceOfWorldObject() {
		return object instanceof WorldObject;
	}

	public boolean instanceOfEntity() {
		return object instanceof Entity;
	}

	public boolean instanceOfFloorItem() {
		return object instanceof FloorItem;
	}

	public Object getObject() {
		return object;
	}

	private void completePath(Player player) {
		if (this.instanceOfEntity())
			player.faceEntity((Entity) object);
		 else if (this.instanceOfWorldObject())
		 player.faceObject((WorldObject) object);
		else if (this.instanceOfFloorItem())
			player.setNextFaceWorldTile(((FloorItem) object).getTile());
		player.setNextFaceEntity(null);
		player.getAppearance().generateAppearenceData();
	}

	public boolean processEvent(final Player player) {
		if (player.isLocked())
			return true;

		if (!simpleCheck(player)) {
			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			completePath(player);
			return true;
		}
		// else if (EffectsManager.isBound(player)){
		// if ((instanceOfEntity() || instanceOfRSObject()) && Utils.getDistance(player, instanceOfEntity() ? (Entity) object : (RSObject) object) >= 2){
		// player.getPackets().sendGameMessage("You can't reach that.");
		// player.getPackets().sendResetMinimapFlag();
		// completePath(player);
		// return true;
		// }
		// else if (instanceOfFloorItem()){
		// if (Utils.getDistance(player, ((FloorItem) object).getTile()) == 1){
		// player.setNextAnimation(new Animation(832));
		// completePath(player);
		// event.run();
		// return true;
		// }
		// else if (Utils.getDistance(player, ((FloorItem) object).getTile()) >= 2){
		// player.getPackets().sendGameMessage("You can't reach that.");
		// player.getPackets().sendResetMinimapFlag();
		// completePath(player);
		// return true;
		// }
		// }
		// }
		RouteStrategy[] strategies = generateStrategies();
		if (strategies == null)
			return false;
		else if (last != null && match(strategies, last) && player.hasWalkSteps())
			return false;
		if (this.instanceOfEntity())
			player.setNextFaceEntity((Entity) object);
		else
			player.setNextFaceEntity(null);
		if (last != null && match(strategies, last) && !player.hasWalkSteps()) {
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getHeight(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1)
					continue;
				if (this.instanceOfEntity() && this.object instanceof NPC) {
					if (Utils.getDistance(((NPC) object), player) <= 3)
						((NPC) object).resetWalkSteps();
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0) || alternative) {
					if (alternative)
						player.getPackets().sendResetMinimapFlag();
					event.run();
					completePath(player);
					return true;
				}
			}

			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			completePath(player);
			return true;
		} else {
			last = strategies;

			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getHeight(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1)
					continue;
				if (this.instanceOfEntity() && this.object instanceof NPC) {
					if (Utils.getDistance(((NPC) object), player) <= 3)
						((NPC) object).resetWalkSteps();
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0)) {
					if (alternative)
						player.getPackets().sendResetMinimapFlag();
					event.run();
					completePath(player);
					return true;
				}
				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();

				WorldTile last = new WorldTile(bufferX[0], bufferY[0], player.getHeight());
				player.resetWalkSteps();
				player.getPackets().sendMinimapFlag(last.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()), last.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
				if (player.getFreezeDelay() > Utils.currentTimeMillis())
					return false;
				for (int step = steps - 1; step >= 0; step--) {
					if (!player.addWalkSteps(bufferX[step], bufferY[step], 25, true))
						break;
				}

				return false;
			}

			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			completePath(player);
			return true;
		}
	}

	private boolean simpleCheck(Player player) {
		if (object == null) {
			return false;
		} else if (object instanceof Entity) {
			return player.getHeight() == ((Entity) object).getHeight();
		} else if (object instanceof WorldObject) {
			return player.getHeight() == ((WorldObject) object).getHeight();
		} else if (object instanceof FloorItem) {
			return player.getHeight() == ((FloorItem) object).getTile().getHeight();
		}

		else {
			throw new RuntimeException(object + " is not instanceof any reachable entity.");
		}
	}

	private RouteStrategy[] generateStrategies() {
		if (object == null)
			return last;
		if (object instanceof Entity) {
			return new RouteStrategy[] { new EntityStrategy((Entity) object) };
		} else if (object instanceof WorldObject) {
			return new RouteStrategy[] { new ObjectStrategy((WorldObject) object) };
		} else if (object instanceof FloorItem) {
			FloorItem item = (FloorItem) object;
			return new RouteStrategy[] { new FixedTileStrategy(item.getTile().getX(), item.getTile().getY()), new FloorItemStrategy(item) };
		} else {
			throw new RuntimeException(object + " is not instanceof any reachable entity.");
		}
	}

	private boolean match(RouteStrategy[] a1, RouteStrategy[] a2) {
		if (a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++)
			if (!a1[i].equals(a2[i]))
				return false;
		return true;
	}

}
