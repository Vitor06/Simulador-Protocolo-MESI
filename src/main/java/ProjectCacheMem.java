import computer.CPU;
import computer.SubstitionMethods;
import queue.Queue;

import java.util.Objects;
import java.util.Scanner;

public class ProjectCacheMem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String cmd;
        SubstitionMethods substitionMethod = SubstitionMethods.RANDOM;

        System.out.println("Qual método de substituição deve ser adotado?\n");
        System.out.println("Digite A para substituição aleatória");
        System.out.println("Digite F para substituição por fila FIFO");

        while(true){
            System.out.print("--> ");
            cmd = scanner.next();
            if(Objects.equals(cmd, "A")){
                substitionMethod = SubstitionMethods.RANDOM;
                break;
            } else if(Objects.equals(cmd, "F")){
                substitionMethod = SubstitionMethods.FIFO;
                break;
            } else {
                System.out.println("Valor inválido, tente novamente");
            }
        }
        CPU cpu = new CPU(substitionMethod);

        while(true){
            System.out.println("\n-------------------------");
            System.out.println("Qual comando a CPU deve executar?\n");
            System.out.println("Comandos disponíveis:");
            System.out.println("Digite R para resgatar um valor em determinada posição da RAM");
            System.out.println("Digite A para atualizar um valor em determinada posição da RAM");
            System.out.println("Digite VR para visualizar os valores atualmente guardados na RAM");
            System.out.println("Digite VC para visualizar os valores atualmente guardados na cache\n" +
                    "e o número de hits e misses no acesso à cache");
            System.out.println("Digite S para sair do programa");
            System.out.print("--> ");

            cmd = scanner.next();
            cpu.handleCommand(cmd);
        }
    }
}
