/* Class105 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class105 {
	String aString1066;
	String aString1067;
	String aString1068;
	static int anInt1069;

	Class105(String string, String string_0_, String string_1_) {
		((Class105) this).aString1067 = string;
		((Class105) this).aString1066 = string_0_;
		((Class105) this).aString1068 = string_1_;
	}

	static final void method1802(Class527 class527, byte i) {
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = ((Class527) class527).aClass61_7010.anInt632 * 1869493667;
	}

	static final void method1803(Class527 class527, int i) {
		Class393.aClass282_Sub54_4783.method13511(Class393.aClass282_Sub54_4783.aClass468_Sub17_8200, ((((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]) != 0) ? 1 : 0, -564300666);
		Class190.method3148((byte) 98);
		client.aClass257_7353.method4547((byte) -119);
	}

	static void method1804(int i, String string, String string_2_, byte i_3_) {
		if (client.aClass184_7475 != null) {
			Class282_Sub23 class282_sub23 = Class271.method4828(OutgoingPacket.aClass379_4533, client.aClass184_7475.aClass432_2283, -368168097);
			class282_sub23.buffer.writeShort((1 + Class234.method3952(string, 1356057931) + Class234.method3952(string_2_, 876996155)), 1417031095);
			class282_sub23.buffer.writeByte(i);
			class282_sub23.buffer.method13070(string_2_, 2115936374);
			class282_sub23.buffer.method13070(string, 2111937462);
			client.aClass184_7475.method3049(class282_sub23, 1078445586);
		}
	}

	public static final void method1805(int i, int i_4_, int i_5_, int i_6_, int i_7_, int i_8_, byte i_9_) {
		if (i >= -612590951 * Class532_Sub1.anInt7071 && i_4_ <= -1345107225 * Class532_Sub1.anInt7069 && i_5_ >= 324226563 * Class532_Sub1.anInt7070 && i_6_ <= Class532_Sub1.anInt7068 * -348932735) {
			if (1 == i_8_)
				Class48_Sub2.method14572(i, i_4_, i_5_, i_6_, i_7_, (short) 26000);
			else
				Class257.method4561(i, i_4_, i_5_, i_6_, i_7_, i_8_, -939335267);
		} else if (i_8_ == 1)
			Class78.method1388(i, i_4_, i_5_, i_6_, i_7_, (byte) 36);
		else
			Class20.method744(i, i_4_, i_5_, i_6_, i_7_, i_8_, -2073679556);
	}

	static void method1806(RsBitsBuffer class282_sub35_sub2, int i, int i_10_) {
		Class219 class219 = client.aClass257_7353.method4519(2087234907);
		boolean bool = class282_sub35_sub2.readBits(1, (byte) 56) == 1;
		if (bool)
			Class197.anIntArray2435[(Class197.anInt2434 += 1879181821) * -706143403 - 1] = i;
		int i_11_ = class282_sub35_sub2.readBits(2, (byte) 38);
		Class521_Sub1_Sub1_Sub2_Sub1 class521_sub1_sub1_sub2_sub1 = client.aClass521_Sub1_Sub1_Sub2_Sub1Array7314[i];
		if (i_11_ == 0) {
			if (bool)
				class521_sub1_sub1_sub2_sub1.aBool10568 = false;
			else {
				if (i == 1595512269 * client.anInt7315)
					throw new RuntimeException();
				Class4 class4 = Class197.aClass4Array2430[i] = new Class4();
				((Class4) class4).anInt31 = (-1096995395 * (((class219.anInt2712 * -1002240017 + class521_sub1_sub1_sub2_sub1.anIntArray10336[0]) >> 6) + ((class521_sub1_sub1_sub2_sub1.aByte7967 << 28) + ((1948093437 * class219.anInt2711 + (class521_sub1_sub1_sub2_sub1.anIntArray10356[0])) >> 6 << 14))));
				if (-1 != 327043279 * class521_sub1_sub1_sub2_sub1.anInt10557)
					((Class4) class4).anInt30 = class521_sub1_sub1_sub2_sub1.anInt10557 * 20062537;
				else
					((Class4) class4).anInt30 = class521_sub1_sub1_sub2_sub1.aClass19_10359.method578((byte) 2) * 2046380647;
				((Class4) class4).anInt32 = class521_sub1_sub1_sub2_sub1.anInt10373 * -384342479;
				((Class4) class4).aBool29 = class521_sub1_sub1_sub2_sub1.aBool10571;
				((Class4) class4).aBool33 = class521_sub1_sub1_sub2_sub1.aBool10550;
				if (class521_sub1_sub1_sub2_sub1.anInt10567 * 1304574447 > 0)
					Class149_Sub2.method14609(class521_sub1_sub1_sub2_sub1, -1660310904);
				client.aClass521_Sub1_Sub1_Sub2_Sub1Array7314[i] = null;
				if (class282_sub35_sub2.readBits(1, (byte) -34) != 0)
					Class346.method6155(class282_sub35_sub2, i, (short) 371);
			}
		} else if (i_11_ == 1) {
			int i_12_ = class282_sub35_sub2.readBits(3, (byte) -56);
			int i_13_ = class521_sub1_sub1_sub2_sub1.anIntArray10356[0];
			int i_14_ = class521_sub1_sub1_sub2_sub1.anIntArray10336[0];
			if (0 == i_12_) {
				i_13_--;
				i_14_--;
			} else if (i_12_ == 1)
				i_14_--;
			else if (2 == i_12_) {
				i_13_++;
				i_14_--;
			} else if (i_12_ == 3)
				i_13_--;
			else if (i_12_ == 4)
				i_13_++;
			else if (5 == i_12_) {
				i_13_--;
				i_14_++;
			} else if (6 == i_12_)
				i_14_++;
			else if (i_12_ == 7) {
				i_13_++;
				i_14_++;
			}
			if (bool) {
				class521_sub1_sub1_sub2_sub1.anInt10569 = i_13_ * -618896179;
				class521_sub1_sub1_sub2_sub1.anInt10570 = i_14_ * -108698839;
				class521_sub1_sub1_sub2_sub1.aBool10568 = true;
			} else
				class521_sub1_sub1_sub2_sub1.method16129(i_13_, i_14_, (Class197.playerMovementTypes[i]), 750897153);
		} else if (i_11_ == 2) {
			int i_15_ = class282_sub35_sub2.readBits(4, (byte) 80);
			int i_16_ = class521_sub1_sub1_sub2_sub1.anIntArray10356[0];
			int i_17_ = class521_sub1_sub1_sub2_sub1.anIntArray10336[0];
			if (0 == i_15_) {
				i_16_ -= 2;
				i_17_ -= 2;
			} else if (1 == i_15_) {
				i_16_--;
				i_17_ -= 2;
			} else if (i_15_ == 2)
				i_17_ -= 2;
			else if (3 == i_15_) {
				i_16_++;
				i_17_ -= 2;
			} else if (i_15_ == 4) {
				i_16_ += 2;
				i_17_ -= 2;
			} else if (i_15_ == 5) {
				i_16_ -= 2;
				i_17_--;
			} else if (i_15_ == 6) {
				i_16_ += 2;
				i_17_--;
			} else if (i_15_ == 7)
				i_16_ -= 2;
			else if (8 == i_15_)
				i_16_ += 2;
			else if (i_15_ == 9) {
				i_16_ -= 2;
				i_17_++;
			} else if (i_15_ == 10) {
				i_16_ += 2;
				i_17_++;
			} else if (i_15_ == 11) {
				i_16_ -= 2;
				i_17_ += 2;
			} else if (12 == i_15_) {
				i_16_--;
				i_17_ += 2;
			} else if (i_15_ == 13)
				i_17_ += 2;
			else if (i_15_ == 14) {
				i_16_++;
				i_17_ += 2;
			} else if (15 == i_15_) {
				i_16_ += 2;
				i_17_ += 2;
			}
			if (bool) {
				class521_sub1_sub1_sub2_sub1.anInt10569 = i_16_ * -618896179;
				class521_sub1_sub1_sub2_sub1.anInt10570 = -108698839 * i_17_;
				class521_sub1_sub1_sub2_sub1.aBool10568 = true;
			} else
				class521_sub1_sub1_sub2_sub1.method16129(i_16_, i_17_, (Class197.playerMovementTypes[i]), -1678489989);
		} else {
			int i_18_ = class282_sub35_sub2.readBits(1, (byte) -12);
			if (0 == i_18_) {
				int i_19_ = class282_sub35_sub2.readBits(12, (byte) 17);
				int i_20_ = i_19_ >> 10;
				int i_21_ = i_19_ >> 5 & 0x1f;
				if (i_21_ > 15)
					i_21_ -= 32;
				int i_22_ = i_19_ & 0x1f;
				if (i_22_ > 15)
					i_22_ -= 32;
				int i_23_ = i_21_ + class521_sub1_sub1_sub2_sub1.anIntArray10356[0];
				int i_24_ = class521_sub1_sub1_sub2_sub1.anIntArray10336[0] + i_22_;
				if (bool) {
					class521_sub1_sub1_sub2_sub1.anInt10569 = i_23_ * -618896179;
					class521_sub1_sub1_sub2_sub1.anInt10570 = -108698839 * i_24_;
					class521_sub1_sub1_sub2_sub1.aBool10568 = true;
				} else
					class521_sub1_sub1_sub2_sub1.method16129(i_23_, i_24_, (Class197.playerMovementTypes[i]), -1262996328);
				class521_sub1_sub1_sub2_sub1.aByte7967 = class521_sub1_sub1_sub2_sub1.aByte7968 = (byte) (i_20_ + class521_sub1_sub1_sub2_sub1.aByte7967 & 0x3);
				if (client.aClass257_7353.method4433(33386298).method5497(i_23_, i_24_, 1753987250))
					class521_sub1_sub1_sub2_sub1.aByte7968++;
				if (i == 1595512269 * client.anInt7315 && (class521_sub1_sub1_sub2_sub1.aByte7967 != Class4.anInt35 * 675588453))
					Class4.anInt35 = class521_sub1_sub1_sub2_sub1.aByte7967 * -647602067;
			} else {
				int i_25_ = class282_sub35_sub2.readBits(30, (byte) -84);
				int i_26_ = i_25_ >> 28;
				int i_27_ = i_25_ >> 14 & 0x3fff;
				int i_28_ = i_25_ & 0x3fff;
				int i_29_ = ((i_27_ + (class219.anInt2711 * 1948093437 + (class521_sub1_sub1_sub2_sub1.anIntArray10356[0])) & 0x3fff) - 1948093437 * class219.anInt2711);
				int i_30_ = (i_28_ + (class521_sub1_sub1_sub2_sub1.anIntArray10336[0] + class219.anInt2712 * -1002240017) & 0x3fff) - class219.anInt2712 * -1002240017;
				if (bool) {
					class521_sub1_sub1_sub2_sub1.anInt10569 = i_29_ * -618896179;
					class521_sub1_sub1_sub2_sub1.anInt10570 = i_30_ * -108698839;
					class521_sub1_sub1_sub2_sub1.aBool10568 = true;
				} else
					class521_sub1_sub1_sub2_sub1.method16129(i_29_, i_30_, (Class197.playerMovementTypes[i]), -106492270);
				class521_sub1_sub1_sub2_sub1.aByte7967 = class521_sub1_sub1_sub2_sub1.aByte7968 = (byte) (i_26_ + class521_sub1_sub1_sub2_sub1.aByte7967 & 0x3);
				if (client.aClass257_7353.method4433(33386298).method5497(i_29_, i_30_, 1539369664))
					class521_sub1_sub1_sub2_sub1.aByte7968++;
				if (i == client.anInt7315 * 1595512269)
					Class4.anInt35 = class521_sub1_sub1_sub2_sub1.aByte7967 * -647602067;
			}
		}
	}

	public static void method1807(int i, byte i_31_) {
		Class282_Sub50_Sub12 class282_sub50_sub12 = Class263.method4778(14, (long) i);
		class282_sub50_sub12.method14965((byte) 94);
	}
}
