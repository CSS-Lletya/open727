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
	public void append(Connection con) throws SQLException {
		System.out.println("?");
//		PreparedStatement stmt2 = con.prepareStatement(generateQuery());
//		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss aa");
//		Date date = new Date();
//		stmt2.clearParameters();
//		stmt2.setInt(1, 0);
//		stmt2.setString(2, player.getDisplayName());
//		stmt2.setString(3, dateFormat.format(date));
//		System.out.println("?");
//		stmt2.execute();
//		System.out.println("?");
	}
	
	@Override
	public void onError() {
		System.out.println("Error updating hiscores for " + player.getUsername());
	}
	
	private static String generateQuery() {
		return "INSERT INTO starter (id,username,date) VALUES (?, ?, ?)";
	}
}
