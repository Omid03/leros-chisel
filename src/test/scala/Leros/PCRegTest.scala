package Leros

import chisel3._
import chisel3.iotesters._

class PCRegTestBench(c: PCReg) extends PeekPokeTester(c) {

  poke(c.io.Next_PC, 0.S)
  expect(c.io.PC, 0.U)

  poke(c.io.Next_PC, 42.S)
  step(1)
  expect(c.io.PC, 42.U)

  poke(c.io.Next_PC, 84.S)
  step(1)
  expect(c.io.PC, 84.U)

  poke(c.io.Next_PC, 128.S)
  step(1)
  expect(c.io.PC, 128.U)
}


object PCRegTester  extends App{
  println("[{Running TestBench for InstructionMemory}]")
  chisel3.iotesters.Driver(() => new PCReg(16)) {c =>
    new PCRegTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new PCReg(16)) {
    c => new PCRegTestBench(c)
  }
}