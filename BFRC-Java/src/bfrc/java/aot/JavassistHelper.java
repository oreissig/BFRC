package bfrc.java.aot;

import java.util.Deque;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.ChangeNode;
import bfrc.ast.Node;
import bfrc.ast.NodeType;

public class JavassistHelper extends AbstractTreeWalker<CannotCompileException> {

	private String className;
	private StringBuilder body;
	private CtClass c;

	public CtClass create(String className, Node root) throws CannotCompileException {
		this.className = className;
		work(root);
		
		CtClass result = c;
		c = null;
		return result;
	}

	@Override
	protected void before() {
		ClassPool cp = ClassPool.getDefault();
		c = cp.makeClass(className);
		body = new StringBuilder("public static int main()");
	}

	@Override
	protected void after() throws CannotCompileException {
		try {
			CtMethod m = CtNewMethod.make(body.toString(), c);
			c.addMethod(m);
		} finally {
			body = null;
		}
	}

	@Override
	protected boolean visit(Node node, Deque<BlockNode> stack) {
		switch (node.type) {
			case ROOT:
				body.append("{")
				    .append("byte[] mem=new byte[10000];")
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
		return true;
	}

	@Override
	protected void leave(BlockNode node, Deque<BlockNode> stack) {
		if (node.type == NodeType.ROOT)
			body.append("return mem[ptr];");
		body.append("}");
	}

	private String varChange(int change) {
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
