package bfrc.bf;

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

public class BFBackend extends AbstractTreeWalker<IOException> implements Backend {
	private final Writer out;

	public BFBackend(String fileName) throws IOException {
		this(new FileWriter(fileName + ".bf"));
	}

	public BFBackend(Writer w) {
		this.out = w;
	}

	@Override
	protected boolean visit(Node n, Deque<BlockNode> stack) throws IOException {
		switch (n.type) {
			case ROOT:
				// nothing to do here
				break;
			case LOOP:
				out.write('[');
				break;
			case INPUT:
				out.write(',');
				break;
			case OUTPUT:
				out.write('.');
				break;
			case POINTER:
				ChangeNode cn = (ChangeNode) n;
				char op;
				int count;
				if (cn.change >= 0) {
					op = '>';
					count = cn.change;
				} else {
					op = '<';
					count = -cn.change;
				}
				for (int i = 0; i < count; i++)
					out.write(op);
				break;
			case VALUE:
				cn = (ChangeNode) n;
				if (cn.change >= 0) {
					op = '+';
					count = cn.change;
				} else {
					op = '-';
					count = -cn.change;
				}
				for (int i = 0; i < count; i++)
					out.write(op);
				break;
			default:
				throw new UnsupportedOperationException("unexpected type: "
						+ n.type);
		}

		return true;
	}

	@Override
	protected void leave(BlockNode block, Deque<BlockNode> stack)
			throws IOException {
		if (block.type == NodeType.LOOP)
			out.write(']');
	}

	@Override
	protected void after() throws IOException {
		out.close();
	}
}
