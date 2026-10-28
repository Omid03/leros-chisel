package Leros

import chisel3._



class Leros extends Module {
    val io = IO(new Bundle{})
    val DP = Module(new DataPath)
    val C = Module(new Controller)
    DP.io <> C.io
    dontTouch(DP.io)
    dontTouch(C.io)
}

object LerosMaker extends App {
    println ("[{(Generating Verilog file for Leros)}]")
    (new chisel3.stage.ChiselStage).emitVerilog(new Leros(), Array("--target-dir", "Verilog/Leros"))
}