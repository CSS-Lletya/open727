package server.database.pooled;

/**
 * Watch for inactive connections and close them.
 * 
 * @author Pb600
 * 
 */
public class DatabaseWatcher implements Runnable {
	
	private static final int SERVICE_TIMER = 60 * (1000);
	private ConnectionPool connectionPool;
	
	private boolean isRunning;
	
	public DatabaseWatcher(String databaseName, ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
		this.isRunning = true;
	}
	
	public void interrupt() {
		isRunning = false;
	}
	
	@Override
	public void run() {
		while (isRunning()) {
			for (DatabaseConnection connection : connectionPool.getDatabaseConnections()) {
				if (connection.isConnectionOpened() && connection.isTimedOut()) {
					//System.out.println("Closed connection timmed out.");
					connection.closeConnection();
				}
			}
			sleepThread(SERVICE_TIMER);
		}
	}
	
	private void sleepThread(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isRunning() {
		return isRunning;
	}
	
}