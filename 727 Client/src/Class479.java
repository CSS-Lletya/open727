
/* Class479 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Point;

public class Class479 {
	Class317 aClass317_5713;
	Class229 aClass229_5714 = new Class229(128);

	public void method8024(int i) {
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3858(i, (byte) 97);
		}
	}

	public Class481 method8025(int i, byte i_0_) {
		Class481 class481;
		synchronized (((Class479) this).aClass229_5714) {
			class481 = ((Class481) ((Class479) this).aClass229_5714.method3865((long) i));
		}
		if (class481 != null)
			return class481;
		byte[] is;
		synchronized (((Class479) this).aClass317_5713) {
			is = (((Class479) this).aClass317_5713.method5607(-71319279 * Class120.aClass120_1504.anInt1521, i, -1999870087));
		}
		class481 = new Class481();
		if (null != is)
			class481.method8050(new RsByteBuffer(is), -341633442);
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3856(class481, (long) i);
		}
		return class481;
	}

	public void method8026(int i) {
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3859(-62537234);
		}
	}

	public Class479(Class486 class486, Class495 class495, Class317 class317) {
		((Class479) this).aClass317_5713 = class317;
		((Class479) this).aClass317_5713.method5624((Class120.aClass120_1504.anInt1521) * -71319279, 345702944);
	}

	public void method8027() {
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3859(-438351749);
		}
	}

	public Class481 method8028(int i) {
		Class481 class481;
		synchronized (((Class479) this).aClass229_5714) {
			class481 = ((Class481) ((Class479) this).aClass229_5714.method3865((long) i));
		}
		if (class481 != null)
			return class481;
		byte[] is;
		synchronized (((Class479) this).aClass317_5713) {
			is = (((Class479) this).aClass317_5713.method5607(-71319279 * Class120.aClass120_1504.anInt1521, i, -1393062609));
		}
		class481 = new Class481();
		if (null != is)
			class481.method8050(new RsByteBuffer(is), 2009630074);
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3856(class481, (long) i);
		}
		return class481;
	}

	public Class481 method8029(int i) {
		Class481 class481;
		synchronized (((Class479) this).aClass229_5714) {
			class481 = ((Class481) ((Class479) this).aClass229_5714.method3865((long) i));
		}
		if (class481 != null)
			return class481;
		byte[] is;
		synchronized (((Class479) this).aClass317_5713) {
			is = (((Class479) this).aClass317_5713.method5607(-71319279 * Class120.aClass120_1504.anInt1521, i, -1675981481));
		}
		class481 = new Class481();
		if (null != is)
			class481.method8050(new RsByteBuffer(is), -979770743);
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3856(class481, (long) i);
		}
		return class481;
	}

	public void method8030(int i, int i_1_) {
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3858(i, (byte) -26);
		}
	}

	public void method8031() {
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3859(-2074436659);
		}
	}

	public void method8032(int i) {
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3863(977235767);
		}
	}

	public void method8033() {
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3859(-1304573321);
		}
	}

	public void method8034(int i) {
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3858(i, (byte) -6);
		}
	}

	public Class481 method8035(int i) {
		Class481 class481;
		synchronized (((Class479) this).aClass229_5714) {
			class481 = ((Class481) ((Class479) this).aClass229_5714.method3865((long) i));
		}
		if (class481 != null)
			return class481;
		byte[] is;
		synchronized (((Class479) this).aClass317_5713) {
			is = (((Class479) this).aClass317_5713.method5607(-71319279 * Class120.aClass120_1504.anInt1521, i, -1932524788));
		}
		class481 = new Class481();
		if (null != is)
			class481.method8050(new RsByteBuffer(is), 68816610);
		synchronized (((Class479) this).aClass229_5714) {
			((Class479) this).aClass229_5714.method3856(class481, (long) i);
		}
		return class481;
	}

	static void method8036(int i, byte i_2_) {
		if (Class393.aClass282_Sub54_4783.aClass468_Sub8_8219.method12675(-1589433086) == 0)
			i = -1;
		if (i != client.anInt7190 * -1474737961) {
			if (i != -1) {
				Class419 class419 = Class468_Sub23.aClass438_7932.method7325(i, -962986657);
				Class91 class91 = class419.method7026(-890527904);
				if (null != class91) {
					Class422.aClass267_5026.setcustomcursor(Class351.aCanvas4096, class91.method1528(), class91.method1519(), class91.method1520(), new Point(class419.anInt5002 * 1805382875, class419.anInt5000 * -683952919));
					client.anInt7190 = i * -618386713;
				} else
					i = -1;
			}
			if (-1 == i && -1 != client.anInt7190 * -1474737961) {
				Class422.aClass267_5026.setcustomcursor(Class351.aCanvas4096, null, -1, -1, new Point());
				client.anInt7190 = 618386713;
			}
		}
	}

	static final void method8037(Class527 class527, int i) {
		int i_3_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 += 1476624725) * 1806726141 - 1] = ((Class527) class527).aClass61_7010.aStringArray639[i_3_];
	}

	public static Class494[] method8038(byte i) {
		return (new Class494[] { Class494.aClass494_5784, Class494.aClass494_5792, Class494.aClass494_5787, Class494.aClass494_5789, Class494.aClass494_5786, Class494.aClass494_5782, Class494.aClass494_5788, Class494.aClass494_5779, Class494.aClass494_5790, Class494.aClass494_5783, Class494.aClass494_5791, Class494.aClass494_5781, Class494.aClass494_5785, Class494.aClass494_5780 });
	}
}
