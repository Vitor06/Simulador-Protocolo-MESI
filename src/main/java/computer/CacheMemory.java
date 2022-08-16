package computer;

import queue.Queue;

import java.util.Random;

public class CacheMemory {
    MESI mesi  = new MESI();

    public static int maxBlocks = 10;
    public static int maxValuesPerBlock = 2;
    public static int tagPosition = maxValuesPerBlock;
    public static int modifiedFlagPosition = maxValuesPerBlock + 1;
    private final int[][] values = new int[maxBlocks][maxValuesPerBlock + 3];
    private final SubstitionMethods substitionMethod;
    private final Queue<Integer> queue = new Queue<Integer>(Integer.class, maxBlocks);
    private final RAM ram;
    private int hit = 0;
    private int miss = 0;

    public CacheMemory(RAM ram, SubstitionMethods substitionMethod) {
        this.ram = ram;
        this.substitionMethod = substitionMethod;
        initializeCacheMemory();
    }

    private void initializeCacheMemory() {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                values[i][j] = -1;
            }
        }

    }

    private boolean isBlockStoredInCache(int blockTag) {
        for (int i = 0; i < values.length; i++) {
            if (values[i][tagPosition] == blockTag) {
                return true;
            }
        }
        return false;
    }

    private boolean isCacheFull() {
        for (int[] memoryBlock : values) {
            for (int value : memoryBlock) {
                if (value == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getNextPositionToUse() {
        int pos = -1;
        if (substitionMethod == SubstitionMethods.RANDOM) {
            Random rand = new Random();
            pos = rand.nextInt(maxBlocks + 1);
        } else if (substitionMethod == SubstitionMethods.FIFO) {
            int blockToBeRemovedTag = queue.remove();
            for(int i = 0; i < maxBlocks; i++){
                if(values[i][tagPosition] == blockToBeRemovedTag){
                    pos = i;
                    break;
                }
            }
        }
        return pos;
    }

    private int[] getBlockWithBlockTag(int blockTag) {
        int[] block = {};
        for (int i = 0; i < values.length; i++) {
            if (values[i][tagPosition] == blockTag) {
                block = values[i];
            }
        }
        return block;
    }

    private int getBlockPosWithBlockTag(int blockTag) {
        for (int i = 0; i < values.length; i++) {
            if (values[i][tagPosition] == blockTag) {
                return i;
            }
        }
        return -1;
    }

        private void storeNewBlock(int blockPosition, int[] block) {
        queue.add(block[tagPosition]);
        for (int i = 0; i < block.length; i++) {
            values[blockPosition][i] = block[i];
        }
    }

    int[] getBlockFromMemoryPosition(int memoryPosition, CPU [] arrayCpu,int posCpu) {
        int[] block = {};
        int blockTag = ram.getBlockTagFromMemoryPosition(memoryPosition);

        if (isBlockStoredInCache(blockTag)) {
            System.out.println("Cache Hit!!!");
            block = getBlockWithBlockTag(blockTag);
            hit++;
            //**
//            mesi.readHit(values,blockTag,tagPosition);

        } else {
            block = ram.getBlockFromMemoryPosition(memoryPosition);
            miss++;
//            mesi.readMiss(values,blockTag,tagPosition,arrayCpu,posCpu);
            if (!isCacheFull()) {
                //gets first empty cache position to store the block
                for (int i = 0; i < values.length; i++) {
                    if (values[i][0] == -1) {
                        storeNewBlock(i, block);
                        break;
                    }
                }
            } else {
                System.out.println("Cache Cheia!!!");
                int nextPositionToUse = getNextPositionToUse();
                removeBlockOnPosition(nextPositionToUse);
                storeNewBlock(nextPositionToUse, block);
            }
        }
        return block;
    }

    void updateBlock(int[] newBlock, int blockTag) {
        newBlock[modifiedFlagPosition] = 1;
        if (isBlockStoredInCache(blockTag)) {
            int blockPos = getBlockPosWithBlockTag(blockTag);
            values[blockPos] = newBlock;
        } else {
            if (!isCacheFull()) {
                //gets first empty cache position to store the block
                for (int i = 0; i < values.length; i++) {
                    if (values[i][0] == -1) {
                        storeNewBlock(i, newBlock);
                        break;
                    }
                }
            } else {
                System.out.println("Cache Cheia!!!");
                int nextPositionToUse = getNextPositionToUse();
                removeBlockOnPosition(nextPositionToUse);
                storeNewBlock(nextPositionToUse, newBlock);
            }
        }
    }

    private void removeBlockOnPosition(int blockPosition) {
        if (values[blockPosition][modifiedFlagPosition] == 1) {
            ram.updateMemoryBlock(values[blockPosition], values[blockPosition][tagPosition]);
        }
    }

    void printCache() {
        System.out.println("----------Cache----------");
        System.out.println("V1 | V2 | TAG | MOD |TAG MESI");
        for (int[] i : values) {
            for (int j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    int getHitCount() {
        return hit;
    }

    int getMissCount() {
        return miss;
    }

    public int[][] getValues() {
        return values;
    }
}
