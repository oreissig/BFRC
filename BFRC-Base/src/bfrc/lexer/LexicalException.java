package bfrc.lexer;

@SuppressWarnings("serial")
public class LexicalException extends RuntimeException {

	public LexicalException() {
		super();
	}

	public LexicalException(String message) {
		super(message);
	}

	public LexicalException(Throwable cause) {
		super(cause);
	}

	public LexicalException(String message, Throwable cause) {
		super(message, cause);
	}
}
