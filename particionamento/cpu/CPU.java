package particionamento.cpu;

import particionamento.memory.Memory;
import particionamento.memory.Word;
import particionamento.utils.Interrupts;

public class CPU {
    private int maxInt;
    private int minInt;

    //private int tamParticao;


    // característica do processador: contexto da CPU ...
    public int pc; // ... composto de program counter,
    private Word ir; // instruction register,
    public int[] reg; // registradores da CPU
    private Interrupts irpt; // durante instrucao, interrupcao pode ser sinalizada
    private int base; // base e limite de acesso na memoria
    private int limite; // por enquanto toda memoria pode ser acessada pelo processo rodando


    private Memory mem; // mem tem funcoes de dump e o array m de memória 'fisica'
    private Word[] m; // CPU acessa MEMORIA, guarda referencia a 'm'. m nao muda. semre será um array
    // de palavras

    private InterruptHandling ih; // significa desvio para rotinas de tratamento de Int - se int ligada, desvia
    private SysCallHandling sysCall; // significa desvio para tratamento de chamadas de sistema - trap


    private int particao; // numero da particao em que está o programa rodando
    private int tamParticao;
    public boolean debug;


    public CPU(Memory mem, int tamParticao, InterruptHandling _ih, SysCallHandling _sysCall) { // ref a MEMORIA e
        // interrupt handler
        // passada na
        // criacao da CPU
        maxInt = 32767; // capacidade de representacao modelada
        minInt = -32767; // se exceder deve gerar interrupcao de overflow
        this.mem = mem; // usa mem para acessar funcoes auxiliares (dump)
        this.m = mem.m; // usa o atributo 'm' para acessar a memoria.
        reg = new int[10]; // aloca o espaço dos registradores - regs 8 e 9 usados somente para IO
        ih = _ih; // aponta para rotinas de tratamento de int
        sysCall = _sysCall; // aponta para rotinas de tratamento de chamadas de sistema
        this.tamParticao = tamParticao;
    }



    private boolean legal(int e) { // todo acesso a memoria tem que ser verificado
        if ((e < minInt) || (e > maxInt)) {
            irpt = Interrupts.intOverflow;
            return false;
        }
        ;
        return true;
    }

    private boolean enderecoValido(int end) { // se acessar endereço fora do limite
        if ((end < base) || (end > limite)) {
            irpt = Interrupts.intEnderecoInvalido;
            return false;
        }
        ;
        return true;
    }

    private boolean registradorValido(int registrador) {// verifica registrador valido 0 a 7
        if (registrador < 0 || registrador > reg.length) {
            irpt = Interrupts.intRegistradorInvalido;
            return false;
        }
        ;
        return true;
    }

    private boolean testOverflow(int v) { // toda operacao matematica deve avaliar se ocorre overflow
        if ((v < minInt) || (v > maxInt)) {
            irpt = Interrupts.intOverflow;
            return false;
        }
        ;
        return true;
    }

    public void setContext(int _base, int _limite, int _pc, int particao) { // no futuro esta funcao vai ter que ser
        base = _base; // expandida para setar todo contexto de execucao,
        limite = _limite; // agora, setamos somente os registradores base,
        pc = _pc; // limite e pc (deve ser zero nesta versao)
        irpt = Interrupts.noInterrupt; // reset da interrupcao registrada
        this.particao = particao;
    }

    public int traduzEnderecoProcesso(int endereco) {
        if (enderecoValido(endereco)) {
            endereco = (particao * tamParticao) - 1 + endereco;
            return endereco;
        }
        System.out.println("Erro ao traduzir endereço do processo");
        System.out.println("Endereço: " + endereco);
        System.out.println("Partição: " + particao);
        System.out.println("Tamanho da Partição: " + tamParticao);

        irpt = Interrupts.intEnderecoInvalido;
        return -1;
    }


    public void run() { // execucao da CPU supoe que o contexto da CPU, vide acima, esta devidamente
        // setado
        while (true) { // ciclo de instrucoes. acaba cfe instrucao, veja cada caso.
            // --------------------------------------------------------------------------------------------------
            // FETCH

            //traduz para endereço dos processos


            if (legal(pc)) { // pc valido
                ir = m[pc]; // <<<<<<<<<<<< busca posicao da memoria apontada por pc, guarda em ir
                if (debug) {
                    System.out.print("                               pc: " + pc + "       exec: ");
                    mem.dump(ir);
                }
                // --------------------------------------------------------------------------------------------------
                // EXECUTA INSTRUCAO NO ir
                switch (ir.opc) { // conforme o opcode (código de operação) executa

                    // Instrucoes de Busca e Armazenamento em Memoria
                    case LDI: // Rd <- k
                        if (registradorValido(ir.r1) && testOverflow(ir.p)) {
                            reg[ir.r1] = ir.p;
                            pc++;
                            break;
                        } else
                            break;

                    case LDD: // Rd <- [A]
                        if (registradorValido(ir.r1) && enderecoValido(traduzEnderecoProcesso(ir.p)) && testOverflow(m[ir.p].p)) {
                            reg[ir.r1] = m[traduzEnderecoProcesso(ir.p)].p;
                            pc++;
                            break;
                        } else
                            break;

                    case LDX: // RD <- [RS] // NOVA carga indireta
                        if (registradorValido(ir.r1) && registradorValido(ir.r2) && enderecoValido(traduzEnderecoProcesso(reg[ir.r2]))
                                && testOverflow(m[reg[ir.r2]].p)) {
                            reg[ir.r1] = m[traduzEnderecoProcesso(reg[ir.r2])].p;
                            pc++;
                            break;
                        } else
                            break;

                    case STD: // [A] ← Rs armazena na memória
                        if (registradorValido(ir.r1) && enderecoValido(traduzEnderecoProcesso(ir.p)) && testOverflow(reg[ir.r1])) {
                            m[traduzEnderecoProcesso(ir.p)].opc = Opcode.DATA;
                            m[traduzEnderecoProcesso(ir.p)].p = reg[ir.r1];
                            pc++;
                            break;
                        } else
                            break;

                    case STX: // [Rd] ←Rs armazenamento indireto na memória
                        if (registradorValido(ir.r1) && registradorValido(ir.r2) && enderecoValido(traduzEnderecoProcesso(reg[ir.r1]))
                                && testOverflow(reg[ir.r2])) {
                            m[traduzEnderecoProcesso(reg[ir.r1])].opc = Opcode.DATA;
                            m[traduzEnderecoProcesso(reg[ir.r1])].p = reg[ir.r2];
                            pc++;
                            break;
                        } else
                            break;

                    case MOVE: // RD <- RS
                        if (registradorValido(ir.r1) && registradorValido(ir.r2)) {
                            reg[ir.r1] = reg[ir.r2];
                            pc++;
                            break;
                        } else
                            break;

                        // Instrucoes Aritmeticas
                    case ADD: // Rd ← Rd + Rs
                        if (registradorValido(ir.r1) && registradorValido(ir.r2) &&
                                testOverflow(reg[ir.r1]) && testOverflow(reg[ir.r2]) &&
                                testOverflow(reg[ir.r1] + reg[ir.r2])) {
                            reg[ir.r1] = reg[ir.r1] + reg[ir.r2];
                            // testOverflow(reg[ir.r1]);
                            pc++;
                            break;
                        } else
                            pc++;
                        break;

                    case ADDI: // Rd ← Rd + k adição imediata
                        if (registradorValido(ir.r1) && testOverflow(reg[ir.r1]) && testOverflow(reg[ir.p]) &&
                                testOverflow(reg[ir.r1] + ir.p)) {
                            reg[ir.r1] = reg[ir.r1] + ir.p;
                            pc++;
                            break;
                        } else
                            pc++;
                        break;

                    case SUB: // Rd ← Rd - Rs
                        if (registradorValido(ir.r1) && registradorValido(ir.r2)
                                && testOverflow(reg[ir.r1]) && testOverflow(reg[ir.r2])
                                && testOverflow(reg[ir.r1] - reg[ir.r2])) {
                            reg[ir.r1] = reg[ir.r1] - reg[ir.r2];
                            pc++;
                            break;
                        } else
                            pc++;
                        break;

                    case SUBI: // RD <- RD - k // NOVA
                        if (registradorValido(ir.r1) && testOverflow(ir.p) && testOverflow(reg[ir.r1])
                                && testOverflow(reg[ir.p]) && testOverflow(reg[ir.r1] - ir.p)) {
                            reg[ir.r1] = reg[ir.r1] - ir.p;
                            pc++;
                            break;
                        } else
                            pc++;
                        break;

                    case MULT: // Rd <- Rd * Rs
                        if (registradorValido(ir.r1) && registradorValido(ir.r2) && testOverflow(reg[ir.r1])
                                && testOverflow(reg[ir.r2]) && testOverflow(reg[ir.r1] * reg[ir.r2])) {
                            reg[ir.r1] = reg[ir.r1] * reg[ir.r2];
                            pc++;
                            break;
                        } else
                            pc++;
                        break;

                    // Instrucoes JUMP
                    case JMP: // PC <- k desvio incondicional
                        if (enderecoValido(traduzEnderecoProcesso(ir.p))) {
                            pc = traduzEnderecoProcesso(ir.p);
                            break;
                        } else
                            break;

                    case JMPIG: // If Rc > 0 Then PC ← Rs Else PC ← PC +1 desvio condicinal
                        if (registradorValido(ir.r2) && registradorValido(ir.r1) && enderecoValido(traduzEnderecoProcesso(reg[ir.r1]))) {
                            if (reg[ir.r2] > 0) {
                                pc = reg[ir.r1];
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPIGK: // If RC > 0 then PC <- k else PC++
                        if (registradorValido(ir.r2) && enderecoValido(traduzEnderecoProcesso(ir.p))) {
                            if (reg[ir.r2] > 0) {
                                pc = traduzEnderecoProcesso(ir.p);
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPILK: // If RC < 0 then PC <- k else PC++
                        if (registradorValido(ir.r2) && enderecoValido(traduzEnderecoProcesso(ir.p))) {
                            if (reg[ir.r2] < 0) {
                                pc = traduzEnderecoProcesso(ir.p);
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPIEK: // If RC = 0 then PC <- k else PC++
                        if (registradorValido(ir.r2) && enderecoValido(traduzEnderecoProcesso(ir.p))) {
                            if (reg[ir.r2] == 0) {
                                pc = traduzEnderecoProcesso(ir.p);
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPIL: // if Rc < 0 then PC <- Rs Else PC <- PC +1
                        if (registradorValido(ir.r2) && registradorValido(ir.r1) &&
                                enderecoValido(traduzEnderecoProcesso(reg[ir.r1]))) {
                            if (reg[ir.r2] < 0) {
                                pc = reg[ir.r1];
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPIE: // If Rc = 0 Then PC <- Rs Else PC <- PC +1
                        if (registradorValido(ir.r2) && registradorValido(ir.r1) && enderecoValido(traduzEnderecoProcesso(reg[ir.r1]))) {
                            if (reg[ir.r2] == 0) {
                                pc = reg[ir.r1];
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPIM: // PC <- [A]
                        if (enderecoValido(traduzEnderecoProcesso(ir.p)) && enderecoValido(m[traduzEnderecoProcesso(ir.p)].p)) {
                            pc = m[traduzEnderecoProcesso(ir.p)].p;
                            break;
                        } else {
                            break;
                        }
                    case JMPIGM: // If RC > 0 then PC <- [A] else PC++
                        if (registradorValido(ir.r2) && enderecoValido(traduzEnderecoProcesso(ir.p)) &&
                                enderecoValido(m[traduzEnderecoProcesso(ir.p)].p)) {
                            if (reg[ir.r2] > 0) {
                                pc = m[traduzEnderecoProcesso(ir.p)].p;
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPILM: // If RC < 0 then PC <- k else PC++
                        if (registradorValido(ir.r2) && enderecoValido(traduzEnderecoProcesso(ir.p)) && enderecoValido(m[traduzEnderecoProcesso(ir.p)].p)) {
                            if (reg[ir.r2] < 0) {
                                pc = m[traduzEnderecoProcesso(ir.p)].p;
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPIEM: // If RC = 0 then PC <- k else PC++
                        if (registradorValido(ir.r2) && enderecoValido(traduzEnderecoProcesso(ir.p)) && enderecoValido(m[traduzEnderecoProcesso(ir.p)].p)) {
                            if (reg[ir.r2] == 0) {
                                pc = m[traduzEnderecoProcesso(ir.p)].p;
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                    case JMPIGT: // If RS>RC then PC <- k else PC++
                        if (registradorValido(ir.r2) && registradorValido(ir.r1) && enderecoValido(traduzEnderecoProcesso(ir.p))) {
                            if (reg[ir.r1] > reg[ir.r2]) {
                                pc = traduzEnderecoProcesso(ir.p);
                            } else {
                                pc++;
                            }
                            break;
                        } else {
                            break;
                        }

                        // outras
                    case STOP: // por enquanto, para execucao
                        irpt = Interrupts.intSTOP;
                        break;

                    case DATA:
                        pc++;
                        break;

                    // Chamada de sistema
                    case TRAP:
                        sysCall.handle(); // <<<<< aqui desvia para rotina de chamada de sistema, no momento so
                        // temos IO
                        pc++;
                        break;

                    // Inexistente
                    default:
                        irpt = Interrupts.intInstrucaoInvalida;
                        break;
                }
            }
            // --------------------------------------------------------------------------------------------------
            // VERIFICA INTERRUPÇÃO !!! - TERCEIRA FASE DO CICLO DE INSTRUÇÕES
            if (!(irpt == Interrupts.noInterrupt)) { // existe interrupção
                ih.handle(irpt, pc); // desvia para rotina de tratamento
                break; // break sai do loop da cpu
            }
        } // FIM DO CICLO DE UMA INSTRUÇÃO
    }
}