package Leros

import chisel3._
import chisel3.iotesters._

class InstructionMemoryTestBench(c: InstructionMemory) extends PeekPokeTester(c) {
  val specificValues = Seq(
    0  -> "b00001001_00000000".U,
    1  -> "b01010000_00000000".U,
    2  -> "b01100000_00000000".U,
    3  -> "b00110000_00000000".U,
    4  -> "b01100000_00000001".U,
    5  -> "b00110000_00000001".U,
    6  -> "b01100000_00000010".U,
    7  -> "b00110000_00000010".U,
    8  -> "b01100000_00000011".U,
    9  -> "b00110000_00000011".U,
    10 -> "b01100000_00000100".U,
    11 -> "b00001000_00000000".U,
    12 -> "b00001000_00000001".U,
    13 -> "b00001000_00000010".U,
    14 -> "b00001000_00000011".U
  )

  // Test specific values
  for ((addr, value) <- specificValues) {
    poke(c.io.addr, addr)
    step(1)
    expect(c.io.data, value)
  }
}

object IMTester  extends App{
  println("[{Running TestBench for InstructionMemory}]")
  chisel3.iotesters.Driver(() => new InstructionMemory()) {c =>
    new InstructionMemoryTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new InstructionMemory()) {
    c => new InstructionMemoryTestBench(c)
  }
}
