package particionamento.cpu;

import particionamento.memory.Memory;
import particionamento.memory.Word;

public class VM {
    public int tamMem;
    public int tamParticao;
    public Word[] m;
    public Memory mem;
    public CPU cpu;
    public boolean debug;


    public VM(InterruptHandling ih, SysCallHandling sysCall, int tamMem, int tamParticao, boolean debug) {
        // vm deve ser configurada com endereço de tratamento de interrupcoes e de chamadas de sistema
        // cria memória
        this.tamMem = tamMem;
        this.tamParticao = tamParticao;
        this.debug = debug;
        mem = new Memory(tamMem);
        m = mem.m;



        // cria cpu
        cpu = new CPU(mem, tamParticao, ih, sysCall);                   // true liga debug
    }


    public int getTamMem() {
        return tamMem;
    }

    public int getTamParticao() {
        return tamParticao;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

}