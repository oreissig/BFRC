package bfrc.opts

import static bfrc.ast.NodeType.*
import spock.lang.Specification
import bfrc.ast.ChangeNode
import bfrc.ast.RootNode
import bfrc.ast.TestAST
import bfrc.optimizer.Optimizer

class ConstantEvaluationSpec extends Specification {

	Optimizer opt = new ConstantEvaluation()
	
	def 'programs without inputs are transformed completely'() {
		given:
		RootNode ast = TestAST.begin()
							  // 5*5
							  .change(5).loop().move(1).change(5).move(-1).change(-1).end()
							  // print it
							  .move(1).write()
							  // some dummy ops
							  .move(5).change(-5).end()

		when:
		opt.work(ast)

		then:
		ast.sub*.type    == [VALUE, OUTPUT]
		changes(ast.sub) == [25   , null  ]
	}
	
	def 'completely dynamic programs are not touched'() {
		given:
		RootNode ast = TestAST.begin().read().change(3).write().end()

		when:
		opt.work(ast)

		then:
		ast.sub*.type    == [INPUT, VALUE, OUTPUT]
		changes(ast.sub) == [null , 3    , null  ]
	}
	
	def 'Execution can be aborted, retaining already evaluated data'() {
		given:
		RootNode ast = TestAST.begin()
		                      // a write before
		                      .change(3).change(2).write()
							  // set up some state
							  .change(-4).move(4).move(-2).set(3).move(-2)
							  // infinite loop
							  .loop().change(0).end()
							  // some code afterwards
							  .move(1).change(6).write().end()

		when:
		opt.work(ast)

		then:
		                    /* outputs    | restore state                 | rest of the source program */
		ast.sub*.type    == [VALUE, OUTPUT, VALUE, POINTER, VALUE, POINTER, LOOP, POINTER, VALUE, OUTPUT]
		changes(ast.sub) == [5    , null  , 1    , 2      , 3    , -2     , null, 1      , 6    , null  ]
	}
	
	private List changes(List<Node> nodes) {
		nodes.collect {
			it instanceof ChangeNode ? it.change : null
		}
	}
}
