
/* Class434 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.IOException;
import java.net.Socket;

public abstract class Class434 {
	static int anInt5329;
	String host;
	int anInt5331;

	public static Class434 method7279(String string, int i) {
		Class434_Sub1 class434_sub1 = new Class434_Sub1();
		((Class434) class434_sub1).host = string;
		((Class434) class434_sub1).anInt5331 = i * -432862573;
		return class434_sub1;
	}

	public abstract Socket method7280(int i) throws IOException;

	Class434() {
		/* empty */
	}

	public abstract Socket method7281() throws IOException;

	public abstract Socket method7282() throws IOException;

	public static Class434 method7283(String string, int i) {
		Class434_Sub1 class434_sub1 = new Class434_Sub1();
		((Class434) class434_sub1).host = string;
		((Class434) class434_sub1).anInt5331 = i * -432862573;
		return class434_sub1;
	}

	public static Class434 method7284(String string, int i) {
		Class434_Sub1 class434_sub1 = new Class434_Sub1();
		((Class434) class434_sub1).host = string;
		((Class434) class434_sub1).anInt5331 = i * -432862573;
		return class434_sub1;
	}

	public static Class434 method7286(String string, int i) {
		Class434_Sub1 class434_sub1 = new Class434_Sub1();
		((Class434) class434_sub1).host = string;
		((Class434) class434_sub1).anInt5331 = i * -432862573;
		return class434_sub1;
	}

	public abstract Socket method7287() throws IOException;

	public static Class434 method7288(String string, int i) {
		Class434_Sub1 class434_sub1 = new Class434_Sub1();
		((Class434) class434_sub1).host = string;
		((Class434) class434_sub1).anInt5331 = i * -432862573;
		return class434_sub1;
	}

	Socket method7291(int i) throws IOException {
		return new Socket(host, Loader.LOBBY_PORT);
	}

	public static void method7292(byte i) {
		Class373.method6365(283616673);
		Class236.aClass534_Sub2_2911.method11417(-2118478860);
		Class236.aClass534_Sub1_2913.method11417(-2125512068);
		if (client.anInt7196 * -809233249 > 0) {
			Class282_Sub23 class282_sub23 = Class271.method4828(OutgoingPacket.KEY_PRESSED_PACKET, client.aClass184_7475.aClass432_2283, 1297366609);
			class282_sub23.buffer.writeShort(client.anInt7196 * 1058034300, 1417031095);
			for (int i_0_ = 0; i_0_ < -809233249 * client.anInt7196; i_0_++) {
				Interface16 interface16 = client.anInterface16Array7298[i_0_];
				long l = (interface16.method119(272047645) - -3063389150359337113L * Class236.aLong2910);
				if (l > 16777215L) {
					l = 16777215L;
				}
				Class236.aLong2910 = interface16.method119(941918410) * 7066845128101291095L;
				class282_sub23.buffer.writeByte(interface16.method92(317240429));
				class282_sub23.buffer.method13195((int) l, (byte) 14);
			}
			client.aClass184_7475.method3049(class282_sub23, 394731592);
		}
		if (1389032649 * Class236.anInt2912 > 0) {
			Class236.anInt2912 -= 1241998713;
		}
		if (client.aBool7371 && Class236.anInt2912 * 1389032649 <= 0) {
			Class236.anInt2912 = -929829516;
			client.aBool7371 = false;
			Class282_Sub23 class282_sub23 = Class271.method4828(OutgoingPacket.MOVE_CAMERA_PACKET, client.aClass184_7475.aClass432_2283, 672473203);
			class282_sub23.buffer.readShortLE128((int) client.aFloat7146 >> 3, -1183932171);
			class282_sub23.buffer.writeShort128((int) client.aFloat7365 >> 3, -800257688);
			client.aClass184_7475.method3049(class282_sub23, 645991437);
		}
		if (Class236.aBool2909 != Class530.aBool7050) {
			Class236.aBool2909 = Class530.aBool7050;
			Class282_Sub23 class282_sub23 = Class271.method4828(OutgoingPacket.aClass379_4597, client.aClass184_7475.aClass432_2283, 108556182);
			class282_sub23.buffer.writeByte(Class530.aBool7050 ? 1 : 0);
			client.aClass184_7475.method3049(class282_sub23, 1669261400);
		}
		if (!client.aBool7175) {
			Class282_Sub23 class282_sub23 = Class271.method4828(OutgoingPacket.aClass379_4537, client.aClass184_7475.aClass432_2283, 1548432137);
			class282_sub23.buffer.writeByte(0);
			int i_1_ = (-1990677291 * class282_sub23.buffer.index);
			RsByteBuffer class282_sub35 = Class393.aClass282_Sub54_4783.method13499(-1147561842);
			class282_sub23.buffer.writeBytes(class282_sub35.buffer, 0, -1990677291 * class282_sub35.index);
			class282_sub23.buffer.method13061((class282_sub23.buffer.index * -1990677291) - i_1_, -1036471531);
			client.aClass184_7475.method3049(class282_sub23, 1020342841);
			client.aBool7175 = true;
		}
	}
}
