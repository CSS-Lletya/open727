
/* Class353 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Class353 {
	static Class353 aClass353_4105;
	static Class353 aClass353_4106;
	static Class353 aClass353_4107 = new Class353();

	public int method6198(int i, int i_0_, int i_1_) {
		int i_2_ = (-969250379 * Class263.anInt3243 > i_0_ ? Class263.anInt3243 * -969250379 : i_0_);
		if (aClass353_4107 == this)
			return 0;
		if (aClass353_4106 == this)
			return i_2_ - i;
		if (aClass353_4105 == this)
			return (i_2_ - i) / 2;
		return 0;
	}

	public int method6199(int i, int i_3_) {
		int i_4_ = (-969250379 * Class263.anInt3243 > i_3_ ? Class263.anInt3243 * -969250379 : i_3_);
		if (aClass353_4107 == this)
			return 0;
		if (aClass353_4106 == this)
			return i_4_ - i;
		if (aClass353_4105 == this)
			return (i_4_ - i) / 2;
		return 0;
	}

	static {
		aClass353_4105 = new Class353();
		aClass353_4106 = new Class353();
	}

	public static Class353[] method6200() {
		return (new Class353[] { aClass353_4107, aClass353_4105, aClass353_4106 });
	}

	public static Class353[] method6201() {
		return (new Class353[] { aClass353_4107, aClass353_4105, aClass353_4106 });
	}

	Class353() {
		/* empty */
	}

	public static Class353[] method6202() {
		return (new Class353[] { aClass353_4107, aClass353_4105, aClass353_4106 });
	}

	public int method6203(int i, int i_5_) {
		int i_6_ = (-969250379 * Class263.anInt3243 > i_5_ ? Class263.anInt3243 * -969250379 : i_5_);
		if (aClass353_4107 == this)
			return 0;
		if (aClass353_4106 == this)
			return i_6_ - i;
		if (aClass353_4105 == this)
			return (i_6_ - i) / 2;
		return 0;
	}

	public int method6204(int i, int i_7_) {
		int i_8_ = (-969250379 * Class263.anInt3243 > i_7_ ? Class263.anInt3243 * -969250379 : i_7_);
		if (aClass353_4107 == this)
			return 0;
		if (aClass353_4106 == this)
			return i_8_ - i;
		if (aClass353_4105 == this)
			return (i_8_ - i) / 2;
		return 0;
	}

	static final void method6205(Class527 class527, byte i) {
		((Class527) class527).anIntArray6999[1942118537 * ((Class527) class527).anInt7012 - 1] = (((Class527) class527).aClass61_7010.method1215((byte) 69)[(((Class527) class527).anIntArray6999[1942118537 * ((Class527) class527).anInt7012 - 1])]);
	}

	static final void method6206(Class527 class527, int i) {
		int i_9_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = client.aClass330Array7428[i_9_].anInt3867 * 1545587551;
	}

	static final void method6207(Class527 class527, byte i) {
		int i_10_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = (int) (Math.random() * (double) i_10_);
	}

	static void method6208(File file, byte[] is, int i, int i_11_) throws IOException {
		DataInputStream datainputstream = (new DataInputStream(new BufferedInputStream(new FileInputStream(file))));
		try {
			datainputstream.readFully(is, 0, i);
		} catch (EOFException eofexception) {
			/* empty */
		}
		datainputstream.close();
	}

	static void method6209(int i, int i_12_, int i_13_, Class282_Sub34 class282_sub34, int i_14_) {
		long l = (long) (i << 28 | i_13_ << 14 | i_12_);
		Class282_Sub29 class282_sub29 = (Class282_Sub29) client.aClass465_7414.method7754(l);
		if (null == class282_sub29) {
			class282_sub29 = new Class282_Sub29();
			client.aClass465_7414.method7765(class282_sub29, l);
			class282_sub29.aClass482_7708.method8059(class282_sub34, 64810777);
		} else {
			Class425 class425 = Class119.aClass426_1463.method7145((class282_sub34.anInt7853 * 1964309863), 1474868024);
			int i_15_ = 15210351 * class425.anInt5051;
			if (318481945 * class425.anInt5049 == 1)
				i_15_ *= 1 + (((Class282_Sub34) class282_sub34).anInt7852 * -1727049729);
			for (Class282_Sub34 class282_sub34_16_ = ((Class282_Sub34) class282_sub29.aClass482_7708.method8097((byte) 33)); null != class282_sub34_16_; class282_sub34_16_ = (Class282_Sub34) class282_sub29.aClass482_7708.method8067(1444311005)) {
				class425 = Class119.aClass426_1463.method7145((1964309863 * (class282_sub34_16_.anInt7853)), 1449282948);
				int i_17_ = class425.anInt5051 * 15210351;
				if (class425.anInt5049 * 318481945 == 1)
					i_17_ *= -1727049729 * (((Class282_Sub34) class282_sub34_16_).anInt7852) + 1;
				if (i_15_ > i_17_) {
					Class446.method7430(class282_sub34, class282_sub34_16_, -1282402285);
					return;
				}
			}
			class282_sub29.aClass482_7708.method8059(class282_sub34, -1833227399);
		}
	}

	static final void method6210(Class527 class527, int i) {
		Class513 class513 = (((Class527) class527).aBool7022 ? ((Class527) class527).aClass513_6994 : ((Class527) class527).aClass513_7007);
		Class118 class118 = ((Class513) class513).aClass118_5886;
		Class98 class98 = ((Class513) class513).aClass98_5885;
		Class29.method789(class118, class98, class527, -19478635);
	}

	static final void method6211(Class527 class527, int i) {
		((Class527) class527).anInt7000 -= -1341717846;
		String string = (String) (((Class527) class527).anObjectArray7019[((Class527) class527).anInt7000 * 1806726141]);
		String string_18_ = (String) (((Class527) class527).anObjectArray7019[((Class527) class527).anInt7000 * 1806726141 + 1]);
		((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 += 1476624725) * 1806726141 - 1] = new StringBuilder().append(string).append(string_18_).toString();
	}
}
