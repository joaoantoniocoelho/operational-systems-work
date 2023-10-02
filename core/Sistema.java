package core;

import memory.Word;
import ui.Menu;

public class Sistema {
    private Menu menu;
    private String mode;

    public Sistema(int tamMem, int tamPartOrPage, String mode) {
        this.mode = mode;
        if (mode.equals("pagination")) {
            menu = new Menu(new Sistema1B(tamMem, tamPartOrPage));
        } else {
            menu = new Menu(new Sistema1A(tamMem, tamPartOrPage));
        }
    }

    public void loadAndExecGM_GP(Word[] p) {
        menu.loadAndExecGM_GP(p);
    }

    public void listaProcessosPCB() {
        menu.listaProcessosPCB();
    }

    public void desaloca(int id) {
        menu.desaloca(id);
    }

    public void dumpM(int inicio, int fim) {
        menu.dumpM(inicio, fim);
    }

    public void executa(int idProcesso) {
        menu.executa(idProcesso);
    }

    public void setDebugProgram(boolean debug) {
        menu.setDebugProgram(debug);
    }

    public void listaProcessosPorID(int id) {
        menu.listaProcessosPorID(id);
    }
}