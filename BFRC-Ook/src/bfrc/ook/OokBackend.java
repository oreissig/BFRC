package bfrc.ook;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Deque;

import bfrc.ast.AbstractTreeVisitor;
import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.ast.RootNode;
import bfrc.backend.FileBackend;

/**
 * This is a backend for creating Ook source code from the given AST.
 * 
 * @author oreissig
 */
public class OokBackend extends AbstractTreeVisitor<IOException> implements FileBackend {
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
	public void visit(Node n, Deque<BlockNode> stack) throws IOException {
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
				count = cn.change;
				if (cn.absolute) {
					// set to 0
					out.write("Ook! Ook? Ook! Ook! Ook? Ook! ");
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
					op = "Ook. Ook. ";
				} else {
					op = "Ook! Ook! ";
					count = -count;
				}
				for (int i = 0; i < count; i++)
					out.write(op);
				break;
			default:
				throw new UnsupportedOperationException("unexpected type: "
						+ n.type);
		}
	}

	@Override
	public void leave(BlockNode block, Deque<BlockNode> stack)
			throws IOException {
		if (block.type == NodeType.LOOP)
			out.write("Ook? Ook! ");
	}

	@Override
	public void after(RootNode root) throws IOException {
		out.close();
	}
}
