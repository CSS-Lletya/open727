
/* Class284 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Login {
	int anInt3385;
	boolean aBool3386;
	int anInt3387;
	public List aList3388 = new LinkedList();
	public static Class210 aClass210_3389;

	public void method5011() {
		Iterator iterator = aList3388.iterator();
		while (iterator.hasNext()) {
			Class285 class285 = (Class285) iterator.next();
			iterator.remove();
			Class279.method4969(class285, 1362412061);
		}
	}

	public void method5012(int i) {
		Iterator iterator = aList3388.iterator();
		while (iterator.hasNext()) {
			Class285 class285 = (Class285) iterator.next();
			iterator.remove();
			Class279.method4969(class285, 1362412061);
		}
	}

	void method5013(Class285 class285, int i) {
		Class521_Sub1 class521_sub1 = class285.aClass521_Sub1_3391;
		boolean bool = true;
		Class275_Sub5[] class275_sub5s = class285.aClass521_Sub1_3391.aClass275_Sub5Array7965;
		for (int i_0_ = 0; i_0_ < class275_sub5s.length; i_0_++) {
			if (class275_sub5s[i_0_].aBool7849) {
				bool = false;
				break;
			}
		}
		if (!bool) {
			class285.aClass284_3393 = this;
			if (this.aBool3386) {
				Iterator iterator = aList3388.iterator();
				while (iterator.hasNext()) {
					Class285 class285_1_ = (Class285) iterator.next();
					if (class521_sub1 == class285_1_.aClass521_Sub1_3391) {
						iterator.remove();
						Class279.method4969(class285_1_, 1362412061);
					}
				}
			}
			ListIterator listiterator = aList3388.listIterator();
			while (listiterator.hasNext()) {
				Class285 class285_2_ = (Class285) listiterator.next();
				if (class521_sub1.anInt7969 * 1272394495 >= (class285_2_.aClass521_Sub1_3391.anInt7969) * 1272394495) {
					listiterator.previous();
					listiterator.add(class285);
					return;
				}
			}
			aList3388.add(class285);
		}
	}

	Login(boolean bool) {
		this.aBool3386 = false;
		this.anInt3387 = 0;
		this.anInt3385 = 0;
		this.aBool3386 = bool;
	}

	public void method5014() {
		Iterator iterator = aList3388.iterator();
		while (iterator.hasNext()) {
			Class285 class285 = (Class285) iterator.next();
			iterator.remove();
			Class279.method4969(class285, 1362412061);
		}
	}

	void method5015(Class285 class285) {
		Class521_Sub1 class521_sub1 = class285.aClass521_Sub1_3391;
		boolean bool = true;
		Class275_Sub5[] class275_sub5s = class285.aClass521_Sub1_3391.aClass275_Sub5Array7965;
		for (int i = 0; i < class275_sub5s.length; i++) {
			if (class275_sub5s[i].aBool7849) {
				bool = false;
				break;
			}
		}
		if (!bool) {
			class285.aClass284_3393 = this;
			if (this.aBool3386) {
				Iterator iterator = aList3388.iterator();
				while (iterator.hasNext()) {
					Class285 class285_3_ = (Class285) iterator.next();
					if (class521_sub1 == class285_3_.aClass521_Sub1_3391) {
						iterator.remove();
						Class279.method4969(class285_3_, 1362412061);
					}
				}
			}
			ListIterator listiterator = aList3388.listIterator();
			while (listiterator.hasNext()) {
				Class285 class285_4_ = (Class285) listiterator.next();
				if (class521_sub1.anInt7969 * 1272394495 >= (class285_4_.aClass521_Sub1_3391.anInt7969) * 1272394495) {
					listiterator.previous();
					listiterator.add(class285);
					return;
				}
			}
			aList3388.add(class285);
		}
	}

	static void method5016(Class527 class527, int i) {
		Class240 class240 = Class409.aClass242_4922.method4156((class527.anIntArray6999[((class527.anInt7012 -= 141891001) * 1942118537)]), -1396181317);
		class527.anIntArray6999[(class527.anInt7012 += 141891001) * 1942118537 - 1] = (null == class240.anIntArray2968 ? 0 : class240.anIntArray2968.length);
	}

	static final void method5017(Class118 class118, Class98 class98, Class527 class527, int i) {
		String string = (String) (class527.anObjectArray7019[(class527.anInt7000 -= 1476624725) * 1806726141]);
		if (Class96_Sub14.method14642(string, class527, -305873852) != null) {
			string = string.substring(0, string.length() - 1);
		}
		class118.anObjectArray1383 = Class351.method6193(string, class527, -186222410);
		class118.aBool1384 = true;
	}

	static final void method5018(byte i) {
		if (Class9.anInt114 * 1923286921 != 2 && Class9.anInt114 * 1923286921 != 105) {
			do {
				try {
					int i_5_;
					if (904165171 * Class9.anInt104 == 0) {
						i_5_ = 250;
					} else {
						i_5_ = 2000;
					}
					if (Class9.aBool105 && 1923286921 * Class9.anInt114 >= 65) {
						i_5_ = 6000;
					}
					if (273 != 368603503 * Class9.anInt92 || (201 != 1923286921 * Class9.anInt114 && -1378127363 * Class9.anInt106 != 42)) {
						Class9.anInt90 += 1691497787;
					}
					if (Class9.anInt90 * 1676336115 > i_5_) {
						Class9.aClass184_73.method3051((byte) -84);
						if (Class9.anInt104 * 904165171 < 3) {
							if (368603503 * Class9.anInt92 == 273) {
								Class159.aClass450_2010.method7493(-541259802);
							} else {
								Class448.aClass450_5424.method7493(-1978908431);
							}
							Class9.anInt90 = 0;
							Class9.anInt104 += -1025483781;
							Class9.anInt114 = 69019906;
						} else {
							Class9.anInt114 = -469549710;
							Class8_Sub1.method13784(-5, -1415589664);
							break;
						}
					}
					if (18 == 1923286921 * Class9.anInt114) {
						if (273 == 368603503 * Class9.anInt92) {
							Class9.aClass184_73.method3050(Class38.method852(Class159.aClass450_2010.method7494((byte) 91), 15000, -215151896), Class159.aClass450_2010.host, (byte) -112);
						} else {
							Class9.aClass184_73.method3050(Class38.method852(Class448.aClass450_5424.method7494((byte) 75), 15000, 1248559901), Class448.aClass450_5424.host, (byte) -64);
						}
						Class9.aClass184_73.method3054((short) -6917);
						Class282_Sub23 class282_sub23 = Class207.method3558((byte) 88);
						if (Class9.aBool105) {
							class282_sub23.buffer.writeByte((1627920319 * Class369.aClass369_4276.anInt4278));
							class282_sub23.buffer.writeShort(0, 1417031095);
							int i_6_ = (-1990677291 * (class282_sub23.buffer.index));
							class282_sub23.buffer.writeInt(727);
							class282_sub23.buffer.writeInt(1);
							if (273 == 368603503 * Class9.anInt92) {
								class282_sub23.buffer.writeByte((10 == -1741204137 * client.anInt7166 ? 1 : 0));
							}
							RsByteBuffer class282_sub35 = Class94.method1587((byte) -40);
							class282_sub35.writeByte((1051306693 * Class9.anInt76));
							class282_sub35.writeShort((int) (Math.random() * 9.9999999E7), 1417031095);
							class282_sub35.writeByte(Class223.aClass495_2772.method243((byte) 1));
							class282_sub35.writeInt((client.anInt7149 * -1543197399));
							for (int i_7_ = 0; i_7_ < 6; i_7_++) {
								class282_sub35.writeInt((int) (Math.random() * 9.9999999E7));
							}
							class282_sub35.writeLong(2357923306087344989L * client.aLong7409);
							class282_sub35.writeByte(((client.aClass486_7450.anInt5746) * 1648080491));
							class282_sub35.writeByte((int) (Math.random() * 9.9999999E7));
							class282_sub35.applyRSA(Class18.LOGIN_EXPONENT, Class18.LOGIN_MODULUS);
							class282_sub23.buffer.writeBytes(class282_sub35.buffer, 0, class282_sub35.index * -1990677291);
							class282_sub23.buffer.method13281(-1990677291 * (class282_sub23.buffer.index) - i_6_, 1201423895);
						} else {
							class282_sub23.buffer.writeByte((Class369.aClass369_4272.anInt4278 * 1627920319));
						}
						Class9.aClass184_73.method3049(class282_sub23, 1846695910);
						Class9.aClass184_73.method3047(146720590);
						Class9.anInt114 = 372814667;
					}
					if (35 == 1923286921 * Class9.anInt114) {
						if (!Class9.aClass184_73.method3053((byte) -38).method3315(1, (byte) 74)) {
							break;
						}
						Class9.aClass184_73.method3053((byte) -94).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 1, -824634075);
						int i_8_ = ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[0]) & 0xff);
						if (i_8_ != 0) {
							Class9.anInt114 = -469549710;
							Class8_Sub1.method13784(i_8_, -2100127470);
							Class9.aClass184_73.method3051((byte) -115);
							Class361.method6270((byte) 36);
							break;
						}
						if (Class9.aBool105) {
							Class9.anInt114 = 2085258558;
						} else {
							Class9.anInt114 = -1602119216;
						}
					}
					if (46 == 1923286921 * Class9.anInt114) {
						if (!Class9.aClass184_73.method3053((byte) -18).method3315(2, (byte) 112)) {
							break;
						}
						Class9.aClass184_73.method3053((byte) -61).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 2, -1471346817);
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = 0;
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = Class9.aClass184_73.aClass282_Sub35_Sub2_2284.readUnsignedShort() * -1115476867;
						Class9.anInt114 = -497264847;
					}
					if (1923286921 * Class9.anInt114 == 57) {
						if (!Class9.aClass184_73.method3053((byte) -87).method3315(((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index) * -1990677291), (byte) 29)) {
							break;
						}
						Class9.aClass184_73.method3053((byte) -97).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, -1990677291 * (Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index), -1629870171);
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.method13100(Class500.anIntArray5827, 1168221158);
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = 0;
						String string = Class9.aClass184_73.aClass282_Sub35_Sub2_2284.method13091(-1864555618);
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = 0;
						String string_9_ = Class361.aClass361_4176.method6253((byte) -35);
						if (!client.aBool7310 || !Class186.method3082(string, 1, string_9_, 145298920)) {
							Class508.method8736(string, true, (Class393.aClass282_Sub54_4783.aClass468_Sub18_8230.method12776(-778399292) == 5), string_9_, client.aBool7158, client.aBool7159, (byte) -110);
						}
						Class9.anInt114 = 1919503609;
					}
					if (65 == 1923286921 * Class9.anInt114) {
						if (!Class9.aClass184_73.method3053((byte) -20).method3315(1, (byte) 54)) {
							break;
						}
						Class9.aClass184_73.method3053((byte) -23).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 1, -2143110299);
						if (1 == ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[0]) & 0xff)) {
							Class9.anInt114 = -428244941;
						}
					}
					if (1923286921 * Class9.anInt114 == 75) {
						if (!Class9.aClass184_73.method3053((byte) -32).method3315(16, (byte) 108)) {
							break;
						}
						Class9.aClass184_73.method3053((byte) -21).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 16, -1653161098);
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = -667760688;
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.method13100(Class500.anIntArray5827, 1118777738);
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = 0;
						Class9.aLong77 = (Class9.aClass184_73.aClass282_Sub35_Sub2_2284.method13087(798642301) * 2087908126908168589L);
						Class9.aLong86 = (Class9.aClass184_73.aClass282_Sub35_Sub2_2284.method13087(1832189608) * -3627564723025713089L);
						Class9.anInt114 = -1602119216;
					}
					if (80 == 1923286921 * Class9.anInt114) {
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = 0;
						Class9.aClass184_73.method3054((short) -24062);
						Class282_Sub23 class282_sub23 = Class207.method3558((byte) 82);
						RsBitsBuffer class282_sub35_sub2 = class282_sub23.buffer;
						if (368603503 * Class9.anInt92 == 273) {
							Class369 class369;
							if (Class9.aBool105) {
								class369 = Class369.aClass369_4269;
							} else {
								class369 = Class369.aClass369_4277;
							}
							class282_sub35_sub2.writeByte((class369.anInt4278 * 1627920319));
							class282_sub35_sub2.writeShort(0, 1417031095);
							int i_10_ = class282_sub35_sub2.index * -1990677291;
							int i_11_ = class282_sub35_sub2.index * -1990677291;
							if (!Class9.aBool105) {
								class282_sub35_sub2.writeInt(727);
								class282_sub35_sub2.writeInt(1);
								class282_sub35_sub2.writeByte((-1741204137 * client.anInt7166 == 10 ? 1 : 0));
								i_11_ = (-1990677291 * class282_sub35_sub2.index);
								RsByteBuffer class282_sub35 = Class108.method1847();
								class282_sub35_sub2.writeBytes(class282_sub35.buffer, 0, -1990677291 * class282_sub35.index);
								i_11_ = (class282_sub35_sub2.index * -1990677291);
								class282_sub35_sub2.writeByte((Class9.aLong77 * 8013795473128403269L == -1L) ? 1 : 0);
								if (8013795473128403269L * Class9.aLong77 == -1L) {
									class282_sub35_sub2.writeString(Class9.aString99);
								} else {
									class282_sub35_sub2.writeLong(Class9.aLong77 * 8013795473128403269L);
								}
							}
							class282_sub35_sub2.writeByte(Class158.method2730((short) -2638));
							class282_sub35_sub2.writeShort((-418109423 * (Class349.anInt4083)), 1417031095);
							class282_sub35_sub2.writeShort((-969250379 * (Class263.anInt3243)), 1417031095);
							class282_sub35_sub2.writeByte(Class393.aClass282_Sub54_4783.aClass468_Sub4_8223.method12641(919663007));
							Class31.method809(class282_sub35_sub2, 972141670);
							class282_sub35_sub2.writeString(client.aString7281);
							class282_sub35_sub2.writeInt((client.anInt7149 * -1543197399));
							RsByteBuffer class282_sub35 = Class393.aClass282_Sub54_4783.method13499(-1147561842);
							class282_sub35_sub2.writeByte((-1990677291 * (class282_sub35.index)));
							class282_sub35_sub2.writeBytes(class282_sub35.buffer, 0, -1990677291 * class282_sub35.index);
							client.aBool7175 = true;
							RsByteBuffer class282_sub35_12_ = (new RsByteBuffer(Class11.aClass282_Sub51_124.method13454(1537607861)));
							Class11.aClass282_Sub51_124.writeMachineInformation(class282_sub35_12_, 419814551);
							class282_sub35_sub2.writeBytes(class282_sub35_12_.buffer, 0, class282_sub35_12_.buffer.length);
							class282_sub35_sub2.writeInt((588706901 * (client.anInt7221)));
							class282_sub35_sub2.writeLong(client.aLong7153 * -8972807970865466165L);
							class282_sub35_sub2.writeString(Class464.aString5555);
							class282_sub35_sub2.writeByte((client.aString7156 == null) ? 0 : 1);
							if (client.aString7156 != null) {
								class282_sub35_sub2.writeString((client.aString7156));
							}
							class282_sub35_sub2.writeByte((Class47_Sub1.aClass262_9143.method4639("jagtheora", (short) 255) ? 1 : 0));
							class282_sub35_sub2.writeByte((client.aBool7310 ? 1 : 0));
							class282_sub35_sub2.writeByte((client.aBool7160 ? 1 : 0));
							class282_sub35_sub2.writeByte((-1439277437 * (Class227.anInt2831)));
							class282_sub35_sub2.writeInt((204524791 * (client.anInt7163)));
							class282_sub35_sub2.writeString(client.aString7164);
							class282_sub35_sub2.writeByte((null == Class448.aClass450_5421 || ((-87869981 * Class159.aClass450_2010.worldId) != (Class448.aClass450_5421.worldId * -87869981))) ? 1 : 0);
							int worldId = -87869981 * Class159.aClass450_2010.worldId;
							class282_sub35_sub2.writeInt(worldId);
							Class240.method4139(class282_sub35_sub2, 1259367589);
							class282_sub35_sub2.method13101(Class500.anIntArray5827, i_11_, class282_sub35_sub2.index * -1990677291, 1754334967);
							class282_sub35_sub2.method13281((-1990677291 * class282_sub35_sub2.index - i_10_), 1201423895);
						} else {
							Class369 class369;
							if (Class9.aBool105) {
								class369 = Class369.aClass369_4269;
							} else {
								class369 = Class369.aClass369_4270;
							}
							class282_sub35_sub2.writeByte((1627920319 * (class369.anInt4278)));
							class282_sub35_sub2.writeShort(0, 1417031095);
							int i_13_ = class282_sub35_sub2.index * -1990677291;
							int i_14_ = class282_sub35_sub2.index * -1990677291;
							if (!Class9.aBool105) {
								class282_sub35_sub2.writeInt(727);
								class282_sub35_sub2.writeInt(1);
								RsByteBuffer class282_sub35 = Class108.method1847();
								class282_sub35_sub2.writeBytes(class282_sub35.buffer, 0, -1990677291 * class282_sub35.index);
								i_14_ = (-1990677291 * class282_sub35_sub2.index);
								class282_sub35_sub2.writeByte((Class9.aLong77 * 8013795473128403269L == -1L) ? 1 : 0);
								if (Class9.aLong77 * 8013795473128403269L == -1L) {
									class282_sub35_sub2.writeString(Class9.aString99);
								} else {
									class282_sub35_sub2.writeLong(Class9.aLong77 * 8013795473128403269L);
								}
							}
							class282_sub35_sub2.writeByte(((client.aClass486_7450.anInt5746) * 1648080491));
							class282_sub35_sub2.writeByte(Class223.aClass495_2772.method243((byte) 1));
							Class31.method809(class282_sub35_sub2, -378154884);
							class282_sub35_sub2.writeString(client.aString7281);
							RsByteBuffer class282_sub35 = Class393.aClass282_Sub54_4783.method13499(-1147561842);
							class282_sub35_sub2.writeByte(((class282_sub35.index) * -1990677291));
							class282_sub35_sub2.writeBytes(class282_sub35.buffer, 0, -1990677291 * class282_sub35.index);
							RsByteBuffer class282_sub35_15_ = (new RsByteBuffer(Class11.aClass282_Sub51_124.method13454(995682121)));
							Class11.aClass282_Sub51_124.writeMachineInformation(class282_sub35_15_, 419814551);
							class282_sub35_sub2.writeBytes(class282_sub35_15_.buffer, 0, class282_sub35_15_.buffer.length);
							class282_sub35_sub2.writeString(Class464.aString5555);
							class282_sub35_sub2.writeInt((client.anInt7149 * -1543197399));
							class282_sub35_sub2.writeInt((client.anInt7163 * 204524791));
							class282_sub35_sub2.writeString(client.aString7164);
							Class240.method4139(class282_sub35_sub2, 860014211);
							class282_sub35_sub2.method13101(Class500.anIntArray5827, i_14_, -1990677291 * class282_sub35_sub2.index, 1203865118);
							class282_sub35_sub2.method13281((class282_sub35_sub2.index * -1990677291 - i_13_), 1201423895);
						}
						Class9.aClass184_73.method3049(class282_sub23, 96752034);
						Class9.aClass184_73.method3047(124172931);
						Class9.aClass184_73.aClass432_2283 = new IsaacCipher(Class500.anIntArray5827);
						for (int i_16_ = 0; i_16_ < 4; i_16_++) {
							Class500.anIntArray5827[i_16_] += 50;
						}
						Class9.aClass184_73.aClass432_2295 = new IsaacCipher(Class500.anIntArray5827);
						new IsaacCipher(Class500.anIntArray5827);
						Class9.aClass184_73.aClass282_Sub35_Sub2_2284.method14867(Class9.aClass184_73.aClass432_2295, 1851257503);
						Class500.anIntArray5827 = null;
						Class9.anInt114 = -1298324455;
					}
					if (97 == 1923286921 * Class9.anInt114) {
						if (!Class9.aClass184_73.method3053((byte) -12).method3315(1, (byte) 99)) {
							break;
						}
						Class9.aClass184_73.method3053((byte) -109).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 1, -973047243);
						int i_17_ = ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[0]) & 0xff);
						if (21 == i_17_) {
							Class9.anInt114 = 13589632;
						} else if (i_17_ == 29 || 45 == i_17_) {
							Class110.anInt1105 = 402538069 * i_17_;
							Class9.anInt114 = 690199060;
						} else {
							if (1 == i_17_) {
								Class9.anInt114 = 1118444001;
								Class8_Sub1.method13784(i_17_, -1633539224);
								break;
							}
							if (i_17_ == 2) {
								Class9.anInt114 = -1160284643;
							} else if (i_17_ == 15) {
								Class9.aClass184_73.anInt2287 = 1281181758;
								Class9.anInt114 = 1228768676;
							} else {
								if (23 == i_17_ && Class9.anInt104 * 904165171 < 3) {
									Class9.anInt90 = 0;
									Class9.anInt104 += -1025483781;
									Class9.anInt114 = 69019906;
									Class9.aClass184_73.method3051((byte) -29);
								} else {
									if (i_17_ == 42) {
										Class9.anInt114 = 54894401;
										Class8_Sub1.method13784(i_17_, -1894609821);
									} else {
										if (Class9.aBool74 && !Class9.aBool105 && (-1 != 1051306693 * Class9.anInt76) && i_17_ == 35) {
											Class9.aBool105 = true;
											Class9.anInt90 = 0;
											Class9.anInt114 = 69019906;
											Class9.aClass184_73.method3051((byte) -19);
										} else {
											Class9.anInt114 = -469549710;
											Class8_Sub1.method13784(i_17_, -476487061);
											Class9.aClass184_73.method3051((byte) -84);
											Class361.method6270((byte) 102);
											break;
										}
										break;
									}
									break;
								}
								break;
							}
						}
					}
					if (Class9.anInt114 * 1923286921 == 115) {
						Class9.aClass184_73.method3054((short) 7627);
						Class282_Sub23 class282_sub23 = Class207.method3558((byte) 32);
						RsBitsBuffer class282_sub35_sub2 = class282_sub23.buffer;
						class282_sub35_sub2.method14867((Class9.aClass184_73.aClass432_2283), 271369997);
						class282_sub35_sub2.method14883(((Class369.aClass369_4273.anInt4278) * 1627920319), 264695091);
						Class9.aClass184_73.method3049(class282_sub23, 132701015);
						Class9.aClass184_73.method3047(-692056530);
						Class9.anInt114 = -1298324455;
					} else if (Class9.anInt114 * 1923286921 == 128) {
						if (Class9.aClass184_73.method3053((byte) -36).method3315(1, (byte) 47)) {
							Class9.aClass184_73.method3053((byte) -43).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 1, -587532919);
							int i_18_ = ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[0]) & 0xff);
							Class9.anInt103 = i_18_ * 547570654;
							Class9.anInt114 = -469549710;
							Class8_Sub1.method13784(21, 17799798);
							Class9.aClass184_73.method3051((byte) -76);
							Class361.method6270((byte) 74);
						}
					} else if (201 == 1923286921 * Class9.anInt114) {
						if (Class9.aClass184_73.method3053((byte) -102).method3315(2, (byte) 20)) {
							Class9.aClass184_73.method3053((byte) -21).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 2, -579379615);
							Class9.anInt113 = ((((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[0]) & 0xff) << 8) + ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[1]) & 0xff)) * 1532398177;
							Class9.anInt114 = -1298324455;
						}
					} else if (180 == Class9.anInt114 * 1923286921) {
						if (-1857761539 * Class110.anInt1105 == 29) {
							if (!Class9.aClass184_73.method3053((byte) -54).method3315(1, (byte) 23)) {
								break;
							}
							Class9.aClass184_73.method3053((byte) -29).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 1, -1191779145);
							Class9.anInt112 = ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[0]) & 0xff) * -1260694625;
						} else if (Class110.anInt1105 * -1857761539 == 45) {
							if (!Class9.aClass184_73.method3053((byte) -110).method3315(3, (byte) 26)) {
								break;
							}
							Class9.aClass184_73.method3053((byte) -123).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 3, -1876355319);
							Class9.anInt112 = ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[0]) & 0xff) * -1260694625;
							Class9.anInt94 = (((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[2]) & 0xff) + (((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[1]) & 0xff) << 8)) * 605935537;
						} else {
							throw new IllegalStateException();
						}
						Class9.anInt114 = -469549710;
						Class8_Sub1.method13784((-1857761539 * Class110.anInt1105), -612160283);
						Class9.aClass184_73.method3051((byte) -74);
						Class361.method6270((byte) 89);
					} else if (133 == 1923286921 * Class9.anInt114) {
						if (Class9.aClass184_73.method3053((byte) -85).method3315(1, (byte) 60)) {
							Class9.aClass184_73.method3053((byte) -2).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 1, -1154230735);
							Class153.anInt1965 = (1058974525 * ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer[0]) & 0xff));
							Class9.anInt114 = 1491258668;
						}
					} else {
						if (140 == Class9.anInt114 * 1923286921) {
							RsBitsBuffer class282_sub35_sub2 = (Class9.aClass184_73.aClass282_Sub35_Sub2_2284);
							if (273 == Class9.anInt92 * 368603503) {
								if (!Class9.aClass184_73.method3053((byte) -4).method3315(1878721557 * Class153.anInt1965, (byte) 38)) {
									break;
								}
								Class9.aClass184_73.method3053((byte) -24).method3327(class282_sub35_sub2.buffer, 0, Class153.anInt1965 * 1878721557, -1595148052);
								class282_sub35_sub2.index = 0;
								client.anInt7318 = (class282_sub35_sub2.readUnsignedByte() * -1751605587);
								client.anInt7319 = (class282_sub35_sub2.readUnsignedByte() * -1574059633);
								client.aBool7224 = class282_sub35_sub2.readUnsignedByte() == 1;
								client.aBool7244 = class282_sub35_sub2.readUnsignedByte() == 1;
								client.aBool7322 = class282_sub35_sub2.readUnsignedByte() == 1;
								client.aBool7325 = class282_sub35_sub2.readUnsignedByte() == 1;
								client.anInt7315 = (class282_sub35_sub2.readUnsignedShort() * -1265803515);
								client.aBool7316 = class282_sub35_sub2.readUnsignedByte() == 1;
								Class504.anInt5832 = (class282_sub35_sub2.method13083(1818887431) * 1346472513);
								client.aBool7317 = class282_sub35_sub2.readUnsignedByte() == 1;
								Class311.aString3643 = class282_sub35_sub2.readString(2109747310);
								client.aClass257_7353.method4436(-1683757670).method7912(client.aBool7317, 65280);
								Class6.aClass340_45.method6052((short) 2983).method4436(-1487690474).method7912(client.aBool7317, 65280);
								Class119.aClass426_1463.method7148(client.aBool7317, -1605655563);
								Class350_Sub1.aClass406_7757.method6847(client.aBool7317, (byte) 53);
							} else {
								if (!Class9.aClass184_73.method3053((byte) -33).method3315(1878721557 * Class153.anInt1965, (byte) 63)) {
									break;
								}
								Class9.aClass184_73.method3053((byte) -48).method3327(class282_sub35_sub2.buffer, 0, Class153.anInt1965 * 1878721557, -1443269131);
								class282_sub35_sub2.index = 0;
								client.anInt7318 = (class282_sub35_sub2.readUnsignedByte() * -1751605587);
								client.anInt7319 = (class282_sub35_sub2.readUnsignedByte() * -1574059633);
								client.aBool7224 = class282_sub35_sub2.readUnsignedByte() == 1;
								Class504.anInt5832 = class282_sub35_sub2.method13083(979570289) * 1346472513;
								Class84.myPlayer.aByte10549 = (byte) class282_sub35_sub2.readUnsignedByte();
								client.aBool7244 = class282_sub35_sub2.readUnsignedByte() == 1;
								client.aBool7322 = class282_sub35_sub2.readUnsignedByte() == 1;
								Class116.aLong1259 = (class282_sub35_sub2.method13087(1319458246) * -3354591821341329229L);
								Class43.aLong420 = (((-6750871795187943813L * Class116.aLong1259) - Class169.method2869(1922966932) - class282_sub35_sub2.method13189(-315353616)) * -4379508303603430445L);
								int i_19_ = class282_sub35_sub2.readUnsignedByte();
								client.aBool7316 = 0 != (i_19_ & 0x1);
								Class509.aBool5870 = 0 != (i_19_ & 0x2);
								Class354.anInt4112 = class282_sub35_sub2.readIntLE() * 544371757;
								Class469.aBool5585 = class282_sub35_sub2.readUnsignedByte() == 1;
								Class232.anInt2879 = (class282_sub35_sub2.readIntLE() * -336113183);
								Class115.anInt1247 = (class282_sub35_sub2.readUnsignedShort() * -997680369);
								Class417.anInt4994 = (class282_sub35_sub2.readUnsignedShort() * 1391940615);
								Class468_Sub28.anInt7961 = (class282_sub35_sub2.readUnsignedShort() * -851908195);
								Class125.anInt1572 = class282_sub35_sub2.readIntLE() * 166473323;
								Class119.aClass12_1461 = new Class12(-1284370877 * Class125.anInt1572);
								new Thread(Class119.aClass12_1461).start();
								Class221.anInt2762 = class282_sub35_sub2.readUnsignedByte() * -301602253;
								Class96_Sub1.anInt8307 = (class282_sub35_sub2.readUnsignedShort() * 1624089299);
								Class440.anInt5357 = (class282_sub35_sub2.readUnsignedShort() * 1616992353);
								Class464.aBool5556 = class282_sub35_sub2.readUnsignedByte() == 1;
								Class84.myPlayer.aString10546 = Class84.myPlayer.aString10563 = RuntimeException_Sub3.aString10458 = class282_sub35_sub2.method13091(540818903);
								Class311.anInt3644 = class282_sub35_sub2.readUnsignedByte() * 1500703337;
								Class121.anInt1526 = (class282_sub35_sub2.readIntLE() * 1336332893);
								client.aBool7323 = class282_sub35_sub2.readUnsignedByte() == 1;
								Class448.aClass450_5421 = new Class450();
								Class448.aClass450_5421.worldId = (class282_sub35_sub2.readUnsignedShort() * -1933199413);
								if ((Class448.aClass450_5421.worldId * -87869981) == 65535) {
									Class448.aClass450_5421.worldId = 1933199413;
								}
								Class448.aClass450_5421.host = class282_sub35_sub2.method13091(-1718279524);
								if (Class90.aClass496_952 != Class496.aClass496_5813) {
									Class448.aClass450_5421.anInt5434 = (1140744768 + -58916693 * (Class448.aClass450_5421.worldId));
									Class448.aClass450_5421.anInt5437 = (-1441381029 * (Class448.aClass450_5421.worldId) + 1250363344);
								}
								if ((Class90.aClass496_952 != Class496.aClass496_5810) && ((Class90.aClass496_952 != Class496.aClass496_5808) || -644057819 * client.anInt7318 < 2) && (Class159.aClass450_2010.method7491(Class448.aClass450_5420, 240325775))) {
									Class274.method4884((byte) 74);
								}
							}
							if (client.aBool7224 && !client.aBool7322 || client.aBool7316) {
								try {
									Class441.method7377((Class282_Sub44.anApplet8065), "zap", 1696424400);
								} catch (Throwable throwable) {
									if (client.aBool7147) {
										try {
											Class282_Sub44.anApplet8065.getAppletContext().showDocument(new URL(Class282_Sub44.anApplet8065.getCodeBase(), "blank.ws"), "tbi");
										} catch (Exception exception) {
											/* empty */
										}
									}
								}
							} else {
								try {
									Class441.method7377((Class282_Sub44.anApplet8065), "unzap", 1103714597);
								} catch (Throwable throwable) {
									/* empty */
								}
							}
							if (Class496.aClass496_5813 == Class90.aClass496_952) {
								Class361.aClass361_4170.method6257(1331971946);
							}
							if (Class9.anInt92 * 368603503 == 273) {
								Class9.anInt114 = -552695121;
							} else {
								Class9.anInt114 = -469549710;
								Class8_Sub1.method13784(2, -1482799);
								IncommingPacket.method6377(-180656847);
								Class365.method6298(0, 1786778195);
								Class9.aClass184_73.aClass375_2286 = null;
								break;
							}
						}
						if (167 == 1923286921 * Class9.anInt114) {
							if (!Class9.aClass184_73.method3053((byte) -128).method3315(3, (byte) 82)) {
								break;
							}
							Class9.aClass184_73.method3053((byte) -81).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 3, -2132354813);
							Class9.anInt114 = -1961344251;
						}
						if (173 == 1923286921 * Class9.anInt114) {
							RsBitsBuffer stream = (Class9.aClass184_73.aClass282_Sub35_Sub2_2284);
							stream.index = 0;
							if (stream.method14869((short) -4782)) {
								if (!Class9.aClass184_73.method3053((byte) -118).method3315(1, (byte) 83)) {
									break;
								}
								Class9.aClass184_73.method3053((byte) -78).method3327(stream.buffer, 3, 1, -1787426101);
							}
							int id = stream.method14870();
							System.out.println("Requesting packet: " + id);
							Class9.aClass184_73.aClass375_2286 = (Class60.getIncommingPacket()[id]);
							Class9.aClass184_73.anInt2287 = (stream.readUnsignedShort() * -640590879);
							Class9.anInt114 = 2029828284;
						}
						if (156 == Class9.anInt114 * 1923286921) {
							if (Class9.aClass184_73.method3053((byte) -89).method3315((Class9.aClass184_73.anInt2287 * 715663393), (byte) 24)) {
								Class9.aClass184_73.method3053((byte) -99).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, (Class9.aClass184_73.anInt2287) * 715663393, -1845568103);
								Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = 0;
								int i_20_ = ((Class9.aClass184_73.anInt2287) * 715663393);
								Class9.anInt114 = -469549710;
								Class8_Sub1.method13784(2, -293580682);
								Class190.method3152((byte) 50);
								Class197.method3203((Class9.aClass184_73.aClass282_Sub35_Sub2_2284), (byte) 87);
								int i_21_ = i_20_ - (Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index) * -1990677291;
								RsBitsBuffer class282_sub35_sub2 = new RsBitsBuffer(i_21_);
								System.arraycopy((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), ((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index) * -1990677291), class282_sub35_sub2.buffer, 0, i_21_);
								Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index += -1115476867 * i_21_;
								if (IncommingPacket.aClass375_4503 == (Class9.aClass184_73.aClass375_2286)) {
									client.aClass257_7353.method4499(new Class335(Class256.aClass256_3155, class282_sub35_sub2), 356381544);
								} else {
									client.aClass257_7353.method4499(new Class335(Class256.aClass256_3158, class282_sub35_sub2), 1199163537);
								}
								if ((-1990677291 * (Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index)) != i_20_) {
									throw new RuntimeException(new StringBuilder().append(-1990677291 * (Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index)).append(" ").append(i_20_).toString());
								}
								Class9.aClass184_73.aClass375_2286 = null;
							}
						} else {
							if (1923286921 * Class9.anInt114 != 196) {
								break;
							}
							if ((715663393 * Class9.aClass184_73.anInt2287) == -2) {
								if (!Class9.aClass184_73.method3053((byte) -123).method3315(2, (byte) 42)) {
									break;
								}
								Class9.aClass184_73.method3053((byte) -35).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, 2, -698163042);
								Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = 0;
								Class9.aClass184_73.anInt2287 = (Class9.aClass184_73.aClass282_Sub35_Sub2_2284.readUnsignedShort() * -640590879);
							}
							if (Class9.aClass184_73.method3053((byte) -40).method3315(715663393 * (Class9.aClass184_73.anInt2287), (byte) 74)) {
								Class9.aClass184_73.method3053((byte) -30).method3327((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.buffer), 0, (Class9.aClass184_73.anInt2287) * 715663393, -1006309637);
								Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index = 0;
								int i_22_ = ((Class9.aClass184_73.anInt2287) * 715663393);
								Class9.anInt114 = -469549710;
								Class8_Sub1.method13784(15, 481770271);
								Class501.method8350(2119490701);
								Class197.method3203((Class9.aClass184_73.aClass282_Sub35_Sub2_2284), (byte) 72);
								if (((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index) * -1990677291) != i_22_) {
									throw new RuntimeException(new StringBuilder().append((Class9.aClass184_73.aClass282_Sub35_Sub2_2284.index) * -1990677291).append(" ").append(i_22_).toString());
								}
								Class9.aClass184_73.aClass375_2286 = null;
							}
						}
					}
				} catch (IOException ioexception) {
					Class9.aClass184_73.method3051((byte) -32);
					if (Class9.anInt104 * 904165171 < 3) {
						if (Class9.anInt92 * 368603503 == 273) {
							Class159.aClass450_2010.method7493(-803067892);
						} else {
							Class448.aClass450_5424.method7493(1578091404);
						}
						Class9.anInt90 = 0;
						Class9.anInt104 += -1025483781;
						Class9.anInt114 = 69019906;
					} else {
						Class9.anInt114 = -469549710;
						Class8_Sub1.method13784(-4, -1690933680);
						Class361.method6270((byte) 123);
					}
					break;
				}
				break;
			} while (false);
		}
	}
}
