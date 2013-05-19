package bfrc.ook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import bfrc.lexer.Lexer;
import bfrc.lexer.LexicalException;
import bfrc.lexer.Token;

/**
 * This is an alternative Lexer implementation, that parses Ook! instead of the
 * standard Brainfuck syntax.
 * 
 * @author oreissig
 * 
 * @see <a href="http://www.dangermouse.net/esoteric/ook.html">Ook! programming language</a>
 */
public class OokLexer implements Lexer {
	private final Reader in;

	private int lineNo = 1;
	private int offset = 0;

	public OokLexer(String fileName) throws IOException {
		this(new BufferedReader(new FileReader(fileName)));
	}

	public OokLexer(Reader in) throws IOException {
		this.in = in;
	}

	@Override
	public Token next() throws LexicalException {
		// TODO Auto-generated method stub
		return null;
	}
}
