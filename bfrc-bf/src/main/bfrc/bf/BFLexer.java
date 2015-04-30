package bfrc.bf;

import static bfrc.lexer.Token.TokenType.*;

import java.io.IOException;
import java.io.Reader;

import bfrc.lexer.Lexer;
import bfrc.lexer.LexicalException;
import bfrc.lexer.Token;

/**
 * This is BFRC's default lexer implementation for the Brainfuck language.
 * 
 * @author oreissig
 */
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
			while (true) {
				int last = in.read();
				offset++;
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
						offset = 0;
						lineNo++;
						break;
					case -1:
						// end of file
						return null;
				}
			}
		} catch (IOException e) {
			throw new LexicalException(e);
		}
	}
}
