package computer;

import java.util.Scanner;

public class CPU {
    Scanner scanner = new Scanner(System.in);
    String input ;
    int data;
    MESI mesi  = new MESI();
    private final RAM ram;

    private final CacheMemory cacheMemory;
    private  int numCpu;

    public CPU(SubstitionMethods substitutionMethod,RAM ram, int numCpu) {
        ram.populateRAMWithRandomValues();
        cacheMemory = new CacheMemory(ram, substitutionMethod);
        this.ram = ram;
        this.numCpu = numCpu;
    }

    public int getValueFromMemoryPosition(int memoryPosition) {
        int[] block = cacheMemory.getBlockFromMemoryPosition(memoryPosition);
        return block[memoryPosition % CacheMemory.maxValuesPerBlock];
    }

    public void updateValueOnMemoryPosition(int newValue, int memoryPosition) {
        int blockTag = ram.getBlockTagFromMemoryPosition(memoryPosition);
        int[] block = cacheMemory.getBlockFromMemoryPosition(memoryPosition);
        int valuePositionInArray =
                memoryPosition - (CacheMemory.maxValuesPerBlock * blockTag);
        block[valuePositionInArray] = newValue;
        cacheMemory.updateBlock(block, blockTag);
    }
    //**
    private int  writeOnMP(int data, RAM ram,CPU [] arrayCpu,int posCpu) {
        return 0 ;
    }

    private int  readOnMP(int data, RAM ram,CPU [] arrayCpu,int posCpu) {
        return  0;
    }
    //**
    public void handleCommand(String cmd,CPU [] arrayCpu,int posCpu) {
        Scanner scanner = new Scanner(System.in);
        int chosenMemoryPosition;
        int newValue;

        switch(cmd){
            case "R":
                System.out.println("Qual posição você deseja acessar?");
                chosenMemoryPosition = scanner.nextInt();

                if(chosenMemoryPosition >= RAM.ramLength){
                    System.out.println("Valor fora do tamanho máximo da RAM" +
                            " (última posição disponível é a " + (RAM.ramLength-1) + ")");
                    break;
                }
                //***

                System.out.println("Valor na posição " + chosenMemoryPosition
                        + ": " + getValueFromMemoryPosition(chosenMemoryPosition));
                break;
            case "A":

                System.out.println("Qual posição você deseja atualizar?");
                chosenMemoryPosition = scanner.nextInt();
                //***


                System.out.println("Qual deve ser o novo valor na posição " + chosenMemoryPosition + "?");
                newValue = scanner.nextInt();
                updateValueOnMemoryPosition(newValue, chosenMemoryPosition);
                break;
            case "VR":
                ram.printRam();
                break;
            case "VC":
                cacheMemory.printCache();
                System.out.println("\nHit: " + cacheMemory.getHitCount());
                System.out.println("Miss: " + cacheMemory.getMissCount());
                break;
            case "E":
                System.exit(0);
                break;
            //**
            case "S":
                System.out.println("Digite L para ler um dado da memória principal ");
                System.out.println("Digite E para alterar um dado da memória principal ");
                input = scanner.next();
                System.out.print("Entre com o dado ->");
                data = scanner.nextInt();
                if(input.equals("L")){

                    readOnMP(data,ram,arrayCpu,posCpu);
                }
                else if(input.equals("E")){

                    writeOnMP(data,ram,arrayCpu,posCpu);
                }
                else System.out.println("Valor inválido");
                //**
            default:
                System.out.println("\nComando não existente, tente novamente\n");
        }
    }



    public CacheMemory getCacheMemory() {
        return cacheMemory;
    }

    public int getNumCpu() {
        return numCpu;
    }
}
