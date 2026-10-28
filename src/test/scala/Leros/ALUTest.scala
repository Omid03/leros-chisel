package Leros

import chisel3.iotesters._
import chisel3.stage._

class ALUTestBench(c: ALU) extends PeekPokeTester(c) {

  poke(c.io.opcode_i, 0)
  poke(c.io.a_i, -3)
  poke(c.io.b_i, 5)
  step(1)
  var result = peek(c.io.res_o).toInt
  println(s"Opcode: 0, a: -3, b: 5, Expected: -3, Output: $result")
  expect(c.io.res_o, -3)

  poke(c.io.opcode_i, 1)
  poke(c.io.a_i, -3)
  poke(c.io.b_i, 5)
  step(1)
  result = peek(c.io.res_o).toInt
  println(s"Opcode: 1, a: -3, b: 5, Expected: 2, Output: $result")
  expect(c.io.res_o, 2)

  poke(c.io.opcode_i, 2)
  poke(c.io.a_i, -3)
  poke(c.io.b_i, 5)
  step(1)
  result = peek(c.io.res_o).toInt
  println(s"Opcode: 2, a: -3, b: 5, Expected: -8, Output: $result")
  expect(c.io.res_o, -8)

  poke(c.io.opcode_i, 3)
  poke(c.io.a_i, -3)
  poke(c.io.b_i, 5)
  step(1)
  result = peek(c.io.res_o).toInt
  println(s"Opcode: 3, a: -3, b: 5, Expected: ${-3 & 5}, Output: $result")
  expect(c.io.res_o, -3 & 5)

  poke(c.io.opcode_i, 4)
  poke(c.io.a_i, -3)
  poke(c.io.b_i, 5)
  step(1)
  result = peek(c.io.res_o).toInt
  println(s"Opcode: 4, a: -3, b: 5, Expected: ${-3 | 5}, Output: $result")
  expect(c.io.res_o, -3 | 5)

  poke(c.io.opcode_i, 5)
  poke(c.io.a_i, -3)
  poke(c.io.b_i, 5)
  step(1)
  result = peek(c.io.res_o).toInt
  println(s"Opcode: 5, a: -3, b: 5, Expected: ${-3 ^ 5}, Output: $result")
  expect(c.io.res_o, -3 ^ 5)

  poke(c.io.opcode_i, 6)
  poke(c.io.a_i, -3)
  poke(c.io.b_i, 5)
  step(1)
  result = peek(c.io.res_o).toInt
  println(s"Opcode: 6, a: -3, b: 5, Expected: 5, Output: $result")
  expect(c.io.res_o, 5)

  poke(c.io.opcode_i, 7)
  poke(c.io.a_i, -3)
  poke(c.io.b_i, 5)
  step(1)
  result = peek(c.io.res_o).toInt
  println(s"Opcode: 7, a: -3, b: 5, Expected: ${-3 >> 1}, Output: $result")
  expect(c.io.res_o, -3 >> 1)
}


object ALUTester extends App{
  println("[{Running TestBench for ALU}]")
  chisel3.iotesters.Driver(() => new ALU(8)) {c =>
    new ALUTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new ALU(8)) {
    c => new ALUTestBench(c)
  }
}
