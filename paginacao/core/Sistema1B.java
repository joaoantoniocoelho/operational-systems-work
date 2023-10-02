package paginacao.core;

import paginacao.cpu.InterruptHandling;
import paginacao.cpu.SysCallHandling;
import paginacao.cpu.VM;
import paginacao.memory.GM;
import paginacao.process.GP;
import paginacao.process.PCB;
import paginacao.programas.Programas;

import java.util.LinkedList;

public class Sistema1B {

    public VM vm;
    public InterruptHandling ih;
    public SysCallHandling sysCall;
    public static Programas progs;
    public GM gm;
    public GP gp;
    public LinkedList<PCB> prontos;
    public boolean debug;

    public Sistema1B(int tamMem, int tamPagina) { // a VM com tratamento de interrupções
        ih = new InterruptHandling();
        sysCall = new SysCallHandling();
        vm = new VM(ih, sysCall, tamMem, tamPagina, debug);
        sysCall.setVM(vm);
        progs = new Programas();
        gm = new GM(vm.m, vm.tamMem, tamPagina);
        gp = new GP();
        prontos = new LinkedList();

        gp.carregaGP(gm, vm.m, prontos);
        //gm.imprimeParticao(0);


    }
    }
