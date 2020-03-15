package main.java.org.smin.dbsystems.store.fs;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static boolean exists(String fileName) {
        return FilePath.get(fileName).exists();
    }


    public static FileChannel open(String fileName, String mode) throws IOException {
        return FilePath.get(fileName).open(mode);
    }


    public static boolean canWrite(String fileName){
        return FilePath.get(fileName).canWrite();
    }

    public static boolean isDirectory(String fileName){
        return FilePath.get(fileName).isDirectory();
    }

    public static void createDirectory(String directoryName){
        FilePath.get(directoryName).createDircetory();
    }

    public static String getParent(String fileName){
        FilePath p = FilePath.get(fileName).getParent();
        return p == null ? null : p.toString();
    }

    public static void createDirectories(String dir){
        if(dir != null){
            if(exists(dir)){
                if(!isDirectory(dir)){
                    createDirectory(dir);
                }
            }else {
                String parent = getParent(dir);
                createDirectories(parent);
                createDirectory(dir);
            }
        }
    }

    public static void writeFully(FileChannel channel, ByteBuffer src) throws IOException{
        do{
            channel.write(src);
        }while(src.remaining() >0 );
    }

    public static void readFully(FileChannel file, ByteBuffer wrap) throws IOException{
        do{
            int r = file.read(wrap);
            if(r < 0){
                throw new EOFException();
            }
        }while(wrap.remaining() >0);
    }

    public static boolean tryDelete(String path) {
        try{
            FilePath.get(path).delete();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
