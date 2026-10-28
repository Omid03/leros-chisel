package Leros

import chisel3._

class Shl extends Module {
  val io = IO(new Bundle {
    val in = Input(SInt(16.W))
    val out = Output(SInt(16.W))
  })

  io.out := io.in << 1
}

object ShlMaker extends App {
  println ("[{(Generating Verilog file for Shl)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new Shl())
}