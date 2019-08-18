package dev.juho.hoi4.parser.data;

public class PlayerCountry {

	private String user;
	private boolean leader;

	public void setLeader(boolean leader) {
		this.leader = leader;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public boolean isLeader() {
		return leader;
	}
}
