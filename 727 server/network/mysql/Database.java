package mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.MoreExecutors;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import mysql.connection.ConnectionPool;

/**
 * The manager class that is responsible for SQL connections.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Database {
	
	/**
	 * The pool for this connection.
	 */
	private final ConnectionPool pool;
	
	public Database(String ip, String database, String username, String password, int threads) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://" + ip + ":3306/" + database + "?autoReconnect=true&useSSL=false&serverTimezone=UTC");
		config.setUsername(username);
		config.setPassword(password);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		HikariDataSource ds = new HikariDataSource(config);
		pool = new HikariPool(ds, MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threads)));
		System.out.println(pool == null ?  "Dead" : "alive");
	}
	
	/**
	 * Gets the {@link ConnectionPool}
	 * @return pooling instance.
	 */
	public ConnectionPool getPool() {
		return pool;
	}
	
	/**
	 * Gets the {@link Connection}.
	 * @return Sql connection instance.
	 * @throws SQLException database exception.
	 */
	public Connection getConnection() throws SQLException {
		return pool.getConnection();
	}
}