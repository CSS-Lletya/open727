/* Class97 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public final class Class97 {
	static long aLong993;
	static long aLong994;
	public static Class427 aClass427_995;
	protected static Class440[] aClass440Array996;

	public static final synchronized long method1610() {
		long l = System.currentTimeMillis();
		if (l < 6289562171134420055L * aLong993)
			aLong994 += -8305020634878961101L * (6289562171134420055L * aLong993 - l);
		aLong993 = l * -3280623870577899673L;
		return l + -3197815037203812101L * aLong994;
	}

	public static final synchronized long method1611() {
		long l = System.currentTimeMillis();
		if (l < 6289562171134420055L * aLong993)
			aLong994 += -8305020634878961101L * (6289562171134420055L * aLong993 - l);
		aLong993 = l * -3280623870577899673L;
		return l + -3197815037203812101L * aLong994;
	}

	Class97() throws Throwable {
		throw new Error();
	}

	public static boolean method1612(int i, int i_0_) {
		return (i == 5 || 11 == i || i == 14 || i == 19 || 3 == i || i == 8 || 9 == i);
	}

	static final void method1613(Class527 class527, int i) {
		((Class527) class527).anInt7012 -= 567564004;
		int i_1_ = (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537]);
		int i_2_ = (((Class527) class527).anIntArray6999[1942118537 * ((Class527) class527).anInt7012 + 1]);
		int i_3_ = (((Class527) class527).anIntArray6999[1942118537 * ((Class527) class527).anInt7012 + 2]);
		int i_4_ = (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 + 3]);
		i_1_ += i_2_ << 14;
		i_1_ += i_3_ << 28;
		i_1_ += i_4_;
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = i_1_;
	}

	static Class222 method1614(RsByteBuffer class282_sub35, int i) {
		int i_5_ = class282_sub35.readUnsignedByte();
		int i_6_ = class282_sub35.readUnsignedByte();
		return new Class222(i_5_, i_6_);
	}
}
