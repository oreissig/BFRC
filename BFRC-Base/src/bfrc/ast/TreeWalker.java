package bfrc.ast;

public interface TreeWalker<E extends Exception> {

	public void work(Node root) throws E;
}
