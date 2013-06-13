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

/**
 * This optimization replaces loops, that just contain one VALUE node with an
 * odd {@link ChangeNode#change change} by the {@link ChangeNode#absolute
 * absolute} VALUE of 0.
 * <p>
 * For example, {@code [-]} will become {@code =0}.
 * <p>
 * This must be applied after {@link FoldMultiOps} has been performed.
 * If {@link RemoveRedundantLoops} is being run, it should also be performed
 * in beforehand.
 * 
 * @author oreissig
 */
public class AbsolutifyLoops extends AbstractTreeWalker<OptimizerException>
		implements Optimizer {

	@Override
	protected void leave(BlockNode block, Deque<BlockNode> stack)
			throws OptimizerException {
		// look for child elements like [-] or [+]
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

			ValueNode vn = (ValueNode) op;
			// no relative change || absolute change to != 0 => infinite loop
			if ((!vn.absolute && vn.change == 0) ||
				(vn.absolute && vn.change != 0))
				throw new OptimizerException("loop at " + n.position() +
						" can never end");
			// even relative loop increments might never end
			if (!vn.absolute && vn.change % 2 == 0) {
				System.err.println("loop at " + n.position() +
						" may never end, did not optimize");
				continue;
			}

			// replace block by absolute value
			Node abs = new ValueNode(n.line, n.offset, 0, true);
			block.sub.set(i, abs);
		}
	}
}
