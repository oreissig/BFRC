package bfrc.ook;

import static bfrc.lexer.Token.TokenType.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bfrc.lexer.Token.TokenType;

public enum Ook {
	Period, Exclam, Question;

	public String toString() {
		switch (this) {
			case Period:
				return "Ook.";
			case Exclam:
				return "Ook!";
			case Question:
				return "Ook?";
			default:
				return null;
		}
	};

	/**
	 * maps Ook keywords to elements of this enum
	 */
	public static final Map<String, Ook> mapping;

	/**
	 * maps pairs of Ook keywords to a Brainfuck Token
	 */
	public static final Map<OokTuple, TokenType> tokens;

	static {
		Map<String, Ook> m = new HashMap<>(3);
		m.put("Ook.", Period);
		m.put("Ook!", Exclam);
		m.put("Ook?", Question);
		mapping = Collections.unmodifiableMap(m);

		Map<OokTuple, TokenType> t = new HashMap<>(8);
		// Ook. Ook? Move the Memory Pointer to the next array cell.
		t.put(new OokTuple(Period, Question), RIGHT);
		// Ook? Ook. Move the Memory Pointer to the previous array cell.
		t.put(new OokTuple(Question, Period), LEFT);
		// Ook. Ook. Increment the array cell pointed at by the Memory Pointer.
		t.put(new OokTuple(Period, Period), PLUS);
		// Ook! Ook! Decrement the array cell pointed at by the Memory Pointer.
		t.put(new OokTuple(Exclam, Exclam), MINUS);
		// Ook. Ook! Read a character from STDIN and put its ASCII value into
		// the cell pointed at by the Memory Pointer.
		t.put(new OokTuple(Period, Exclam), IN);
		// Ook! Ook. Print the character with ASCII value equal to the value in
		// the cell pointed at by the Memory Pointer.
		t.put(new OokTuple(Exclam, Period), OUT);
		// Ook! Ook? Move to the command following the matching Ook? Ook! if the
		// value in the cell pointed at by the Memory Pointer is zero. Note that
		// Ook! Ook? and Ook? Ook! commands nest like pairs of parentheses, and
		// matching pairs are defined in the same way as for parentheses.
		t.put(new OokTuple(Exclam, Question), BEGIN);
		// Ook? Ook! Move to the command following the matching Ook! Ook? if the
		// value in the cell pointed at by the Memory Pointer is non-zero.
		t.put(new OokTuple(Question, Exclam), END);

		tokens = Collections.unmodifiableMap(t);
	}
}
