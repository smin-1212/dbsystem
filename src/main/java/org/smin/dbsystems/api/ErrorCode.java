package main.java.org.smin.dbsystems.api;

public class ErrorCode {

    public static final int NO_DATA_AVAILABLE =2000;

    public static final int IO_EXCEPTION_1 = 90028;

    public static final int IO_EXCEPTION_2 = 90031;




    public static String getState(int errorCode){
        switch (errorCode){
            case NO_DATA_AVAILABLE : return "02000" ;

            default:
                return Integer.toString(errorCode);
        }
    }
}
