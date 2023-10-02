package particionamento.cpu;

import particionamento.utils.Interrupts;

public class SysCallHandling {
    private VM vm;
    private InterruptHandling ih;

    public void setInterruptHandler(InterruptHandling _ih) {
        ih = _ih;
    }
    public void setVM(VM _vm) {
        vm = _vm;
    }

    public void handle() { // apenas avisa - todas interrupcoes neste momento finalizam o programa
        System.out.println("                                               Chamada de Sistema com op  /  par:  "
                + vm.cpu.reg[8] + " / " + vm.cpu.reg[9]);

        ih.handle(Interrupts.chamadaTrap, vm.cpu.pc);
    }
}