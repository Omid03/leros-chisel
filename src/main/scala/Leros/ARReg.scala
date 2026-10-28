package Leros

import chisel3._

class ARReg(WIDTH: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(WIDTH.W))
    val out = Output(UInt(WIDTH.W))
    val en = Input(Bool())
  })

  val reg = RegInit(0.U(WIDTH.W))

  when(io.en) {
    reg := io.in
  }

  io.out := reg.asUInt
}

object ARRegMaker extends App {
  println ("[{(Generating Verilog file for ARReg)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new ARReg(16))
}
