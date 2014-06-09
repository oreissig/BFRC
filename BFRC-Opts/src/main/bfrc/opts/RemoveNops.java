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
 * This optimization removes relative {@link ChangeNode ChangeNodes}
 * (POINTER, VALUE), whose {@link ChangeNode#change change} is zero.
 * <p>
 * This must be applied after {@link FoldMultiOps} has been performed.
 * 
 * @author oreissig
 */
public class RemoveNops extends AbstractTreeVisitor<OptimizerException>
		implements Optimizer {

	@Override
	public void leave(BlockNode block, Deque<BlockNode> stack) {
		Iterator<Node> i = block.sub.iterator();
		while (i.hasNext()) {
			Node n = i.next();
			if (n instanceof ChangeNode) {
				ChangeNode cn = (ChangeNode) n;
				if (!cn.absolute && cn.change == 0)
					i.remove();
			}
		}
	}
}
