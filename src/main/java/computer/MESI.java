package computer;

public class MESI {
    private int i, k;

    public int findLineinCache(int[][] values, int blockTag, int tagPosition) {
        for (int i = 0; i < values.length; i++) {
            if (values[i][tagPosition] == blockTag) {
                return i;
            }
        }
        return -1;
    }

    public void readHit(int[][] values, int blockTag, int tagPosition) {
        States states;
        //achar linha da cachae ->mudar o estado
        i = findLineinCache(values, blockTag, tagPosition);

        if (values[i][tagPosition + 2] == 0) {//M
            values[i][tagPosition + 2] = 0;
            states = States.M;
        } else if (values[i][tagPosition + 2] == 1) {//S
            values[i][tagPosition + 2] = 1;
            states = States.S;
        } else if (values[i][tagPosition + 2] == 2) {//E
            values[i][tagPosition + 2] = 2;
            states = States.E;
        }


    }


    public void readMiss(int[][] values, int blockTag, int tagPosition,CPU [] arrayCpu,int posCpu) {
        for(int i = 0;i<arrayCpu.length;i++){
            if(i!=posCpu){
                k = findLineinCache(arrayCpu[i].getCacheMemory().getValues(),blockTag,tagPosition);
                if(k!=-1){
                    values[i][tagPosition + 2] = 1;//S
                }

            }
        }

    }

    public void writeMiss(int[][] values, int blockTag, int tagPosition, CacheMemory arrayCache) {

    }

    public void writeHit(int[][] values, int blockTag, int tagPosition,CacheMemory arrayCache) {

    }
}