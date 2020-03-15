package main.java.org.smin.dbsystems.util;

public class MathUtils {

    static volatile boolean seeded;


    private MathUtils(){

    }


    public static int roundUpInt(int x, int blockSizePoserOf2){
        return (x + blockSizePoserOf2 -1) & (-blockSizePoserOf2);
    }

}
