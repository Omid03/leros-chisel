| Opcode     | Instruction | Function                          |
|------------|-------------|-----------------------------------|
| ADD        | 000010x0    | ACC = ACC + Rn                   |
| ADDI       | 000010x1    | ACC = ACC + Imm                  |
| SUB        | 000011x0    | ACC = ACC – Rn                   |
| SUBI       | 000011x1    | ACC = ACC – Imm                  |
| SHR        | 00010xxx    | ACC = ACC >>> 1                 |
| AND        | 00100010    | ACC = ACC AND Rn                 |
| ANDI       | 00100011    | ACC = ACC AND Imm                |
| OR         | 00100100    | ACC = ACC OR Rn                  |
| ORI        | 00100101    | ACC = ACC OR Imm                 |
| XOR        | 00100110    | ACC = ACC XOR Rn                 |
| XORI       | 00100111    | ACC = ACC XOR Imm                |
| Ld         | 00100000    | ACC = Rn                         |
| LdI        | 00100001    | ACC = Imm                        |
| Str        | 00110xxx    | Rn = ACC                         |
| JAL        | 01000xxx    | PC = ACC, Rn = PC + 2            |
| LdAddr     | 01010xxx    | MAR = ACC                        |
| LdInd      | 01100x00    | ACC = MEM[MAR + (Imm << 1)]     |
| StrInd     | 01110x00    | MEM[MAR + (Imm << 1)] = ACC     |
| Br         | 1000xxxx    | PC = PC + Imm                    |
| BrZ        | 1001xxxx    | If ACC == 0 , PC = PC + Imm     |
| BrLT        | 1010xxxx    | If ACC < 0 , PC = PC + Imm     |