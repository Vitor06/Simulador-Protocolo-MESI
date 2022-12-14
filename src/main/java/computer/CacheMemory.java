package computer;

import queue.Queue;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Random;

public class CacheMemory {

    public static int maxBlocks = 10;
    public static int maxValuesPerBlock = 2;
    public static int tagPosition = maxValuesPerBlock;
    public static int modifiedFlagPosition = maxValuesPerBlock + 1;
    public static int TagEstatePosition = maxValuesPerBlock + 2;
    private final int[][] values = new int[maxBlocks][maxValuesPerBlock + 3];
    private final SubstitionMethods substitionMethod;
    private final Queue<Integer> queue = new Queue<Integer>(Integer.class, maxBlocks);
    private final RAM ram;
    private int hit = 0;
    private int miss = 0;
    private boolean isCache;
    MESI mesi  = new MESI();


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

    public boolean isBlockStoredInCache(int blockTag) {
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

        public void storeNewBlock(int blockPosition, int[] block) {
        queue.add(block[tagPosition]);
        for (int i = 0; i < block.length; i++) {
            values[blockPosition][i] = block[i];
        }
    }

    int[] getBlockFromMemoryPosition(int memoryPosition,CPU cpuRequester,CPU [] arrayCpu,String type) {
        int[] block = {};
        int blockTag = ram.getBlockTagFromMemoryPosition(memoryPosition);

        if (isBlockStoredInCache(blockTag)) {
            block = getBlockWithBlockTag(blockTag);
            hit++;
            if(type.equals("R"))mesi.readHit();
            else if(type.equals("W"))mesi.writeHit(blockTag,arrayCpu,cpuRequester);
        } else {

            block = ram.getBlockFromMemoryPosition(memoryPosition);
            miss++;
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
            if(type.equals("R")) mesi.readMiss(blockTag,arrayCpu,cpuRequester,ram);
            else if(type.equals("W"))  mesi.writeMiss(blockTag,arrayCpu,cpuRequester,ram,block,memoryPosition);
        }
        return block;
    }

    void updateBlock(int[] newBlock, int blockTag,CPU cpuRequester,CPU [] arrayCpu,int posCpu,int memoryPosition) {

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
        /*0 ->M , 1 ->E ,2->S, 3->I*/
        System.out.println("M(Modificado)->0 | E(Exclusivo)->1 | S(compartilhado)->2 | I(invalido)->3");
        System.out.println("----------Cache----------");
        System.out.println("V1 | V2 | TAG |TAG MESI");
        for (int[] i : values) {
            int k = 0;
            for (int j : i) {
                if(k!=3) { System.out.print(j + " ");}
                k++;
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
