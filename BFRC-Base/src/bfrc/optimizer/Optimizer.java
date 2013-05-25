package bfrc.optimizer;

import bfrc.ast.TreeWalker;

/**
 * Common interface for all BFRC optimizations.
 * 
 * @author oreissig
 */
public interface Optimizer extends TreeWalker<OptimizerException> {
}
