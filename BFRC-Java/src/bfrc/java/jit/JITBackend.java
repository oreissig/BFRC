package bfrc.java.jit;

import static javassist.Modifier.*;
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
import bfrc.ast.RootNode;
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
	private static final boolean writeClasses = true;
	private static final boolean trace = false;

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
	public void work(RootNode root) {
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
	public JITBlock interpret(int blockID) {
		if (trace)
			new Trace("interpret", blockID).printStackTrace();
		return new InterpretedJITBlock(blocks.get(blockID));
	}

	@Override
	public JITBlock compile(int blockID) {
		if (trace)
			new Trace("compile", blockID).printStackTrace();
		
		int blocksBefore = blocks.size();
		BlockNode block = blocks.get(blockID);
		String method = buildMethod(block);
		int blocksAfter = blocks.size();

		Collection<Integer> newBlocks = new ArrayList<>(blocksAfter - blocksBefore);
		for (int i=blocksBefore; i<blocksAfter; i++)
			newBlocks.add(i);

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
				body.append("if($1.mem[$1.ptr]!=0)")
				    .append("do{")
				    .append("switch(c" + id + "++){")
				    .append("case 0:b" + id + "=$1.interpret(" + id + ");break;")
				    .append("case " + COMPILE_THRESHOLD + ":b" + id + "=$1.compile(" + id + ");")
				    .append("}")
				    .append("b" + id + ".call($1);")
				    .append("}while($1.mem[$1.ptr]!=0);");
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
		return body.toString();
	}

	private synchronized int add(BlockNode block) {
		int blockID = blocks.size();
		blocks.add(block);
		return blockID;
	}

	@SuppressWarnings("unchecked")	// it really creates JITCallable classes
	private Class<? extends JITBlock> buildClass(String className,
			Collection<Integer> blockIDs, String code) {
		try {
			CtClass c = cp.makeClass(className);
			c.addInterface(blockType);

			for (int blockID : blockIDs) {
				CtField block = new CtField(blockType, "b" + blockID, c);
				c.addField(block);
				CtField count = new CtField(CtClass.byteType, "c" + blockID, c);
				c.addField(count);
			}

			CtMethod m = CtNewMethod.make(PUBLIC | FINAL, CtClass.voidType,
					"call", paramTypes, null, '{' + code + '}', c);
			c.addMethod(m);

			if (writeClasses)
				try {
					c.writeFile();
				} catch (Exception e) {
					e.printStackTrace(); // do not abort
				}

			Class<? extends JITBlock> built = c.toClass();
			c.detach();
			return built;
		} catch (CannotCompileException e) {
			throw new RuntimeException("could not compile " + className +
					blockIDs + ": " + code, e);
		}
	}
}
