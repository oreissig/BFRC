package bfrc.opts;

import java.util.Deque;

import bfrc.ast.AbstractTreeWalker;
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
public class RemoveNops extends AbstractTreeWalker<OptimizerException>
		implements Optimizer {

	@Override
	protected boolean visit(Node n, Deque<BlockNode> stack) {
		if (n instanceof ChangeNode) {
			ChangeNode cn = (ChangeNode) n;
			if (!cn.absolute && cn.change == 0)
				return false;
		}
		return true;
	}
}
