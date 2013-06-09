package bfrc.opts;

import java.util.Deque;
import java.util.List;

import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.optimizer.Optimizer;
import bfrc.optimizer.OptimizerException;

/**
 * This optimization merges consecutive absolute and relative {@link ChangeNode
 * ChangeNodes} (VALUE, POINTER).
 * <p>
 * For example, {@code -3 =0 +2} will result in {@code =2}.
 * <p>
 * This should be applied after {@link AbsolutifyLoops} has been performed.
 * 
 * @author oreissig
 */
public class FuseAbsoluteOps extends AbstractTreeWalker<OptimizerException>
		implements Optimizer {

	@Override
	protected boolean enter(BlockNode block, Deque<BlockNode> stack) {
		ChangeNode previous = null;
		int prevIndex = -1;

		List<Node> nodes = block.sub;
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (n instanceof ChangeNode) {
				ChangeNode cn = (ChangeNode) n;

				if (previous != null && cn.type == previous.type) {
					// match, merge them
					if (cn.absolute) {
						// replace previous, as its effect is superseded
						nodes.remove(prevIndex);
						previous = cn;
						prevIndex = i;
					} else {
						// merge relative change to last
						previous.change += cn.change;
						nodes.remove(i);
					}
					// mind the reduced node count
					i--;
				} else {
					// first of its kind
					previous = cn;
					prevIndex = i;
				}
			} else {
				// different type, reset
				previous = null;
				prevIndex = -1;
			}
		}

		return true;
	}
}
