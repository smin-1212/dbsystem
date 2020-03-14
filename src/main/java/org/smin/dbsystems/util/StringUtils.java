package main.java.org.smin.dbsystems.util;

import java.lang.ref.SoftReference;

public class StringUtils {

    private static SoftReference<String[]> softCache;
    private static long softCacheCreatedNs;

    private static final char[] HEX = "0123456789abcdef".toCharArray();
    private static final int[] HEX_DECODE = new int['f' + 1];

    private static final int TO_UPPER_CACHE_LENGTH = 2*1024;
    private static final int TO_UPPER_CACHE_MAX_ENTRY_LENGTH = 64;
    private static final String[][] TO_UPPER_CACHE = new String[TO_UPPER_CACHE_LENGTH][];

    static {
        for(int i = 0 ; i< HEX_DECODE.length; i++){
            HEX_DECODE[i] = -1;
        }
        for(int i = 0 ; i <=9; i++){
            HEX_DECODE[i + '0'] = i;
        }
        for(int i = 0 ; i<=5; i++){
            HEX_DECODE[i +'a'] = HEX_DECODE[i + 'A'] = i + 10;
        }
    }

    private StringUtils(){

    }

    public static String quoteIdentifier(String s){
        return quoteIdentifier(new StringBuilder(s.length() + 2), s).toString();
    }

    public static StringBuilder quoteIdentifier(StringBuilder builder, String s){
        builder.append('"');
        for(int i = 0, length = s.length(); i <length ; i ++){
            char c = s.charAt(i);
            if(c == '"'){
                builder.append(c);
            }
            builder.append(c);
        }
        return builder.append('"');
    }

}
