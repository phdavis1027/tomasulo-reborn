package simulator;

//This class is incomplete.  You'll need functions
//for getting and setting registers and updating the
//register file using the value on the CDB

public class Registers
{
    protected long[] Regs; 
    protected String[] Qi;
    static final int REGS = 32;
    public Registers()
    {
        int i;
        Regs = new long[REGS];
        Qi = new String[REGS];
        for (i = 0; i < REGS; i++)
        {
            Regs[i] = 0;
            Qi[i] = null;
        }
    }

    //output contents of Register File
    public void dumpRow(int start, int count)
    {
        int i, j, k;
        i = start;
        for (k = 0; k < count; k++, i++)
        {
            if (Qi[i] != null)
                System.out.print(Tools.pad(Qi[i], 16, " ", Direction.RIGHT) + " ");
            else
                System.out.print(Tools.pad(Long.toHexString(Regs[i]), 16, "0",
                                 Direction.RIGHT) + " ");
        }
        System.out.println();
    }

    //These two functions are used by the GUI
    public long[] cloneRegs()
    {
        return Regs.clone();
    }

    public String[] cloneQi()
    {
        return Qi.clone();
    }
}
