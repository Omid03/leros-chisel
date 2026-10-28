package Leros

import chisel3._
import chisel3.util._

class ALUIO(val WIDTH: Int) extends Bundle {
  val a_i = Input(SInt(WIDTH.W))
  val b_i = Input(SInt(WIDTH.W))
  val opcode_i = Input(UInt(3.W))
  val res_o = Output(SInt(WIDTH.W))
}

class ALU(WIDTH: Int) extends Module {
  val io = IO(new ALUIO(WIDTH))

  io.res_o := 0.S

  switch(io.opcode_i) {
    is(0.U) { io.res_o := io.a_i }
    is(1.U) { io.res_o := io.a_i + io.b_i }
    is(2.U) { io.res_o := io.a_i - io.b_i }
    is(3.U) { io.res_o := io.a_i & io.b_i }
    is(4.U) { io.res_o := io.a_i | io.b_i }
    is(5.U) { io.res_o := io.a_i ^ io.b_i }
    is(6.U) { io.res_o := io.b_i }
    is(7.U) { io.res_o := io.a_i >> 1 }
  }
}

