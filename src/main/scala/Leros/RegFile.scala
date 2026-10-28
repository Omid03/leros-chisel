package Leros

import chisel3._

class RegisterFile extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(8.W))
    val WE = Input(Bool())
    val Write_Data = Input(SInt(16.W)) 
    val Read_Val = Output(SInt(16.W))
  })

  val regs = RegInit(VecInit(Seq.fill(256)(0.S(16.W))))

  when(io.WE) {
    regs(io.addr) := io.Write_Data
  }

  io.Read_Val := regs(io.addr)
}

object RegFileMaker extends App {
  println ("[{(Generating Verilog file for RegisterFile)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new RegisterFile())
}