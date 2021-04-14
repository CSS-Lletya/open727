package skills.runecrafting;

import java.util.Arrays;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.NewForceMovement;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.others.YellowWizard;
import com.rs.game.player.Player;
import com.rs.game.player.controlers.Controler;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * 
 * @author Tyler & Dragonkk
 * 
 */
public class RunespanControler extends Controler {
	public static WorldTile WIZARD_TOWER = new WorldTile(3106, 6160, 1);
	public static WorldTile LOWER_LEVEL = new WorldTile(3994, 6105, 1);
	public static WorldTile HIGHER_LEVEL = new WorldTile(4149, 6104, 1);
	public static WorldTile VINE_LADDER = new WorldTile(3957, 6106, 1);
	private static final WorldTile[] RANDOM_LOCATIONS = { new WorldTile(3959, 6104, 1), new WorldTile(3996, 6055, 1),
			new WorldTile(3984, 6086, 1), new WorldTile(4399, 6041, 1)

	};
	private long startTime;
	private YellowWizard yellowWizard;

	private static enum HandledPlatforms {
		EARTH_1(3983, 6112, 3978, 6117), EARTH_2(4019, 6093, 4019, 6099), EARTH_3(4002, 6118, 4007, 6123), EARTH_4(4004,
				6134, 3998, 6134), EARTH_5(3957, 6097, 3957, 6091), EARTH_6(3943, 6053, 3943, 6047), EARTH_7(3973, 6047,
						3968, 6042), EARTH_8(3984, 6053, 3990, 6053), EARTH_9(4021, 6053, 4021, 6047), EARTH_10(3952,
								6039, 3958,
								6039), EARTH_11(3943, 6047, 3943, 6053), EARTH_12(3940, 6033, 3935, 6028), EARTH_13(
										3993, 6036, 3988, 6041), EARTH_14(4021, 6060, 4021, 6066), EARTH_15(3923, 6047,
												3928, 6052), EARTH_16(3924, 6063, 3919, 6068), EARTH_17(3925, 6098,
														3931, 6098), EARTH_18(3921, 6101, 3921, 6107), EARTH_19(3940,
																6122, 3946,
																6122), EARTH_20(3946, 6122, 3940, 6122), EARTH_21(3909,
																		6116, 3909, 6110), EARTH_22(4180, 6086, 4180,
																				6080), EARTH_23(4303, 6040, 4303,
																						6046), EARTH_24(4400, 6052,
																								4400,
																								6058), EARTH_25(4336,
																										6134, 4330,
																										6134), ICE_1(
																												3996,
																												6118,
																												3991,
																												6123), ICE_2(
																														3991,
																														6097,
																														3986,
																														6092), ICE_3(
																																4008,
																																6087,
																																4014,
																																6087), ICE_4(
																																		4023,
																																		6110,
																																		4023,
																																		6116), ICE_5(
																																				4017,
																																				6130,
																																				4023,
																																				6130), ICE_6(
																																						3954,
																																						6087,
																																						3948,
																																						6087), ICE_7(
																																								4024,
																																								6033,
																																								4024,
																																								6027), ICE_8(
																																										3994,
																																										6029,
																																										3988,
																																										6029), ICE_9(
																																												3947,
																																												6032,
																																												3947,
																																												6026), ICE_10(
																																														3922,
																																														6032,
																																														3922,
																																														6026), ICE_11(
																																																3966,
																																																6032,
																																																3966,
																																																6026), ICE_12(
																																																		4002,
																																																		6025,
																																																		4007,
																																																		6020), ICE_13(
																																																				3921,
																																																				6057,
																																																				3915,
																																																				6057), ICE_14(
																																																						3916,
																																																						6085,
																																																						3921,
																																																						6090), ICE_15(
																																																								3916,
																																																								6100,
																																																								3911,
																																																								6105), ICE_16(
																																																										3919,
																																																										6114,
																																																										3914,
																																																										6119), ICE_17(
																																																												3950,
																																																												6133,
																																																												3955,
																																																												6138), ICE_18(
																																																														3927,
																																																														6127,
																																																														3922,
																																																														6132), ICE_19(
																																																																4381,
																																																																6025,
																																																																4386,
																																																																6030), ICE_20(
																																																																		4302,
																																																																		6066,
																																																																		4302,
																																																																		6072), ICE_21(
																																																																				4377,
																																																																				6136,
																																																																				4371,
																																																																				6136), SMALL_MISSILE_1(
																																																																						3932,
																																																																						6130,
																																																																						3932,
																																																																						6136), SMALL_MISSLE_2(
																																																																								4012,
																																																																								6122,
																																																																								4012,
																																																																								6116), SMALL_MISSLE_3(
																																																																										3973,
																																																																										6132,
																																																																										3978,
																																																																										6137), SMALL_MISSLE_4(
																																																																												3963,
																																																																												6126,
																																																																												3957,
																																																																												6126), SMALL_MISSLE_5(
																																																																														4019,
																																																																														6036,
																																																																														4014,
																																																																														6031), SMALL_MISSLE_6(
																																																																																4009,
																																																																																6029,
																																																																																4003,
																																																																																6029), SMALL_MISSLE_7(
																																																																																		3980,
																																																																																		6026,
																																																																																		3974,
																																																																																		6026), SMALL_MISSLE_8(
																																																																																				3937,
																																																																																				6039,
																																																																																				3931,
																																																																																				6039), SMALL_MISSLE_9(
																																																																																						4003,
																																																																																						6029,
																																																																																						4009,
																																																																																						6029), SMALL_MISSLE_10(
																																																																																								4022,
																																																																																								6073,
																																																																																								4022,
																																																																																								6079), SMALL_MISSLE_11(
																																																																																										3918,
																																																																																										6035,
																																																																																										3912,
																																																																																										6035), SMALL_MISSLE_12(
																																																																																												3915,
																																																																																												6072,
																																																																																												3915,
																																																																																												6078), SMALL_MISSLE_13(
																																																																																														3942,
																																																																																														6107,
																																																																																														3937,
																																																																																														6112), SMALL_MISSLE_14(
																																																																																																3911,
																																																																																																6126,
																																																																																																3911,
																																																																																																6132), SMALL_MISSLE_15(
																																																																																																		4189,
																																																																																																		6053,
																																																																																																		4184,
																																																																																																		6058), SMALL_MISSLE_16(
																																																																																																				4203,
																																																																																																				6103,
																																																																																																				4208,
																																																																																																				6108), SMALL_MISSLE_17(
																																																																																																						4153,
																																																																																																						6064,
																																																																																																						4159,
																																																																																																						6064), SMALL_MISSLE_18(
																																																																																																								4130,
																																																																																																								6089,
																																																																																																								4130,
																																																																																																								6095), SMALL_MISSLE_19(
																																																																																																										4167,
																																																																																																										6092,
																																																																																																										4173,
																																																																																																										6092), SMALL_MISSLE_20(
																																																																																																												4344,
																																																																																																												6025,
																																																																																																												4350,
																																																																																																												6025), SMALL_MISSLE_21(
																																																																																																														4405,
																																																																																																														6123,
																																																																																																														4400,
																																																																																																														6128), MISSILE_1(
																																																																																																																4125,
																																																																																																																6096,
																																																																																																																4120,
																																																																																																																6091), MISSILE_2(
																																																																																																																		4114,
																																																																																																																		6033,
																																																																																																																		4114,
																																																																																																																		6027), MISSILE_3(
																																																																																																																				4146,
																																																																																																																				6026,
																																																																																																																				4146,
																																																																																																																				6020), MISSILE_4(
																																																																																																																						4205,
																																																																																																																						6036,
																																																																																																																						4200,
																																																																																																																						6041), MISSILE_5(
																																																																																																																								4113,
																																																																																																																								6042,
																																																																																																																								4119,
																																																																																																																								6042), MISSILE_6(
																																																																																																																										4130,
																																																																																																																										6051,
																																																																																																																										4135,
																																																																																																																										6056), MISSILE_7(
																																																																																																																												4119,
																																																																																																																												6042,
																																																																																																																												4113,
																																																																																																																												6042), MISSLE_8(
																																																																																																																														4113,
																																																																																																																														6058,
																																																																																																																														4119,
																																																																																																																														6058), MISSLE_9(
																																																																																																																																4167,
																																																																																																																																6031,
																																																																																																																																4172,
																																																																																																																																6036), MISSLE_10(
																																																																																																																																		4195,
																																																																																																																																		6055,
																																																																																																																																		4172,
																																																																																																																																		6036), MISSLE_11(
																																																																																																																																				4200,
																																																																																																																																				6060,
																																																																																																																																				4195,
																																																																																																																																				6055), MISSLE_12(
																																																																																																																																						4205,
																																																																																																																																						6071,
																																																																																																																																						4210,
																																																																																																																																						6076), MISSLE_13(
																																																																																																																																								4194,
																																																																																																																																								6104,
																																																																																																																																								4189,
																																																																																																																																								6109), MISSLE_14(
																																																																																																																																										4195,
																																																																																																																																										6126,
																																																																																																																																										4189,
																																																																																																																																										6126), MISSLE_15(
																																																																																																																																												4110,
																																																																																																																																												6119,
																																																																																																																																												4105,
																																																																																																																																												6124), MISSLE_16(
																																																																																																																																														4358,
																																																																																																																																														6033,
																																																																																																																																														4358,
																																																																																																																																														6039), MISSLE_17(
																																																																																																																																																4334,
																																																																																																																																																6052,
																																																																																																																																																4334,
																																																																																																																																																6058), MISSLE_18(
																																																																																																																																																		4304,
																																																																																																																																																		6082,
																																																																																																																																																		4310,
																																																																																																																																																		6082), MISSLE_19(
																																																																																																																																																				4397,
																																																																																																																																																				6107,
																																																																																																																																																				4391,
																																																																																																																																																				6107), MISSLE_20(
																																																																																																																																																						4382,
																																																																																																																																																						6130,
																																																																																																																																																						4377,
																																																																																																																																																						6125), VINE_1(
																																																																																																																																																								4125,
																																																																																																																																																								6109,
																																																																																																																																																								4120,
																																																																																																																																																								6114), VINE_2(
																																																																																																																																																										4104,
																																																																																																																																																										6031,
																																																																																																																																																										4104,
																																																																																																																																																										6025), VINE_3(
																																																																																																																																																												4188,
																																																																																																																																																												6031,
																																																																																																																																																												4183,
																																																																																																																																																												6036), VINE_4(
																																																																																																																																																														4147,
																																																																																																																																																														6033,
																																																																																																																																																														4152,
																																																																																																																																																														6038), VINE_5(
																																																																																																																																																																4203,
																																																																																																																																																																6046,
																																																																																																																																																																4209,
																																																																																																																																																																6046), VINE_6(
																																																																																																																																																																		4212,
																																																																																																																																																																		6085,
																																																																																																																																																																		4212,
																																																																																																																																																																		6091), VINE_7(
																																																																																																																																																																				4188,
																																																																																																																																																																				6130,
																																																																																																																																																																				4193,
																																																																																																																																																																				6135), VINE_8(
																																																																																																																																																																						4152,
																																																																																																																																																																						6131,
																																																																																																																																																																						4157,
																																																																																																																																																																						6126), VINE_9(
																																																																																																																																																																								4119,
																																																																																																																																																																								6123,
																																																																																																																																																																								4114,
																																																																																																																																																																								6128), VINE_10(
																																																																																																																																																																										4332,
																																																																																																																																																																										6034,
																																																																																																																																																																										4332,
																																																																																																																																																																										6040), VINE_11(
																																																																																																																																																																												4311,
																																																																																																																																																																												6109,
																																																																																																																																																																												4316,
																																																																																																																																																																												6104), CONJURATION_1(
																																																																																																																																																																														4141,
																																																																																																																																																																														6103,
																																																																																																																																																																														4135,
																																																																																																																																																																														6103), CONJURATION_2(
																																																																																																																																																																																4193,
																																																																																																																																																																																6072,
																																																																																																																																																																																4187,
																																																																																																																																																																																6072), CONJURATION_3(
																																																																																																																																																																																		4193,
																																																																																																																																																																																		6091,
																																																																																																																																																																																		4187,
																																																																																																																																																																																		6091), CONJURATION_5(
																																																																																																																																																																																				4206,
																																																																																																																																																																																				6118,
																																																																																																																																																																																				4201,
																																																																																																																																																																																				6123), CONJURATION_6(
																																																																																																																																																																																						4146,
																																																																																																																																																																																						6107,
																																																																																																																																																																																						4146,
																																																																																																																																																																																						6113), COSMIC_1(
																																																																																																																																																																																								4125,
																																																																																																																																																																																								6122,
																																																																																																																																																																																								4131,
																																																																																																																																																																																								6122), COSMIC_2(
																																																																																																																																																																																										4194,
																																																																																																																																																																																										6028,
																																																																																																																																																																																										4200,
																																																																																																																																																																																										6028), COSMIC_3(
																																																																																																																																																																																												4130,
																																																																																																																																																																																												6040,
																																																																																																																																																																																												4135,
																																																																																																																																																																																												6035), COSMIC_4(
																																																																																																																																																																																														4109,
																																																																																																																																																																																														6045,
																																																																																																																																																																																														4109,
																																																																																																																																																																																														6051), COSMIC_5(
																																																																																																																																																																																																4163,
																																																																																																																																																																																																6041,
																																																																																																																																																																																																4169,
																																																																																																																																																																																																6041), COSMIC_6(
																																																																																																																																																																																																		4213,
																																																																																																																																																																																																		6050,
																																																																																																																																																																																																		4218,
																																																																																																																																																																																																		6055), COSMIC_7(
																																																																																																																																																																																																				4186,
																																																																																																																																																																																																				6122,
																																																																																																																																																																																																				4186,
																																																																																																																																																																																																				6116), COSMIC_8(
																																																																																																																																																																																																						4103,
																																																																																																																																																																																																						6071,
																																																																																																																																																																																																						4103,
																																																																																																																																																																																																						6077), COSMIC_9(
																																																																																																																																																																																																								4140,
																																																																																																																																																																																																								6136,
																																																																																																																																																																																																								4146,
																																																																																																																																																																																																								6136), COSMIC_10(
																																																																																																																																																																																																										4390,
																																																																																																																																																																																																										6043,
																																																																																																																																																																																																										4384,
																																																																																																																																																																																																										6043), COSMIC_11(
																																																																																																																																																																																																												4325,
																																																																																																																																																																																																												6047,
																																																																																																																																																																																																												4320,
																																																																																																																																																																																																												6052), COSMIC_12(
																																																																																																																																																																																																														4310,
																																																																																																																																																																																																														6056,
																																																																																																																																																																																																														4304,
																																																																																																																																																																																																														6056), MIST_1(
																																																																																																																																																																																																																4110,
																																																																																																																																																																																																																6109,
																																																																																																																																																																																																																4110,
																																																																																																																																																																																																																6103), MIST_2(
																																																																																																																																																																																																																		4125,
																																																																																																																																																																																																																		6037,
																																																																																																																																																																																																																		4125,
																																																																																																																																																																																																																		6031), MIST_3(
																																																																																																																																																																																																																				4213,
																																																																																																																																																																																																																				6023,
																																																																																																																																																																																																																				4208,
																																																																																																																																																																																																																				6028), MIST_4(
																																																																																																																																																																																																																						4153,
																																																																																																																																																																																																																						6029,
																																																																																																																																																																																																																						4159,
																																																																																																																																																																																																																						6029), MIST_5(
																																																																																																																																																																																																																								4152,
																																																																																																																																																																																																																								6045,
																																																																																																																																																																																																																								4147,
																																																																																																																																																																																																																								6050), MIST_6(
																																																																																																																																																																																																																										4212,
																																																																																																																																																																																																																										6062,
																																																																																																																																																																																																																										4206,
																																																																																																																																																																																																																										6062), MIST_7(
																																																																																																																																																																																																																												4206,
																																																																																																																																																																																																																												6084,
																																																																																																																																																																																																																												4201,
																																																																																																																																																																																																																												6089), MIST_8(
																																																																																																																																																																																																																														4210,
																																																																																																																																																																																																																														6119,
																																																																																																																																																																																																																														4210,
																																																																																																																																																																																																																														6125), MIST_9(
																																																																																																																																																																																																																																4180,
																																																																																																																																																																																																																																6132,
																																																																																																																																																																																																																																4175,
																																																																																																																																																																																																																																6137), MIST_10(
																																																																																																																																																																																																																																		4183,
																																																																																																																																																																																																																																		6112,
																																																																																																																																																																																																																																		4177,
																																																																																																																																																																																																																																		6112), MIST_11(
																																																																																																																																																																																																																																				4113,
																																																																																																																																																																																																																																				6068,
																																																																																																																																																																																																																																				4107,
																																																																																																																																																																																																																																				6068), MIST_12(
																																																																																																																																																																																																																																						4136,
																																																																																																																																																																																																																																						6126,
																																																																																																																																																																																																																																						4136,
																																																																																																																																																																																																																																						6132), MIST_13(
																																																																																																																																																																																																																																								4158,
																																																																																																																																																																																																																																								6117,
																																																																																																																																																																																																																																								4158,
																																																																																																																																																																																																																																								6111), MIST_14(
																																																																																																																																																																																																																																										4312,
																																																																																																																																																																																																																																										6031,
																																																																																																																																																																																																																																										4318,
																																																																																																																																																																																																																																										6031), MIST_15(
																																																																																																																																																																																																																																												4339,
																																																																																																																																																																																																																																												6127,
																																																																																																																																																																																																																																												4339,
																																																																																																																																																																																																																																												6121), MIST_16(
																																																																																																																																																																																																																																														4324,
																																																																																																																																																																																																																																														6111,
																																																																																																																																																																																																																																														4324,
																																																																																																																																																																																																																																														6117), MIST_17(
																																																																																																																																																																																																																																																4376,
																																																																																																																																																																																																																																																6111,
																																																																																																																																																																																																																																																4376,
																																																																																																																																																																																																																																																6105), MIST_18(
																																																																																																																																																																																																																																																		4390,
																																																																																																																																																																																																																																																		6077,
																																																																																																																																																																																																																																																		4384,
																																																																																																																																																																																																																																																		6077), FLOAT_1(
																																																																																																																																																																																																																																																				3997,
																																																																																																																																																																																																																																																				6094,
																																																																																																																																																																																																																																																				3997,
																																																																																																																																																																																																																																																				6088), FLOAT_2(
																																																																																																																																																																																																																																																						3971,
																																																																																																																																																																																																																																																						6118,
																																																																																																																																																																																																																																																						3971,
																																																																																																																																																																																																																																																						6112), FLOAT_3(
																																																																																																																																																																																																																																																								3982,
																																																																																																																																																																																																																																																								6103,
																																																																																																																																																																																																																																																								3988,
																																																																																																																																																																																																																																																								6103), FLOAT_4(
																																																																																																																																																																																																																																																										4007,
																																																																																																																																																																																																																																																										6104,
																																																																																																																																																																																																																																																										4013,
																																																																																																																																																																																																																																																										6104), FLOAT_5(
																																																																																																																																																																																																																																																												3954,
																																																																																																																																																																																																																																																												6104,
																																																																																																																																																																																																																																																												3948,
																																																																																																																																																																																																																																																												6104), FLOAT_6(
																																																																																																																																																																																																																																																														3937,
																																																																																																																																																																																																																																																														6086,
																																																																																																																																																																																																																																																														3937,
																																																																																																																																																																																																																																																														6092), FLOAT_7(
																																																																																																																																																																																																																																																																3933,
																																																																																																																																																																																																																																																																6075,
																																																																																																																																																																																																																																																																3927,
																																																																																																																																																																																																																																																																6075), FLOAT_8(
																																																																																																																																																																																																																																																																		3935,
																																																																																																																																																																																																																																																																		6061,
																																																																																																																																																																																																																																																																		3929,
																																																																																																																																																																																																																																																																		6061), FLOAT_9(
																																																																																																																																																																																																																																																																				3954,
																																																																																																																																																																																																																																																																				6064,
																																																																																																																																																																																																																																																																				3959,
																																																																																																																																																																																																																																																																				6059), FLOAT_10(
																																																																																																																																																																																																																																																																						3999,
																																																																																																																																																																																																																																																																						6062,
																																																																																																																																																																																																																																																																						3999,
																																																																																																																																																																																																																																																																						6068), FLOAT_11(
																																																																																																																																																																																																																																																																								4010,
																																																																																																																																																																																																																																																																								6058,
																																																																																																																																																																																																																																																																								4016,
																																																																																																																																																																																																																																																																								6058), FLOAT_12(
																																																																																																																																																																																																																																																																										4004,
																																																																																																																																																																																																																																																																										6043,
																																																																																																																																																																																																																																																																										3999,
																																																																																																																																																																																																																																																																										6038), FLOAT_13(
																																																																																																																																																																																																																																																																												3925,
																																																																																																																																																																																																																																																																												6114,
																																																																																																																																																																																																																																																																												3930,
																																																																																																																																																																																																																																																																												6119), FLOAT_14(
																																																																																																																																																																																																																																																																														4167,
																																																																																																																																																																																																																																																																														6099,
																																																																																																																																																																																																																																																																														4172,
																																																																																																																																																																																																																																																																														6104), FLOAT_15(
																																																																																																																																																																																																																																																																																4136,
																																																																																																																																																																																																																																																																																6062,
																																																																																																																																																																																																																																																																																4136,
																																																																																																																																																																																																																																																																																6068), FLOAT_16(
																																																																																																																																																																																																																																																																																		4130,
																																																																																																																																																																																																																																																																																		6071,
																																																																																																																																																																																																																																																																																		4125,
																																																																																																																																																																																																																																																																																		6066), FLOAT_17(
																																																																																																																																																																																																																																																																																				4141,
																																																																																																																																																																																																																																																																																				6092,
																																																																																																																																																																																																																																																																																				4146,
																																																																																																																																																																																																																																																																																				6097), FLOAT_18(
																																																																																																																																																																																																																																																																																						4406,
																																																																																																																																																																																																																																																																																						6093,
																																																																																																																																																																																																																																																																																						4406,
																																																																																																																																																																																																																																																																																						6099), FLOAT_19(
																																																																																																																																																																																																																																																																																								4310,
																																																																																																																																																																																																																																																																																								6123,
																																																																																																																																																																																																																																																																																								4315,
																																																																																																																																																																																																																																																																																								6128), FLOAT_20(
																																																																																																																																																																																																																																																																																										4406,
																																																																																																																																																																																																																																																																																										6093,
																																																																																																																																																																																																																																																																																										4406,
																																																																																																																																																																																																																																																																																										6099), FLESH(
																																																																																																																																																																																																																																																																																												4337,
																																																																																																																																																																																																																																																																																												6066,
																																																																																																																																																																																																																																																																																												4337,
																																																																																																																																																																																																																																																																																												6072), FLESH2(
																																																																																																																																																																																																																																																																																														4358,
																																																																																																																																																																																																																																																																																														6126,
																																																																																																																																																																																																																																																																																														4358,
																																																																																																																																																																																																																																																																																														6120), FLESH3(
																																																																																																																																																																																																																																																																																																4324,
																																																																																																																																																																																																																																																																																																6093,
																																																																																																																																																																																																																																																																																																4324,
																																																																																																																																																																																																																																																																																																6099), FLESH4(
																																																																																																																																																																																																																																																																																																		4364,
																																																																																																																																																																																																																																																																																																		6100,
																																																																																																																																																																																																																																																																																																		4364,
																																																																																																																																																																																																																																																																																																		6094), FLESH5(
																																																																																																																																																																																																																																																																																																				4389,
																																																																																																																																																																																																																																																																																																				6059,
																																																																																																																																																																																																																																																																																																				4383,
																																																																																																																																																																																																																																																																																																				6059), FLESH6(
																																																																																																																																																																																																																																																																																																						4356,
																																																																																																																																																																																																																																																																																																						6066,
																																																																																																																																																																																																																																																																																																						4362,
																																																																																																																																																																																																																																																																																																						6066), SKELETAL(
																																																																																																																																																																																																																																																																																																								4320,
																																																																																																																																																																																																																																																																																																								6059,
																																																																																																																																																																																																																																																																																																								4325,
																																																																																																																																																																																																																																																																																																								6064), SKELETAL2(
																																																																																																																																																																																																																																																																																																										4329,
																																																																																																																																																																																																																																																																																																										6100,
																																																																																																																																																																																																																																																																																																										4334,
																																																																																																																																																																																																																																																																																																										6095), SKELETAL3(
																																																																																																																																																																																																																																																																																																												4371,
																																																																																																																																																																																																																																																																																																												6112,
																																																																																																																																																																																																																																																																																																												4366,
																																																																																																																																																																																																																																																																																																												6107), SKELETAL4(
																																																																																																																																																																																																																																																																																																														4383,
																																																																																																																																																																																																																																																																																																														6098,
																																																																																																																																																																																																																																																																																																														4383,
																																																																																																																																																																																																																																																																																																														6092), SKELETAL5(
																																																																																																																																																																																																																																																																																																																4374,
																																																																																																																																																																																																																																																																																																																6074,
																																																																																																																																																																																																																																																																																																																4369,
																																																																																																																																																																																																																																																																																																																6069), SKELETAL6(
																																																																																																																																																																																																																																																																																																																		4379,
																																																																																																																																																																																																																																																																																																																		6048,
																																																																																																																																																																																																																																																																																																																		4379,
																																																																																																																																																																																																																																																																																																																		6054), GREATER_MISSILE7(
																																																																																																																																																																																																																																																																																																																				4353,
																																																																																																																																																																																																																																																																																																																				6054,
																																																																																																																																																																																																																																																																																																																				4353,
																																																																																																																																																																																																																																																																																																																				6060), GREATER_MISSILE6(
																																																																																																																																																																																																																																																																																																																						4374,
																																																																																																																																																																																																																																																																																																																						6082,
																																																																																																																																																																																																																																																																																																																						4369,
																																																																																																																																																																																																																																																																																																																						6087), GREATER_MISSILE5(
																																																																																																																																																																																																																																																																																																																								4362,
																																																																																																																																																																																																																																																																																																																								6117,
																																																																																																																																																																																																																																																																																																																								4368,
																																																																																																																																																																																																																																																																																																																								6117), GREATER_MISSILE4(
																																																																																																																																																																																																																																																																																																																										4348,
																																																																																																																																																																																																																																																																																																																										6107,
																																																																																																																																																																																																																																																																																																																										4348,
																																																																																																																																																																																																																																																																																																																										6101), GREATER_MISSILE3(
																																																																																																																																																																																																																																																																																																																												4317,
																																																																																																																																																																																																																																																																																																																												6085,
																																																																																																																																																																																																																																																																																																																												4317,
																																																																																																																																																																																																																																																																																																																												6091), GREATER_MISSILE2(
																																																																																																																																																																																																																																																																																																																														4300,
																																																																																																																																																																																																																																																																																																																														6092,
																																																																																																																																																																																																																																																																																																																														4300,
																																																																																																																																																																																																																																																																																																																														6098), GREATER_MISSILE1(
																																																																																																																																																																																																																																																																																																																																4371,
																																																																																																																																																																																																																																																																																																																																6032,
																																																																																																																																																																																																																																																																																																																																4376,
																																																																																																																																																																																																																																																																																																																																6037), GREATER_MISSILE(
																																																																																																																																																																																																																																																																																																																																		4325,
																																																																																																																																																																																																																																																																																																																																		6076,
																																																																																																																																																																																																																																																																																																																																		4331,
																																																																																																																																																																																																																																																																																																																																		6076);

		private WorldTile smallIsland, largeIsland;

		private HandledPlatforms(int largeIslandX, int largeIslandY, int smallIslandX, int smallIslandY) {
			largeIsland = new WorldTile(largeIslandX, largeIslandY, 1);
			smallIsland = new WorldTile(smallIslandX, smallIslandY, 1);
		}

		private static Object[] getToPlataform(WorldTile fromPlataform) {
			for (HandledPlatforms toPlatraform : HandledPlatforms.values()) {
				if (toPlatraform.smallIsland.matches(fromPlataform))
					return new Object[] { toPlatraform.largeIsland, true };
				else if (toPlatraform.largeIsland.matches(fromPlataform))
					return new Object[] { toPlatraform.smallIsland, false };
			}
			return null;

		}
	}

	private static int AIR_RUNE = 24215, EARTH_RUNE = 24216, WATER_RUNE = 24214, MIND_RUNE = 24217, FIRE_RUNE = 24213,
			ELEMENTAL_RUNE = -1, BODY_RUNE = 24218, RUNE_ESSENCE = 24227, CHAOS_RUNE = 24221, NATURE_RUNE = 24220,
			COSMIC_RUNE = 24223, ASTRAL_RUNE = 24224, LAW_RUNE = 24222, BLOOD_RUNE = 24225, DEATH_RUNE = 24219;

	private static enum Platforms {
		EARTH(70478, 16645, 3072, -1, -1, -1, -1, false, EARTH_RUNE), EARTH2(70479, 16645, 3072, -1, -1, -1, -1, false,
				EARTH_RUNE), EARTH3(70485, 16645, 3072, -1, -1, -1, -1, false, EARTH_RUNE), EARTH4(70495, 16645, 3072,
						-1, -1, -1, -1, false,
						EARTH_RUNE), ICE(70480, 16646, 3076, -1, -1, -1, -1, false, AIR_RUNE, WATER_RUNE), ICE2(70496,
								16646, 3076, -1, -1, -1, -1, false, AIR_RUNE, WATER_RUNE), VINE(70490, 16645, 3080, -1,
										-1, -1, -1, false, WATER_RUNE, EARTH_RUNE, NATURE_RUNE), VINE2(70500, 16645,
												3080, -1, -1, -1, -1, false, WATER_RUNE, EARTH_RUNE,
												NATURE_RUNE), SMALL_MISSILE(70481, 16635, 3086, -1, -2, 16672, 3087,
														true, ELEMENTAL_RUNE, MIND_RUNE), SMALL_MISSILE2(70482, 16635,
																3086, -1, -2, 16672, 3087, true, ELEMENTAL_RUNE,
																MIND_RUNE), SMALL_MISSILE3(70487, 16635, 3086, -1, -2,
																		16672, 3087, true, ELEMENTAL_RUNE,
																		MIND_RUNE), SMALL_MISSILE4(70497, 16635, 3086,
																				-1, -2, 16672, 3087, true,
																				ELEMENTAL_RUNE,
																				MIND_RUNE), GREATER_MISSILE(70504,
																						16635, 3086, -1, -2, 16672,
																						3087, true, ELEMENTAL_RUNE,
																						BLOOD_RUNE,
																						DEATH_RUNE), GREATER_MISSILE2(
																								70505, 16635, 3086, -1,
																								-2, 16672, 3087, true,
																								ELEMENTAL_RUNE,
																								BLOOD_RUNE,
																								DEATH_RUNE), MISSILE(
																										70489, 16635,
																										3086, -1, -3,
																										16672, 3087,
																										true,
																										ELEMENTAL_RUNE,
																										CHAOS_RUNE), MISSILE2(
																												70499,
																												16635,
																												3086,
																												-1, -3,
																												16672,
																												3087,
																												true,
																												ELEMENTAL_RUNE,
																												CHAOS_RUNE), MIST1(
																														70491,
																														16635,
																														3086,
																														-1,
																														3092,
																														16672,
																														3087,
																														true,
																														BODY_RUNE,
																														WATER_RUNE,
																														NATURE_RUNE), MIST2(
																																70492,
																																16635,
																																3086,
																																-1,
																																3092,
																																16672,
																																3087,
																																true,
																																BODY_RUNE,
																																WATER_RUNE,
																																NATURE_RUNE), MIST3(
																																		70501,
																																		16635,
																																		3086,
																																		-1,
																																		3092,
																																		16672,
																																		3087,
																																		true,
																																		BODY_RUNE,
																																		WATER_RUNE,
																																		NATURE_RUNE), COSMIC(
																																				70493,
																																				16685,
																																				3095,
																																				-1,
																																				-1,
																																				16686,
																																				3096,
																																				true,
																																				COSMIC_RUNE,
																																				ASTRAL_RUNE,
																																				LAW_RUNE), COSMIC2(
																																						70502,
																																						16685,
																																						3095,
																																						-1,
																																						-1,
																																						16686,
																																						3096,
																																						true,
																																						COSMIC_RUNE,
																																						ASTRAL_RUNE,
																																						LAW_RUNE), CONJURATION(
																																								70488,
																																								16662,
																																								3089,
																																								16645,
																																								-1,
																																								-1,
																																								-1,
																																								false,
																																								MIND_RUNE,
																																								BODY_RUNE,
																																								RUNE_ESSENCE), FLESH(
																																										70506,
																																										16645,
																																										3074,
																																										-1,
																																										-1,
																																										-1,
																																										-1,
																																										false,
																																										BODY_RUNE,
																																										DEATH_RUNE,
																																										BLOOD_RUNE), SKELETAL(
																																												70503,
																																												16645,
																																												3078,
																																												-1,
																																												-1,
																																												-1,
																																												-1,
																																												false,
																																												DEATH_RUNE), FLOAT(
																																														70476,
																																														16654,
																																														3082,
																																														16652,
																																														3084,
																																														16651,
																																														3083,
																																														false,
																																														AIR_RUNE), FLOAT2(
																																																70477,
																																																16654,
																																																3082,
																																																16652,
																																																3084,
																																																16651,
																																																3083,
																																																false,
																																																AIR_RUNE), FLOAT3(
																																																		70483,
																																																		16654,
																																																		3082,
																																																		16652,
																																																		3084,
																																																		16651,
																																																		3083,
																																																		false,
																																																		AIR_RUNE), FLOAT4(
																																																				70494,
																																																				16654,
																																																				3082,
																																																				16652,
																																																				3084,
																																																				16651,
																																																				3083,
																																																				false,
																																																				AIR_RUNE);

		private int objectId, startEmote, startGraphic, middleEmote, middleGraphic, endEmote, endGraphic;
		private boolean invisible;
		private int[] runes;

		private Platforms(int objectId, int startEmote, int startGraphic, int middleEmote, int middleGraphic,
				int endEmote, int endGraphic, boolean invisible, int... runes) {
			this.objectId = objectId;
			this.startEmote = startEmote;
			this.startGraphic = startGraphic;
			this.middleEmote = middleEmote;
			this.middleGraphic = middleGraphic;
			this.endEmote = endEmote;
			this.endGraphic = endGraphic;
			this.invisible = invisible;
			this.runes = runes;
		}
	}

	public static void enterRunespan(final Player player, boolean high) {
		player.useStairs(-1, high ? HIGHER_LEVEL : LOWER_LEVEL, 0, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getControlerManager().startControler("RuneSpanControler");
			}
		});
	}

	/**
	 * Handles platforms.
	 * @param object
	 * @return
	 */
	private boolean handlePlataform(WorldObject object) {
		for (Platforms plataform : Platforms.values()) {
			if (plataform.objectId == object.getId())
				return handleCrossingPlataform(object, plataform);
		}
		return false;
	}

	/**
	 * Gets platform GFX.
	 * @param runesAmt
	 * @return platform GFX.
	 */
	private int getPlataformGfx(int runesAmt) {
		if (runesAmt == 1)
			return 3065;
		else if (runesAmt == 2)
			return 3064;
		return 3063;
	}

	/**
	 * Handle Crossing.
	 * @param object
	 * @param plataform
	 * @return
	 */
	private boolean handleCrossingPlataform(final WorldObject object, final Platforms plataform) {
		Object[] toPlataform = HandledPlatforms.getToPlataform(object);
		if (toPlataform == null)
			return false;
		boolean largeIsland = (boolean) toPlataform[1];
		final int[] runes = Arrays.copyOf(plataform.runes, plataform.runes.length);
		if (largeIsland)
			player.getPackets().sendGameMessage("You weren't charged for traveling to a larger island.");
		else {
			for (int runeId : plataform.runes) {
				if (runeId == ELEMENTAL_RUNE) {
					if (player.getInventory().containsOneItem(AIR_RUNE))
						runes[0] = AIR_RUNE;
					else if (player.getInventory().containsOneItem(WATER_RUNE))
						runes[0] = WATER_RUNE;
					else if (player.getInventory().containsOneItem(EARTH_RUNE))
						runes[0] = EARTH_RUNE;
					else if (player.getInventory().containsOneItem(FIRE_RUNE))
						runes[0] = FIRE_RUNE;
					else {
						player.getPackets().sendGameMessage("You need 1 Elemental rune to cross this.");
						return true;
					}
				} else if (!player.getInventory().containsOneItem(runeId)
						&& !player.getUsername().equalsIgnoreCase("tyler")) {
					player.getPackets().sendGameMessage(
							"You need 1 " + ItemDefinitions.getItemDefinitions(runeId).getName() + " to cross this.");
					return true;
				}
			}
			StringBuilder builder = new StringBuilder();
			for (int runeId : runes) {
				if (builder.length() != 0)
					builder.append(" & ");
				builder.append("1 " + ItemDefinitions.getItemDefinitions(runeId).getName());
				player.getInventory().deleteItem(runeId, 1);
			}
			player.getPackets().sendGameMessage(builder.toString() + " have been removed from your inventory.");

		}
		final WorldTile toTile = (WorldTile) toPlataform[0];
		player.lock(7);
		player.addWalkSteps(object.getX(), object.getY(), 1, false);
		World.sendGraphics(player, new Graphics(getPlataformGfx(plataform.runes.length)), object);
		WorldTasksManager.schedule(new WorldTask() {

			private int stage;

			@Override
			public void run() {
				if (stage == 0) {
					if (plataform.startEmote != -1)
						player.setNextAnimation(new Animation(plataform.startEmote));
					if (plataform.startGraphic != -1)
						player.setNextGraphics(new Graphics(plataform.startGraphic));
					player.setNextForceMovement(new NewForceMovement(player, 1, toTile, 5,
							Utils.getFaceDirection(toTile.getX() - object.getX(), toTile.getY() - object.getY())));
				} else if (stage == 1) {
					player.setNextWorldTile(toTile);
					if (plataform.middleEmote != -1)
						player.setNextAnimation(new Animation(plataform.middleEmote));
					if (plataform.middleGraphic == -2) { // EXCEPTION USED BY SMALL MISSILE
						int gfx;
						if (runes[0] == AIR_RUNE)
							gfx = 2699;
						else if (runes[0] == WATER_RUNE)
							gfx = 2703;
						else if (runes[0] == EARTH_RUNE)
							gfx = 2718;
						else
							gfx = 2729;
						World.sendProjectile(player, toTile, gfx, 18, 18, 20, 50, 145, 0);
					} else if (plataform.middleGraphic == -3) { // EXCEPTION USED BY MISSILE
						int gfx;
						if (runes[0] == AIR_RUNE)
							gfx = 2699;
						else if (runes[0] == WATER_RUNE)
							gfx = 2704;
						else if (runes[0] == EARTH_RUNE)
							gfx = 2719;
						else
							gfx = 2731;
						World.sendProjectile(player, toTile, gfx, 18, 18, 20, 50, 145, 0);
					} else if (plataform.middleGraphic != -1)
						player.setNextGraphics(new Graphics(plataform.middleGraphic));
					if (plataform.invisible)
						player.getAppearance().transformIntoNPC(1957);
				} else if (stage == 5) {
					if (plataform.invisible)
						player.getAppearance().transformIntoNPC(-1);
					if (plataform.endEmote != -1)
						player.setNextAnimation(new Animation(plataform.endEmote));
					if (plataform.endGraphic != -1)
						player.setNextGraphics(new Graphics(plataform.endGraphic));
				} else if (stage == 6) {
					World.sendGraphics(player, new Graphics(getPlataformGfx(plataform.runes.length)), toTile);
					stop();
				}
				stage++;

			}

		}, 0, 0);
		return true;
	}

	public int getCurrentFloor() {
		if (player.getX() > 4280)
			return 3;
		if (player.getX() > 4090)
			return 2;
		return 1;
	}

	@Override
	public void start() {
		sendInterfaces();
		player.getPackets().sendGlobalConfig(1917, getCurrentFloor());
		player.getPackets().sendGlobalConfig(1918, 0); // no runesphere active
		startTime = Utils.currentTimeMillis();
		player.getDialogueManager().startDialogue("SimpleMessage", "Welcome To the Runespan ",
				"WARNING:When you leave all the runes you make here will be removed and",
				"converted into reward points.");

	}

	@Override
	public void magicTeleported(int teleType) {
		exitRunespan();
	}

	/**
	 * Handles the exiting.
	 */
	public void exitRunespan() {
		handlePoints();
		player.addRunespanPoints(getRunespanInventoryPoints());
		player.getInterfaceManager().closeOverlay(false);
		removeControler();
	}

	public void handlePoints() {
		for (int i = 0; i < player.getInventory().getItemsContainerSize(); i++) {

		}
	}

	/**
	 * Handles the yellow wizard every ten minutes.
	 * @param player
	 * @param called
	 */
	public void handleWizzard(Player player) {
		startTime = Utils.currentTimeMillis();
		yellowWizard = new YellowWizard(new WorldTile(RANDOM_LOCATIONS[Utils.random(RANDOM_LOCATIONS.length)]), this);
		player.getPackets().sendGameMessage(
				"<col=FF0000>You hear a wizard calling for help, find him and you may recieve a reward.</col>");
	}

	/**
	 * Remove the yellow wizard.
	 */
	public void removeWizard() {
		yellowWizard = null;
	}

	@Override
	public void process() {
		if (startTime + 600000 < Utils.currentTimeMillis())
			handleWizzard(player);
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendOverlay(1274, false);
		player.getPackets().sendIComponentText(1274, 5, Integer.toString(player.getRuneSpanPoints()));
		refreshInventoryPoints();
	}

	/**
	 * gets points for inventory.
	 * @return points.
	 */
	public int getRunespanInventoryPoints() {
		if (getArguments() == null || getArguments().length == 0)
			return 0;
		return (Integer) getArguments()[0];
	}

	/**
	 * Adds inv. points.
	 * @param value
	 */
	public void addInventoryPoints(int value) {
		if (getArguments() == null || getArguments().length == 0)
			this.setArguments(new Object[1]);
		getArguments()[0] = getRunespanInventoryPoints() + value;
		refreshInventoryPoints();
	}

	/**
	 * Refresh screen interface.
	 */
	public void refreshInventoryPoints() {
		player.getPackets().sendIComponentText(1274, 2, Integer.toString(getRunespanInventoryPoints()));
	}

	@Override
	public boolean processNPCClick2(NPC npc) {
		if (SiphonActionCreatures.chipCreature(player, npc))
			return false;
		return true;
	}

	@Override
	public boolean processNPCClick3(NPC npc) {
		if (npc.getId() == 15433 && yellowWizard != null) {
			WorldTile tile = yellowWizard;
			player.getHintIconsManager().addHintIcon(tile.getX(), tile.getY(), tile.getPlane(), 150, 2, 0, -1, true);
			return false;
		}
		return true;
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		switch (npc.getId()) {
		case 15402:
			if (player.getInventory().containsItem(24227, 25)) {
				player.getPackets().sendGameMessage(
						"You have already obtained enough essense from the floating essence. Get more");
				player.getPackets().sendGameMessage("from an essence creature.");
				return false;
			}
			player.setNextAnimation(new Animation(12832));
			player.getInventory().addItem(new Item(24227, 25));
			return true;
		}
		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 70507) {
			if (object.getX() == 4367 && object.getY() == 6062) {
				player.addWalkSteps(object.getX(), object.getY(), 0, false);
				player.lock(35);
				final WorldTile dest = new WorldTile(4367, 6033, 1);
				WorldTasksManager.schedule(new WorldTask() {
					private int stage;

					@Override
					public void run() {
						if (stage == 0) {
							player.setNextFaceWorldTile(new WorldTile(4367, 6062, 1));
							player.setNextAnimation(new Animation(16662));
							player.setNextGraphics(new Graphics(3090));
						} else if (stage == 4) {
							player.setNextForceMovement(new NewForceMovement(player, 1, dest, 35,
									Utils.getFaceDirection(dest.getX() - object.getX(), dest.getY() - object.getY())));
							player.setNextGraphics(new Graphics(3091));
						} else if (stage == 35) {
							player.unlock();
							player.setNextWorldTile(dest);
							stop();
						}
						stage++;
					}

				}, 0, 0);
			} else if (object.getX() == 4367 && object.getY() == 6033) {
				player.addWalkSteps(object.getX(), object.getY(), 0, false);
				player.lock(35);
				final WorldTile dest = new WorldTile(4367, 6062, 1);
				WorldTasksManager.schedule(new WorldTask() {
					private int stage;

					@Override
					public void run() {
						if (stage == 0) {
							player.setNextFaceWorldTile(new WorldTile(4367, 6062, 1));
							player.setNextAnimation(new Animation(16662));
							player.setNextGraphics(new Graphics(3090));
						} else if (stage == 4) {
							player.setNextForceMovement(new NewForceMovement(player, 1, dest, 35,
									Utils.getFaceDirection(dest.getX() - object.getX(), dest.getY() - object.getY())));
							player.setNextGraphics(new Graphics(3091));
						} else if (stage == 35) {
							player.unlock();
							player.setNextWorldTile(dest);
							stop();
						}
						stage++;
					}

				}, 0, 0);
			}
		}
		if (object.getId() == 70508) {
			player.useStairs(16668, HIGHER_LEVEL, 4, 5);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextAnimation(new Animation(-1));
				}
			}, 3);
			player.getPackets().sendGlobalConfig(1917, 2);
			return false;
		} else if (object.getId() == 70509) {
			player.useStairs(16675, VINE_LADDER, 2, 3);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextAnimation(new Animation(-1));
				}
			}, 1);
			player.getPackets().sendGlobalConfig(1917, 1);
			return false;
		}
		return !handlePlataform(object);
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void forceClose() {
		exitRunespan();
	}
}
