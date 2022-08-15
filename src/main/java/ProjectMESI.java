import computer.CPU;
import computer.RAM;
import computer.SubstitionMethods;

import java.util.Objects;
import java.util.Scanner;

public class ProjectMESI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int  cmd;
        String input;

        SubstitionMethods substitionMethod = SubstitionMethods.RANDOM;
        substitionMethod = SubstitionMethods.FIFO;

        RAM ram = new RAM();
        CPU cpu = new CPU(substitionMethod,ram,"generic");
        CPU cpu1 = new CPU(substitionMethod,ram,"CPU1");
        CPU cpu2 = new CPU(substitionMethod, ram,"CPU2");
        CPU cpu3 = new CPU(substitionMethod,ram,"CPU3");

        System.out.print("Cpu1 - Cpu2 - Cpu3\n");
        System.out.print("Qual  processador?\n");
        System.out.print("Digite 1 para o processador 1 ?\n");
        System.out.print("Digite 2 para o processador 2?\n");
        System.out.print("Digite 3 para o processador 3?\n");

        while(true){
            System.out.print("--> ");
            cmd = scanner.nextInt();
            if(Objects.equals(cmd, 1)){
                cpu = cpu1;
                break;
            } else if(Objects.equals(cmd, 2)){
                cpu = cpu2;
                break;
            } else if(Objects.equals(cmd, 3)){
                cpu = cpu3;
                break;
            } else {
                System.out.println("Valor inválido, tente novamente");
            }
        }



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

            input = scanner.next();
            System.out.println(cpu.getCpuName());
            cpu.handleCommand(input);


        }
    }
}
