package bfrc.parser;

import bfrc.ast.BlockNode;
import bfrc.lexer.Lexer;
import bfrc.lexer.Token;

public interface Parser {

	/**
	 * The parser builds an Abstract Syntax Tree from the {@link Token}s
	 * provided by the given Lexer.
	 * 
	 * @return root node of AST
	 * @throws ParserException
	 */
	public BlockNode parse(Lexer input) throws ParserException;

}