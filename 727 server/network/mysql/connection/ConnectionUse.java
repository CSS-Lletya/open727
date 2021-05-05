package mysql.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * An abstracted class which handles the connection pool usages.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class ConnectionUse {
	
	/**
	 * The connection pool used in the context.
	 */
	private final ConnectionPool pool;
	
	/**
	 * Constructs a new pool usage.
	 * @param pool the pool being used.
	 */
	public ConnectionUse(ConnectionPool pool) {
		this.pool = pool;
	}
	
	/**
	 * Submits the usage.
	 */
	public void submit() {
		if(pool == null)
			return;
		try {
			Futures.addCallback(pool.obtainConnection(), new FutureCallback<Connection>() {
				@Override
				public void onFailure(Throwable arg0) {
					onError();
				}
				@Override
				public void onSuccess(Connection arg0) {
					try {
						try(Connection conn = arg0) {
							if(conn == null) {
								onError();
							} else {
								append(conn);
							}
						}
					} catch(Exception e) {
						e.printStackTrace();
						onFailure(e);
					}
				}
			}, MoreExecutors.directExecutor());
		} catch(Exception e) {
			e.printStackTrace();
			onError();
		}
	}
	
	/**
	 * Appending the connection.
	 * @param con the connection being used.
	 */
	public abstract void append(Connection con) throws SQLException;
	
	/**
	 * Process handled on an error.
	 */
	public abstract void onError();
	
	/**
	 * The Query we're initially sending.
	 * @return
	 */
	public abstract String getQuery();
}
