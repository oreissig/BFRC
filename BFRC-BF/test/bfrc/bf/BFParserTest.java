package bfrc.bf;

import static bfrc.ast.NodeType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

import org.junit.Test;

import bfrc.ast.ChangeNode;
import bfrc.ast.InputNode;
import bfrc.ast.LoopNode;
import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.ast.OutputNode;
import bfrc.ast.RootNode;
import bfrc.lexer.Lexer;

public class BFParserTest {

	private BFParser p = new BFParser();

	@Test
	public void parseEmptyInput() throws IOException {
		try (Reader in = new StringReader("")) {
			Lexer l = new BFLexer();
			l.setInput(in);
			RootNode ast = p.parse(l);

			assertNotNull(ast);
			assertEquals(ROOT, ast.type);
			assertTrue(ast.sub.isEmpty());
			assertPosition(1, 1, ast);
		}
	}

	@Test
	public void parseCommands() throws IOException {
		try (Reader in = new StringReader("+-><[].,")) {
			Lexer l = new BFLexer();
			l.setInput(in);
			Iterator<Node> nodes = p.parse(l).sub.iterator();

			assertNode(VALUE, 1, nodes.next());
			assertNode(VALUE, -1, nodes.next());
			assertNode(POINTER, 1, nodes.next());
			assertNode(POINTER, -1, nodes.next());
			assertNode(LOOP, LoopNode.class, nodes.next());
			assertNode(OUTPUT, OutputNode.class, nodes.next());
			assertNode(INPUT, InputNode.class, nodes.next());
		}
	}

	public static void assertNode(NodeType expectedType,
			Class<? extends Node> expectedClass, Node actual) {
		assertNotNull(actual);
		assertEquals(expectedType, actual.type);
		assertTrue(expectedClass.isAssignableFrom(actual.getClass()));
	}

	public static void assertNode(NodeType expectedType, int expectedValue,
			Node actual) {
		assertNotNull(actual);
		assertEquals(expectedType, actual.type);
		if (actual instanceof ChangeNode) {
			ChangeNode cn = (ChangeNode) actual;
			assertEquals(expectedValue, cn.change);
			assertFalse(cn.absolute);
		}
		else
			fail("wrong type: " + actual.getClass());
	}

	public static void assertPosition(int expectedLine, int expectedOffset,
			Node actual) {
		assertNotNull(actual);
		assertEquals(expectedLine, actual.line);
		assertEquals(expectedOffset, actual.offset);
	}
}
