package Leros

import chisel3._

class PCReg(WIDTH: Int) extends Module {
  val io = IO(new Bundle {
    val Next_PC = Input(UInt(WIDTH.W))
    val PC = Output(UInt(WIDTH.W))
  })
  val reg = RegInit(0.U(WIDTH.W))
  reg := io.Next_PC
  io.PC := reg
}

object PCRegMaker extends App {
  println ("[{(Generating Verilog file for PCReg)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new PCReg(16))
}