package bfrc.dot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Deque;

import bfrc.ast.AbstractTreeVisitor;
import bfrc.ast.BlockNode;
import bfrc.ast.Node;
import bfrc.backend.Backend;

public class DotGrapher extends AbstractTreeVisitor<IOException> implements Backend {
	private final Writer out;
	private final String graphName;

	public DotGrapher(String fileName) throws IOException {
		this(new FileWriter(fileName + ".dot"), fileName);
	}

	public DotGrapher(Writer w, String graphName) {
		this.out = w;
		this.graphName = graphName;
	}

	public void write(BlockNode ast) throws IOException {
		out.write("digraph " + graphName + " {\n");
		work(ast);
		out.write("}");
		out.close();
	}

	@Override
	protected void visit(Node node, Deque<BlockNode> stack) throws IOException {
		Node parent = stack.peek();
		if (parent != null)
			out.write("\t\"" + parent + "\" -> \"" + node + "\";\n");
	}
}
