package particionamento.utils;

public enum Interrupts { // possiveis interrupcoes que esta CPU gera
    noInterrupt, intEnderecoInvalido, intInstrucaoInvalida, intRegistradorInvalido, intOverflow, chamadaTrap,
    intSTOP;
}