package dk.madsboddum.discordwolfet.model;

public class GameServer {
	private final String address;
	private final int port;
	
	public GameServer(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public String getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	@Override
	public String toString() {
		return "GameServer{" + "address='" + address + '\'' + ", port=" + port + '}';
	}
}
