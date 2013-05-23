package bfrc.java;

import java.util.Deque;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import bfrc.ast.AbstractTreeWalker;
import bfrc.ast.BlockNode;
import bfrc.ast.Node;
import bfrc.ast.NodeType;
import bfrc.ast.PointerNode;
import bfrc.ast.ValueNode;

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
				ValueNode vn = (ValueNode) node;
				body.append("mem[ptr]+=" + vn.change + ";");
				break;
			case POINTER:
				PointerNode pn = (PointerNode) node;
				body.append("ptr+=" + pn.change + ";");
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
}
