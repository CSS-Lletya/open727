package server.database.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import server.database.pooled.DatabaseConnection;

/**
 * Server database model structure
 * to handle DAO Operation requests.
 * 
 * @author Pb600
 * @version 2.0 05/11/13
 */
public abstract class DatabaseModel implements Modelable {

	protected PreparedStatement insertStatement;
	protected PreparedStatement deleteStatement;
	protected PreparedStatement updateStatement;
	protected PreparedStatement selectStatement;

	private DatabaseConnection databaseConnection;
	private ResultSet resultSet;

	/**
	 * Constructor
	 */
	protected DatabaseModel() {
		setDefault();
	}

	private void setDefault() {
		insertStatement = null;
		deleteStatement = null;
		updateStatement = null;
		selectStatement = null;
		databaseConnection = null;
	}

	protected void reserveConnection(DatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

	protected void registerResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * implement AutoCloseable interface, for cleaning statement on destruct object
	 */
	@Override
	public void close() {
		if (databaseConnection != null) {
			databaseConnection.close(selectStatement);
			databaseConnection.close(deleteStatement);
			databaseConnection.close(insertStatement);
			databaseConnection.close(updateStatement);
			databaseConnection.close(resultSet);
			databaseConnection.release();
		}
		setDefault();
	}

	public static enum StatementType {
		SELECT, INSERT, DELETE, UPDATE;
	}

	public ResultSet getResultset() {
		return resultSet;
	}

	protected void prepareStatement(DatabaseConnection databaseConnection, StatementType statementType) {
		reserveConnection(databaseConnection);
		if (statementType == StatementType.INSERT) {
			if (getInsertStatement() != null)
				insertStatement = databaseConnection.prepareStatement(getInsertStatement());
		} else if (statementType == StatementType.DELETE) {
			if (getDeleteStatement() != null)
				deleteStatement = databaseConnection.prepareStatement(getDeleteStatement());
		} else if (statementType == StatementType.SELECT) {
			if (getSelectStatement() != null)
				selectStatement = databaseConnection.prepareStatement(getSelectStatement());
		} else if (statementType == StatementType.UPDATE) {
			if (getUpdateStatement() != null)
				updateStatement = databaseConnection.prepareStatement(getUpdateStatement());
		}
	}

	public abstract String getInsertStatement();

	public abstract String getDeleteStatement();

	public abstract String getUpdateStatement();

	public abstract String getSelectStatement();
}
