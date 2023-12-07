package com.aperturelabs.app.simulator;

// TODO: This class is incomplete.  You'll need functions
// for getting and setting registers and updating the
// register file using the value on the CDB

public class Registers {
    protected long[] Regs;
    protected String[] Qi;
    static final int REGS = 32;

    public Registers() {
        int i;
        Regs = new long[REGS];
        Qi = new String[REGS];
        for (i = 0; i < REGS; i++) {
            Regs[i] = 0;
            Qi[i] = null;
        }
    }

    /**
     * Tell the register file what it should read data from on the CDB.
     */
    public void setRegQi(int reg, String qi) {
        Qi[reg] = qi;
    }

    /**
     * Get the value from the register file.
     *
     * @return The register to get the value from. If -1 is returned,
     *         getRegQi(ref) should be called to get the reservation station that
     *         will be producing the expected value.
     */
    public long getReg(int reg) {
        if (Qi[reg] != null) {
            return -1;
        } else {
            return Regs[reg];
        }
    }

    /**
     * Read the CDB and update the register file with value on the bus.
     */
    public void readCDB() {
        CDB cdb = CDB.getInstance();
        if (!cdb.busy)
            return;
        for (int i = 0; i < REGS; i++) {
            if (cdb.source == Qi[i] && Qi[i] != null) {
                Regs[i] = cdb.data;
                Qi[i] = null;
            }
        }
    }

    /**
     * Get the functional unit that will be producing the expected value.
     */
    public String getRegQi(int reg) {
        return Qi[reg];
    }

    // output contents of Register File
    public void dumpRow(int start, int count) {
        int i, j, k;
        i = start;
        for (k = 0; k < count; k++, i++) {
            if (Qi[i] != null)
                System.out.print(Tools.pad(Qi[i], 16, " ", Direction.RIGHT) + " ");
            else
                System.out.print(Tools.pad(Long.toHexString(Regs[i]), 16, "0",
                        Direction.RIGHT) + " ");
        }
        System.out.println();
    }

    // These two functions are used by the GUI
    public long[] cloneRegs() {
        return Regs.clone();
    }

    public String[] cloneQi() {
        return Qi.clone();
    }
}
