package nickmafra.telegramBot;

public class TelegramException extends Exception {
	private static final long serialVersionUID = 1L;
	public TelegramException() {
		super();
	}
	public TelegramException(String message) {
		super(message);
	}
	public TelegramException(Throwable cause) {
		super(cause);
	}
	public TelegramException(String message, Throwable cause) {
		super(message, cause);
	}
}
