package main.java.org.smin.dbsystems.message;


import main.java.org.smin.dbsystems.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import static main.java.org.smin.dbsystems.api.ErrorCode.*;


public class DbException extends RuntimeException{

    private static final long serialVersionUID = 1L;


    public static final String HIDE_SQL = "--hide--";

    private static final Properties MESSAGES = new Properties();


    public static DbException convertIOException(IOException e, String message){
        if(message == null){
            Throwable t = e.getCause();
            if(t instanceof DbException){
                return (DbException) t;
            }
            return get(IO_EXCEPTION_1, e, e.toString());
        }
        return get(IO_EXCEPTION_2, e, e.toString());
    }


    public static SQLException getJdbcSQLException(int errorCode, Throwable cause, String... params){
        String sqlstate = getState(errorCode);
        String message = translate(sqlstate, params);
        return getJdbcSQLException(message, null, sqlstate, errorCode, cause, null);
    }

    public static SQLException getJdbcSQLException(String message, String sql, String state, int errorCode,
                                                   Throwable cause, String stackTrace){
        sql = filterSQL(sql);

//        switch (errorCode / 1_000){
//            case 1:
//
//        }
        return null;
        //return new JdbcSQLException(message, sql, state, errorCode, cause, stackTrace);
    }

    private static String translate(String key, String... params){
        String message = MESSAGES.getProperty(key);
        if(message == null){
            message = "(message " + key + " not found)";
        }
        if(params != null){
            for(int i = 0 ; i< params.length; i++){
                String s = params[i];
                if(s != null && s.length() > 0){
                    params[i] = StringUtils.quoteIdentifier(s);
                }
            }
            message = MessageFormat.format(message, (Object[]) params);
        }
        return message;
    }


    public static DbException get(int errorCode, Throwable cause,
                                  String... params){
        return new DbException();
    }


    private static String filterSQL(String sql){
        return sql==null || !sql.contains(HIDE_SQL) ? sql : "-";
    }




}
