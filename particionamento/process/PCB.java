package particionamento.process;

public class PCB {
    private int idProcesso;
    private int particao;
    private int pc;
    private int tamanhoProcesso;

    public void setPCB(int particao, int idProcesso, int pc, int tamanhoProcesso) {
        this.particao = particao;
        this.idProcesso = idProcesso;
        this.pc = pc;
        this.tamanhoProcesso = tamanhoProcesso;
    }

    public int getParticao() {
        return particao;
    }

    public int getIdProcesso() {
        return idProcesso;
    }

    public int getPc() {
        return pc;
    }

    public int getTamanhoProcesso() {
        return tamanhoProcesso;
    }
    public void setPc(int pc) {
        this.pc = pc;
    }
    public void setParticao(int particao) {
        this.particao = particao;
    }
    public void setIdProcesso(int idProcesso) {
        this.idProcesso = idProcesso;
    }
    public void setTamanhoProcesso(int tamanhoProcesso) {
        this.tamanhoProcesso = tamanhoProcesso;
    }

    @Override
    public String toString() {
        return "Processo: " + idProcesso + " na partição: " + particao + " com tamanho: " + tamanhoProcesso + " e pc: " + pc;
    }
}