package main.java.org.smin.dbsystems.store;

import main.java.org.smin.dbsystems.engine.Constants;
import main.java.org.smin.dbsystems.message.DbException;
import main.java.org.smin.dbsystems.store.fs.FileUtils;

import java.io.IOException;
import java.lang.ref.Reference;
import java.nio.channels.FileChannel;

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
            file = FileUtils.open(name, mode);
        }catch (IOException e){
            throw DbException.convertIOException(
                    e, "name: " +name+" mode:" +mode
            );
        }
        this.mode = mode;
    }


}
