package bfrc.interpreter.test;

import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.interpreter.Interpreter.InterpreterListener;

public class StepCounter implements InterpreterListener {

    private long steps;

    @Override
    public void visit(Node n) {
        // don't count root node
        if (n.type == NodeType.ROOT)
            steps = 0;
        else
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
}
