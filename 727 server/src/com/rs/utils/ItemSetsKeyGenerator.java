package com.rs.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ItemSetsKeyGenerator {

	private final static AtomicInteger nextKey = new AtomicInteger(500); // After
																			// 400
																			// keys
																			// uses
																			// negative
																			// keys

	public static final int generateKey() {
		int key = nextKey.getAndDecrement();
		if (key > 0 && key <= 100)
			nextKey.set(-1); // starts at negative
		return key;
	}
}
