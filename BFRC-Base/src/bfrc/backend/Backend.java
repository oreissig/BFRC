package bfrc.backend;

import java.io.IOException;

import bfrc.ast.TreeWalker;

public interface Backend extends TreeWalker<IOException> {
}
