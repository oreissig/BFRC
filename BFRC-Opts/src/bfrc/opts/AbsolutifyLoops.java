package bfrc.opts;

import static bfrc.ast.NodeType.LOOP;
import static bfrc.ast.NodeType.VALUE;

import java.util.Deque;
import java.util.List;

import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.Node;
import bfrc.ast.ValueNode;
import bfrc.optimizer.Optimizer;
import bfrc.optimizer.OptimizerException;

public class AbsolutifyLoops extends AbstractTreeWalker<OptimizerException> implements Optimizer {

	@Override
	protected boolean enter(BlockNode block, Deque<BlockNode> stack)
			throws OptimizerException {
		// look for child elements: [-] or [+]
		for (int i = 0; i < block.sub.size(); i++) {
			Node n = block.sub.get(i);

			if (n.type != LOOP)
				continue;

			List<Node> sub = ((BlockNode) n).sub;
			if (sub.size() != 1)
				continue;

			Node op = sub.get(0);
			if (op.type != VALUE)
				continue;

			// replace block by absolute value
			Node abs = new ValueNode(n.position, 0, true);
			block.sub.set(i, abs);
		}
		return true;
	}
}
