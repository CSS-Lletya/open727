package server.database.pooled;

/**
 * Database connection settings used to
 * create a database connection, being able
 * to set a pool connection or not.
 * 
 * @author Pb600
 * 
 */
public class ConnectionSettings {
	
	private final String host;
	private final String database;
	private final String user;
	private final String password;
	private final int port;
	private final boolean pooled;
	private final long timeOutDelay;
	private final boolean persistent;
	private final int preferredPoolSize;
	private final int maxSize;
	private final int initialSize;
	private final int priorityConnections;
	
	/**
	 * @param host: Host of the database.
	 * @param database: Database name.
	 * @param user: User name to connect to the database.
	 * @param password: Password used to connect to database;
	 * @param port: Port used to connect to the database;
	 * @param pooled: Flag to determine if this database will handle pooled connections.
	 * @param timeOutDelay: Time out of connections;
	 * @param persistent: State if connections are persistent or not.
	 */
	public ConnectionSettings(String host, int port, String database, String user, String password, boolean pooled, int maxSize, int initialSize, int priorityConnections, int preferredPoolSize, long timeOutDelay, boolean persistent) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = port;
		this.pooled = pooled;
		this.maxSize = maxSize;
		this.initialSize = initialSize;
		this.priorityConnections = priorityConnections;
		this.preferredPoolSize = preferredPoolSize;
		this.timeOutDelay = timeOutDelay;
		this.persistent = persistent;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean isPooled() {
		return pooled;
	}
	
	public long getTimeOutDelay() {
		return timeOutDelay;
	}
	
	public boolean isPersistent() {
		return persistent;
	}
	
	public int getPreferredPoolSize() {
		return preferredPoolSize;
	}
	
	public int getPriorityConnections() {
		return priorityConnections;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public int getMaxSize() {
		return maxSize;
	}

	@Override
	public String toString() {
		return "ConnectionSettings [host=" + host + ", database=" + database + ", user=" + user + ", password=" + password + ", port=" + port + ", pooled=" + pooled + ", timeOutDelay=" + timeOutDelay + ", persistent=" + persistent + ", preferredPoolSize=" + preferredPoolSize + ", maxSize=" + maxSize + ", initialSize=" + initialSize + ", priorityConnections=" + priorityConnections + "]";
	}
}