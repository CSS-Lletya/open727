
/* Class383 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Dimension;

public class Class383 implements Interface41 {
	String aString4662;
	Class317 aClass317_4663;
	static int anInt4664;
	public static Class317 aClass317_4665;

	public Class445 method234() {
		return Class445.aClass445_5380;
	}

	public int method84(int i) {
		if (((Class383) this).aClass317_4663.method5628(((Class383) this).aString4662, 433021340))
			return 100;
		return 0;
	}

	Class383(Class317 class317, String string) {
		((Class383) this).aClass317_4663 = class317;
		((Class383) this).aString4662 = string;
	}

	public int method231() {
		if (((Class383) this).aClass317_4663.method5628(((Class383) this).aString4662, 433021340))
			return 100;
		return 0;
	}

	public Class445 method235() {
		return Class445.aClass445_5380;
	}

	public Class445 method233() {
		return Class445.aClass445_5380;
	}

	public Class445 method230() {
		return Class445.aClass445_5380;
	}

	public Class445 method232(byte i) {
		return Class445.aClass445_5380;
	}

	public static Class268 method6509(int i, int i_0_, int i_1_, int i_2_, int i_3_, int i_4_, byte i_5_) {
		if (Class393.aClass282_Sub54_4783.aClass468_Sub13_8225.method12714(-784920848) != 0 && 0 != i_0_ && Class260.anInt3219 * -458827259 < 50 && i != -1) {
			Class268 class268 = new Class268((byte) 1, i, i_0_, i_1_, i_2_, i_3_, i_4_, null);
			Class260.aClass268Array3232[(Class260.anInt3219 += -1221598515) * -458827259 - 1] = class268;
			return class268;
		}
		return null;
	}

	static final void method6510(Class527 class527, byte i) {
		Class513 class513 = (((Class527) class527).aBool7022 ? ((Class527) class527).aClass513_6994 : ((Class527) class527).aClass513_7007);
		Class118 class118 = ((Class513) class513).aClass118_5886;
		Class98 class98 = ((Class513) class513).aClass98_5885;
		Class82.method1455(class118, class98, class527, -1784890209);
	}

	public static void method6511(int i) {
		for (Class282_Sub37 class282_sub37 = ((Class282_Sub37) Class492.aClass465_5774.method7750(76127195)); class282_sub37 != null; class282_sub37 = (Class282_Sub37) Class492.aClass465_5774.method7751((byte) 48)) {
			if (!((Class282_Sub37) class282_sub37).aBool7996)
				Class92.method1563(1729403683 * ((Class282_Sub37) class282_sub37).anInt7999, 1917715893);
			else
				((Class282_Sub37) class282_sub37).aBool7996 = false;
		}
	}

	static Class350 method6512(RsByteBuffer class282_sub35, int i) {
		Class356 class356 = (Class350_Sub3_Sub1.method15558(-535190719)[class282_sub35.readUnsignedByte()]);
		Class353 class353 = (Class483.method8155(152314627)[class282_sub35.readUnsignedByte()]);
		int i_6_ = class282_sub35.method13081(1882805883);
		int i_7_ = class282_sub35.method13081(1909994135);
		int i_8_ = class282_sub35.readUnsignedShort();
		int i_9_ = class282_sub35.readUnsignedShort();
		int i_10_ = class282_sub35.method13081(2095051682);
		int i_11_ = class282_sub35.readBigSmart(1942978390);
		int i_12_ = class282_sub35.readIntLE();
		return new Class350(class356, class353, i_6_, i_7_, i_8_, i_9_, i_10_, i_11_, i_12_);
	}

	public static int method6513(int i, int i_13_, int i_14_) {
		if (-1 == Class58.aClass529_527.anInt7034 * -1878247413)
			return 1;
		if (i != Class393.aClass282_Sub54_4783.aClass468_Sub18_8230.method12776(-408744518)) {
			Class231.method3914(i, Class433.aClass433_5252.method7273(Class223.aClass495_2772, -1872391356), true, (byte) 110);
			if (Class393.aClass282_Sub54_4783.aClass468_Sub18_8230.method12776(-455278899) != i)
				return -1;
		}
		int i_15_;
		try {
			Dimension dimension = Class351.aCanvas4096.getSize();
			Class446.method7447(Class433.aClass433_5252.method7273(Class223.aClass495_2772, -1923061831), true, Class316.aClass505_3680, Class16.aClass8_144, Class16.aClass414_139, (byte) -8);
			Class157 class157 = Class157.method2689(Class110.aClass317_1106, (Class58.aClass529_527.anInt7034 * -1878247413), 0);
			long l = Class169.method2869(2130587013);
			Class316.aClass505_3680.L();
			client.aClass294_7457.method5223(0.0F, 256.0F, 0.0F);
			Class316.aClass505_3680.method8457(client.aClass294_7457);
			Class384 class384 = Class316.aClass505_3680.method8449();
			class384.method6531((float) (dimension.width / 2), (float) (dimension.height / 2), 512.0F, 512.0F, (float) client.aClass257_7353.method4544(1468645069), (float) client.aClass257_7353.method4522(-1126120958), (float) dimension.width, (float) dimension.height);
			Class316.aClass505_3680.method8424(class384);
			Class316.aClass505_3680.IA(1.0F);
			Class316.aClass505_3680.m(16777215, 0.5F, 0.5F, 20.0F, -50.0F, 30.0F);
			Class528 class528 = Class316.aClass505_3680.method8451(class157, 2048, 64, 64, 768);
			int i_16_ = 0;
			while_185_: for (int i_17_ = 0; i_17_ < 500; i_17_++) {
				Class316.aClass505_3680.ba(3, 0);
				for (int i_18_ = 15; i_18_ >= 0; i_18_--) {
					for (int i_19_ = 0; i_19_ <= i_18_; i_19_++) {
						client.aClass294_7169.method5223((float) (int) (512.0F * ((float) i_19_ - (float) i_18_ / 2.0F)), 0.0F, (float) ((i_18_ + 1) * 512));
						class528.method11282(client.aClass294_7169, null, 0);
						i_16_++;
						if (Class169.method2869(1699271863) - l >= (long) i_13_)
							break while_185_;
					}
				}
			}
			Class316.aClass505_3680.method8395();
			long l_20_ = ((long) (1000 * i_16_) / (Class169.method2869(1634298146) - l));
			Class316.aClass505_3680.ba(3, 0);
			i_15_ = (int) l_20_;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return -1;
		}
		return i_15_;
	}

	static final void method6514(int i, int i_21_, int i_22_) {
		if (Class456_Sub3.method12682(i, null, -1336109157))
			Class151.method2591((Class468_Sub8.aClass98Array7889[i].aClass118Array998), i_21_, (byte) -17);
	}

	public static String method6515(CharSequence charsequence, int i) {
		return Class222.method3754(charsequence, null, -198017552);
	}
}
