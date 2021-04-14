package com.rs.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Oct 12, 2013
 */
public class FileClassLoader {

	/**
	 * Gets all of the classes in a directory
	 *
	 * @param directory
	 *            The directory to iterate through
	 * @return The list of classes
	 */
	public static List<Object> getClassesInDirectory(String directory) {
		List<Object> classes = new ArrayList<Object>();
		for (File file : new File("./bin/" + directory.replace(".", "/")).listFiles()) {
			if (file.getName().contains("$") || file.getName().contains("desktop.ini")) {
				continue;
			}
			try {
				Object objectEvent = (Class.forName(directory + "." + file.getName().replace(".class", "")).newInstance());
				classes.add(objectEvent);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classes;
	}

}
