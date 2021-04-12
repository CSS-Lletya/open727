package com.rs.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConvertCommands {

	public static ArrayList<String> lines = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			rewrite();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void rewrite() throws IOException {
		BufferedReader reader;
		File real = new File("src/com/rs/game/player/content/Commands.java");
		String line;
		reader = new BufferedReader(new FileReader(real));
		while ((line = reader.readLine()) != null) {
			line = line.replace("if (cmd[0].equals(", "case ").replace(")) {", ":").replace("} else case", "\n case");

			if (line.contains("case")) {
				System.out.println(line);
				System.out.println("			return true; \n");
			}
		}
	}

}
