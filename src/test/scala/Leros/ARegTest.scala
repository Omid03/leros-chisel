package Leros

import chisel3.iotesters._

class ARegTestBench(c: AReg) extends PeekPokeTester(c) {

  poke(c.io.in, 0)
  step(1)
  expect(c.io.out, 0)

  poke(c.io.in, 12)
  step(1)
  expect(c.io.out, 12)

  poke(c.io.in, 34)
  step(1)
  expect(c.io.out, 34)

  poke(c.io.in, 98)
  step(1)
  expect(c.io.out, 98)
}

object ARegTester extends App{
  println("[{Running TestBench for AReg}]")
  chisel3.iotesters.Driver(() => new AReg(16)) {c =>
    new ARegTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new AReg(16)) {
    c => new ARegTestBench(c)
  }
}