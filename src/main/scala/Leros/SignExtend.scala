package Leros

import chisel3._

class SignExtend extends Module {
  val io = IO(new Bundle {
    val in = Input(SInt(8.W))
    val out = Output(SInt(16.W))
  })

  io.out := io.in.pad(16)

}

object SignExtendMaker extends App {
  println ("[{(Generating Verilog file for SignExtend)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new SignExtend())
}
