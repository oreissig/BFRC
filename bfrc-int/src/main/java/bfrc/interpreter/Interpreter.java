package bfrc.interpreter;

import java.io.IOException;

import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.ast.RootNode;
import bfrc.backend.Backend;

/**
 * A simple interpreter backend, that directly traverses the AST during
 * execution.
 * 
 * @author oreissig
 */
public class Interpreter implements Backend {

	private final byte[] mem = new byte[MEM_SIZE];
	private final InputOutput io;
	private int p = 0;

	public Interpreter() {
		this(new ConsoleIO());
	}

	public Interpreter(InputOutput io) {
		this.io = io;
	}

	@Override
	public void work(RootNode root) {
		for (Node n : root.sub)
			visit(n);
	}

	/**
	 * This method implements a custom traversion mechanism to handle repeated
	 * loop execution.
	 * 
	 * @param n node to visit
	 */
	private void visit(Node n) {
		try {
			switch (n.type) {
				case LOOP:
					BlockNode block = (BlockNode) n;
					while (mem[p] != 0)
						for (Node child : block.sub)
							visit(child);
					break;
				case VALUE:
					ChangeNode cn = (ChangeNode) n;
					if (cn.absolute)
						mem[p] = (byte) cn.change;
					else
						mem[p] += cn.change;
					break;
				case POINTER:
					cn = (ChangeNode) n;
					p += cn.change;
					break;
				case INPUT:
					mem[p] = read();
					break;
				case OUTPUT:
					write(mem[p]);
					break;
				default:
					throw new RuntimeException("unexpected node type: " + n.type);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error interpreting " + n, e);
		}
	}

	void write(byte value) throws IOException {
		io.write(value);
	}

	byte read() throws IOException {
		return io.read();
	}
}
