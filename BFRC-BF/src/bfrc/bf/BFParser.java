package bfrc.bf;

import bfrc.ast.BlockNode;
import bfrc.ast.ChangeValueNode;
import bfrc.ast.InputNode;
import bfrc.ast.LoopNode;
import bfrc.ast.MovePointerNode;
import bfrc.ast.Node;
import bfrc.ast.OutputNode;
import bfrc.ast.RootNode;
import bfrc.lexer.Lexer;
import bfrc.lexer.Token;
import bfrc.lexer.Token.TokenType;
import bfrc.parser.Parser;
import bfrc.parser.ParserException;

public class BFParser implements Parser {

	private Lexer in;

	@Override
	public synchronized BlockNode parse(Lexer input) throws ParserException {
		in = input;

		BlockNode ast = new RootNode();
		root(ast);

		in = null;
		return ast;
	}

	/**
	 * Parses a program (root block).
	 * Returns on EOF, fails at ']'.
	 * 
	 * @param root block
	 */
	private void root(BlockNode root) {
		Token t = in.next();

		while (t != null) {
			if (t.type == TokenType.END)
				throw new ParserException("extra ']' without matching '['");
			else
				common(root, t);

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
				common(loop, t);

			// next token
			t = in.next();
		}
		throw new ParserException("loop at " + loop.position +
				" has no according ']'");
	}

	private void common(BlockNode context, Token t) {
		switch (t.type) {
			case PLUS:
				Node n = new ChangeValueNode(t.pos, +1);
				context.sub.add(n);
				break;
			case MINUS:
				n = new ChangeValueNode(t.pos, -1);
				context.sub.add(n);
				break;
			case LEFT:
				n = new MovePointerNode(t.pos, -1);
				context.sub.add(n);
				break;
			case RIGHT:
				n = new MovePointerNode(t.pos, +1);
				context.sub.add(n);
				break;
			case IN:
				n = new InputNode(t.pos);
				context.sub.add(n);
				break;
			case OUT:
				n = new OutputNode(t.pos);
				context.sub.add(n);
				break;
			case BEGIN:
				BlockNode ln = new LoopNode(t.pos);
				loop(ln);
				context.sub.add(ln);
				break;
			default:
				throw new ParserException("unexpected Token type " + t.type);
		}
	}
}
