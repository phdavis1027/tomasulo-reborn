package com.aperturelabs.app.simulator;

public class CDB {
    long data;
    String source;
    boolean busy;

    static CDB instance = null;

    private CDB() {
        this.data = -1;
        this.source = "";
        this.busy = false;
    }

    public static CDB getInstance() {
        if (instance == null)
            instance = new CDB();
        return instance;
    }
}
