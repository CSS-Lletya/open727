/* Class242 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class242 {
	int anInt2978;
	Class317 aClass317_2979;
	Class240[] aClass240Array2980;
	static Class91 aClass91_2981;
	public static int anInt2982;

	public Class240 method4153(int i) {
		return ((Class242) this).aClass240Array2980[i];
	}

	int method4154(Interface42 interface42, int i) {
		int i_0_ = 0;
		for (int i_1_ = 0; i_1_ < ((Class242) this).anInt2978 * 526499893; i_1_++) {
			Class240 class240 = method4156(i_1_, -1396181317);
			if (class240.method4099(interface42, 490029588))
				i_0_ += 554241429 * class240.anInt2963;
		}
		return i_0_;
	}

	public Class240 method4155(int i) {
		return ((Class242) this).aClass240Array2980[i];
	}

	public Class242(Class486 class486, Class495 class495, Class317 class317, boolean bool) {
		((Class242) this).aClass317_2979 = class317;
		if (((Class242) this).aClass317_2979 != null)
			((Class242) this).anInt2978 = (((Class242) this).aClass317_2979.method5624(-71319279 * Class120.aClass120_1498.anInt1521, -1883638938)) * 1089704477;
		else
			((Class242) this).anInt2978 = 0;
		if (bool) {
			((Class242) this).aClass240Array2980 = new Class240[((Class242) this).anInt2978 * 526499893];
			for (int i = 0; i < 526499893 * ((Class242) this).anInt2978; i++) {
				byte[] is;
				synchronized (((Class242) this).aClass317_2979) {
					is = (((Class242) this).aClass317_2979.method5607(-71319279 * Class120.aClass120_1498.anInt1521, i, -1290272928));
				}
				Class240 class240 = new Class240();
				if (null != is)
					class240.method4119(new RsByteBuffer(is), -1345439646);
				class240.method4095((byte) 43);
				((Class242) this).aClass240Array2980[i] = class240;
				((Class240) ((Class242) this).aClass240Array2980[i]).aClass242_2976 = this;
			}
		}
	}

	public Class240 method4156(int i, int i_2_) {
		return ((Class242) this).aClass240Array2980[i];
	}

	public Class240 method4157(int i) {
		return ((Class242) this).aClass240Array2980[i];
	}

	int method4158(Interface42 interface42) {
		int i = 0;
		for (int i_3_ = 0; i_3_ < ((Class242) this).anInt2978 * 526499893; i_3_++) {
			Class240 class240 = method4156(i_3_, -1396181317);
			if (class240.method4099(interface42, -1427808591))
				i += 554241429 * class240.anInt2963;
		}
		return i;
	}

	static final void method4159(Class118 class118, Class98 class98, Class527 class527, int i) {
		((Class527) class527).anInt7012 -= 567564004;
		class118.anInt1340 = 788815611 * (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537]);
		class118.anInt1350 = (2093089603 * (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 + 1]));
		class118.anInt1281 = (-875347675 * (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 + 2]));
		class118.anInt1354 = ((((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 + 3]) * -797924565);
		Class109.method1858(class118, (byte) -26);
	}

	static final void method4160(Class118 class118, Class98 class98, Class527 class527, short i) {
		class118.anInt1323 = ((((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]) * -848451677);
		Class109.method1858(class118, (byte) -6);
	}

	static final void method4161(Class527 class527, int i) {
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = Class152.method2598(1461837839).method243((byte) 1);
	}

	public static final int method4162(int i, int i_4_, byte i_5_) {
		if (i == -2)
			return 12345678;
		if (i == -1) {
			if (i_4_ < 2)
				i_4_ = 2;
			else if (i_4_ > 126)
				i_4_ = 126;
			return i_4_;
		}
		i_4_ = (i & 0x7f) * i_4_ >> 7;
		if (i_4_ < 2)
			i_4_ = 2;
		else if (i_4_ > 126)
			i_4_ = 126;
		return i_4_ + (i & 0xff80);
	}

	public static byte method4163(char c, byte i) {
		byte i_6_;
		if (c > 0 && c < '\u0080' || c >= '\u00a0' && c <= '\u00ff')
			i_6_ = (byte) c;
		else if (c == '\u20ac')
			i_6_ = (byte) -128;
		else if ('\u201a' == c)
			i_6_ = (byte) -126;
		else if ('\u0192' == c)
			i_6_ = (byte) -125;
		else if (c == '\u201e')
			i_6_ = (byte) -124;
		else if ('\u2026' == c)
			i_6_ = (byte) -123;
		else if (c == '\u2020')
			i_6_ = (byte) -122;
		else if (c == '\u2021')
			i_6_ = (byte) -121;
		else if (c == '\u02c6')
			i_6_ = (byte) -120;
		else if ('\u2030' == c)
			i_6_ = (byte) -119;
		else if ('\u0160' == c)
			i_6_ = (byte) -118;
		else if (c == '\u2039')
			i_6_ = (byte) -117;
		else if ('\u0152' == c)
			i_6_ = (byte) -116;
		else if ('\u017d' == c)
			i_6_ = (byte) -114;
		else if ('\u2018' == c)
			i_6_ = (byte) -111;
		else if (c == '\u2019')
			i_6_ = (byte) -110;
		else if (c == '\u201c')
			i_6_ = (byte) -109;
		else if (c == '\u201d')
			i_6_ = (byte) -108;
		else if (c == '\u2022')
			i_6_ = (byte) -107;
		else if (c == '\u2013')
			i_6_ = (byte) -106;
		else if ('\u2014' == c)
			i_6_ = (byte) -105;
		else if ('\u02dc' == c)
			i_6_ = (byte) -104;
		else if (c == '\u2122')
			i_6_ = (byte) -103;
		else if ('\u0161' == c)
			i_6_ = (byte) -102;
		else if (c == '\u203a')
			i_6_ = (byte) -101;
		else if (c == '\u0153')
			i_6_ = (byte) -100;
		else if (c == '\u017e')
			i_6_ = (byte) -98;
		else if (c == '\u0178')
			i_6_ = (byte) -97;
		else
			i_6_ = (byte) 63;
		return i_6_;
	}

	static void method4164(int i, int i_7_, int i_8_) {
		if (Class20.anInt167 * -468886213 == 1)
			Class96_Sub4.method13789(Class46.aClass282_Sub50_Sub7_438, i, i_7_, -549787245);
		else if (2 == -468886213 * Class20.anInt167)
			Class262.method4653(i, i_7_, -769540795);
		Class20.anInt167 = 0;
		Class46.aClass282_Sub50_Sub7_438 = null;
	}

	static Class149_Sub3 method4165(RsByteBuffer class282_sub35, int i) {
		return new Class149_Sub3(class282_sub35.method13081(1658104919), class282_sub35.method13081(1705227576), class282_sub35.method13081(1944156439), class282_sub35.method13081(1826689073), class282_sub35.method13082((short) 30111), class282_sub35.readUnsignedByte());
	}

	public static final void method4166(int i) {
		if (!client.aBool7331) {
			client.aFloat7284 += (12.0F - client.aFloat7284) / 2.0F;
			client.aBool7371 = true;
			client.aBool7331 = true;
		}
	}

	public static void method4167(String string, int i) {
		if (client.aBool7344 && (1310510077 * Class506.anInt5858 & 0x18) != 0) {
			boolean bool = false;
			int i_9_ = Class197.anInt2429 * -963499271;
			int[] is = Class197.anIntArray2433;
			for (int i_10_ = 0; i_10_ < i_9_; i_10_++) {
				Class521_Sub1_Sub1_Sub2_Sub1 class521_sub1_sub1_sub2_sub1 = client.aClass521_Sub1_Sub1_Sub2_Sub1Array7314[is[i_10_]];
				if (null != class521_sub1_sub1_sub2_sub1.aString10546 && class521_sub1_sub1_sub2_sub1.aString10546.equalsIgnoreCase(string) && (((Class84.myPlayer == class521_sub1_sub1_sub2_sub1) && (Class506.anInt5858 * 1310510077 & 0x10) != 0) || (Class506.anInt5858 * 1310510077 & 0x8) != 0)) {
					Class282_Sub23 class282_sub23 = Class271.method4828(OutgoingPacket.INTERFACE_ON_PLAYER_PACKET, (client.aClass184_7475.aClass432_2283), 623584347);
					class282_sub23.buffer.writeShort(-1673073865 * client.anInt7345, 1417031095);
					class282_sub23.buffer.writeShortLE(is[i_10_], (short) -11612);
					class282_sub23.buffer.write128Byte(0, -2130241701);
					class282_sub23.buffer.readIntV2(728544879 * Class7.anInt56, (byte) 104);
					class282_sub23.buffer.writeShortLE(client.anInt7346 * -1555739329, (short) -16837);
					client.aClass184_7475.method3049(class282_sub23, 2109181627);
					int i_11_ = class521_sub1_sub1_sub2_sub1.method15805(828768449);
					Class257.method4559(class521_sub1_sub1_sub2_sub1.anIntArray10356[0], class521_sub1_sub1_sub2_sub1.anIntArray10336[0], true, Class344.method6115((class521_sub1_sub1_sub2_sub1.anIntArray10356[0]), (class521_sub1_sub1_sub2_sub1.anIntArray10336[0]), i_11_, i_11_, 0, 971339209), 1352975566);
					bool = true;
					break;
				}
			}
			if (!bool)
				Class387.method6681(4, new StringBuilder().append(Class433.aClass433_5280.method7273(Class223.aClass495_2772, -1621273158)).append(string).toString(), (byte) -49);
			if (client.aBool7344)
				Class60.method1170(-609337146);
		}
	}

	static final void method4168(Class527 class527, int i) {
		Class393.aClass282_Sub54_4783.method13511(Class393.aClass282_Sub54_4783.aClass468_Sub4_8187, (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]), -1129391338);
		Class190.method3148((byte) 102);
	}
}
