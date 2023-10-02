import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Particionamento");
            System.out.println("2. Paginação");
            System.out.println("3. Sair");
            System.out.print("Sua escolha: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    particionamento.ui.MenuA.main(args);
                    break;
                case 2:
                    paginacao.ui.MenuB.main(args);
                    break;
                case 3:
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }
}
