package me.retrodaredevil.pokecreator.resources;

/**
 * @author retro
 *
 */
public enum ServerType {

	CLIENT_SERVER(true, true, false), CLIENT_ONLY(true, false, false), SERVER_ONLY(false, true, false), CLIENT_CONNECTED(true, false, true);
	
	private static ServerType type = null;
	
	private boolean client;
	private boolean server;
	private boolean connected;
	
	private ServerType(boolean client, boolean server, boolean connected){
		this.client = client;
		this.server = server;
		this.connected = connected;
	}
	
	public boolean isClient(){
		return client;
	}
	public boolean isServer(){
		return server;
	}
	public boolean isConnected(){
		return connected;
	}
	
	public boolean hasScreen(){
		return client;
	}
	public boolean hasCommandLine(){
		return !client;
	}
	
	@Override
	public String toString() {
		return "ServerType{client:" + client + ",server:" + server + ",connected" + connected + "}";
	}
	
	
	public static void setType(ServerType type){
		ServerType.type = type;
	}
	public static ServerType getType(){
		return type;
	}
	
	
}
