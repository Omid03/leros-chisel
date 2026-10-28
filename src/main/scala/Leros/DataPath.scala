package Leros

import chisel3._
import chisel3.util._

class DataPath extends Module {
    val io = IO(new Bundle {
        val op = Output(UInt(8.W))
        val AReg = Output(SInt(16.W))
        val PCSel = Input(UInt(1.W))
        val RegWSel = Input(UInt(1.W))
        val PCAdrSel = Input(UInt(1.W))
        val SrcBSel = Input(UInt(2.W))
        val MemWe = Input(UInt(1.W))
        val AREn = Input(UInt(1.W))
        val ALUOpC = Input(UInt(3.W))
        val RegWe = Input(UInt(1.W))
    })

    //Components
    private val Pc_Reg = Module(new PCReg(16))
    private val InstMem = Module(new InstructionMemory)
    private val RegFile = Module(new RegisterFile)
    private val ALUUnit = Module(new ALU(16))
    private val A_reg = Module(new AReg(16))
    private val Ar_Reg = Module(new ARReg(16))
    private val AddrAdder = Module(new Adder(16))
    private val MemFile = Module(new DataMemory)
    private val ShlUnit = Module(new Shl)
    private val SignEx = Module(new SignExtend)
    private val PCAdder = Module(new Adder(16))

    io.AReg := A_reg.io.out.asSInt
    io.op := InstMem.io.data(15, 8)

    private val Val2ALU = Mux(io.SrcBSel(1).asBool, MemFile.io.Data, Mux(io.SrcBSel(0).asBool, SignEx.io.out, RegFile.io.Read_Val))
    private val PC_mux = Mux(io.PCSel.asBool, A_reg.io.out, PCAdder.io.sum.asUInt())
    private val RegWriteVal = Mux(io.RegWSel.asBool, PCAdder.io.sum, A_reg.io.out.asSInt)
    private val PCVal2 = Mux(io.PCAdrSel.asBool, SignEx.io.out, 2.S(16.W))


    Pc_Reg.io.Next_PC := PC_mux
    InstMem.io.addr := Cat(0.U(1.W), Pc_Reg.io.PC(15, 1))
    PCAdder.io.a := Pc_Reg.io.PC.asSInt()
    PCAdder.io.b := PCVal2
    RegFile.io.addr := InstMem.io.data(7, 0)
    RegFile.io.Write_Data := RegWriteVal
    RegFile.io.WE := io.RegWe.asBool
    SignEx.io.in := InstMem.io.data(7, 0).asSInt()
    ShlUnit.io.in := SignEx.io.out.asSInt()
    ALUUnit.io.a_i := A_reg.io.out.asSInt
    ALUUnit.io.b_i := Val2ALU
    ALUUnit.io.opcode_i := io.ALUOpC
    A_reg.io.in := ALUUnit.io.res_o
    Ar_Reg.io.in := A_reg.io.out
    Ar_Reg.io.en := io.AREn.asBool
    AddrAdder.io.a := Ar_Reg.io.out.asSInt()
    AddrAdder.io.b := ShlUnit.io.out
    MemFile.io.addr := Cat(0.U(1.W), AddrAdder.io.sum(15, 1).asUInt())
    MemFile.io.WE := io.MemWe
    MemFile.io.Write_data := A_reg.io.out.asSInt
}

object DataPathMaker extends App {
    println ("[{(Generating Verilog file for DataPath)}]")
    (new chisel3.stage.ChiselStage).emitVerilog(new DataPath())
}