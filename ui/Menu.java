package ui;

import common.AbstractPCB;
import core.Sistema1A;
import core.Sistema1B;
import memory.GM_Pagination;
import memory.Word;
import processA.PCB_A;
import processB.PCB_B;

import java.util.Arrays;
import java.util.Scanner;

import static common.Programas.*;


public class Menu {

    private static Object sistema; // Using Object to allow for different systems.

    public Menu(Object sistema) {
        this.sistema = sistema;
    }

    //case 1
    public static void loadAndExecGM_GP(Word[] p) {
        if (sistema instanceof Sistema1A) {
            ((Sistema1A) sistema).gpA.criaProcesso(p);
        } else if (sistema instanceof Sistema1B) {
            ((Sistema1B) sistema).gpB.criaProcesso(p);
        }
    }

    //case 2
    public static void listaProcessosPCB() {
        if (sistema instanceof Sistema1A) {
            ((Sistema1A) sistema).gpA.listaProcessosPCB();
        } else if (sistema instanceof Sistema1B) {
            ((Sistema1B) sistema).gpB.listaProcessos();
        }
    }

    //case 3
    public static void desaloca(int id) {
        try {
            if (sistema instanceof Sistema1A) {
                ((Sistema1A) sistema).gpA.desalocaProcesso(id);
            } else if (sistema instanceof Sistema1B) {
                AbstractPCB pcb = ((Sistema1B) sistema).prontos.get(id);
                ((Sistema1B) sistema).gpB.desalocaProcesso((PCB_B) pcb);
            }
        } catch (Exception e) {
            System.out.println("------- Processo não encontrado");
        }
    }

    //case 4
    public static void dumpM(int inicio, int fim) {
        System.out.println("-      Imprimindo memoria: de " + inicio + " até " + fim);
        try {
            if (sistema instanceof Sistema1A) {
                ((Sistema1A) sistema).vm.mem.dump(inicio, fim);
            } else if (sistema instanceof Sistema1B) {
                ((Sistema1B) sistema).vm.mem.dump(inicio, fim);
            }
        } catch (Exception e) {
            System.out.println("------- Memória não encontrada");
        }
    }

    //case 5
    public static void executa(int idProcesso) {
        try {
            AbstractPCB pcbProcesso;
            if (sistema instanceof Sistema1A) {
                pcbProcesso = ((Sistema1A) sistema).prontos.get(idProcesso);
                int pcProcesso = pcbProcesso.getPc();
                int particaoProcesso = ((PCB_A) pcbProcesso).getParticao();
                int tamanhoProcesso = pcProcesso + pcbProcesso.getTamanhoProcesso();
                System.out.println("-      Executando processo: " + idProcesso + " na particao: " + particaoProcesso + " com pc: " + pcProcesso + " e tamanho: " + pcbProcesso.getTamanhoProcesso());
                ((Sistema1A) sistema).vm.mem.dump(pcProcesso, tamanhoProcesso);
                System.out.println("---------------------------------- inicia execucao ");
                ((Sistema1A) sistema).vm.cpu.setContext(0, ((Sistema1A) sistema).vm.tamMem - 1, pcProcesso, particaoProcesso);
                ((Sistema1A) sistema).vm.cpu.run();
                System.out.println("---------------------------------- memoria após execucao ");
                ((Sistema1A) sistema).vm.mem.dump(pcProcesso, tamanhoProcesso);
            } else {
                pcbProcesso = ((Sistema1B) sistema).prontos.get(idProcesso);
                int pcProcesso = pcbProcesso.getPc();

                int[] paginasProcesso = ((PCB_B) pcbProcesso).getFrames();
                int ultimoFrame = paginasProcesso[paginasProcesso.length-1];
                int tamFrame = ((Sistema1B) sistema).gm.tamFrame;
                int offset =  tamFrame % pcbProcesso.tamanhoProcesso;

                int posLimite = ultimoFrame*tamFrame+ offset;
                int posInicio = ((Sistema1B) sistema).gm.traduzEnderecoFisico((PCB_B) pcbProcesso, 0);





                System.out.println("-      Executando processo: " + idProcesso + " nos frames : " + Arrays.toString(paginasProcesso) + " com pc em: " + pcProcesso + " e tamanho de programa: " + pcbProcesso.getTamanhoProcesso());

                ((Sistema1B) sistema).gm.dumpPagina((PCB_B) pcbProcesso); // dump da memoria antes da execucao

                System.out.println("---------------------------------- inicia execucao ");


                ((Sistema1B) sistema).vm.cpu.setContext(posInicio, posLimite, pcProcesso, paginasProcesso); // seta estado da cpu
                ((Sistema1B) sistema).vm.cpu.run(); // executa cpu

                System.out.println("---------------------------------- memoria após execucao ");
                ((Sistema1B) sistema).gm.dumpPagina((PCB_B) pcbProcesso); // dump da memoria após a execucao
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("----- Processo não encontrado");
        }
    }


    //case 6 e 7
    public static void setDebugProgram(boolean debug) {
        if (sistema instanceof Sistema1A) {
            ((Sistema1A) sistema).vm.cpu.debug = debug;
            ((Sistema1A) sistema).vm.setDebug(debug);
        } else if (sistema instanceof Sistema1B) {
            ((Sistema1B) sistema).vm.cpu.debug = debug;
            ((Sistema1B) sistema).vm.setDebug(debug);
        }
    }

    //case 8
    public static void listaProcessosPorID(int id) {
        try {
            AbstractPCB pcbProcesso;
            if (sistema instanceof Sistema1A) {
                pcbProcesso = ((Sistema1A) sistema).prontos.get(id);
                int inicio = ((PCB_A) pcbProcesso).getParticao() * ((Sistema1A) sistema).gm.tamPart;
                int fim = inicio + pcbProcesso.getTamanhoProcesso();
                System.out.println("------- Processo encontrado: ");
                System.out.println("------- Conteúdo do PCB: " + pcbProcesso.toString());
                System.out.println("------- Conteúdo da memória: ");
                ((Sistema1A) sistema).vm.mem.dump(inicio, fim);
            } else {
                pcbProcesso = ((Sistema1B) sistema).prontos.get(id);
                // Handle the case for Sistema1B if needed
            }
        } catch (Exception e) {
            System.out.println("------- Processo não encontrado");
        }
    }



    public static void main(String args[]) {

        int tamMemoria = 1024;
        int tamParticao = 64;

        Scanner sc = new Scanner(System.in);

        while (true) {
            if (sistema == null) {
                System.out.println("Escolha um modo:");
                System.out.println("1. Sistema1A");
                System.out.println("2. Sistema1B");
                int escolha = sc.nextInt();
                if (escolha == 1) {
                    sistema = new Sistema1A(tamMemoria, tamParticao);
                } else {
                    sistema = new Sistema1B(tamMemoria, tamParticao); // Supondo que Sistema1B tem um construtor similar.
                }
                Menu menu = new Menu(sistema);
            }

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
                        System.out.println("        1 - Fibonacci");
                        System.out.println("        2 - ProgMinimo");
                        System.out.println("        3 - Fatorial");
                        System.out.println("        4 - fatorialTRAP");
                        System.out.println("        5 - fibonacciTRAP");
                        System.out.println("        6 - bubble sort");
                        System.out.println("        7 - testeLeitura");
                        System.out.println("        8 - testeEscrita");
                        System.out.println("        0 - Sair \n");



                        System.out.print("Informe o numero de um programa: ");
                        int option2;
                        option2 = sc.nextInt();

                        switch (option2) {
                            case 1:
                                loadAndExecGM_GP(fibonacci10);
                                break;
                            case 2:
                                loadAndExecGM_GP(progMinimo);
                                break;
                            case 3:
                                loadAndExecGM_GP(fatorial);
                                break;
                            case 4:
                                loadAndExecGM_GP(fatorialTRAP);
                                break;
                            case 5:
                                loadAndExecGM_GP(fibonacciTRAP);
                                break;
                            case 6:
                                loadAndExecGM_GP(PC);
                                break;
                            case 7:
                                loadAndExecGM_GP(testeLeitura);
                                break;
                            case 8:
                                loadAndExecGM_GP(testeEscrita);
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
                        System.out.println("Digite o id do processo consultado:");
                        int id3;
                        id3 = sc.nextInt();
                        listaProcessosPorID(id3);
                        break;
                    case 0:
                        System.exit(0);

                }
            }

            // Lembre-se de fazer pequenas alterações nos métodos auxiliares para que eles possam funcionar com ambos os sistemas.
            // Por exemplo, você pode verificar o tipo do sistema usando instanceof e, em seguida, fazer um cast para o tipo correto.
        }
    }
}
