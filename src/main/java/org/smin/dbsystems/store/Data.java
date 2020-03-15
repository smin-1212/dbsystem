package main.java.org.smin.dbsystems.store;

import main.java.org.smin.dbsystems.engine.Constants;
import main.java.org.smin.dbsystems.util.Bits;
import main.java.org.smin.dbsystems.util.MathUtils;
import main.java.org.smin.dbsystems.util.Utils;

public class Data {

    public static final int LENGTH_INT =4;
    private final boolean storeLocalTime;
    private final DataHandler handler;

    private byte[] data;

    private int pos;

    private Data(DataHandler handler, byte[] data, boolean storeLocalTime){
        this.handler = handler;
        this.data = data;
        this.storeLocalTime = storeLocalTime;
    }

    public static Data create(DataHandler handler, int capacity, boolean storeLocalTime) {
        return new Data(handler, new byte[capacity], storeLocalTime);
    }

    public int readByte() {
        return data[pos++];
    }

    public void reset() {
        pos = 0;
    }

    public byte[] getBytes() {
        return data;
    }

    public int readInt() {
        int x = Bits.readInt(data, pos);
        return 0;
    }

    public void checkCapacity(int plus) {
        if(pos + plus >= data.length){
            expand(plus);
        }
    }

    private void expand(int plus) {
        data = Utils.copyBytes(data, (data.length + plus) *2);
    }

    public int length() {
        return pos;
    }

    public void setPos(int i) {
        this.pos = i;
    }

    public void fillAligned() {
        int len = MathUtils.roundUpInt(pos +2, Constants.FILE_BLOCK_SIZE);
        pos = len;
        if(data.length < len){
            checkCapacity(len-data.length);
        }
    }
}
