package core;

import cpu.InterruptHandling;
import cpu.SysCallHandling;
import cpu.VM;
import memory.GM_Pagination;
import processB.GP_B;
import processB.PCB_B;

import java.util.LinkedList;

public class Sistema1B {

    public VM vm;
    public InterruptHandling ih;
    public SysCallHandling sysCall;
    public static GP_B gpB;
    public GM_Pagination gm;
    public LinkedList<PCB_B> prontos;
    public boolean debug;

    public Sistema1B(int tamMem, int tamPagina) { // a VM com tratamento de interrupções
        ih = new InterruptHandling();
        sysCall = new SysCallHandling();

        vm = new VM(tamPagina, ih, sysCall, tamMem, true);
        sysCall.setVM(vm);
        gm = new GM_Pagination(vm.m, vm.tamMem, tamPagina);
        gpB = new GP_B();
        prontos = new LinkedList<>();


        gpB.carregaGP(gm, vm.m, prontos);
        //gm.imprimeParticao(0);


    }
}
