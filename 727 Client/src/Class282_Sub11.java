/* Class282_Sub11 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public abstract class Class282_Sub11 extends Class282 {
	Class282_Sub11() {
		/* empty */
	}

	abstract void method12203(RsByteBuffer class282_sub35, int i);

	abstract void method12204(Class282_Sub4 class282_sub4, int i);

	abstract void method12205(Class282_Sub4 class282_sub4);

	abstract void method12206(RsByteBuffer class282_sub35);

	abstract void method12207(RsByteBuffer class282_sub35);

	abstract void method12208(RsByteBuffer class282_sub35);

	static final void method12209(Class118 class118, Class98 class98, Class527 class527, int i) {
		((Class527) class527).anInt7012 -= 283782002;
		int i_0_ = 10;
		byte[] is = { (byte) ((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537] };
		byte[] is_1_ = { (byte) ((Class527) class527).anIntArray6999[1942118537 * ((Class527) class527).anInt7012 + 1] };
		Class445.method7429(class118, i_0_, is, is_1_, class527, -2033836462);
	}

	static final void method12210(Class527 class527, byte i) {
		((Class527) class527).anInt7000 -= 134906879;
		Class533.method11404((String) (((Class527) class527).anObjectArray7019[((Class527) class527).anInt7000 * 1806726141]), (String) (((Class527) class527).anObjectArray7019[((Class527) class527).anInt7000 * 1806726141 + 1]), (String) (((Class527) class527).anObjectArray7019[2 + 1806726141 * ((Class527) class527).anInt7000]), ((((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]) == 1), true, -94301647);
	}

	public static void method12211(int i) {
		Class442 class442 = null;
		try {
			class442 = Class345.method6137("2", client.aClass486_7450.aString5748, true, (byte) -1);
			RsByteBuffer class282_sub35 = new RsByteBuffer(3 + client.anInt7399 * 753573902);
			class282_sub35.writeByte(1);
			class282_sub35.writeShort(841423533 * client.anInt7399, 1417031095);
			for (int i_2_ = 0; i_2_ < Class320.anIntArray3724.length; i_2_++) {
				if (Class282_Sub17_Sub2.aBoolArray9934[i_2_]) {
					class282_sub35.writeShort(i_2_, 1417031095);
					class282_sub35.writeInt(Class320.anIntArray3724[i_2_]);
				}
			}
			class442.method7386(class282_sub35.buffer, 0, class282_sub35.index * -1990677291, 18074141);
		} catch (Exception exception) {
			/* empty */
		}
		try {
			if (class442 != null)
				class442.method7385((short) 27554);
		} catch (Exception exception) {
			/* empty */
		}
		client.aLong7401 = Class169.method2869(1757662467) * -3531340789837788151L;
		client.aBool7400 = false;
	}
}
