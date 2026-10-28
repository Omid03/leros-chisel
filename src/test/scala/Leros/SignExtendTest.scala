package Leros

import chisel3._
import chisel3.iotesters._

class SignExtendTestBench(c: SignExtend) extends PeekPokeTester(c) {
  poke(c.io.in, 0x7F)
  step(1)
  expect(c.io.out, 127.S)

  poke(c.io.in, 0x3F)
  step(1)
  expect(c.io.out, 63.S)

  poke(c.io.in, 0xFF)
  step(1)
  expect(c.io.out, -1.S)

  poke(c.io.in, 0x80)
  step(1)
  expect(c.io.out, -128.S)


  poke(c.io.in, 0x00)
  step(1)
  expect(c.io.out, 0.S)
}

object SignExtendTester extends App{
  println("[{Running TestBench for SignExtend}]")
  chisel3.iotesters.Driver(() => new SignExtend) {c =>
    new SignExtendTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new SignExtend) {
    c => new SignExtendTestBench(c)
  }
}
