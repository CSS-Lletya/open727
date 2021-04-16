package com.rs.cache.loaders;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.cache.io.InputStream;

public class RenderAnimDefinitions {

	public int anInt949;
	public int anInt951 = -1;
	public int anInt952;
	public int anInt953 = -1;
	public int anInt954;
	public int anInt955;
	public int anInt956;
	public int anInt957;
	public int anInt958;
	public int[] anIntArray959 = null;
	public int anInt960;
	public int anInt961 = 0;
	public int anInt962;
	public int anInt963;
	public int anInt964;
	public int anInt965;
	public int anInt966;
	public int[] anIntArray967;
	public int anInt969;
	public int[] anIntArray971;
	public int anInt972;
	public int anInt973;
	public int anInt974;
	public int anInt975;
	public int anInt976;
	public int anInt977;
	public boolean aBoolean978;
	public int[][] anIntArrayArray979;
	public int anInt980;
	public int anInt981;
	public int anInt983;
	public int anInt985;
	public int anInt986;
	public int anInt987;
	public int anInt988;
	public int anInt989;
	public int anInt990;
	public int anInt992;
	public int anInt993;
	public int anInt994;

	private static final ConcurrentHashMap<Integer, RenderAnimDefinitions> renderAimDefs = new ConcurrentHashMap<Integer, RenderAnimDefinitions>();

	public static final RenderAnimDefinitions getRenderAnimDefinitions(int emoteId) {
		RenderAnimDefinitions defs = renderAimDefs.get(emoteId);
		if (defs != null)
			return defs;
		if (emoteId == -1)
			return null;
		byte[] data = Cache.STORE.getIndexes()[2].getFile(32, emoteId);
		defs = new RenderAnimDefinitions();
		if (data != null)
			defs.readValueLoop(new InputStream(data));
		renderAimDefs.put(emoteId, defs);
		return defs;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		// added opcode
		if (opcode == 54) {
			@SuppressWarnings("unused")
			int anInt1260 = stream.readUnsignedByte() << 6;
			@SuppressWarnings("unused")
			int anInt1227 = stream.readUnsignedByte() << 6;
		} else if (opcode == 55) {
			int[] anIntArray1246 = new int[12];
			int i_14_ = stream.readUnsignedByte();
			anIntArray1246[i_14_] = stream.readUnsignedShort();
		} else if (opcode == 56) {
			int[][] anIntArrayArray1217 = new int[12][];
			int i_12_ = stream.readUnsignedByte();
			anIntArrayArray1217[i_12_] = new int[3];
			for (int i_13_ = 0; i_13_ < 3; i_13_++)
				anIntArrayArray1217[i_12_][i_13_] = stream.readShort();
		} else if ((opcode ^ 0xffffffff) != -2) {
			if ((opcode ^ 0xffffffff) != -3) {
				if (opcode != 3) {
					if ((opcode ^ 0xffffffff) != -5) {
						if (opcode == 5)
							anInt977 = stream.readUnsignedShort();
						else if ((opcode ^ 0xffffffff) != -7) {
							if (opcode == 7)
								anInt960 = stream.readUnsignedShort();
							else if ((opcode ^ 0xffffffff) == -9)
								anInt985 = stream.readUnsignedShort();
							else if (opcode == 9)
								anInt957 = stream.readUnsignedShort();
							else if (opcode == 26) {
								anInt973 = (short) (4 * stream.readUnsignedByte());
								anInt975 = (short) (stream.readUnsignedByte() * 4);
							} else if ((opcode ^ 0xffffffff) == -28) {
								if (anIntArrayArray979 == null)
									anIntArrayArray979 = new int[12][];
								int i = stream.readUnsignedByte();
								anIntArrayArray979[i] = new int[6];
								for (int i_1_ = 0; (i_1_ ^ 0xffffffff) > -7; i_1_++)
									anIntArrayArray979[i][i_1_] = stream.readShort();
							} else if ((opcode ^ 0xffffffff) == -29) {
								anIntArray971 = new int[12];
								for (int i = 0; i < 12; i++) {
									anIntArray971[i] = stream.readUnsignedByte();
									if (anIntArray971[i] == 255)
										anIntArray971[i] = -1;
								}
							} else if (opcode != 29) {
								if (opcode != 30) {
									if ((opcode ^ 0xffffffff) != -32) {
										if (opcode != 32) {
											if ((opcode ^ 0xffffffff) != -34) {
												if (opcode != 34) {
													if (opcode != 35) {
														if ((opcode ^ 0xffffffff) != -37) {
															if ((opcode ^ 0xffffffff) != -38) {
																if (opcode != 38) {
																	if ((opcode ^ 0xffffffff) != -40) {
																		if ((opcode ^ 0xffffffff) != -41) {
																			if ((opcode ^ 0xffffffff) == -42)
																				anInt953 = stream.readUnsignedShort();
																			else if (opcode != 42) {
																				if ((opcode ^ 0xffffffff) == -44)
																					stream.readUnsignedShort();
																				else if ((opcode ^ 0xffffffff) != -45) {
																					if ((opcode ^ 0xffffffff) == -46)
																						anInt964 = stream
																								.readUnsignedShort();
																					else if ((opcode
																							^ 0xffffffff) != -47) {
																						if (opcode == 47)
																							anInt966 = stream
																									.readUnsignedShort();
																						else if (opcode == 48)
																							anInt989 = stream
																									.readUnsignedShort();
																						else if (opcode != 49) {
																							if ((opcode
																									^ 0xffffffff) != -51) {
																								if (opcode != 51) {
																									if (opcode == 52) {
																										int i = stream
																												.readUnsignedByte();
																										anIntArray959 = new int[i];
																										anIntArray967 = new int[i];
																										for (int i_2_ = 0; i_2_ < i; i_2_++) {
																											anIntArray967[i_2_] = stream
																													.readUnsignedShort();
																											int i_3_ = stream
																													.readUnsignedByte();
																											anIntArray959[i_2_] = i_3_;
																											anInt994 += i_3_;
																										}
																									} else if (opcode == 53)
																										aBoolean978 = false;
																								} else
																									anInt962 = stream
																											.readUnsignedShort();
																							} else
																								anInt990 = stream
																										.readUnsignedShort();
																						} else
																							anInt952 = stream
																									.readUnsignedShort();
																					} else
																						anInt983 = stream
																								.readUnsignedShort();
																				} else
																					anInt955 = stream
																							.readUnsignedShort();
																			} else
																				anInt981 = stream.readUnsignedShort();
																		} else
																			anInt949 = stream.readUnsignedShort();
																	} else
																		anInt954 = stream.readUnsignedShort();
																} else
																	anInt958 = (stream.readUnsignedShort());
															} else
																anInt951 = (stream.readUnsignedByte());
														} else
															anInt965 = (stream.readShort());
													} else
														anInt969 = (stream.readUnsignedShort());
												} else
													anInt993 = stream.readUnsignedByte();
											} else
												anInt956 = (stream.readShort());
										} else
											anInt961 = stream.readUnsignedShort();
									} else
										anInt988 = stream.readUnsignedByte();
								} else
									anInt980 = stream.readUnsignedShort();
							} else
								anInt992 = stream.readUnsignedByte();
						} else
							anInt976 = stream.readUnsignedShort();
					} else
						anInt986 = stream.readUnsignedShort();
				} else
					anInt987 = stream.readUnsignedShort();
			} else
				anInt974 = stream.readUnsignedShort();
		} else {
			anInt972 = stream.readUnsignedShort();
			anInt963 = stream.readUnsignedShort();
			if ((anInt972 ^ 0xffffffff) == -65536)
				anInt972 = -1;
			if ((anInt963 ^ 0xffffffff) == -65536)
				anInt963 = -1;
		}
	}

	public static void main(String[] args) throws IOException {
		Cache.init();
		// if(defs.anInt972 == 11789)
		// System.out.println(id);
		// int animId = 1467;

		/*
		 * for (int i = 0; i < 20000; i++) { RenderAnimDefinitions def = getRenderAnimDefinitions(i); if (def == null) { continue; } if (def.anInt963 == animId || def.anInt972 == animId) { System.out.println("Render anim: " + i); break; } else if (def.anIntArray967 != null) { for (int anim : def.anIntArray967) { if (anim == animId) { System.out.println("Render anim: " + i); break; } } } }
		 */
		for (int animId = 11623; animId <= 11623; animId++) {
			for (int i = 0; i < 10000; i++) {
				RenderAnimDefinitions defs = RenderAnimDefinitions.getRenderAnimDefinitions(i);

				if (defs.anInt972 == animId || defs.anInt963 == animId) {
					System.out.println(animId + ", " + i);
					// continue y;
				}
			}
		}

		/*
		 * File file = new File("./r2anims.txt"); BufferedWriter writer = new BufferedWriter(new FileWriter(file)); for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) { RenderAnimDefinitions defs = RenderAnimDefinitions .getRenderAnimDefinitions(NPCDefinitions .getNPCDefinitions(i).renderEmote); if (defs != null) { writer.write(i + ", run: " + defs.anInt972 + ", walk: " + defs.anInt963 + ", stand: " + Arrays.toString(defs.anIntArray967)); writer.newLine(); writer.flush(); } }
		 */
		/*
		 * for (int i = 0; i < 2263; i++) { RenderAnimDefinitions defs = RenderAnimDefinitions .getRenderAnimDefinitions(i); if (defs != null) { writer.write(i + ", run: " + defs.anInt972 + ", walk: " + defs.anInt963 + ", stand: " + Arrays.toString(defs.anIntArray967)); writer.newLine(); writer.flush(); } }
		 * 
		 * writer.close();
		 */
	}

	public RenderAnimDefinitions() {
		anInt957 = -1;
		anInt954 = -1;
		anInt960 = -1;
		anInt958 = -1;
		anInt965 = 0;
		anInt973 = 0;
		anInt949 = -1;
		anInt956 = 0;
		anInt972 = -1;
		anIntArray967 = null;
		anInt952 = -1;
		anInt983 = -1;
		anInt985 = -1;
		anInt962 = -1;
		anInt966 = -1;
		anInt977 = -1;
		anInt975 = 0;
		anInt976 = -1;
		anInt988 = 0;
		anInt981 = -1;
		anInt987 = -1;
		anInt980 = 0;
		anInt964 = -1;
		anInt963 = -1;
		anInt986 = -1;
		aBoolean978 = true;
		anInt992 = 0;
		anInt955 = -1;
		anInt989 = -1;
		anInt974 = -1;
		anInt969 = 0;
		anInt994 = 0;
		anInt990 = -1;
		anInt993 = 0;
	}

}
