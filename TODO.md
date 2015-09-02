TODOs
=====

Some ideas for future work are collected here.

#### Testing
* Verify that optimizations don't alter the runtime behaviour (call test-interpreter).
* Verify that all optimizations are idempotent, i.e. opt(P) == opt(opt(P)) (check AST for that).
* Come up with some programs to test:
    * a set of representative real world programs
    * edge cases designed to verify potential optimization pitfalls
* Take a look at [QuickCheck](https://github.com/mcandre/gruesome/blob/master/src/main/groovy/example.groovy).

#### Optimization
* Implement cell bounds analysis
    * Check that all loops contain the same number of `<` as they do `>`.
    * If they do, we can statically count the farthest cell the program will reach and only allocate so much memory.
    * Warn/Error for the program wraps around (-1 or >32k)
* Implement dead code elimination
    * Remove all the loops, that can never enter, e.g.
        * loop as first instruction of a program
        * loop after =0
        * for all consecutive loops like `[foo][bar][baz]`, `bar` and `baz` are dead
    * If there's a static assignment of cell contents (see cell bounds analysis)
        * Track which cells get read (i.e. `.` or `[`).
        * Remove the according write operations into unused cells.
        * Take absolute ops into account, e.g. `+++[-]+` to `[-]+` or `+++,+` to `,+`
        * Warn about `,` that will not be used, but still keep them in.
* Implement generic framework to assess the impact of operations on memory cells at compile time. (useful for cell bounds analysis and dead code elimination)
* Detect patterns in loops like multiplication or cell moves and use dedicated operations for those.
* Find optimal ordering of optimizations
    * Apply all optimizations and recurse in case the AST has been altered by the particular optimization.
    * The minimal path's AST (by means of minimal interpretation steps) wins.

#### Backends
* Implement a backend emitting LLVM assembly.
* The BFBackend can save the `[-]` after a loop for absolute ops, e.g. `LOOP =3` to `[loop]+++` instead of `[loop][-]+++`.
