package main.java.org.smin.dbsystems.store;


import main.java.org.smin.dbsystems.engine.Constants;
import main.java.org.smin.dbsystems.tools.CompressTool;

import java.io.IOException;
import java.io.InputStream;

public class FileStoreInputStream extends InputStream {

    private final boolean alwaysClose;
    private FileStore store;
    private boolean endOfFile;
    private final Data page;
    private final CompressTool compres;
    private int remainingInBuffer;

    public FileStoreInputStream(FileStore sotre, DataHandler handler,
                                boolean compression, boolean alwaysClose){
        this.store = store;
        this.alwaysClose = alwaysClose;
        if(compression){
            compres = CompressTool.getInstance();
        }else {
            compres = null;
        }
        page = Data.create(handler, Constants.FILE_BLOCK_SIZE, true);

        try{
            if(sotre.length() <= FileStore.HEADER_LENGTH){
                close();
            }else{
                fillBuffer();;
            }
        }catch (IOException e){
            // TODO dbException
        }
    }

    @Override
    public int read() throws IOException {
        fillBuffer();
        if(endOfFile){
            return -1;
        }
        int i = page.readByte() & 0xff;
        remainingInBuffer--;
        return i;
    }

    private void fillBuffer() throws IOException{
        if(remainingInBuffer> 0 || endOfFile){
            return;
        }
        page.reset();
        store.openFile();
        if(store.length() == store.getFilePointer()){
            close();
            return;
        }
        store.readFully(page.getBytes(), 0, Constants.FILE_BLOCK_SIZE);
        remainingInBuffer = page.readInt();
        if(remainingInBuffer <0){
            close();
            return;
        }
        page.checkCapacity(remainingInBuffer);

        if(compres != null){
            page.checkCapacity(Data.LENGTH_INT);
            page.readInt();
        }
        page.setPos(page.length() + remainingInBuffer);
        page.fillAligned();
        int len = page.length() - Constants.FILE_BLOCK_SIZE;
        page.reset();
        page.readInt();
        store.readFully(page.getBytes(), Constants.FILE_BLOCK_SIZE, len);
        page.reset();
        page.readInt();
        if(compres !=null){

        }
        if(alwaysClose){
            store.closeFile();
        }
    }

    @Override
    public void close(){
        if(store != null){
            try{
                store.close();
                endOfFile = true;
            }finally {
                store = null;
            }
        }
    }

    @Override
    protected void finalize(){
        close();
    }


}
