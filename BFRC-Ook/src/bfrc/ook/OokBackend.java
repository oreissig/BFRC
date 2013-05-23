package bfrc.ook;

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

public class OokBackend extends AbstractTreeWalker<IOException> implements Backend {
	private Writer out;

	@Override
	public void setOutput(String output) throws IOException {
		this.out = new FileWriter(output);
	}

	@Override
	public String getDefaultExtension() {
		return "ook";
	}

	@Override
	protected boolean visit(Node n, Deque<BlockNode> stack) throws IOException {
		switch (n.type) {
			case ROOT:
				// nothing to do here
				break;
			case LOOP:
				out.write("Ook! Ook? ");
				break;
			case INPUT:
				out.write("Ook. Ook! ");
				break;
			case OUTPUT:
				out.write("Ook! Ook. ");
				break;
			case POINTER:
				ChangeNode cn = (ChangeNode) n;
				String op;
				int count;
				if (cn.change >= 0) {
					op = "Ook. Ook? ";
					count = cn.change;
				} else {
					op = "Ook? Ook. ";
					count = -cn.change;
				}
				for (int i = 0; i < count; i++)
					out.write(op);
				break;
			case VALUE:
				cn = (ChangeNode) n;
				if (cn.change >= 0) {
					op = "Ook. Ook. ";
					count = cn.change;
				} else {
					op = "Ook! Ook! ";
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
			out.write("Ook? Ook! ");
	}

	@Override
	protected void after() throws IOException {
		out.close();
	}
}
