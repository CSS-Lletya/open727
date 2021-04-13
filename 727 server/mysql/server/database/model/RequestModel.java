package server.database.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import com.rs.game.player.Player;

import javafx.util.Callback;
import server.database.pooled.Database;
import server.database.pooled.DatabaseConnection;

/**
 * Asynchronous database operation request model.
 * 
 * @author Pb600
 * 
 */
public class RequestModel {

	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private QueryType queryType;
	private ResultNotification resultNotification;
	private Database database;
	private Player requestSource;
	private int updatedRows;

	private Callback<DatabaseConnection, PreparedStatement> statementSupplier;
	private Consumer<ResultSet> future;

	public static enum QueryType {
		SELECT,
		UPDATE;
	}

	public RequestModel(Player requestSource, QueryType queryType, Database database) {
		this.requestSource = requestSource;
		this.queryType = queryType;
		this.resultSet = null;
		this.database = database;
	}

	public RequestModel(Player requestSource, QueryType queryType) {
		this.requestSource = requestSource;
		this.queryType = queryType;
		this.resultSet = null;
		this.database = null;
	}
	
	public RequestModel(QueryType queryType) {
		if(queryType != QueryType.UPDATE) {
			throw new RuntimeException("Invalid argument: Query Type not supported.");
		}
		this.requestSource = null;
		this.queryType = queryType;
		this.resultSet = null;
		this.database =  null;
	}

	public PreparedStatement prepareStatement(DatabaseConnection databaseConnection) {
		if (statementSupplier != null) {
			return statementSupplier.call(databaseConnection);
		}
		return null;
	};

	public RequestModel setStatementSupplier(Callback<DatabaseConnection, PreparedStatement> statementSupplier) {
		this.statementSupplier = statementSupplier;
		return this;
	}

	public RequestModel setFuture(Consumer<ResultSet> future) {
		this.future = future;
		return this;
	}

	public void future() {
		if (future != null) {
			future.accept(getResults());
		}
	};

	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}

	public void clear() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void setResults(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public ResultSet getResults() {
		return this.resultSet;
	}

	public void setResultNotification(ResultNotification resultNotification) {
		this.resultNotification = resultNotification;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	public void notify(Object object) {
		if (resultNotification != null) {
			resultNotification.notify(object);
		}
	}

	public Player getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Player requestSource) {
		this.requestSource = requestSource;
	}

	@Override
	public String toString() {
		return "RequestModel [preparedStatement=" + preparedStatement + ", resultSet=" + resultSet + ", queryType=" + queryType + ", resultNotification=" + resultNotification + ", requestSource=" + requestSource + "]";
	}

	public int getUpdatedRows() {
		return updatedRows;
	}

	public void setUpdatedRows(int updatedRows) {
		this.updatedRows = updatedRows;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
