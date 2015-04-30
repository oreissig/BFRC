package bfrc.ast;

import java.util.List;

/**
 * This builder class can be used to create ASTs for testing purposes without
 * relying on a concrete brainfuck parser. Use {@link #begin()} to start.
 * 
 * @author oreissig
 */
public class TestAST {

	// forbid instantiation
	private TestAST() {
	}

	public static BlockBuilder<RootNode> begin() {
		RootNode root = new RootNode();
		return new BlockBuilder<>(root, root);
	}

	public static class BlockBuilder<R> {

		private final List<Node> nodes;
		private final R result;

		BlockBuilder(BlockNode block, R result) {
			this.nodes = block.sub;
			this.result = result;
		}

		public R end() {
			return result;
		}

		public BlockBuilder<R> change(int relative) {
			nodes.add(new ValueNode(0, 0, relative));
			return this;
		}

		public BlockBuilder<R> set(int absolute) {
			nodes.add(new ValueNode(0, 0, absolute, true));
			return this;
		}

		public BlockBuilder<R> move(int steps) {
			nodes.add(new PointerNode(0, 0, steps));
			return this;
		}

		public BlockBuilder<R> read() {
			nodes.add(new InputNode(0, 0));
			return this;
		}

		public BlockBuilder<R> write() {
			nodes.add(new OutputNode(0, 0));
			return this;
		}

		public BlockBuilder<BlockBuilder<R>> loop() {
			LoopNode loop = new LoopNode(0, 0);
			nodes.add(loop);
			return new BlockBuilder<>(loop, this);
		}
	}
}
