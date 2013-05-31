package bfrc.c;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Deque;

import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.backend.Backend;

public class CGenerator extends AbstractTreeWalker<IOException> implements Backend {
	private Writer out;

	@Override
	public void setOutput(String output) throws IOException {
		this.out = new FileWriter(output);
	}

	@Override
	public String getDefaultExtension() {
		return "c";
	}

	@Override
	protected void before() throws IOException {
		out.write("#include <stdio.h>\n\n");
	}

	@Override
	protected boolean visit(Node node, Deque<BlockNode> stack) throws IOException {
		indent(stack);

		switch (node.type) {
			case ROOT:
				out.write("int main() {\n");
				indent(stack);
				out.write("\tchar array[" + MEM_SIZE + "];\n");
				indent(stack);
				out.write("\tchar *ptr = array;\n\n");
				break;
			case LOOP:
				out.write("while (*ptr) {\n");
				break;
			case POINTER:
				ChangeNode cn = (ChangeNode) node;
				out.write("ptr" + varChange(cn.change) + ";\n");
				break;
			case VALUE:
				cn = (ChangeNode) node;
				if (cn.absolute)
					out.write("*ptr = " + cn.change + ";\n");
				else
					out.write("*ptr" + varChange(cn.change) + ";\n");
				break;
			case INPUT:
				out.write("*ptr = getchar();\n");
				break;
			case OUTPUT:
				out.write("putchar(*ptr);\n");
				break;
			default:
				throw new UnsupportedOperationException("unexpected node type "
						+ node.type);
		}
		return true;
	}

	@Override
	protected void leave(BlockNode node, Deque<BlockNode> stack)
			throws IOException {
		indent(stack);
		if (node.type == NodeType.ROOT)
			out.write("\n\treturn *ptr;\n");
		out.write("}\n");
	}

	@Override
	protected void after() throws IOException {
		out.close();
	}

	private void indent(Deque<BlockNode> stack) throws IOException {
		for (int i = 0; i < stack.size(); i++)
			out.write('\t');
	}

	private String varChange(int change) {
		if (change > 0) {
			if (change == 1)
				return "++";
			else
				return " += " + change;
		} else if (change < 0) {
			int abs = -change;
			if (abs == 1)
				return "--";
			else
				return " -= " + abs;
		} else
			return ""; // no change
	}
}
