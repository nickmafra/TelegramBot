package nickmafra.telegramBot;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private final int id;
	private String firstName;
	private String userName;
	
	public User (int id, String firstName) {
		this.id = id;
		this.firstName = firstName;
	}
	
	public User (int id, String firstName, String userName) {
		this(id, userName);
		this.userName = userName;
	}

	public User (JSONObject json) throws JSONException, TelegramException {
		this(json.getInt("id"),json.getString("first_name"));
		try {
			userName = json.getString("username");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Getters/Setters

	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Override
	public boolean equals(Object user)
	{
		if (this == user) return true;
		if (!(user instanceof User)) return false;
		return this.getId() == ((User) user).getId();
	}
	
}
