package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	private Connection conn;
	private Statement stmt;
	
	// do not modify
	private String host;
	private String user;
	private String pass;
	private String database;
	
	public Database(String host, String user, String pass, String data) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.database = data;
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public Statement getStatement() {
		return stmt;
	}
	
	public boolean init() {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public PreparedStatement prepare(String query) throws SQLException {
		return conn.prepareStatement(query);
	}
	
	public int executeUpdate(String query) {
        try {
        	this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
	
	public ResultSet executeQuery(String query) {
        try {
        	this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
	
	public void destroyAll() {
        try {
    		conn.close();
        	conn = null;
        	if (stmt != null) {
    			stmt.close();
        		stmt = null;
        	}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
}