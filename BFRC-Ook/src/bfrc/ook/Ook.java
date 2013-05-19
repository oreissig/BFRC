package bfrc.ook;

import static bfrc.lexer.Token.TokenType.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bfrc.lexer.Token.TokenType;

public enum Ook {
	Period, Exclam, Question;

	public static final Map<OokTuple, TokenType> mapping;

	static {
		Map<OokTuple, TokenType> tmp = new HashMap<>(8);
		// Ook. Ook? Move the Memory Pointer to the next array cell.
		tmp.put(new OokTuple(Period, Question), RIGHT);
		// Ook? Ook. Move the Memory Pointer to the previous array cell.
		tmp.put(new OokTuple(Question, Period), LEFT);
		// Ook. Ook. Increment the array cell pointed at by the Memory Pointer.
		tmp.put(new OokTuple(Period, Period), PLUS);
		// Ook! Ook! Decrement the array cell pointed at by the Memory Pointer.
		tmp.put(new OokTuple(Exclam, Exclam), MINUS);
		// Ook. Ook! Read a character from STDIN and put its ASCII value into
		// the cell pointed at by the Memory Pointer.
		tmp.put(new OokTuple(Period, Exclam), IN);
		// Ook! Ook. Print the character with ASCII value equal to the value in
		// the cell pointed at by the Memory Pointer.
		tmp.put(new OokTuple(Exclam, Period), OUT);
		// Ook! Ook? Move to the command following the matching Ook? Ook! if the
		// value in the cell pointed at by the Memory Pointer is zero. Note that
		// Ook! Ook? and Ook? Ook! commands nest like pairs of parentheses, and
		// matching pairs are defined in the same way as for parentheses.
		tmp.put(new OokTuple(Exclam, Question), BEGIN);
		// Ook? Ook! Move to the command following the matching Ook! Ook? if the
		// value in the cell pointed at by the Memory Pointer is non-zero.
		tmp.put(new OokTuple(Question, Exclam), END);

		mapping = Collections.unmodifiableMap(tmp);
	}
}
