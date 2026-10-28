# Source and evidence review

## Scope

One integrated Chisel processor is supplied, with BrLT already added. There are 13 main Scala files and 13 test Scala files. The base and extension sections in the README describe engineering scope, not independently available versions.

The active 34-word instruction image matches the hexadecimal GCD program in the extension specification. `MemoryInit.txt` contains six words; the first five represent the requested array values, but the GCD program does not establish indirect data-memory behavior. The surviving `InstructionMemoryTestBench` contains 15 expected words from an earlier program, incompatible with the active image. No replacement program was invented or silently installed.

## Execution evidence

The VCD header dates the capture to 2025-06-10 and declares 1 ns units. The analysis script samples all signal changes at each timestamp before detecting a settled rising edge. It uses `Leros.clock`, `Leros.reset`, `Leros.DP.PCReg.io_PC`, `Leros.DP.io_AReg`, the decoded opcode and selected register-file values.

The first active rising edge is at 51 ns. The 120th is at 1241 ns, where PC=38 and A=4 begin their persistent terminal state. There are 2000 active samples and five reset samples. The 1241 ns figure includes initial reset scheduling; it should not be described as a measured device runtime. The last sampled edge is at 20041 ns and the file ends at 20046 ns.

The original top-level test has no `expect` statement. The terminal-state check in `scripts/analyze_vcd.py` is a newly added check of historical data, not a newly run hardware test. No program input sweep or proof of termination for all inputs is available. The GCD workload exercises branches, arithmetic, register operations and JAL; it does not certify the entire ISA or data-memory path.

## Generated RTL mismatch

`results/archive/Verilog/InstructionMemory.sv` contains `wire [15:0] io_data_0 = 16'h0` and assigns that value to the output. The archived VCD instead shows execution of the nonzero GCD instructions. Therefore these artifacts cannot be treated as one verified generation/simulation chain.

The SystemVerilog header identifies CIRCT firtool-1.146.0; `build.sbt` pins Chisel 3.4.2. The archive does not explain the intervening conversion procedure. Do not use the archived RTL as an executable release without regeneration and memory-image validation. High/low FIRRTL and annotations are retained exactly for investigation. Embedded original machine paths inside historical artifacts are preserved as evidence; only the active Scala memory paths were relocated.

## Test coverage

`results/test-inventory.csv` counts textual `expect` call sites, not executed assertions. For example, the instruction-memory tester has one call site inside a 15-element loop. The ALU tester instantiates an 8-bit ALU, while the processor uses 16 bits. Its eight opcode examples do not prove processor-width overflow and boundary behavior.

The controller tester has directed checks for 21 named instruction cases, including a negative BrLT input. It does not exhaust all wildcard opcode variants or all taken/not-taken branch conditions. Leros, datapath and data-memory testers have no assertions. Many component tester Apps call both `Driver(...)` and `Driver.execute(...)`; neither returned Boolean is explicitly converted into a process failure. Existing tests are preserved rather than rewritten during packaging.

Three archived sbt logs reach project loading and IntelliJ shell startup under Java 11.0.27. They contain no usable test pass summary. No dedicated test-result XML was found. The Metals log was inspected as IDE diagnostic material, not counted as simulation evidence, and excluded from the publication package.

## Addressing and memory limits

Both memory arrays declare 65536 16-bit elements. The datapath maps a 16-bit byte address to `Cat(0, address(15,1))`, leaving only 32768 reachable words. The least significant bit is discarded, so odd addresses alias the preceding aligned location. No byte-write enables or alignment traps are present.

Branch displacement is the sign-extended low eight instruction bits added to the current PC. It is not implicitly multiplied by two. JAL selects A as the destination and stores the normal PC+2 value. Indirect addressing shifts the signed immediate left by one before addition to AR; the resulting 16-bit arithmetic wraps. Memories are initialized from external files, which contain far fewer words than the declared arrays. Do not assume every unlisted location has a portable defined value across tools.

## Packaging changes

Only the absolute Windows prefixes in `DataMemory.scala` and `InstructionMemory.scala` were removed. Build dependencies, test expectations, program images and hardware expressions were preserved. SHA-256 values for original and packaged Scala files are in `results/source-manifest.json`. Archived execution and generated artifacts are byte-identical to the supplied files.

No new sbt, Chisel, FIRRTL or RTL run was performed. No timing, area, resource count, physical frequency or power result is available. The new artifacts are documentation, source organization and analysis/plots of the original VCD.
