package mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import mysql.connection.ConnectionPool;

/**
 * A {@link ConnectionPool} impleme

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import javantation for HikariCP.
 * @author Whis
 */
public final class HikariPool implements ConnectionPool {

	/**
	 * The {@link ListeningExecutorService} to submit tasks to.
	 */
	private final ListeningExecutorService service;

	/**
	 * The {@link HikariDataSource} to obtain {@link Connection}s from.
	 */
	private final HikariDataSource dataSource;

	/**
	 * Creates a new {@link HikariPool} using the given {@link HikariConfig}.
	 */
	public HikariPool(HikariDataSource dataSource, ListeningExecutorService service) {
		this.service = service;
		this.dataSource = dataSource;
	}

	@Override
	public ListenableFuture<Connection> obtainConnection() throws Exception {
		return service.submit((Callable<Connection>) dataSource::getConnection);
	}

	@Override
	public ListenableFuture<?> terminate() throws Exception {
		return service.submit(dataSource::close);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
