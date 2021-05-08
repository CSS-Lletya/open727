package com.rs.game.player;

import java.security.MessageDigest;

import com.rs.Settings;
import com.rs.cache.io.OutputStream;
import com.rs.game.Bar;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.utils.Utils;

public final class LocalPlayerUpdate {

	/**
	 * The maximum amount of local players being added per tick. This is to decrease time it takes to load crowded places (such as home).
	 */
	private static final byte MAX_PLAYER_ADD = 15;

	private transient Player player;

	private byte[] slotFlags;

	private transient Player[] localPlayers;
	private short[] localPlayersIndexes;
	private byte localPlayersIndexesCount;

	private short[] outPlayersIndexes;
	private short outPlayersIndexesCount;

	private short[] regionHashes;

	private byte[][] cachedAppearencesHashes;
	private byte totalRenderDataSentLength;

	/**
	 * The amount of local players added this tick.
	 */
	private int localAddedPlayers;

	public Player[] getLocalPlayers() {
		return localPlayers;
	}

	public boolean needAppearenceUpdate(byte index, byte[] hash) {
		if (totalRenderDataSentLength > ((Settings.PACKET_SIZE_LIMIT - 500) / 2) || hash == null)
			return false;
		return cachedAppearencesHashes[index] == null || !MessageDigest.isEqual(cachedAppearencesHashes[index], hash);
	}

	public LocalPlayerUpdate(Player player) {
		this.player = player;
		slotFlags = new byte[2048];
		localPlayers = new Player[2048];
		localPlayersIndexes = new short[Settings.PLAYERS_LIMIT];
		outPlayersIndexes = new short[2048];
		regionHashes = new short[2048];
		cachedAppearencesHashes = new byte[Settings.PLAYERS_LIMIT][];
	}

	public void init(OutputStream stream) {
		stream.initBitAccess();
		stream.writeBits(30, player.getTileHash());
		localPlayers[player.getIndex()] = player;
		localPlayersIndexes[localPlayersIndexesCount++] = (short) player.getIndex();
		for (int playerIndex = 1; playerIndex < 2048; playerIndex++) {
			if (playerIndex == player.getIndex())
				continue;
			Player player = World.getPlayers().get(playerIndex);
			stream.writeBits(18, regionHashes[playerIndex] = (short) (player == null ? 0 : player.getRegionHash()));
			outPlayersIndexes[outPlayersIndexesCount++] = (short) playerIndex;

		}
		stream.finishBitAccess();
	}

	private boolean needsRemove(Player p) {
		return (p.hasFinished() || !player.withinDistance(p, player.hasLargeSceneView() ? 126 : 14));
	}

	private boolean needsAdd(Player p) {
		return p != null && !p.hasFinished() && player.withinDistance(p, player.hasLargeSceneView() ? 126 : 14) && localAddedPlayers < MAX_PLAYER_ADD;
	}

	private void updateRegionHash(OutputStream stream, int lastRegionHash, int currentRegionHash) {
		int lastRegionX = lastRegionHash >> 8;
		int lastRegionY = 0xff & lastRegionHash;
		int lastPlane = lastRegionHash >> 16;
		int currentRegionX = currentRegionHash >> 8;
		int currentRegionY = 0xff & currentRegionHash;
		int currentPlane = currentRegionHash >> 16;
		int planeOffset = currentPlane - lastPlane;
		if (lastRegionX == currentRegionX && lastRegionY == currentRegionY) {
			stream.writeBits(2, 1);
			stream.writeBits(2, planeOffset);
		} else if (Math.abs(currentRegionX - lastRegionX) <= 1 && Math.abs(currentRegionY - lastRegionY) <= 1) {
			int opcode;
			int dx = currentRegionX - lastRegionX;
			int dy = currentRegionY - lastRegionY;
			if (dx == -1 && dy == -1)
				opcode = 0;
			else if (dx == 1 && dy == -1)
				opcode = 2;
			else if (dx == -1 && dy == 1)
				opcode = 5;
			else if (dx == 1 && dy == 1)
				opcode = 7;
			else if (dy == -1)
				opcode = 1;
			else if (dx == -1)
				opcode = 3;
			else if (dx == 1)
				opcode = 4;
			else
				opcode = 6;
			stream.writeBits(2, 2);
			stream.writeBits(5, (planeOffset << 3) + (opcode & 0x7));
		} else {
			int xOffset = currentRegionX - lastRegionX;
			int yOffset = currentRegionY - lastRegionY;
			stream.writeBits(2, 3);
			stream.writeBits(18, (yOffset & 0xff) + ((xOffset & 0xff) << 8) + (planeOffset << 16));
		}
	}

	private void processOutsidePlayers(OutputStream stream, OutputStream updateBlockData, boolean nsn2) {
		stream.initBitAccess();
		int skip = 0;
		localAddedPlayers = 0;
		for (int i = 0; i < outPlayersIndexesCount; i++) {
			int playerIndex = outPlayersIndexes[i];
			if (nsn2 ? (0x1 & slotFlags[playerIndex]) == 0 : (0x1 & slotFlags[playerIndex]) != 0)
				continue;
			if (skip > 0) {
				skip--;
				slotFlags[playerIndex] = (byte) (slotFlags[playerIndex] | 2);
				continue;
			}
			Player p = World.getPlayers().get(playerIndex);
			if (needsAdd(p)) {
				stream.writeBits(1, 1);
				stream.writeBits(2, 0); // request add
				int hash = p.getRegionHash();
				if (hash == regionHashes[playerIndex])
					stream.writeBits(1, 0);
				else {
					stream.writeBits(1, 1);
					updateRegionHash(stream, regionHashes[playerIndex], hash);
					regionHashes[playerIndex] = (short) hash;
				}
				stream.writeBits(6, p.getXInRegion());
				stream.writeBits(6, p.getYInRegion());
				boolean needAppearenceUpdate = needAppearenceUpdate((byte) p.getIndex(), p.getAppearance().getMD5AppeareanceDataHash());
				appendUpdateBlock(p, updateBlockData, needAppearenceUpdate, true);
				stream.writeBits(1, 1);
				localAddedPlayers++;
				localPlayers[p.getIndex()] = p;
				slotFlags[playerIndex] = (byte) (slotFlags[playerIndex] | 2);
			} else {
				int hash = p == null ? regionHashes[playerIndex] : p.getRegionHash();
				if (p != null && hash != regionHashes[playerIndex]) {
					stream.writeBits(1, 1);
					updateRegionHash(stream, regionHashes[playerIndex], hash);
					regionHashes[playerIndex] = (short) hash;
				} else {
					stream.writeBits(1, 0); // no update needed
					for (int i2 = i + 1; i2 < outPlayersIndexesCount; i2++) {
						int p2Index = outPlayersIndexes[i2];
						if (nsn2 ? (0x1 & slotFlags[p2Index]) == 0 : (0x1 & slotFlags[p2Index]) != 0)
							continue;
						Player p2 = World.getPlayers().get(p2Index);
						if (needsAdd(p2) || (p2 != null && p2.getRegionHash() != regionHashes[p2Index]))
							break;
						skip++;
					}
					skipPlayers(stream, skip);
					slotFlags[playerIndex] = (byte) (slotFlags[playerIndex] | 2);
				}
			}
		}
		stream.finishBitAccess();
	}

	private void processLocalPlayers(OutputStream stream, OutputStream updateBlockData, boolean nsn0) {
		stream.initBitAccess();
		int skip = 0;
		for (int i = 0; i < localPlayersIndexesCount; i++) {
			int playerIndex = localPlayersIndexes[i];
			if (nsn0 ? (0x1 & slotFlags[playerIndex]) != 0 : (0x1 & slotFlags[playerIndex]) == 0)
				continue;
			if (skip > 0) {
				skip--;
				slotFlags[playerIndex] = (byte) (slotFlags[playerIndex] | 2);
				continue;
			}
			Player p = localPlayers[playerIndex];
			if (needsRemove(p)) {
				stream.writeBits(1, 1); // needs update
				stream.writeBits(1, 0); // no masks update needeed
				stream.writeBits(2, 0); // request remove
				regionHashes[playerIndex] = (short) (p.getLastWorldTile() == null ? p.getRegionHash() : p.getLastWorldTile().getRegionHash());
				int hash = p.getRegionHash();
				if (hash == regionHashes[playerIndex])
					stream.writeBits(1, 0);
				else {
					stream.writeBits(1, 1);
					updateRegionHash(stream, regionHashes[playerIndex], hash);
					regionHashes[playerIndex] = (short) hash;
				}
				localPlayers[playerIndex] = null;
			} else {
				boolean needAppearenceUpdate = needAppearenceUpdate((byte) p.getIndex(), p.getAppearance().getMD5AppeareanceDataHash());
				boolean needUpdate = p.needMasksUpdate() || needAppearenceUpdate;
				if (needUpdate)
					appendUpdateBlock(p, updateBlockData, needAppearenceUpdate, false);
				if (p.hasTeleported()) {
					stream.writeBits(1, 1); // needs update
					stream.writeBits(1, needUpdate ? 1 : 0);
					stream.writeBits(2, 3);
					int xOffset = p.getX() - p.getLastWorldTile().getX();
					int yOffset = p.getY() - p.getLastWorldTile().getY();
					int planeOffset = p.getHeight() - p.getLastWorldTile().getHeight();
					if (Math.abs(p.getX() - p.getLastWorldTile().getX()) <= 14 // 14 for safe
							&& Math.abs(p.getY() - p.getLastWorldTile().getY()) <= 14) { // 14 for safe
						stream.writeBits(1, 0);
						if (xOffset < 0) // viewport used to be 15 now 16
							xOffset += 32;
						if (yOffset < 0)
							yOffset += 32;
						stream.writeBits(12, yOffset + (xOffset << 5) + (planeOffset << 10));
					} else {
						stream.writeBits(1, 1);
						stream.writeBits(30, (yOffset & 0x3fff) + ((xOffset & 0x3fff) << 14) + ((planeOffset & 0x3) << 28));
					}
				} else if (p.getNextWalkDirection() != -1) {
					int dx = Utils.DIRECTION_DELTA_X[p.getNextWalkDirection()];
					int dy = Utils.DIRECTION_DELTA_Y[p.getNextWalkDirection()];
					boolean running;
					int opcode;
					if (p.getNextRunDirection() != -1) {
						dx += Utils.DIRECTION_DELTA_X[p.getNextRunDirection()];
						dy += Utils.DIRECTION_DELTA_Y[p.getNextRunDirection()];
						opcode = Utils.getPlayerRunningDirection(dx, dy);
						if (opcode == -1) {
							running = false;
							opcode = Utils.getPlayerWalkingDirection(dx, dy);
						} else
							running = true;
					} else {
						running = false;
						opcode = Utils.getPlayerWalkingDirection(dx, dy);
					}
					stream.writeBits(1, 1);
					if ((dx == 0 && dy == 0)) {
						stream.writeBits(1, 1); // quick fix
						stream.writeBits(2, 0);
						if (!needUpdate) // hasnt been sent yet
							appendUpdateBlock(p, updateBlockData, needAppearenceUpdate, false);
					} else {
						stream.writeBits(1, needUpdate ? 1 : 0);
						stream.writeBits(2, running ? 2 : 1);
						stream.writeBits(running ? 4 : 3, opcode);
					}
				} else if (needUpdate) {
					stream.writeBits(1, 1); // needs update
					stream.writeBits(1, 1);
					stream.writeBits(2, 0);
				} else { // skip
					stream.writeBits(1, 0); // no update needed
					for (int i2 = i + 1; i2 < localPlayersIndexesCount; i2++) {
						int p2Index = localPlayersIndexes[i2];
						if (nsn0 ? (0x1 & slotFlags[p2Index]) != 0 : (0x1 & slotFlags[p2Index]) == 0)
							continue;
						Player p2 = localPlayers[p2Index];
						if (needsRemove(p2) || p2.hasTeleported() || p2.getNextWalkDirection() != -1 || (p2.needMasksUpdate() || needAppearenceUpdate((byte) p2.getIndex(), p2.getAppearance().getMD5AppeareanceDataHash())))
							break;
						skip++;
					}
					skipPlayers(stream, skip);
					slotFlags[playerIndex] = (byte) (slotFlags[playerIndex] | 2);
				}

			}
		}
		stream.finishBitAccess();
	}

	private void skipPlayers(OutputStream stream, int amount) {
		stream.writeBits(2, amount == 0 ? 0 : amount > 255 ? 3 : (amount > 31 ? 2 : 1));
		if (amount > 0)
			stream.writeBits(amount > 255 ? 11 : (amount > 31 ? 8 : 5), amount);
	}

	private void appendUpdateBlock(Player p, OutputStream data, boolean needAppearenceUpdate, boolean added) {
		int maskData = 0;

		if (p.getNextGraphics2() != null)
			maskData |= 0x200;
		if (added || (p.getNextFaceWorldTile() != null && p.getNextRunDirection() == -1 && p.getNextWalkDirection() == -1))
			maskData |= 0x20;
		if (p.getNextForceTalk() != null)
			maskData |= 0x4000;
		if (p.getNextGraphics3() != null)
			maskData |= 0x400000;
		if (!p.getNextHits().isEmpty())
			maskData |= 0x40;
		if (needAppearenceUpdate)
			maskData |= 0x1;
		if (p.getNextAnimation() != null)
			maskData |= 0x10;
		if (p.getNextFaceEntity() != -2 || (added && p.getLastFaceEntity() != -1))
			maskData |= 0x2;
		if (p.getTemporaryMoveType() != -1)
			maskData |= 0x1000;
		if (added || p.isUpdateMovementType())
			maskData |= 0x4;
		if (p.getNextForceMovement() != null)
			maskData |= 0x800;
		if (p.getNextGraphics1() != null)
			maskData |= 0x80;

		if (maskData >= 256)
			maskData |= 0x8;
		if (maskData >= 65536)
			maskData |= 0x100;
		data.writeByte(maskData);
		if (maskData >= 256)
			data.writeByte(maskData >> 8);
		if (maskData >= 65536)
			data.writeByte(maskData >> 16);

		if (p.getNextGraphics2() != null)
			applyGraphicsMask2(p, data);
		if (added || (p.getNextFaceWorldTile() != null && p.getNextRunDirection() == -1 && p.getNextWalkDirection() == -1))
			applyFaceDirectionMask(p, data);
		if (p.getNextForceTalk() != null)
			applyForceTalkMask(p, data);
		if (p.getNextGraphics3() != null)
			applyGraphicsMask3(p, data);
		if (!p.getNextHits().isEmpty())
			applyHitsMask(p, data);
		if (needAppearenceUpdate)
			applyAppearanceMask(p, data);
		if (p.getNextAnimation() != null)
			applyAnimationMask(p, data);
		if (p.getNextFaceEntity() != -2 || (added && p.getLastFaceEntity() != -1))
			applyFaceEntityMask(p, data);
		if (p.getTemporaryMoveType() != -1)
			applyTemporaryMoveTypeMask(p, data);
		if (added || p.isUpdateMovementType())
			applyMoveTypeMask(p, data);
		if (p.getNextForceMovement() != null)
			applyForceMovementMask(p, data);
		if (p.getNextGraphics1() != null)
			applyGraphicsMask1(p, data);

		// if (p.getNextGraphics4() != null)
		// maskData |= 0x800000;
		//



		// if (p.getNextGraphics4() != null)
		// applyGraphicsMask4(p, data);
	}

	private void applyForceTalkMask(Player p, OutputStream data) {
		data.writeString(p.getNextForceTalk().getText());
	}
	
	private void applyHitsMask(Player p, OutputStream data) {
		int count = p.getNextHits().size();
		data.write128Byte(count);
		 if (count > 0) {
	            for (Hit hit : p.getNextHits()) {
	                if (hit == null) {
	                    continue;
	                }
	                
	                //start old matrix method
	                boolean interactingWith = hit.interactingWith(player, p);
					if (hit.missed() && !interactingWith){
						data.writeSmart(32766);
						data.writeByteC(hit.getDamage()); //added line - new to 727
					}
					else {
						if (hit.getSoaking() != null) {
							data.writeSmart(32767);
							data.writeSmart(hit.getMark(player, p));
							data.writeSmart(hit.getDamage());
							data.writeSmart(hit.getSoaking().getMark(player, p));
							data.writeSmart(hit.getSoaking().getDamage());
						} else {
							data.writeSmart(hit.getMark(player, p));
							data.writeSmart(hit.getDamage());
						}
					}
					//end old matrix method
	               /* 
	                int hitType = hit.getMark(player, p); // new method @Jordan
	                data.writeSmart(hitType);
	                if (hitType == 32767) {
	                    data.writeSmart(hitType);
	                    data.writeSmart(hit.getDamage());
	                    data.writeSmart(hit.getSoaking().getMark(player, p));
	                    data.writeSmart(hit.getSoaking().getDamage());
	                } else if (32766 != hitType) {
	                    data.writeSmart(hit.getDamage());
	                } else {
	                    data.writeByteC(hit.getDamage());
	                }*/
	                data.writeSmart(hit.getDelay());
	            }
	        }
		data.writeByte(count);
		if (count > 0) {
			for (Bar bar : p.getNextBars()) {
				if (bar == null) {
					continue;
				}
			
				data.writeSmart(bar.getBarType());
				int maxPercentage = bar.getMaxPercentage();
				int percentage = bar.getPercentage(p);
				data.writeSmart(maxPercentage != percentage ? 1 : 0);
				data.writeSmart(0);
				data.write128Byte(maxPercentage);
				if (maxPercentage != percentage) {
					data.writeByte128(bar.getPercentage(p));
				}
				
			}
		}
	}

	private void applyFaceEntityMask(Player p, OutputStream data) {
		data.writeShort128(p.getNextFaceEntity() == -2 ? p.getLastFaceEntity() : p.getNextFaceEntity());
	}

	private void applyFaceDirectionMask(Player p, OutputStream data) {
		data.writeShort128(p.getDirection()); // also works as face tile as dir
												// calced on setnextfacetile
	}

	private void applyMoveTypeMask(Player p, OutputStream data) {
		data.write128Byte(p.getRun() ? 2 : 1);
	}

	private void applyTemporaryMoveTypeMask(Player p, OutputStream data) {
		data.writeByteC(p.getTemporaryMoveType());
	}

	private void applyGraphicsMask1(Player p, OutputStream data) {
		data.writeShortLE(p.getNextGraphics1().getId());
		data.writeIntLE(p.getNextGraphics1().getSettingsHash());
		data.writeByte128(p.getNextGraphics1().getSettings2Hash());
	}

	private void applyGraphicsMask2(Player p, OutputStream data) {
		data.writeShort(p.getNextGraphics2().getId());
		data.writeIntV2(p.getNextGraphics2().getSettingsHash());
		data.write128Byte(p.getNextGraphics2().getSettings2Hash());
	}

	private void applyGraphicsMask3(Player p, OutputStream data) {
		data.writeShortLE128(p.getNextGraphics3().getId());
		data.writeIntV2(p.getNextGraphics3().getSettingsHash());
		data.writeByte128(p.getNextGraphics3().getSettings2Hash());
	}

	@SuppressWarnings("unused")
	private void applyGraphicsMask4(Player p, OutputStream data) {
		data.writeShort128(p.getNextGraphics4().getId());
		data.writeIntV1(p.getNextGraphics4().getSettingsHash());
		data.writeByte128(p.getNextGraphics4().getSettings2Hash());
	}

	private void applyAnimationMask(Player p, OutputStream data) {
		for (int id : p.getNextAnimation().getIds())
			data.writeBigSmart(id);
		data.writeByte(p.getNextAnimation().getSpeed());
	}

	private void applyAppearanceMask(Player p, OutputStream data) {
		byte[] renderData = p.getAppearance().getAppeareanceBlocks();
		totalRenderDataSentLength += renderData.length;
		cachedAppearencesHashes[p.getIndex()] = p.getAppearance().getMD5AppeareanceDataHash();
		data.writeByteC(renderData.length);
		data.writeBytes(renderData);

	}

	private void applyForceMovementMask(Player p, OutputStream data) {
		data.writeByteC(p.getNextForceMovement().getToFirstTile().getX() - p.getX());
		data.write128Byte(p.getNextForceMovement().getToFirstTile().getY() - p.getY());
		data.writeByte128(p.getNextForceMovement().getToSecondTile() == null ? 0 : p.getNextForceMovement().getToSecondTile().getX() - p.getX());
		data.writeByteC(p.getNextForceMovement().getToSecondTile() == null ? 0 : p.getNextForceMovement().getToSecondTile().getY() - p.getY());
		data.writeShortLE128(p.getNextForceMovement().getFirstTileTicketDelay() * 30);
		data.writeShortLE(p.getNextForceMovement().getToSecondTile() == null ? 0 : p.getNextForceMovement().getSecondTileTicketDelay() * 30);
		data.writeShort128(p.getNextForceMovement().getDirection());
	}

	public OutputStream createPacketAndProcess() {
		OutputStream stream = new OutputStream();
		OutputStream updateBlockData = new OutputStream();
		stream.writePacketVarShort(player, 43);
		processLocalPlayers(stream, updateBlockData, true);
		processLocalPlayers(stream, updateBlockData, false);
		processOutsidePlayers(stream, updateBlockData, true);
		processOutsidePlayers(stream, updateBlockData, false);
		stream.writeBytes(updateBlockData.getBuffer(), 0, updateBlockData.getOffset());
		stream.endPacketVarShort();
		totalRenderDataSentLength = 0;
		localPlayersIndexesCount = 0;
		outPlayersIndexesCount = 0;
		for (int playerIndex = 1; playerIndex < 2048; playerIndex++) {
			slotFlags[playerIndex] >>= 1;
			Player player = localPlayers[playerIndex];
			if (player == null)
				outPlayersIndexes[outPlayersIndexesCount++] = (short) playerIndex;
			else
				localPlayersIndexes[localPlayersIndexesCount++] = (short) playerIndex;
		}
		return stream;
	}

}