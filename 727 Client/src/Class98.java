/* Class98 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class98 {
	Class118[] aClass118Array997;
	public Class118[] aClass118Array998;
	public boolean aBool999;

	public Class118[] method1615() {
		if (((Class98) this).aClass118Array997 == null) {
			int i = aClass118Array998.length;
			((Class98) this).aClass118Array997 = new Class118[i];
			System.arraycopy(aClass118Array998, 0, ((Class98) this).aClass118Array997, 0, aClass118Array998.length);
		}
		return ((Class98) this).aClass118Array997;
	}

	public Class118[] method1616(int i) {
		return (((Class98) this).aClass118Array997 == null ? aClass118Array998 : ((Class98) this).aClass118Array997);
	}

	public Class118[] method1617(int i) {
		if (((Class98) this).aClass118Array997 == null) {
			int i_0_ = aClass118Array998.length;
			((Class98) this).aClass118Array997 = new Class118[i_0_];
			System.arraycopy(aClass118Array998, 0, ((Class98) this).aClass118Array997, 0, aClass118Array998.length);
		}
		return ((Class98) this).aClass118Array997;
	}

	public Class118 method1618(int i, byte i_1_) {
		if (aClass118Array998[0].anInt1287 * -1952846363 >>> 16 != i >>> 16)
			throw new IllegalArgumentException();
		return aClass118Array998[i & 0xffff];
	}

	public Class118[] method1619() {
		if (((Class98) this).aClass118Array997 == null) {
			int i = aClass118Array998.length;
			((Class98) this).aClass118Array997 = new Class118[i];
			System.arraycopy(aClass118Array998, 0, ((Class98) this).aClass118Array997, 0, aClass118Array998.length);
		}
		return ((Class98) this).aClass118Array997;
	}

	public Class118[] method1620() {
		if (((Class98) this).aClass118Array997 == null) {
			int i = aClass118Array998.length;
			((Class98) this).aClass118Array997 = new Class118[i];
			System.arraycopy(aClass118Array998, 0, ((Class98) this).aClass118Array997, 0, aClass118Array998.length);
		}
		return ((Class98) this).aClass118Array997;
	}

	Class98(boolean bool, Class118[] class118s) {
		aClass118Array998 = class118s;
		aBool999 = bool;
	}

	public Class118 method1621(int i) {
		if (aClass118Array998[0].anInt1287 * -1952846363 >>> 16 != i >>> 16)
			throw new IllegalArgumentException();
		return aClass118Array998[i & 0xffff];
	}

	public Class118 method1622(int i) {
		if (aClass118Array998[0].anInt1287 * -1952846363 >>> 16 != i >>> 16)
			throw new IllegalArgumentException();
		return aClass118Array998[i & 0xffff];
	}

	public static final void method1623(int i) {
		Class216.method3675(1786345593);
		for (int i_2_ = 0; i_2_ < Class260.anInt3219 * -458827259; i_2_++) {
			Class268 class268 = Class260.aClass268Array3232[i_2_];
			boolean bool = false;
			if (((Class268) class268).aClass282_Sub15_Sub5_3304 == null) {
				((Class268) class268).anInt3297 -= 1909850437;
				if (535587213 * ((Class268) class268).anInt3297 >= (class268.method4793((byte) -64) ? -1500 : -10)) {
					if (1 == ((Class268) class268).aByte3300 && ((Class268) class268).aClass343_3303 == null) {
						((Class268) class268).aClass343_3303 = Class343.method6094(Class219.aClass317_2714, (((Class268) class268).anInt3291) * 1932438803, 0);
						if (null == ((Class268) class268).aClass343_3303)
							continue;
						((Class268) class268).anInt3297 += ((Class268) class268).aClass343_3303.method6090() * 1909850437;
					} else if (class268.method4793((byte) -41) && ((((Class268) class268).aClass282_Sub18_3294 == null) || ((((Class268) class268).aClass282_Sub26_Sub1_Sub1_3296) == null))) {
						if (((Class268) class268).aClass282_Sub18_3294 == null)
							((Class268) class268).aClass282_Sub18_3294 = Class282_Sub18.method12270((Class313.aClass317_3665), (1932438803 * (((Class268) class268).anInt3291)));
						if (((Class268) class268).aClass282_Sub18_3294 == null)
							continue;
						if ((((Class268) class268).aClass282_Sub26_Sub1_Sub1_3296) == null) {
							((Class268) class268).aClass282_Sub26_Sub1_Sub1_3296 = ((Class268) class268).aClass282_Sub18_3294.method12272();
							if ((((Class268) class268).aClass282_Sub26_Sub1_Sub1_3296) == null)
								continue;
						}
					}
					if (535587213 * ((Class268) class268).anInt3297 < 0) {
						int i_3_ = 8192;
						int i_4_;
						if (0 != ((Class268) class268).anInt3298 * -19172361) {
							int i_5_ = ((((Class268) class268).anInt3298 * -19172361 >> 24) & 0x3);
							if ((Class84.myPlayer.aByte7967) == i_5_) {
								int i_6_ = ((-19172361 * (((Class268) class268).anInt3298) & 0xff) << 9);
								int i_7_ = Class84.myPlayer.method15805(828768449) << 8;
								Class385 class385 = Class84.myPlayer.method11166().aClass385_3595;
								int i_8_ = ((((Class268) class268).anInt3298 * -19172361) >> 16 & 0xff);
								int i_9_ = (256 + (i_8_ << 9) - (int) class385.aFloat4671 + i_7_);
								int i_10_ = (-19172361 * (((Class268) class268).anInt3298) >> 8 & 0xff);
								int i_11_ = i_7_ + (256 + (i_10_ << 9) - (int) class385.aFloat4673);
								int i_12_ = Math.abs(i_9_) + Math.abs(i_11_) - 512;
								if (i_12_ > i_6_) {
									((Class268) class268).anInt3297 = 1176901669;
									continue;
								}
								if (i_12_ < 0)
									i_12_ = 0;
								i_4_ = ((i_6_ - i_12_) * Class393.aClass282_Sub54_4783.aClass468_Sub13_8193.method12714(347360160) * (((Class268) class268).anInt3299 * -1064835407) / i_6_) >> 2;
								if (((Class268) class268).anInt3305 * 929824435 != -1) {
									i_8_ = (((Class268) class268).anInt3305 * 929824435);
									i_10_ = (((Class268) class268).anInt3306 * 1256992047);
								}
								if (0 != i_9_ || 0 != i_11_) {
									int i_13_ = ((-(1236051449 * Class518.anInt5930) - (int) (Math.atan2((double) i_9_, (double) i_11_) * 2607.5945876176133) - 4096) & 0x3fff);
									if (i_13_ > 8192)
										i_13_ = 16384 - i_13_;
									int i_14_;
									if (i_12_ <= 0)
										i_14_ = 8192;
									else if (i_12_ >= 4096)
										i_14_ = 16384;
									else
										i_14_ = 8192 + (8192 - i_12_) / 4096;
									i_3_ = i_13_ * i_14_ / 8192 + (16384 - i_14_ >> 1);
								}
							} else
								i_4_ = 0;
						} else
							i_4_ = (((Class268) class268).anInt3299 * -1064835407 * (((Class268) class268).aByte3300 == 3 ? Class393.aClass282_Sub54_4783.aClass468_Sub13_8227.method12714(-356829345) : Class393.aClass282_Sub54_4783.aClass468_Sub13_8225.method12714(-2116926454))) >> 2;
						if (i_4_ > 0) {
							if (((Class268) class268).aByte3300 == 1) {
								Object object = null;
								Class282_Sub26_Sub1_Sub2 class282_sub26_sub1_sub2 = (((Class268) class268).aClass343_3303.method6089().method16062(Class119.aClass344_1460));
								((Class268) class268).aClass282_Sub15_Sub5_3304 = (class282_sub26_sub1_sub2.method15225((((Class268) class268).anInt3295 * 995065247), i_4_, i_3_));
							} else if (class268.method4793((byte) -6))
								((Class268) class268).aClass282_Sub15_Sub5_3304 = (((Class268) class268).aClass282_Sub26_Sub1_Sub1_3296.method15225((((Class268) class268).anInt3295 * 995065247), i_4_, i_3_));
							((Class268) class268).aClass282_Sub15_Sub5_3304.method15325((-2056768149 * ((Class268) class268).anInt3302 - 1), (byte) 8);
							Class79.aClass282_Sub15_Sub4_783.method15275(((Class268) class268).aClass282_Sub15_Sub5_3304);
						}
					}
				} else
					bool = true;
			} else if (!((Class268) class268).aClass282_Sub15_Sub5_3304.method4994(1880697803))
				bool = true;
			if (bool) {
				Class260.anInt3219 -= -1221598515;
				for (int i_15_ = i_2_; i_15_ < Class260.anInt3219 * -458827259; i_15_++)
					Class260.aClass268Array3232[i_15_] = Class260.aClass268Array3232[1 + i_15_];
				i_2_--;
			}
		}
		if (Class260.aBool3220 && !Class116.method1966(1117518618)) {
			if (Class393.aClass282_Sub54_4783.aClass468_Sub13_8228.method12714(-1550374075) != 0 && 1293234709 * Class260.anInt3223 != -1) {
				if (Class260.aClass282_Sub15_Sub2_3231 != null)
					Class217.method3690(Class512.aClass317_5884, Class260.anInt3223 * 1293234709, 0, Class393.aClass282_Sub54_4783.aClass468_Sub13_8228.method12714(1791834725), false, Class260.aClass282_Sub15_Sub2_3231, 62181845);
				else
					Class282_Sub43.method13400(Class512.aClass317_5884, 1293234709 * Class260.anInt3223, 0, Class393.aClass282_Sub54_4783.aClass468_Sub13_8228.method12714(-786685196), false, (byte) -3);
			}
			Class260.aBool3220 = false;
			Class260.aClass282_Sub15_Sub2_3231 = null;
		} else if (Class393.aClass282_Sub54_4783.aClass468_Sub13_8228.method12714(-597103482) != 0 && Class260.anInt3223 * 1293234709 != -1 && !Class116.method1966(1621182156)) {
			Class282_Sub23 class282_sub23 = Class271.method4828(OutgoingPacket.aClass379_4529, client.aClass184_7475.aClass432_2283, 61949935);
			class282_sub23.buffer.writeInt(1293234709 * Class260.anInt3223);
			client.aClass184_7475.method3049(class282_sub23, -952420572);
			Class260.anInt3223 = -919080253;
		}
	}

	static final void method1624(Class527 class527, int i) {
		int i_16_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		Class118 class118 = Class117.method1981(i_16_, (byte) 24);
		Class98 class98 = Class468_Sub8.aClass98Array7889[i_16_ >> 16];
		Class17.method569(class118, class98, class527, -575655435);
	}

	static final void method1625(Class527 class527, int i) {
		int i_17_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		if (2 == 1609086245 * client.anInt7434 && i_17_ >= 0 && i_17_ < client.anInt7449 * 493536965)
			((Class527) class527).anIntArray6999[((((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1)] = -315765031 * client.aClass6Array7452[i_17_].anInt42;
		else
			((Class527) class527).anIntArray6999[((((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1)] = 0;
	}

	public static Class483[] method1626(int i) {
		return (new Class483[] { Class483.aClass483_5729, Class483.aClass483_5738, Class483.aClass483_5734, Class483.aClass483_5733, Class483.aClass483_5730, Class483.aClass483_5735, Class483.aClass483_5732, Class483.aClass483_5736, Class483.aClass483_5737, Class483.aClass483_5731 });
	}

	static final void method1627(Class527 class527, int i) {
		Class521_Sub1_Sub5_Sub2.method16114(1752936805);
	}
}
