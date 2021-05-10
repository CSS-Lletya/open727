package mysql.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * An interface for asynchronously pooling database {@link Connection}s.
 * @author Whis
 */
public interface ConnectionPool {
	/**
	 * Attempts to obtain a {@link Connection} from the pool.
	 */
	ListenableFuture<Connection> obtainConnection() throws Exception;
	
	/**
	 * Terminates this pool.
	 */
	ListenableFuture<?> terminate() throws Exception;
	
	/**
	 * Gets the main connection.
	 * @return database connection.
	 */
	Connection getConnection() throws SQLException;
}
