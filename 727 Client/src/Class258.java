
/* Class258 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.util.Iterator;

public class Class258 {
	public static final int anInt3208 = 6;
	public static final int anInt3209 = 0;
	public static final int anInt3210 = 1;
	public static final int anInt3211 = 2;
	public static final int anInt3212 = 5;

	Class258() throws Throwable {
		throw new Error();
	}

	static final void method4565(Class527 class527, int i) {
		Class513 class513 = (((Class527) class527).aBool7022 ? ((Class527) class527).aClass513_6994 : ((Class527) class527).aClass513_7007);
		Class118 class118 = ((Class513) class513).aClass118_5886;
		if (-1 != -56249735 * class118.anInt1426)
			((Class527) class527).anIntArray6999[((((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1)] = 6040081 * class118.anInt1427;
		else
			((Class527) class527).anIntArray6999[((((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1)] = 0;
	}

	static final void method4566(Class527 class527, int i) {
		int i_0_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		if (null != client.aString7426 && i_0_ < Class459.anInt5534 * -1772444859)
			((Class527) class527).anObjectArray7019[((((Class527) class527).anInt7000 += 1476624725) * 1806726141 - 1)] = Class467.aClass173Array5575[i_0_].aString2127;
		else
			((Class527) class527).anObjectArray7019[((((Class527) class527).anInt7000 += 1476624725) * 1806726141 - 1)] = "";
	}

	static final void method4567(Class527 class527, short i) {
		((Class527) class527).anInt7012 -= 283782002;
		int i_1_ = (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537]);
		int i_2_ = (((Class527) class527).anIntArray6999[1942118537 * ((Class527) class527).anInt7012 + 1]);
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = i_1_ > i_2_ ? i_1_ : i_2_;
	}

	static final void method4568(Class527 class527, int i) {
		((Class527) class527).anInt7000 -= -1341717846;
		((Class527) class527).anInt7012 -= 283782002;
		Class119.method2074((String) (((Class527) class527).anObjectArray7019[((Class527) class527).anInt7000 * 1806726141]), (String) (((Class527) class527).anObjectArray7019[1 + 1806726141 * ((Class527) class527).anInt7000]), (((Class527) class527).anIntArray6999[1942118537 * ((Class527) class527).anInt7012]), 1 == (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 + 1]), (byte) 55);
	}

	static final void method4569(int i) {
		Iterator iterator = client.aClass465_7334.iterator();
		while (iterator.hasNext()) {
			Class282_Sub50_Sub10 class282_sub50_sub10 = (Class282_Sub50_Sub10) iterator.next();
			Class521_Sub1_Sub1_Sub4 class521_sub1_sub1_sub4 = (((Class282_Sub50_Sub10) class282_sub50_sub10).aClass521_Sub1_Sub1_Sub4_9636);
			if (client.aClass257_7353.method4430(-831174311) != null && class521_sub1_sub1_sub4.method15927(-831489293))
				client.aClass257_7353.method4430(-1442376239).method3397(class521_sub1_sub1_sub4, true, 402306072);
		}
	}

	public static Class357 method4570(RsByteBuffer class282_sub35, int i) {
		int i_3_ = class282_sub35.readBigSmart(2126295637);
		Class356 class356 = (Class350_Sub3_Sub1.method15558(251815760)[class282_sub35.readUnsignedByte()]);
		Class353 class353 = (Class483.method8155(152314627)[class282_sub35.readUnsignedByte()]);
		int i_4_ = class282_sub35.method13081(1666577229);
		int i_5_ = class282_sub35.method13081(1649901104);
		return new Class357(i_3_, class356, class353, i_4_, i_5_);
	}
}
