package bfrc.interpreter.test;

import bfrc.ast.Node;
import bfrc.interpreter.Interpreter.InterpreterListener;

public class StepCounter implements InterpreterListener {

    // start at -1, therefore the root node is not counted
    private long steps = -1;

    @Override
    public void visit(Node n) {
        steps++;
    }

    /**
     * Return the number of AST nodes traversed (except the root node).
     * 
     * @return number of nodes traversed
     * @throws IllegalStateException
     *             if no program has been executed
     */
    public long getSteps() {
        if (steps < 0)
            throw new IllegalStateException("You must first execute some program.");
        return steps;
    }

    public void reset() {
        steps = -1;
    }
}
