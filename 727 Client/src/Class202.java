
/* Class202 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.IOException;
import java.net.Socket;

public abstract class Class202 {
	public abstract void method3310();

	public abstract void method3311(byte[] is, int i, int i_0_, int i_1_) throws IOException;

	public abstract int method3312(int i) throws IOException;

	public static Class202 method3313(Socket socket, int i) throws IOException {
		return new Class202_Sub1(socket, i);
	}

	public abstract void method3314();

	public abstract boolean method3315(int i, byte i_2_) throws IOException;

	public abstract void method3316();

	public abstract boolean method3317(int i) throws IOException;

	public abstract void method3318(int i);

	public abstract boolean method3319(int i) throws IOException;

	public abstract void method3320(byte i);

	public abstract boolean method3321(int i) throws IOException;

	public abstract int method3322() throws IOException;

	public abstract int method3323() throws IOException;

	public abstract int method3324(byte[] is, int i, int i_3_) throws IOException;

	public abstract void method3325(byte[] is, int i, int i_4_) throws IOException;

	public abstract void method3326(byte[] is, int i, int i_5_) throws IOException;

	public abstract int method3327(byte[] is, int i, int i_6_, int i_7_) throws IOException;

	public abstract boolean method3328(int i) throws IOException;

	public abstract boolean method3329(int i) throws IOException;

	public abstract void method3330();

	public abstract void method3331();

	public abstract void method3332();

	public abstract void method3333();

	Class202() {
		/* empty */
	}

	public static Class202 method3334(Socket socket, int i) throws IOException {
		return new Class202_Sub1(socket, i);
	}

	public static Class202 method3335(Socket socket, int i) throws IOException {
		return new Class202_Sub1(socket, i);
	}

	public static Class202 method3336(Socket socket, int i) throws IOException {
		return new Class202_Sub1(socket, i);
	}

	public static void method3337(Class118[] class118s, int i) {
		for (int i_8_ = 0; i_8_ < class118s.length; i_8_++) {
			Class118 class118 = class118s[i_8_];
			if (null != class118.anObjectArray1318) {
				Class282_Sub43 class282_sub43 = new Class282_Sub43();
				class282_sub43.aClass118_8053 = class118;
				class282_sub43.anObjectArray8054 = class118.anObjectArray1318;
				Class400.method6794(class282_sub43, 2000000, (byte) 84);
			}
		}
	}

	static final void method3338(Class118 class118, Class98 class98, Class527 class527, byte i) {
		String string = (String) (((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 -= 1476624725) * 1806726141]);
		int[] is = Class96_Sub14.method14642(string, class527, 510066471);
		if (is != null)
			string = string.substring(0, string.length() - 1);
		class118.anObjectArray1387 = Class351.method6193(string, class527, 1621483934);
		class118.anIntArray1402 = is;
		class118.aBool1384 = true;
	}

	static final void method3339(Class527 class527, byte i) {
		String string = (String) (((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 -= 1476624725) * 1806726141]);
		Class2.method258(string, 779827732);
	}

	static final void method3340(Class527 class527, int i) {
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = Class84.myPlayer.aClass19_10359.method578((byte) 2) >> 3;
	}

	static final void method3341(Class527 class527, byte i) {
		((Class527) class527).anInt7012 -= 283782002;
		int i_9_ = (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537]);
		int i_10_ = (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 + 1]);
		if (Class468_Sub8.aClass98Array7889[i_9_] == null)
			((Class527) class527).anObjectArray7019[((((Class527) class527).anInt7000 += 1476624725) * 1806726141 - 1)] = "";
		else {
			String string = (Class468_Sub8.aClass98Array7889[i_9_].aClass118Array998[i_10_].aString1285);
			if (null == string)
				((Class527) class527).anObjectArray7019[((((Class527) class527).anInt7000 += 1476624725) * 1806726141) - 1] = "";
			else
				((Class527) class527).anObjectArray7019[((((Class527) class527).anInt7000 += 1476624725) * 1806726141) - 1] = string;
		}
	}

	public static String method3342(long l, int i, boolean bool, Class495 class495, byte i_11_) {
		char c = ',';
		char c_12_ = '.';
		if (Class495.aClass495_5795 == class495) {
			c = '.';
			c_12_ = ',';
		}
		if (class495 == Class495.aClass495_5801)
			c_12_ = '\u00a0';
		boolean bool_13_ = false;
		if (l < 0L) {
			bool_13_ = true;
			l = -l;
		}
		StringBuilder stringbuilder = new StringBuilder(26);
		if (i > 0) {
			for (int i_14_ = 0; i_14_ < i; i_14_++) {
				int i_15_ = (int) l;
				l /= 10L;
				stringbuilder.append((char) (i_15_ + 48 - (int) l * 10));
			}
			stringbuilder.append(c);
		}
		int i_16_ = 0;
		for (;;) {
			int i_17_ = (int) l;
			l /= 10L;
			stringbuilder.append((char) (48 + i_17_ - 10 * (int) l));
			if (0L == l)
				break;
			if (bool && ++i_16_ % 3 == 0)
				stringbuilder.append(c_12_);
		}
		if (bool_13_)
			stringbuilder.append('-');
		return stringbuilder.reverse().toString();
	}

	static Class461[] method3343(int i) {
		return (new Class461[] { Class461.aClass461_5539, Class461.aClass461_5538, Class461.aClass461_5540 });
	}

	static final void method3344(Class527 class527, int i) {
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = (client.anInt7162 * 2080273591 == ((Class527) class527).aClass521_Sub1_Sub1_Sub2_7006.method15794(-592862572)) ? 1 : 0;
	}

	public static final void method3345(String string, int i) {
		if (!string.equals("")) {
			Class184 class184 = Class468_Sub20.method12807(-1721172592);
			Class282_Sub23 class282_sub23 = Class271.method4828(OutgoingPacket.JOIN_FRIEND_CHAT_PACKET, class184.aClass432_2283, 595380659);
			class282_sub23.buffer.writeByte(Class108.method1846(string, -1127770384));
			class282_sub23.buffer.writeString(string);
			class184.method3049(class282_sub23, 1755458135);
		}
	}

	static final void method3346(Class527 class527, int i) {
		String string = (String) (((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 -= 1476624725) * 1806726141]);
		((Class527) class527).anInt7012 -= 283782002;
		int i_18_ = (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537]);
		int i_19_ = (((Class527) class527).anIntArray6999[1 + ((Class527) class527).anInt7012 * 1942118537]);
		Class414 class414 = Class94.method1588(Class410.aClass317_4924, i_19_, 0, 1119085558);
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = class414.method6949(string, i_18_, Class182.aClass160Array2261, 437013959);
	}
}
