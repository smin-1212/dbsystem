package main.java.org.smin.dbsystems.store.fs;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static boolean exists(String fileName) {
        return FilePath.get(fileName).exists();
    }


    public static FileChannel open(String fileName, String mode) throws IOException {
        return FilePath.get(fileName).open(mode);
    }
}
