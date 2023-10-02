package paginacao.cpu;

import paginacao.memory.Memory;
import paginacao.memory.Word;

public class VM {
    public int tamMem;
    public int tamPag;
    public Word[] m;
    public Memory mem;
    public CPU cpu;
    public boolean debug;


    public VM(InterruptHandling ih, SysCallHandling sysCall, int tamMem, int tamPag, boolean debug) {
        // vm deve ser configurada com endereço de tratamento de interrupcoes e de chamadas de sistema
        // cria memória
        this.tamMem = tamMem;
        this.tamPag = tamPag;
        this.debug = debug;
        mem = new Memory(tamMem);
        m = mem.m;



        // cria cpu
        cpu = new CPU(mem, tamPag, ih, sysCall);                   // true liga debug
    }


    public int getTamMem() {
        return tamMem;
    }

    public int getTamPagina() {
        return tamPag;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

}