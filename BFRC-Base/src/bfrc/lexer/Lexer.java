package bfrc.lexer;

public interface Lexer {

	/**
	 * Reads the next token.
	 * 
	 * @return next Token, or null if no more Tokens
	 * @throws LexicalException
	 */
	public Token next() throws LexicalException;

}