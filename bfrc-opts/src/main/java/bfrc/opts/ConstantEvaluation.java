package bfrc.opts;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bfrc.ast.Node;
import bfrc.ast.OutputNode;
import bfrc.ast.PointerNode;
import bfrc.ast.RootNode;
import bfrc.ast.ValueNode;
import bfrc.interpreter.InputOutput;
import bfrc.interpreter.Interpreter;
import bfrc.optimizer.Optimizer;
import bfrc.optimizer.OptimizerException;

public class ConstantEvaluation implements Optimizer {

	// 2M is large enough for 99 bottles of beer :-)
	final static long THRESHOLD = 2000000;
	final Logger log = LoggerFactory.getLogger(getClass());

	private final ConstantInterpreter interpreter;
	private final ConstantInputOutput io;
	private byte[] savedMem;
	private int savedPointer;

	public ConstantEvaluation() {
		io = new ConstantInputOutput();
		interpreter = new ConstantInterpreter(io);
	}

	@Override
	public void work(RootNode root) throws OptimizerException {
		final int blockSize = root.sub.size();
		int nodeNo = 0;
		List<Node> newProgram = new ArrayList<>();

		// interpret as far as possible
		try {
			while (nodeNo < blockSize) {
				savedMem = interpreter.getMemory();
				savedPointer = interpreter.getPointer();
				interpreter.visit(root.sub.get(nodeNo));
				// success if it didn't throw
				nodeNo++;
				// record outputs
				for (byte out : io.getNewWrites()) {
					newProgram.add(new ValueNode(-1, -1, out, true));
					newProgram.add(new OutputNode(-1, -1));
				}
			}
		} catch (Exception e) {
			if (e.getCause() != null)
				e = (Exception) e.getCause();
			log.info("Aborting constant evaluation", e);
			// restore state of when the interpretation was aborted
			int lastCell = 0;
			for (int i = 0; i < savedMem.length; i++) {
				byte value = savedMem[i];
				if (value != 0) {
					if (i != lastCell) {
						// aggregate moves over empty cells
						PointerNode move = new PointerNode(-1, -1, i - lastCell);
						newProgram.add(move);
					}
					ValueNode set = new ValueNode(-1, -1, value, true);
					newProgram.add(set);
					lastCell = i;
				}
			}
			if (savedPointer - lastCell != 0) {
				PointerNode restorePointer = new PointerNode(-1, -1,
						savedPointer - lastCell);
				newProgram.add(restorePointer);
			}
			// copy&paste following nodes
			List<Node> rest = root.sub.subList(nodeNo, blockSize);
			newProgram.addAll(rest);
		}

		root.sub.clear();
		root.sub.addAll(newProgram);
	}

	protected class ConstantInterpreter extends Interpreter {
		private long execCount = 0;

		public ConstantInterpreter(InputOutput io) {
			super(io);
		}

		@Override
		public void visit(Node n) throws InterruptedException {
			if (execCount++ > THRESHOLD) {
				throw new AbortEvaluation("execution threshold has been reached");
			}
			super.visit(n);
		}

		public byte[] getMemory() {
			return mem.clone();
		}

		public int getPointer() {
			return p;
		}
	}

	protected static class ConstantInputOutput implements InputOutput {
		private List<Byte> writes = new ArrayList<>();

		@Override
		public void write(byte value) {
			writes.add(value);
		}

		@Override
		public byte read() {
			throw new AbortEvaluation("input encountered");
		}

		public List<Byte> getNewWrites() {
			List<Byte> newWrites = writes;
			writes = new ArrayList<>();
			return newWrites;
		}
	}

	public static class AbortEvaluation extends RuntimeException {
		public AbortEvaluation(String string) {
			super(string);
		}

		@Override
		public Throwable fillInStackTrace() {
			// we don't need the stacktrace
			return this;
		}
	}
}
