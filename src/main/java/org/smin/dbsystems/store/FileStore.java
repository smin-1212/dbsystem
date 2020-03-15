package main.java.org.smin.dbsystems.store;

import main.java.org.smin.dbsystems.engine.Constants;
import main.java.org.smin.dbsystems.message.DbException;
import main.java.org.smin.dbsystems.store.fs.FileUtils;

import java.io.IOException;
import java.lang.ref.Reference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileStore {
    /**
     * 파일 헤더 블록사이즈
     */
    public static final int HEADER_LENGTH = 3* Constants.FILE_BLOCK_SIZE;

    /**
     * 매직 해더
     */
    private static final String HEADER = "SMIN_DB_SYSTEMS".substring(0, Constants.FILE_BLOCK_SIZE -1) +"\n";

    private static final boolean ASSERT ;

    static {
        boolean a = false;
        // 의도적 사이드이펙트
        assert a = true;
        ASSERT=a;
    }

    /**
     * 파일명
     */
    protected String name;

    /**
     *
     */
    private final DataHandler handler;

    private FileChannel file;
    private long filePos;
    private long fileLength;
    private Reference<?> autoDeleteReference;
    private boolean checkedWriting = true;
    private final String mode;
    private java.nio.channels.FileLock lock;

    protected FileStore(DataHandler handler, String name, String mode){
        this.handler = handler;
        this.name = name;
        try{
            boolean exists = FileUtils.exists(name);

            if(exists && !FileUtils.canWrite(name)){
                mode="r";
            }else{
                FileUtils.createDirectories(FileUtils.getParent(name));
            }
            file = FileUtils.open(name, mode);
            if(exists){
                fileLength = file.size();
            }

        }catch (IOException e){
            throw DbException.convertIOException(
                    e, "name: " +name+" mode:" +mode
            );
        }
        this.mode = mode;
    }

    public static FileStore open(DataHandler handler, String name, String mode, String cipher, byte[] key){
        return open(handler,name, mode, cipher, key, Constants.ENCRYPTION_KEY_HASH_ITERATIONS);
    }

    public static  FileStore open(DataHandler handler, String name, String mode,
                                  String cipher, byte[] key, int keyIterations){
        FileStore store;
        if(cipher == null){
            store = new FileStore(handler, name, mode);
        }else{
            // store = new SecureFileStore(handler, name, )
            // TODO 1. secureFileStore ~
            store = new FileStore(handler, name, mode);
        }
        return store;
    }

    protected byte[] generateSalt(){
        return HEADER.getBytes(StandardCharsets.UTF_8);
    }

    public void setCheckedWriting(boolean value){
        this.checkedWriting = value;
    }

    private void checkWritingAllowed(){
        if(handler != null && checkedWriting){
            handler.checkWritingAllowed();;
        }
    }

    private void checkPowerOff(){
        if(handler != null){
            handler.checkPowerOff();
        }
    }

    public void init(){
        int len = Constants.FILE_BLOCK_SIZE;
        byte[] salt;
        byte[] magic = HEADER.getBytes(StandardCharsets.UTF_8);
        if(length() <HEADER_LENGTH){
            checkedWriting = false;
            writeDirect(magic, 0, len);
            salt = generateSalt();
            writeDirect(salt, 0, len);

            write(magic, 0, len);
            checkedWriting = true;
        }else{
            seek(0);
            byte[] buff = new byte[len];
            readFullyDirect(buff,0, len);
            if(!Arrays.equals(buff, magic)){
                // TODO DBEXCeption
            }
            salt = new byte[len];
            readFullyDirect(salt, 0, len);

            readFully(buff, 0, Constants.FILE_BLOCK_SIZE);
            if(!Arrays.equals(buff, magic)){
                // TODO DBException
            }
        }

    }

    private void readFullyDirect(byte[] buff, int off ,int length) {
        readFully(buff, off, length);
    }

    private void readFully(byte[] buff, int off, int length) {
        if(length < 0 || length % Constants.FILE_BLOCK_SIZE != 0){
            // TODO dbexception
        }
        checkPowerOff();
        try{
            FileUtils.readFully(file, ByteBuffer.wrap(buff, off, length));
        }catch (IOException e){
            // TODO dbexception
        }
        filePos += length;
    }


    protected void writeDirect(byte[] b, int off, int len){
        write(b, off, len);
    }

    public void write(byte[] b, int off, int len){
        if(len<0 || len%Constants.FILE_BLOCK_SIZE != 0){

        }
        checkWritingAllowed();;
        checkPowerOff();
        try{
            FileUtils.writeFully(file, ByteBuffer.wrap(b, off, len));
        }catch (IOException e){
            closeFileSilently();

        }
        filePos += len;
        fileLength = Math.max(filePos, fileLength);
    }

    public void seek(long pos){
        if(pos % Constants.FILE_BLOCK_SIZE != 0 ){
            // TODO DBException
        }
        try{
            if(pos != filePos){
                file.position(pos);
                filePos = pos;
            }
        }catch (IOException e){
            // TODO throw DbException
        }
    }

    private void closeFileSilently(){
        try{
            file.close();
        }catch (IOException e){
            // ignore
        }
    }

    public long length(){
        long len = fileLength;
        if(ASSERT){
            try{
                len = file.size();
                if(len != fileLength){
                    // DbException
                }

            }catch (IOException e){

            }
        }

        return len;
    }

    public long getFilePointer(){
        if(ASSERT){
            try{
                if(file.position() != filePos){
                    // TODO dbException
                }
            }catch (IOException e){
                // TODO DbException
            }
        }
        return filePos;
    }

    public void sync(){
        try{
            file.force(true);
        }catch (IOException e){
            closeFileSilently();
            // TODO DBException
        }
    }

    public void autoDelete(){
        if(autoDeleteReference == null){
            // autoDeleteReference = handler.getTempFileDeleter();
        }
    }

}
