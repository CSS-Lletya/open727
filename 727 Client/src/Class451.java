
/* Class451 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.util.Iterator;

public class Class451 implements Iterator {
	Class465 aClass465_5439;
	Class282 aClass282_5440;
	Class282 aClass282_5441 = null;
	int anInt5442;

	public Class282 method7503(int i) {
		method7512(789041827);
		return (Class282) next();
	}

	public Class451(Class465 class465) {
		((Class451) this).aClass465_5439 = class465;
		method7512(-303095976);
	}

	public boolean method7504() {
		if (((Class451) this).aClass282_5440 != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1]))
			return true;
		while (-965471089 * ((Class451) this).anInt5442 < (((Class465) ((Class451) this).aClass465_5439).anInt5560 * 25900449)) {
			if ((((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((((Class451) this).anInt5442 += -1123382673) * -965471089 - 1)].aClass282_3378) != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((Class451) this).anInt5442 * -965471089 - 1])) {
				((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1].aClass282_3378);
				return true;
			}
			((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1]);
		}
		return false;
	}

	public Object method7505() {
		if (((Class451) this).aClass282_5440 != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1])) {
			Class282 class282 = ((Class451) this).aClass282_5440;
			((Class451) this).aClass282_5440 = class282.aClass282_3378;
			((Class451) this).aClass282_5441 = class282;
			return class282;
		}
		while (((Class451) this).anInt5442 * -965471089 < (((Class465) ((Class451) this).aClass465_5439).anInt5560 * 25900449)) {
			Class282 class282 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((((Class451) this).anInt5442 += -1123382673) * -965471089 - 1)].aClass282_3378);
			if ((((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((Class451) this).anInt5442 * -965471089 - 1]) != class282) {
				((Class451) this).aClass282_5440 = class282.aClass282_3378;
				((Class451) this).aClass282_5441 = class282;
				return class282;
			}
		}
		return null;
	}

	public boolean hasNext() {
		if (((Class451) this).aClass282_5440 != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1]))
			return true;
		while (-965471089 * ((Class451) this).anInt5442 < (((Class465) ((Class451) this).aClass465_5439).anInt5560 * 25900449)) {
			if ((((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((((Class451) this).anInt5442 += -1123382673) * -965471089 - 1)].aClass282_3378) != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((Class451) this).anInt5442 * -965471089 - 1])) {
				((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1].aClass282_3378);
				return true;
			}
			((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1]);
		}
		return false;
	}

	public void remove() {
		if (null == ((Class451) this).aClass282_5441)
			throw new IllegalStateException();
		((Class451) this).aClass282_5441.method4991(-371378792);
		((Class451) this).aClass282_5441 = null;
	}

	public boolean method7506() {
		if (((Class451) this).aClass282_5440 != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1]))
			return true;
		while (-965471089 * ((Class451) this).anInt5442 < (((Class465) ((Class451) this).aClass465_5439).anInt5560 * 25900449)) {
			if ((((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((((Class451) this).anInt5442 += -1123382673) * -965471089 - 1)].aClass282_3378) != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((Class451) this).anInt5442 * -965471089 - 1])) {
				((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1].aClass282_3378);
				return true;
			}
			((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1]);
		}
		return false;
	}

	public Object next() {
		if (((Class451) this).aClass282_5440 != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1])) {
			Class282 class282 = ((Class451) this).aClass282_5440;
			((Class451) this).aClass282_5440 = class282.aClass282_3378;
			((Class451) this).aClass282_5441 = class282;
			return class282;
		}
		while (((Class451) this).anInt5442 * -965471089 < (((Class465) ((Class451) this).aClass465_5439).anInt5560 * 25900449)) {
			Class282 class282 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((((Class451) this).anInt5442 += -1123382673) * -965471089 - 1)].aClass282_3378);
			if ((((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((Class451) this).anInt5442 * -965471089 - 1]) != class282) {
				((Class451) this).aClass282_5440 = class282.aClass282_3378;
				((Class451) this).aClass282_5441 = class282;
				return class282;
			}
		}
		return null;
	}

	public void method7507() {
		if (null == ((Class451) this).aClass282_5441)
			throw new IllegalStateException();
		((Class451) this).aClass282_5441.method4991(-371378792);
		((Class451) this).aClass282_5441 = null;
	}

	public boolean method7508() {
		if (((Class451) this).aClass282_5440 != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1]))
			return true;
		while (-965471089 * ((Class451) this).anInt5442 < (((Class465) ((Class451) this).aClass465_5439).anInt5560 * 25900449)) {
			if ((((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((((Class451) this).anInt5442 += -1123382673) * -965471089 - 1)].aClass282_3378) != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((Class451) this).anInt5442 * -965471089 - 1])) {
				((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1].aClass282_3378);
				return true;
			}
			((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1]);
		}
		return false;
	}

	public Object method7509() {
		if (((Class451) this).aClass282_5440 != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1])) {
			Class282 class282 = ((Class451) this).aClass282_5440;
			((Class451) this).aClass282_5440 = class282.aClass282_3378;
			((Class451) this).aClass282_5441 = class282;
			return class282;
		}
		while (((Class451) this).anInt5442 * -965471089 < (((Class465) ((Class451) this).aClass465_5439).anInt5560 * 25900449)) {
			Class282 class282 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((((Class451) this).anInt5442 += -1123382673) * -965471089 - 1)].aClass282_3378);
			if ((((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((Class451) this).anInt5442 * -965471089 - 1]) != class282) {
				((Class451) this).aClass282_5440 = class282.aClass282_3378;
				((Class451) this).aClass282_5441 = class282;
				return class282;
			}
		}
		return null;
	}

	public Object method7510() {
		if (((Class451) this).aClass282_5440 != (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[-965471089 * ((Class451) this).anInt5442 - 1])) {
			Class282 class282 = ((Class451) this).aClass282_5440;
			((Class451) this).aClass282_5440 = class282.aClass282_3378;
			((Class451) this).aClass282_5441 = class282;
			return class282;
		}
		while (((Class451) this).anInt5442 * -965471089 < (((Class465) ((Class451) this).aClass465_5439).anInt5560 * 25900449)) {
			Class282 class282 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((((Class451) this).anInt5442 += -1123382673) * -965471089 - 1)].aClass282_3378);
			if ((((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[((Class451) this).anInt5442 * -965471089 - 1]) != class282) {
				((Class451) this).aClass282_5440 = class282.aClass282_3378;
				((Class451) this).aClass282_5441 = class282;
				return class282;
			}
		}
		return null;
	}

	public void method7511() {
		if (null == ((Class451) this).aClass282_5441)
			throw new IllegalStateException();
		((Class451) this).aClass282_5441.method4991(-371378792);
		((Class451) this).aClass282_5441 = null;
	}

	void method7512(int i) {
		((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[0].aClass282_3378);
		((Class451) this).anInt5442 = -1123382673;
		((Class451) this).aClass282_5441 = null;
	}

	void method7513() {
		((Class451) this).aClass282_5440 = (((Class465) ((Class451) this).aClass465_5439).aClass282Array5558[0].aClass282_3378);
		((Class451) this).anInt5442 = -1123382673;
		((Class451) this).aClass282_5441 = null;
	}

	public Class282 method7514() {
		method7512(-2019603507);
		return (Class282) next();
	}

	public Class282 method7515() {
		method7512(-1032267257);
		return (Class282) next();
	}

	public Class282 method7516() {
		method7512(-397199448);
		return (Class282) next();
	}

	public static boolean method7517(byte i) {
		Class282_Sub42 class282_sub42 = (Class282_Sub42) Class435.aClass482_5332.method8097((byte) 42);
		if (class282_sub42 == null)
			return false;
		return true;
	}

	static final void method7518(Class527 class527, byte i) {
		Class482.method8146(-1130869373);
	}

	static Class411[] method7519(short i) {
		return (new Class411[] { Class411.aClass411_4928, Class411.aClass411_4952, Class411.aClass411_4939, Class411.aClass411_4954, Class411.aClass411_4942, Class411.aClass411_4948, Class411.aClass411_4941, Class411.aClass411_4953, Class411.aClass411_4934, Class411.aClass411_4926, Class411.aClass411_4951, Class411.aClass411_4930, Class411.aClass411_4935, Class411.aClass411_4946, Class411.aClass411_4927, Class411.aClass411_4944, Class411.aClass411_4943, Class411.aClass411_4931, Class411.aClass411_4949, Class411.aClass411_4940, Class411.aClass411_4936, Class411.aClass411_4947, Class411.aClass411_4925, Class411.aClass411_4929, Class411.aClass411_4955, Class411.aClass411_4950, Class411.aClass411_4937, Class411.aClass411_4932, Class411.aClass411_4933, Class411.aClass411_4938, Class411.aClass411_4945 });
	}

	static final void method7520(Class527 class527, int i) {
		int i_0_ = (((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 -= 141891001) * 1942118537]);
		Class118 class118 = Class117.method1981(i_0_, (byte) 58);
		Class98 class98 = Class468_Sub8.aClass98Array7889[i_0_ >> 16];
		Class282_Sub11.method12209(class118, class98, class527, 1467575804);
	}

	static final void method7521(Class527 class527, int i) {
		((Class527) class527).anIntArray6999[(((Class527) class527).anInt7012 += 141891001) * 1942118537 - 1] = Class393.aClass282_Sub54_4783.aClass468_Sub24_8216.method12920(308447312) == 2 ? 1 : 0;
	}

	static final void method7522(Class527 class527, int i) {
		Class513 class513 = (((Class527) class527).aBool7022 ? ((Class527) class527).aClass513_6994 : ((Class527) class527).aClass513_7007);
		Class118 class118 = ((Class513) class513).aClass118_5886;
		Class98 class98 = ((Class513) class513).aClass98_5885;
		Class344.method6122(class118, class98, class527, -345845771);
	}
}
