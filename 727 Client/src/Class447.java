/* Class447 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class447 {
	Class229 aClass229_5417 = new Class229(64);
	Class317 aClass317_5418;

	public void method7449() {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3863(1287002144);
		}
	}

	public Class449 method7450(int i, byte i_0_) {
		Class449 class449;
		synchronized (((Class447) this).aClass229_5417) {
			class449 = ((Class449) ((Class447) this).aClass229_5417.method3865((long) i));
		}
		if (class449 != null)
			return class449;
		byte[] is = ((Class447) this).aClass317_5418.method5607(((Class120.aClass120_1522.anInt1521) * -71319279), i, -1401240120);
		class449 = new Class449();
		if (null != is)
			class449.method7487(new RsByteBuffer(is), -267873948);
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3856(class449, (long) i);
		}
		return class449;
	}

	public void method7451() {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3859(867706329);
		}
	}

	public void method7452(int i, byte i_1_) {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3858(i, (byte) -19);
		}
	}

	public void method7453(byte i) {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3863(972747632);
		}
	}

	public void method7454(int i) {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3858(i, (byte) -32);
		}
	}

	public void method7455() {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3859(713724825);
		}
	}

	public void method7456() {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3859(-1628120155);
		}
	}

	public Class447(Class486 class486, Class495 class495, Class317 class317) {
		((Class447) this).aClass317_5418 = class317;
		if (((Class447) this).aClass317_5418 != null)
			((Class447) this).aClass317_5418.method5624(-71319279 * Class120.aClass120_1522.anInt1521, 1239350984);
	}

	public void method7457(int i) {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3858(i, (byte) 44);
		}
	}

	public void method7458(int i) {
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3859(1246806514);
		}
	}

	public Class449 method7459(int i) {
		Class449 class449;
		synchronized (((Class447) this).aClass229_5417) {
			class449 = ((Class449) ((Class447) this).aClass229_5417.method3865((long) i));
		}
		if (class449 != null)
			return class449;
		byte[] is = ((Class447) this).aClass317_5418.method5607(((Class120.aClass120_1522.anInt1521) * -71319279), i, -1764756902);
		class449 = new Class449();
		if (null != is)
			class449.method7487(new RsByteBuffer(is), -267873948);
		synchronized (((Class447) this).aClass229_5417) {
			((Class447) this).aClass229_5417.method3856(class449, (long) i);
		}
		return class449;
	}

	static final void method7460(int i, int i_2_, int i_3_, int i_4_) {
		String string = new StringBuilder().append("tele ").append(i).append(Class2.aString14).append(i_2_ >> 6).append(Class2.aString14).append(i_3_ >> 6).append(Class2.aString14).append(i_2_ & 0x3f).append(Class2.aString14).append(i_3_ & 0x3f).toString();
		System.out.println(string);
		Class251.method4313(string, true, false, -354697449);
	}

	static void method7461(Class527 class527, byte i) {
		((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 - 1] = (Class409.aClass242_4922.method4156((((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 - 1]), -1396181317).aBool2962) ? 1 : 0;
	}

	static final void method7462(Class527 class527, int i) {
		String string = null;
		if (null != Class119.aClass12_1461)
			string = Class119.aClass12_1461.method473(1840459942);
		if (null == string)
			string = "";
		((Class527) class527).anObjectArray7019[(((Class527) class527).anInt7000 += 1476624725) * 1806726141 - 1] = string;
	}

	static final void method7463(Class527 class527, int i) {
		((Class527) class527).anInt7012 -= 709455005;
		int i_5_ = (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537]);
		int i_6_ = (((Class527) class527).anIntArray6999[((Class527) class527).anInt7012 * 1942118537 + 1]);
		int i_7_ = (((Class527) class527).anIntArray6999[2 + 1942118537 * ((Class527) class527).anInt7012]);
		int i_8_ = (((Class527) class527).anIntArray6999[3 + ((Class527) class527).anInt7012 * 1942118537]);
		int i_9_ = (((Class527) class527).anIntArray6999[4 + ((Class527) class527).anInt7012 * 1942118537]);
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = i_5_ + (i_9_ - i_7_) * (i_6_ - i_5_) / (i_8_ - i_7_);
	}
}
