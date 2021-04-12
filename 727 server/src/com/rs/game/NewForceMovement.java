package com.rs.game;

public final class NewForceMovement extends ForceMovement {

	public NewForceMovement(WorldTile toFirstTile, int firstTileTicketDelay, WorldTile toSecondTile,
			int secondTileTicketDelay, int direction) {
		super(toFirstTile, firstTileTicketDelay, toSecondTile, secondTileTicketDelay, direction);
	}

	@Override
	public int getDirection() {
		return direction;
	}

}
