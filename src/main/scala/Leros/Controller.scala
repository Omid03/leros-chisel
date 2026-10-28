package Leros

import chisel3._
import chisel3.util._

class Controller extends Module {
    val io = IO(new Bundle {
        val op = Input(UInt(8.W))
        val AReg = Input(SInt(16.W))
        val PCSel = Output(UInt(1.W))
        val RegWSel = Output(UInt(1.W))
        val PCAdrSel = Output(UInt(1.W))
        val SrcBSel = Output(UInt(2.W))
        val MemWe = Output(UInt(1.W))
        val AREn = Output(UInt(1.W))
        val ALUOpC = Output(UInt(3.W))
        val RegWe = Output(UInt(1.W))
    })

    def maskedEqual(op: UInt, pattern: UInt, mask: UInt): Bool = {
      (op & mask) === (pattern & mask)
    }

    val controls = Wire(UInt(11.W))
    controls := 0.U
    io.PCSel := controls(10)
    io.RegWe := controls(9)
    io.RegWSel := controls(8)
    io.PCAdrSel := controls(7)
    io.SrcBSel := controls(6, 5)
    io.ALUOpC := controls(4, 2)
    io.AREn := controls(1)
    io.MemWe := controls(0)

    val ZeroFlag = Wire(UInt())
    ZeroFlag := Mux(io.AReg === 0.S, 1.U, 0.U)

    val LTFlag = Wire(UInt())
    LTFlag := Mux(io.AReg < 0.S , 1.U, 0.U)
    
    //Local parameters
    val Add = maskedEqual(io.op, "b00001000".U, "b11111101".U)
    val Addi = maskedEqual(io.op, "b00001001".U, "b11111101".U)
    val Sub = maskedEqual(io.op, "b00001100".U, "b11111101".U)
    val Subi = maskedEqual(io.op, "b00001101".U, "b11111101".U)
    val Shr = maskedEqual(io.op, "b00010000".U, "b11111000".U)
    val And = maskedEqual(io.op, "b00100010".U, "b11111111".U)
    val Andi = maskedEqual(io.op, "b00100011".U, "b11111111".U)
    val Or = maskedEqual(io.op, "b00100100".U, "b11111111".U)
    val Ori = maskedEqual(io.op, "b00100101".U, "b11111111".U)
    val Xor = maskedEqual(io.op, "b00100110".U, "b11111111".U)
    val Xori = maskedEqual(io.op, "b00100111".U, "b11111111".U)
    val Load = maskedEqual(io.op, "b00100000".U, "b11111111".U)
    val Loadi = maskedEqual(io.op, "b00100001".U, "b11111111".U)
    val Store = maskedEqual(io.op, "b00110000".U, "b11111000".U)
    val Jal = maskedEqual(io.op, "b01000000".U, "b11111000".U)
    val LdAddr = maskedEqual(io.op, "b01010000".U, "b11111000".U)
    val Loadind = maskedEqual(io.op, "b01100000".U, "b11111011".U)
    val Storeind = maskedEqual(io.op, "b01110000".U, "b11111011".U)
    val Br = maskedEqual(io.op, "b10000000".U, "b11110000".U)
    val Brz = maskedEqual(io.op, "b10010000".U, "b11110000".U)
    val BrLT = maskedEqual(io.op, "b10100000".U, "b11110000".U)

    //PCSel_RegWe_RegWSel_PCAdrSel_SrcBSel_ALUOpC_AREn_MemWe
    when(Add) {
        controls := "b0_0_0_0_00_001_0_0".U
    } .elsewhen(Addi) {
        controls := "b0_0_0_0_01_001_0_0".U
    } .elsewhen(Sub) {
        controls := "b0_0_0_0_00_010_0_0".U
    } .elsewhen(Subi) {
        controls := "b0_0_0_0_01_010_0_0".U
    } .elsewhen(Shr) {
        controls := "b0_0_0_0_00_111_0_0".U
    } .elsewhen(And) {
        controls := "b0_0_0_0_00_011_0_0".U
    } .elsewhen(Andi) {
        controls := "b0_0_0_0_01_011_0_0".U
    } .elsewhen(Or) {
        controls := "b0_0_0_0_00_100_0_0".U
    } .elsewhen(Ori) {
        controls := "b0_0_0_0_01_100_0_0".U
    } .elsewhen(Xor) {
        controls := "b0_0_0_0_00_101_0_0".U
    } .elsewhen(Xori) {
        controls := "b0_0_0_0_01_101_0_0".U
    } .elsewhen(Load) {
        controls := "b0_0_0_0_00_110_0_0".U
    } .elsewhen(Loadi) {
        controls := "b0_0_0_0_01_110_0_0".U
    } .elsewhen(Store) {
        controls := "b0_1_0_0_00_000_0_0".U
    } .elsewhen(Jal) {
        controls := "b1_1_1_0_00_000_0_0".U
    } .elsewhen(LdAddr) {
        controls := "b0_0_0_0_00_000_1_0".U
    } .elsewhen(Loadind) {
        controls := "b0_0_0_0_10_110_0_0".U
    } .elsewhen(Storeind) {
        controls := "b0_0_0_0_00_000_0_1".U
    } .elsewhen(Br) {
        controls := "b0_0_0_1_00_000_0_0".U
    } .elsewhen(Brz) {
        controls := Cat(0.U(1.W), 0.U(1.W), 0.U(1.W), ZeroFlag, 0.U(2.W), 0.U(3.W), 0.U(1.W), 0.U(1.W))
    } .elsewhen(BrLT) {
        controls := Cat(0.U(1.W), 0.U(1.W), 0.U(1.W), LTFlag, 0.U(2.W), 0.U(3.W), 0.U(1.W), 0.U(1.W))
    } .otherwise {
        controls := 0.U
    }
}

object ControllerMaker extends App {
    println ("[{(Generating Verilog file for Controller)}]")
    (new chisel3.stage.ChiselStage).emitVerilog(new Controller())
}