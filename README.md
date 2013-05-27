Brainfuck Research Compliler
============================

The project is designed as a tool for learning about compiler construction in an extremely simple way.
Although being kind of over-engineered for the purpose of compiling Brainfuck, this compiler features most of the architectural elements of a mature language compiler.
Nevertheless, its simplicity allows beginners to implement extensions in the course of an afternoon instead of weeks of work.

This project is licensed under a liberal and easily understandable ISC license, see LICENSE file.

The repository contains several sub-projects:
#### BFRC-Base
defines the common API used by all other projects, including the Abstract Syntax Tree (AST) structure.

The general flow through a compiler is described in the following illustration. BFRC provides Interfaces for all active elements depicted.

    Input -----> Lexer -----> Parser -----+----------------------+-----> Backend -----> Output
                                          |                      |
                                          +----- Optimizer <-----+

#### BFRC-Run
provides the runtime environment to execute the compiler.

You generally call the compiler by `{-config} inputFile [outputFile]` where
* `inputFile` is the mandatory parameter specifying the input file
* `outputFile` is optional, when omitted BFRC will generate the output according to `inputFile`
* `-config` loads the configuration `config`

A configuration is defined by a `.config` file in the java package `bfrc`.
It is a simple [Java properties file](https://en.wikipedia.org/wiki/.properties), that defines the classes to load via the keys `bfrc.lexer`, `bfrc.parser`, `bfrc.optimizers` and `bfrc.backend`.
Optimizers may be multiple entries separated by whitespace, that are called in the order provided.
The Configuration `default.config` (in BFRC-Run) will always be loaded as first config and may be overridden by multiple configurations in the order they are named.

#### BFRC-BF
includes the default Brainfuck frontend (`-bf`) and an additional Brainfuck backend (`-bf-out`).

#### BFRC-C
contains a backend (`-c`), that produces simple C files.

#### BFRC-DOT
provides a backend, that creates a cool Graph representation of the AST based on the [DOT-language](https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29) (`-dot`), that may be rendered by a tool like [Graphviz](http://graphviz.org/).

#### BFRC-Java
uses Javassist (required library) to compile a Java class, that can be executed in RAM (`-jit`) or saved to disk (`-java`).

#### BFRC-Ook
provides an additional frontend, that lexes the [Ook! language](http://www.dangermouse.net/esoteric/ook.html) (`-ook`) while utilizing BFRC-BF's parser. It also includes a backend (`-ook-out`).

#### BFRC-Opts
contains all optimizations, that may be performed on the AST independently of the front- or backend.
