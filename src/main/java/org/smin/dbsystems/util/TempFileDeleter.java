package main.java.org.smin.dbsystems.util;

import main.java.org.smin.dbsystems.engine.SysProperties;
import main.java.org.smin.dbsystems.store.fs.FileUtils;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;

public class TempFileDeleter {
    private final ReferenceQueue<Object> queue = new ReferenceQueue();
    private final HashMap<PhantomReference<?>, Object> refMap = new HashMap();

    private TempFileDeleter(){
        // utility class
    }

    public static TempFileDeleter getInstance() {
        return new TempFileDeleter();
    }

    public synchronized Reference<?> addFile(Object resource, Object monitor){
        if(!(resource instanceof String) && !(resource instanceof AutoCloseable)){
            // TODO DBexception
        }
        IOUtils.trace("TempfileDeleter.addFile",
                resource instanceof String ? (String)resource : "-",
                monitor);
        PhantomReference<?> ref = new PhantomReference(monitor, queue);
        refMap.put(ref, resource);
        deleteUnused();
        return ref;
    }

    private void deleteUnused() {
        while(queue != null){
            Reference<?> ref = queue.poll();
            if(ref == null){
                break;
            }
            deleteFile(ref, null);
        }
    }

    public synchronized void deleteFile(Reference<?> ref, Object resource) {
        if(ref != null){
            Object f2 = refMap.remove(ref);
            if(f2 != null){
                if(SysProperties.CHECK){
                    if(resource != null && !f2.equals(resource)){
                        // TODO dbException
                    }
                }
                resource = f2;
            }
        }
        if(resource instanceof String){
            String fileName = (String) resource;
            if(FileUtils.exists(fileName)){
                try{
                    IOUtils.trace("TempFileDeleter.deleteFile", fileName, null);
                    FileUtils.tryDelete(fileName);
                }catch (Exception e){

                }
            }
        }else if(resource instanceof AutoCloseable){
            AutoCloseable closeable = (AutoCloseable) resource;
            try{
                IOUtils.trace("TempFileDeleter.deleteCloseable", "-", null);
                closeable.close();
            }catch (Exception e){
                //
            }
        }
    }
}
