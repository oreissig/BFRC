package bfrc.parser;

import bfrc.ast.RootNode;
import bfrc.lexer.Lexer;
import bfrc.lexer.Token;

/**
 * Common interface for all BFRC parsers.
 * 
 * @author oreissig
 */
public interface Parser {

	/**
	 * The parser builds an Abstract Syntax Tree from the {@link Token}s
	 * provided by the given Lexer.
	 * 
	 * @return root node of AST
	 * @throws ParserException
	 */
	public RootNode parse(Lexer input) throws ParserException;

}