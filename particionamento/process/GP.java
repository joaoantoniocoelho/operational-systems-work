package particionamento.process;


import particionamento.cpu.Opcode;
import particionamento.memory.GM;
import particionamento.memory.Word;

import java.util.LinkedList;

public class GP {
    private GM gm;
    private Word[] mem;
    private int idProcesso;
    private LinkedList<PCB> prontos;
    private int idProcessoRodando;


    public GP() {
        this.idProcesso = 0;
        this.idProcessoRodando = 0;
    }

    public void carregaGP(GM gm, Word[] mem, LinkedList<PCB> prontos) {
        this.gm = gm;
        this.mem = mem;
        this.prontos = prontos;
    }

    public boolean criaProcesso(Word[] programa) {
        int particao = gm.alocaParticao(programa);
        if (particao == -1) {
            System.out.println("----- Não foi possível ciar processo em memória");
            return false;
        }
        //carga do processo na partição retornada por GM
        for (int i = 0; i < programa.length; i++) {
            mem[gm.traduzEnderecoFisico(particao) + i].opc = programa[i].opc;
            mem[gm.traduzEnderecoFisico(particao) + i].r1 = programa[i].r1;
            mem[gm.traduzEnderecoFisico(particao) + i].r2 = programa[i].r2;
            mem[gm.traduzEnderecoFisico(particao) + i].p = programa[i].p;

        }
			/*
			for (int i = 0; i < programa.length; i++) {
				mem[particao + i].opc = programa[i].opc;
				mem[particao + i].r1 = programa[i].r1;
				mem[particao + i].r2 = programa[i].r2;
				mem[particao + i].p = programa[i].p;
			}
			 */

        PCB pcbProcesso = new PCB();
        int pcProcesso = gm.traduzEnderecoFisico(particao);
        pcbProcesso.setPCB(particao, idProcesso, pcProcesso, programa.length);
        prontos.add(pcbProcesso);
        System.out.println("-     Gerente de processos: Processo criado com id: " + idProcesso + " na posição: " + particao + " com tamanho: " + programa.length + " e pc: " + pcProcesso + " \n");
        idProcesso++;

        return true;

    }

    public void desalocaProcesso(int idProcesso) {
        PCB pcbProcesso = prontos.get(idProcesso);

        int inicioDesaloca = gm.traduzEnderecoFisico(pcbProcesso.getParticao());
        int fimDesaloca = inicioDesaloca + pcbProcesso.getTamanhoProcesso()-1;
        //remoção do processo na partição
        for (int i = inicioDesaloca; i <= fimDesaloca; i++) {
            mem[i].opc = Opcode.___;
            mem[i].r1 = -1;
            mem[i].r2 = -1;
            mem[i].p = -1;
        }

        gm.desalocaParticao(pcbProcesso);
        System.out.println("*** Processo de id " + idProcesso + " desalocado com sucesso. ***\n");

        prontos.get(idProcesso).setPCB(-1, -1, -1, -1);
        //prontos.remove(idProcesso);
    }

    public void imprimeProcessoPorID(int id) {
        PCB pcbProcesso = prontos.get(id);
        int particao = pcbProcesso.getParticao();
        int inicio = gm.traduzEnderecoFisico(particao);
        int fim = inicio + pcbProcesso.getTamanhoProcesso();
        for (int i = inicio; i <= fim; i++) {
            gm.dump(mem[i]);
        }
    }

    public void listaProcessos() {
        System.out.println("\n        ***** Lista de processos criados: *****\n");
        if (prontos.isEmpty()) {
            System.out.println("    Nenhum processo criado até o momento.\n");
        } else {
            for (int i = 0; i < prontos.size(); i++) {
                PCB pcbProcesso = prontos.get(i);
                if(pcbProcesso.getParticao() != -1) {
                    System.out.println("-      Processo: " + pcbProcesso.getIdProcesso() + " na partição: " + (prontos.get(i).getParticao()) + " posição física: " + gm.traduzEnderecoFisico(prontos.get(i).getParticao()));

                }

            }
        }

    }

    public void listaProcessosPCB() {
        System.out.println("\n*****       Lista de PCB de cada processos: *****");
        if (prontos.isEmpty()) {
            System.out.println("    Nenhum processo criado até o momento.\n");
        } else {
            for (int i = 0; i < prontos.size(); i++) {
                PCB pcbProcesso = prontos.get(i);
                if(pcbProcesso.getParticao() != -1) {
                    System.out.println("-       Processo: " + pcbProcesso.getIdProcesso() + " na partição: " + (prontos.get(i).getParticao()) + " tamanho do processo: " + pcbProcesso.getTamanhoProcesso() + " posição física: " + gm.traduzEnderecoFisico(prontos.get(i).getParticao()));
                }
            }
        }
    }

}