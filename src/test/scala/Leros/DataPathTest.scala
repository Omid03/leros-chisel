package Leros

import chisel3.iotesters._

class DataPathTestBench(c: DataPath) extends PeekPokeTester(c) {
  step(20)
}

object DataPathTester  extends App{
  println("[{Running TestBench for Adder}]")
  chisel3.iotesters.Driver(() => new DataPath) {c =>
    new DataPathTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new DataPath) {
    c => new DataPathTestBench(c)
  }
}