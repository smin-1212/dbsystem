package main.java.org.smin.dbsystems.util;

import main.java.org.smin.dbsystems.engine.SysProperties;

public class IOUtils {

    private IOUtils(){
        // utility class
    }

    public static void closeSilently(AutoCloseable out){
        if(out != null){
            try{
                trace("", null, out);
                out.close();
            }catch (Exception e){
                // ignore
            }
        }
    }

    public static void trace(String method, Object fileName, Object out) {
        if(SysProperties.TRACE_IO){
            System.out.println("IOUtils." + method + ' ' + fileName + ' ' + out);
        }
    }
}
