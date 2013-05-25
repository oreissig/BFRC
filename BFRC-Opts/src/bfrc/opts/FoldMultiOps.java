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
		ChangeNode last = null;

		List<Node> nodes = block.sub;
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (n instanceof ChangeNode) {
				ChangeNode cn = (ChangeNode) n;
				// do not merge absolute ChangeNodes
				if (cn.absolute) {
					last = null;
					continue;
				}
				
				if (last != null && cn.type == last.type) {
					// merge nodes
					last.change += cn.change;
					nodes.remove(i);
					// mind the reduced node count
					i--;
				} else {
					// first of its kind
					last = cn;
				}
			} else {
				// different type, reset
				last = null;
			}
		}

		return true;
	}
}
