package bfrc.optimizer;

import bfrc.ast.RootNode;

/**
 * Common interface for all BFRC optimizations.
 * 
 * @author oreissig
 */
public interface Optimizer {

	/**
	 * Performs optimization of the program given by the AST.
	 * 
	 * @param root of AST
	 */
	public void work(RootNode root) throws OptimizerException;
}
