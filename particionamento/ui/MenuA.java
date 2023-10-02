package particionamento.ui;

import particionamento.core.Sistema1A;
import particionamento.memory.Word;
import particionamento.process.PCB;

import java.util.Scanner;

import static particionamento.core.Sistema1A.progs;

public class MenuA {

    private static Sistema1A sistema;

    public MenuA(Sistema1A sistema) {
        this.sistema = sistema;
    }

    //case 1
    private static void loadAndExecGM_GP(Word[] p) {
        sistema.gp.criaProcesso(p);
    }

    //case 2
    private static void listaProcessosPCB() {
        sistema.gp.listaProcessosPCB();
    }

    //case 3
    private static void desaloca(int id) {
        try {
            sistema.gp.desalocaProcesso(id);
        } catch (Exception e) {
            System.out.println("------- Processo não encontrado");
        }
    }

    //case 4
    private static void dumpM(int inicio, int fim) {
        System.out.println("-      Imprimindo memoria: de " + inicio + " até " + fim);
        try {
            sistema.vm.mem.dump(inicio, fim);
        } catch (Exception e) {
            System.out.println("------- Memória não encontrada");
        }
    }

    //case 5
    private static void executa(int idProcesso) {
        try {
            PCB pcbProcesso = sistema.prontos.get(idProcesso);
            int pcProcesso = pcbProcesso.getPc();
            int particaoProcesso = pcbProcesso.getParticao();
            int tamanhoProcesso = pcProcesso + pcbProcesso.getTamanhoProcesso();

            System.out.println("-      Executando processo: " + idProcesso + " na particao: " + particaoProcesso + " com pc: " + pcProcesso + " e tamanho: " + pcbProcesso.getTamanhoProcesso());
            sistema.vm.mem.dump(pcProcesso, tamanhoProcesso);

            System.out.println("---------------------------------- inicia execucao ");

            sistema.vm.cpu.setContext(0, sistema.vm.tamMem - 1, pcProcesso, particaoProcesso);
            sistema.vm.cpu.run();

            System.out.println("---------------------------------- memoria após execucao ");
            sistema.vm.mem.dump(pcProcesso, tamanhoProcesso);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("----- Processo não encontrado");
        }
    }

    //case 6 e 7
    private static void setDebugProgram(boolean debug) {
        sistema.vm.cpu.debug = debug;
        sistema.vm.setDebug(debug);
    }

    //case 8
    public static void listaProcessosPorID(int id) {
        try {
            PCB pcbProcesso = sistema.prontos.get(id);

            System.out.println("------- Processo encontrado: ");
            System.out.println("------- Conteúdo do PCB: " + pcbProcesso.toString());
            int inicio = pcbProcesso.getParticao() * sistema.gm.tamPart;
            int fim = inicio + pcbProcesso.getTamanhoProcesso();
            System.out.println("------- Conteúdo da memória: ");
            sistema.vm.mem.dump(inicio, fim);
        } catch (Exception e) {
            System.out.println("------- Processo não encontrado");
        }
    }


    public static void main(String args[]) {

        int tamMemoria = 1024;
        int tamParticao = 64;

        Sistema1A s = new Sistema1A(tamMemoria, tamParticao);
        MenuA menu = new MenuA(s);

        //--------- Menu ----------------------------------------------------------------
        Scanner sc = new Scanner(System.in);


        while (true) {
            System.out.println(" \n____________________________________________________________________________________");
            System.out.println("    Escolha uma opção:");
            System.out.println("    1 - Cria programa - cria um processo com memória alocada, PCB, etc."); //ok
            System.out.println("    2 - ListaProcessos - lista os processos prontos para rodar."); //ok
            System.out.println("    3 - Desaloca - retira o processo id do sistema."); //ok
            System.out.println("    4 - DumpM - lista a memória entre posições início e fim."); //ok
            System.out.println("    5 - Executa - executa o processo com id fornecido.");
            System.out.println("    6 - TraceOn - liga modo de execução em que CPU print cada instrução executada.");
            System.out.println("    7 - TraceOff - desliga o modo acima TraceOn.");
            System.out.println("    8 - Dump - lista o conteúdo do PCB.");//ok
            System.out.println("    0 - Sair - encerra o programa.");//ok
            System.out.println(" ____________________________________________________________________________________\n");

            int option;

            System.out.print("Opção: ");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    System.out.println("\n       Escolha um programa da lista:");
                    System.out.println("        1 - Fatorial");
                    System.out.println("        2 - ProgMinimo");
                    System.out.println("        3 - Fibonacci10");
                    System.out.println("        4 - FatorialTRAP");
                    System.out.println("        5 - FibonacciTRAP");
                    System.out.println("        6 - PB");
                    System.out.println("        7 - PC");
                    System.out.println("        0 - Sair \n");



                    System.out.print("Informe o numero de um programa: ");
                    int option2;
                    option2 = sc.nextInt();

                    switch (option2) {
                        case 1:
                            loadAndExecGM_GP(progs.fatorial);
                            break;
                        case 2:
                            loadAndExecGM_GP(progs.progMinimo);
                            break;
                        case 3:
                            loadAndExecGM_GP(progs.fibonacci10);
                            break;
                        case 4:
                            loadAndExecGM_GP(progs.fatorialTRAP);
                            break;
                        case 5:
                            loadAndExecGM_GP(progs.fibonacciTRAP);
                            break;
                        case 6:
                            loadAndExecGM_GP(progs.PB);
                            break;
                        case 7:
                            loadAndExecGM_GP(progs.PC);
                            break;
                        case 0:
                            System.exit(0);
                            break;
                    }
                    break;

                case 2:
                    listaProcessosPCB();
                    break;

                case 3:
                    System.out.println("Digite o id do processo a ser desalocado:");
                    int id;
                    id = sc.nextInt();
                    desaloca(id);
                    break;

                case 4:
                    System.out.println("Digite a posição inicial:");
                    int ini;
                    ini = sc.nextInt();
                    System.out.println("Digite a posição final:");
                    int fim;
                    fim = sc.nextInt();
                    dumpM(ini, fim);
                    break;

                case 5:
                    System.out.println("Digite o id do processo a ser executado:");
                    int id2;
                    id2 = sc.nextInt();
                    executa(id2);
                    //s.desaloca(id2);
                    break;

                case 6:
                    setDebugProgram(true);
                    System.out.println("\n---- Debug ativado");
                    break;
                case 7:
                    setDebugProgram(false);
                    System.out.println("\n---- Debug desativado");
                    break;

                case 8:
                    System.out.println("Digite o id do processo consultado:");
                    int id3;
                    id3 = sc.nextInt();
                    listaProcessosPorID(id3);
                    break;
                case 0:
                    System.exit(0);

            }
        }

        //--------- Menu - fim ------------------------------------------------------------

        //SistemaT1_v1 s = new SistemaT1_v1(tamMemoria, tamParticao);
        //System.out.println("Sistema iniciado");
        //System.out.println("Memória: " + tamMemoria);
        //System.out.println("Tamanho partição: " + tamParticao );
        //s.loadAndExecGM_GP(progs.fibonacci10);
        //s.loadAndExecGM_GP(progs.fatorial);
        // s.loadAndExec(progs.progMinimo);
        // s.loadAndExec(progs.fatorial);
        // s.loadAndExec(progs.fatorialTRAP); // saida
        // s.loadAndExec(progs.fibonacciTRAP); // entrada
        // s.loadAndExec(progs.PC); // bubble sort

        // Teste interrupções
        // s.loadAndExec(progs.testeLeitura);//teste leitura
        // s.loadAndExec(progs.testeEscrita);//teste escrita
        // s.loadAndExec(progs.fatorialTeste); // testando registrador inválido
        // s.loadAndExec(progs.fatorialTesteEndereco); // testando endereço inválido
        // s.loadAndExec(progs.TestandoOverflow); // testando overflow na soma
        //s.loadAndExec(progs.TestandoOverflowParam); // testando overflow no parametro recebido

    }
}
