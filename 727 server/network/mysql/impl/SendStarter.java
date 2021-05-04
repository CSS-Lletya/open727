package mysql.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rs.game.player.Player;

import mysql.connection.ConnectionPool;
import mysql.connection.ConnectionUse;

public class SendStarter extends ConnectionUse {
	
	private final Player player;
	
	public SendStarter(ConnectionPool pool, Player player) {
		super(pool);
		this.player = player;
	}
	
	@Override
	public void append(Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(getQuery());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss aa");
		Date date = new Date();
		statement.clearParameters();
		statement.setInt(1, 0);
		statement.setString(2, player.getDisplayName());
		statement.setString(3, dateFormat.format(date));
		statement.execute();
	}
	
	@Override
	public void onError() {
		System.out.println("Error updating starter for " + player.getUsername());
	}

	@Override
	public String getQuery() {
		return "INSERT INTO starter (id,username,date) VALUES (?, ?, ?)";
	}
}