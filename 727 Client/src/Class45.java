/* Class45 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class45 {
	Class229 aClass229_432 = new Class229(16);
	Class317 aClass317_433;
	protected static int anInt434;
	public static Class447 aClass447_435;

	public Class38 method914(int i, int i_0_) {
		Class38 class38;
		synchronized (((Class45) this).aClass229_432) {
			class38 = ((Class38) ((Class45) this).aClass229_432.method3865((long) i));
		}
		if (class38 != null)
			return class38;
		byte[] is;
		synchronized (((Class45) this).aClass317_433) {
			is = ((Class45) this).aClass317_433.method5607(((Class120.aClass120_1489.anInt1521) * -71319279), i, -2121078318);
		}
		class38 = new Class38();
		if (is != null)
			class38.method850(new RsByteBuffer(is), -50150458);
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3856(class38, (long) i);
		}
		return class38;
	}

	public void method915(int i) {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3858(i, (byte) -10);
		}
	}

	public void method916(byte i) {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3859(-1765413444);
		}
	}

	public void method917(int i, int i_1_) {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3858(i, (byte) 1);
		}
	}

	public void method918(int i) {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3863(1208180587);
		}
	}

	public Class38 method919(int i) {
		Class38 class38;
		synchronized (((Class45) this).aClass229_432) {
			class38 = ((Class38) ((Class45) this).aClass229_432.method3865((long) i));
		}
		if (class38 != null)
			return class38;
		byte[] is;
		synchronized (((Class45) this).aClass317_433) {
			is = ((Class45) this).aClass317_433.method5607(((Class120.aClass120_1489.anInt1521) * -71319279), i, -1886940547);
		}
		class38 = new Class38();
		if (is != null)
			class38.method850(new RsByteBuffer(is), -50150458);
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3856(class38, (long) i);
		}
		return class38;
	}

	public Class38 method920(int i) {
		Class38 class38;
		synchronized (((Class45) this).aClass229_432) {
			class38 = ((Class38) ((Class45) this).aClass229_432.method3865((long) i));
		}
		if (class38 != null)
			return class38;
		byte[] is;
		synchronized (((Class45) this).aClass317_433) {
			is = ((Class45) this).aClass317_433.method5607(((Class120.aClass120_1489.anInt1521) * -71319279), i, -1753855933);
		}
		class38 = new Class38();
		if (is != null)
			class38.method850(new RsByteBuffer(is), -50150458);
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3856(class38, (long) i);
		}
		return class38;
	}

	public void method921() {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3859(-225919651);
		}
	}

	public void method922() {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3859(-721663611);
		}
	}

	public void method923(int i) {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3858(i, (byte) -103);
		}
	}

	public Class45(Class486 class486, Class495 class495, Class317 class317) {
		((Class45) this).aClass317_433 = class317;
		((Class45) this).aClass317_433.method5624((Class120.aClass120_1489.anInt1521) * -71319279, -422485706);
	}

	public void method924() {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3863(1921594853);
		}
	}

	public void method925() {
		synchronized (((Class45) this).aClass229_432) {
			((Class45) this).aClass229_432.method3863(1337347693);
		}
	}

	static final void method926(Class527 class527, int i) {
		String string = (String) (((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 -= 1476624725) * 1806726141]);
		int i_2_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 += 1476624725) * 1806726141 - 1] = new StringBuilder().append(string).append(Class290.method5120(i_2_, true, -948271642)).toString();
	}

	static final void method927(Class527 class527, int i) {
		int i_3_ = client.aClass465_7442.method7748((short) 622);
		if (-1 != client.anInt7349 * -1699899559)
			i_3_++;
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = i_3_;
	}

	static final void method928(Class527 class527, int i) {
		if (Class96_Sub21.aClass199_9438.method3236(82, -2064164643))
			((Class527) class527).anIntArray6999[((((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1)] = 1;
		else
			((Class527) class527).anIntArray6999[((((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1)] = 0;
	}
}
