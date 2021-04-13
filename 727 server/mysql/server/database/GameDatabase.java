package server.database;

import server.database.pooled.ConnectionSettings;
import server.database.pooled.Database;

/**
 * Game database connections
 * 
 * @author Pb600
 *
 */
public class GameDatabase {

	public static Database actionManager, websiteDatabaseManager, forumManager;
	
	public static void initializeWebsiteDatabases() {
		websiteDatabaseManager = new Database("Website manager", new ConnectionSettings("localhost", 3306, "open727", "root", "", true, 50, 1, 0, 3, 60000, true));
		websiteDatabaseManager.initializeSystem();
	}

	public static Database getWebsiteDatabaseManager() {
		return websiteDatabaseManager;
	}

	public static Database getActionManager() {
		return actionManager;
	}
}