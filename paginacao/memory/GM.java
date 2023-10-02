package paginacao.memory;

import paginacao.cpu.Opcode;
import paginacao.process.PCB;

import java.util.Arrays;

public class GM {

    private Word[] m;
    private int tamMem;
    private int tamPagina;
    public int tamFrame;
    private int numFrames;
    public boolean[] tabelaPaginas;
    private int[] framesParaPrograma;

    //public boolean[] particoesLivres;

    public GM(Word[] m, int tamMem, int tamPagina) {
        this.m = m;
        this.tamMem = tamMem;
        this.tamPagina = tamPagina;
        numFrames = tamMem / tamPagina;
        tamFrame = tamPagina;
        this.tabelaPaginas = new boolean[numFrames];
        for (int i = 0; i < numFrames; i++) {
            tabelaPaginas[i] = true;
        }

        //simulando partição ocupada
        tabelaPaginas[0]=false;
        tabelaPaginas[2]=false;
        tabelaPaginas[4]=false;
    }


    public int[] alocaPagina(Word[] programa) {
        int tamanhoPrograma = programa.length; //pega o tamanho do programa
        int numPaginasPrograma = tamanhoPrograma / tamPagina; // calcula o numero de paginas necessárias
        if (tamanhoPrograma % tamPagina != 0) { // alocar mais uma pagina, se o tamanho do programa dividido pelo tamanho de pagina não resultar em divisão inteira
            numPaginasPrograma++;
        }
        framesParaPrograma = new int[numPaginasPrograma]; // cria um vetor de frames com o tamanho do numero de paginas necessárias


        //percorre o tabela de página para achar espaço livre que seja possível alocar o programa cnfme numPaginasPrograma
        int frames = 0;
        for(int i=0; i < numFrames; i++){
            if(tabelaPaginas[i] == true){
                frames++;
            }
        }
        //int posPrograma = 0;
        int posVetorFrames = 0;
        if(frames < numPaginasPrograma){ //se não houver espaço livre para alocar o programa, ou seja o numero de frames livres é menor que o numero de paginas necessárias
            System.out.println("----- Não há espaço para alocar o programa");
            framesParaPrograma[0] = -1;
            return framesParaPrograma;
        }else{

            for(int i=0; i < numFrames; i++){
                if(numPaginasPrograma > 0){// se a posição estiver livre, aloca
                    if(tabelaPaginas[i] == true){
                        tabelaPaginas[i] = false; //marca como ocupado a posição na tabela de páginas

                        framesParaPrograma[posVetorFrames] = i;
                        posVetorFrames++;
                        numPaginasPrograma--;

                    }
                }
            }
            System.out.println("-     Gerente de memória: Alocar programa nos frames: " + Arrays.toString(framesParaPrograma));

            return framesParaPrograma;
        }

    }

    public void desalocaPagina(PCB pcbProcesso) {
        int[] paginas = pcbProcesso.getFrames();
        System.out.println("-     Gerente de memória: Desalocar programa nos frames: " + Arrays.toString(paginas));

        for(int i = 0; i < paginas.length; i++) {
            tabelaPaginas[paginas[i]] = true; //desaloca da tabela de páginas do processo -- tabela de paginas na pos do frame, recebe true
            System.out.println("\n-     Gerente de memória: Página desalocada: " + paginas[i]);


            //libera a memória
            for(int j = paginas[i]*tamFrame; j < (paginas[i]+1)*tamFrame; j++){    //frame inicia em (f)*tamFrame --- ex: 1 * 8 = 8
                //frame termina em (f+1)*tamFrame -1 --- ex:(1+1)*8-1= 15
                m[j].opc =  Opcode.___;
                m[j].r1 = -1;
                m[j].r2 = -1;
                m[j].p = -1;

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


    public void imprimeMemoria(Word[] m, int inicio, int fim) {
        for (int i = inicio; i <= fim; i++) {
            System.out.print("Posição da Memória " + i);
            System.out.print(":  ");
            dump(m[i]);

        }
    }

    public int traduzEnderecoFisico(PCB programa, int enderecoLogicoPrograma) {// função que traduz para o enederço físico da memória
        if (enderecoLogicoPrograma < 0 || enderecoLogicoPrograma > tamMem) {
            System.out.println("----- Endereço inválido");
            return -1;
        }
        int [] framesDoPrograma = programa.getFrames();
        int paginaDoPrograma = enderecoLogicoPrograma / tamPagina; // a pagina é a divisão inteira do endereço lógico pelo tamanho da página

        int offsetDoPrograma = enderecoLogicoPrograma % tamPagina; // offset é a posição exata dentro da página
        int frameDoEndereco = framesDoPrograma[paginaDoPrograma]; // utiliza a pagina do programa para pegar o frame exato no vetor de frames do programa
        int enderecoFisico = frameDoEndereco * tamFrame + offsetDoPrograma; // (f)*tamFrame
        return enderecoFisico;
    }

    public void dumpPagina(PCB programa){

        for (int i = 0; i <= programa.tamanhoProcesso; i++) {
            int enderecoFisico = traduzEnderecoFisico(programa, i);
            dump(m[enderecoFisico]);
        }

    }

}