package main.java.org.smin.dbsystems.util;

import java.util.Arrays;
import java.util.HashMap;

public class Utils {

    public static final byte[] EMPTY_BYTES = {};

    public static final int[] EMPTY_INT_ARRAY = {};

    public static final long[] EMPTY_LONG_ARRAY = {};

    private static final HashMap<String, byte[]> RESOURCES = new HashMap();

    private Utils(){
        // utility class
    }

    public static int getProperty(String key, int defaultValue){
        String s = getProperty(key, null);
        if(s != null){
            try{
                return Integer.decode(s);
            }catch (NumberFormatException e){
                // ignore
            }
        }
        return defaultValue;
    }

    public static String getProperty(String key, String defaultValue){
        try{
            return System.getProperty(key, defaultValue);
        }catch (SecurityException se){
            return defaultValue;
        }
    }

    public static boolean getProperty(String key, boolean defaultValue) {
        return parseBoolean(getProperty(key, null), defaultValue, false);
    }

    private static boolean parseBoolean(String value, boolean defaultValue, boolean throwException) {
        if(value == null){
            return defaultValue;
        }
        switch (value.length()){
            case 1:
                if(value.equals("1") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("y")){
                    return true;
                }
                if(value.equals("0") || value.equalsIgnoreCase("f") || value.equalsIgnoreCase("n")){
                    return false;
                }
                break;
            case 2:
                if(value.equalsIgnoreCase("no")){
                    return false;
                }
                break;
            case 3:
                if(value.equalsIgnoreCase("yes")){
                    return true;
                }
                break;
            case 4:
                if(value.equalsIgnoreCase("true")){
                    return true;
                }
                break;
            case 5:
                if(value.equalsIgnoreCase("false")){
                    return false;
                }
                break;
        }
        if(throwException){
            throw new IllegalArgumentException(value);
        }
        return defaultValue;
    }

    public static byte[] copyBytes(byte[] bytes, int len) {
        if(len == 0){
            return EMPTY_BYTES;
        }
        try{
            return Arrays.copyOf(bytes, len);
        }catch (OutOfMemoryError e){
            Error e2 = new OutOfMemoryError("Request memory : " + len);
            e2.initCause(e);
            throw e2;
        }
    }
}
