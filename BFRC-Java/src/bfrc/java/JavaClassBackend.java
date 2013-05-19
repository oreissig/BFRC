package bfrc.java;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class JavaClassBackend extends AbstractJavassistBackend<IOException> {

	public static final String main = "public static void main(String[] args) { main(); }";

	public JavaClassBackend(String className) {
		super(className);
	}

	@Override
	public void write(CtClass clazz) throws IOException {
		try {
			CtMethod m = CtNewMethod.make(main, clazz);
			clazz.addMethod(m);
			clazz.writeFile();
		} catch (NotFoundException | CannotCompileException e) {
			throw new IOException(e);
		}
	}

}
