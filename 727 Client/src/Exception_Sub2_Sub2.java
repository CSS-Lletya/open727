/* Exception_Sub2_Sub2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Exception_Sub2_Sub2 extends Exception_Sub2 {
	Exception_Sub2_Sub2(String string, boolean bool) {
		super(string);
	}

	Exception_Sub2_Sub2(String string) {
		this(string, false);
	}

	static final void method15951(int i) {
		RsBitsBuffer buffer = ((Class184) client.aClass184_7475).aClass282_Sub35_Sub2_2284;
		buffer.method14872((byte) 104);
		int localSize = buffer.readBits(8, (byte) -33);
		if (localSize < client.anInt7211 * -685729279) {
			for (int i_1_ = localSize; i_1_ < client.anInt7211 * -685729279; i_1_++)
				client.anIntArray7421[(client.anInt7216 += 1192125241) * -423388407 - 1] = client.anIntArray7212[i_1_];
		}
		if (localSize > -685729279 * client.anInt7211)
			throw new RuntimeException();
		client.anInt7211 = 0;
		for (int index = 0; index < localSize; index++) {
			int i_3_ = client.anIntArray7212[index];
			Class521_Sub1_Sub1_Sub2_Sub2 class521_sub1_sub1_sub2_sub2 = ((Class521_Sub1_Sub1_Sub2_Sub2) (((Class282_Sub47) client.aClass465_7208.method7754((long) i_3_)).anObject8068));
			int i_4_ = buffer.readBits(1, (byte) 26);
			if (i_4_ == 0) {
				client.anIntArray7212[(client.anInt7211 += 1568892417) * -685729279 - 1] = i_3_;
				class521_sub1_sub1_sub2_sub2.anInt10353 = client.anInt7332 * -2017074209;
			} else {
				int i_5_ = buffer.readBits(2, (byte) 53);
				if (0 == i_5_) {
					client.anIntArray7212[(client.anInt7211 += 1568892417) * -685729279 - 1] = i_3_;
					class521_sub1_sub1_sub2_sub2.anInt10353 = client.anInt7332 * -2017074209;
					client.anIntArray7282[(client.anInt7213 += -1510626643) * -1300281051 - 1] = i_3_;
				} else if (i_5_ == 1) {
					client.anIntArray7212[(client.anInt7211 += 1568892417) * -685729279 - 1] = i_3_;
					class521_sub1_sub1_sub2_sub2.anInt10353 = client.anInt7332 * -2017074209;
					Class252 class252 = ((Class252) Class386.method6672(Class46.method931(1696399796), buffer.readBits(3, (byte) -36), -2098267169));
					class521_sub1_sub1_sub2_sub2.method16161(class252, (Class249.aClass249_3084.aByte3085), 59420458);
					int i_6_ = buffer.readBits(1, (byte) 10);
					if (i_6_ == 1)
						client.anIntArray7282[((client.anInt7213 += -1510626643) * -1300281051 - 1)] = i_3_;
				} else if (2 == i_5_) {
					client.anIntArray7212[(client.anInt7211 += 1568892417) * -685729279 - 1] = i_3_;
					class521_sub1_sub1_sub2_sub2.anInt10353 = client.anInt7332 * -2017074209;
					if (buffer.readBits(1, (byte) 34) == 1) {
						Class252 class252 = ((Class252) (Class386.method6672(Class46.method931(-112780956), buffer.readBits(3, (byte) 44), -2077804920)));
						class521_sub1_sub1_sub2_sub2.method16161(class252, Class249.aClass249_3086.aByte3085, -82675465);
						Class252 class252_7_ = ((Class252) (Class386.method6672(Class46.method931(-882869718), buffer.readBits(3, (byte) 30), -1179003968)));
						class521_sub1_sub1_sub2_sub2.method16161(class252_7_, Class249.aClass249_3086.aByte3085, 1093039209);
					} else {
						Class252 class252 = ((Class252) (Class386.method6672(Class46.method931(-867338683), buffer.readBits(3, (byte) 35), -1403757890)));
						class521_sub1_sub1_sub2_sub2.method16161(class252, Class249.aClass249_3082.aByte3085, 1992160730);
					}
					int i_8_ = buffer.readBits(1, (byte) 3);
					if (i_8_ == 1)
						client.anIntArray7282[((client.anInt7213 += -1510626643) * -1300281051 - 1)] = i_3_;
				} else if (i_5_ == 3)
					client.anIntArray7421[(client.anInt7216 += 1192125241) * -423388407 - 1] = i_3_;
			}
		}
	}
}
