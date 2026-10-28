package Leros

import chisel3.iotesters._

class ARRegTestBench(c: ARReg) extends PeekPokeTester(c) {
  poke(c.io.en, false)
  poke(c.io.in, 0)
  step(1)
  expect(c.io.out, 0)

  poke(c.io.en, true)
  poke(c.io.in, 42)
  step(1)
  expect(c.io.out, 42)

  poke(c.io.en, false)
  poke(c.io.in, 84)
  step(1)
  expect(c.io.out, 42)

  poke(c.io.en, true)
  poke(c.io.in, 128)
  step(1)
  expect(c.io.out, 128)
}

object ARRegTester extends App{
  println("[{Running TestBench for ARReg}]")
  chisel3.iotesters.Driver(() => new ARReg(16)) {c =>
    new ARRegTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new ARReg(16)) {
    c => new ARRegTestBench(c)
  }
}