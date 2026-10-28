package Leros

import chisel3.iotesters._

class DataMemoryTestBench(c: DataMemory) extends PeekPokeTester(c) {
  poke(c.io.addr, 0)
  step(1)

  poke(c.io.addr, 1)
  step(1)

  poke(c.io.addr, 2)
  step(1)

  poke(c.io.addr, 3)
  step(1)

  poke(c.io.addr, 4)
  step(1)

  poke(c.io.addr, 5)
  step(1)

  poke(c.io.addr, 6)
  step(1)
}

object DMTester extends App{
  println("[{Running TestBench for DataMemory}]")
  chisel3.iotesters.Driver(() => new DataMemory) {c =>
    new DataMemoryTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new DataMemory) {
    c => new DataMemoryTestBench(c)
  }
}
