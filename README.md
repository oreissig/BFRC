Brainfuck Research Compliler
============================

The project is designed as a tool for learning about compiler construction in an extremely simple way.
Although being kind of over-engineered for the purpose of compiling Brainfuck, this compiler features most of the architectural elements of a mature language compiler.
Nevertheless, its simplicity allows beginners to implement extensions in the course of an afternoon instead of weeks of work.

This project is licensed under an ISC license, see LICENSE file.

The repository contains several sub-projects:
* **BFRC-Base** defines the common API used by all other projects, including the Abstract Syntax Tree (AST) structure.
* **BFRC-Run** provides the runtime environment to execute the compiler.
* **BFRC-BF** includes the default Brainfuck frontend (-bf) and an additional Brainfuck backend (-bf-out)
* **BFRC-C** contains a backend (-c), that produces simple C files.
* **BFRC-DOT** provides a backend, that creates a cool Graph representation of the AST based on the [DOT-language](https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29) (-dot), that may be rendered by a tool like [Graphviz](http://graphviz.org/).
* **BFRC-Java** uses Javassist to compile a Java class, that can be executed in RAM (-jit) or saved to disk (-java).
* **BFRC-Ook** provides an additional frontend, that lexes the [Ook! language](http://www.dangermouse.net/esoteric/ook.html) (-ook)while utilizing BFRC-BF's parser.
* **BFRC-Opts** contains all optimizations, that may be performed on the AST independently of the front- or backend.
