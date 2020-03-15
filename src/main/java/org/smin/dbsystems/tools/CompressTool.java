package main.java.org.smin.dbsystems.tools;

import main.java.org.smin.dbsystems.engine.Constants;

public class CompressTool {
    private static final int MAX_BUFFER_SIZE = 3 * Constants.IO_BUFFER_SIZE_COMPRESS;

    private CompressTool(){

    }


    public static CompressTool getInstance(){
        return new CompressTool();
    }
}
