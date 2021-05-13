package skills;

/**
 * All constants that are important for skill levelling are stored in this class
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public interface SkillConstants {
	
	/**
	 * The id of all skills
	 */
	short ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20, CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23, DUNGEONEERING = 24;
	
	/**
	 * The names of all skills
	 */
	String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Constitution", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction", "Summoning", "Dungeoneering" };
	
	/**
	 * The maximum experience the player can have
	 */
	double MAXIMUM_EXP = 200000000;
}
