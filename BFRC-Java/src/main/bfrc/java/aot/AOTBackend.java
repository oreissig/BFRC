package bfrc.java.aot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.CtClass;
import bfrc.ast.RootNode;
import bfrc.backend.Backend;

/**
 * This backend generates a Java class from the given AST and directly
 * executes the code in memory.
 * 
 * @author oreissig
 */
public class AOTBackend implements Backend {

	private static final String className = "Temp";
	private final JavassistHelper helper = new JavassistHelper();

	@Override
	public void work(RootNode root) {
		Method m;
		CtClass clazz = null;
		try {
			clazz = helper.create(className, root);
			Class<?> compiled = clazz.toClass();
			m = compiled.getMethod("main");
		} catch (CannotCompileException | NoSuchMethodException e) {
			throw new IllegalArgumentException("invalid class", e);
		} finally {
			if (clazz != null)
				clazz.detach();
		}
		// call static method
		try {
			m.invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException("exception calling compiled code", e);
		}
	}
}
