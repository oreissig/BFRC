package bfrc.bf;

import static bfrc.lexer.Token.TokenType.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import bfrc.lexer.Token;
import static org.junit.Assert.*;

public class BFLexerTest {

	private BFLexer l = new BFLexer();

	@Test
	public void parseEmptyString() throws IOException {
		try (Reader in = new StringReader("")) {
			l.setInput(in);
			assertNull(l.next());
		}
	}

	@Test
	public void parseJustComment() throws IOException {
		try (Reader in = new StringReader("Hello World!!123")) {
			l.setInput(in);
			assertNull(l.next());
		}
	}

	@Test
	public void parseCommands() throws IOException {
		try (Reader in = new StringReader("+-<>[].,")) {
			l.setInput(in);
			assertEquals(PLUS, l.next().type);
			assertEquals(MINUS, l.next().type);
			assertEquals(LEFT, l.next().type);
			assertEquals(RIGHT, l.next().type);
			assertEquals(BEGIN, l.next().type);
			assertEquals(END, l.next().type);
			assertEquals(OUT, l.next().type);
			assertEquals(IN, l.next().type);
		}
	}

	@Test
	public void trackPositions() throws IOException {
		try (Reader in = new StringReader("+  ++\n\n+\t+")) {
			l.setInput(in);

			assertPosition(1, 1, l.next());
			assertPosition(1, 4, l.next());
			assertPosition(1, 5, l.next());
			assertPosition(3, 1, l.next());
			assertPosition(3, 3, l.next());
		}
	}

	public static void assertPosition(int expectedLine, int expectedOffset, Token actual) {
		assertNotNull(actual);
		assertEquals(expectedLine, actual.line);
		assertEquals(expectedOffset, actual.offset);
	}
}
