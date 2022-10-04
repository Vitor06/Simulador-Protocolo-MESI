import computer.CPU;
import computer.RAM;
import computer.SubstitionMethods;

import java.util.Objects;
import java.util.Scanner;

public class ProjectMESI {
    public static void main(String[] args) {
        RAM ram = new RAM();
        Scanner scanner = new Scanner(System.in);
        CPU cpu;
        int  numCpu ;
        int posCpu;
        String input;
        String cmd;

        SubstitionMethods substitionMethod = SubstitionMethods.RANDOM;
        substitionMethod = SubstitionMethods.FIFO;

        System.out.println("Quantos Processadores?");
        numCpu= scanner.nextInt();
        CPU [] arrayCpuAux = new CPU[numCpu];
        CPU [] arrayCpu = new CPU[numCpu];
        arrayCpu = storeCpu(numCpu,arrayCpuAux,substitionMethod,ram);

        System.out.print("Qual processador?\n");
        System.out.print("Digite 1 para o processador 1, 2 para o processador 2 ,...?\n");

        posCpu = scanner.nextInt();
        posCpu--;

        while(true){

            cpu = arrayCpu[posCpu];
            System.out.println("Processador : " +(cpu.getNumCpu()+1));
            menu();
            input = scanner.next();
            cpu.handleCommand(input, cpu, arrayCpu,posCpu);


            System.out.println("Deseja Trocar de processador? Digite Y para sim e N para não");
            cmd = scanner.next();


            if(cmd.equals("Y")){
                System.out.print("Qual  processador?\n");
                posCpu = scanner.nextInt();
                posCpu--;

            }
        }

    }

    private static CPU[] storeCpu(int numCpu, CPU[] arrayCpu, SubstitionMethods substitionMethod, RAM ram) {

        for (int i =0;i<numCpu;i++){
            arrayCpu[i] = new CPU(substitionMethod,ram,i);
        }
        return arrayCpu;
    }



    public static void menu(){
        System.out.println("\n-------------------------");
        System.out.println("Qual comando a CPU deve executar?\n");
        System.out.println("Comandos disponíveis:");
        System.out.println("Digite R para Solicitar uma leitura");
        System.out.println("Digite W para Solicitar uma escrita");
        System.out.println("Digite VR para visualizar os valores atualmente guardados na RAM");
        System.out.println("Digite VC para visualizar os valores atualmente guardados na cache\n" +
                "e o número de hits e misses no acesso à cache");
        System.out.println("Digite E para sair do programa");

    }

}
