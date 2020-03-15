package main.java.org.smin.dbsystems.engine;

import main.java.org.smin.dbsystems.util.Utils;

public class SysProperties {


    public static final boolean TRACE_IO =
            Utils.getProperty(
                    "dbsystem.traceIO",
                    false);

    public static final boolean CHECK =
            Utils.getProperty(
                    "dbsystem.check",
                    !"0.9".equals(
                            Utils.getProperty("java.specification.version",
                                            null)
                    )
            );
}
