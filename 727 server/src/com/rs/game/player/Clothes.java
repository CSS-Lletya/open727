package com.rs.game.player;

import java.util.HashMap;

public class Clothes {
	
	/*
	 * Male tops
	 * Size: 46
	 */
	public static final int[] ORDINAL_TOPS_1 = { 457, 445, 459, 460, 461, 462, 452, 463, 464, 446, 465, 466, 467, 451, 468, 453, 454, 455, 469, 470, 450, 458, 447, 448, 449, 471, 443, 472, 473, 444, 474, 456, 111, 113, 114, 115, 112, 116, 18, 19, 20, 21, 22, 23, 24, 25 };
	
	/*
	 * Female tops
	 * Size: 43
	 */
	public static final int[] ORDINAL_TOPS_2 = { 565, 567, 568, 569, 570, 571, 561, 572, 573, 574, 575, 576, 577, 560, 578, 562, 563, 564, 579, 559, 580, 566, 581, 582, 557, 583, 584, 585, 586, 556, 587, 558, 153, 155, 156, 157, 154, 158, 56, 57, 58, 59, 60 };
	
	/*
	 * Male arms
	 * Size: 12
	 */
	public static final int[] ORDINAL_ARMS_1 = { 105, 108, 106, 107, 109, 110, 28, 26, 27, 29, 30, 31 };
	
	/*
	 * Female arms
	 * Size: 11
	 */
	public static final int[] ORDINAL_ARMS_2 = { 147, 150, 148, 149, 151, 152, 64, 61, 63, 65, 62 };
	
	/*
	 * Male wrists
	 * Size: 13
	 */
	public static final int[] ORDINAL_WRISTS_1 = { 34, 33, 84, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126 };
	
	/*
	 * Female wrists
	 * Size: 13
	 */
	public static final int[] ORDINAL_WRISTS_2 = { 68, 67, 127, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168 };

	/*
	 * Male legs
	 * Size: 43
	 */
	public static final int[] ORDINAL_LEGGINGS_1 = { 620, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641, 621, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 36, 85, 37, 89, 90, 40, 86, 88, 39, 38, 87 };
	
	/*
	 * Female legs
	 * Size: 48
	 */
	public static final int[] ORDINAL_LEGGINGS_2 = { 475, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 476, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 129, 130, 128, 74, 133, 134, 77, 131, 132, 75, 73, 76, 72, 70, 71, 363 };
	
	/*
	 * Male shoes
	 * Size: 18
	 */
	public static final int[] ORDINAL_SHOES_1 = { 427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 42, 43 };
	
	/*
	 * Female shoes
	 * Size: 19
	 */
	public static final int[] ORDINAL_SHOES_2 = { 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555, 79, 80 };
	
	/*
	 * Male varient arms
	 * Size: 32
	 */
	public static final int[] ORDINAL_VARIENT_ARMS_1 = { 588, 590, 591, 592, 593, 594, 595, 596, 597, 598, 599, 600, 601, 602, 603, 604, 605, 606, 607, 608, 609, 589, 610, 611, 612, 613, 614, 615, 616, 617, 618, 619 };
	
	/*
	 * Female varient arms
	 * Size: 32
	 */
	public static final int[] ORDINAL_VARIENT_ARMS_2 = { 395, 397, 398, 399, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 396, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426 };

	/*
	 * Male varient wrists
	 * Size: 32
	 */
	public static final int[] ORDINAL_VARIENT_WRISTS_1 = { 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 365, 386, 387, 388, 389, 390, 391, 392, 393, 394, 9 };
	
	/*
	 * Female varient wrists
	 * Size: 32
	 */
	public static final int[] ORDINAL_VARIENT_WRISTS_2 = { 507, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 525, 526, 527, 528, 508, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538 };

	public static HashMap<Integer, Integer> MATCHES = getMatches();
	
	private static HashMap<Integer, Integer> getMatches(){
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		map.put(46, 34); //female skirt = male shorts
		
		/*
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 */
		
		
		return map;
	}
	
//	public static int getMatch(int type, int clothing, boolean male){
//		switch (type){
//		case Appearence:
//			if (!male)
//				return clothing;
//			switch (clothing){
//			case 42://male shirt = female plain top
//				return 38;
//			case 43://male stitching = female plain
//				return 38;
//			case 44://male tagged = female torn
//				return 42;
//			case 45://male two toned = female plain
//				return 38;
//			}
//			return clothing;
//		case Appearance.LEGS:
//			if (male){
//				switch (clothing){
//
//				}
//			}else{
//				switch (clothing){
//				case 46://female skirt = male shorts
//					return 34;
//				}
//			}
//			return clothing;
//		}
//		return 1;
//	}
	/*
	 * 
	 
	 plain trou
	 princel
	 shorts
	 ragged
	 tattered
	 torn
	 breeches
	 striped
	 turnu[s
	 flares
	 fine breeches
	 
	 fin skirt
	 frilled skirt
	 layered skirt
	 long narrow skirt
	 ragged skirt
	 tattered skirt
	 short skirt
	 sashed skirt
	 fitted skirt
	 torn trousers
	 long skirt
	 turn ups
	 flares
	 plain trousers
	 skirt
	 shorts
	 
	 
	 
	 
	 */
	public static int getMatch(int clothes){
		Integer match = MATCHES.get(clothes);
		return match == null ? clothes : match;
	}

}
