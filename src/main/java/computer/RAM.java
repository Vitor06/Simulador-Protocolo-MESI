package computer;

import java.util.Random;

public class RAM {
    public static int ramLength = 10240;
    private int[] values = new int[ramLength];

    public RAM() {
    }

    void populateRAMWithRandomValues() {
        Random random = new Random();

        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = random.nextInt(1000);
        }
    }
    //leitura
    int[] getBlockFromMemoryPosition(int memoryPosition) {
        int[] block = new int[CacheMemory.maxValuesPerBlock + 2];
        int blockTag = getBlockTagFromMemoryPosition(memoryPosition);

        for (int i = 0; i < CacheMemory.maxValuesPerBlock; i++) {
            block[i] = values[(blockTag * CacheMemory.maxValuesPerBlock) + i];
        }
        block[CacheMemory.tagPosition] = blockTag;
        block[CacheMemory.modifiedFlagPosition] = 0;

        return block;
    }

    int getBlockTagFromMemoryPosition(int memoryPosition) {
        return Math.floorDiv(memoryPosition, CacheMemory.maxValuesPerBlock);
    }
    //escrita
    void updateMemoryBlock(int[] newBlock, int blockTag) {
        for(int i = 0; i < CacheMemory.maxValuesPerBlock; i++){
            values[blockTag * CacheMemory.maxValuesPerBlock + i] = newBlock[i];
        }
    }
    
    void printRam() {
        System.out.println("-----------Ram-----------");
        for (int i = 0; i < ramLength; i++) {
            System.out.print("Pos " + i + ": " + this.values[i]);
            System.out.println();
        }
        System.out.println("-------------------------");
    }
}
