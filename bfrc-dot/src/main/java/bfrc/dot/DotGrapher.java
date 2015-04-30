package bfrc.dot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Deque;

import bfrc.ast.AbstractTreeVisitor;
import bfrc.ast.BlockNode;
import bfrc.ast.Node;
import bfrc.ast.RootNode;
import bfrc.backend.FileBackend;

/**
 * This backend creates a graph representation of the AST, that can be
 * rendered by a tool like <a href="http://www.graphviz.org/">Graphviz</a>.
 * 
 * @author oreissig
 * 
 * @see <a href="http://www.graphviz.org/content/dot-language">The DOT Language</a>
 */
public class DotGrapher extends AbstractTreeVisitor<IOException> implements FileBackend {
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
	public void before(RootNode root) throws IOException {
		out.write("digraph BFRC {\n");
	}

	@Override
	public void visit(Node node, Deque<BlockNode> stack) throws IOException {
		Node parent = stack.peek();
		if (parent != null)
			out.write("\t\"" + parent + "\" -> \"" + node + "\";\n");
	}

	@Override
	public void after(RootNode root) throws IOException {
		out.write("}");
		out.close();
	}
}
