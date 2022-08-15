package computer;

import java.util.Scanner;

public class CPU {
    private final RAM ram;
    private final CacheMemory cacheMemory;

    public CPU(SubstitionMethods substitutionMethod) {
        ram = new RAM();
        ram.populateRAMWithRandomValues();
        cacheMemory = new CacheMemory(ram, substitutionMethod);
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

    public void handleCommand(String cmd) {
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

                System.out.println("Valor na posição " + chosenMemoryPosition
                        + ": " + getValueFromMemoryPosition(chosenMemoryPosition));
                break;
            case "A":
                System.out.println("Qual posição você deseja atualizar?");
                chosenMemoryPosition = scanner.nextInt();
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
            case "S":
                System.exit(0);
                break;
            default:
                System.out.println("\nComando não existente, tente novamente\n");
        }
    }
}
