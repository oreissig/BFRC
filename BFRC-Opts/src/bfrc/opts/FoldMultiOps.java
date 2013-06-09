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
 * This optimization folds multiple relative operations of the same kind
 * (POINTER, VALUE) into one operation, that does more than one step.
 * <p>
 * Examples:
 * <ul>
 * <li>{@code + +} will result in a {@code +2}</li>
 * <li>{@code + + + -} will result in a {@code +2}</li>
 * <li>{@code > > < >} will result in a {@code 2>}</li>
 * </ul>
 * <p>
 * This optimization has no prerequisites.
 * 
 * @author oreissig
 */
public class FoldMultiOps extends AbstractTreeWalker<OptimizerException>
		implements Optimizer {

	@Override
	protected boolean enter(BlockNode block, Deque<BlockNode> stack) {
		ChangeNode previous = null;

		List<Node> nodes = block.sub;
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (n instanceof ChangeNode) {
				ChangeNode cn = (ChangeNode) n;
				// do not merge absolute ChangeNodes
				if (cn.absolute) {
					previous = null;
					continue;
				}

				if (previous != null && cn.type == previous.type) {
					// merge nodes
					previous.change += cn.change;
					nodes.remove(i);
					// mind the reduced node count
					i--;
				} else {
					// first of its kind
					previous = cn;
				}
			} else {
				// different type, reset
				previous = null;
			}
		}

		return true;
	}
}
