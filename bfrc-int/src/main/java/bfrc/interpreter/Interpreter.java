package bfrc.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;

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

	protected final byte[] mem = new byte[MEM_SIZE];
	protected int p = 0;
	protected final InputOutput io;
	/**
	 * The execution stack of the program contains loop nodes.
	 */
	protected final Deque<Node> stack = new ArrayDeque<>();

	public Interpreter() {
		this(new ConsoleIO());
	}

	public Interpreter(InputOutput io) {
		this.io = io;
	}

	@Override
	public void work(RootNode root) throws InterruptedException {
		for (Node n : root.sub)
			visit(n);
	}

	/**
	 * This method implements a custom traversion mechanism to handle repeated
	 * loop execution.
	 * 
	 * @param n node to visit
	 * @throws InterruptedException 
	 */
	protected void visit(Node n) throws InterruptedException {
		try {
			switch (n.type) {
				case LOOP:
					stack.push(n);
					BlockNode block = (BlockNode) n;
					while (mem[p] != 0) {
						if (Thread.interrupted())
							throw new InterruptedException();
						for (Node child : block.sub)
							visit(child);
					}
					stack.pop();
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
					mem[p] = io.read();
					break;
				case OUTPUT:
					io.write(mem[p]);
					break;
				default:
					throw new RuntimeException("unexpected node type: " + n.type);
			}
		} catch (Exception e) {
			RuntimeException ex = new RuntimeException("Error interpreting " + n, e);
			// set custom stack trace to point to the actual source program
			ex.setStackTrace(getStack());
			throw ex;
		}
	}
	
	private StackTraceElement[] getStack() {
		return stack.stream().map(n -> new StackTraceElement("Brainfuck", "main",
										Integer.toString(n.line), n.offset))
							 .toArray(StackTraceElement[]::new);
	}
}
