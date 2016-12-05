package nickmafra.telegramBot;

import org.json.JSONException;
import org.json.JSONObject;

public class Message{
	private final int id;
	private int date;
	private Chat chat;
	private String text;
	private User from;
	
	public Message(int id, int date, Chat chat) {
		this.id = id;
		this.date = date;
		this.chat = chat;
	}
	
	public Message (int id, int date, Chat chat, String text) {
		this(id, date, chat);
		this.text = text;
	}
	
	public Message (int id, int date, Chat chat, String text, User from) {
		this(id, date, chat, text);
		this.from = from;
	}

	public Message (JSONObject json) throws JSONException, TelegramException {
		this(json.getInt("message_id"), json.getInt("date"), new Chat(json.getJSONObject("chat")));
		try {
			text = json.getString("text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			from = new User(json.getJSONObject("from"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Getters/Setters*/

	public int getId() {
		return id;
	}
	
	public int getDate() {
		return date;
	}

	public Chat getChat() {
		return chat;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	@Override
	public boolean equals(Object message)
	{
		if (this == message) return true;
		if (!(message instanceof Message)) return false;
		return this.getId() == ((Message) message).getId();
	}

}
