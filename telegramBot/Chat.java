package nickmafra.telegramBot;

import org.json.JSONException;
import org.json.JSONObject;

public class Chat{
	private long id;
	private String type;
	private String userName;

	public Chat(long id, String type) throws TelegramException {
		this.id = id;
		if (type.toLowerCase().equals("private") || type.toLowerCase().equals("group") ||
				type.toLowerCase().equals("supergroup") || type.toLowerCase().equals("channel"))
			this.type = type;
		else
			throw new TelegramException("Invalid chat type \"" + type + "\"");
	}
	
	public Chat(long id, String userName, String type) throws TelegramException {
		this(id, type);
		this.userName = userName;
	}
	
	public Chat (JSONObject json) throws JSONException, TelegramException {
		this(json.getLong("id"), json.getString("type"));
		try {
			userName = json.getString("username");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// Getters/Setters
	
	public long getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Override
	public boolean equals(Object chat)
	{
		if (this == chat) return true;
		if (!(chat instanceof Chat)) return false;
		return this.getId() == ((Chat) chat).getId();
	}

}
