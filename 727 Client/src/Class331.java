/* Class331 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class331 {
	static final int anInt3869 = 0;
	Class317 aClass317_3870;
	Class229 aClass229_3871 = new Class229(64);
	Class317 aClass317_3872;
	static final int anInt3873 = 32768;

	public Class282_Sub50_Sub14 method5917(int i) {
		Class282_Sub50_Sub14 class282_sub50_sub14 = ((Class282_Sub50_Sub14) ((Class331) this).aClass229_3871.method3865((long) i));
		if (null != class282_sub50_sub14)
			return class282_sub50_sub14;
		byte[] is;
		if (i >= 32768)
			is = ((Class331) this).aClass317_3870.method5607(0, i & 0x7fff, -2051884154);
		else
			is = ((Class331) this).aClass317_3872.method5607(0, i, -1995501795);
		class282_sub50_sub14 = new Class282_Sub50_Sub14();
		if (null != is)
			class282_sub50_sub14.method15209(new RsByteBuffer(is), -514314699);
		if (i >= 32768)
			class282_sub50_sub14.method15213(707027607);
		((Class331) this).aClass229_3871.method3856(class282_sub50_sub14, (long) i);
		return class282_sub50_sub14;
	}

	public Class282_Sub50_Sub14 method5918(int i, int i_0_) {
		Class282_Sub50_Sub14 class282_sub50_sub14 = ((Class282_Sub50_Sub14) ((Class331) this).aClass229_3871.method3865((long) i));
		if (null != class282_sub50_sub14)
			return class282_sub50_sub14;
		byte[] is;
		if (i >= 32768)
			is = ((Class331) this).aClass317_3870.method5607(0, i & 0x7fff, -2001151071);
		else
			is = ((Class331) this).aClass317_3872.method5607(0, i, -2070203520);
		class282_sub50_sub14 = new Class282_Sub50_Sub14();
		if (null != is)
			class282_sub50_sub14.method15209(new RsByteBuffer(is), 625561470);
		if (i >= 32768)
			class282_sub50_sub14.method15213(707027607);
		((Class331) this).aClass229_3871.method3856(class282_sub50_sub14, (long) i);
		return class282_sub50_sub14;
	}

	public Class282_Sub50_Sub14 method5919(int i) {
		Class282_Sub50_Sub14 class282_sub50_sub14 = ((Class282_Sub50_Sub14) ((Class331) this).aClass229_3871.method3865((long) i));
		if (null != class282_sub50_sub14)
			return class282_sub50_sub14;
		byte[] is;
		if (i >= 32768)
			is = ((Class331) this).aClass317_3870.method5607(0, i & 0x7fff, -1429282699);
		else
			is = ((Class331) this).aClass317_3872.method5607(0, i, -1561425664);
		class282_sub50_sub14 = new Class282_Sub50_Sub14();
		if (null != is)
			class282_sub50_sub14.method15209(new RsByteBuffer(is), -326489303);
		if (i >= 32768)
			class282_sub50_sub14.method15213(707027607);
		((Class331) this).aClass229_3871.method3856(class282_sub50_sub14, (long) i);
		return class282_sub50_sub14;
	}

	public Class282_Sub50_Sub14 method5920(int i) {
		Class282_Sub50_Sub14 class282_sub50_sub14 = ((Class282_Sub50_Sub14) ((Class331) this).aClass229_3871.method3865((long) i));
		if (null != class282_sub50_sub14)
			return class282_sub50_sub14;
		byte[] is;
		if (i >= 32768)
			is = ((Class331) this).aClass317_3870.method5607(0, i & 0x7fff, -2080493810);
		else
			is = ((Class331) this).aClass317_3872.method5607(0, i, -2118557851);
		class282_sub50_sub14 = new Class282_Sub50_Sub14();
		if (null != is)
			class282_sub50_sub14.method15209(new RsByteBuffer(is), 434656878);
		if (i >= 32768)
			class282_sub50_sub14.method15213(707027607);
		((Class331) this).aClass229_3871.method3856(class282_sub50_sub14, (long) i);
		return class282_sub50_sub14;
	}

	public Class331(Class495 class495, Class317 class317, Class317 class317_1_) {
		((Class331) this).aClass317_3872 = class317;
		((Class331) this).aClass317_3870 = class317_1_;
		if (null != ((Class331) this).aClass317_3872)
			((Class331) this).aClass317_3872.method5624(0, -998395379);
		if (((Class331) this).aClass317_3870 != null)
			((Class331) this).aClass317_3870.method5624(0, -1184061863);
	}

	static void method5921(byte i) {
		if (client.aByteArray7152 != null) {
			if (i > 2)
				Class346.method6160(1908586920);
		} else if (-1 != Class9.anInt76 * 1051306693) {
			if (i > 2)
				Class279.method4964(-1370855311);
		} else
			Class282_Sub20_Sub26.lobbyLogin(Class9.aString99, Class9.aString102, (byte) -8);
	}

	static final void method5922(Class527 class527, int i) {
		((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 += 1476624725) * 1806726141 - 1] = ((Class527) class527).aClass61_7010.aString622;
	}

	public static void method5923(Class521_Sub1_Sub1_Sub2 class521_sub1_sub1_sub2, int[] is, int[] is_2_, int[] is_3_, int i) {
		for (int i_4_ = 0; i_4_ < is.length; i_4_++) {
			int i_5_ = is[i_4_];
			int i_6_ = is_3_[i_4_];
			int i_7_ = is_2_[i_4_];
			for (int i_8_ = 0; i_6_ != 0 && i_8_ < (class521_sub1_sub1_sub2.aClass456_Sub2_Sub1Array10354).length; i_6_ >>>= 1) {
				if ((i_6_ & 0x1) != 0) {
					if (i_5_ == -1)
						class521_sub1_sub1_sub2.aClass456_Sub2_Sub1Array10354[i_8_] = null;
					else {
						Class518 class518 = Class330.aClass523_3868.method11205(i_5_, (byte) 24);
						int i_9_ = class518.anInt5907 * 554947543;
						Class456_Sub2_Sub1 class456_sub2_sub1 = (class521_sub1_sub1_sub2.aClass456_Sub2_Sub1Array10354[i_8_]);
						if (null != class456_sub2_sub1 && class456_sub2_sub1.method7564(646988470)) {
							if (i_5_ == class456_sub2_sub1.method7597(-1055922469)) {
								if (0 == i_9_) {
									class521_sub1_sub1_sub2.aClass456_Sub2_Sub1Array10354[i_8_] = null;
									class456_sub2_sub1 = null;
								} else if (i_9_ == 1) {
									class456_sub2_sub1.method7582((byte) -96);
									class456_sub2_sub1.anInt10065 = i_7_ * 872304347;
								} else if (i_9_ == 2)
									class456_sub2_sub1.method7584(-309852534);
							} else if (class518.anInt5916 * -1834317435 >= ((class456_sub2_sub1.method7565(-1632742162).anInt5916) * -1834317435)) {
								class521_sub1_sub1_sub2.aClass456_Sub2_Sub1Array10354[i_8_] = null;
								class456_sub2_sub1 = null;
							}
						}
						if (null == class456_sub2_sub1 || !class456_sub2_sub1.method7564(1061451130)) {
							class456_sub2_sub1 = class521_sub1_sub1_sub2.aClass456_Sub2_Sub1Array10354[i_8_] = (new Class456_Sub2_Sub1(class521_sub1_sub1_sub2));
							class456_sub2_sub1.method7567(i_5_, (short) 8960);
							class456_sub2_sub1.anInt10065 = i_7_ * 872304347;
						}
					}
				}
				i_8_++;
			}
		}
	}

	static void method5924(int i, boolean bool, int i_10_) {
		Class282_Sub50_Sub12 class282_sub50_sub12 = Class263.method4778(21, (long) i);
		class282_sub50_sub12.method14995(1949020630);
		((Class282_Sub50_Sub12) class282_sub50_sub12).anInt9668 = -1773141545 * (bool ? 1 : 0);
	}

	static final void method5925(Class527 class527, int i) {
		((Class527) class527).anInt7000 -= -1341717846;
		String string = (String) (((Class527) class527).anObjectArray7019[((Class527) class527).anInt7000 * 1806726141]);
		String string_11_ = (String) (((Class527) class527).anObjectArray7019[1 + 1806726141 * ((Class527) class527).anInt7000]);
		Class155.method2635(string, string_11_, 431989150);
	}

	static final void method5926(Class527 class527, int i) {
		int i_12_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		Class118 class118 = Class117.method1981(i_12_, (byte) 108);
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = class118.anInt1293 * 1552292309;
	}
}
