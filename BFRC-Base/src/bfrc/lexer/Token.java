package bfrc.lexer;

import bfrc.parser.Parser;

/**
 * Tokens represent the basic building blocks of a programming language, that
 * are infered from source code by a {@link Lexer} and are consumed by a
 * {@link Parser}.
 * 
 * @author oreissig
 */
public class Token {
	/**
	 * The basic elements of the Brainfuck language.
	 */
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
