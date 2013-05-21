package bfrc.ook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import bfrc.lexer.Lexer;
import bfrc.lexer.LexicalException;
import bfrc.lexer.Token;
import bfrc.lexer.Token.TokenType;

/**
 * This is an alternative Lexer implementation, that parses Ook! instead of the
 * standard Brainfuck syntax.
 * 
 * @author oreissig
 * 
 * @see <a href="http://www.dangermouse.net/esoteric/ook.html">Ook! programming
 *      language</a>
 */
public class OokLexer implements Lexer {
	private final Reader in;

	private int lineNo = 1;
	private int offset = 0;
	private Character last;

	public OokLexer(String fileName) throws IOException {
		this(new BufferedReader(new FileReader(fileName)));
	}

	public OokLexer(Reader in) throws IOException {
		this.in = in;
	}

	@Override
	public Token next() throws LexicalException {
		try {
			// read first word
			String word1 = readWord();
			int offset1 = offset - 4;
			int line1 = lineNo;
			if (word1 == null)
				return null;
			Ook ook1 = Ook.reverse.get(word1);
			if (ook1 == null)
				throw new LexicalException("invalid Ook at " +
						line1 + ":" + offset1);

			// read second word
			String word2 = readWord();
			int offset2 = offset - 4;
			int line2 = lineNo;
			if (word2 == null)
				throw new LexicalException("expected Ook for " + word1 +
						" at " + lineNo + ":" + offset);
			Ook ook2 = Ook.reverse.get(word2);
			if (ook2 == null)
				throw new LexicalException("invalid Ook at " +
						line2 + ":" + offset2);

			// get brainfuck token type
			OokTuple tuple = new OokTuple(ook1, ook2);
			TokenType tt = Ook.tokens.get(tuple);
			if (tt == null)
				throw new LexicalException(tuple + " is no valid Ook command");

			return new Token(tt, line1, offset1);
		} catch (IOException e) {
			throw new LexicalException(e);
		}
	}

	private String readWord() throws IOException {
		// skip white spaces
		while (nextChar() && Character.isWhitespace(last))
			;
		if (last == null)
			return null;

		StringBuilder word = new StringBuilder();
		word.append(last);
		while (nextChar() && !Character.isWhitespace(last))
			word.append(last);

		return word.toString();
	}

	private boolean nextChar() throws IOException {
		int c = in.read();
		if (c < 0) {
			last = null;
			return false;
		} else {
			last = (char) c;
			if (last == '\n') {
				lineNo++;
				offset = 1;
			} else {
				offset++;
			}
			return true;
		}
	}
}
