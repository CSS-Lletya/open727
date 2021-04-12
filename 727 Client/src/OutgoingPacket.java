
/* Class379 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class OutgoingPacket {
	public static OutgoingPacket ACTION_BUTTON_3_PACKET;
	public static OutgoingPacket PLAYER_OPTION_6_PACKET;
	public static OutgoingPacket aClass379_4529;
	public static OutgoingPacket NPC_EXAMINE_PACKET;
	public static OutgoingPacket aClass379_4531;
	public static OutgoingPacket ATTACK_NPC_PACKET;
	public static OutgoingPacket aClass379_4533;
	public static OutgoingPacket CHANGE_FRIEND_CHAT_PACKET;
	public static OutgoingPacket aClass379_4535;
	public static OutgoingPacket ACTION_BUTTON_4_PACKET;
	public static OutgoingPacket aClass379_4537;
	public static OutgoingPacket CHAT_PACKET;
	public static OutgoingPacket PLAYER_OPTION_1_PACKET;
	public static OutgoingPacket INTERFACE_ON_PLAYER_PACKET;
	public static OutgoingPacket SEND_FRIEND_QUICK_CHAT_PACKET;
	public static OutgoingPacket SEND_FRIEND_MESSAGE_PACKET;
	public static OutgoingPacket aClass379_4543;
	public static OutgoingPacket GRAND_EXCHANGE_PACKET;
	public static OutgoingPacket aClass379_4545 = new OutgoingPacket(0, 0);
	public static OutgoingPacket ACTION_BUTTON_6_PACKET;
	public static OutgoingPacket aClass379_4547;
	public static OutgoingPacket DIALOGUE_CONTINUE_PACKET;
	public static OutgoingPacket WORLD_MAP_CLICK;
	public static OutgoingPacket ACTION_BUTTON_7_PACKET;
	public static OutgoingPacket REMOVE_FRIEND_PACKET;
	public static OutgoingPacket aClass379_4552;
	public static OutgoingPacket ADD_FRIEND_PACKET;
	public static OutgoingPacket ACTION_BUTTON_2_PACKET;
	public static OutgoingPacket KEY_PRESSED_PACKET;
	public static OutgoingPacket aClass379_4556;
	public static OutgoingPacket CHAT_TYPE_PACKET;
	public static OutgoingPacket aClass379_4558;
	public static OutgoingPacket aClass379_4559;
	public static OutgoingPacket WALKING_PACKET;
	public static OutgoingPacket REMOVE_IGNORE_PACKET;
	public static OutgoingPacket ACTION_BUTTON_5_PACKET;
	public static OutgoingPacket aClass379_4563;
	public static OutgoingPacket aClass379_4564;
	public static OutgoingPacket OBJECT_CLICK_3_PACKET;
	public static OutgoingPacket COLOR_ID_PACKET;
	public static OutgoingPacket aClass379_4567;
	public static OutgoingPacket INTERFACE_ON_NPC;
	public static OutgoingPacket MINI_WALKING_PACKET;
	public static OutgoingPacket aClass379_4570;
	public static OutgoingPacket PLAYER_OPTION_2_PACKET;
	public static OutgoingPacket aClass379_4572;
	public static OutgoingPacket INTERFACE_ON_INTERFACE_PACKET;
	public static OutgoingPacket ACTION_BUTTON_1_PACKET;
	public static OutgoingPacket OBJECT_CLICK_5_PACKET;
	public static OutgoingPacket OBJECT_EXAMINE_PACKET;
	public static OutgoingPacket NPC_CLICK_2_PACKET;
	public static OutgoingPacket ADD_IGNORE_PACKET;
	public static OutgoingPacket aClass379_4579;
	public static OutgoingPacket PLAYER_OPTION_9_PACKET;
	public static OutgoingPacket ITEM_TAKE_PACKET;
	public static OutgoingPacket aClass379_4582;
	public static OutgoingPacket aClass379_4583;
	public static OutgoingPacket CLICK_PACKET;
	public static OutgoingPacket ENTER_INTEGER_PACKET;
	public static OutgoingPacket aClass379_4586;
	public static OutgoingPacket CLOSE_INTERFACE_PACKET;
	public static OutgoingPacket GROUND_ITEM_EXAMINE_PACKET;
	public static OutgoingPacket aClass379_4589;
	public static OutgoingPacket aClass379_4590;
	public static OutgoingPacket aClass379_4591;
	public static OutgoingPacket NPC_CLICK_1_PACKET;
	public static OutgoingPacket MOVE_CAMERA_PACKET;
	public static OutgoingPacket PLAYER_OPTION_7_PACKET;
	public static OutgoingPacket aClass379_4595;
	public static OutgoingPacket aClass379_4596;
	public static OutgoingPacket aClass379_4597;
	public static OutgoingPacket JOIN_FRIEND_CHAT_PACKET;
	public static OutgoingPacket NPC_CLICK_3_PACKET;
	public static OutgoingPacket aClass379_4600;
	public static OutgoingPacket SWITCH_INTERFACE_ITEM_PACKET;
	public static OutgoingPacket OBJECT_CLICK_1_PACKET;
	public static OutgoingPacket aClass379_4603;
	public static OutgoingPacket ACTION_BUTTON_9_PACKET;
	public static OutgoingPacket ACTION_BUTTON_8_PACKET;
	public static OutgoingPacket aClass379_4606;
	public static OutgoingPacket ENTER_NAME_PACKET;
	public static OutgoingPacket ACTION_BUTTON_10_PACKET;
	public static OutgoingPacket aClass379_4609;
	public static OutgoingPacket PLAYER_OPTION_5_PACKET;
	public static OutgoingPacket REPORT_ABUSE_PACKET;
	public static OutgoingPacket COMMANDS_PACKET;
	public static OutgoingPacket aClass379_4613;
	public static OutgoingPacket ENTER_LONGSTRING_PACKET;
	public static OutgoingPacket aClass379_4615;
	public static OutgoingPacket PLAYER_OPTION_4_PACKET;
	public static OutgoingPacket aClass379_4617;
	public static OutgoingPacket KICK_FRIEND_CHAT_PACKET;
	public static OutgoingPacket aClass379_4619;
	public static OutgoingPacket OBJECT_CLICK_2_PACKET;
	public static OutgoingPacket PLAYER_OPTION_8_PACKET;
	public static OutgoingPacket NPC_CLICK_4_PACKET;
	public static OutgoingPacket PLAYER_OPTION_3_PACKET;
	public static OutgoingPacket aClass379_4624;
	public static OutgoingPacket INTERFACE_ON_OBJECT;
	public static OutgoingPacket OBJECT_CLICK_4_PACKET;
	public static OutgoingPacket SCREEN_PACKET;
	public static OutgoingPacket aClass379_4628;
	public static OutgoingPacket PLAYER_OPTION_10_PACKET;
	public static OutgoingPacket aClass379_4630;
	int anInt4631;
	int anInt4632;

	OutgoingPacket(int id, int size) {
		this.anInt4631 = -2043160167 * id;
		this.anInt4632 = -1422665881 * size;
	}

	static {
		PLAYER_OPTION_6_PACKET = new OutgoingPacket(1, 3);
		aClass379_4529 = new OutgoingPacket(2, 4);
		NPC_EXAMINE_PACKET = new OutgoingPacket(3, 3);
		INTERFACE_ON_INTERFACE_PACKET = new OutgoingPacket(4, 16);
		WORLD_MAP_CLICK = new OutgoingPacket(5, 4);
		PLAYER_OPTION_2_PACKET = new OutgoingPacket(6, 3);
		CHANGE_FRIEND_CHAT_PACKET = new OutgoingPacket(7, -1);
		aClass379_4535 = new OutgoingPacket(8, 7);
		ACTION_BUTTON_4_PACKET = new OutgoingPacket(9, 8);
		aClass379_4537 = new OutgoingPacket(10, -1);
		COLOR_ID_PACKET = new OutgoingPacket(11, 2);
		REMOVE_IGNORE_PACKET = new OutgoingPacket(12, -1);
		INTERFACE_ON_PLAYER_PACKET = new OutgoingPacket(13, 11);
		SEND_FRIEND_QUICK_CHAT_PACKET = new OutgoingPacket(14, -1);
		SEND_FRIEND_MESSAGE_PACKET = new OutgoingPacket(15, -2);
		ATTACK_NPC_PACKET = new OutgoingPacket(16, 3);
		GRAND_EXCHANGE_PACKET = new OutgoingPacket(17, 2);
		aClass379_4531 = new OutgoingPacket(18, 4);
		ACTION_BUTTON_6_PACKET = new OutgoingPacket(19, 8);
		aClass379_4547 = new OutgoingPacket(20, 3);
		ACTION_BUTTON_8_PACKET = new OutgoingPacket(21, 8);
		ACTION_BUTTON_9_PACKET = new OutgoingPacket(22, 8);
		ACTION_BUTTON_7_PACKET = new OutgoingPacket(23, 8);
		aClass379_4559 = new OutgoingPacket(24, 7);
		aClass379_4552 = new OutgoingPacket(25, 7);
		ADD_FRIEND_PACKET = new OutgoingPacket(26, -1);
		ACTION_BUTTON_2_PACKET = new OutgoingPacket(27, 8);
		KEY_PRESSED_PACKET = new OutgoingPacket(28, -2);
		REMOVE_FRIEND_PACKET = new OutgoingPacket(29, -1);
		CHAT_TYPE_PACKET = new OutgoingPacket(30, 1);
		PLAYER_OPTION_3_PACKET = new OutgoingPacket(31, 3);
		OBJECT_CLICK_4_PACKET = new OutgoingPacket(32, 9);
		WALKING_PACKET = new OutgoingPacket(33, 5);
		ADD_IGNORE_PACKET = new OutgoingPacket(34, -1);
		aClass379_4533 = new OutgoingPacket(35, -2);
		aClass379_4563 = new OutgoingPacket(36, -1);
		aClass379_4556 = new OutgoingPacket(37, 2);
		OBJECT_CLICK_3_PACKET = new OutgoingPacket(38, 9);
		aClass379_4630 = new OutgoingPacket(39, -1);
		aClass379_4567 = new OutgoingPacket(40, -1);
		INTERFACE_ON_NPC = new OutgoingPacket(41, 11);
		MINI_WALKING_PACKET = new OutgoingPacket(42, 18);
		aClass379_4564 = new OutgoingPacket(43, 7);
		aClass379_4595 = new OutgoingPacket(44, 9);
		aClass379_4572 = new OutgoingPacket(45, 1);
		aClass379_4589 = new OutgoingPacket(46, 12);
		aClass379_4613 = new OutgoingPacket(47, 4);
		OBJECT_CLICK_5_PACKET = new OutgoingPacket(48, 9);
		DIALOGUE_CONTINUE_PACKET = new OutgoingPacket(49, 6);
		NPC_CLICK_2_PACKET = new OutgoingPacket(50, 3);
		PLAYER_OPTION_7_PACKET = new OutgoingPacket(51, 3);
		aClass379_4579 = new OutgoingPacket(52, -1);
		PLAYER_OPTION_9_PACKET = new OutgoingPacket(53, 3);
		ITEM_TAKE_PACKET = new OutgoingPacket(54, 7);
		aClass379_4582 = new OutgoingPacket(55, 4);
		aClass379_4543 = new OutgoingPacket(56, -2);
		CLICK_PACKET = new OutgoingPacket(57, 7);
		ENTER_INTEGER_PACKET = new OutgoingPacket(58, 4);
		aClass379_4586 = new OutgoingPacket(59, 6);
		CLOSE_INTERFACE_PACKET = new OutgoingPacket(60, 0);
		GROUND_ITEM_EXAMINE_PACKET = new OutgoingPacket(61, 7);
		aClass379_4597 = new OutgoingPacket(62, 1);
		aClass379_4590 = new OutgoingPacket(63, 4);
		aClass379_4591 = new OutgoingPacket(64, -1);
		NPC_CLICK_1_PACKET = new OutgoingPacket(65, 3);
		PLAYER_OPTION_1_PACKET = new OutgoingPacket(66, 3);
		aClass379_4583 = new OutgoingPacket(67, 15);
		ACTION_BUTTON_3_PACKET = new OutgoingPacket(68, 8);
		aClass379_4596 = new OutgoingPacket(69, -1);
		PLAYER_OPTION_10_PACKET = new OutgoingPacket(70, 3);
		JOIN_FRIEND_CHAT_PACKET = new OutgoingPacket(71, -1);
		ACTION_BUTTON_5_PACKET = new OutgoingPacket(72, 8);
		OBJECT_EXAMINE_PACKET = new OutgoingPacket(73, 9);
		SWITCH_INTERFACE_ITEM_PACKET = new OutgoingPacket(74, 16);
		OBJECT_CLICK_1_PACKET = new OutgoingPacket(75, 9);
		aClass379_4603 = new OutgoingPacket(76, 0);
		NPC_CLICK_3_PACKET = new OutgoingPacket(77, 3);
		aClass379_4600 = new OutgoingPacket(78, -1);
		aClass379_4606 = new OutgoingPacket(79, 1);
		ENTER_NAME_PACKET = new OutgoingPacket(80, -1);
		ACTION_BUTTON_10_PACKET = new OutgoingPacket(81, 8);
		aClass379_4609 = new OutgoingPacket(82, 4);
		MOVE_CAMERA_PACKET = new OutgoingPacket(83, 4);
		SCREEN_PACKET = new OutgoingPacket(84, 6);
		COMMANDS_PACKET = new OutgoingPacket(85, -1);
		CHAT_PACKET = new OutgoingPacket(86, -1);
		ENTER_LONGSTRING_PACKET = new OutgoingPacket(87, -1);
		aClass379_4615 = new OutgoingPacket(88, 2);
		PLAYER_OPTION_4_PACKET = new OutgoingPacket(89, 3);
		aClass379_4617 = new OutgoingPacket(90, -1);
		KICK_FRIEND_CHAT_PACKET = new OutgoingPacket(91, -1);
		aClass379_4619 = new OutgoingPacket(92, -2);
		OBJECT_CLICK_2_PACKET = new OutgoingPacket(93, 9);
		PLAYER_OPTION_8_PACKET = new OutgoingPacket(94, 3);
		NPC_CLICK_4_PACKET = new OutgoingPacket(95, 3);
		ACTION_BUTTON_1_PACKET = new OutgoingPacket(96, 8);
		aClass379_4624 = new OutgoingPacket(97, -1);
		INTERFACE_ON_OBJECT = new OutgoingPacket(98, 17);
		aClass379_4558 = new OutgoingPacket(99, -2);
		REPORT_ABUSE_PACKET = new OutgoingPacket(100, -1);
		aClass379_4628 = new OutgoingPacket(101, -2);
		aClass379_4570 = new OutgoingPacket(102, -2);
		PLAYER_OPTION_5_PACKET = new OutgoingPacket(103, 3);
	}

	static final void method6439(Class527 class527, int i) {
		class527.anInt7012 -= 283782002;
		int i_1_ = (class527.anIntArray6999[1942118537 * class527.anInt7012]);
		int i_2_ = (class527.anIntArray6999[1 + 1942118537 * class527.anInt7012]);
		int i_3_ = (Class368.aClass429_4265.method7214(i_1_, -1579462583).method14918(i_2_, -1799645652).anInt2997 * -1869685303);
		class527.anIntArray6999[(class527.anInt7012 += 141891001) * 1942118537 - 1] = i_3_;
	}

	static final void method6440(Class527 class527, int i) {
		String string = "";
		if (Class182.aClipboard2263 != null) {
			Transferable transferable = Class182.aClipboard2263.getContents(null);
			if (null != transferable) {
				try {
					string = (String) transferable.getTransferData(DataFlavor.stringFlavor);
					if (string == null) {
						string = "";
					}
				} catch (Exception exception) {
					/* empty */
				}
			}
		}
		class527.anObjectArray7019[(class527.anInt7000 += 1476624725) * 1806726141 - 1] = string;
	}
}
