package bfrc.interpreter.test;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.interpreter.Interpreter.InterpreterListener;

public class CodeCoverage implements InterpreterListener {

    private final Set<Node> visited = new HashSet<>();

    @Override
    public void visit(Node n) {
        if (n.type == NodeType.ROOT)
            visited.clear();
        else
            visited.add(n);
    }

    public Set<Node> getVisitedNodes() {
        return ImmutableSet.copyOf(visited);
    }
}
