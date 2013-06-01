package bfrc.interpreter;

import java.io.IOException;

import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.backend.Backend;

/**
 * A simple interpreter backend, that directly traverses the AST during
 * execution.
 * 
 * @author oreissig
 */
public class Interpreter implements Backend {

	private final byte[] mem = new byte[MEM_SIZE];
	private int p = 0;

	@Override
	public void work(Node n) throws IOException {
		switch (n.type) {
			case LOOP:
				BlockNode bn = (BlockNode) n;
				while (mem[p] != 0)
					for (Node sub : bn.sub)
						work(sub);
				break;
			case ROOT:
				bn = (BlockNode) n;
				for (Node sub : bn.sub)
					work(sub);
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
				mem[p] = (byte) System.in.read();
				break;
			case OUTPUT:
				System.out.write(mem[p]);
				break;
			default:
				throw new RuntimeException("unexpected node type: " + n.type);
		}
	}
}
