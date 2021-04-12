
/* Class282_Sub35 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.math.BigInteger;

public class RsByteBuffer extends Class282 {
	public static long[] aLongArray7979;
	public int index;
	public static final int anInt7981 = 5000;
	public byte[] buffer;
	static final int anInt7983 = -306674912;
	static final long aLong7984 = -3932672073523589310L;
	public static final int anInt7985 = 100;
	static int[] anIntArray7986 = new int[256];

	public void method13058(byte[] is, int i, int i_0_) {
		for (int i_1_ = i; i_1_ < i + i_0_; i_1_++) {
			is[i_1_] = buffer[(index += -1115476867) * -1990677291 - 1];
		}
	}

	public RsByteBuffer(byte[] is) {
		buffer = is;
		index = 0;
	}

	public void method13059(int i) {
		if (null != buffer) {
			Class351.method6197(buffer, -1154101175);
		}
		buffer = null;
	}

	public void writeByte(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13061(int i, int i_3_) { //readByte? i think
		buffer[-1990677291 * index - i - 1] = (byte) i;
	}

	public void method13062(int i, byte i_4_) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public void method13063(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public void writeInt(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public int method13065() {
		index += -166940172;
		return ((buffer[index * -1990677291 - 4] & 0xff) + (((buffer[index * -1990677291 - 3] & 0xff) << 8) + (((buffer[-1990677291 * index - 2] & 0xff) << 16) + ((buffer[-1990677291 * index - 1] & 0xff) << 24))));
	}

	public void writeShortLE(int i, short i_6_) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public void writeLong(long l) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 56);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 48);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 40);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 32);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) l;
	}

	public void method13068(long l, int i, int i_7_) {
		if (--i < 0 || i > 7) {
			throw new IllegalArgumentException();
		}
		for (int i_8_ = i * 8; i_8_ >= 0; i_8_ -= 8) {
			buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> i_8_);
		}
	}

	public byte method13069() {
		return buffer[(index += -1115476867) * -1990677291 - 1];
	}

	public void method13070(String string, int i) {
		int i_9_ = string.indexOf('\0');
		if (i_9_ >= 0) {
			throw new IllegalArgumentException("");
		}
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
		index += (Class317.method5693(string, 0, string.length(), buffer, -1990677291 * index, -74015201) * -1115476867);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
	}

	public void method13071(CharSequence charsequence, byte i) {
		int i_10_ = charsequence.length();
		int i_11_ = 0;
		for (int i_12_ = 0; i_12_ < i_10_; i_12_++) {
			char c = charsequence.charAt(i_12_);
			if (c <= '\u007f') {
				i_11_++;
			} else if (c <= '\u07ff') {
				i_11_ += 2;
			} else {
				i_11_ += 3;
			}
		}
		int i_13_ = i_11_;
		i_10_ = i_13_;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
		method13077(i_10_, 1646116683);
		index += Class330.method5914(buffer, -1990677291 * index, charsequence, -1772783727) * -1115476867;
	}

	public void writeBytes(byte[] is, int i, int i_14_) {
		for (int i_16_ = i; i_16_ < i_14_ + i; i_16_++) {
			buffer[(index += -1115476867) * -1990677291 - 1] = is[i_16_];
		}
	}

	public void method13073(int i, int i_17_) {
		buffer[-1990677291 * index - i - 4] = (byte) (i >> 24);
		buffer[index * -1990677291 - i - 3] = (byte) (i >> 16);
		buffer[-1990677291 * index - i - 2] = (byte) (i >> 8);
		buffer[index * -1990677291 - i - 1] = (byte) i;
	}

	public int readUnsignedShort128() {
		index += 2064013562;
		return (((buffer[-1990677291 * index - 2] & 0xff) << 8) + (buffer[index * -1990677291 - 1] - 128 & 0xff));
	}

	public void method13075(int i, int i_18_) {//name? 
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (128 + i);
	}

	public void method13076(int i, int i_19_) {
		if (i >= 0 && i < 128) {
			writeByte(i);
		} else if (i >= 0 && i < 32768) {
			writeShort(32768 + i, 1417031095);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void method13077(int i, int i_20_) {
		if (0 != (i & ~0x7f)) {
			if ((i & ~0x3fff) != 0) {
				if (0 != (i & ~0x1fffff)) {
					if (0 != (i & ~0xfffffff)) {
						writeByte(i >>> 28 | 0x80);
					}
					writeByte(i >>> 21 | 0x80);
				}
				writeByte(i >>> 14 | 0x80);
			}
			writeByte(i >>> 7 | 0x80);
		}
		writeByte(i & 0x7f);
	}

	public int readUnsignedByte() {
		return (buffer[(index += -1115476867) * -1990677291 - 1] & 0xff);
	}

	static int method13079(byte[] is, int i, int i_21_) {
		int i_22_ = -1;
		for (int i_23_ = i; i_23_ < i_21_; i_23_++) {
			i_22_ = i_22_ >>> 8 ^ anIntArray7986[(i_22_ ^ is[i_23_]) & 0xff];
		}
		i_22_ ^= 0xffffffff;
		return i_22_;
	}

	public int readUnsignedShort() {
		index += 2064013562;
		return (((buffer[-1990677291 * index - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
	}

	public int method13081(int i) {
		index += 2064013562;
		int i_24_ = ((buffer[index * -1990677291 - 1] & 0xff) + ((buffer[index * -1990677291 - 2] & 0xff) << 8));
		if (i_24_ > 32767) {
			i_24_ -= 65536;
		}
		return i_24_;
	}

	public int method13082(short i) {
		index += 948536695;
		return (((buffer[index * -1990677291 - 2] & 0xff) << 8) + ((buffer[index * -1990677291 - 3] & 0xff) << 16) + (buffer[-1990677291 * index - 1] & 0xff));
	}

	public int method13083(int i) {
		index += 948536695;
		int i_25_ = (((buffer[-1990677291 * index - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
		if (i_25_ > 8388607) {
			i_25_ -= 16777216;
		}
		return i_25_;
	}

	public int method13084() {
		index += 2064013562;
		return ((buffer[index * -1990677291 - 2] & 0xff) + ((buffer[-1990677291 * index - 1] & 0xff) << 8));
	}

	public int method13085(int i) {
		index += -166940172;
		return ((buffer[index * -1990677291 - 4] & 0xff) + (((buffer[index * -1990677291 - 3] & 0xff) << 8) + (((buffer[-1990677291 * index - 2] & 0xff) << 16) + ((buffer[-1990677291 * index - 1] & 0xff) << 24))));
	}

	public void method13086(int i) {
		if (i >= 0 && i < 128) {
			writeByte(i);
		} else if (i >= 0 && i < 32768) {
			writeShort(32768 + i, 1417031095);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public long method13087(int i) {
		long l = readIntLE() & 0xffffffffL;
		long l_26_ = readIntLE() & 0xffffffffL;
		return l_26_ + (l << 32);
	}

	public void writeShort(int i, int i_27_) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public long method13089(int i, int i_28_) {
		if (--i < 0 || i > 7) {
			throw new IllegalArgumentException();
		}
		int i_29_ = i * 8;
		long l = 0L;
		for (/**/; i_29_ >= 0; i_29_ -= 8) {
			l |= ((buffer[(index += -1115476867) * -1990677291 - 1]) & 0xffL) << i_29_;
		}
		return l;
	}

	public String method13090(int i) {
		if (buffer[-1990677291 * index] == 0) {
			index += -1115476867;
			return null;
		}
		return readString(613401213);
	}

	public RsByteBuffer(int i) {
		buffer = Class491.method8239(i, 1959390720);
		index = 0;
	}

	public String method13091(int i) {
		byte i_30_ = buffer[(index += -1115476867) * -1990677291 - 1];
		if (0 != i_30_) {
			throw new IllegalStateException("");
		}
		int i_31_ = -1990677291 * index;
		while (buffer[(index += -1115476867) * -1990677291 - 1] != 0) {
			/* empty */
		}
		int i_32_ = index * -1990677291 - i_31_ - 1;
		if (i_32_ == 0) {
			return "";
		}
		return Class344.method6118(buffer, i_31_, i_32_, (byte) -81);
	}

	public void method13092(int[] is) {
		int i = index * -1990677291 / 8;
		index = 0;
		for (int i_33_ = 0; i_33_ < i; i_33_++) {
			int i_34_ = readIntLE();
			int i_35_ = readIntLE();
			int i_36_ = -957401312;
			int i_37_ = -1640531527;
			int i_38_ = 32;
			while (i_38_-- > 0) {
				i_35_ -= ((i_34_ << 4 ^ i_34_ >>> 5) + i_34_ ^ is[i_36_ >>> 11 & 0x3] + i_36_);
				i_36_ -= i_37_;
				i_34_ -= (i_35_ + (i_35_ << 4 ^ i_35_ >>> 5) ^ is[i_36_ & 0x3] + i_36_);
			}
			index -= -333880344;
			writeInt(i_34_);
			writeInt(i_35_);
		}
	}

	public static int method13093(String string) {
		return string.length() + 2;
	}

	public int method13094(int i) {
		int i_39_ = buffer[-1990677291 * index] & 0xff;
		if (i_39_ < 128) {
			return readUnsignedByte() - 64;
		}
		return readUnsignedShort() - 49152;
	}

	public void method13095(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
	}

	public int method13096(int i) {
		int i_40_ = 0;
		int i_41_;
		for (i_41_ = readUnsignedSmart(2017494920); i_41_ == 32767; i_41_ = readUnsignedSmart(1731195561)) {
			i_40_ += 32767;
		}
		i_40_ += i_41_;
		return i_40_;
	}

	public int method13097(byte i) {
		if (buffer[index * -1990677291] < 0) {
			return readIntLE() & 0x7fffffff;
		}
		return readUnsignedShort();
	}

	public void method13098(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public int method13099(int i) {
		int i_42_ = buffer[(index += -1115476867) * -1990677291 - 1];
		int i_43_ = 0;
		for (/**/; i_42_ < 0; i_42_ = (buffer[(index += -1115476867) * -1990677291 - 1])) {
			i_43_ = (i_43_ | i_42_ & 0x7f) << 7;
		}
		return i_43_ | i_42_;
	}

	public void method13100(int[] is, int i) {
		int i_44_ = index * -1990677291 / 8;
		index = 0;
		for (int i_45_ = 0; i_45_ < i_44_; i_45_++) {
			int i_46_ = readIntLE();
			int i_47_ = readIntLE();
			int i_48_ = -957401312;
			int i_49_ = -1640531527;
			int i_50_ = 32;
			while (i_50_-- > 0) {
				i_47_ -= ((i_46_ << 4 ^ i_46_ >>> 5) + i_46_ ^ is[i_48_ >>> 11 & 0x3] + i_48_);
				i_48_ -= i_49_;
				i_46_ -= (i_47_ + (i_47_ << 4 ^ i_47_ >>> 5) ^ is[i_48_ & 0x3] + i_48_);
			}
			index -= -333880344;
			writeInt(i_46_);
			writeInt(i_47_);
		}
	}

	public void method13101(int[] is, int i, int i_51_, int i_52_) {
		int i_53_ = index * -1990677291;
		index = -1115476867 * i;
		int i_54_ = (i_51_ - i) / 8;
		for (int i_55_ = 0; i_55_ < i_54_; i_55_++) {
			int i_56_ = readIntLE();
			int i_57_ = readIntLE();
			int i_58_ = 0;
			int i_59_ = -1640531527;
			int i_60_ = 32;
			while (i_60_-- > 0) {
				i_56_ += (i_57_ + (i_57_ << 4 ^ i_57_ >>> 5) ^ is[i_58_ & 0x3] + i_58_);
				i_58_ += i_59_;
				i_57_ += (i_56_ + (i_56_ << 4 ^ i_56_ >>> 5) ^ i_58_ + is[i_58_ >>> 11 & 0x3]);
			}
			index -= -333880344;
			writeInt(i_56_);
			writeInt(i_57_);
		}
		index = -1115476867 * i_53_;
	}

	public void method13102(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (128 + i);
	}

	public void applyRSA(BigInteger biginteger, BigInteger biginteger_61_) {
		int i_62_ = index * -1990677291;
		index = 0;
		byte[] is = new byte[i_62_];
		readBytes(is, 0, i_62_, 1787887302);
		BigInteger biginteger_63_ = new BigInteger(is);
		BigInteger biginteger_64_ = biginteger_63_.modPow(biginteger, biginteger_61_);
		byte[] is_65_ = biginteger_64_.toByteArray();
		index = 0;
		writeShort(is_65_.length, 1417031095);
		writeBytes(is_65_, 0, is_65_.length);
	}

	public int method13104(int i, int i_66_) {
		int i_67_ = Class455.method7559(buffer, i, -1990677291 * index, -1209894190);
		writeInt(i_67_);
		return i_67_;
	}

	public boolean method13105(byte i) {
		index -= -166940172;
		int i_68_ = Class455.method7559(buffer, 0, -1990677291 * index, -1770060686);
		int i_69_ = readIntLE();
		if (i_68_ == i_69_) {
			return true;
		}
		return false;
	}

	public static int method13106(byte[] is, int i) {
		return Class455.method7559(is, 0, i, -1085121585);
	}

	public void readUnsignedByteC(int i, int i_70_) { //edited by me
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (0 - i);
	}

	public void write128Byte(int i, int i_71_) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (128 - i);
	}

	public void method13109(long l) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 56);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 48);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 40);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 32);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) l;
	}

	public int readUnsignedByte128() {
		return ((buffer[(index += -1115476867) * -1990677291 - 1] - 128) & 0xff);
	}

	public int readUnsigned128Byte() {
		return (128 - (buffer[(index += -1115476867) * -1990677291 - 1]) & 0xff);
	}

	public byte readByte128(int i) {
		return (byte) ((buffer[(index += -1115476867) * -1990677291 - 1]) - 128);
	}

	public String method13113() {
		byte i = buffer[(index += -1115476867) * -1990677291 - 1];
		if (0 != i) {
			throw new IllegalStateException("");
		}
		int i_72_ = -1990677291 * index;
		while (buffer[(index += -1115476867) * -1990677291 - 1] != 0) {
			/* empty */
		}
		int i_73_ = index * -1990677291 - i_72_ - 1;
		if (i_73_ == 0) {
			return "";
		}
		return Class344.method6118(buffer, i_72_, i_73_, (byte) -127);
	}

	public byte read128Byte(short i) {
		return (byte) (128 - (buffer[(index += -1115476867) * -1990677291 - 1]));
	}

	public int method13115(int i) {
		int i_74_ = Class455.method7559(buffer, i, -1990677291 * index, -639674292);
		writeInt(i_74_);
		return i_74_;
	}

	public void writeShort128(int i, int i_75_) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (128 + i);
	}

	public void readShortLE128(int i, int i_76_) { //edited by me
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i + 128);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public int readUnsignedShortLE(byte i) {
		index += 2064013562;
		return ((buffer[index * -1990677291 - 2] & 0xff) + ((buffer[-1990677291 * index - 1] & 0xff) << 8));
	}

	public void method13119(String string) {
		int i = string.indexOf('\0');
		if (i >= 0) {
			throw new IllegalArgumentException("");
		}
		index += (Class317.method5693(string, 0, string.length(), buffer, -1990677291 * index, -74015201) * -1115476867);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
	}

	public int readUnsignedShortLE128(int i) {
		index += 2064013562;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 8) + (buffer[index * -1990677291 - 2] - 128 & 0xff));
	}

	public int method13121(int i) {
		index += 2064013562;
		int i_77_ = ((buffer[-1990677291 * index - 2] & 0xff) + ((buffer[-1990677291 * index - 1] & 0xff) << 8));
		if (i_77_ > 32767) {
			i_77_ -= 65536;
		}
		return i_77_;
	}

	public int method13122(int i) {
		index += 2064013562;
		int i_78_ = ((buffer[-1990677291 * index - 1] - 128 & 0xff) + ((buffer[index * -1990677291 - 2] & 0xff) << 8));
		if (i_78_ > 32767) {
			i_78_ -= 65536;
		}
		return i_78_;
	}

	public int method13123(int i) {
		index += 2064013562;
		int i_79_ = ((buffer[-1990677291 * index - 2] - 128 & 0xff) + ((buffer[index * -1990677291 - 1] & 0xff) << 8));
		if (i_79_ > 32767) {
			i_79_ -= 65536;
		}
		return i_79_;
	}

	public void method13124(CharSequence charsequence) {
		int i = charsequence.length();
		int i_80_ = 0;
		for (int i_81_ = 0; i_81_ < i; i_81_++) {
			char c = charsequence.charAt(i_81_);
			if (c <= '\u007f') {
				i_80_++;
			} else if (c <= '\u07ff') {
				i_80_ += 2;
			} else {
				i_80_ += 3;
			}
		}
		int i_82_ = i_80_;
		i = i_82_;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
		method13077(i, 2096504269);
		index += Class330.method5914(buffer, -1990677291 * index, charsequence, 297537866) * -1115476867;
	}

	public void writeIntLE(int i, byte i_83_) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
	}

	public static int method13126(byte[] is, int i) {
		return Class455.method7559(is, 0, i, -1533731611);
	}

	public void readIntV2(int i, byte i_84_) { //renamed by me
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public int readUnsignedIntLE(int i) {
		index += -166940172;
		return ((buffer[index * -1990677291 - 4] & 0xff) + (((buffer[-1990677291 * index - 2] & 0xff) << 16) + ((buffer[index * -1990677291 - 1] & 0xff) << 24) + ((buffer[-1990677291 * index - 3] & 0xff) << 8)));
	}

	public int readUnsignedIntV1() {
		index += -166940172;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 16) + ((buffer[-1990677291 * index - 2] & 0xff) << 24) + ((buffer[-1990677291 * index - 4] & 0xff) << 8) + (buffer[index * -1990677291 - 3] & 0xff));
	}

	public int readUnsignedIntV2(int i) {
		index += -166940172;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 8) + (((buffer[index * -1990677291 - 4] & 0xff) << 16) + ((buffer[index * -1990677291 - 3] & 0xff) << 24)) + (buffer[index * -1990677291 - 2] & 0xff));
	}

	public void method13131(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (128 + i);
	}

	public int method13132(int i) {
		index += 948536695;
		return ((buffer[-1990677291 * index - 2] & 0xff) + (((buffer[index * -1990677291 - 1] & 0xff) << 8) + ((buffer[-1990677291 * index - 3] & 0xff) << 16)));
	}

	public void readBytes(byte[] is, int i, int i_85_, int i_86_) {
		for (int i_87_ = i; i_87_ < i + i_85_; i_87_++) {
			is[i_87_] = buffer[(index += -1115476867) * -1990677291 - 1];
		}
	}

	public void method13134(String string) {
		int i = string.indexOf('\0');
		if (i >= 0) {
			throw new IllegalArgumentException("");
		}
		index += (Class317.method5693(string, 0, string.length(), buffer, -1990677291 * index, -74015201) * -1115476867);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
	}

	public long method13135(int i) {
		if (--i < 0 || i > 7) {
			throw new IllegalArgumentException();
		}
		int i_88_ = i * 8;
		long l = 0L;
		for (/**/; i_88_ >= 0; i_88_ -= 8) {
			l |= ((buffer[(index += -1115476867) * -1990677291 - 1]) & 0xffL) << i_88_;
		}
		return l;
	}

	static {
		for (int i = 0; i < 256; i++) {
			int i_89_ = i;
			for (int i_90_ = 0; i_90_ < 8; i_90_++) {
				if (1 == (i_89_ & 0x1)) {
					i_89_ = i_89_ >>> 1 ^ ~0x12477cdf;
				} else {
					i_89_ >>>= 1;
				}
			}
			anIntArray7986[i] = i_89_;
		}
		aLongArray7979 = new long[256];
		for (int i = 0; i < 256; i++) {
			long l = i;
			for (int i_91_ = 0; i_91_ < 8; i_91_++) {
				if (1L == (l & 0x1L)) {
					l = l >>> 1 ^ ~0x3693a86a2878f0bdL;
				} else {
					l >>>= 1;
				}
			}
			aLongArray7979[i] = l;
		}
	}

	public void method13136(int i) {
		buffer[-1990677291 * index - i - 1] = (byte) i;
	}

	public int readIntLE() {
		index += -166940172;
		return (((buffer[index * -1990677291 - 4] & 0xff) << 24) + ((buffer[index * -1990677291 - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
	}

	public void method13138(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public void method13139(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public void method13140(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public void method13141(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13142(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13143(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13144(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13145(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13146(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
	}

	public void method13147(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
	}

	public int method13148() {
		int i = buffer[-1990677291 * index] & 0xff;
		if (i < 128) {
			return readUnsignedByte() - 64;
		}
		return readUnsignedShort() - 49152;
	}

	public void writeString(String string) {
		int i_92_ = string.indexOf('\0');
		if (i_92_ >= 0) {
			throw new IllegalArgumentException("");
		}
		index += (Class317.method5693(string, 0, string.length(), buffer, -1990677291 * index, -74015201) * -1115476867);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
	}

	public static int method13150(String string) {
		return string.length() + 1;
	}

	public static int method13151(String string) {
		return string.length() + 1;
	}

	public static int method13152(String string) {
		return string.length() + 1;
	}

	public void method13153(BigInteger biginteger, BigInteger biginteger_93_) {
		int i = index * -1990677291;
		index = 0;
		byte[] is = new byte[i];
		readBytes(is, 0, i, 292151665);
		BigInteger biginteger_94_ = new BigInteger(is);
		BigInteger biginteger_95_ = biginteger_94_.modPow(biginteger, biginteger_93_);
		byte[] is_96_ = biginteger_95_.toByteArray();
		index = 0;
		writeShort(is_96_.length, 1417031095);
		writeBytes(is_96_, 0, is_96_.length);
	}

	public static int method13154(String string) {
		return string.length() + 1;
	}

	public void method13155(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public boolean method13156() {
		index -= -166940172;
		int i = Class455.method7559(buffer, 0, -1990677291 * index, -641181321);
		int i_97_ = readIntLE();
		if (i == i_97_) {
			return true;
		}
		return false;
	}

	public static int method13157(String string) {
		return string.length() + 2;
	}

	public void method13158(int i) {
		buffer[-1990677291 * index - i - 4] = (byte) (i >> 24);
		buffer[index * -1990677291 - i - 3] = (byte) (i >> 16);
		buffer[-1990677291 * index - i - 2] = (byte) (i >> 8);
		buffer[index * -1990677291 - i - 1] = (byte) i;
	}

	public void readIntV1(int i, int i_98_) {  //edited by me
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
	}

	public int method13160() {
		index += -166940172;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 8) + (((buffer[index * -1990677291 - 4] & 0xff) << 16) + ((buffer[index * -1990677291 - 3] & 0xff) << 24)) + (buffer[index * -1990677291 - 2] & 0xff));
	}

	public int readBigSmart(int i) {
		if (buffer[-1990677291 * index] < 0) {
			return readIntLE() & 0x7fffffff;
		}
		int i_99_ = readUnsignedShort();
		if (i_99_ == 32767) {
			return -1;
		}
		return i_99_;
	}

	public long method13162(int i) {
		if (--i < 0 || i > 7) {
			throw new IllegalArgumentException();
		}
		int i_100_ = i * 8;
		long l = 0L;
		for (/**/; i_100_ >= 0; i_100_ -= 8) {
			l |= ((buffer[(index += -1115476867) * -1990677291 - 1]) & 0xffL) << i_100_;
		}
		return l;
	}

	public void method13163(CharSequence charsequence) {
		int i = charsequence.length();
		int i_101_ = 0;
		for (int i_102_ = 0; i_102_ < i; i_102_++) {
			char c = charsequence.charAt(i_102_);
			if (c <= '\u007f') {
				i_101_++;
			} else if (c <= '\u07ff') {
				i_101_ += 2;
			} else {
				i_101_ += 3;
			}
		}
		int i_103_ = i_101_;
		i = i_103_;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
		method13077(i, 2103974236);
		index += Class330.method5914(buffer, -1990677291 * index, charsequence, -320080138) * -1115476867;
	}

	public void method13164(CharSequence charsequence) {
		int i = charsequence.length();
		int i_104_ = 0;
		for (int i_105_ = 0; i_105_ < i; i_105_++) {
			char c = charsequence.charAt(i_105_);
			if (c <= '\u007f') {
				i_104_++;
			} else if (c <= '\u07ff') {
				i_104_ += 2;
			} else {
				i_104_ += 3;
			}
		}
		int i_106_ = i_104_;
		i = i_106_;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
		method13077(i, 1870375770);
		index += Class330.method5914(buffer, -1990677291 * index, charsequence, 2031646276) * -1115476867;
	}

	public void method13165(byte[] is, int i, int i_107_) {
		for (int i_108_ = i; i_108_ < i_107_ + i; i_108_++) {
			buffer[(index += -1115476867) * -1990677291 - 1] = is[i_108_];
		}
	}

	public void method13166(byte[] is, int i, int i_109_) {
		for (int i_110_ = i; i_110_ < i_109_ + i; i_110_++) {
			buffer[(index += -1115476867) * -1990677291 - 1] = is[i_110_];
		}
	}

	public void method13167(int i) {
		buffer[-1990677291 * index - i - 4] = (byte) (i >> 24);
		buffer[index * -1990677291 - i - 3] = (byte) (i >> 16);
		buffer[-1990677291 * index - i - 2] = (byte) (i >> 8);
		buffer[index * -1990677291 - i - 1] = (byte) i;
	}

	public int method13168() {
		index += -166940172;
		return ((buffer[index * -1990677291 - 4] & 0xff) + (((buffer[-1990677291 * index - 2] & 0xff) << 16) + ((buffer[index * -1990677291 - 1] & 0xff) << 24) + ((buffer[-1990677291 * index - 3] & 0xff) << 8)));
	}

	public void method13169(int i) {
		if (i >= 0 && i < 128) {
			writeByte(i);
		} else if (i >= 0 && i < 32768) {
			writeShort(32768 + i, 1417031095);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void method13170(int i) {
		buffer[-1990677291 * index - i - 2] = (byte) (i >> 8);
		buffer[-1990677291 * index - i - 1] = (byte) i;
	}

	public void method13171(int i) {
		buffer[-1990677291 * index - i - 1] = (byte) i;
	}

	public void method13172(int i) {
		buffer[-1990677291 * index - i - 1] = (byte) i;
	}

	public void method13173(int i) {
		buffer[-1990677291 * index - i - 1] = (byte) i;
	}

	public void method13174(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13175(int i) {
		if (i >= 0 && i < 128) {
			writeByte(i);
		} else if (i >= 0 && i < 32768) {
			writeShort(32768 + i, 1417031095);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public int method13176() {
		index += -166940172;
		return (((buffer[index * -1990677291 - 4] & 0xff) << 24) + ((buffer[index * -1990677291 - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
	}

	public void method13177(int i) {
		if (i >= 0 && i < 128) {
			writeByte(i);
		} else if (i >= 0 && i < 32768) {
			writeShort(32768 + i, 1417031095);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public int readUnsignedSmart(int i) {
		int i_111_ = buffer[index * -1990677291] & 0xff;
		if (i_111_ < 128) {
			return readUnsignedByte();
		}
		return readUnsignedShort() - 32768;
	}

	public void method13179(int i) {
		if (0 != (i & ~0x7f)) {
			if ((i & ~0x3fff) != 0) {
				if (0 != (i & ~0x1fffff)) {
					if (0 != (i & ~0xfffffff)) {
						writeByte(i >>> 28 | 0x80);
					}
					writeByte(i >>> 21 | 0x80);
				}
				writeByte(i >>> 14 | 0x80);
			}
			writeByte(i >>> 7 | 0x80);
		}
		writeByte(i & 0x7f);
	}

	public void method13180(int i) {
		if (0 != (i & ~0x7f)) {
			if ((i & ~0x3fff) != 0) {
				if (0 != (i & ~0x1fffff)) {
					if (0 != (i & ~0xfffffff)) {
						writeByte(i >>> 28 | 0x80);
					}
					writeByte(i >>> 21 | 0x80);
				}
				writeByte(i >>> 14 | 0x80);
			}
			writeByte(i >>> 7 | 0x80);
		}
		writeByte(i & 0x7f);
	}

	public int method13181() {
		return (buffer[(index += -1115476867) * -1990677291 - 1] & 0xff);
	}

	public int method13182() {
		return (buffer[(index += -1115476867) * -1990677291 - 1] & 0xff);
	}

	public int method13183() {
		index += 948536695;
		int i = (((buffer[-1990677291 * index - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
		if (i > 8388607) {
			i -= 16777216;
		}
		return i;
	}

	public long method13184() {
		long l = readUnsignedByte() & 0xffffffffL;
		long l_112_ = readIntLE() & 0xffffffffL;
		return (l << 32) + l_112_;
	}

	public byte method13185() {
		return buffer[(index += -1115476867) * -1990677291 - 1];
	}

	public int method13186() {
		index += 2064013562;
		return (((buffer[-1990677291 * index - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
	}

	public int method13187() {
		index += 2064013562;
		int i = ((buffer[index * -1990677291 - 1] & 0xff) + ((buffer[index * -1990677291 - 2] & 0xff) << 8));
		if (i > 32767) {
			i -= 65536;
		}
		return i;
	}

	public int method13188() {
		index += 948536695;
		return (((buffer[index * -1990677291 - 2] & 0xff) << 8) + ((buffer[index * -1990677291 - 3] & 0xff) << 16) + (buffer[-1990677291 * index - 1] & 0xff));
	}

	public long method13189(int i) {
		long l = readUnsignedByte() & 0xffffffffL;
		long l_113_ = readIntLE() & 0xffffffffL;
		return (l << 32) + l_113_;
	}

	public int method13190() {
		index += 948536695;
		return ((buffer[-1990677291 * index - 2] & 0xff) + (((buffer[index * -1990677291 - 1] & 0xff) << 8) + ((buffer[-1990677291 * index - 3] & 0xff) << 16)));
	}

	public int method13191() {
		index += 948536695;
		int i = (((buffer[-1990677291 * index - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
		if (i > 8388607) {
			i -= 16777216;
		}
		return i;
	}

	public int method13192() {
		index += 948536695;
		int i = (((buffer[-1990677291 * index - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
		if (i > 8388607) {
			i -= 16777216;
		}
		return i;
	}

	public int method13193() {
		index += 948536695;
		int i = (((buffer[-1990677291 * index - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
		if (i > 8388607) {
			i -= 16777216;
		}
		return i;
	}

	public int method13194() {
		index += -166940172;
		return (((buffer[index * -1990677291 - 4] & 0xff) << 24) + ((buffer[index * -1990677291 - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
	}

	public void method13195(int i, byte i_114_) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13196(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (0 - i);
	}

	public int method13197() {
		index += -166940172;
		return ((buffer[index * -1990677291 - 4] & 0xff) + (((buffer[index * -1990677291 - 3] & 0xff) << 8) + (((buffer[-1990677291 * index - 2] & 0xff) << 16) + ((buffer[-1990677291 * index - 1] & 0xff) << 24))));
	}

	public int method13198() {
		index += 2064013562;
		int i = ((buffer[-1990677291 * index - 1] - 128 & 0xff) + ((buffer[index * -1990677291 - 2] & 0xff) << 8));
		if (i > 32767) {
			i -= 65536;
		}
		return i;
	}

	public int method13199() {
		return (0 - (buffer[(index += -1115476867) * -1990677291 - 1]) & 0xff);
	}

	public long method13200() {
		long l = readIntLE() & 0xffffffffL;
		long l_115_ = readIntLE() & 0xffffffffL;
		return l_115_ + (l << 32);
	}

	public void method13201(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public long method13202() {
		long l = method13085(229935645) & 0xffffffffL;
		long l_116_ = method13085(-1278727577) & 0xffffffffL;
		return l + (l_116_ << 32);
	}

	public long method13203(int i) {
		if (--i < 0 || i > 7) {
			throw new IllegalArgumentException();
		}
		int i_117_ = i * 8;
		long l = 0L;
		for (/**/; i_117_ >= 0; i_117_ -= 8) {
			l |= ((buffer[(index += -1115476867) * -1990677291 - 1]) & 0xffL) << i_117_;
		}
		return l;
	}

	public void method13204(long l) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 32);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) l;
	}

	public long method13205(int i) {
		if (--i < 0 || i > 7) {
			throw new IllegalArgumentException();
		}
		int i_118_ = i * 8;
		long l = 0L;
		for (/**/; i_118_ >= 0; i_118_ -= 8) {
			l |= ((buffer[(index += -1115476867) * -1990677291 - 1]) & 0xffL) << i_118_;
		}
		return l;
	}

	public String readString(int i) {
		int i_119_ = index * -1990677291;
		while (buffer[(index += -1115476867) * -1990677291 - 1] != 0) {
			/* empty */
		}
		int i_120_ = index * -1990677291 - i_119_ - 1;
		if (i_120_ == 0) {
			return "";
		}
		return Class344.method6118(buffer, i_119_, i_120_, (byte) -24);
	}

	public long method13207(int i) {
		if (--i < 0 || i > 7) {
			throw new IllegalArgumentException();
		}
		int i_121_ = i * 8;
		long l = 0L;
		for (/**/; i_121_ >= 0; i_121_ -= 8) {
			l |= ((buffer[(index += -1115476867) * -1990677291 - 1]) & 0xffL) << i_121_;
		}
		return l;
	}

	public String method13208() {
		if (buffer[-1990677291 * index] == 0) {
			index += -1115476867;
			return null;
		}
		return readString(-519586845);
	}

	public String method13209() {
		if (buffer[-1990677291 * index] == 0) {
			index += -1115476867;
			return null;
		}
		return readString(-200944106);
	}

	public String method13210() {
		if (buffer[-1990677291 * index] == 0) {
			index += -1115476867;
			return null;
		}
		return readString(97554165);
	}

	public String method13211() {
		int i = index * -1990677291;
		while (buffer[(index += -1115476867) * -1990677291 - 1] != 0) {
			/* empty */
		}
		int i_122_ = index * -1990677291 - i - 1;
		if (i_122_ == 0) {
			return "";
		}
		return Class344.method6118(buffer, i, i_122_, (byte) -15);
	}

	public String method13212() {
		int i = index * -1990677291;
		while (buffer[(index += -1115476867) * -1990677291 - 1] != 0) {
			/* empty */
		}
		int i_123_ = index * -1990677291 - i - 1;
		if (i_123_ == 0) {
			return "";
		}
		return Class344.method6118(buffer, i, i_123_, (byte) -114);
	}

	public String method13213() {
		int i = index * -1990677291;
		while (buffer[(index += -1115476867) * -1990677291 - 1] != 0) {
			/* empty */
		}
		int i_124_ = index * -1990677291 - i - 1;
		if (i_124_ == 0) {
			return "";
		}
		return Class344.method6118(buffer, i, i_124_, (byte) -94);
	}

	public int readUnsignedByte128(int i) {
		return ((buffer[(index += -1115476867) * -1990677291 - 1] - 128) & 0xff);
	}

	public String method13215() {
		byte i = buffer[(index += -1115476867) * -1990677291 - 1];
		if (0 != i) {
			throw new IllegalStateException("");
		}
		int i_125_ = -1990677291 * index;
		while (buffer[(index += -1115476867) * -1990677291 - 1] != 0) {
			/* empty */
		}
		int i_126_ = index * -1990677291 - i_125_ - 1;
		if (i_126_ == 0) {
			return "";
		}
		return Class344.method6118(buffer, i_125_, i_126_, (byte) -117);
	}

	public String method13216() {
		byte i = buffer[(index += -1115476867) * -1990677291 - 1];
		if (i != 0) {
			throw new IllegalStateException("");
		}
		int i_127_ = method13099(1243519826);
		if (-1990677291 * index + i_127_ > buffer.length) {
			throw new IllegalStateException("");
		}
		String string = Class271.method4824(buffer, -1990677291 * index, i_127_, 336004634);
		index += i_127_ * -1115476867;
		return string;
	}

	public String method13217() {
		byte i = buffer[(index += -1115476867) * -1990677291 - 1];
		if (i != 0) {
			throw new IllegalStateException("");
		}
		int i_128_ = method13099(409981223);
		if (-1990677291 * index + i_128_ > buffer.length) {
			throw new IllegalStateException("");
		}
		String string = Class271.method4824(buffer, -1990677291 * index, i_128_, 336004634);
		index += i_128_ * -1115476867;
		return string;
	}

	public void method13218(byte[] is, int i, int i_129_) {
		for (int i_130_ = i; i_130_ < i + i_129_; i_130_++) {
			is[i_130_] = buffer[(index += -1115476867) * -1990677291 - 1];
		}
	}

	public static int method13219(String string) {
		return string.length() + 2;
	}

	public void method13220(byte[] is, int i, int i_131_) {
		for (int i_132_ = i; i_132_ < i + i_131_; i_132_++) {
			is[i_132_] = buffer[(index += -1115476867) * -1990677291 - 1];
		}
	}

	public void method13221(byte[] is, int i, int i_133_) {
		for (int i_134_ = i; i_134_ < i + i_133_; i_134_++) {
			is[i_134_] = buffer[(index += -1115476867) * -1990677291 - 1];
		}
	}

	public void method13222(byte[] is, int i, int i_135_) {
		for (int i_136_ = i; i_136_ < i + i_135_; i_136_++) {
			is[i_136_] = buffer[(index += -1115476867) * -1990677291 - 1];
		}
	}

	public void method13223(byte[] is, int i, int i_137_) {
		for (int i_138_ = i; i_138_ < i + i_137_; i_138_++) {
			is[i_138_] = buffer[(index += -1115476867) * -1990677291 - 1];
		}
	}

	public void method13224(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public int method13225() {
		int i = buffer[-1990677291 * index] & 0xff;
		if (i < 128) {
			return readUnsignedByte() - 64;
		}
		return readUnsignedShort() - 49152;
	}

	public byte method13226() {
		return (byte) (0 - (buffer[(index += -1115476867) * -1990677291 - 1]));
	}

	public int method13227() {
		int i = buffer[-1990677291 * index] & 0xff;
		if (i < 128) {
			return readUnsignedByte() - 64;
		}
		return readUnsignedShort() - 49152;
	}

	public int method13228() {
		int i = buffer[index * -1990677291] & 0xff;
		if (i < 128) {
			return readUnsignedByte();
		}
		return readUnsignedShort() - 32768;
	}

	public void method13229(long l, int i) {
		if (--i < 0 || i > 7) {
			throw new IllegalArgumentException();
		}
		for (int i_139_ = i * 8; i_139_ >= 0; i_139_ -= 8) {
			buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (int) (l >> i_139_);
		}
	}

	public int method13230() {
		return (128 - (buffer[(index += -1115476867) * -1990677291 - 1]) & 0xff);
	}

	public int method13231() {
		if (buffer[index * -1990677291] < 0) {
			return readIntLE() & 0x7fffffff;
		}
		return readUnsignedShort();
	}

	public void method13232(int i, byte i_140_) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
	}

	public int method13233() {
		if (buffer[-1990677291 * index] < 0) {
			return readIntLE() & 0x7fffffff;
		}
		int i = readUnsignedShort();
		if (i == 32767) {
			return -1;
		}
		return i;
	}

	public int method13234() {
		if (buffer[-1990677291 * index] < 0) {
			return readIntLE() & 0x7fffffff;
		}
		int i = readUnsignedShort();
		if (i == 32767) {
			return -1;
		}
		return i;
	}

	public int method13235() {
		int i = buffer[(index += -1115476867) * -1990677291 - 1];
		int i_141_ = 0;
		for (/**/; i < 0; i = buffer[(index += -1115476867) * -1990677291 - 1]) {
			i_141_ = (i_141_ | i & 0x7f) << 7;
		}
		return i_141_ | i;
	}

	public byte method13236(short i) {
		return buffer[(index += -1115476867) * -1990677291 - 1];
	}

	public void method13237(int[] is) {
		int i = index * -1990677291 / 8;
		index = 0;
		for (int i_142_ = 0; i_142_ < i; i_142_++) {
			int i_143_ = readIntLE();
			int i_144_ = readIntLE();
			int i_145_ = -957401312;
			int i_146_ = -1640531527;
			int i_147_ = 32;
			while (i_147_-- > 0) {
				i_144_ -= ((i_143_ << 4 ^ i_143_ >>> 5) + i_143_ ^ is[i_145_ >>> 11 & 0x3] + i_145_);
				i_145_ -= i_146_;
				i_143_ -= (i_144_ + (i_144_ << 4 ^ i_144_ >>> 5) ^ is[i_145_ & 0x3] + i_145_);
			}
			index -= -333880344;
			writeInt(i_143_);
			writeInt(i_144_);
		}
	}

	public void method13238(int[] is) {
		int i = index * -1990677291 / 8;
		index = 0;
		for (int i_148_ = 0; i_148_ < i; i_148_++) {
			int i_149_ = readIntLE();
			int i_150_ = readIntLE();
			int i_151_ = -957401312;
			int i_152_ = -1640531527;
			int i_153_ = 32;
			while (i_153_-- > 0) {
				i_150_ -= ((i_149_ << 4 ^ i_149_ >>> 5) + i_149_ ^ is[i_151_ >>> 11 & 0x3] + i_151_);
				i_151_ -= i_152_;
				i_149_ -= (i_150_ + (i_150_ << 4 ^ i_150_ >>> 5) ^ is[i_151_ & 0x3] + i_151_);
			}
			index -= -333880344;
			writeInt(i_149_);
			writeInt(i_150_);
		}
	}

	public void method13239(int[] is, int i, int i_154_) {
		int i_155_ = index * -1990677291;
		index = -1115476867 * i;
		int i_156_ = (i_154_ - i) / 8;
		for (int i_157_ = 0; i_157_ < i_156_; i_157_++) {
			int i_158_ = readIntLE();
			int i_159_ = readIntLE();
			int i_160_ = 0;
			int i_161_ = -1640531527;
			int i_162_ = 32;
			while (i_162_-- > 0) {
				i_158_ += (i_159_ + (i_159_ << 4 ^ i_159_ >>> 5) ^ is[i_160_ & 0x3] + i_160_);
				i_160_ += i_161_;
				i_159_ += (i_158_ + (i_158_ << 4 ^ i_158_ >>> 5) ^ i_160_ + is[i_160_ >>> 11 & 0x3]);
			}
			index -= -333880344;
			writeInt(i_158_);
			writeInt(i_159_);
		}
		index = -1115476867 * i_155_;
	}

	public void method13240(int[] is, int i, int i_163_) {
		int i_164_ = -1990677291 * index;
		index = -1115476867 * i;
		int i_165_ = (i_163_ - i) / 8;
		for (int i_166_ = 0; i_166_ < i_165_; i_166_++) {
			int i_167_ = readIntLE();
			int i_168_ = readIntLE();
			int i_169_ = -957401312;
			int i_170_ = -1640531527;
			int i_171_ = 32;
			while (i_171_-- > 0) {
				i_168_ -= ((i_167_ << 4 ^ i_167_ >>> 5) + i_167_ ^ is[i_169_ >>> 11 & 0x3] + i_169_);
				i_169_ -= i_170_;
				i_167_ -= (i_168_ + (i_168_ << 4 ^ i_168_ >>> 5) ^ i_169_ + is[i_169_ & 0x3]);
			}
			index -= -333880344;
			writeInt(i_167_);
			writeInt(i_168_);
		}
		index = -1115476867 * i_164_;
	}

	public int method13241() {
		int i = 0;
		int i_172_;
		for (i_172_ = readUnsignedSmart(1727907944); i_172_ == 32767; i_172_ = readUnsignedSmart(2137544299)) {
			i += 32767;
		}
		i += i_172_;
		return i;
	}

	public int method13242(int i) {
		int i_173_ = Class455.method7559(buffer, i, -1990677291 * index, -1332729621);
		writeInt(i_173_);
		return i_173_;
	}

	public void method13243(String string) {
		int i = string.indexOf('\0');
		if (i >= 0) {
			throw new IllegalArgumentException("");
		}
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
		index += (Class317.method5693(string, 0, string.length(), buffer, -1990677291 * index, -74015201) * -1115476867);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) 0;
	}

	public int method13244(int i) {
		int i_174_ = Class455.method7559(buffer, i, -1990677291 * index, -1466586232);
		writeInt(i_174_);
		return i_174_;
	}

	public int method13245() {
		if (buffer[-1990677291 * index] < 0) {
			return readIntLE() & 0x7fffffff;
		}
		int i = readUnsignedShort();
		if (i == 32767) {
			return -1;
		}
		return i;
	}

	public int method13246() {
		index += 2064013562;
		return ((buffer[index * -1990677291 - 2] & 0xff) + ((buffer[-1990677291 * index - 1] & 0xff) << 8));
	}

	public boolean method13247() {
		index -= -166940172;
		int i = Class455.method7559(buffer, 0, -1990677291 * index, -1644915365);
		int i_175_ = readIntLE();
		if (i == i_175_) {
			return true;
		}
		return false;
	}

	public String method13248(int i) {
		byte i_176_ = buffer[(index += -1115476867) * -1990677291 - 1];
		if (i_176_ != 0) {
			throw new IllegalStateException("");
		}
		int i_177_ = method13099(-154954210);
		if (-1990677291 * index + i_177_ > buffer.length) {
			throw new IllegalStateException("");
		}
		String string = Class271.method4824(buffer, -1990677291 * index, i_177_, 336004634);
		index += i_177_ * -1115476867;
		return string;
	}

	public void method13249(int[] is, int i, int i_178_, int i_179_) {
		int i_180_ = -1990677291 * index;
		index = -1115476867 * i;
		int i_181_ = (i_178_ - i) / 8;
		for (int i_182_ = 0; i_182_ < i_181_; i_182_++) {
			int i_183_ = readIntLE();
			int i_184_ = readIntLE();
			int i_185_ = -957401312;
			int i_186_ = -1640531527;
			int i_187_ = 32;
			while (i_187_-- > 0) {
				i_184_ -= ((i_183_ << 4 ^ i_183_ >>> 5) + i_183_ ^ is[i_185_ >>> 11 & 0x3] + i_185_);
				i_185_ -= i_186_;
				i_183_ -= (i_184_ + (i_184_ << 4 ^ i_184_ >>> 5) ^ i_185_ + is[i_185_ & 0x3]);
			}
			index -= -333880344;
			writeInt(i_183_);
			writeInt(i_184_);
		}
		index = -1115476867 * i_180_;
	}

	public long method13250() {
		long l = readUnsignedByte() & 0xffffffffL;
		long l_188_ = readIntLE() & 0xffffffffL;
		return (l << 32) + l_188_;
	}

	public void method13251(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (0 - i);
	}

	public int method13252() {
		return ((buffer[(index += -1115476867) * -1990677291 - 1] - 128) & 0xff);
	}

	public static int method13253(String string) {
		return string.length() + 2;
	}

	public long method13254() {
		long l = method13085(1684405813) & 0xffffffffL;
		long l_189_ = method13085(1962993091) & 0xffffffffL;
		return l + (l_189_ << 32);
	}

	public int method13255() {
		return (0 - (buffer[(index += -1115476867) * -1990677291 - 1]) & 0xff);
	}

	public byte method13256() {
		return (byte) ((buffer[(index += -1115476867) * -1990677291 - 1]) - 128);
	}

	public int method13257() {
		int i = buffer[-1990677291 * index] & 0xff;
		if (i < 128) {
			return readUnsignedByte() - 64;
		}
		return readUnsignedShort() - 49152;
	}

	public byte method13258() {
		return (byte) ((buffer[(index += -1115476867) * -1990677291 - 1]) - 128);
	}

	public void method13259(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public byte readByteC(int i) {
		return (byte) (0 - (buffer[(index += -1115476867) * -1990677291 - 1]));
	}

	public int readUnsignedByteC(int i) {
		return (0 - (buffer[(index += -1115476867) * -1990677291 - 1]) & 0xff);
	}

	public byte method13262() {
		return (byte) (128 - (buffer[(index += -1115476867) * -1990677291 - 1]));
	}

	public byte method13263() {
		return (byte) (128 - (buffer[(index += -1115476867) * -1990677291 - 1]));
	}

	public void method13264(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public void method13265(int i) {
		buffer[-1990677291 * index - i - 4] = (byte) (i >> 24);
		buffer[index * -1990677291 - i - 3] = (byte) (i >> 16);
		buffer[-1990677291 * index - i - 2] = (byte) (i >> 8);
		buffer[index * -1990677291 - i - 1] = (byte) i;
	}

	public void method13266(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i + 128);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public int method13267() {
		index += 948536695;
		int i = (((buffer[-1990677291 * index - 3] & 0xff) << 16) + ((buffer[index * -1990677291 - 2] & 0xff) << 8) + (buffer[-1990677291 * index - 1] & 0xff));
		if (i > 8388607) {
			i -= 16777216;
		}
		return i;
	}

	public byte method13268() {
		return (byte) (0 - (buffer[(index += -1115476867) * -1990677291 - 1]));
	}

	public int method13269() {
		index += 2064013562;
		return ((buffer[index * -1990677291 - 2] & 0xff) + ((buffer[-1990677291 * index - 1] & 0xff) << 8));
	}

	public int method13270() {
		index += 2064013562;
		return ((buffer[index * -1990677291 - 2] & 0xff) + ((buffer[-1990677291 * index - 1] & 0xff) << 8));
	}

	public int method13271() {
		index += 2064013562;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 8) + (buffer[index * -1990677291 - 2] - 128 & 0xff));
	}

	public int method13272() {
		index += 2064013562;
		int i = ((buffer[-1990677291 * index - 2] & 0xff) + ((buffer[-1990677291 * index - 1] & 0xff) << 8));
		if (i > 32767) {
			i -= 65536;
		}
		return i;
	}

	public int method13273() {
		index += 2064013562;
		int i = ((buffer[-1990677291 * index - 2] & 0xff) + ((buffer[-1990677291 * index - 1] & 0xff) << 8));
		if (i > 32767) {
			i -= 65536;
		}
		return i;
	}

	public int method13274() {
		index += 2064013562;
		int i = ((buffer[-1990677291 * index - 1] - 128 & 0xff) + ((buffer[index * -1990677291 - 2] & 0xff) << 8));
		if (i > 32767) {
			i -= 65536;
		}
		return i;
	}

	public int method13275() {
		return (buffer[(index += -1115476867) * -1990677291 - 1] & 0xff);
	}

	public int method13276() {
		index += 2064013562;
		int i = ((buffer[-1990677291 * index - 1] - 128 & 0xff) + ((buffer[index * -1990677291 - 2] & 0xff) << 8));
		if (i > 32767) {
			i -= 65536;
		}
		return i;
	}

	public int method13277() {
		index += 948536695;
		return ((buffer[-1990677291 * index - 2] & 0xff) + (((buffer[index * -1990677291 - 1] & 0xff) << 8) + ((buffer[-1990677291 * index - 3] & 0xff) << 16)));
	}

	public long method13278(byte i) {
		long l = method13085(-1967517434) & 0xffffffffL;
		long l_190_ = method13085(341921667) & 0xffffffffL;
		return l + (l_190_ << 32);
	}

	public int method13279() {
		index += 948536695;
		return ((buffer[-1990677291 * index - 2] & 0xff) + (((buffer[index * -1990677291 - 1] & 0xff) << 8) + ((buffer[-1990677291 * index - 3] & 0xff) << 16)));
	}

	public int method13280(int i) {
		int i_191_ = Class455.method7559(buffer, i, -1990677291 * index, -1015416003);
		writeInt(i_191_);
		return i_191_;
	}

	public void method13281(int i, int i_192_) { //readShort or readUnsignedShort? i think
		buffer[-1990677291 * index - i - 2] = (byte) (i >> 8);
		buffer[-1990677291 * index - i - 1] = (byte) i;
	}

	public void method13282(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
	}

	public void method13283(int i) {
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 16);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 24);
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) i;
		buffer[(index += -1115476867) * -1990677291 - 1] = (byte) (i >> 8);
	}

	public static int method13284(String string) {
		return string.length() + 1;
	}

	public int method13285() {
		index += -166940172;
		return ((buffer[index * -1990677291 - 4] & 0xff) + (((buffer[-1990677291 * index - 2] & 0xff) << 16) + ((buffer[index * -1990677291 - 1] & 0xff) << 24) + ((buffer[-1990677291 * index - 3] & 0xff) << 8)));
	}

	public int method13286() {
		return (128 - (buffer[(index += -1115476867) * -1990677291 - 1]) & 0xff);
	}

	public int method13287() {
		index += -166940172;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 16) + ((buffer[-1990677291 * index - 2] & 0xff) << 24) + ((buffer[-1990677291 * index - 4] & 0xff) << 8) + (buffer[index * -1990677291 - 3] & 0xff));
	}

	public int method13288() {
		index += -166940172;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 16) + ((buffer[-1990677291 * index - 2] & 0xff) << 24) + ((buffer[-1990677291 * index - 4] & 0xff) << 8) + (buffer[index * -1990677291 - 3] & 0xff));
	}

	public int method13289() {
		index += -166940172;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 16) + ((buffer[-1990677291 * index - 2] & 0xff) << 24) + ((buffer[-1990677291 * index - 4] & 0xff) << 8) + (buffer[index * -1990677291 - 3] & 0xff));
	}

	public int method13290() {
		index += -166940172;
		return (((buffer[-1990677291 * index - 1] & 0xff) << 8) + (((buffer[index * -1990677291 - 4] & 0xff) << 16) + ((buffer[index * -1990677291 - 3] & 0xff) << 24)) + (buffer[index * -1990677291 - 2] & 0xff));
	}

	public int method13291() {
		int i = 0;
		int i_193_;
		for (i_193_ = readUnsignedSmart(1535300669); i_193_ == 32767; i_193_ = readUnsignedSmart(1770710864)) {
			i += 32767;
		}
		i += i_193_;
		return i;
	}

	static final void method13292(Class118 class118, Class98 class98, Class527 class527, byte i) {
		class527.anInt7012 -= 283782002;
		class118.anInt1441 = 304814545 * (class527.anIntArray6999[class527.anInt7012 * 1942118537]);
		class118.anInt1263 = ((class527.anIntArray6999[1942118537 * class527.anInt7012 + 1]) * -1208146817);
		Class109.method1858(class118, (byte) -27);
	}
}
