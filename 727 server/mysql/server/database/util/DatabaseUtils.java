package server.database.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.rs.game.player.Player;

/**
 * Utilities for Database handling.
 * 
 * @author Pb600
 * @version 1.0 17/03/13
 */
public class DatabaseUtils {

	/***
	 * Generate a MySQL insert script based on table name and its columns names.
	 * 
	 * @param tableName: Name of table.
	 * @param tableColums: Array of columns names.
	 * @return: An insert statement script.
	 */
	public static String generateInsertStatement(String tableName, String... tableColums) {
		StringBuffer sb = new StringBuffer("INSERT INTO `" + tableName + "`");
		sb.append("(");
		for (String column : tableColums) {
			sb.append('`' + column + "`, ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		sb.append("VALUES");
		sb.append("(");
		for (int i = 0; i < tableColums.length; i++) {
			sb.append("?");
			if (i + 1 < tableColums.length)
				sb.append(", ");
		}
		sb.append(");");
		return sb.toString();
	}
	
	public static String generateUpdateStatement(String tableName, Player player, String... tableColums) {
		StringBuffer sb = new StringBuffer("update adventurelog where accountName = ? `" + tableName + "`");
		sb.append("(");
		for (String column : tableColums) {
			sb.append('`' + column + "`, ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		sb.append("VALUES");
		sb.append("(");
		for (int i = 0; i < tableColums.length; i++) {
			sb.append("?");
			if (i + 1 < tableColums.length)
				sb.append(", ");
		}
		sb.append(");");
		return sb.toString();
	}

	public static String generateGERemovalStatement(String tableName, String... tableColums) {
		StringBuffer sb = new StringBuffer("delete from `" + tableName + "` where hash = ?");
		sb.append("(");
		for (String column : tableColums) {
			sb.append('`' + column + "`, ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		sb.append("VALUES");
		sb.append("(");
		for (int i = 0; i < tableColums.length; i++) {
			sb.append("?");
			if (i + 1 < tableColums.length)
				sb.append(", ");
		}
		sb.append(");");
		return sb.toString();
	}
	
	public static String getDataStrucutre(Iterator<?> iterator, boolean updateType) {
		StringBuffer sb = new StringBuffer();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			sb.append('`' + object.toString() + (updateType ? "` = ?, " : "`, "));
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	public static String getUpdateStrucutre(String[] keys) {
		StringBuffer sb = new StringBuffer();
		for (String string : keys) {
			if (string != null) {
				sb.append('`' + string + "` = ?, ");
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	public static String getSelectStructure(ArrayList<String> keys) {
		StringBuffer sb = new StringBuffer();
		for (String string : keys) {
			if (string != null) {
				sb.append('`' + string + "`, ");
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	public static String getSelectStructure(String[] keys) {
		StringBuffer sb = new StringBuffer();
		for (String string : keys) {
			if (string != null) {
				sb.append('`' + string + "`, ");
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}
}
