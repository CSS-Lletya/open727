/* Class210 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class210 {
	Class229 aClass229_2664 = new Class229(20);
	Class317 aClass317_2665;
	Class317 aClass317_2666;
	Class229 aClass229_2667 = new Class229(64);
	public static int[] anIntArray2668;

	public void method3602(int i, int i_0_) {
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3858(i, (byte) -65);
		}
		synchronized (((Class210) this).aClass229_2664) {
			((Class210) this).aClass229_2664.method3858(i, (byte) 47);
		}
	}

	public Class228 method3603(int i, int i_1_) {
		Class228 class228;
		synchronized (((Class210) this).aClass229_2667) {
			class228 = ((Class228) ((Class210) this).aClass229_2667.method3865((long) i));
		}
		if (null != class228)
			return class228;
		byte[] is;
		synchronized (((Class210) this).aClass317_2665) {
			is = (((Class210) this).aClass317_2665.method5607(-71319279 * Class120.aClass120_1509.anInt1521, i, -1498386951));
		}
		class228 = new Class228();
		((Class228) class228).aClass210_2850 = this;
		if (is != null)
			class228.method3837(new RsByteBuffer(is), 738101655);
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3856(class228, (long) i);
		}
		return class228;
	}

	public void method3604(short i) {
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3859(1876702995);
		}
		synchronized (((Class210) this).aClass229_2664) {
			((Class210) this).aClass229_2664.method3859(2003512741);
		}
	}

	public void method3605(int i) {
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3858(i, (byte) -44);
		}
		synchronized (((Class210) this).aClass229_2664) {
			((Class210) this).aClass229_2664.method3858(i, (byte) 43);
		}
	}

	public void method3606() {
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3863(1160672503);
		}
		synchronized (((Class210) this).aClass229_2664) {
			((Class210) this).aClass229_2664.method3863(1641352532);
		}
	}

	public Class228 method3607(int i) {
		Class228 class228;
		synchronized (((Class210) this).aClass229_2667) {
			class228 = ((Class228) ((Class210) this).aClass229_2667.method3865((long) i));
		}
		if (null != class228)
			return class228;
		byte[] is;
		synchronized (((Class210) this).aClass317_2665) {
			is = (((Class210) this).aClass317_2665.method5607(-71319279 * Class120.aClass120_1509.anInt1521, i, -1498305941));
		}
		class228 = new Class228();
		((Class228) class228).aClass210_2850 = this;
		if (is != null)
			class228.method3837(new RsByteBuffer(is), 1313817347);
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3856(class228, (long) i);
		}
		return class228;
	}

	public Class210(Class486 class486, Class495 class495, Class317 class317, Class317 class317_2_) {
		((Class210) this).aClass317_2666 = class317_2_;
		((Class210) this).aClass317_2665 = class317;
		((Class210) this).aClass317_2665.method5624((-71319279 * (Class120.aClass120_1509.anInt1521)), -512720264);
	}

	public void method3608(int i) {
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3863(994543412);
		}
		synchronized (((Class210) this).aClass229_2664) {
			((Class210) this).aClass229_2664.method3863(1441931247);
		}
	}

	public void method3609() {
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3863(2086367744);
		}
		synchronized (((Class210) this).aClass229_2664) {
			((Class210) this).aClass229_2664.method3863(1682011582);
		}
	}

	public void method3610() {
		synchronized (((Class210) this).aClass229_2667) {
			((Class210) this).aClass229_2667.method3863(1241737838);
		}
		synchronized (((Class210) this).aClass229_2664) {
			((Class210) this).aClass229_2664.method3863(733911460);
		}
	}

	static final void method3611(Class527 class527, int i) {
		Class393.aClass282_Sub54_4783.method13511(Class393.aClass282_Sub54_4783.aClass468_Sub2_8205, (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]), -163458202);
		client.aClass257_7353.method4547((byte) -4);
		Class190.method3148((byte) 43);
		client.aBool7175 = false;
	}

	static final void method3612(int i, int i_3_, int i_4_, int i_5_, int i_6_, boolean bool, byte i_7_) {
		if (!bool && (i_3_ < 512 || i_4_ < 512 || i_3_ > (client.aClass257_7353.method4424(112357923) - 2) * 512 || i_4_ > (client.aClass257_7353.method4451(-200996261) - 2) * 512)) {
			float[] fs = client.aFloatArray7292;
			client.aFloatArray7292[1] = -1.0F;
			fs[0] = -1.0F;
		} else {
			int i_8_ = Class504.method8389(i_3_, i_4_, i, (byte) 110) - i_6_;
			client.aClass294_7457.method5209(Class316.aClass505_3680.method8458());
			client.aClass294_7457.method5219((float) i_5_, 0.0F, 0.0F);
			Class316.aClass505_3680.method8457(client.aClass294_7457);
			if (bool)
				Class316.aClass505_3680.method8479((float) i_3_, (float) i_8_, (float) i_4_, client.aFloatArray7292);
			else
				Class316.aClass505_3680.method8515((float) i_3_, (float) i_8_, (float) i_4_, client.aFloatArray7292);
			client.aClass294_7457.method5219((float) -i_5_, 0.0F, 0.0F);
			Class316.aClass505_3680.method8457(client.aClass294_7457);
			client.aFloatArray7292[0] -= (float) (client.anInt7444 * -1007294471);
			client.aFloatArray7292[1] -= (float) (client.anInt7445 * 915815265);
		}
	}

	static final void method3613(Class527 class527, int i) {
		if (Class475.aBool5623 && Class263.aFrame3260 != null)
			Class440.method7373(Class393.aClass282_Sub54_4783.aClass468_Sub9_8226.method12687(551500203), -1, -1, false, (byte) 52);
	}

	public static void method3614(Class397 class397, int i, int i_9_, byte i_10_) {
		Class527 class527 = Class125.method2167(-547790370);
		Class107.method1834(class397, i, i_9_, class527, (byte) 7);
	}
}
