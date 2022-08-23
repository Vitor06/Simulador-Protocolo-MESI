package computer;

import java.util.ArrayList;

public class MESI {
    public static int maxValuesPerBlock = 2;
    public static int tagPosition = maxValuesPerBlock;
    public static int tagEstate= tagPosition + 2;
    int tagMesi;
    String typeSolicitation;
    int [] valuesRam;
    CPU [] arrayCpu;
    int posCpu;
    int i;
    int k = 0;
    boolean isValue;
    int numCpu;

    private ArrayList<Boolean> findOnothercaches(CPU[] arrayCpu, int posCpu, int memoryPosition, int blockTag) {
        ArrayList<Boolean>isInCache = new ArrayList<>() ;
        for (CPU cpu: arrayCpu){
            isValue = findDataInCache(cpu.getCacheMemory(), posCpu, memoryPosition, blockTag);

            if(isValue==true) {
                numCpu = cpu.getNumCpu();
                chageEstate(arrayCpu,numCpu,blockTag);
            }
            isInCache.add(isValue);
        }
        return isInCache;
    }

    private void chageEstate(CPU[] arrayCpu,int numCpu,int blockTag) {
        int[][] valuesCache = arrayCpu[numCpu].getCacheMemory().getValues();
        i = findLineInCacheMemory(valuesCache,blockTag);
        valuesCache[i][tagEstate] = 2;

    }

    private boolean  findDataInCache(CacheMemory cache, int posCpu, int data, int blockTag) {
        return cache.isBlockStoredInCache(blockTag);
    }
    public int findLineInCacheMemory(int[][] values, int blockTag) {
        for (int i = 0; i < values.length; i++) {
            if (values[i][tagPosition] == blockTag) {
                return i;
            }
        }
        return -1;
    }
    public void readHit() {
            //Mantém estado
            System.out.println("Read Hit");

    }

    public boolean readMiss(int blockTag,CPU [] arrayCpu,int posCpu,int memoryPosition) {
        /*0 ->M , 1 ->E ,2->S, 3->I*/
        //Procurar nas outras caches
        ArrayList<Boolean>isInCacheAux = new ArrayList<>();
        isInCacheAux = findOnothercaches(arrayCpu,posCpu,memoryPosition,blockTag);
        if(isInCacheAux.contains(true)){
           return true;
        }
        else{
            return false;
        }

    }

    public void writeMiss(int[][] values, int blockTag, int tagPosition, CacheMemory arrayCache) {

    }

    public void writeHit(int[][] values, int blockTag, int tagPosition,CacheMemory arrayCache) {

    }
    /*
    public void execute(String typeSolicitation, CPU[] arrayCpu, int posCpu,int data,RAM ram) {
        int blockTag = ram.getBlockTagFromMemoryPosition(data);
        CPU cpuRequester = arrayCpu[posCpu];
        if(typeSolicitation=="R"){
            if(dataIsIncache(data,cpuRequester,blockTag)){
                readHit();
            }
            else{

                readMiss(arrayCpu,posCpu, data,blockTag);
                cpuRequester.getCacheMemory().printCache();
            }
        }
        else if(typeSolicitation=="W"){

        }
    }

     */

    private boolean dataIsIncache(int data, CPU cpuRequester,int blockTag) {
        CacheMemory cpuRequesterCacheMemory = cpuRequester.getCacheMemory();

        return cpuRequesterCacheMemory.isBlockStoredInCache(blockTag);

        //calcular a tag do bloco

    }
    private void printArray(ArrayList<Boolean> array){
        for (int i =0;i<array.size();i++){
            System.out.println(array.get(i));

        }
    }
}