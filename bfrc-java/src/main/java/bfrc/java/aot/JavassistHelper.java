package bfrc.java.aot;

import java.util.Deque;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import bfrc.ast.AbstractTreeVisitor;
import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.ast.RootNode;
import bfrc.backend.Backend;

/**
 * This helper class generates a Java class, whose only method contains the
 * program represented by the given AST.
 * The generated Java code is not intended to be human-readable.
 * 
 * @author oreissig
 */
class JavassistHelper extends AbstractTreeVisitor<CannotCompileException> {

	private StringBuilder body;
	private CtClass c;

	public synchronized CtClass create(String className, RootNode root)
			throws CannotCompileException {
		ClassPool cp = ClassPool.getDefault();
		c = cp.makeClass(className);

		work(root);

		CtClass result = c;
		c = null;
		return result;
	}

	@Override
	public void before(RootNode root) {
		body = new StringBuilder("public static int main()");
	}

	@Override
	public void after(RootNode root) throws CannotCompileException {
		try {
			CtMethod m = CtNewMethod.make(body.toString(), c);
			c.addMethod(m);
		} finally {
			body = null;
		}
	}

	@Override
	public void visit(Node node, Deque<BlockNode> stack) {
		switch (node.type) {
			case ROOT:
				body.append("{")
				    .append("byte[] mem=new byte[" + Backend.MEM_SIZE + "];")
				    .append("int ptr=0;");
				break;
			case LOOP:
				body.append("while(mem[ptr]!=0){");
				break;
			case VALUE:
				ChangeNode cn = (ChangeNode) node;
				if (cn.absolute)
					body.append("mem[ptr]=" + cn.change + ";");
				else
					body.append("mem[ptr]" + varChange(cn.change) + ";");
				break;
			case POINTER:
				cn = (ChangeNode) node;
				body.append("ptr" + varChange(cn.change) + ";");
				break;
			case INPUT:
				body.append("mem[ptr]=System.in.read();");
				break;
			case OUTPUT:
				body.append("System.out.print((char)mem[ptr]);");
				break;
			default:
				throw new RuntimeException("unexpected node: " + node.type);
		}
	}

	@Override
	public void leave(BlockNode node, Deque<BlockNode> stack) {
		if (node.type == NodeType.ROOT)
			body.append("return mem[ptr];");
		body.append("}");
	}

	private static String varChange(int change) {
		if (change > 0) {
			if (change == 1)
				return "++";
			else
				return "+=" + change;
		} else if (change < 0) {
			int abs = -change;
			if (abs == 1)
				return "--";
			else
				return "-=" + abs;
		} else
			return ""; // no change
	}
}
