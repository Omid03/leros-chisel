package Leros

import chisel3.iotesters._

class LerosTestBench(c: Leros) extends PeekPokeTester(c) {
  step(2000)
}

object LerosTester extends App{
  println("[{Running TestBench for Leros}]")
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on", "--target-dir", "VCD_O/Leros"), () => new Leros) {
    c => new LerosTestBench(c)
  }
}