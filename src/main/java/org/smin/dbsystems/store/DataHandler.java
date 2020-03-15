package main.java.org.smin.dbsystems.store;

import main.java.org.smin.dbsystems.message.DbException;
import main.java.org.smin.dbsystems.util.TempFileDeleter;

import java.lang.ref.Reference;

public interface DataHandler {

    String getDatabasePath();

    FileStore openFile(String name, String mode, boolean mustExist);

    void checkPowerOff() throws DbException;

    void checkWritingAllowed() throws DbException;


    TempFileDeleter getTempFileDeleter();
}
