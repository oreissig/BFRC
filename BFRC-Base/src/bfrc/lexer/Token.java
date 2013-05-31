package bfrc.lexer;

public class Token {
	public static enum TokenType {
		PLUS, MINUS, LEFT, RIGHT, IN, OUT, BEGIN, END
	}

	public final TokenType type;
	public final int line;
	public final int offset;

	public Token(TokenType type, int line, int offset) {
		this.type = type;
		this.line = line;
		this.offset = offset;
	}

	@Override
	public String toString() {
		return line + ":" + offset + "-" + type;
	}
}
