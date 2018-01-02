package edu.ecnu.yt.pretty.reference.strategy.authority;

/**
 * @author yt
 * @date 2017-12-12 19:38:05
 */
public class ClientAuthentication {
	private final String id;
	private final String password;
	
	public ClientAuthentication(String id, String password) {
		this.id = id;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public String getID() {
		return id;
	}
}
