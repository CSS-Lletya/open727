/* Class445 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class445 {
	public static Class445 aClass445_5380;
	public static Class445 aClass445_5381 = new Class445();
	public static Class445 aClass445_5382;
	public static Class445 aClass445_5383;
	static String aString5384;

	Class445() {
		/* empty */
	}

	static {
		aClass445_5380 = new Class445();
		aClass445_5382 = new Class445();
		aClass445_5383 = new Class445();
	}

	static final void method7428(Class527 class527, byte i) {
		int i_0_ = (((Class527) class527).anIntArray7018[301123709 * ((Class527) class527).anInt7020]);
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = ((Class521_Sub1_Sub1_Sub2_Sub1) ((Class527) class527).aClass521_Sub1_Sub1_Sub2_7006).aClass155_10561.method2626(i_0_, (byte) 56);
	}

	static final void method7429(Class118 class118, int i, byte[] is, byte[] is_1_, Class527 class527, int i_2_) {
		if (class118.aByteArrayArray1366 == null) {
			if (null != is) {
				class118.aByteArrayArray1366 = new byte[11][];
				class118.aByteArrayArray1367 = new byte[11][];
				class118.anIntArray1395 = new int[11];
				class118.anIntArray1267 = new int[11];
			} else
				return;
		}
		class118.aByteArrayArray1366[i] = is;
		if (null != is)
			class118.aBool1424 = true;
		else {
			class118.aBool1424 = false;
			for (int i_3_ = 0; i_3_ < class118.aByteArrayArray1366.length; i_3_++) {
				if (class118.aByteArrayArray1366[i_3_] != null || class118.anIntArray1267[i_3_] > 0) {
					class118.aBool1424 = true;
					break;
				}
			}
		}
		class118.aByteArrayArray1367[i] = is_1_;
	}
}
