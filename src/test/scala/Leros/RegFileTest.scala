package Leros

import chisel3._
import chisel3.iotesters._

class RegisterFileTestBench(c: RegisterFile) extends PeekPokeTester(c) {
  poke(c.io.addr, 10)
  poke(c.io.WE, true.B)
  poke(c.io.Write_Data, 42.S)
  step(1)
  poke(c.io.WE, false.B)
  poke(c.io.addr, 10)
  step(1)
  expect(c.io.Read_Val, 42.S)

  poke(c.io.addr, 20)
  poke(c.io.WE, true.B)
  poke(c.io.Write_Data, -15.S)
  step(1)
  poke(c.io.WE, false.B)
  poke(c.io.addr, 20)
  step(1)
  expect(c.io.Read_Val, -15.S)

  poke(c.io.addr, 30)
  poke(c.io.WE, false.B)
  poke(c.io.Write_Data, 99.S)
  step(1)
  poke(c.io.addr, 30)
  step(1)
  expect(c.io.Read_Val, 0.S)
}

object RegisterFileTester extends App{
  println("[{Running TestBench for RegisterFile}]")
  chisel3.iotesters.Driver(() => new RegisterFile) {c =>
    new RegisterFileTestBench(c)
  }
  chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new RegisterFile) {
    c => new RegisterFileTestBench(c)
  }
}