package paginacao.ui;

import paginacao.core.Sistema1B;
import paginacao.memory.Word;
import paginacao.process.PCB;
import paginacao.programas.Programas;

import java.util.Arrays;
import java.util.Scanner;

public class MenuB {

    private static Sistema1B sistema;

    public MenuB(Sistema1B sistema) {
        this.sistema = sistema;
    }

    //case 1
    private static void loadAndExecGM_GP(Word[] p) {
        //aciona o GP para criar processos
        //gm.imprimeParticao(vm.m, 0, 128);
        sistema.gp.criaProcesso(p);
        //aciona o GM para imprimir partição
        //gm.imprimeParticao(vm.m, 0, 128);

    }

    //case 2
    private static void listaProcessosPCB() {
        sistema.gp.listaProcessos();
    }

    //case 3
    private static void desaloca(int id) {
        try{
            PCB pcbProcesso = sistema.prontos.get(id);
            sistema.gp.desalocaProcesso(pcbProcesso);
        }
        catch (Exception e){
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
        //executa o processo

        try {

            PCB pcbProcesso = sistema.gp.getPCB(idProcesso);
            int pcProcesso = pcbProcesso.getPc();
            int[] paginasProcesso = pcbProcesso.getFrames();
            int ultimoFrame = paginasProcesso[paginasProcesso.length-1];
            int offset = sistema.gm.tamFrame % pcbProcesso.getTamanhoProcesso();
            int posLimite = ultimoFrame*(sistema.gm.tamFrame)+ offset;
            int posInicio = sistema.gm.traduzEnderecoFisico(pcbProcesso,0);





            System.out.println("-      Executando processo: " + idProcesso + " nos frames : " + Arrays.toString(paginasProcesso) + " com pc em: " + pcProcesso + " e tamanho de programa: " + pcbProcesso.getTamanhoProcesso());

            sistema.gm.dumpPagina(pcbProcesso);

            System.out.println("---------------------------------- inicia execucao ");


            sistema.vm.cpu.setContext(posInicio, posLimite, pcProcesso, paginasProcesso); // seta estado da cpu ]
            sistema.vm.cpu.run();

            System.out.println("---------------------------------- memoria após execucao ");
            sistema.gm.dumpPagina(pcbProcesso); // dump da memoria com resultado
        }
        catch(IndexOutOfBoundsException e) {
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

        try{
            PCB pcbProcesso = sistema.gp.getPCB(id);
            System.out.println("------- Processo encontrado: ");
            System.out.println("\nPCB: " + pcbProcesso.toString());
            sistema.gm.dumpPagina(pcbProcesso);

        }
        catch (Exception e){
            System.out.println("------- Processo não encontrado");
        }
    }

    // ------------------- GM GP PCB - inicio


    public static void main(String args[]) {
        int tamMemoria = 1024;
        int tamPagina = 8;
        sistema = new Sistema1B(tamMemoria, tamPagina);

        //--------- Menu ----------------------------------------------------------------
        Scanner sc = new Scanner(System.in);

        Sistema1B s = new Sistema1B(tamMemoria, tamPagina);


        while (true) {
            System.out.println(" \n____________________________________________________________________________________");
            System.out.println("    Escolha uma opção:");
            System.out.println("    1 - Cria programa - cria um processo com memória alocada, PCB, etc.");
            System.out.println("    2 - ListaProcessos - lista os processos prontos para rodar.");
            System.out.println("    3 - Desaloca - retira o processo id do sistema.");
            System.out.println("    4 - DumpM - lista a memória entre posições início e fim.");
            System.out.println("    5 - Executa - executa o processo com id fornecido.");
            System.out.println("    6 - TraceOn - liga modo de execução em que CPU print cada instrução executada.");
            System.out.println("    7 - TraceOff - desliga o modo acima TraceOn.");
            System.out.println("    8 - DumpID - lista o conteúdo do PCB e o conteúdo das páginas de memória do processo com id.");
            System.out.println("    0 - Sair - encerra o programa.");
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
                    System.out.println("        7 - pc");
                    System.out.println("        0 - Sair \n");


                    System.out.print("Informe o numero de um programa: ");
                    int option2;
                    option2 = sc.nextInt();

                    switch (option2) {
                        case 1:
                            loadAndExecGM_GP(Programas.fatorial);
                            break;
                        case 2:
                            loadAndExecGM_GP(Programas.progMinimo);
                            break;
                        case 3:
                            loadAndExecGM_GP(Programas.fibonacci10);
                            break;
                        case 4:
                            loadAndExecGM_GP(Programas.fatorialTRAP);
                            break;
                        case 5:
                            loadAndExecGM_GP(Programas.fibonacciTRAP);
                            break;
                        case 6:
                            loadAndExecGM_GP(Programas.PB);
                            break;
                        case 7:
                            loadAndExecGM_GP(Programas.PC);
                            break;
                        case 0:
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
                    System.out.println("Digite o id do processo a ser consultado:");
                    int id3;
                    id3 = sc.nextInt();
                    listaProcessosPorID(id3);
                    break;
                case 0:
                    System.exit(0);

            }
        }

    }

}
