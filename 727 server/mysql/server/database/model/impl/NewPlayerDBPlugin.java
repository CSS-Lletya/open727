package server.database.model.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import server.database.GameDatabase;
import server.database.model.DatabaseModel;
import server.database.pooled.DatabaseConnection;
import server.database.util.DatabaseUtils;

public class NewPlayerDBPlugin extends DatabaseModel {
	
	private String username;
	
	public NewPlayerDBPlugin(String username) {
		this.username = username;
	}
	
	@Override
	public String getInsertStatement() {
		return INSERT_STATEMENT;
	}
	
	@Override
	public String getSelectStatement() {
		return null;
	}
	
	@Override
	public void insert() throws SQLException {
		DatabaseConnection databaseConnection = GameDatabase.getWebsiteDatabaseManager().getConnection(NewPlayerDBPlugin.class.getSimpleName());
		if (databaseConnection == null) {
			GameDatabase.getWebsiteDatabaseManager().reportUnavailableConnection();
			return;
		}
		prepareStatement(databaseConnection, StatementType.INSERT);
		if (insertStatement == null) {
			System.err.println("Invalid declared 'insertStatement' - " + getClass().getName());
			return;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss aa");
		Date date = new Date();
		insertStatement.clearParameters();
		insertStatement.setInt(1, 0);
		insertStatement.setString(2, username);
		insertStatement.setString(3, dateFormat.format(date));
		insertStatement.executeUpdate();
	}
	
	@Override
	public void update() throws SQLException {
		/** Empty **/
	}
	
	@Override
	public void delete() throws SQLException {
		/** Empty **/
	}
	
	@Override
	public void select() throws SQLException {
		/** Empty **/
	}
	
	private static final String INSERT_STATEMENT;
	
	static {
		INSERT_STATEMENT = DatabaseUtils.generateInsertStatement("starter", "id", "username", "date");
	}
	
	@Override
	public String getDeleteStatement() {
		return null;
	}
	
	@Override
	public String getUpdateStatement() {
		return null;
	}
}