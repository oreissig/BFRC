package bfrc.lexer;

import java.io.Reader;

/**
 * Common interface for all BFRC lexers.
 * 
 * @author oreissig
 */
public interface Lexer {

	/**
	 * Specifies the input for this lexer.
	 * 
	 * @param input input to be lexed
	 */
	public void setInput(Reader input);

	/**
	 * Reads the next token.
	 * 
	 * @return next Token, or null if no more Tokens
	 * @throws LexicalException
	 */
	public Token next() throws LexicalException;
}