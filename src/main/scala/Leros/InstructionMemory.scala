package Leros

import chisel3._
import chisel3.util.experimental.loadMemoryFromFile

class InstructionMemory extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(16.W))  
    val data = Output(UInt(16.W))
  })
  val mem = Mem(65536, UInt(16.W))
  loadMemoryFromFile(mem, "Instructions.txt")
  io.data := mem(io.addr)
}

object IMMaker extends App {
  println ("[{(Generating Verilog file for IM)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new InstructionMemory())
}
