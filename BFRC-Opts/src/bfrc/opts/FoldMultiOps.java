package bfrc.opts;

import java.util.Deque;
import java.util.List;

import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.optimizer.Optimizer;
import bfrc.optimizer.OptimizerException;

public class FoldMultiOps extends AbstractTreeWalker<OptimizerException>
		implements Optimizer {

	@Override
	protected boolean enter(BlockNode block, Deque<BlockNode> stack)
			throws OptimizerException {
		ChangeNode last = null;

		List<Node> nodes = block.sub;
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (n instanceof ChangeNode) {
				if (last != null && n.type == last.type) {
					// merge nodes
					last.change += ((ChangeNode) n).change;
					nodes.remove(i);
					// mind the reduced node count
					i--;
				} else {
					// first of its kind
					last = (ChangeNode) n;
				}
			} else {
				// different type, reset
				last = null;
			}
		}

		return true;
	}
}
