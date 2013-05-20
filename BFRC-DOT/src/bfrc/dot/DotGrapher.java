package bfrc.dot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Deque;

import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.Node;
import bfrc.backend.Backend;

public class DotGrapher extends AbstractTreeWalker<IOException> implements Backend {
	private final Writer out;
	private final String graphName;

	public DotGrapher(String fileName) throws IOException {
		this(new FileWriter(fileName + ".dot"), fileName);
	}

	public DotGrapher(Writer w, String graphName) {
		this.out = w;
		this.graphName = graphName;
	}

	@Override
	protected void before() throws IOException {
		out.write("digraph " + graphName + " {\n");
	}

	@Override
	protected boolean visit(Node node, Deque<BlockNode> stack) throws IOException {
		Node parent = stack.peek();
		if (parent != null)
			out.write("\t\"" + parent + "\" -> \"" + node + "\";\n");
		return true;
	}

	@Override
	protected void after() throws IOException {
		out.write("}");
		out.close();
	}
}
