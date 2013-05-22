package bfrc.opts;

import java.util.Deque;
import java.util.List;

import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.ast.PointerNode;
import bfrc.ast.ValueNode;
import bfrc.optimizer.Optimizer;
import bfrc.optimizer.OptimizerException;

public class FoldMultiOps extends AbstractTreeWalker<OptimizerException>
		implements Optimizer {

	@Override
	protected boolean enter(BlockNode block, Deque<BlockNode> stack)
			throws OptimizerException {
		if (block.sub.isEmpty())
			return false;

		Node last = block; // initialize with pointless block
		int change = 0;

		List<Node> nodes = block.sub;
		for (int i = 1; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (n instanceof ChangeNode) {
				if (n.type == last.type) {
					change += ((ChangeNode) n).change;
					// create combined node
					ChangeNode merged;
					switch (last.type) {
						case VALUE:
							merged = new ValueNode(last.position, change);
							break;
						case POINTER:
							merged = new PointerNode(last.position, change);
							break;
						default:
							throw new OptimizerException("unexpected type: "
									+ last.type);
					}
					// replace both nodes by combined node
					nodes.remove(i);
					nodes.set(i - 1, merged);
					// mind the reduced node count
					i--;
				} else {
					// first of its kind
					change = ((ChangeNode) n).change;
					last = n;
				}
			} else {
				// different type, reset
				last = n;
				change = 0;
			}
		}

		return true;
	}
}
