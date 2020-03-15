package main.java.org.smin.dbsystems.store.fs;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FilePath {

    private static final FilePath defaultProvider;

    private static final ConcurrentHashMap<String, FilePath> providers;

    static{
        FilePath def = null;
        ConcurrentHashMap<String, FilePath> map = new ConcurrentHashMap();

        defaultProvider = def;
        providers = map;
    }

    public static FilePath get(String path){
        path = path.replace('\\', '/');
        int index = path.indexOf(':');
        if(index < 2){
            return defaultProvider;
        }
        String scheme = path.substring(0, index);
        FilePath p = providers.get(scheme);
        if(p == null){
            p = defaultProvider;
        }
        return p.getPath(path);
    }

    public abstract FilePath getPath(String path);

    public abstract boolean exists();

    public abstract FileChannel open(String mode) throws IOException;

    public abstract boolean canWrite() ;

    public abstract boolean isDirectory();

    public abstract void createDircetory();

    public abstract FilePath getParent();

    public abstract void delete();

}
