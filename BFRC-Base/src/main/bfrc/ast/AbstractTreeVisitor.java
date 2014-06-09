package bfrc.ast;

import java.util.Deque;

public abstract class AbstractTreeVisitor<E extends Exception> implements TreeVisitor<E> {

	public final void work(RootNode root) throws E {
		root.visit(this);
	}

	@Override
	public void visit(Node n, Deque<BlockNode> stack) throws E {
		// do nothing per default
	}

	@Override
	public void enter(BlockNode block, Deque<BlockNode> stack) throws E {
		// do nothing per default
	}

	@Override
	public void leave(BlockNode block, Deque<BlockNode> stack) throws E {
		// do nothing per default
	}

	@Override
	public void before(RootNode root) throws E {
		// do nothing per default
	}

	@Override
	public void after(RootNode root) throws E {
		// do nothing per default
	}
}
