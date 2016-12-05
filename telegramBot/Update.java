package br.com.virtus.sgc.telegram;

import org.json.JSONException;
import org.json.JSONObject;

public class Update{
	private final int id;
	private Message message;
	
	public Update(int id) {
		this.id = id;
	}
	
	public Update(int id, Message message) {
		this(id);
		this.message = message;
	}
	
	public Update(JSONObject json) throws JSONException, TelegramException {
		this(json.getInt("update_id"));
		try {
			message = new Message(json.getJSONObject("message"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Getters / Setters
	
	public int getId() {
		return id;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}
	
	@Override
	public boolean equals(Object update)
	{
		if (this == update) return true;
		if (!(update instanceof Update)) return false;
		return this.getId() == ((Update) update).getId();
	}
	
}
