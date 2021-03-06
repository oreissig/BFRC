package bfrc.bf;

import bfrc.ast.BlockNode;
import bfrc.ast.ValueNode;
import bfrc.ast.InputNode;
import bfrc.ast.LoopNode;
import bfrc.ast.PointerNode;
import bfrc.ast.Node;
import bfrc.ast.OutputNode;
import bfrc.ast.RootNode;
import bfrc.lexer.Lexer;
import bfrc.lexer.Token;
import bfrc.lexer.Token.TokenType;
import bfrc.parser.Parser;
import bfrc.parser.ParserException;

/**
 * This is BFRC's default parser implementation for the Brainfuck language.
 * <p>
 * 
 * This parser is based on the following grammar:
 * <tt><ul>
 * <li>program := operation*</li>
 * <li>operation := "+" | "-" | "<" | ">" | "." | "," | loop</li>
 * <li>loop := "[" operation* "]"</li>
 * </ul></tt>
 * 
 * @author oreissig
 */
public class BFParser implements Parser {

	private Lexer in;

	@Override
	public synchronized RootNode parse(Lexer input) throws ParserException {
		in = input;

		RootNode ast = new RootNode();
		program(ast);

		in = null;
		return ast;
	}

	/**
	 * Parses a program (root block).
	 * Returns on EOF, fails at ']'.
	 * 
	 * @param root block
	 */
	private void program(BlockNode root) {
		Token t = in.next();

		while (t != null) {
			if (t.type == TokenType.END)
				throw new ParserException("extra ']' without matching '['");
			else
				operation(root, t);

			// next token
			t = in.next();
		}
	}

	/**
	 * Parses a loop block.
	 * Returns on ']', fails at EOF.
	 * 
	 * @param loop block
	 */
	private void loop(BlockNode loop) {
		Token t = in.next();

		while (t != null) {
			if (t.type == TokenType.END)
				return;
			else
				operation(loop, t);

			// next token
			t = in.next();
		}
		throw new ParserException("loop at " + loop.position() +
				" has no according ']'");
	}

	private void operation(BlockNode context, Token t) {
		switch (t.type) {
			case PLUS:
				Node n = new ValueNode(t.line, t.offset, +1);
				context.sub.add(n);
				break;
			case MINUS:
				n = new ValueNode(t.line, t.offset, -1);
				context.sub.add(n);
				break;
			case LEFT:
				n = new PointerNode(t.line, t.offset, -1);
				context.sub.add(n);
				break;
			case RIGHT:
				n = new PointerNode(t.line, t.offset, +1);
				context.sub.add(n);
				break;
			case IN:
				n = new InputNode(t.line, t.offset);
				context.sub.add(n);
				break;
			case OUT:
				n = new OutputNode(t.line, t.offset);
				context.sub.add(n);
				break;
			case BEGIN:
				BlockNode ln = new LoopNode(t.line, t.offset);
				loop(ln);
				context.sub.add(ln);
				break;
			default:
				throw new ParserException("unexpected Token type " + t.type);
		}
	}
}
