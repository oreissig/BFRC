package bfrc.java.jit;

import java.io.IOException;

import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;

final class InterpretedJITBlock implements JITBlock {

	private final BlockNode block;

	public InterpretedJITBlock(BlockNode block) {
		this.block = block;
	}

	@Override
	public void call(JITContext c) throws IOException {
		for (Node n : block.sub) {
			switch (n.type) {
				case LOOP:
					// only compiled blocks can compile sub-blocks
					BlockNode bn = (BlockNode) n;
					JITBlock block = new InterpretedJITBlock(bn);
					block.call(c);
					break;
				case VALUE:
					ChangeNode cn = (ChangeNode) n;
					if (cn.absolute)
						c.mem[c.ptr] = (byte) cn.change;
					else
						c.mem[c.ptr] += cn.change;
					break;
				case POINTER:
					cn = (ChangeNode) n;
					c.ptr += cn.change;
					break;
				case INPUT:
					c.mem[c.ptr] = (byte) System.in.read();
					break;
				case OUTPUT:
					System.out.write(c.mem[c.ptr]);
					break;
				default:
					throw new RuntimeException("unexpected node type: " + n.type);
			}
		}
	}
}
