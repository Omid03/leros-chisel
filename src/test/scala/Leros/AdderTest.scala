package Leros

import chisel3.iotesters._

class AdderTestBench(c: Adder) extends PeekPokeTester(c) {

  poke(c.io.a, 0)
  poke(c.io.b, 0)
  step(1)
  expect(c.io.sum, 0)

  poke(c.io.a, 1)
  poke(c.io.b, 1)
  step(1)
  expect(c.io.sum, 2)

  poke(c.io.a, 10)
  poke(c.io.b, 5)
  step(1)
  expect(c.io.sum, 15)

  poke(c.io.a, 123)
  poke(c.io.b, 456)
  step(1)
  expect(c.io.sum, 579)

  poke(c.io.a, (BigInt(1) << c.io.a.getWidth) - 1)
  poke(c.io.b, (BigInt(1) << c.io.b.getWidth) - 1)
  step(1)
  expect(c.io.sum, (BigInt(1) << c.io.sum.getWidth) - 2)
}

object AdderTester  extends App{
  println("[{Running TestBench for Adder}]")
  chisel3.iotesters.Driver(() => new Adder(32)) {c =>
    new AdderTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new Adder(32)) {
    c => new AdderTestBench(c)
  }
}
