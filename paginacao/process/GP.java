package paginacao.process;

import paginacao.memory.GM;
import paginacao.memory.Word;

import java.util.Arrays;
import java.util.LinkedList;

public class GP {
    private GM gm;
    private Word[] mem;
    private int idProcesso;
    private LinkedList<PCB> prontos;



    public GP() {
        this.idProcesso = 0;

    }

    public PCB getPCB(int id) {
        return  prontos.get(id);
    }

    public void carregaGP(GM gm, Word[] mem, LinkedList<PCB> prontos) {
        this.gm = gm;
        this.mem = mem;
        this.prontos = prontos;
    }

    public boolean criaProcesso(Word[] programa) {
        int[] paginasAlocadas = gm.alocaPagina(programa);
        //System.out.println(paginasAlocadas.length);
        if (paginasAlocadas[0] == -1) {
            System.out.println("----- Não foi possível criar processo em memória");
            return false;
        }
        //carga do processo na partição retornada por GM
        PCB processo = new PCB(idProcesso, 0, programa.length, paginasAlocadas);
        //int pcProcesso = gm.traduzEnderecoFisico(processo, 0); // traduz o endereço lógico do pc do programa para o endereço físico
        //int pcProcesso = paginasAlocadas[0] * gm.tamFrame; // traduz o endereço lógico do pc do programa para o endereço físico
        //processo.setPc(pcProcesso);
        int indicePrograma=0;
        for (int i=0; i<paginasAlocadas.length; i++) {
            //System.out.println("i: " + i);
            //System.out.println("Pagina alocada: " + paginasAlocadas[i]);
            int indice = paginasAlocadas[i]*gm.tamFrame;
            for (int j=indice; j<indice+gm.tamFrame; j++) {
                if(indicePrograma>=programa.length) {
                    break;
                }
                mem[j].opc = programa[indicePrograma].opc;
                mem[j].r1 = programa[indicePrograma].r1;
                mem[j].r2 = programa[indicePrograma].r2;
                mem[j].p = programa[indicePrograma].p;
                indicePrograma++;
            }
        }


        prontos.add(processo);
        System.out.println("\n-     Gerente de processos: Processo criado com id: " + idProcesso + " com tamanho: " + programa.length +  " distribuído nos Frames: "+ Arrays.toString(paginasAlocadas));
        idProcesso++;

        return true;

    }

    public void desalocaProcesso(PCB processo) {


        gm.desalocaPagina(processo);
        System.out.println("\n*** Processo de id " + processo.getIdPCB() + " desalocado com sucesso. ***\n");

        prontos.remove(processo);
    }

    public void imprimeProcessoPorID(int id) {
        PCB pcbProcesso = prontos.get(id);
        int [] paginasAlocadas = pcbProcesso.getFrames();

        int endereco=0;
        for (int j = 0; j < paginasAlocadas.length; j++) {
            int inicio = gm.traduzEnderecoFisico(pcbProcesso, endereco);
            for (int i = inicio; i <= gm.tamFrame; i++) {
                gm.dump(mem[i]);
            }
            endereco++;

        }
    }

    public void listaProcessos() {
        System.out.println("\n        ***** Lista de processos criados: *****\n");
        if (prontos.isEmpty()) {
            System.out.println("    Nenhum processo criado até o momento.\n");
        } else {
            for (int i = 0; i < prontos.size(); i++) {
                PCB pcbProcesso = prontos.get(i);
                if(pcbProcesso.getPc() != -1) {
                    System.out.println("-      Processo: " + pcbProcesso.getIdPCB() + " na Frames ocupados: " + Arrays.toString(pcbProcesso.getFrames()) );

                }

            }
        }

    }
}