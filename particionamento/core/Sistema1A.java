package particionamento.core;

import particionamento.cpu.InterruptHandling;
import particionamento.cpu.SysCallHandling;
import particionamento.cpu.VM;
import particionamento.memory.GM;
import particionamento.process.GP;
import particionamento.process.PCB;
import particionamento.programas.Programas;

import java.util.LinkedList;

public class Sistema1A {

    public VM vm;
    public InterruptHandling ih;
    public SysCallHandling sysCall;
    public static Programas progs;
    public GM gm;
    public GP gp;
    public LinkedList<PCB> prontos;
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

        progs = new Programas();
        gm = new GM(vm.tamMem, tamPart);
        gp = new GP();
        prontos = new LinkedList<>();

        gp.carregaGP(gm, vm.m, prontos);
        //gm.imprimeParticao(0);
    }
}
