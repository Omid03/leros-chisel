package Leros

import chisel3._
import chisel3.util.experimental.loadMemoryFromFile

class DataMemory extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(16.W))
    val Write_data = Input(SInt(16.W))
    val WE = Input(Bool())
    val Data = Output(SInt(16.W))
  })

  val mem = Mem(65536, SInt(16.W))
  loadMemoryFromFile(mem, "MemoryInit.txt")

  when(io.WE) {
    mem.write(io.addr, io.Write_data)
  }

  io.Data := mem.read(io.addr)
}

object DMMaker extends App {
  println ("[{(Generating Verilog file for DM)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new DataMemory())
}