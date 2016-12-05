package nickmafra.telegramBot;

import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;

public final class TelegramBot {

    private final String endpoint = "https://api.telegram.org/";
    private final String token;
    private int lastUpdateId;
    private Callable<String> func;

    public TelegramBot(String token) {
    	this.token = token;
        lastUpdateId = 0;
    }
    
    public TelegramBot(String token, Callable<String> func) {
        this(token);
        this.func = func;
    }

    // Getters/Setters
    
    public Callable<String> getFunc() {
    	return func;
    }
    
    public void setFunc(Callable<String> func) {
    	this.func = func;
    }
    
    public int getLastUpdateId() {
    	return lastUpdateId;
    }
    
    public void setLastUpdateId(int lastUpdateId) {
    	this.lastUpdateId = lastUpdateId;
    }

    private String getToken() {
    	return token;
    }
    
    // Private Methods
    
    private JSONObject getResult(JSONObject json) throws TelegramException {
    	try {
			return json.getJSONObject("result");
		} catch (JSONException e) {
			throw new TelegramException("Error while getting result", e);
		}
    }
    
    private JSONArray getResults(JSONObject json) throws TelegramException {
    	try {
			return json.getJSONArray("result");
		} catch (JSONException e) {
			throw new TelegramException("Error while getting result", e);
		}
    }

    // Public Methods
    
    public JSONObject callMethod(String methodName, String[] params, Object... args) throws TelegramException {
    	try {
    		HttpRequestWithBody request = Unirest.post(endpoint + "bot" + token + "/" + methodName);
    		MultipartBody body = null;
    		if (params!= null && args != null)
	    		for (int i = 0; i < (params.length<args.length?params.length:args.length); i++) {
	    			if (i == 0) body = request.field(params[i], args[i]);
	    			else body = body.field(params[i], args[i]);
	    		}
			JSONObject json = body.asJson().getBody().getObject();
			if (!json.getBoolean("ok"))
				throw new TelegramException(json.getString("description"));
	    	return json;
    	}
    	catch (Exception e) {
    		throw new TelegramException("Error while invoking method", e);
    	}
    }

    public Update[] getUpdates(int offset) throws TelegramException {
		String[] params = new String[]{"offset"};
		JSONObject json = callMethod("getUpdates", params, offset);
    	try {
    		JSONArray results = getResults(json);
    		Update[] updates = new Update[results.length()];
            for (int i = 0; i < results.length(); i++)
            	updates[i] = new Update(results.getJSONObject(i));
	        if (updates.length > 0) lastUpdateId = updates[updates.length - 1].getId();
            return updates;
    	}
    	catch (Exception e) {
    		throw new TelegramException("Error while getting updates", e);
    	}
    }

    public User getMe() throws TelegramException {
    	JSONObject json = callMethod("getMe", null);
    	try {
			return new User(json.getJSONObject("result"));
		} catch (Exception e) {
			throw new TelegramException("Error while getting bot info", e);
		}
    }
    
    public Message sendMessage(long chatId, String text) throws TelegramException {
		String[] params = new String[]{"chat_id", "text"};
		JSONObject json = callMethod("sendMessage", params, chatId, text);
		try {
        	return new Message(getResult(json));
    	}
    	catch (Exception e) {
    		throw new TelegramException("Error while sending message", e);
    	}
    }

    public void answer(Message[] sent, Message[] received) throws TelegramException {
    	if (func == null) throw new TelegramException("A função func não foi definida");
    	try {
	    	Update[] updates = getUpdates(++lastUpdateId);
	        if (updates == null || !(updates.length > 0)) return;
	        received = new Message[updates.length];
	        sent = new Message[updates.length];
	        for (int i = 0; i < updates.length; i++) {
	        	received[i] = updates[i].getMessage();
	        	sent[i] = sendMessage(received[i].getChat().getId(),func.call());
	        }
    	}
    	catch (Exception e) {
    		throw new TelegramException("Error while answer", e);
    	}
    }
    
    
	@Override
	public boolean equals(Object telegramBot)
	{
		if (this == telegramBot) return true;
		if (!(telegramBot instanceof TelegramBot)) return false;
		return this.getToken() == ((TelegramBot) telegramBot).getToken();
	}
}