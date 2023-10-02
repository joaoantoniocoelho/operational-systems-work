package paginacao.process;

import java.util.Arrays;

public class PCB {
    private int idPCB;
    private int pc;
    public int tamanhoProcesso;
    private int[] frames;


    public PCB(int idPCB, int[] frames) {
        this.idPCB = idPCB;
        this.frames = frames;
    }

    public PCB(int idPCB, int pc, int tamanhoProcesso, int [] frames) {
        this.idPCB = idPCB;
        this.pc = pc;
        this.tamanhoProcesso = tamanhoProcesso;
        this.frames = frames;
    }

    public int[] getFrames() {
        return frames;
    }

    public int getIdPCB() {
        return idPCB;
    }

    public int getPc() {
        return pc;
    }

    public int setPc(int pc) {
        return this.pc = pc;
    }

    public int getTamanhoProcesso() {
        return tamanhoProcesso;
    }

    @Override
    public String toString() {
        return "PCB [id do Programa =" + idPCB + ", pc=" + pc + ", tamanhoProcesso=" + tamanhoProcesso + " Alocação em memória: " + Arrays.toString(frames) + "]";
    }
}