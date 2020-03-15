package main.java.org.smin.dbsystems.util;

public final class Bits {


    /**
     * byte 배열을 int 형으로 변환
     * big-endian 방식임
     * @param buff
     * @param pos
     * @return
     */
    public static int readInt(byte[] buff, int pos) {
        return (
                 buff[pos++] << 24 )
                + ( ( buff[pos++] & 0xff ) << 16 ) + ( (buff[pos++] & 0xff) << 8 )
                + (buff[pos] & 0xff
        ) ;
    }
}
