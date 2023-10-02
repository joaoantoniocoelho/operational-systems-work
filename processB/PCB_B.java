package processB;

import common.AbstractPCB;

import java.util.Arrays;

public class PCB_B extends AbstractPCB {
    private int idPCB;
    private int pc;
    public int tamanhoProcesso;
    private int[] frames;


    public PCB_B(int idPCB, int[] frames) {
        super(idPCB, 0, 0); // Assuming default values for pc and tamanhoProcesso
        this.idPCB = idPCB;
        this.frames = frames;
    }

    public PCB_B(int idPCB, int pc, int tamanhoProcesso, int[] frames) {
        super(idPCB, pc, tamanhoProcesso);
        this.idPCB = idPCB;
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

    public void setPc(int pc) {
        this.pc = pc;
    }


    public int getTamanhoProcesso() {
        return tamanhoProcesso;
    }

    @Override
    public String toString() {
        return "PCB [id do Programa =" + idPCB + ", pc=" + pc + ", tamanhoProcesso=" + tamanhoProcesso + " Alocação em memória: " + Arrays.toString(frames) + "]";
    }
}