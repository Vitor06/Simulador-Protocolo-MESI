package computer;
import java.util.ArrayList;

public class MESI {
    public static int maxValuesPerBlock = 2;
    public static int tagPosition = maxValuesPerBlock;
    public static int tagState = tagPosition + 2;
    /*RH,RM - OK , */
    private ArrayList<Integer> findInOtherCache(CPU[] arrayCpu, int blockTag, CPU cpuRequester, int type) {
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

    private ArrayList<Integer> getEstate( CPU arrayCpu [],ArrayList<Integer> arrayNumCpu , int blockTag) {
            int i;
            ArrayList<Integer> arrayEstate = new ArrayList<Integer>();
            CPU cpu;

            for (Integer numCpu: arrayNumCpu){
                cpu = arrayCpu[numCpu];
                int [][] cache = cpu.getCacheMemory().getValues();
                i = getLineInCacheMemory(cache,blockTag);
                arrayEstate.add(cache[i][tagState]) ;
            }
            return  arrayEstate;
    }

    private void chageEstateCacheMemory(CPU[] arrayCpu,ArrayList<Integer> arrayNumCpu,int blockTag,int type) {
        int i;
        for (CPU cpu: arrayCpu){
            if(arrayNumCpu.contains( cpu.getNumCpu())){
                int[][] valuesCache = cpu.getCacheMemory().getValues();
                i = getLineInCacheMemory(valuesCache,blockTag);
                if(type==0){
                    valuesCache[i][tagState] = 2;
                }
                else if(type==1){
                    valuesCache[i][tagState] = 3   ;
                }
                else if(type==2){
                    valuesCache[i][tagState] = 0   ;
                }
            }
        }
    }

    private boolean  findDataInCache(CacheMemory cache, int blockTag) {
        return cache.isBlockStoredInCache(blockTag);
    }
    public int getLineInCacheMemory(int[][] values, int blockTag) {
        for (int i = 0; i < values.length; i++) {
            if (values[i][tagPosition] == blockTag) {
                return i;
            }
        }
        return -1;
    }
    private void chageValuesOfblock(CPU cpuRequester, int blockTag, int[] blockModify) {
        int i;
        int [] [] cache = cpuRequester.getCacheMemory().getValues();
        i = getLineInCacheMemory(cache,blockTag);
        for(int j=0;j<cache[i].length;j++){
            cache[i][j] = blockModify[j];
        }
    }

    private int getIndex(ArrayList<Integer> arrayEstate,int e) {
        for (int i =0 ; i<arrayEstate.size();  i++){
            if(arrayEstate.get(i) == e){
                return i;
            }
        }
        return 0;
    }

    public void readHit() {
        System.out.println("---------------");
        System.out.println("\nREAD HIT\n");
        System.out.println("---------------");
    }

    public void  readMiss(int blockTag,CPU [] arrayCpu,CPU cpuRequester,RAM ram) {
        int i;
        System.out.println("---------------");
        System.out.println("\nREAD MIS\n");
        System.out.println("---------------");

        int type = 0;//RM
        int [][] cahecpuRequesterCacheMemory  = cpuRequester.getCacheMemory().getValues();
        ArrayList<Integer> arrayNumCpu;
        ArrayList<Integer> arrayEstate;
        arrayNumCpu = findInOtherCache(arrayCpu, blockTag, cpuRequester, type);
        arrayEstate = getEstate(arrayCpu,arrayNumCpu,blockTag);

        if(arrayNumCpu.size()>0 &&  !arrayEstate.contains(0)){

             i = getLineInCacheMemory(cahecpuRequesterCacheMemory,blockTag);
             cahecpuRequesterCacheMemory[i][tagState] = 2;
             chageEstateCacheMemory(arrayCpu, arrayNumCpu, blockTag,type);
        }
        else if(arrayNumCpu.size()>0 &&  arrayEstate.contains(0)==true){
            //passar para o cpu requisitante o dado modificado na outra cache
            CPU cpuWithModifyValue;
            int j = getIndex(arrayEstate,0);
            Integer NumCacheModify = arrayNumCpu.get(j);
            cpuWithModifyValue = arrayCpu[NumCacheModify];
            i = getLineInCacheMemory(cpuWithModifyValue.getCacheMemory().getValues(),blockTag);
            int[] blockModify = cpuWithModifyValue.getCacheMemory().getValues()[i];

            chageValuesOfblock(cpuRequester,blockTag,blockModify);
            cpuWithModifyValue.getCacheMemory().getValues()[i][tagState] = 2;

            i = getLineInCacheMemory(cahecpuRequesterCacheMemory,blockTag);
            cahecpuRequesterCacheMemory[i][tagState] = 2;
            ram.updateMemoryBlock(blockModify,blockTag);
        }
        else{
            i = getLineInCacheMemory(cpuRequester.getCacheMemory().getValues(),blockTag);
            cahecpuRequesterCacheMemory[i][tagState] = 1;
        }
    }

    public void writeMiss(int blockTag, CPU[] arrayCpu, CPU cpuRequester,RAM ram,int [] newBlock,int memoryPosition) {
        int i;
        System.out.println("---------------");
        System.out.println("\nWRITE MISS\n");
        System.out.println("---------------");

        int type = 1;//WM
        int [][] cpuRequesterCacheMemory  = cpuRequester.getCacheMemory().getValues();
        ArrayList<Integer> arrayNumCpu;
        ArrayList<Integer> arrayEstate;
        i = getLineInCacheMemory(cpuRequesterCacheMemory, blockTag);
        CPU cpuWithModifyValue;
        cpuRequesterCacheMemory[i][tagState] = 0;
        arrayNumCpu = findInOtherCache(arrayCpu, blockTag, cpuRequester, type);
        arrayEstate = getEstate(arrayCpu,arrayNumCpu,blockTag);

        if(arrayEstate.size()>0 && arrayEstate.contains(0)!=true){
            invalidateLineInCaches(arrayNumCpu,blockTag,arrayCpu);
        }

        else if(arrayNumCpu.size()>0 && arrayEstate.contains(0)==true){
            int j = getIndex(arrayEstate,0);
            Integer NumCacheModify = arrayNumCpu.get(j);
            cpuWithModifyValue = arrayCpu[NumCacheModify];
            i = getLineInCacheMemory(cpuWithModifyValue.getCacheMemory().getValues(),blockTag);
            int[] blockModify = cpuWithModifyValue.getCacheMemory().getValues()[i];
            ram.updateMemoryBlock(blockModify,blockTag);
            cpuWithModifyValue.getCacheMemory().getValues()[i][tagState]=3;

            //passar para ivalido
            int [] block = ram.getBlockFromMemoryPosition(memoryPosition);
            chageValuesOfblock(cpuRequester,blockTag,newBlock);
            i = getLineInCacheMemory(cpuRequesterCacheMemory,blockTag);
            cpuRequesterCacheMemory[i][tagState] = 0;
        }
    }

    private void invalidateLineInCaches(ArrayList<Integer> arrayNumCpu, int blockTag, CPU[] arrayCpu) {
        int j;
        for (int i :arrayNumCpu){
            int [][] cache = arrayCpu[i].getCacheMemory().getValues();
            j = getLineInCacheMemory(cache,blockTag);
            if(cache[j][tagState]==1 || cache[j][tagState]==2 ){
               cache[j][tagState]=3;
            }
        }
    }

    public void writeHit(int blockTag, CPU[] arrayCpu, CPU cpuRequester) {
        System.out.println("---------------");
        System.out.println("\nWRITE HIT\n");
        System.out.println("---------------");

        int i;
        int type =0;
        int [][] cpuRequesterCacheMemory  = cpuRequester.getCacheMemory().getValues();
        ArrayList<Integer> arrayNumCpu;
        ArrayList<Integer> arrayEstate;
        ArrayList<Integer> arrayNumCpuShare;
        arrayNumCpu = findInOtherCache(arrayCpu, blockTag, cpuRequester,type);
        arrayEstate = getEstate(arrayCpu,arrayNumCpu,blockTag);


        if(arrayNumCpu.size()>0){
            arrayNumCpuShare = getCpu(arrayEstate,arrayNumCpu);
             i = getLineInCacheMemory(cpuRequester.getCacheMemory().getValues(),blockTag);
            cpuRequesterCacheMemory[i][tagState]=0;
            invalidateLineInCaches(arrayNumCpuShare,blockTag,arrayCpu);

        }
         else {
             i = getLineInCacheMemory(cpuRequester.getCacheMemory().getValues(),blockTag);
             cpuRequesterCacheMemory[i][tagState]=0;
        }
    }
    private ArrayList<Integer> getCpu(ArrayList<Integer> arrayEstate, ArrayList<Integer> arrayNumCpu) {
        ArrayList<Integer> arrayCpuShare = new ArrayList<Integer>();
        int j;
        for (int i =0; i<arrayEstate.size();i++){
            if(arrayEstate.get(i)==2){

                arrayCpuShare.add(arrayNumCpu.get(i));
            }
        }
        return arrayCpuShare;
    }
}