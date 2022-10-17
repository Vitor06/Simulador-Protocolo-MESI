package computer;

import java.util.Scanner;

public class CPU {
    String typeSolicitation;
    String input ;
    int data;
    private   RAM ram ;
    MESI mesi  = new MESI();
    private final CacheMemory cacheMemory;
    private  int numCpu;

    public CPU(SubstitionMethods substitutionMethod,RAM ram, int numCpu) {
        ram.populateRAMWithRandomValues();
        cacheMemory = new CacheMemory(ram, substitutionMethod);
        this.ram = ram;
        this.numCpu = numCpu;
    }

    public int getValueFromMemoryPosition(int memoryPosition,CPU cpuRequester,CPU [] arrayCpu,int posCpu) {
        int[] block = cacheMemory.getBlockFromMemoryPosition(memoryPosition,cpuRequester, arrayCpu, "R");
        return block[memoryPosition % CacheMemory.maxValuesPerBlock];
    }

    public void updateValueOnMemoryPosition(int newValue, int memoryPosition,CPU cpuRequester,CPU [] arrayCpu,int posCpu) {
        int blockTag = ram.getBlockTagFromMemoryPosition(memoryPosition);

        int[] block = cacheMemory.getBlockFromMemoryPosition(memoryPosition,cpuRequester, arrayCpu, "W");

        int valuePositionInArray =
                memoryPosition - (CacheMemory.maxValuesPerBlock * blockTag);
        block[valuePositionInArray] = newValue;
        cacheMemory.updateBlock(block, blockTag, cpuRequester, arrayCpu, posCpu,memoryPosition);
    }

    public void handleCommand(String cmd,CPU cpuRequester,CPU [] arrayCpu,int posCpu) {
        Scanner scanner = new Scanner(System.in);
        int chosenMemoryPosition;
        int newValue;

        switch(cmd){
            case "R":
                System.out.println("Qual posicao voce deseja acessar?");
                chosenMemoryPosition = scanner.nextInt();

                if(chosenMemoryPosition >= RAM.ramLength){
                    System.out.println("Valor fora do tamanho maximo da RAM" +
                            " (ultima posicao disponivel é a " + (RAM.ramLength-1) + ")");
                    break;
                }

                System.out.println("Valor na posicao " + chosenMemoryPosition
                        + ": " + getValueFromMemoryPosition(chosenMemoryPosition,cpuRequester, arrayCpu, posCpu));
                break;
            case "W":

                System.out.println("Qual posicao voce deseja atualizar?");
                chosenMemoryPosition = scanner.nextInt();

                System.out.println("Qual deve ser o novo valor na posicao " + chosenMemoryPosition + "?");
                newValue = scanner.nextInt();
                updateValueOnMemoryPosition(newValue, chosenMemoryPosition,cpuRequester, arrayCpu, posCpu);
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
            default:
                System.out.println("\nComando nao existente, tente novamente\n");
        }
    }


    public CacheMemory getCacheMemory() {
        return cacheMemory;
    }

    public int getNumCpu() {
        return numCpu;
    }
}
