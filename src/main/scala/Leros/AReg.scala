package Leros

import chisel3._

class AReg(WIDTH: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(SInt(WIDTH.W))
    val out = Output(UInt(WIDTH.W))
  })

  val reg = RegInit(0.U(WIDTH.W))
  reg := io.in.asUInt
  io.out := reg
}

object ARegMaker extends App {
  println ("[{(Generating Verilog file for AReg)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new AReg(16))
}