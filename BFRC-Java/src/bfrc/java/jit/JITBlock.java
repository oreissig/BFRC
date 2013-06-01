package bfrc.java.jit;

/**
 * Interface for all classes generated by the JIT compiler.
 * 
 * @author oreissig
 */
public interface JITBlock {

	public int call(final JITContext c);
}
