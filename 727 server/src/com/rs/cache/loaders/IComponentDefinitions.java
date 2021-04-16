package com.rs.cache.loaders;

import java.util.Hashtable;

import com.rs.cache.Cache;
import com.rs.cache.io.InputStream;
import com.rs.utils.Utils;

@SuppressWarnings("unused")
public class IComponentDefinitions {

	private static IComponentDefinitions[][] icomponentsdefs = new IComponentDefinitions[Utils
			.getInterfaceDefinitionsSize()][];
	private static IComponentSettings GLOBAL_SETTINGS = new IComponentSettings(0, -1);

	protected int anInt4679;
	protected Object[] anObjectArray4680;
	protected int anInt4681;
	protected int anInt4682;
	protected int anInt4683;
	protected String[] aStringArray4686;
	protected int anInt4687;
	protected Object[] anObjectArray4688;
	protected boolean hidden;
	private short[] aShortArray4690;
	protected int anInt4691;
	protected int parentId;
	protected int anInt4693;
	protected int anInt4694 = -1;
	protected int anInt4695;
	protected int anInt4697;
	protected int anInt4698;
	protected int anInt4700;
	protected Object[] anObjectArray4701;
	protected int anInt4703;
	protected int[] anIntArray4705;
	protected Object[] anObjectArray4706;
	protected boolean aBoolean4707;
	protected int anInt4708;
	protected int anInt4709 = 0;
	protected boolean aBoolean4710;
	protected Object[] anObjectArray4711;
	protected Object[] anObjectArray4712;
	protected int anInt4714;
	private short[] aShortArray4717;
	protected int anInt4718;
	protected int anInt4719;
	protected byte aByte4720;
	protected boolean aBoolean4721;
	protected int anInt4722;
	protected boolean aBoolean4723;
	protected int anInt4724;
	// protected EntityNode_Sub4 anEntityNode_Sub4_4726;
	protected boolean aBoolean4727;
	protected int anInt4728;
	protected boolean aBoolean4730;
	protected boolean aBoolean4732;
	protected byte[] aByteArray4733;
	protected int anInt4734;
	protected int anInt4735;
	protected boolean aBoolean4738;
	protected Object[] anObjectArray4740;
	protected byte aByte4741;
	protected Object[] anObjectArray4742;
	protected boolean aBoolean4743;
	protected int anInt4744;
	protected Object[] anObjectArray4745;
	protected int anInt4746;
	protected int anInt4747;
	protected int anInt4748;
	protected byte aByte4750;
	protected Object[] anObjectArray4751;
	protected int anInt4752;
	protected Object[] anObjectArray4753;
	protected int anInt4754;
	// protected Animator anAnimator4755;
	protected Object[] anObjectArray4756;
	protected int anInt4757;
	public Object[] anObjectArray4758;
	protected int anInt4759;
	protected int anInt4760;
	protected int anInt4761;
	protected int anInt4762;
	protected int anInt4764;
	public String aString4765;
	protected int anInt4767;
	protected Object[] anObjectArray4768;
	protected boolean aBoolean4769;
	protected Object[] anObjectArray4770;
	protected Object[] anObjectArray4771;
	protected int[] anIntArray4772;
	protected int anInt4773;
	protected Object[] anObjectArray4774;
	protected Object[] anObjectArray4775;
	protected Object[] anObjectArray4777;
	protected Object[] anObjectArray4778;
	public String aString4779;
	protected int anInt4780;
	protected boolean aBoolean4782;
	protected int anInt4783;
	protected String aString4784;
	private short[] aShortArray4785;
	protected String aString4786;
	protected int anInt4787;
	protected Object[] anObjectArray4788;
	protected int[] anIntArray4789;
	public String aString4790;
	protected int anInt4792;
	protected IComponentDefinitions[] aWidgetArray4793;
	protected int anInt4794;
	protected int anInt4795;
	protected int anInt4796;
	protected int anInt4797;
	protected Object[] anObjectArray4798;
	protected Object[] anObjectArray4799;
	protected int anInt4800;
	protected int anInt4801;
	protected boolean aBoolean4802;
	protected Object[] anObjectArray4803;
	protected IComponentDefinitions[] aWidgetArray4804;
	protected int[] anIntArray4805;
	protected byte[] aByteArray4806;
	protected Object[] anObjectArray4807;
	protected boolean hasScripts;
	protected int anInt4809;
	protected int anInt4810;
	protected int anInt4811;
	protected int[] anIntArray4812;
	protected int anInt4813;
	protected int anInt4814;
	protected int anInt4815;
	protected int anInt4816;
	protected int anInt4817;
	protected Object[] anObjectArray4818;
	protected boolean aBoolean4819;
	protected int anInt4820;
	protected int anInt4821;
	protected int ihash;
	@SuppressWarnings("rawtypes")
	public Hashtable aHashTable4823;
	protected int anInt4824;
	protected int anInt4825;
	protected int anInt4826;
	protected Object[] anObjectArray4828;
	protected int[] anIntArray4829;
	protected int anInt4831;
	protected boolean aBoolean4832;
	protected int[] anIntArray4833;
	protected Object[] anObjectArray4834;
	protected int anInt4835;
	protected IComponentDefinitions aWidget4836;
	private short[] aShortArray4837;
	protected int[] anIntArray4838;
	protected int anInt4839;
	protected IComponentSettings aNode_Sub35_4840;
	public int type;
	protected int anInt4842;
	protected int modelType;
	protected Object[] anObjectArray4846;
	protected int anInt4848;
	protected int anInt4849;
	protected int anInt4850;
	protected byte aByte4851;
	protected Object[] anObjectArray4852;
	protected Object[] anObjectArray4854;
	protected Object[] anObjectArray4856;
	protected Object[] anObjectArray4857;
	protected boolean aBoolean4858;
	protected int anInt4860;
	protected boolean aBoolean4861;
	protected Object[] anObjectArray4862;
	protected int[] anIntArray4863;
	protected int anInt4864;
	protected boolean aBoolean4865;

	public static IComponentDefinitions getInterfaceComponent(int id, int component) {
		IComponentDefinitions[] inter = getInterface(id);
		if (inter == null || component >= inter.length)
			return null;
		return inter[component];
	}

	public static IComponentDefinitions[] getInterface(int id) {
		if (id >= icomponentsdefs.length)
			return null;
		if (icomponentsdefs[id] == null) {
			icomponentsdefs[id] = new IComponentDefinitions[Utils.getInterfaceDefinitionsComponentsSize(id)];
			for (int i = 0; i < icomponentsdefs[id].length; i++) {
				byte[] data = Cache.STORE.getIndexes()[3].getFile(id, i);
				if (data != null) {
					IComponentDefinitions defs = icomponentsdefs[id][i] = new IComponentDefinitions();
					defs.ihash = i + (id << 16);
					if (data[0] != -1) {
						throw new IllegalStateException("if1");
					}
					defs.decode(new InputStream(data));
				}
			}
		}
		return icomponentsdefs[id];
	}

	@SuppressWarnings("unchecked")
	final void decode(InputStream stream) {
		int newInt = stream.readUnsignedByte();
		if (newInt == 255) {
			newInt = -1;
		}
		type = stream.readUnsignedByte();
		if ((type & 0x80 ^ 0xffffffff) != -1) {
			type &= 0x7f;
			aString4765 = stream.readString();
		}
		anInt4814 = stream.readUnsignedShort();
		anInt4850 = stream.readShort();
		anInt4816 = stream.readShort();
		anInt4693 = stream.readUnsignedShort();
		anInt4722 = stream.readUnsignedShort();
		aByte4750 = (byte) stream.readByte();
		aByte4741 = (byte) stream.readByte();
		aByte4720 = (byte) stream.readByte();
		aByte4851 = (byte) stream.readByte();
		parentId = stream.readUnsignedShort();
		if (parentId != 65535) {
			parentId = parentId + (ihash & ~0xffff);
		} else {
			parentId = -1;
		}
		int i_17_ = stream.readUnsignedByte();
		hidden = (0x1 & i_17_ ^ 0xffffffff) != -1;
		if (newInt >= 0) {
			aBoolean4858 = (i_17_ & 0x2 ^ 0xffffffff) != -1;
		}
		if (type == 0) {
			anInt4735 = stream.readUnsignedShort();
			anInt4691 = stream.readUnsignedShort();
			if ((newInt ^ 0xffffffff) > -1) {
				aBoolean4858 = stream.readUnsignedByte() == 1;
			}
		}
		if (type == 5) {
			anInt4820 = stream.readInt();
			anInt4728 = stream.readUnsignedShort();
			int i_18_ = stream.readUnsignedByte();
			aBoolean4861 = (i_18_ & 0x1 ^ 0xffffffff) != -1;
			aBoolean4738 = (i_18_ & 0x2) != 0;
			anInt4757 = stream.readUnsignedByte();
			anInt4744 = stream.readUnsignedByte();
			anInt4796 = stream.readInt();
			aBoolean4732 = stream.readUnsignedByte() == 1;
			aBoolean4743 = stream.readUnsignedByte() == 1;
			anInt4754 = stream.readInt();
			if ((newInt ^ 0xffffffff) <= -4) {
				aBoolean4782 = (stream.readUnsignedByte() ^ 0xffffffff) == -2;
			}
		}
		if (type == 6) {
			modelType = 1;
			if ((ihash >> 16) > 1144)
				anInt4864 = stream.readBigSmart();
			else {
				anInt4864 = stream.readUnsignedShort();
				if ((anInt4864 ^ 0xffffffff) == -65536) {
					anInt4864 = -1;
				}
			}
			int i_19_ = stream.readUnsignedByte();
			aBoolean4707 = (0x4 & i_19_) == 4;
			boolean bool = (0x1 & i_19_) == 1;
			aBoolean4865 = (i_19_ & 0x2 ^ 0xffffffff) == -3;
			aBoolean4727 = (0x8 & i_19_ ^ 0xffffffff) == -9;
			if (bool) {
				anInt4709 = stream.readShort();
				anInt4797 = stream.readShort();
				anInt4815 = stream.readUnsignedShort();
				anInt4821 = stream.readUnsignedShort();
				anInt4682 = stream.readUnsignedShort();
				anInt4787 = stream.readUnsignedShort();
			} else if (aBoolean4865) {
				anInt4709 = stream.readShort();
				anInt4797 = stream.readShort();
				anInt4842 = stream.readShort();
				anInt4815 = stream.readUnsignedShort();
				anInt4821 = stream.readUnsignedShort();
				anInt4682 = stream.readUnsignedShort();
				anInt4787 = stream.readShort();
			}
			if ((ihash >> 16) > 1144)
				anInt4773 = stream.readBigSmart();
			else {
				anInt4773 = stream.readUnsignedShort();
				if ((anInt4773 ^ 0xffffffff) == -65536) {
					anInt4773 = -1;
				}
			}
			if (aByte4750 != 0) {
				anInt4800 = stream.readUnsignedShort();
			}
			if ((aByte4741 ^ 0xffffffff) != -1) {
				anInt4849 = stream.readUnsignedShort();
			}
		}
		if (type == 4) {
			if ((ihash >> 16) > 1144)
				anInt4759 = stream.readBigSmart();
			else {
				anInt4759 = stream.readUnsignedShort();
				if ((anInt4759 ^ 0xffffffff) == -65536) {
					anInt4759 = -1;
				}
			}
			if ((newInt ^ 0xffffffff) <= -3) {
				aBoolean4832 = (stream.readUnsignedByte() ^ 0xffffffff) == -2;
			}
			aString4790 = stream.readString();
			anInt4697 = stream.readUnsignedByte();
			anInt4835 = stream.readUnsignedByte();
			anInt4825 = stream.readUnsignedByte();
			aBoolean4710 = (stream.readUnsignedByte() ^ 0xffffffff) == -2;
			anInt4754 = stream.readInt();
			anInt4757 = stream.readUnsignedByte();
			if ((newInt ^ 0xffffffff) <= -1) {
				anInt4767 = stream.readUnsignedByte();
			}
		}
		if ((type ^ 0xffffffff) == -4) {
			anInt4754 = stream.readInt();
			aBoolean4769 = (stream.readUnsignedByte() ^ 0xffffffff) == -2;
			anInt4757 = stream.readUnsignedByte();
		}
		if ((type ^ 0xffffffff) == -10) {
			anInt4752 = stream.readUnsignedByte();
			anInt4754 = stream.readInt();
			aBoolean4721 = stream.readUnsignedByte() == 1;
		}
		int i_20_ = stream.read24BitInt();
		int i_21_ = stream.readUnsignedByte();
		if ((i_21_ ^ 0xffffffff) != -1) {
			aByteArray4806 = new byte[11];
			aByteArray4733 = new byte[11];
			anIntArray4705 = new int[11];
			for (/**/; i_21_ != 0; i_21_ = stream.readUnsignedByte()) {
				int i_22_ = (i_21_ >> 4) - 1;
				i_21_ = stream.readUnsignedByte() | i_21_ << 8;
				i_21_ &= 0xfff;
				if (i_21_ == 4095) {
					i_21_ = -1;
				}
				byte b_23_ = (byte) stream.readByte();
				if ((b_23_ ^ 0xffffffff) != -1) {
					aBoolean4802 = true;
				}
				byte b_24_ = (byte) stream.readByte();
				anIntArray4705[i_22_] = i_21_;
				aByteArray4806[i_22_] = b_23_;
				aByteArray4733[i_22_] = b_24_;
			}
		}
		aString4779 = stream.readString();
		int i_25_ = stream.readUnsignedByte();
		int i_26_ = 0xf & i_25_;
		if (i_26_ > 0) {
			aStringArray4686 = new String[i_26_];
			for (int i_27_ = 0; (i_27_ ^ 0xffffffff) > (i_26_ ^ 0xffffffff); i_27_++)
				aStringArray4686[i_27_] = stream.readString();
		}
		int i_28_ = i_25_ >> 4;
		if ((i_28_ ^ 0xffffffff) < -1) {
			int i_29_ = stream.readUnsignedByte();
			anIntArray4863 = new int[i_29_ - -1];
			for (int i_30_ = 0; (i_30_ ^ 0xffffffff) > (anIntArray4863.length ^ 0xffffffff); i_30_++)
				anIntArray4863[i_30_] = -1;
			anIntArray4863[i_29_] = stream.readUnsignedShort();
		}
		if ((i_28_ ^ 0xffffffff) < -2) {
			int i_31_ = stream.readUnsignedByte();
			anIntArray4863[i_31_] = stream.readUnsignedShort();
		}
		aString4784 = stream.readString();
		if (aString4784.equals("")) {
			aString4784 = null;
		}
		anInt4708 = stream.readUnsignedByte();
		anInt4795 = stream.readUnsignedByte();
		anInt4860 = stream.readUnsignedByte();
		aString4786 = stream.readString();
		int i_32_ = -1;
		if ((method925(i_20_) ^ 0xffffffff) != -1) {
			i_32_ = stream.readUnsignedShort();
			if (i_32_ == 65535) {
				i_32_ = -1;
			}
			anInt4698 = stream.readUnsignedShort();
			if ((anInt4698 ^ 0xffffffff) == -65536) {
				anInt4698 = -1;
			}
			anInt4839 = stream.readUnsignedShort();
			if (anInt4839 == 65535) {
				anInt4839 = -1;
			}
		}
		if (newInt >= 0) {
			anInt4761 = stream.readUnsignedShort();
			if (anInt4761 == 65535) {
				anInt4761 = -1;
			}
		}
		aNode_Sub35_4840 = new IComponentSettings(i_20_, i_32_);
		if (newInt >= 0) {
			int i_33_ = stream.readUnsignedByte();
			for (int i_34_ = 0; i_33_ > i_34_; i_34_++) {
				int i_35_ = stream.read24BitInt();
				int i_36_ = stream.readInt();
				aHashTable4823.put((long) i_35_, i_36_);
			}
			int i_37_ = stream.readUnsignedByte();
			for (int i_38_ = 0; i_38_ < i_37_; i_38_++) {
				int i_39_ = stream.read24BitInt();
				String string = stream.readJagString();
				aHashTable4823.put((long) i_39_, string);
			}
		}
		anObjectArray4758 = decodeScript(stream);
		anObjectArray4706 = decodeScript(stream);
		anObjectArray4818 = decodeScript(stream);
		anObjectArray4771 = decodeScript(stream);
		anObjectArray4768 = decodeScript(stream);
		anObjectArray4807 = decodeScript(stream);
		anObjectArray4742 = decodeScript(stream);
		anObjectArray4788 = decodeScript(stream);
		anObjectArray4701 = decodeScript(stream);
		anObjectArray4770 = decodeScript(stream);
		if ((newInt ^ 0xffffffff) <= -1) {
			anObjectArray4751 = decodeScript(stream);
		}
		anObjectArray4834 = decodeScript(stream);
		anObjectArray4774 = decodeScript(stream);
		anObjectArray4803 = decodeScript(stream);
		anObjectArray4680 = decodeScript(stream);
		anObjectArray4856 = decodeScript(stream);
		anObjectArray4852 = decodeScript(stream);
		anObjectArray4711 = decodeScript(stream);
		anObjectArray4753 = decodeScript(stream);
		anObjectArray4688 = decodeScript(stream);
		anObjectArray4775 = decodeScript(stream);
		anIntArray4838 = method4150(stream);
		anIntArray4833 = method4150(stream);
		anIntArray4789 = method4150(stream);
		anIntArray4829 = method4150(stream);
		anIntArray4805 = method4150(stream);
	}

	private final Object[] decodeScript(InputStream buffer) {
		int i = buffer.readUnsignedByte();
		if ((i ^ 0xffffffff) == -1) {
			return null;
		}
		Object[] objects = new Object[i];
		for (int i_3_ = 0; i > i_3_; i_3_++) {
			int i_4_ = buffer.readUnsignedByte();
			if (i_4_ == 0) {
				objects[i_3_] = new Integer(buffer.readInt());
			} else if ((i_4_ ^ 0xffffffff) == -2) {
				objects[i_3_] = buffer.readString();
			}
		}
		hasScripts = true;
		return objects;
	}

	private final int[] method4150(InputStream buffer) {
		int i = buffer.readUnsignedByte();
		if (i == 0) {
			return null;
		}
		int[] is = new int[i];
		for (int i_60_ = 0; i_60_ < i; i_60_++)
			is[i_60_] = buffer.readInt();
		return is;
	}

	static final int method925(int i) {
		return (i & 0x3fda8) >> 11;
	}

	public IComponentDefinitions() {
		anInt4698 = -1;
		aBoolean4730 = false;
		anInt4747 = 0;
		aBoolean4707 = false;
		anInt4682 = 0;
		anInt4752 = 1;
		anInt4728 = 0;
		anInt4759 = -1;
		aBoolean4727 = false;
		aBoolean4769 = false;
		anInt4693 = 0;
		anInt4767 = 0;
		aByte4750 = (byte) 0;
		anInt4679 = 0;
		anInt4761 = -1;
		aString4779 = "";
		aByte4741 = (byte) 0;
		anInt4780 = 0;
		aBoolean4782 = true;
		anInt4757 = 0;
		anInt4754 = 0;
		anInt4687 = -1;
		anInt4783 = -1;
		anInt4773 = -1;
		anInt4795 = 0;
		anInt4796 = 0;
		anInt4681 = 0;
		anInt4714 = 2;
		aBoolean4802 = false;
		hasScripts = false;
		anInt4744 = 0;
		aString4786 = "";
		anInt4700 = 1;
		anInt4697 = 0;
		aBoolean4819 = false;
		ihash = -1;
		anInt4821 = 0;
		anInt4815 = 0;
		anInt4718 = -1;
		anInt4820 = -1;
		aBoolean4721 = false;
		aByte4720 = (byte) 0;
		anInt4817 = 0;
		anInt4708 = 0;
		anInt4810 = 0;
		anInt4787 = 100;
		aBoolean4738 = false;
		anInt4691 = 0;
		aBoolean4710 = false;
		anInt4825 = 0;
		anInt4719 = 0;
		anInt4734 = 0;
		aString4790 = "";
		aBoolean4832 = true;
		anInt4762 = 0;
		anInt4792 = 1;
		anInt4735 = 0;
		anInt4831 = 0;
		anInt4800 = 0;
		anInt4748 = -1;
		anInt4809 = 0;
		anInt4835 = 0;
		anInt4695 = 0;
		anInt4813 = 0;
		modelType = 1;
		aNode_Sub35_4840 = GLOBAL_SETTINGS;
		anInt4814 = 0;
		anInt4811 = 0;
		anInt4722 = 0;
		anInt4849 = 0;
		anInt4683 = -1;
		parentId = -1;
		anInt4801 = 0;
		anInt4824 = -1;
		aWidget4836 = null;
		anInt4703 = -1;
		aBoolean4858 = false;
		hidden = false;
		anInt4850 = 0;
		aBoolean4861 = false;
		anInt4848 = -1;
		anInt4797 = 0;
		anInt4860 = 0;// Class339_Sub4.anInt8679;
		aBoolean4723 = false;
		aByte4851 = (byte) 0;
		anInt4816 = 0;
		anInt4842 = 0;
		anInt4839 = -1;
	}

}
