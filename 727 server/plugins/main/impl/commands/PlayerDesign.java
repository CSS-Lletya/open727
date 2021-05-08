package main.impl.commands;

import com.rs.cache.loaders.ClientScriptMap;
import com.rs.game.player.Player;

/**
 * Created by EradicationX
 */
public class PlayerDesign {

    /**
     * The players looks.
     */
    private final short[] looks;

    /**
     * The players body colors.
     */
    private final short[] colors;

    /**
     * The players skin colors array.
     */
    private static final short[] SKIN_COLORS = { 9, 8, 7, 0, 1, 2, 3, 4, 5, 6, 10, 11 };

    /**
     * The player's hair color array.
     */
    private static final short[] HAIR_COLORS = { 20, 19, 10, 18, 4, 5, 15, 7, 0, 6, 21, 9, 22, 17, 8, 16, 24, 11, 23, 3, 2, 1, 14, 13, 12 };

    /**
     * The player's torso and leg colors array.
     */
    private static final short[] TORSO_AND_LEG_COLORS = { 32, 101, 48, 56, 165, 103, 167, 106, 54, 198, 199, 200, 225, 35, 39, 53, 42, 46, 29, 91, 57, 90, 34, 102, 104, 105, 107, 173, 137, 201, 204, 211, 197, 108, 217, 220, 221, 226, 227, 215, 222, 166, 212, 174, 175, 169, 144, 135, 136, 133, 123, 119, 192, 194, 117, 115, 111, 141, 45, 49, 84, 77, 118, 88, 85, 138, 51, 92, 112, 145, 179, 143, 149, 151, 153, 44, 154, 155, 86, 89, 72, 66, 33, 206, 109, 110, 114, 116, 184, 170, 120, 113, 150, 205, 210, 207, 209, 193, 152, 156, 183, 161, 159, 160, 73, 75, 181, 185, 208, 74, 36, 37, 43, 50, 58, 55, 139, 148, 147, 64, 69, 70, 71, 68, 93, 94, 95, 124, 182, 96, 97, 219, 63, 228, 79, 82, 98, 99, 100, 125, 126, 127, 40, 128, 129, 188, 130, 131, 186, 132, 164, 157, 180, 187, 31, 162, 168, 52, 163,
            158, 196, 59, 60, 87, 78, 61, 76, 80, 171, 172, 176, 177, 178, 38, 41, 47, 62, 65, 67, 81, 83, 121, 122, 134, 140, 142, 146, 189, 190, 191, 195, 202, 203, 213, 214, 216, 218, 223, 224 };

    /**
     * The player's boot colors array.
     */
    private static final short[] BOOT_COLORS = { 55, 54, 14, 120, 194, 53, 111, 154, 0, 1, 2, 3, 4, 5, 9, 78, 25, 33, 142, 80, 144, 83, 31, 175, 176, 177, 12, 16, 30, 19, 23, 6, 68, 34, 67, 11, 79, 81, 82, 84, 150, 114, 178, 181, 188, 174, 85, 194, 197, 198, 203, 204, 192, 199, 143, 189, 151, 152, 146, 121, 112, 113, 110, 100, 96, 169, 171, 94, 92, 88, 118, 22, 26, 61, 95, 65, 62, 115, 28, 69, 89, 122, 156, 126, 128, 130, 21, 131, 132, 63, 66, 49, 43, 10, 183, 86, 87, 91, 93, 161, 147, 97, 90, 127, 182, 187, 184, 186, 170, 129, 133, 160, 138, 136, 137, 50, 52, 158, 162, 185, 51, 13, 20, 27, 35, 32, 116, 125, 124, 41, 46, 47, 48, 45, 70, 71, 72, 101, 159, 73, 74, 196, 40, 205, 56, 59, 75, 76, 77, 102, 103, 104, 17, 105, 106, 165, 107, 108, 163, 109, 141, 134, 157, 164, 8, 139, 145, 29,
            140, 135, 173, 36, 37, 64, 38, 57, 148, 149, 153, 155, 15, 18, 24, 39, 42, 44, 58, 60, 98, 99, 117, 119, 123, 166, 167, 168, 172, 179, 180, 190, 191, 193, 195, 200, 201 };

    /**
     * The player's male hair styles array.
     */
    private static final short[] MALE_HAIR_STYLES = { 5, 6, 93, 96, 92, 268, 265, 264, 267, 315, 94, 263, 312, 313, 311, 314, 261, 310, 1, 0, 97, 95, 262, 316, 309, 3, 91, 4 };

    /**
     * The player's male torso array.
     */
    private static final short[][] MALE_TORSOS = { { 457, 588, 364 }, { 445, 0, 366 }, { 459, 591, 367 }, { 460, 592, 368 }, { 461, 593, 369 }, { 462, 594, 370 }, { 452, 0, 371 }, { 463, 596, 372 }, { 464, 597, 373 }, { 446, 0, 374 }, { 465, 599, 375 }, { 466, 600, 376 }, { 467, 601, 377 }, { 451, 0, 378 }, { 468, 603, 379 }, { 453, 0, 380 }, { 454, 0, 381 }, { 455, 0, 382 }, { 469, 607, 383 }, { 470, 608, 384 }, { 450, 0, 385 }, { 458, 589, 365 }, { 447, 0, 386 }, { 448, 0, 387 }, { 449, 0, 388 }, { 471, 613, 389 }, { 443, 0, 390 }, { 472, 615, 391 }, { 473, 616, 392 }, { 444, 0, 393 }, { 474, 618, 394 }, { 456, 0, 9 } };

    /**
     * The player's male legs torso array.
     */
    private static final short[] MALE_LEGS = { 620, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641, 621, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651 };

    /**
     * The player's male boots array.
     */
    private static final short[] MALE_BOOTS = { 427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 42, 43 };

    /**
     * The player's male beards array.
     */
    private static final short[] MALE_BEARDS = { 14, 13, 98, 308, 305, 307, 10, 15, 16, 100, 12, 11, 102, 306, 99, 101, 104, 17 };

    /**
     * The player's female hair styles.
     */
    private static final short[] FEMALE_HAIR_STYLES = { 141, 361, 272, 273, 359, 274, 353, 277, 280, 360, 356, 269, 358, 270, 275, 357, 145, 271, 354, 355, 45, 52, 49, 47, 48, 46, 143, 362, 144, 279, 142, 146, 278, 135 };

    /**
     * The player's female torso array.
     */
    private static final short[][] FEMALE_TORSOS = { { 565, 395, 507 }, { 567, 397, 509 }, { 568, 398, 510 }, { 569, 399, 511 }, { 570, 400, 512 }, { 571, 401, 513 }, { 561, -1, 514 }, { 572, 403, 515 }, { 573, 404, 516 }, { 574, 405, 517 }, { 575, 406, 518 }, { 576, 407, 519 }, { 577, 408, 520 }, { 560, -1, 521 }, { 578, 410, 522 }, { 562, -1, 523 }, { 563, -1, 524 }, { 564, -1, 525 }, { 579, 414, 526 }, { 559, -1, 527 }, { 580, 416, 528 }, { 566, 396, 508 }, { 581, 417, 529 }, { 582, 418, 530 }, { 557, -1, 531 }, { 583, 420, 532 }, { 584, 421, 533 }, { 585, 422, 534 }, { 586, 423, 535 }, { 556, -1, 536 }, { 587, 425, 537 }, { 558, -1, 538 } };

    /**
     * The player's female legs array.
     */
    private static final short[] FEMALE_LEGS = { 475, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 476, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506 };

    /**
     * The player's female boots array.
     */
    private static final short[] FEMALE_BOOTS = { 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555, 79, 80 };

    /**
     * These hold all of the male and female presets.
     */
    private static final PlayerDesign[] MALE_SET1 = {
            new PlayerDesign(
                    new short[] { -1, -1, 473, 616, 392, 648, 441, -1 },
                    new short[] { -1, 44, 220, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(
                    new short[] { -1, -1, 443, -1, 390, 646, 440, -1 },
                    new short[] { -1, 44, 220, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(
                    new short[] { -1, -1, 474, 618, 394, 650, 441, -1 },
                    new short[] { -1, 44, 220, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] MALE_SET2 = {
            new PlayerDesign(new short[] { -1, -1, 453, -1, 380, 636, 429, -1 },
                    new short[] { -1, 204, 201, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 454, -1, 381, 637, 429, -1 },
                    new short[] { -1, 204, 201, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 455, -1, 382, 638, 429, -1 },
                    new short[] { -1, 204, 201, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] MALE_SET3 = {
            new PlayerDesign(new short[] { -1, -1, 447, -1, 386, 642, 429, -1 },
                    new short[] { -1, 132, 132, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 448, -1, 387, 643, 429, -1 },
                    new short[] { -1, 132, 132, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 449, -1, 388, 644, 429, -1 },
                    new short[] { -1, 132, 132, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] MALE_SET4 = {
            new PlayerDesign(new short[] { -1, -1, 469, 607, 383, 639, 431, -1 },
                    new short[] { -1, 156, 149, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 470, 608, 384, 640, 429, -1 },
                    new short[] { -1, 156, 149, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 450, -1, 385, 641, 429, -1 },
                    new short[] { -1, 156, 149, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] MALE_SET5 = {
            new PlayerDesign(new short[] { -1, -1, 452, -1, 371, 627, 434, -1 },
                    new short[] { -1, 196, 196, 39, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] MALE_SET6 = {
            new PlayerDesign(new short[] { -1, -1, 461, 593, 369, 625, 432, -1 },
                    new short[] { -1, 84, 77, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] MALE_SET7 = {
            new PlayerDesign(new short[] { -1, -1, 446, -1, 374, 630, 429, -1 },
                    new short[] { -1, 116, 116, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] FEMALE_SET1 = {
            new PlayerDesign(new short[] { -1, -1, 586, 423, 535, 503, 553, -1 },
                    new short[] { -1, 60, 60, 155, 0, 0, 0, 0, 0, 0 }),

            new PlayerDesign(new short[] { -1, -1, 585, 422, 534, 502, 554, -1 },
                    new short[] { -1, 52, 221, 4, 0, 0, 0, 0, 0, 0 }),

            new PlayerDesign(new short[] { -1, -1, 587, 425, 537, 505, 551, -1 },
                    new short[] { -1, 60, 60, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] FEMALE_SET2 = {
            new PlayerDesign(new short[] { -1, -1, 562, -1, 523, 491, 551, -1 },
                    new short[] { -1, 204, 201, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 563, -1, 524, 492, 551, -1 },
                    new short[] { -1, 204, 201, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 564, -1, 525, 493, 551, -1 },
                    new short[] { -1, 204, 201, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] FEMALE_SET3 = {
            new PlayerDesign(new short[] { -1, -1, 581, 417, 529, 497, 551, -1 },
                    new short[] { -1, 132, 132, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 582, 418, 530, 498, 551, -1 },
                    new short[] { -1, 132, 128, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 557, -1, 531, 499, 551, -1 },
                    new short[] { -1, 132, 132, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] FEMALE_SET4 = {
            new PlayerDesign(new short[] { -1, -1, 579, 414, 526, 494, 551, -1 },
                    new short[] { -1, 156, 156, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 559, -1, 527, 495, 551, -1 },
                    new short[] { -1, 156, 149, 4, 0, 0, 0, 0, 0, 0 }),
            new PlayerDesign(new short[] { -1, -1, 580, 416, 528, 496, 552, -1 },
                    new short[] { -1, 156, 149, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] FEMALE_SET5 = {
            new PlayerDesign(new short[] { -1, -1, 561, -1, 514, 482, 545, -1 },
                    new short[] { -1, 196, 196, 39, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] FEMALE_SET6 = {
            new PlayerDesign(new short[] { -1, -1, 570, 400, 512, 480, 543, -1 },
                    new short[] { -1, 84, 77, 4, 0, 0, 0, 0, 0, 0 }) };

    private static final PlayerDesign[] FEMALE_SET7 = {
            new PlayerDesign(new short[] { -1, -1, 574, 405, 517, 485, 548, -1 },
                    new short[] { -1, 116, 116, 4, 0, 0, 0, 0, 0, 0 }) };

    /**
     * Constructs a new {@code PlayerDesign} instance.
     *
     * @param looks
     *            The looks to pass.
     * @param colors
     *            The colors to pass.
     */
    public PlayerDesign(short[] looks, short[] colors) {
        this.looks = looks;
        this.colors = colors;
    }

    /**
     * Opens the player design screen.
     *
     * @param player
     */
    public static void open(Player player) {
        if (player.getEquipment().wearingArmour()) {
            player.getPackets().sendGameMessage("Please remove all equipment first.");
            return;
        }
        player.getTemporaryAttributtes().put("player_design", 0);
        player.getPackets().sendWindowsPane(1028, 0);
        player.getPackets().sendConfig(1158, 352);
        player.getPackets().sendConfig(1363, player.getAppearance().isMale() ? 8249 : 12345);
        player.getPackets().sendAccessMask(player, 0, 11, 1028, 65, 2);
        player.getPackets().sendAccessMask(player, 0, 204, 1028, 132, 2);
        player.getPackets().sendAccessMask(player, 0, 33, 1028, 128, 2);
    }


    public static void handle(Player player, int buttonId, int slot) {
        System.out.println("button: " + buttonId + " slot: " + slot);
        if ((buttonId >= 116) && (buttonId <= 121)) {
//            System.out.println(Integer.valueOf(buttonId - 115));
            player.getTemporaryAttributtes().put("player_design", Integer.valueOf(buttonId - 115));
            return;
        }
        if (buttonId == 137) {
            player.getTemporaryAttributtes().put("player_design", Integer.valueOf(0));
            return;
        }
        if (buttonId == 139) {
            player.getTemporaryAttributtes().put("player_design", Integer.valueOf(1));
            return;
        }
        if (buttonId == 138) {
            player.getTemporaryAttributtes().remove("player_design");
            player.getAppearance().generateAppearenceData();
            player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, 0);
            return;
        }
        Integer screen = (Integer) player.getTemporaryAttributtes().get("player_design");
        System.out.println("238 screen: " + screen);
        if (screen == null)
            return;
        if (screen.intValue() == 0) {
            if (buttonId == 62) {
                changeGender(player, true);
                return;
            }
            if (buttonId == 63) {
                changeGender(player, false);
                return;
            }
            if (buttonId == 65) {
                player.getAppearance().setBodyColor(4, SKIN_COLORS[slot]);
                return;
            }
            Integer style = (Integer) player.getTemporaryAttributtes().get("DESIGN_STYLE");
            if ((buttonId >= 68) && (buttonId <= 74))
                player.getTemporaryAttributtes().put("DESIGN_STYLE", style = Integer.valueOf(buttonId - 67));
            if (style == null)
                return;
            int subIndex = 0;
            if ((buttonId >= 103) && (buttonId <= 105))
                subIndex = buttonId - 103;
            PlayerDesign[] set = player.getAppearance().isMale() ? getMaleSet(style.intValue()) : getFemaleSet(style.intValue());
            player.getAppearance().setLooks(set[subIndex].looks);
            player.getAppearance().copyColors(set[subIndex].colors);
            return;
        }
        if (screen.intValue() == 1) {
            if ((slot < 0) || (slot >= SKIN_COLORS.length))
                return;
            player.getAppearance().setBodyColor(4, SKIN_COLORS[slot]);
            return;
        }
        if (screen.intValue() == 2) {
            if (buttonId == 128) {
                player.getAppearance().setHairStyle(player.getAppearance().isMale() ? MALE_HAIR_STYLES[slot] : FEMALE_HAIR_STYLES[slot]);
            } else if (buttonId == 132)
                player.getAppearance().setBodyColor(0, HAIR_COLORS[slot]);
            return;
        }
        if (screen.intValue() == 3) {
            if (buttonId == 128) {
                if (player.getAppearance().isMale()) {
                    player.getAppearance().setBodyStyle(2, MALE_TORSOS[slot][0]);
                    int arms = MALE_TORSOS[slot][1];
                    player.getAppearance().setBodyStyle(3, (short) (arms == -1 ? 1000 : arms));
                    player.getAppearance().setBodyStyle(4, MALE_TORSOS[slot][2]);
                } else {
                    player.getAppearance().setBodyStyle(2, FEMALE_TORSOS[slot][0]);
                    int arms = FEMALE_TORSOS[slot][1];
                    player.getAppearance().setBodyStyle(3, (short) (arms == -1 ? 1000 : arms));
                    player.getAppearance().setBodyStyle(4, FEMALE_TORSOS[slot][2]);
                }
            } else if (buttonId == 132)
                player.getAppearance().setTopColor(ClientScriptMap.getMap(3282).getIntValue(slot));
            return;
        }
        if (screen.intValue() == 4) {
            if (buttonId == 128) {
                player.getAppearance().setLegsStyle(!player.getAppearance().isMale() ? FEMALE_LEGS[slot] : MALE_LEGS[slot]);
            } else if (buttonId == 132)
                player.getAppearance().setBodyColor(2, TORSO_AND_LEG_COLORS[slot]);
            return;
        }
        if (screen.intValue() == 5) {
            if (buttonId == 128) {
                player.getAppearance().setBodyStyle(6, !player.getAppearance().isMale() ? FEMALE_BOOTS[slot] : MALE_BOOTS[slot]);
            } else if (buttonId == 132)
                player.getAppearance().setBodyColor(3, BOOT_COLORS[slot]);
            return;
        }
        if (screen.intValue() == 6) {
            if (!player.getAppearance().isMale())
                return;
            if (buttonId == 128)
                player.getAppearance().setBodyStyle(1, MALE_BEARDS[slot]);
            else if (buttonId == 132)
                player.getAppearance().setBodyColor(0, HAIR_COLORS[slot]);
            return;
        }
        System.err.println("Invalid character design screen: " + screen);
    }

    private static void changeGender(Player player, boolean male) {
        if (male == player.getAppearance().isMale())
            return;
        player.getPackets().sendConfig(1158, 352);
        if (male) {
            PlayerDesign[] set = getMaleSet(1);
            player.getAppearance().setLooks(set[0].looks);
            player.getAppearance().copyColors(set[0].colors);
            player.getAppearance().setBodyStyle(0, (short) 5);
            player.getAppearance().setBodyStyle(1, (short) 14);
        } else {
            PlayerDesign[] set = getFemaleSet(1);
            player.getAppearance().setLooks(set[0].looks);
            player.getAppearance().copyColors(set[0].colors);
            player.getAppearance().setBodyStyle(0, (byte) 141);
            player.getAppearance().setBodyStyle(1, (short) 57);
        }
        player.getPackets().sendConfig(1363, male ? 8249 : 12345);
        player.getAppearance().setMale(male);
        player.getAppearance().generateAppearenceData();
    }


    /**
     * Gets the default male sets by set id.
     *
     * @param set
     *            the set to use.
     * @return the set design.
     */
    private static PlayerDesign[] getMaleSet(int set) {
        switch (set) {
            case 1:
                return MALE_SET1;
            case 2:
                return MALE_SET2;
            case 3:
                return MALE_SET3;
            case 4:
                return MALE_SET4;
            case 5:
                return MALE_SET5;
            case 6:
                return MALE_SET6;
            case 7:
                return MALE_SET7;
        }
        return null;
    }

    /**
     * Gets the default female set by set id.
     *
     * @param set
     *            The set to use.
     * @return The set.
     */
    private static PlayerDesign[] getFemaleSet(int set) {
        switch (set) {
            case 1:
                return FEMALE_SET1;
            case 2:
                return FEMALE_SET2;
            case 3:
                return FEMALE_SET3;
            case 4:
                return FEMALE_SET4;
            case 5:
                return FEMALE_SET5;
            case 6:
                return FEMALE_SET6;
            case 7:
                return FEMALE_SET7;
        }
        return null;
    }
}