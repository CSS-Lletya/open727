package com.rs.game.item;
 
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.rs.game.WorldTile;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
 
public class AutomaticGroundItem {
   
    private static HashMap<Long, AutomaticItem> automatic_items = new HashMap<>();
   
    private static boolean completed_initialisation;
   
    private static final Object[][] ITEMS = {
            { TimeUnit.SECONDS.toMillis(45), new Item(1059), new WorldTile(3097, 3486, 0) }, //leather gloves, edgeville
            { TimeUnit.SECONDS.toMillis(45), new Item(946), new WorldTile(3215, 9625, 0) }, //knife, lumbridge basement
            { TimeUnit.SECONDS.toMillis(45), new Item(1965), new WorldTile(3285, 3175, 0) }, //cabbage, alkharid palace
            { TimeUnit.SECONDS.toMillis(45), new Item(1937), new WorldTile(3302, 3170, 0) }, //jug of water, alkharid palace
            { TimeUnit.SECONDS.toMillis(45), new Item(1061), new WorldTile(3301, 3189, 0) }, //leather boots, alkharid
            { TimeUnit.SECONDS.toMillis(45), new Item(1925), new WorldTile(3307, 3195, 0) }, //bucket, alkharid
            { TimeUnit.SECONDS.toMillis(45), new Item(1422), new WorldTile(3320, 3137, 0) }, //bronze mace, alkharid
            { TimeUnit.SECONDS.toMillis(45), new Item(558), new WorldTile(3206, 3208, 0) }, //mind rune, lumbridge castle
            { TimeUnit.SECONDS.toMillis(45), new Item(1937), new WorldTile(3211, 3212, 0) }, //jug of water, lumbridge kitchen
            { TimeUnit.SECONDS.toMillis(45), new Item(1931), new WorldTile(3209, 3214, 0) }, //empty pot, lumbridge kitchen
            { TimeUnit.SECONDS.toMillis(45), new Item(1923), new WorldTile(3208, 3214, 0) }, //bowl, lumbridge kitchen
            { TimeUnit.SECONDS.toMillis(45), new Item(946), new WorldTile(3205, 3212, 0) }, //knife, lumbridge kitchen
            { TimeUnit.SECONDS.toMillis(45), new Item(1061), new WorldTile(3210, 9615, 0) }, //leather boots, lumbridge basement
            { TimeUnit.SECONDS.toMillis(45), new Item(1061), new WorldTile(3208, 9620, 0) }, //leather boots, lumbridge basement
            { TimeUnit.SECONDS.toMillis(45), new Item(1965), new WorldTile(3217, 9622, 0) }, //cabbage, lumbridge basement
            { TimeUnit.SECONDS.toMillis(45), new Item(1925), new WorldTile(3216, 9625, 0) }, //bucket, lumbridge basement
            { TimeUnit.SECONDS.toMillis(45), new Item(1935), new WorldTile(3211, 9625, 0) }, //jug, lumbridge basement
            { TimeUnit.SECONDS.toMillis(45), new Item(882), new WorldTile(3205, 3227, 0) }, //bronze arrow, lumbridge castle
            { TimeUnit.SECONDS.toMillis(45), new Item(1205), new WorldTile(3213, 3216, 1) }, //bronze dagger, lumbridge castle, 2nd floor
            { TimeUnit.SECONDS.toMillis(45), new Item(1511), new WorldTile(3205, 3224, 2) }, //logs, lumbridge castle, 3rd floor
            { TimeUnit.SECONDS.toMillis(45), new Item(1511), new WorldTile(3205, 3226, 2) }, //logs, lumbridge castle, 3rd floor
            { TimeUnit.SECONDS.toMillis(45), new Item(1511), new WorldTile(3208, 3225, 2) }, //logs, lumbridge castle, 3rd floor
            { TimeUnit.SECONDS.toMillis(45), new Item(1511), new WorldTile(3209, 3224, 2) }, //logs, lumbridge castle, 3rd floor
            { TimeUnit.SECONDS.toMillis(45), new Item(1203), new WorldTile(3248, 3245, 0) }, //iron dagger, goblin hut
            { TimeUnit.SECONDS.toMillis(45), new Item(1944), new WorldTile(3229, 3299, 0) }, //egg, lumbridge chickens
            { TimeUnit.SECONDS.toMillis(45), new Item(1925), new WorldTile(3225, 3294, 0) }, //bucket, lumbridge chickens
            // Jug
            { TimeUnit.SECONDS.toMillis(45), new Item(1935), new WorldTile(3272, 3409, 0) }, //
            // Leather body
            { TimeUnit.SECONDS.toMillis(45), new Item(1129), new WorldTile(3244, 3398, 0) }, //
            // Logs
            { TimeUnit.SECONDS.toMillis(45), new Item(1511), new WorldTile(3243, 3397, 0) }, //
            // Logs
            { TimeUnit.SECONDS.toMillis(45), new Item(1511), new WorldTile(3243, 3395, 0) }, //
            // Empty pot
            { TimeUnit.SECONDS.toMillis(45), new Item(1931), new WorldTile(3232, 3399, 0) }, //
            // Bronze pickaxe
            { TimeUnit.SECONDS.toMillis(45), new Item(1265), new WorldTile(3081, 3429, 0) }, //
            // Empty pot
            { TimeUnit.SECONDS.toMillis(45), new Item(1931), new WorldTile(3074, 3431, 0) }, //
            // Beer
            { TimeUnit.SECONDS.toMillis(45), new Item(1917), new WorldTile(3077, 3439, 0) }, //
            // Cooked meat
            { TimeUnit.SECONDS.toMillis(45), new Item(2142), new WorldTile(3077, 3441, 0) }, //
            // Cooked meat
            { TimeUnit.SECONDS.toMillis(45), new Item(2142), new WorldTile(3080, 3443, 0) }, //
            // Beer
            { TimeUnit.SECONDS.toMillis(45), new Item(1917), new WorldTile(3080, 3441, 0) }, //
            // Beer
            { TimeUnit.SECONDS.toMillis(45), new Item(1917), new WorldTile(3077, 3443, 0) }, //
            // Monk's robe
            { TimeUnit.SECONDS.toMillis(45), new Item(544), new WorldTile(3059, 3488, 1) }, //
            // Monk's robe
            { TimeUnit.SECONDS.toMillis(45), new Item(542), new WorldTile(3059, 3487, 1) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3093, 9879, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3093, 9884, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3098, 9886, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 4), new WorldTile(3088, 9898, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995), new WorldTile(3088, 9899, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3097, 9901, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3094, 9906, 0) }, //
            // Bronze arrow
            { TimeUnit.SECONDS.toMillis(45), new Item(882, 3), new WorldTile(3130, 9903, 0) }, //
            // Bronze arrow
            { TimeUnit.SECONDS.toMillis(45), new Item(882), new WorldTile(3135, 9916, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3143, 9878, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3141, 9879, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3142, 9880, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3138, 9880, 0) }, //
            // Brass necklace
            { TimeUnit.SECONDS.toMillis(45), new Item(1009), new WorldTile(3122, 9881, 0) }, //
            // Brass key
            { TimeUnit.SECONDS.toMillis(45), new Item(983), new WorldTile(3132, 9862, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3110, 9825, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3109, 9822, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3107, 9823, 0) }, //
            // Bones
            { TimeUnit.SECONDS.toMillis(45), new Item(526), new WorldTile(3102, 9826, 0) }, //
            // Bronze arrow
            { TimeUnit.SECONDS.toMillis(45), new Item(882, 2), new WorldTile(2944, 3332, 0) }, //
            // Bronze pickaxe
            { TimeUnit.SECONDS.toMillis(45), new Item(1265), new WorldTile(3009, 3342, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995), new WorldTile(3003, 9801, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995), new WorldTile(3001, 9798, 0) }, //
            // Bronze chainbody
            { TimeUnit.SECONDS.toMillis(45), new Item(1103), new WorldTile(2985, 9817, 0) }, //
            // Pie dish
            { TimeUnit.SECONDS.toMillis(45), new Item(2313), new WorldTile(2993, 9847, 0) }, //
            // Spade
            { TimeUnit.SECONDS.toMillis(45), new Item(952), new WorldTile(2981, 3370, 0) }, //
            // Bucket
            { TimeUnit.SECONDS.toMillis(45), new Item(1925), new WorldTile(2958, 3510, 0) }, //
            // Wine of zamorak
            { TimeUnit.SECONDS.toMillis(22), new Item(245), new WorldTile(2946, 3473, 0) }, //
            // Skull
            { TimeUnit.SECONDS.toMillis(45), new Item(964), new WorldTile(2977, 3529, 0) }, //
            // Skull
            { TimeUnit.SECONDS.toMillis(45), new Item(964), new WorldTile(2978, 3531, 0) }, //
            // Body rune
            { TimeUnit.SECONDS.toMillis(45), new Item(559), new WorldTile(3193, 3496, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 3), new WorldTile(3228, 3504, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 4), new WorldTile(3232, 3500, 0) }, //
            // Pie dish
            { TimeUnit.SECONDS.toMillis(45), new Item(2313), new WorldTile(3222, 3494, 0) }, //
            // Bucket
            { TimeUnit.SECONDS.toMillis(45), new Item(1925), new WorldTile(3221, 3497, 1) }, //
            // Bucket
            { TimeUnit.SECONDS.toMillis(45), new Item(1925), new WorldTile(3222, 3491, 1) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 3), new WorldTile(3195, 9834, 0) }, //
            // Gold bar
            { TimeUnit.SECONDS.toMillis(45), new Item(2357), new WorldTile(3192, 9822, 0) }, //
            // Ruby ring
            { TimeUnit.SECONDS.toMillis(45), new Item(1641), new WorldTile(3196, 9822, 0) }, //
            // Gold ore
            { TimeUnit.SECONDS.toMillis(45), new Item(444), new WorldTile(3195, 9821, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 4), new WorldTile(3195, 9820, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 25), new WorldTile(3191, 9821, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 25), new WorldTile(3190, 9819, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 25), new WorldTile(3189, 9819, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 25), new WorldTile(3188, 9819, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995, 25), new WorldTile(3188, 9820, 0) }, //
            // Garlic
            { TimeUnit.SECONDS.toMillis(45), new Item(1550), new WorldTile(3101, 3270, 1) }, //
            // Gold necklace
            { TimeUnit.SECONDS.toMillis(45), new Item(1654), new WorldTile(3192, 9821, 0) }, //
            // Brass necklace
            { TimeUnit.SECONDS.toMillis(45), new Item(1009), new WorldTile(3191, 9820, 0) }, //
            // Rope
            { TimeUnit.SECONDS.toMillis(45), new Item(954), new WorldTile(2904, 3152, 0) }, //
            // Knife
            { TimeUnit.SECONDS.toMillis(45), new Item(946), new WorldTile(2903, 3148, 0) }, //
            // Jug
            { TimeUnit.SECONDS.toMillis(45), new Item(1935), new WorldTile(2905, 3146, 0) }, //
            // Banana
            { TimeUnit.SECONDS.toMillis(45), new Item(1963), new WorldTile(2907, 3146, 0) }, //
            // Red spiders' eggs
            { TimeUnit.SECONDS.toMillis(45), new Item(223), new WorldTile(2832, 9586, 0) }, //
            // Fire rune
            { TimeUnit.SECONDS.toMillis(45), new Item(554), new WorldTile(2836, 9566, 0) }, //
            // Fire rune
            { TimeUnit.SECONDS.toMillis(45), new Item(554), new WorldTile(2833, 9561, 0) }, //
            // Fire rune
            { TimeUnit.SECONDS.toMillis(45), new Item(554), new WorldTile(2838, 9552, 0) }, //
            // Fire rune
            { TimeUnit.SECONDS.toMillis(45), new Item(554), new WorldTile(2842, 9552, 0) }, //
            // Fire rune
            { TimeUnit.SECONDS.toMillis(45), new Item(554), new WorldTile(2843, 9560, 0) }, //
            // Plank
            { TimeUnit.SECONDS.toMillis(45), new Item(960), new WorldTile(2851, 3239, 0) }, //
            // Plank
            { TimeUnit.SECONDS.toMillis(45), new Item(960), new WorldTile(2847, 3238, 0) }, //
            // Plank
            { TimeUnit.SECONDS.toMillis(45), new Item(960), new WorldTile(2847, 3232, 0) }, //
            // Plank
            { TimeUnit.SECONDS.toMillis(45), new Item(960), new WorldTile(2857, 3236, 0) }, //
            // Plank
            { TimeUnit.SECONDS.toMillis(45), new Item(960), new WorldTile(2856, 3231, 0) }, //
            // Cheese
            { TimeUnit.SECONDS.toMillis(45), new Item(1985), new WorldTile(3082, 3262, 0) }, //
            // Tomato
            { TimeUnit.SECONDS.toMillis(45), new Item(1982), new WorldTile(3083, 3262, 0) }, //
            // Logs
            { TimeUnit.SECONDS.toMillis(45), new Item(1511), new WorldTile(3087, 3266, 0) }, //
            // Logs
            { TimeUnit.SECONDS.toMillis(45), new Item(1511), new WorldTile(3087, 3265, 0) }, //
            // Rubber tube
            { TimeUnit.SECONDS.toMillis(45), new Item(276), new WorldTile(3111, 3367, 0) }, //
            // Poison
            { TimeUnit.SECONDS.toMillis(45), new Item(273), new WorldTile(3098, 3366, 0) }, //
            // Spade
            { TimeUnit.SECONDS.toMillis(45), new Item(952), new WorldTile(3120, 3361, 0) }, //
            // Bucket
            { TimeUnit.SECONDS.toMillis(45), new Item(1925), new WorldTile(3121, 3362, 0) }, //
            // Bronze helm
            { TimeUnit.SECONDS.toMillis(45), new Item(1139), new WorldTile(3122, 3363, 0) }, //
            // Shears
            { TimeUnit.SECONDS.toMillis(45), new Item(1735), new WorldTile(3126, 3359, 0) }, //
            // Fish food
            { TimeUnit.SECONDS.toMillis(45), new Item(272), new WorldTile(3108, 3356, 1) }, //
            // Tinderbox
            { TimeUnit.SECONDS.toMillis(45), new Item(590), new WorldTile(3112, 3369, 2) }, //
            // White apron
            { TimeUnit.SECONDS.toMillis(45), new Item(7957), new WorldTile(3016, 3229, 0) }, //
            // White apron
            { TimeUnit.SECONDS.toMillis(45), new Item(1005), new WorldTile(3009, 3204, 0) }, //
            // Banana
            { TimeUnit.SECONDS.toMillis(45), new Item(1963), new WorldTile(3009, 3207, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995), new WorldTile(2988, 9584, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995), new WorldTile(2987, 9583, 0) }, //
            // Coins
            { TimeUnit.SECONDS.toMillis(45), new Item(995), new WorldTile(2988, 9582, 0) }, //
            // Brass key
            { TimeUnit.SECONDS.toMillis(45), new Item(983), new WorldTile(3132, 9862, 0) }, //
            // Bronze pickaxe
            { TimeUnit.SECONDS.toMillis(45), new Item(1265), new WorldTile(3229, 3223, 2) }, //
            // Bronze pickaxe
            { TimeUnit.SECONDS.toMillis(45), new Item(1265), new WorldTile(3229, 3214, 2) },

            { TimeUnit.SECONDS.toMillis(45), new Item(952), new WorldTile(3571, 3311, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(88), new WorldTile(2657, 9874, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(1409), new WorldTile(2657, 9875, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(4024), new WorldTile(2803, 9146, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2905, 329, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2905, 3294, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2908, 3294, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2909, 3291, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2907, 3287, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2909, 3286, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2912, 3283, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2909, 3282, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(231), new WorldTile(2912, 3279, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(1987), new WorldTile(3142, 3447, 2) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(1987), new WorldTile(3144, 3450, 2) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(1985), new WorldTile(3144, 3449, 2) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(1052), new WorldTile(2731, 3379, 2) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(563), new WorldTile(3231, 3842, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(561), new WorldTile(3230, 3841, 0) }, //
            
            { TimeUnit.SECONDS.toMillis(45), new Item(239), new WorldTile(3215, 3825, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(239), new WorldTile(3224, 3834, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(239), new WorldTile(3208, 3852, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(239), new WorldTile(3194, 3849, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(239), new WorldTile(3186, 3835, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(223), new WorldTile(3165, 9881, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(223), new WorldTile(3177, 9880, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(223), new WorldTile(3186, 9886, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(223), new WorldTile(3184, 9894, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(223), new WorldTile(3174, 9900, 0) }, //
            { TimeUnit.SECONDS.toMillis(45), new Item(1735), new WorldTile(3192, 3272, 0) }, //
            
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2419, 3508, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2417, 3512, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2414, 3510, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2410, 3512, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2408, 3515, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2410, 3519, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2415, 3517, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2417, 3515, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2421, 3519, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2429, 3511, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TOAD), new WorldTile(2425, 3506, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TAR), new WorldTile(2412, 3511, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TAR), new WorldTile(2409, 3514, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TAR), new WorldTile(2413, 3519, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TAR), new WorldTile(2418, 3516, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TAR), new WorldTile(2423, 3517, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TAR), new WorldTile(2425, 3514, 0) },
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.SWAMP_TAR), new WorldTile(2427, 3508, 0) },
            
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.KNIFE), new WorldTile(3176, 3957, 0) },
            
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.BLUE_DRAGON_SCALE), new WorldTile(2900, 9807, 0) }, // blue d scales
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.BLUE_DRAGON_SCALE), new WorldTile(2905, 9809, 0) }, // blue d scales
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.BLUE_DRAGON_SCALE), new WorldTile(2912, 9807, 0) }, // blue d scales
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.BLUE_DRAGON_SCALE), new WorldTile(2906, 9801, 0) }, // blue d scales
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.BLUE_DRAGON_SCALE), new WorldTile(2906, 9795, 0) }, // blue d scales
            { TimeUnit.SECONDS.toMillis(45), new Item(ItemNames.BLUE_DRAGON_SCALE), new WorldTile(2892, 9796, 0) }, // blue d scales
    };
    
   
    public static void initialize(){
        for (Object[] args : ITEMS){
            Item item = (Item) args[1];
            WorldTile location = (WorldTile) args[2];
            AutomaticItem value = new AutomaticItem();
            value.item = item;
            value.location = location;
            value.active = true;
            value.maxDelay = (long) args[0];
            automatic_items.put(location.getFixedUniqueId() + item.getFixedUniqueId(), value);
            spawn(value);
        }
        Logger.log("Ground Item Manager", "Loaded " + ITEMS.length + " Ground Item spawns.");
        completed_initialisation = true;
    }
   
    public static void processGameTick(){
        if (!completed_initialisation)
            return;
        for (Object[] args : ITEMS){
            Item item = (Item) args[1];
            WorldTile location = (WorldTile) args[2];
            AutomaticItem automatic_item = automatic_items.get(location.getFixedUniqueId() + item.getFixedUniqueId());
            if (!automatic_item.active){
                if (automatic_item.delay == 0){
                    automatic_item.delay = Utils.currentTimeMillis() + automatic_item.maxDelay;
                    continue;
                }
                else if (automatic_item.delay < Utils.currentTimeMillis()){
                    automatic_item.delay = 0;
                    automatic_item.active = true;
                    spawn(automatic_item);
                }
            }
        }
    }
   
    private static void spawn(AutomaticItem value) {
       // System.out.println(String.format("Spawned [uid=%s, itemId=%s, locationX=%s, locationY=%s, respawnTime=%s]", value.location.getFixedUniqueId() + value.item.getFixedUniqueId(), value.item.getId(), value.location.getX(), value.location.getY(), value.maxDelay));
      //  World.addGroundItem(value.item, value.location, null, false, 1, true, false, 1337);
      //  World.updateGroundItem(value.item, value.location, null, -1, 0);
    	FloorItem.createGroundItem(value.item, value.location);
    }
 
    public static void pickup(WorldTile location, Item item){
        AutomaticItem automatic_item = automatic_items.get(location.getFixedUniqueId() + item.getFixedUniqueId());
        if (automatic_item != null) {
        automatic_item.active = false;
       // despawn(automatic_item);
        }
    }
   
    @SuppressWarnings("serial")
	private static class AutomaticItem implements Serializable {

        private long delay, maxDelay;
       
        private boolean active;
       
        private WorldTile location;
       
        private Item item;
    }
 
}