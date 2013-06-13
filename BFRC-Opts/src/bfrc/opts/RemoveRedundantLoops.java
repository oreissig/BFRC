package bfrc.opts;

import java.util.Deque;

import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.optimizer.Optimizer;
import bfrc.optimizer.OptimizerException;

/**
 * This optimization removes pointless nesting of loops.
 * <p>
 * Example:
 * <ul>
 * <li>{@code [[.]]} will result in {@code [.]}</li>
 * </ul>
 * <p>
 * This optimization has no prerequisites.
 * 
 * @author oreissig
 */
public class RemoveRedundantLoops extends AbstractTreeWalker<OptimizerException>
		implements Optimizer {

	@Override
	protected void leave(BlockNode block, Deque<BlockNode> stack) {
		for (int i = 0; i < block.sub.size(); i++) {
			Node n = block.sub.get(i);
			if (n.type == NodeType.LOOP) {
				BlockNode superLoop = (BlockNode) n;
				if (superLoop.sub.size() == 1) {
					Node subLoop = superLoop.sub.get(0);
					if (subLoop.type == NodeType.LOOP)
						block.sub.set(i, subLoop);
				}
			}
		}
	}
}
