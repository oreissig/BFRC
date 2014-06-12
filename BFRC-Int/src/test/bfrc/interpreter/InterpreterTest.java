package bfrc.interpreter;

import java.io.IOException;

import org.junit.Test;

import bfrc.ast.RootNode;
import bfrc.ast.TestAST;

public class InterpreterTest {

	@Test
	public void emptyProgram() {
		RootNode n = TestAST.begin().end();
		new TestInterpreter().work(n);
	}

	@Test
	public void changeAndSet() {
		RootNode n = TestAST.begin().change(42).set(21).change(2).write().end();
		new TestInterpreter((byte) 23).work(n);
	}

	@Test
	public void inputAndOutput() {
		RootNode n = TestAST.begin().read().change(1).write().end();
		new TestInterpreter(new Byte[] { 42 }, new Byte[] { 43 }).work(n);
	}

	@Test
	public void moves() {
		RootNode n = TestAST.begin().set(1).move(1).set(2).move(2).set(3)
				.move(-3).write().move(1).write().move(2).write().end();
		new TestInterpreter(new Byte[] { 1, 2, 3 }).work(n);
	}

	@Test
	public void loopAsIf() {
		RootNode n = TestAST.begin().set(1).loop().write().set(0).end()
				.loop().write().end().end();
		new TestInterpreter((byte) 1).work(n);
	}

	@Test
	public void loop() {
		RootNode n = TestAST.begin().set(3).loop().write().change(-1).end().end();
		new TestInterpreter(new Byte[] { 3, 2, 1 }).work(n);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void moveUnderflow() {
		RootNode n = TestAST.begin().move(-1).change(1).end();
		new TestInterpreter().work(n);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void moveOverflow() {
		RootNode n = TestAST.begin().set(1).loop().move(1).change(1).end()
				.end();
		new TestInterpreter().work(n);
	}
}
