package com.rs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import skills.Skills;

public final class Utils {

	private static final Object ALGORITHM_LOCK = new Object();
	private static final Random RANDOM = new Random();

	private static long timeCorrection;
	private static long lastTimeUpdate;

	public static synchronized long currentTimeMillis() {
		long l = System.currentTimeMillis();
		if (l < lastTimeUpdate)
			timeCorrection += lastTimeUpdate - l;
		lastTimeUpdate = l;
		return l + timeCorrection;
	}

	public static byte[] cryptRSA(byte[] data, BigInteger exponent, BigInteger modulus) {
		return new BigInteger(data).modPow(exponent, modulus).toByteArray();
	}

	/**
	 * Gets all of the classes in a directory
	 * @param directory The directory to iterate through
	 * @return The list of classes
	 */
	public static ObjectList<Object> getClassesInDirectory(String directory) {
		ObjectList<Object> classes = new ObjectArrayList<>();
		for(File file : new File("./bin/" + directory.replace(".", "/")).listFiles()) {
			if(file.getName().contains("$")) {
				continue;
			}
			try {
				Object objectEvent = (Class.forName(directory + "." + file.getName().replace(".class", "")).newInstance());
				classes.add(objectEvent);
			} catch(InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				//e.printStackTrace();
			}
		}
		return classes;
	}
	
	/**
	 * Gets all of the sub directories of a folder
	 */
	public static ObjectList<String> getSubDirectories(Class<?> clazz) {
		String directory = "./bin/" + clazz.getPackage().getName().replace(".", "/");
		File file = new File(directory);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		return new ObjectArrayList<>(directories);
	}
	
	public static final byte[] encryptUsingMD5(byte[] buffer) {
		// prevents concurrency problems with the algorithm
		synchronized (ALGORITHM_LOCK) {
			try {
				MessageDigest algorithm = MessageDigest.getInstance("MD5");
				algorithm.update(buffer);
				byte[] digest = algorithm.digest();
				algorithm.reset();
				return digest;
			} catch (Throwable e) {
				Logger.handle(e);
			}
			return null;
		}
	}

	public static boolean inCircle(WorldTile location, WorldTile center, int radius) {
		return getDistance(center, location) < radius;
	}

	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile().replaceAll("%20", " ")));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				try {
					classes.add(Class
							.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
				} catch (Throwable e) {

				}
			}
		}
		return classes;
	}

	public static final int getDistance(WorldTile t1, WorldTile t2) {
		return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
	}

	public static final int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };

	public static int getNpcMoveDirection(int dd) {
		if (dd < 0)
			return -1;
		return getNpcMoveDirection(DIRECTION_DELTA_X[dd], DIRECTION_DELTA_Y[dd]);
	}

	public static int getNpcMoveDirection(int dx, int dy) {
		if (dx == 0 && dy > 0)
			return 0;
		if (dx > 0 && dy > 0)
			return 1;
		if (dx > 0 && dy == 0)
			return 2;
		if (dx > 0 && dy < 0)
			return 3;
		if (dx == 0 && dy < 0)
			return 4;
		if (dx < 0 && dy < 0)
			return 5;
		if (dx < 0 && dy == 0)
			return 6;
		if (dx < 0 && dy > 0)
			return 7;
		return -1;
	}

	public static final int[][] getCoordOffsetsNear(int size) {
		int[] xs = new int[4 + (4 * size)];
		int[] xy = new int[xs.length];
		xs[0] = -size;
		xy[0] = 1;
		xs[1] = 1;
		xy[1] = 1;
		xs[2] = -size;
		xy[2] = -size;
		xs[3] = 1;
		xy[2] = -size;
		for (int fakeSize = size; fakeSize > 0; fakeSize--) {
			xs[(4 + ((size - fakeSize) * 4))] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4))] = 1;
			xs[(4 + ((size - fakeSize) * 4)) + 1] = -size;
			xy[(4 + ((size - fakeSize) * 4)) + 1] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 2] = 1;
			xy[(4 + ((size - fakeSize) * 4)) + 2] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 3] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4)) + 3] = -size;
		}
		return new int[][] { xs, xy };
	}

	public static final int getFaceDirection(int xOffset, int yOffset) {
		return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
	}

	public static final int getMoveDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0)
				return 5;
			else if (yOffset > 0)
				return 0;
			else
				return 3;
		} else if (xOffset > 0) {
			if (yOffset < 0)
				return 7;
			else if (yOffset > 0)
				return 2;
			else
				return 4;
		} else {
			if (yOffset < 0)
				return 6;
			else if (yOffset > 0)
				return 1;
			else
				return -1;
		}
	}

	public static final int getGraphicDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[21].getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndexes()[21].getValidFilesCount(lastArchiveId);
	}

	public static final int getAnimationDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[20].getLastArchiveId();
		return lastArchiveId * 128 + Cache.STORE.getIndexes()[20].getValidFilesCount(lastArchiveId);
	}

	public static final int getConfigDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[22].getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndexes()[22].getValidFilesCount(lastArchiveId);
	}

	public static final int getObjectDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[16].getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndexes()[16].getValidFilesCount(lastArchiveId);
	}

	public static final int getNPCDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[18].getLastArchiveId();
		return lastArchiveId * 128 + Cache.STORE.getIndexes()[18].getValidFilesCount(lastArchiveId);
	}
	// 22314

	public static final int getItemDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[19].getLastArchiveId();
		return (lastArchiveId * 256 + Cache.STORE.getIndexes()[19].getValidFilesCount(lastArchiveId));
	}

	public static boolean itemExists(int id) {
		int size = getItemDefinitionsSize();
		if (id >= size){ // setted because of custom items
			return false;
		}
		return Cache.STORE.getIndexes()[19].fileExists(id >>> 8, 0xff & id);
	}

	public static final int getInterfaceDefinitionsSize() {
		return Cache.STORE.getIndexes()[3].getLastArchiveId() + 1;
	}

	public static final int getInterfaceDefinitionsComponentsSize(int interfaceId) {
		return Cache.STORE.getIndexes()[3].getLastFileId(interfaceId) + 1;
	}

	public static String[] ALPHABET = { "a", "b", "c", "d", "e",
			"f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
			"s", "t", "u", "v", "w", "x", "y", "z" };

	public static String getCharacterFromByte(int byte0){
		if (byte0 >= 97 && byte0 <= 122)
			return ALPHABET[byte0 - 97];
		else if (byte0 >= 48 && byte0 <= 57)
			return String.valueOf(byte0 - 48);
		return "a";
				
	}
	
	public static int[] KEY_PRESS_ORDINALITY = { 12288, 17408, 16896, 12800, 
			8704, 13056, 13312, 13568, 9984, 13824, 14080, 14336, 17920, 17664, 
			10240, 10496, 8192, 8960, 12544, 9216, 9728, 17152, 8448, 16640, 9472, 
			16384, 6400, 4096, 4352, 4608, 4864, 5120, 5376, 5632, 5888, 6144 };
	
	public static String getKeyPressedFromListenerByte(int short0){
		switch (short0){ //shortcutting
		case 3328:
			return "esc";
		case 21248:
			return "space_bar";
		}
		int keyIndex = -1;
		for (int index = 0; index < KEY_PRESS_ORDINALITY.length; index++){
			if (KEY_PRESS_ORDINALITY[index] == short0){
				keyIndex = index;
				break;
			}
		}
		if (keyIndex >= 0 && keyIndex <= ALPHABET.length - 1)
			return ALPHABET[keyIndex];
		else if (keyIndex >= ALPHABET.length && keyIndex <= ALPHABET.length + 9)
			return String.valueOf(keyIndex - ALPHABET.length);
		return "-a";
	}
	
	public static String formatPlayerNameForProtocol(String name) {
		if (name == null)
			return "";
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}

	public static String formatPlayerNameForDisplay(String name) {
		if (name == null)
			return "";
		name = name.replaceAll("_", " ");
		name = name.toLowerCase();
		StringBuilder newName = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				newName.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				newName.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return newName.toString();
	}

	public static final int getRandom(int maxValue) {
		return (int) (Math.random() * (maxValue + 1));
	}

	public static final int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}

	public static final double random(double min, double max) {
		final double n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random((int) n));
	}

	public static final int next(int max, int min) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static final double getRandomDouble(double maxValue) {
		return (Math.random() * (maxValue + 1));

	}

	public static final int random(int maxValue) {
		if (maxValue <= 0)
			return 0;
		return RANDOM.nextInt(maxValue);
	}

	public static final String longToString(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return null;
		if (l % 37L == 0L)
			return null;
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9' };

	public static boolean invalidAccountName(String name) {
		return name.length() < 2 || name.length() > 12 || name.startsWith("_") || name.endsWith("_")
				|| name.contains("__") || containsInvalidCharacter(name);
	}

	public static boolean invalidAuthId(String auth) {
		return auth.length() != 10 || auth.contains("_") || containsInvalidCharacter(auth);
	}

	public static boolean containsInvalidCharacter(char c) {
		for (char vc : VALID_CHARS) {
			if (vc == c)
				return false;
		}
		return true;
	}

	public static boolean containsInvalidCharacter(String name) {
		for (char c : name.toCharArray()) {
			if (containsInvalidCharacter(c))
				return true;
		}
		return false;
	}

	public static final long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	public static final int packGJString2(int position, byte[] buffer, String String) {
		int length = String.length();
		int offset = position;
		for (int index = 0; length > index; index++) {
			int character = String.charAt(index);
			if (character > 127) {
				if (character > 2047) {
					buffer[offset++] = (byte) ((character | 919275) >> 12);
					buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
					buffer[offset++] = (byte) (128 | (character & 63));
				} else {
					buffer[offset++] = (byte) ((character | 12309) >> 6);
					buffer[offset++] = (byte) (128 | (character & 63));
				}
			} else
				buffer[offset++] = (byte) character;
		}
		return offset - position;
	}

	public static final int calculateGJString2Length(String String) {
		int length = String.length();
		int gjStringLength = 0;
		for (int index = 0; length > index; index++) {
			char c = String.charAt(index);
			if (c > '\u007f') {
				if (c <= '\u07ff')
					gjStringLength += 2;
				else
					gjStringLength += 3;
			} else
				gjStringLength++;
		}
		return gjStringLength;
	}

	public static final int getNameHash(String name) {
		name = name.toLowerCase();
		int hash = 0;
		for (int index = 0; index < name.length(); index++)
			hash = method1258(name.charAt(index)) + ((hash << 5) - hash);
		return hash;
	}

	public static final byte method1258(char c) {
		byte charByte;
		if (c > 0 && c < '\200' || c >= '\240' && c <= '\377') {
			charByte = (byte) c;
		} else if (c != '\u20AC') {
			if (c != '\u201A') {
				if (c != '\u0192') {
					if (c == '\u201E') {
						charByte = -124;
					} else if (c != '\u2026') {
						if (c != '\u2020') {
							if (c == '\u2021') {
								charByte = -121;
							} else if (c == '\u02C6') {
								charByte = -120;
							} else if (c == '\u2030') {
								charByte = -119;
							} else if (c == '\u0160') {
								charByte = -118;
							} else if (c == '\u2039') {
								charByte = -117;
							} else if (c == '\u0152') {
								charByte = -116;
							} else if (c != '\u017D') {
								if (c == '\u2018') {
									charByte = -111;
								} else if (c != '\u2019') {
									if (c != '\u201C') {
										if (c == '\u201D') {
											charByte = -108;
										} else if (c != '\u2022') {
											if (c == '\u2013') {
												charByte = -106;
											} else if (c == '\u2014') {
												charByte = -105;
											} else if (c == '\u02DC') {
												charByte = -104;
											} else if (c == '\u2122') {
												charByte = -103;
											} else if (c != '\u0161') {
												if (c == '\u203A') {
													charByte = -101;
												} else if (c != '\u0153') {
													if (c == '\u017E') {
														charByte = -98;
													} else if (c != '\u0178') {
														charByte = 63;
													} else {
														charByte = -97;
													}
												} else {
													charByte = -100;
												}
											} else {
												charByte = -102;
											}
										} else {
											charByte = -107;
										}
									} else {
										charByte = -109;
									}
								} else {
									charByte = -110;
								}
							} else {
								charByte = -114;
							}
						} else {
							charByte = -122;
						}
					} else {
						charByte = -123;
					}
				} else {
					charByte = -125;
				}
			} else {
				charByte = -126;
			}
		} else {
			charByte = -128;
		}
		return charByte;
	}

	public static char[] aCharArray6385 = { '\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', '\u2021',
			'\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018', '\u2019', '\u201c',
			'\u201d', '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0', '\u017e',
			'\u0178' };

	public static final String getUnformatedMessage(int messageDataLength, int messageDataOffset, byte[] messageData) {
		char[] cs = new char[messageDataLength];
		int i = 0;
		for (int i_6_ = 0; i_6_ < messageDataLength; i_6_++) {
			int i_7_ = 0xff & messageData[i_6_ + messageDataOffset];
			if ((i_7_ ^ 0xffffffff) != -1) {
				if ((i_7_ ^ 0xffffffff) <= -129 && (i_7_ ^ 0xffffffff) > -161) {
					int i_8_ = aCharArray6385[i_7_ - 128];
					if (i_8_ == 0)
						i_8_ = 63;
					i_7_ = i_8_;
				}
				cs[i++] = (char) i_7_;
			}
		}
		return new String(cs, 0, i);
	}

	public static final byte[] getFormatedMessage(String message) {
		int i_0_ = message.length();
		byte[] is = new byte[i_0_];
		for (int i_1_ = 0; (i_1_ ^ 0xffffffff) > (i_0_ ^ 0xffffffff); i_1_++) {
			int i_2_ = message.charAt(i_1_);
			if (((i_2_ ^ 0xffffffff) >= -1 || i_2_ >= 128) && (i_2_ < 160 || i_2_ > 255)) {
				if ((i_2_ ^ 0xffffffff) != -8365) {
					if ((i_2_ ^ 0xffffffff) == -8219)
						is[i_1_] = (byte) -126;
					else if ((i_2_ ^ 0xffffffff) == -403)
						is[i_1_] = (byte) -125;
					else if (i_2_ == 8222)
						is[i_1_] = (byte) -124;
					else if (i_2_ != 8230) {
						if ((i_2_ ^ 0xffffffff) != -8225) {
							if ((i_2_ ^ 0xffffffff) != -8226) {
								if ((i_2_ ^ 0xffffffff) == -711)
									is[i_1_] = (byte) -120;
								else if (i_2_ == 8240)
									is[i_1_] = (byte) -119;
								else if ((i_2_ ^ 0xffffffff) == -353)
									is[i_1_] = (byte) -118;
								else if ((i_2_ ^ 0xffffffff) != -8250) {
									if (i_2_ == 338)
										is[i_1_] = (byte) -116;
									else if (i_2_ == 381)
										is[i_1_] = (byte) -114;
									else if ((i_2_ ^ 0xffffffff) == -8217)
										is[i_1_] = (byte) -111;
									else if (i_2_ == 8217)
										is[i_1_] = (byte) -110;
									else if (i_2_ != 8220) {
										if (i_2_ == 8221)
											is[i_1_] = (byte) -108;
										else if ((i_2_ ^ 0xffffffff) == -8227)
											is[i_1_] = (byte) -107;
										else if ((i_2_ ^ 0xffffffff) != -8212) {
											if (i_2_ == 8212)
												is[i_1_] = (byte) -105;
											else if ((i_2_ ^ 0xffffffff) != -733) {
												if (i_2_ != 8482) {
													if (i_2_ == 353)
														is[i_1_] = (byte) -102;
													else if (i_2_ != 8250) {
														if ((i_2_ ^ 0xffffffff) == -340)
															is[i_1_] = (byte) -100;
														else if (i_2_ != 382) {
															if (i_2_ == 376)
																is[i_1_] = (byte) -97;
															else
																is[i_1_] = (byte) 63;
														} else
															is[i_1_] = (byte) -98;
													} else
														is[i_1_] = (byte) -101;
												} else
													is[i_1_] = (byte) -103;
											} else
												is[i_1_] = (byte) -104;
										} else
											is[i_1_] = (byte) -106;
									} else
										is[i_1_] = (byte) -109;
								} else
									is[i_1_] = (byte) -117;
							} else
								is[i_1_] = (byte) -121;
						} else
							is[i_1_] = (byte) -122;
					} else
						is[i_1_] = (byte) -123;
				} else
					is[i_1_] = (byte) -128;
			} else
				is[i_1_] = (byte) i_2_;
		}
		return is;
	}

	public static char method2782(byte value) {
		int byteChar = 0xff & value;
		if (byteChar == 0)
			throw new IllegalArgumentException(
					"Non cp1252 character 0x" + Integer.toString(byteChar, 16) + " provided");
		if ((byteChar ^ 0xffffffff) <= -129 && byteChar < 160) {
			int i_4_ = aCharArray6385[-128 + byteChar];
			if ((i_4_ ^ 0xffffffff) == -1)
				i_4_ = 63;
			byteChar = i_4_;
		}
		return (char) byteChar;
	}

	public static int getHashMapSize(int size) {
		size--;
		size |= size >>> -1810941663;
		size |= size >>> 2010624802;
		size |= size >>> 10996420;
		size |= size >>> 491045480;
		size |= size >>> 1388313616;
		return 1 + size;
	}

	/**
	 * Walk dirs 0 - South-West 1 - South 2 - South-East 3 - West 4 - East 5 - North-West 6 - North 7 - North-East
	 */
	public static int getPlayerWalkingDirection(int dx, int dy) {
		if (dx == -1 && dy == -1) {
			return 0;
		}
		if (dx == 0 && dy == -1) {
			return 1;
		}
		if (dx == 1 && dy == -1) {
			return 2;
		}
		if (dx == -1 && dy == 0) {
			return 3;
		}
		if (dx == 1 && dy == 0) {
			return 4;
		}
		if (dx == -1 && dy == 1) {
			return 5;
		}
		if (dx == 0 && dy == 1) {
			return 6;
		}
		if (dx == 1 && dy == 1) {
			return 7;
		}
		return -1;
	}

	public static int getPlayerRunningDirection(int dx, int dy) {
		if (dx == -2 && dy == -2)
			return 0;
		if (dx == -1 && dy == -2)
			return 1;
		if (dx == 0 && dy == -2)
			return 2;
		if (dx == 1 && dy == -2)
			return 3;
		if (dx == 2 && dy == -2)
			return 4;
		if (dx == -2 && dy == -1)
			return 5;
		if (dx == 2 && dy == -1)
			return 6;
		if (dx == -2 && dy == 0)
			return 7;
		if (dx == 2 && dy == 0)
			return 8;
		if (dx == -2 && dy == 1)
			return 9;
		if (dx == 2 && dy == 1)
			return 10;
		if (dx == -2 && dy == 2)
			return 11;
		if (dx == -1 && dy == 2)
			return 12;
		if (dx == 0 && dy == 2)
			return 13;
		if (dx == 1 && dy == 2)
			return 14;
		if (dx == 2 && dy == 2)
			return 15;
		return -1;
	}

	public static byte[] completeQuickMessage(Player player, int fileId, byte[] data) {
		if (fileId == 1)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.AGILITY) };
		else if (fileId == 8)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.ATTACK) };
		else if (fileId == 13)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.CONSTRUCTION) };
		else if (fileId == 16)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.COOKING) };
		else if (fileId == 23)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.CRAFTING) };
		else if (fileId == 30)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.DEFENCE) };
		else if (fileId == 34)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.FARMING) };
		else if (fileId == 41)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.FIREMAKING) };
		else if (fileId == 47)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.FISHING) };
		else if (fileId == 55)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.FLETCHING) };
		else if (fileId == 62)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.HERBLORE) };
		else if (fileId == 70)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.HITPOINTS) };
		else if (fileId == 74)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.HUNTER) };
		else if (fileId == 135)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.MAGIC) };
		else if (fileId == 127)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.MINING) };
		else if (fileId == 120)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.PRAYER) };
		else if (fileId == 116)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.RANGE) };
		else if (fileId == 111)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.RUNECRAFTING) };
		else if (fileId == 103)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.SLAYER) };
		else if (fileId == 96)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.SMITHING) };
		else if (fileId == 92)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.STRENGTH) };
		else if (fileId == 85)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.SUMMONING) };
		else if (fileId == 79)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.THIEVING) };
		else if (fileId == 142)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.WOODCUTTING) };
		else if (fileId == 990)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.DUNGEONEERING) };
		else if (fileId == 965) {
			int value = player.getHitpoints();
			data = new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
		}
		else if (Settings.DEBUG)
			Logger.log("Utils", "qc: " + fileId + ", " + (data == null ? 0 : data.length));
		return data;
	}

	public static String fixChatMessage(String message) {
		StringBuilder newText = new StringBuilder();
		boolean wasSpace = true;
		boolean exception = false;
		for (int i = 0; i < message.length(); i++) {
			if (!exception) {
				if (wasSpace) {
					newText.append(("" + message.charAt(i)).toUpperCase());
					if (!String.valueOf(message.charAt(i)).equals(" "))
						wasSpace = false;
				} else {
					newText.append(("" + message.charAt(i)).toLowerCase());
				}
			} else {
				newText.append(("" + message.charAt(i)));
			}
			if (String.valueOf(message.charAt(i)).contains(":"))
				exception = true;
			else if (String.valueOf(message.charAt(i)).contains(".") || String.valueOf(message.charAt(i)).contains("!")
					|| String.valueOf(message.charAt(i)).contains("?"))
				wasSpace = true;
		}
		return newText.toString();
	}

	private Utils() {

	}

		/**
	 * Decipher.
	 *
	 * @param key  the key
	 * @param data the data
	 * @return the byte[]
	 */
	public static byte[] decipher(int[] key, byte[] data) {
		return decipher(key, data, 0, data.length);
	}

	public static byte[] decipher(int[] key, byte[] data, int offset, int length) {
		int numBlocks = (length - offset) / 8;
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.position(offset);
		for (int i = 0; i < numBlocks; i++) {
			int y = buffer.getInt();
			int z = buffer.getInt();
			int sum = -957401312;
			int delta = -1640531527;
			int numRounds = 32;
			while (numRounds > 0) {
				z -= ((y >>> 5 ^ y << 4) + y ^ sum + key[sum >>> 11 & 0x56c00003]);
				sum -= delta;
				y -= ((z >>> 5 ^ z << 4) - -z ^ sum + key[sum & 0x3]);
				numRounds--;
			}
			buffer.position(buffer.position() - 8);
			buffer.putInt(y);
			buffer.putInt(z);
		}
		return buffer.array();
	}
	
	/**
	 * Decode base37.
	 *
	 * @param value the value
	 * @return the string
	 */

	public static String decodeBase37(long value) {
		char[] chars = new char[12];
		int pos = 0;
		while (value != 0) {
			int remainder = (int) (value % 37);
			value /= 37;

			char c;
			if (remainder >= 1 && remainder <= 26) {
				c = (char) ('a' + remainder - 1);
			} else if (remainder >= 27 && remainder <= 36) {
				c = (char) ('0' + remainder - 27);
			} else {
				c = '_';
			}

			chars[chars.length - pos++ - 1] = c;
		}
		return new String(chars, chars.length - pos, pos);
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive {@code min} and inclusive {@code max}.
	 * @param min The minimum inclusive number.
	 * @param max The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException If {@code max - min + 1} is less than {@code 0}.
	 */
	public static int inclusive(int min, int max) {
		if(min == max)
			return min;
		return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
	}

	/**
	 * Pseudo-randomly retrieves a element from {@code list}.
	 * @param list The list to retrieve an element from.
	 * @return The element retrieved from the list.
	 */
	public static <T> T random(List<T> list) {
		return list.get((int) (ThreadLocalRandom.current().nextDouble() * list.size()));
	}

	/**
	 * Appends the determined plural check to {@code thing}.
	 * @param thing the thing to append.
	 * @return the {@code thing} after the plural check has been appended.
	 */
	public static String appendPluralCheck(String thing) {
		return thing.concat(determinePluralCheck(thing));
	}
	
	/**
	 * Determines the plural check of {@code thing}.
	 * @param thing the thing to determine for.
	 * @return the plural check.
	 */
	public static String determinePluralCheck(String thing) {
		boolean needsPlural = !thing.endsWith("s") && !thing.endsWith(")");
		return needsPlural ? "s" : "";
	}

	/**
	 * Determines if a pseudorandomly generated double rounded to two decimal
	 * places is below or equal to {@code value}.
	 * @param value the value to determine this for.
	 * @return {@code true} if successful, {@code false} otherwise.
	 */
	public static boolean success(double value) {
		if(value >= 1)
			return true;
		return ThreadLocalRandom.current().nextDouble() <= value;
	}

	public static String format(long number) {
		return NumberFormat.getIntegerInstance().format(number);
	}

	public static void runLater(Runnable runThis, int milliseconds) {
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						runThis.run();
					}
				}, milliseconds);
	}


}
