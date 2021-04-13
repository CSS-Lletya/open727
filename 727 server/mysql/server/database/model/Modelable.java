package server.database.model;

import java.sql.SQLException;

public interface Modelable {
	
	public void insert() throws SQLException;
	
	public void update() throws SQLException;
	
	public void select() throws SQLException;
	
	public void delete() throws SQLException;
	
	public void close();
	
}
