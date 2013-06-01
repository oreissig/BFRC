package bfrc.java.jit;

import static javassist.Modifier.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.backend.Backend;

/**
 * A JIT compiler implementation, that works on a per-block (loop) level.
 * Whenever a loop is entered for the first time, the generated code calls back
 * the compiler to create the requested block. The compiled code is then saved
 * so that it will not need to be built again.
 * 
 * The generated Java code is not intended to be human-readable.
 * 
 * @author oreissig
 */
public class JITBackend extends JITContext implements Backend {

	private static final String className = "Block";
	private final List<BlockNode> blocks = new ArrayList<>();
	private final ClassPool cp = ClassPool.getDefault();
	private final CtClass blockType;
	private final CtClass[] paramTypes;

	public JITBackend() {
		try {
			cp.importPackage(JITContext.class.getPackage().getName());
			blockType = cp.get(JITBlock.class.getName());
			paramTypes = new CtClass[] { cp.get(JITContext.class.getName()) };
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void work(Node root) {
		if (root instanceof BlockNode) {
			blocks.add((BlockNode) root);
			JITBlock main = compile(0);
			try {
				main.call(this);
			} catch (Throwable t) {
				throw new RuntimeException("Exception executing generated code", t);
			}
		}
	}

	@Override
	public JITBlock compile(int blockID) {
		int blocksBefore = blocks.size();
		BlockNode block = blocks.get(blockID);
		String method = buildMethod(block);
		int blocksAfter = blocks.size();

		Collection<String> newBlocks = new ArrayList<>(blocksAfter - blocksBefore);
		for (int i=blocksBefore; i<blocksAfter; i++)
			newBlocks.add("b" + i);

		Class<? extends JITBlock> clazz = buildClass(className + 'L' + block.line + "P" + block.offset,
				newBlocks, method);
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private String buildMethod(BlockNode block) {
		StringBuilder body = new StringBuilder();
		for (Node n : block.sub) {
			switch (n.type) {
			case LOOP:
				BlockNode nested = (BlockNode) n;
				int id = add(nested);
				body.append("if($1.mem[$1.ptr]!=0){")
					.append("if(b" + id + "==null)b" + id + "=$1.compile(" + id + ");")
					.append("while(b" + id + ".call($1)!=0);")
					.append("}");
				break;
			case VALUE:
				ChangeNode cn = (ChangeNode) n;
				if (cn.absolute)
					body.append("$1.mem[$1.ptr]=" + cn.change + ";");
				else
					body.append("$1.mem[$1.ptr]+=" + cn.change + ";");
				break;
			case POINTER:
				cn = (ChangeNode) n;
				body.append("$1.ptr+=" + cn.change + ";");
				break;
			case INPUT:
				body.append("$1.mem[$1.ptr]=System.in.read();");
				break;
			case OUTPUT:
				body.append("System.out.print((char)$1.mem[$1.ptr]);");
				break;
			default:
				throw new RuntimeException("unexpected node: " + n.type);
			}
		}
		body.append("return $1.mem[$1.ptr];");
		return body.toString();
	}

	private synchronized int add(BlockNode block) {
		int blockID = blocks.size();
		blocks.add(block);
		return blockID;
	}

	@SuppressWarnings("unchecked")	// it really creates JITCallable classes
	private Class<? extends JITBlock> buildClass(String className,
			Collection<String> fields, String code) {
		try {
			CtClass c = cp.makeClass(className);
			c.addInterface(blockType);

			for (String fieldName : fields) {
				CtField f = new CtField(blockType, fieldName, c);
				c.addField(f);
			}

			CtMethod m = CtNewMethod.make(PUBLIC | FINAL, CtClass.intType,
					"call", paramTypes, null, '{' + code + '}', c);
			c.addMethod(m);

			// TODO optionally write temp classes or source code to disk
			try {
				c.writeFile();
			} catch (NotFoundException | IOException e) {
				e.printStackTrace();
			}
			return c.toClass();
		} catch (CannotCompileException e) {
			throw new RuntimeException("could not compile " + className +
					fields.toString() + ": " + code, e);
		}
	}
}
