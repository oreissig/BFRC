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
	private Writer out;

	@Override
	public void setOutput(String output) throws IOException {
		this.out = new FileWriter(output);
	}

	@Override
	public String getDefaultExtension() {
		return "dot";
	}

	@Override
	protected void before() throws IOException {
		out.write("digraph BFRC {\n");
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
