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
	private ConnectionPool pool;
	
	public void init() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost/open727?autoReconnect=true");
		config.setUsername("root");
		config.setPassword("");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		HikariDataSource ds = new HikariDataSource(config);
		pool = new HikariPool(ds, MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1)));
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