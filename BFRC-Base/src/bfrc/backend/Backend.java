package bfrc.backend;

import java.io.IOException;

import bfrc.ast.BlockNode;

public interface Backend {
	public void write(BlockNode ast) throws IOException;
}
