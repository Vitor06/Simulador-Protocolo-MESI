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
    /*RH,RM - OK , */
    private ArrayList<Integer> findOnotherCaches(CPU[] arrayCpu, int blockTag, CPU cpuRequester, int type) {
        ArrayList<Integer> arrayNumCpu = new ArrayList<Integer>();

        boolean valueIsInCache = false;
        for (CPU cpu: arrayCpu){
            if(cpu.getNumCpu()!=cpuRequester.getNumCpu()) {
                valueIsInCache = findDataInCache(cpu.getCacheMemory(), blockTag);
                if (valueIsInCache == true) {
                    arrayNumCpu.add( cpu.getNumCpu());
                }
            }
        }
        return arrayNumCpu;
    }

    private void chageEstateCacheMemory(CPU[] arrayCpu,ArrayList<Integer> arrayNumCpu,int blockTag,int type) {
        for (CPU cpu: arrayCpu){
            if(arrayNumCpu.contains( cpu.getNumCpu())){
                int[][] valuesCache = cpu.getCacheMemory().getValues();
                i = findLineInCacheMemory(valuesCache,blockTag);
                if(type==0){
                    valuesCache[i][tagEstate] = 2;
                }
                else if(type==1){
                    valuesCache[i][tagEstate] = 3   ;
                }
            }
        }
    }

    private boolean  findDataInCache(CacheMemory cache, int blockTag) {
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

    public void  readMiss(int blockTag,CPU [] arrayCpu,CPU cpuRequester) {
        /*0 ->M , 1 ->E ,2->S, 3->I*/
        //Procurar nas outras caches
        int type = 0;//RM
        ArrayList<Integer> arrayNumCpu;
        arrayNumCpu = findOnotherCaches(arrayCpu, blockTag, cpuRequester, type);
        if(arrayNumCpu.size()>0){
             i = findLineInCacheMemory(cpuRequester.getCacheMemory().getValues(),blockTag);
             cpuRequester.getCacheMemory().getValues()[i][tagEstate] = 2;
             chageEstateCacheMemory(arrayCpu, arrayNumCpu, blockTag,type);
        }
        else{
            cpuRequester.getCacheMemory().getValues()[i][tagEstate] = 1;
        }
    }


    public void writeMiss(int blockTag, CPU[] arrayCpu, CPU cpuRequester) {
        int type = 1;//WM
        int cache[][] = cpuRequester.getCacheMemory().getValues();
        ArrayList<Integer> arrayNumCpu;
        i = findLineInCacheMemory(cache, blockTag);
        cache[i][tagEstate] = 0;
        arrayNumCpu = findOnotherCaches(arrayCpu, blockTag, cpuRequester, type);
        chageEstateCacheMemory(arrayCpu, arrayNumCpu, blockTag,type);


    }
    public void writeHit(int blockTag, CPU[] arrayCpu, CPU cpuRequester) {
        int type = 2;//WH
        int cache[][] = cpuRequester.getCacheMemory().getValues();
        ArrayList<Integer> arrayNumCpu;
        arrayNumCpu = findOnotherCaches(arrayCpu, blockTag, cpuRequester, type);
        if(arrayNumCpu.size()>0){
            i = findLineInCacheMemory(cache, blockTag);
            cache[i][tagEstate] = 0;
            type = 1;
            chageEstateCacheMemory(arrayCpu, arrayNumCpu, blockTag,type);
        }
        else {
            i = findLineInCacheMemory(cache, blockTag);
            cache[i][tagEstate] = 0;
            type = 1;
        }

    }

    private boolean dataIsIncache(int data, CPU cpuRequester,int blockTag) {
        CacheMemory cpuRequesterCacheMemory = cpuRequester.getCacheMemory();

        return cpuRequesterCacheMemory.isBlockStoredInCache(blockTag);

        //calcular a tag do bloco

//    }
//    private void printArray(ArrayList<Boolean> array){
//        for (int i =0;i<array.size();i++){
//            System.out.println(array.get(i));
//
//        }
    }


}