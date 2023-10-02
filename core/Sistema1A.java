package core;

import cpu.InterruptHandling;
import cpu.SysCallHandling;
import cpu.VM;
import memory.GM;
import processA.GP_A;
import processA.PCB_A;


import java.util.LinkedList;

public class Sistema1A {

    public VM vm;
    public InterruptHandling ih;
    public SysCallHandling sysCall;

    public GM gm;
    public GP_A gpA;
    public LinkedList<PCB_A> prontos;
    public boolean debug;

    public Sistema1A(int tamMem, int tamPart) {
        // Initialize the InterruptHandling instance
        ih = new InterruptHandling();

        // Initialize the SysCallHandling instance
        sysCall = new SysCallHandling();

        // Initialize the VM instance and pass the InterruptHandling and SysCallHandling instances
        vm = new VM(ih, sysCall, tamMem, tamPart, debug);

        // Set the VM instance for the InterruptHandling and SysCallHandling instances
        ih.setVM(vm);
        sysCall.setVM(vm);

        // Link the InterruptHandling instance to the SysCallHandling instance
        sysCall.setInterruptHandler(ih);

        gm = new GM(vm.tamMem, tamPart);
        gpA = new GP_A();
        prontos = new LinkedList<>();

        gpA.carregaGP(gm, vm.m, prontos);
        //gm.imprimeParticao(0);
    }
}
