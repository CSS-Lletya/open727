package com.rs.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 1, 2014
 */
public abstract class GsonLoader<T> {

	/**
	 * Loads up all of the data
	 */
	public abstract void initialize();

	/**
	 * Gets the location of the file to load from
	 *
	 * @return
	 */
	public abstract String getFileLocation();

	/**
	 * Populates a list with all of the data in the {@link #getFileLocation()}
	 *
	 * @return The list with data
	 */
	protected abstract List<T> load();

	/**
	 * Generates a list, if there are no elements in {@link #load()}, it will be
	 * null, so this will make sure there is no null return type from
	 * {@link #load()}
	 */
	public synchronized List<T> generateList() {
		List<T> list = load();
		if (list == null) {
			list = new ArrayList<>();
		}
		return Collections.synchronizedList(list);
	}

	/**
	 * Saves the data to the file
	 *
	 * @param data
	 *            The list to save
	 */
	public void save(List<T> data) {
		try {
			File file = new File(getFileLocation());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileWriter fw = new FileWriter(getFileLocation());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(gson.toJson(data));
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The gson instance
	 */
	protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
}
