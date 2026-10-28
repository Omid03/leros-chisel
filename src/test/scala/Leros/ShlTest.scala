package Leros

import chisel3._
import chisel3.iotesters._

class ShlTestBench(c: Shl) extends PeekPokeTester(c) {
  poke(c.io.in, 0x0001.asSInt())
  step(1)
  expect(c.io.out, 0x0002.asSInt())

  poke(c.io.in, 0x8000.asSInt())
  step(1)
  expect(c.io.out, 0x0000.asSInt())

  poke(c.io.in, 0x1234.asSInt())
  step(1)
  expect(c.io.out, 0x2468.asSInt())

  poke(c.io.in, 0xFFFF.asSInt())
  step(1)
  expect(c.io.out, 0xFFFE.asSInt())
}

object ShlTester extends App{
  println("[{Running TestBench for Shl}]")
  chisel3.iotesters.Driver(() => new Shl) {c =>
    new ShlTestBench(c)
  }
}