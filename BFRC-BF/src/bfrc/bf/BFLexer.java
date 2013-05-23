package bfrc.bf;

import static bfrc.lexer.Token.TokenType.*;

import java.io.IOException;
import java.io.Reader;

import bfrc.lexer.Lexer;
import bfrc.lexer.LexicalException;
import bfrc.lexer.Token;

public class BFLexer implements Lexer {
	private Reader in;
	private int lineNo;
	private int offset;

	@Override
	public void setInput(Reader input) {
		lineNo = 1;
		offset = 0;
		in = input;
	}

	@Override
	public Token next() {
		try {
			int last = in.read();
			offset++;
			while (last >= 0) {
				switch (last) {
					case '+':
						return new Token(PLUS, lineNo, offset);
					case '-':
						return new Token(MINUS, lineNo, offset);
					case '>':
						return new Token(RIGHT, lineNo, offset);
					case '<':
						return new Token(LEFT, lineNo, offset);
					case '[':
						return new Token(BEGIN, lineNo, offset);
					case ']':
						return new Token(END, lineNo, offset);
					case '.':
						return new Token(OUT, lineNo, offset);
					case ',':
						return new Token(IN, lineNo, offset);
					case '\n':
						offset = 1;
						lineNo++;
				}
				// no hit, read next char
				last = in.read();
			}
			// end of file
			return null;
		} catch (IOException e) {
			throw new LexicalException(e);
		}
	}
}
