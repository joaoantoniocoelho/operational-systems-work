package memory;

import processA.PCB_A;

import java.util.LinkedList;

public class GM {

    private Word[] m;
    private Memory mem;
    private int tamMem;
    public int tamPart;
    private int partAlocada;
    private int numPart;
    private LinkedList<PCB_A> prontos;

    public boolean[] particoesLivres;

    public GM(int tamMem, int tamPart) {
        this.tamMem = tamMem;
        this.tamPart = tamPart;
        numPart = tamMem / tamPart;
        this.partAlocada = -1;
        this.particoesLivres = new boolean[numPart];
        for (int i = 0; i < numPart; i++) {
            particoesLivres[i] = true;
        }

        //simulando partição ocupada
        //particoesLivres[0]=false;
    }


    public int alocaParticao(Word[] programa) {
        if (programa.length > tamPart) {
            System.out.println("----- Programa muito grande para o tamanho de partição");
            return -1;
        }

        for (int i = 0; i < numPart; i++) {//percorre as partiçoes, a primeira que estiver livre retorna para o GP
            if (particoesLivres[i]) {
                particoesLivres[i] = false;
                partAlocada = i * tamPart;
                System.out.println("-     Gerente de memória: Alocar na partição: " + i + " que inicia na posição: " + partAlocada);
                return i;
            }
        }
        System.out.println("----- Não há partições livres");
        return 0; // se 0 significa que não há partições livres


    }

    public void desalocaParticao(PCB_A pcbAProcesso) {
        //particao = particao/tamPart;
        int particao = pcbAProcesso.getParticao();

            /*liberando na memória
            for (int i = particao; i < particao + pcbProcesso.getTamanhoProcesso() -1; i++) {
                if (particao == i) {
                    m[particao].opc = Opcode.___;
                    m[particao].r1 = -1;
                    m[particao].r2 = -1;
                    m[particao].p = -1;
                }
            }
            */
        //liberando no vetor de partições livres
        for(int i = 0; i < numPart; i++){
            if(particao == i){
                particoesLivres[i] = true;
                System.out.println("*** Partição desalocada: " + particao);
            }
        }
    }

    public void dump(Word w) {
        System.out.print("[ ");
        System.out.print(w.opc);
        System.out.print(", ");
        System.out.print(w.r1);
        System.out.print(", ");
        System.out.print(w.r2);
        System.out.print(", ");
        System.out.print(w.p);
        System.out.println("  ] ");
    }


    public void imprimeParticao(Word[] m, int inicio, int fim) {
        //System.out.println("Partição: " + inicio + " até " + fim);
        //int inicio = traduzEnderecoFisico(prontos.get().getParticao());

        for (int i = inicio; i <= fim; i++) {
            System.out.print("Posição da Memória " + i);
            System.out.print(":  ");
            dump(m[i]);

        }
    }

    private int traduzEnderecoFisico(int enderecoPrograma, int particaoPrograma) {// função que traduz para o enederço físico da memória
        if (enderecoPrograma < 0 || enderecoPrograma > tamPart) {
            System.out.println("----- Endereço inválido");
            return -1;
        }
        int enderecoFisico = (particaoPrograma * tamPart) - 1 + enderecoPrograma;
        return enderecoFisico;
    }

    public int traduzEnderecoFisico(int particaoPrograma) {// função que traduz para o enederço físico da memória
        int enderecoFisico = (particaoPrograma * tamPart);
        return enderecoFisico;
    }

}
