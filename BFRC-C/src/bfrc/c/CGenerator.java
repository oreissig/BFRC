package bfrc.c;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Deque;

import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.ValueNode;
import bfrc.ast.PointerNode;
import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.backend.Backend;

public class CGenerator extends AbstractTreeWalker<IOException> implements Backend {
	private final Writer out;

	public CGenerator(String fileName) throws IOException {
		this(new FileWriter(fileName + ".c"));
	}

	public CGenerator(Writer w) {
		this.out = w;
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
				out.write("\tchar array[9001];\n");
				indent(stack);
				out.write("\tchar *ptr = array;\n\n");
				break;
			case LOOP:
				out.write("while (*ptr) {\n");
				break;
			case POINTER:
				PointerNode pn = (PointerNode) node;
				out.write("ptr" + varChange(pn.change) + ";\n");
				break;
			case VALUE:
				ValueNode vn = (ValueNode) node;
				out.write("*ptr" + varChange(vn.change) + ";\n");
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
