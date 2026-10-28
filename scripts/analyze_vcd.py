#!/usr/bin/env python3
"""Extract settled rising-edge samples from the archived VCD. No HDL execution."""
from pathlib import Path
import csv,json,re,hashlib
ROOT=Path(__file__).resolve().parents[1]
f=ROOT/'results/archive/Leros.vcd'
text=f.read_text();head,body=re.split(r'\$enddefinitions\s+\$end',text,maxsplit=1)
assert re.search(r'\$timescale\s+1ns\s+\$end',head), 'Expected 1 ns VCD units'
scopes=[];names={}
for line in head.splitlines():
    s=line.split()
    if not s:continue
    if s[0]=='$scope':scopes.append(s[2])
    elif s[0]=='$upscope':scopes.pop()
    elif s[0]=='$var':names['.'.join(scopes+[s[4]])]=s[3]
paths={'clock':'Leros.clock','reset':'Leros.reset','pc':'Leros.DP.PCReg.io_PC','acc':'Leros.DP.io_AReg','opcode':'Leros.DP.io_op'}
paths.update({'r'+str(i):'Leros.DP.RegisterFile.regs_'+str(i) for i in [0,1,4,255]})
ids={k:names[v] for k,v in paths.items()};vals={};rows=[];time=0;prev=None

def sample():
    global prev
    clk=vals.get(ids['clock'])
    if clk==1 and prev==0:
        row={'time_ns':time};row.update({k:vals.get(v) for k,v in ids.items() if k!='clock'})
        if row['acc'] is not None and row['acc']>=32768:row['acc']-=65536
        rows.append(row)
    prev=clk

for line in body.splitlines():
    line=line.strip()
    if not line:continue
    if line[0]=='#':sample();time=int(line[1:])
    elif line[0] in 'bB':
        bits,code=line[1:].split()
        vals[code]=int(bits,2) if set(bits)<=set('01') else None
    elif line[0] in '01xXzZ':vals[line[1:]]=int(line[0]) if line[0] in '01' else None
sample()
active=[x for x in rows if x['reset']==0]
assert len(active)==2000
stable=None
for i,row in enumerate(active):
    if all(x['pc']==38 and x['acc']==4 for x in active[i:]):stable=(i+1,row['time_ns']);break
assert stable is not None
summary={'method':'settled values at each rising clock edge; archived waveform only','vcd_sha256':hashlib.sha256(f.read_bytes()).hexdigest(),'timescale':'1 ns','last_timestamp_ns':time,'rising_edges':len(rows),'reset_edges':len(rows)-len(active),'active_edges':len(active),'terminal_pc_byte_address':38,'final_acc':active[-1]['acc'],'terminal_state_first_active_edge':stable[0],'terminal_state_first_time_ns':stable[1],'terminal_state_held_to_last_sample_ns':active[-1]['time_ns']}
(ROOT/'results/vcd-summary.json').write_text(json.dumps(summary,indent=2)+'\n')
with (ROOT/'results/vcd-samples.csv').open('w',newline='') as out:
    w=csv.DictWriter(out,fieldnames=list(rows[0]));w.writeheader();w.writerows(rows)
print(json.dumps(summary,indent=2))
