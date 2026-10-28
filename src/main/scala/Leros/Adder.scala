package Leros

import chisel3._

class Adder(WIDTH: Int) extends Module {
  val io: Bundle {val a: SInt; val b: SInt; val sum: SInt} = IO(new Bundle {
    val a : SInt = Input(SInt(WIDTH.W))
    val b : SInt = Input(SInt(WIDTH.W))
    val sum : SInt = Output(SInt(WIDTH.W))
  })

  io.sum := io.a + io.b
}

object AdderMaker extends App {
  println ("(Generating Verilog file for Adder)")
  (new chisel3.stage.ChiselStage).emitVerilog(new Adder(32))
}
