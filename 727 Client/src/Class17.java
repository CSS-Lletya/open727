/* Class17 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

final class Class17 implements Interface25 {
	public static int anInt148;

	public boolean method182(Class521_Sub1_Sub1 class521_sub1_sub1) {
		return class521_sub1_sub1 instanceof Interface12;
	}

	public boolean method183(Class521_Sub1_Sub1 class521_sub1_sub1, int i) {
		return class521_sub1_sub1 instanceof Interface12;
	}

	static final void method569(Class118 class118, Class98 class98, Class527 class527, int i) {
		class118.anInt1362 = ((((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]) * -310295877);
		Class109.method1858(class118, (byte) 60);
	}

	static final void method570(int i) {
		int i_0_ = 0;
		Class206 class206 = client.aClass257_7353.method4430(-1483143416);
		for (int i_1_ = 0; i_1_ < client.aClass257_7353.method4424(1126202391); i_1_++) {
			for (int i_2_ = 0; i_2_ < client.aClass257_7353.method4451(-1126245004); i_2_++) {
				if (Class309.method5492(class206.aClass293ArrayArrayArray2604, i_0_, i_1_, i_2_, true, (byte) 56))
					i_0_++;
				if (i_0_ >= 512)
					return;
			}
		}
	}
}
