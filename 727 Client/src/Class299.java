
/* Class299 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

public class Class299 implements Interface27 {
	Interface2[] anInterface2Array3539;
	int anInt3540;
	Class393 aClass393_3541;
	Class505 aClass505_3542;
	static byte[] aByteArray3543 = { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0, -5, 127, -29, -1, 109, 6, 103, 6, 14, 54, 54, 118, 54, 86, 14, 118, 118, 118, 78, 78, 14, 46, 30, 17, 94, 30, 110, 110, 30, 73, 33, 97, 126, 17, 89, 41, 121, 57, 89, 41, 25, 25, 5, 21, 61, 117, 5, 37, 29, 101, 25, 25, 13, 115, 77, 29, 3, 67, 19, 19, 19, 121, 117, 75, 91, 11, 35, 27, 61, 99, 19, 35, -112, 33, -116, -100, -100, -100, 60, -36, 60, 18, -68, -68, 18, 70, -118, 50, -118, 70, 36, -125, -1, 7, 24, 68, 56, 24, 24, 25, 24, 89, 24, -107, 24, -104, 4, 25, -103, 5, 25, 89, -108, 24, -2, 31, 97, -112, 103, 96, 96, 100, 101, 4, 3, 6, 40, 96, 100, 98, 102, 97, 101, 99, -25, -32, -28, -30, 6, 42, -40, 42, -64, -64, -60, -56, -52, -52, -60, -62, -52, -54, -54, -62, 2, -108, -83, 5, -54, 51, -80, 8, -78, 10, 41, 26, 58, -78, 9, 7, 38, -78, 43, 21, -118, 24, 53, 78, 92, -56, -95, -20, -76, -15, -96, 104, -48, -59, 15, 42, -58, 73, 69, 77, -100, 92, 98, -30, 18, -110, 82, -86, 106, -22, 26, -102, 90, 38, -90, 102, -26, 22, -106, 86, -50, 46, -82, 110, -18, 30, -98, 94, -63, 33, -95, 97, -31, 17, -111, 81, -55, 41, -87, 105, -23, 25, -103, 89, -59, 37, -91, 101, -27, 21, -107, 85, -51, 45, -83, 109, -19, 29, -99, 93, -109, 38, 79, -103, 58, 109, -6, -116, -103, -77, 22, 45, 94, -78, 116, -39, -14, 21, 43, 87, 109, -38, -68, 101, -21, -74, -19, 59, 118, -18, 58, 116, -8, -56, -47, 99, -57, 79, -100, 60, 117, -23, -14, -107, -85, -41, -82, -33, -72, 121, -21, -31, -93, -57, 79, -98, 62, 123, -2, -30, -27, -85, -113, -97, 62, 127, -7, -6, -19, -5, -113, -97, -65, 64, -2, 98, 100, 96, 102, -124, 1, -84, -2, 18, 4, -6, -117, -119, -123, -123, -103, -123, 29, -28, 47, 70, -90, 114, -112, 2, 65, 22, 86, 69, 67, 54, 33, -57, 64, -10, -60, 66, 97, 37, -93, 70, 14, 17, -89, -119, 11, 55, 30, -28, 84, 54, 14, -6, 32, -102, 84, 116, -111, 75, 76, -59, -28, -95, -22, 71, -112, -41, -64, 62, 35, -50, 99, 77, 100, -7, 12, -18, 49, -124, -65, 110, 49, -16, -79, 48, 2, -29, 15, -24, 78, 6, 6, 123, -122, -97, -1, -70, 52, 22, -16, -49, 127, -79, -88, -21, -1, 77, 0, 38, 27, -28, 10, 110, 2, 0, 0 };
	int anInt3544;
	static Class229 aClass229_3545 = new Class229(128, 4);
	boolean aBool3546;

	Class299(Class393 class393, int i, int i_0_, Class29 class29) {
		((Class299) this).aClass393_3541 = class393;
		((Class299) this).anInt3540 = i * -239238277;
		((Class299) this).anInt3544 = 1396538451 * i_0_;
		((Class299) this).anInterface2Array3539 = new Interface2[(((Class299) this).aClass393_3541.anInterface3Array4782).length];
		for (int i_1_ = 0; i_1_ < ((Class299) this).anInterface2Array3539.length; i_1_++)
			((Class299) this).anInterface2Array3539[i_1_] = class29.method781((((Class299) this).aClass393_3541.anInterface3Array4782[i_1_]), -1285211063);
	}

	public void method161(int i) {
		if (Class316.aClass505_3680 != ((Class299) this).aClass505_3542) {
			((Class299) this).aClass505_3542 = Class316.aClass505_3680;
			((Class299) this).aBool3546 = true;
		}
		((Class299) this).aClass505_3542.ba(3, 0);
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i_2_ = 0; i_2_ < interface2s.length; i_2_++) {
			Interface2 interface2 = interface2s[i_2_];
			if (interface2 != null)
				interface2.method22(1273546148);
		}
	}

	public void method187(boolean bool, int i) {
		bool = true;
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i_3_ = 0; i_3_ < interface2s.length; i_3_++) {
			Interface2 interface2 = interface2s[i_3_];
			if (null != interface2)
				interface2.method20(bool || ((Class299) this).aBool3546, -150787532);
		}
		((Class299) this).aBool3546 = false;
	}

	public int method71() {
		return -1024317477 * ((Class299) this).anInt3544;
	}

	public void method188(byte i) {
		/* empty */
	}

	public boolean method189(long l) {
		return (Class169.method2869(1518246840) >= l + (long) (((Class299) this).anInt3540 * 2055594931));
	}

	public int method190(int i) {
		return -1024317477 * ((Class299) this).anInt3544;
	}

	public void x(boolean bool) {
		bool = true;
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i = 0; i < interface2s.length; i++) {
			Interface2 interface2 = interface2s[i];
			if (null != interface2)
				interface2.method20(bool || ((Class299) this).aBool3546, -1868763759);
		}
		((Class299) this).aBool3546 = false;
	}

	public void method194(boolean bool) {
		bool = true;
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i = 0; i < interface2s.length; i++) {
			Interface2 interface2 = interface2s[i];
			if (null != interface2)
				interface2.method20(bool || ((Class299) this).aBool3546, 91368365);
		}
		((Class299) this).aBool3546 = false;
	}

	public void method157() {
		/* empty */
	}

	public void method186() {
		/* empty */
	}

	public int method191(int i) {
		int i_4_ = 0;
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i_5_ = 0; i_5_ < interface2s.length; i_5_++) {
			Interface2 interface2 = interface2s[i_5_];
			if (interface2 == null || interface2.method10(1908739146))
				i_4_++;
		}
		return i_4_ * 100 / ((Class299) this).anInterface2Array3539.length;
	}

	public void method158() {
		if (Class316.aClass505_3680 != ((Class299) this).aClass505_3542) {
			((Class299) this).aClass505_3542 = Class316.aClass505_3680;
			((Class299) this).aBool3546 = true;
		}
		((Class299) this).aClass505_3542.ba(3, 0);
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i = 0; i < interface2s.length; i++) {
			Interface2 interface2 = interface2s[i];
			if (interface2 != null)
				interface2.method22(1273546148);
		}
	}

	public void method159() {
		if (Class316.aClass505_3680 != ((Class299) this).aClass505_3542) {
			((Class299) this).aClass505_3542 = Class316.aClass505_3680;
			((Class299) this).aBool3546 = true;
		}
		((Class299) this).aClass505_3542.ba(3, 0);
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i = 0; i < interface2s.length; i++) {
			Interface2 interface2 = interface2s[i];
			if (interface2 != null)
				interface2.method22(1273546148);
		}
	}

	public void method195() {
		if (Class316.aClass505_3680 != ((Class299) this).aClass505_3542) {
			((Class299) this).aClass505_3542 = Class316.aClass505_3680;
			((Class299) this).aBool3546 = true;
		}
		((Class299) this).aClass505_3542.ba(3, 0);
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i = 0; i < interface2s.length; i++) {
			Interface2 interface2 = interface2s[i];
			if (interface2 != null)
				interface2.method22(1273546148);
		}
	}

	public int method196() {
		int i = 0;
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i_6_ = 0; i_6_ < interface2s.length; i_6_++) {
			Interface2 interface2 = interface2s[i_6_];
			if (interface2 == null || interface2.method10(1170991378))
				i++;
		}
		return i * 100 / ((Class299) this).anInterface2Array3539.length;
	}

	static void method5305() {
		aClass229_3545.method3859(-1088836697);
	}

	public int method198() {
		int i = 0;
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i_7_ = 0; i_7_ < interface2s.length; i_7_++) {
			Interface2 interface2 = interface2s[i_7_];
			if (interface2 == null || interface2.method10(739049801))
				i++;
		}
		return i * 100 / ((Class299) this).anInterface2Array3539.length;
	}

	public boolean method193(long l) {
		return (Class169.method2869(1596220251) >= l + (long) (((Class299) this).anInt3540 * 2055594931));
	}

	static boolean method5306() {
		boolean bool;
		try {
			Class395 class395 = new Class395();
			byte[] is = class395.method6764(aByteArray3543, (byte) -6);
			Class103_Sub1.method14490(is, (byte) -83);
			bool = true;
		} catch (Exception exception) {
			return false;
		}
		return bool;
	}

	public int method197() {
		int i = 0;
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i_8_ = 0; i_8_ < interface2s.length; i_8_++) {
			Interface2 interface2 = interface2s[i_8_];
			if (interface2 == null || interface2.method10(1731347065))
				i++;
		}
		return i * 100 / ((Class299) this).anInterface2Array3539.length;
	}

	static boolean method5307() {
		boolean bool;
		try {
			Class395 class395 = new Class395();
			byte[] is = class395.method6764(aByteArray3543, (byte) -54);
			Class103_Sub1.method14490(is, (byte) -57);
			bool = true;
		} catch (Exception exception) {
			return false;
		}
		return bool;
	}

	public static Class160 method5308(Class317 class317, int i) {
		Class160 class160 = (Class160) aClass229_3545.method3865((long) i);
		if (class160 == null) {
			if (Class339.aBool3987)
				class160 = Class316.aClass505_3680.method8444(Class91.method1515(class317, i), true);
			else
				class160 = (Class103_Sub1.method14490(class317.method5615(i, -1425472520), (byte) -78));
			aClass229_3545.method3856(class160, (long) i);
		}
		return class160;
	}

	public static Class160 method5309(Class317 class317, int i) {
		Class160 class160 = (Class160) aClass229_3545.method3865((long) i);
		if (class160 == null) {
			if (Class339.aBool3987)
				class160 = Class316.aClass505_3680.method8444(Class91.method1515(class317, i), true);
			else
				class160 = (Class103_Sub1.method14490(class317.method5615(i, -2080551630), (byte) -127));
			aClass229_3545.method3856(class160, (long) i);
		}
		return class160;
	}

	static Class160 method5310(byte[] is) {
		if (is == null)
			throw new RuntimeException("");
		Class160 class160;
		for (;;) {
			try {
				Image image = Toolkit.getDefaultToolkit().createImage(is);
				MediaTracker mediatracker = new MediaTracker(Class282_Sub44.anApplet8065);
				mediatracker.addImage(image, 0);
				mediatracker.waitForAll();
				int i = image.getWidth(Class282_Sub44.anApplet8065);
				int i_9_ = image.getHeight(Class282_Sub44.anApplet8065);
				if (mediatracker.isErrorAny() || i < 0 || i_9_ < 0)
					throw new RuntimeException("");
				int[] is_10_ = new int[i * i_9_];
				PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, i, i_9_, is_10_, 0, i);
				pixelgrabber.grabPixels();
				class160 = Class316.aClass505_3680.method8549(is_10_, 0, i, i, i_9_, 877342890);
				break;
			} catch (InterruptedException interruptedexception) {
				/* empty */
			}
		}
		return class160;
	}

	public int method72() {
		return -1024317477 * ((Class299) this).anInt3544;
	}

	static Class160 method5311(byte[] is) {
		if (is == null)
			throw new RuntimeException("");
		Class160 class160;
		for (;;) {
			try {
				Image image = Toolkit.getDefaultToolkit().createImage(is);
				MediaTracker mediatracker = new MediaTracker(Class282_Sub44.anApplet8065);
				mediatracker.addImage(image, 0);
				mediatracker.waitForAll();
				int i = image.getWidth(Class282_Sub44.anApplet8065);
				int i_11_ = image.getHeight(Class282_Sub44.anApplet8065);
				if (mediatracker.isErrorAny() || i < 0 || i_11_ < 0)
					throw new RuntimeException("");
				int[] is_12_ = new int[i * i_11_];
				PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, i, i_11_, is_12_, 0, i);
				pixelgrabber.grabPixels();
				class160 = Class316.aClass505_3680.method8549(is_12_, 0, i, i, i_11_, 2138062190);
				break;
			} catch (InterruptedException interruptedexception) {
				/* empty */
			}
		}
		return class160;
	}

	public void method192(boolean bool) {
		bool = true;
		Interface2[] interface2s = ((Class299) this).anInterface2Array3539;
		for (int i = 0; i < interface2s.length; i++) {
			Interface2 interface2 = interface2s[i];
			if (null != interface2)
				interface2.method20(bool || ((Class299) this).aBool3546, 1993030646);
		}
		((Class299) this).aBool3546 = false;
	}

	static final void method5312(Class527 class527, int i) {
		Class465 class465 = (((Class527) class527).aClass282_Sub50_Sub5_7021.aClass465Array9531[(((Class527) class527).anIntArray7018[((Class527) class527).anInt7020 * 301123709])]);
		Class282_Sub38 class282_sub38 = ((Class282_Sub38) class465.method7754((long) ((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]));
		if (null != class282_sub38)
			((Class527) class527).anInt7020 += class282_sub38.anInt8002 * 750266701;
	}

	public static void method5313(int i, int i_13_, int i_14_, int i_15_, int i_16_, int i_17_, int i_18_, int i_19_) {
		Class282_Sub31 class282_sub31 = null;
		for (Class282_Sub31 class282_sub31_20_ = ((Class282_Sub31) Class282_Sub31.aClass482_7775.method8097((byte) 122)); null != class282_sub31_20_; class282_sub31_20_ = ((Class282_Sub31) Class282_Sub31.aClass482_7775.method8067(-848062278))) {
			if ((1291499461 * ((Class282_Sub31) class282_sub31_20_).anInt7764 == i) && class282_sub31_20_.anInt7762 * 37618455 == i_13_ && i_14_ == -322610393 * class282_sub31_20_.anInt7763 && i_15_ == -497894501 * (((Class282_Sub31) class282_sub31_20_).anInt7766)) {
				class282_sub31 = class282_sub31_20_;
				break;
			}
		}
		if (null == class282_sub31) {
			class282_sub31 = new Class282_Sub31();
			((Class282_Sub31) class282_sub31).anInt7764 = -362696947 * i;
			((Class282_Sub31) class282_sub31).anInt7766 = 1143878291 * i_15_;
			class282_sub31.anInt7762 = 1690395815 * i_13_;
			class282_sub31.anInt7763 = i_14_ * 765748375;
			if (i_13_ >= 0 && i_14_ >= 0 && i_13_ < client.aClass257_7353.method4424(-1083484093) && i_14_ < client.aClass257_7353.method4451(-978246056))
				Class275_Sub4.method12585(class282_sub31, -1988647724);
			Class282_Sub31.aClass482_7775.method8059(class282_sub31, 1387539500);
		}
		((Class282_Sub31) class282_sub31).anInt7769 = -567871853 * i_16_;
		((Class282_Sub31) class282_sub31).anInt7771 = i_17_ * -763092445;
		((Class282_Sub31) class282_sub31).anInt7772 = i_18_ * 111963359;
		((Class282_Sub31) class282_sub31).aBool7773 = true;
		((Class282_Sub31) class282_sub31).aBool7774 = false;
	}

	static final void method5314(Class527 class527, byte i) {
		Class513 class513;
		if (((Class527) class527).aBool7022) {
			if (i != 0)
				return;
			class513 = ((Class527) class527).aClass513_6994;
		} else
			class513 = ((Class527) class527).aClass513_7007;
		Class513 class513_21_ = class513;
		Class118 class118 = ((Class513) class513_21_).aClass118_5886;
		Class98 class98 = ((Class513) class513_21_).aClass98_5885;
		Class290.method5121(class118, class98, class527, (byte) 7);
	}

	static final void method5315(Class527 class527, int i) {
		int i_22_ = (((Class527) class527).anIntArray7018[((Class527) class527).anInt7020 * 301123709]);
		Class282_Sub50_Sub5 class282_sub50_sub5 = Class286.method5049(i_22_, -1272520729);
		if (class282_sub50_sub5 == null)
			throw new RuntimeException();
		int[] is = new int[693687803 * class282_sub50_sub5.anInt9525];
		Object[] objects = new Object[1886892247 * class282_sub50_sub5.anInt9526];
		long[] ls = new long[-684160137 * class282_sub50_sub5.anInt9530];
		for (int i_23_ = 0; i_23_ < -1312392163 * class282_sub50_sub5.anInt9528; i_23_++)
			is[i_23_] = (((Class527) class527).anIntArray6999[i_23_ + (((Class527) class527).anInt7012 * 1942118537 - class282_sub50_sub5.anInt9528 * -1312392163)]);
		for (int i_24_ = 0; i_24_ < class282_sub50_sub5.anInt9529 * 1570560503; i_24_++)
			objects[i_24_] = (((Class527) class527).anObjectArray7019[i_24_ + (1806726141 * ((Class527) class527).anInt7000 - 1570560503 * class282_sub50_sub5.anInt9529)]);
		for (int i_25_ = 0; i_25_ < -2105377515 * class282_sub50_sub5.anInt9524; i_25_++)
			ls[i_25_] = (((Class527) class527).aLongArray7003[i_25_ + (1820448321 * ((Class527) class527).anInt7001 - -2105377515 * class282_sub50_sub5.anInt9524)]);
		((Class527) class527).anInt7012 -= 1642009077 * class282_sub50_sub5.anInt9528;
		((Class527) class527).anInt7000 -= class282_sub50_sub5.anInt9529 * -1798384125;
		((Class527) class527).anInt7001 -= class282_sub50_sub5.anInt9524 * -1483277867;
		Class509 class509 = new Class509();
		((Class509) class509).aClass282_Sub50_Sub5_5869 = ((Class527) class527).aClass282_Sub50_Sub5_7021;
		((Class509) class509).anInt5866 = ((Class527) class527).anInt7020 * 2054263885;
		((Class509) class509).anIntArray5867 = ((Class527) class527).anIntArray6995;
		((Class509) class509).anObjectArray5865 = ((Class527) class527).anObjectArray7017;
		((Class509) class509).aLongArray5868 = ((Class527) class527).aLongArray6996;
		if (((Class527) class527).anInt7002 * -1837903909 >= ((Class527) class527).aClass509Array7016.length)
			throw new RuntimeException();
		((Class527) class527).aClass509Array7016[(((Class527) class527).anInt7002 += -72641453) * -1837903909 - 1] = class509;
		((Class527) class527).aClass282_Sub50_Sub5_7021 = class282_sub50_sub5;
		((Class527) class527).aClass522Array7005 = (((Class527) class527).aClass282_Sub50_Sub5_7021.aClass522Array9521);
		((Class527) class527).anIntArray7018 = ((Class527) class527).aClass282_Sub50_Sub5_7021.anIntArray9522;
		((Class527) class527).anInt7020 = 1051529003;
		((Class527) class527).anIntArray6995 = is;
		((Class527) class527).anObjectArray7017 = objects;
		((Class527) class527).aLongArray6996 = ls;
	}

	public static void method5316(int i, int i_26_, int i_27_, int i_28_, int i_29_, int i_30_, int i_31_, short i_32_) {
		if (i_28_ == i_27_)
			Class532_Sub1.method12838(i, i_26_, i_27_, i_29_, i_30_, i_31_, (short) 4096);
		else if (i - i_27_ >= Class532_Sub3_Sub1.anInt7071 * -612590951 && i + i_27_ <= Class532_Sub3_Sub1.anInt7069 * -1345107225 && i_26_ - i_28_ >= 324226563 * Class532_Sub3_Sub1.anInt7070 && i_26_ + i_28_ <= Class532_Sub3_Sub1.anInt7068 * -348932735)
			Class282_Sub20_Sub7.method15241(i, i_26_, i_27_, i_28_, i_29_, i_30_, i_31_, (byte) 61);
		else
			Class405.method6824(i, i_26_, i_27_, i_28_, i_29_, i_30_, i_31_, -424326901);
	}
}
