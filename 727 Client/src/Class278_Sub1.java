
/* Class278_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.IOException;

public class Class278_Sub1 extends Class278 {
	static final int anInt8110 = 10;
	byte[][] aByteArrayArray8111 = new byte[10][];
	RsByteBuffer aClass282_Sub35_8112 = new RsByteBuffer(null);
	int anInt8113;
	int[] anIntArray8114;
	int anInt8115;
	RsByteBuffer aClass282_Sub35_8116 = new RsByteBuffer(null);
	Class317 aClass317_8117;

	int method4925(byte[] is) throws IOException {
		if (((Class278_Sub1) this).anIntArray8114 == null) {
			if (!((Class278_Sub1) this).aClass317_8117.method5688(((Class278_Sub1) this).anInt8113 * 735121369, 0, 16711935))
				return 0;
			byte[] is_0_ = (((Class278_Sub1) this).aClass317_8117.method5607(735121369 * ((Class278_Sub1) this).anInt8113, 0, -2056397853));
			if (null == is_0_)
				throw new IllegalStateException("");
			((Class278_Sub1) this).aClass282_Sub35_8116.buffer = is_0_;
			((Class278_Sub1) this).aClass282_Sub35_8116.index = 0;
			int i = is_0_.length >> 1;
			((Class278_Sub1) this).anIntArray8114 = new int[i];
			for (int i_1_ = 0; i_1_ < i; i_1_++)
				((Class278_Sub1) this).anIntArray8114[i_1_] = ((Class278_Sub1) this).aClass282_Sub35_8116.readUnsignedShort();
		}
		if (-285637449 * ((Class278_Sub1) this).anInt8115 >= ((Class278_Sub1) this).anIntArray8114.length)
			return -1;
		method13447(492449179);
		((Class278_Sub1) this).aClass282_Sub35_8116.buffer = is;
		((Class278_Sub1) this).aClass282_Sub35_8116.index = 0;
		while ((-1990677291 * ((Class278_Sub1) this).aClass282_Sub35_8116.index) < (((Class278_Sub1) this).aClass282_Sub35_8116.buffer).length) {
			if (null == (((Class278_Sub1) this).aClass282_Sub35_8112.buffer)) {
				if (null == ((Class278_Sub1) this).aByteArrayArray8111[0]) {
					((Class278_Sub1) this).aClass282_Sub35_8116.buffer = null;
					return ((((Class278_Sub1) this).aClass282_Sub35_8116.index) * -1990677291);
				}
				((Class278_Sub1) this).aClass282_Sub35_8112.buffer = ((Class278_Sub1) this).aByteArrayArray8111[0];
			}
			int i = ((((Class278_Sub1) this).aClass282_Sub35_8116.buffer).length - -1990677291 * (((Class278_Sub1) this).aClass282_Sub35_8116.index));
			int i_2_ = ((((Class278_Sub1) this).aClass282_Sub35_8112.buffer).length - -1990677291 * (((Class278_Sub1) this).aClass282_Sub35_8112.index));
			if (i < i_2_) {
				((Class278_Sub1) this).aClass282_Sub35_8112.readBytes((((Class278_Sub1) this).aClass282_Sub35_8116.buffer), (((Class278_Sub1) this).aClass282_Sub35_8116.index * -1990677291), i, 17733425);
				((Class278_Sub1) this).aClass282_Sub35_8116.buffer = null;
				return is.length;
			}
			((Class278_Sub1) this).aClass282_Sub35_8116.writeBytes(((Class278_Sub1) this).aClass282_Sub35_8112.buffer, (((Class278_Sub1) this).aClass282_Sub35_8112.index * -1990677291), i_2_);
			((Class278_Sub1) this).aClass282_Sub35_8112.buffer = null;
			((Class278_Sub1) this).aClass282_Sub35_8112.index = 0;
			((Class278_Sub1) this).anInt8115 += -603678457;
			for (int i_3_ = 0; i_3_ < 9; i_3_++)
				((Class278_Sub1) this).aByteArrayArray8111[i_3_] = ((Class278_Sub1) this).aByteArrayArray8111[1 + i_3_];
			((Class278_Sub1) this).aByteArrayArray8111[9] = null;
			if (((Class278_Sub1) this).anInt8115 * -285637449 >= ((Class278_Sub1) this).anIntArray8114.length) {
				((Class278_Sub1) this).aClass282_Sub35_8116.buffer = null;
				return -1990677291 * (((Class278_Sub1) this).aClass282_Sub35_8116.index);
			}
		}
		((Class278_Sub1) this).aClass282_Sub35_8116.buffer = null;
		return is.length;
	}

	public Class278_Sub1(int i, Class317 class317, int i_4_) {
		super(i);
		((Class278_Sub1) this).aClass317_8117 = class317;
		((Class278_Sub1) this).anInt8113 = 963936361 * i_4_;
	}

	int method4951(byte[] is, int i) throws IOException {
		if (((Class278_Sub1) this).anIntArray8114 == null) {
			if (!((Class278_Sub1) this).aClass317_8117.method5688(((Class278_Sub1) this).anInt8113 * 735121369, 0, 16711935))
				return 0;
			byte[] is_5_ = (((Class278_Sub1) this).aClass317_8117.method5607(735121369 * ((Class278_Sub1) this).anInt8113, 0, -1220217893));
			if (null == is_5_)
				throw new IllegalStateException("");
			((Class278_Sub1) this).aClass282_Sub35_8116.buffer = is_5_;
			((Class278_Sub1) this).aClass282_Sub35_8116.index = 0;
			int i_6_ = is_5_.length >> 1;
			((Class278_Sub1) this).anIntArray8114 = new int[i_6_];
			for (int i_7_ = 0; i_7_ < i_6_; i_7_++)
				((Class278_Sub1) this).anIntArray8114[i_7_] = ((Class278_Sub1) this).aClass282_Sub35_8116.readUnsignedShort();
		}
		if (-285637449 * ((Class278_Sub1) this).anInt8115 >= ((Class278_Sub1) this).anIntArray8114.length)
			return -1;
		method13447(-1706767091);
		((Class278_Sub1) this).aClass282_Sub35_8116.buffer = is;
		((Class278_Sub1) this).aClass282_Sub35_8116.index = 0;
		while ((-1990677291 * ((Class278_Sub1) this).aClass282_Sub35_8116.index) < (((Class278_Sub1) this).aClass282_Sub35_8116.buffer).length) {
			if (null == (((Class278_Sub1) this).aClass282_Sub35_8112.buffer)) {
				if (null == ((Class278_Sub1) this).aByteArrayArray8111[0]) {
					((Class278_Sub1) this).aClass282_Sub35_8116.buffer = null;
					return ((((Class278_Sub1) this).aClass282_Sub35_8116.index) * -1990677291);
				}
				((Class278_Sub1) this).aClass282_Sub35_8112.buffer = ((Class278_Sub1) this).aByteArrayArray8111[0];
			}
			int i_8_ = ((((Class278_Sub1) this).aClass282_Sub35_8116.buffer).length - -1990677291 * (((Class278_Sub1) this).aClass282_Sub35_8116.index));
			int i_9_ = ((((Class278_Sub1) this).aClass282_Sub35_8112.buffer).length - -1990677291 * (((Class278_Sub1) this).aClass282_Sub35_8112.index));
			if (i_8_ < i_9_) {
				((Class278_Sub1) this).aClass282_Sub35_8112.readBytes((((Class278_Sub1) this).aClass282_Sub35_8116.buffer), (((Class278_Sub1) this).aClass282_Sub35_8116.index * -1990677291), i_8_, 437167462);
				((Class278_Sub1) this).aClass282_Sub35_8116.buffer = null;
				return is.length;
			}
			((Class278_Sub1) this).aClass282_Sub35_8116.writeBytes(((Class278_Sub1) this).aClass282_Sub35_8112.buffer, (((Class278_Sub1) this).aClass282_Sub35_8112.index * -1990677291), i_9_);
			((Class278_Sub1) this).aClass282_Sub35_8112.buffer = null;
			((Class278_Sub1) this).aClass282_Sub35_8112.index = 0;
			((Class278_Sub1) this).anInt8115 += -603678457;
			for (int i_10_ = 0; i_10_ < 9; i_10_++)
				((Class278_Sub1) this).aByteArrayArray8111[i_10_] = ((Class278_Sub1) this).aByteArrayArray8111[1 + i_10_];
			((Class278_Sub1) this).aByteArrayArray8111[9] = null;
			if (((Class278_Sub1) this).anInt8115 * -285637449 >= ((Class278_Sub1) this).anIntArray8114.length) {
				((Class278_Sub1) this).aClass282_Sub35_8116.buffer = null;
				return -1990677291 * (((Class278_Sub1) this).aClass282_Sub35_8116.index);
			}
		}
		((Class278_Sub1) this).aClass282_Sub35_8116.buffer = null;
		return is.length;
	}

	public void method13447(int i) {
		if (null != ((Class278_Sub1) this).anIntArray8114) {
			for (int i_11_ = 0; (i_11_ < 10 && (i_11_ + ((Class278_Sub1) this).anInt8115 * -285637449 < ((Class278_Sub1) this).anIntArray8114.length)); i_11_++) {
				if (((Class278_Sub1) this).aByteArrayArray8111[i_11_] == null && (((Class278_Sub1) this).aClass317_8117.method5688((((Class278_Sub1) this).anIntArray8114[(i_11_ + ((Class278_Sub1) this).anInt8115 * -285637449)]), 0, 16711935)))
					((Class278_Sub1) this).aByteArrayArray8111[i_11_] = (((Class278_Sub1) this).aClass317_8117.method5607((((Class278_Sub1) this).anIntArray8114[(-285637449 * ((Class278_Sub1) this).anInt8115 + i_11_)]), 0, -1550945086));
			}
		}
	}

	static void method13448(Class118 class118, int i, int i_12_, int i_13_) {
		Class119 class119 = class118.method2046(Class316.aClass505_3680, -1375447309);
		if (class119 != null) {
			Class316.aClass505_3680.r(i, i_12_, i + 1506818197 * class118.anInt1301, i_12_ + class118.anInt1429 * -492594917);
			if (Class187.anInt2363 * -1221526793 < 3) {
				int i_14_ = (int) -client.aFloat7365;
				i_14_ = i_14_ + 714818342 * client.anInt7255 & 0x3fff;
				i_14_ <<= 2;
				Class16.aClass160_146.method2761(((float) (class118.anInt1301 * 1506818197) / 2.0F + (float) i), ((float) (-492594917 * class118.anInt1429) / 2.0F + (float) i_12_), 4141, i_14_, class119.aClass455_1456, i, i_12_);
			} else
				Class316.aClass505_3680.DA(-16777216, class119.aClass455_1456, i, i_12_);
		}
	}

	public static void method13449(byte i) {
		if (Class320.aClass253_3723 != null)
			Class320.aClass253_3723.method4335();
		if (null != Class100.aClass253_1008)
			Class100.aClass253_1008.method4335();
	}

	static final void method13450(Class118 class118, Class98 class98, Class527 class527, int i) {
		int i_15_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		class118.aBool1357 = 1 == i_15_;
		Class109.method1858(class118, (byte) 41);
	}
}
