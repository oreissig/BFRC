package bfrc.opts;

import java.util.Deque;
import java.util.Iterator;

import bfrc.ast.AbstractTreeVisitor;
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
public class FoldMultiOps extends AbstractTreeVisitor<OptimizerException>
		implements Optimizer {

	@Override
	public void enter(BlockNode block, Deque<BlockNode> stack) {
		ChangeNode previous = null;

		Iterator<Node> nodes = block.sub.iterator();
		while (nodes.hasNext()) {
			Node n = nodes.next();
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
					nodes.remove();
				} else {
					// first of its kind
					previous = cn;
				}
			} else {
				// different type, reset
				previous = null;
			}
		}
	}
}
