package common;

import cpu.InterruptHandling;
import cpu.SysCallHandling;
import memory.Memory;
import memory.Word;
import utils.Interrupts;

public abstract class ICPU {

    public int maxInt; // maximum and minimum values for integers in this CPU
    public int minInt;
    public int pc; // program counter
    public Word ir; // instruction register
    public int[] reg; // CPU registers
    public Interrupts irpt; // during instruction, interruption can be signaled
    public Memory mem; // reference to memory
    public Word[] m; // reference to the physical memory array
    public InterruptHandling ih; // points to interrupt handling routines
    public SysCallHandling sysCall; // points to system call handling routinesdurante instrucao, interrupcao pode ser sinalizada
    public int base; // base e limite de acesso na memoria
    public int limite; // por enquanto toda memoria pode ser acessada pelo processo rodando
    public boolean debug;


    public ICPU(Memory _mem, InterruptHandling _ih, SysCallHandling _sysCall) {
        maxInt = 32767;
        minInt = -32767;
        mem = _mem;
        m = mem.m;
        reg = new int[10];
        ih = _ih;
        sysCall = _sysCall;
    }

    public abstract void run();
    public abstract void setContext(int _base, int _limite, int _pc, int particao);

    public abstract void setContext(int _base, int _limite, int _pc, int [] paginasProcesso);


}
