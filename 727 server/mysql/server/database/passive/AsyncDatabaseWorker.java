package server.database.passive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rs.utils.NamedThreadFactory;

import server.database.GameDatabase;
import server.database.model.RequestModel;
import server.database.model.RequestModel.QueryType;
import server.database.pooled.DatabaseConnection;

/**
 * Process Asynchronous database operation requests.
 * 
 * @author Pb600
 * 
 */
public class AsyncDatabaseWorker {
	/**
	 * The to which models are submitted to be processed.
	 */
	private final ExecutorService executor = Executors.newFixedThreadPool(3, new NamedThreadFactory("Async Database Worker Executor"));

	/**
	 * Submit a request model to be executed in an available core as soon as
	 * possible.
	 * 
	 * @param requestModel:
	 *            Model to be processed.
	 */
	public void submitModel(final RequestModel requestModel) {
		executor.submit(() -> {
			DatabaseConnection databaseConnection = null;

			if (requestModel.getDatabase() != null) {
				databaseConnection = requestModel.getDatabase().getConnection(AsyncDatabaseWorker.class.getSimpleName() + ":" + 40);
			} else {
				databaseConnection = GameDatabase.getActionManager().getConnection(AsyncDatabaseWorker.class.getSimpleName() + ":" + 42);
			}

			if (databaseConnection == null) {
				GameDatabase.getActionManager().reportUnavailableConnection();
				requestModel.clear();
				return;
			}

			PreparedStatement preparedStatement = requestModel.prepareStatement(databaseConnection);
			try {
				requestModel.setPreparedStatement(preparedStatement);

				QueryType queryType = requestModel.getQueryType();
				if (queryType == QueryType.SELECT) {
					ResultSet results = preparedStatement.executeQuery();
					requestModel.setResults(results);
				} else if (queryType == QueryType.UPDATE) {
					int updatedRows = preparedStatement.executeUpdate();
					requestModel.notify(new Integer(updatedRows));
					requestModel.setUpdatedRows(updatedRows);
				}
				if (requestModel.getRequestSource() != null) {
					requestModel.getRequestSource().requestResults.offer(requestModel);
				} else {
					requestModel.clear();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				databaseConnection.release();
			}
		});
	}
}
