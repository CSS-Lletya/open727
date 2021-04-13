package server.database;

import com.rs.game.player.Player;

import server.database.passive.PassiveDatabaseWorker.RequestType;

public class DatabaseRequest {
	
	private Player client;
	private RequestType requestType;
	
	public DatabaseRequest(Player client, RequestType requestType) {
		this.client = client;
		this.requestType = requestType;
	}
	
	public RequestType getRequestType() {
		return requestType;
	}
	
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	
	public Player getClient() {
		return client;
	}
	
	public void setClient(Player client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "DatabaseRequest [client=" + client.getUsername() + ", requestType=" + requestType + "]";
	}

	public void clear() {
		setRequestType(null);
		setClient(null);
	}
}
