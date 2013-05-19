package bfrc.lexer;

public class Token {
	public static enum TokenType {
		PLUS, MINUS, LEFT, RIGHT, IN, OUT, BEGIN, END
	}

	public final TokenType type;
	public final String pos;

	public Token(TokenType type, int line, int offset) {
		this.type = type;
		this.pos = line + ":" + offset;
	}
}
