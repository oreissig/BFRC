package bfrc.interpreter

import spock.lang.Specification
import bfrc.ast.RootNode
import bfrc.ast.TestAST

class InterpreterSpec extends Specification {

	final TestInterpreter i = new TestInterpreter()

	def 'an empty program does nothing'() {
		given:
		RootNode n = TestAST.begin().end()

		when:
		i.work(n)

		then:
		i.outputs.empty
	}

	def 'change and set operations work as expected'() {
		given:
		RootNode n = TestAST.begin().change(42).set(21).change(2).write().end()

		when:
		i.work(n)

		then:
		i.outputs == [23]
	}

	def 'input and output operations work as expected'() {
		given:
		RootNode n = TestAST.begin().read().change(1).write().end()

		when:
		i.inputs = [42]
		i.work(n)

		then:
		i.outputs == [43]
		i.inputs.empty
	}

	def 'move operations work as expected'() {
		given:
		RootNode n = TestAST.begin().set(1).move(1).set(2).move(2).set(3)
				.move(-3).write().move(1).write().move(2).write().end()

		when:
		i.work(n)

		then:
		i.outputs == [1, 2, 3]
	}

	def 'a loop can run its body multiple times'() {
		given:
		RootNode n = TestAST.begin().set(3).loop().write().change(-1).end().end()

		when:
		i.work(n)

		then:
		i.outputs == [3, 2, 1]
	}

	def 'a loop can skip its body'() {
		given:
		RootNode n = TestAST.begin().set(1).loop().write().set(0).end()
				.loop().write().end().end()

		when:
		i.work(n)

		then:
		i.outputs == [1]
	}
}
