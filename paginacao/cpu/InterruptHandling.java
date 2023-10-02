package paginacao.cpu;

import paginacao.utils.Interrupts;

import java.util.Locale;
import java.util.Scanner;

public class InterruptHandling {
    private VM vm;
    public void handle(Interrupts irpt, int pc) { // apenas avisa - todas interrupcoes neste momento finalizam o
        // programa
        System.out.println("                                               Interrupcao " + irpt + "   pc: " + pc);

        switch (irpt) {
            case intEnderecoInvalido:
                System.out.println("Motivo: Endereço de memória inválido");
                finalizaPrograma();
                break;
            case intInstrucaoInvalida:
                System.out.println("Motivo: Instrução inválida");
                finalizaPrograma();
                break;
            case intRegistradorInvalido:
                System.out.println("Motivo: Registrador inválido");
                finalizaPrograma();
                break;
            case intOverflow:
                System.out.println("Motivo: Overflow");
                finalizaPrograma();
                break;
            case chamadaTrap:
                System.out.println("Motivo: Chamada de sistema");
                terminal();
                break;
            case intSTOP:
                System.out.println("Final do Programa");
                //finalizaProcesso();
                break;
        }
    }


    private void terminal() {
        Locale.setDefault(Locale.US);


        Scanner sc = new Scanner(System.in);

        if (vm.cpu.reg[8] == 1) {// leitura de um inteiro
            int pos = vm.cpu.reg[9];
            System.out.println(
                    "                                               Lendo da função Trap para pos de memória: "
                            + pos);
            int var = sc.nextInt();
            vm.m[pos].p = var;
        }
        if (vm.cpu.reg[8] == 2) { // escrita de um inteiro
            int pos = vm.cpu.reg[9];
            int var = vm.m[pos].p;
            vm.m[pos].p = var;
            System.out
                    .println("                                               Imprimindo da função Trap: resposta: "
                            + var + " e pos de memória: " + pos);
        }
        if (vm.cpu.reg[8] == 3) {
            //sisop pede para gm mais um frame
            // inclui na próxima posiçao do vetor de frames do programa e devolve
            // e o processo continua até o novo frame alocador
            // o gm deverá colocar essa pagina como ocupada e associar a chave 777
            int chave =vm.cpu.reg[9];


        }
        if (vm.cpu.reg[8] == 4) {
            //outro processo com a chave pede para acessar a memoria compartilhada
            //gm cuida disso, na tabela de paginas deve verificar se está ocupado e se tem chave associada


        }

    }

    private void finalizaPrograma() {
        System.out.println("Fim do programa por interrupção.");
        System.exit(0);
    }


    private void finalizaProcesso() {
        System.out.println("Fim do programa");
        System.exit(0);
    }

}