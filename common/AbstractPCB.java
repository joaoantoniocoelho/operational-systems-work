package common;

public abstract class AbstractPCB {
    protected int id;
    protected int pc;
    public int tamanhoProcesso;

    public AbstractPCB(int id, int pc, int tamanhoProcesso) {
        this.id = id;
        this.pc = pc;
        this.tamanhoProcesso = tamanhoProcesso;
    }

    public AbstractPCB() {

    }

    public int getId() {
        return id;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getTamanhoProcesso() {
        return this.tamanhoProcesso;
    }

    public abstract String toString(); // Since the toString implementations are different in both classes, keep it abstract
}
