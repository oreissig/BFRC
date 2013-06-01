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
import bfrc.backend.FileBackend;

/**
 * This is a backend for creating Brainfuck source code from the given AST.
 * 
 * @author oreissig
 */
public class BFBackend extends AbstractTreeWalker<IOException> implements FileBackend {
	private Writer out;

	@Override
	public void setOutput(String output) throws IOException {
		this.out = new FileWriter(output);
	}

	@Override
	public String getDefaultExtension() {
		return "bf";
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
				count = cn.change;
				if (cn.absolute) {
					// set to 0
					out.write("[-]");
					// shorten wrap-arounds
					while (count > 255)
						count -= 256;
					while (count < 0)
						count += 256;
					// take the shorter route
					if (count > 128)
						count -= 256;
				}
				
				if (count >= 0) {
					op = '+';
				} else {
					op = '-';
					count = -count;
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
