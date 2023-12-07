package com.aperturelabs.app.simulator;
import java.io.*;

//This class is incomplete.  You'll still need methods for
//loading memory and getting and setting 32 and 64 bit
//words.  Remember accesses must be aligned.

public class Memory
{
    private static Memory mem;
    private int [] memory;
    private int size;

    private Memory(int size)
    {
        this.size = size;
        memory = new int[size];
    }

    public static Memory getInstance()
    {
        if (mem == null) mem = new Memory(4000);
        return mem;
    }


    //helper function for dumping memory
    private String buildLine(int i)
    {
        String line;
        int j;
        line = new String();

        for (j = i; j < i + 8; j++)
        {
            line = line + Tools.pad(Integer.toHexString(memory[j]), 8, 
                                    "0", Direction.RIGHT) + " ";
        }
        return line;
    }

    //output contents of memory
    public void dump()
    {
        int address = 0;
        String lastline = new String("junk");
        String nextline;
        boolean star = false, needNewline = false;
        for (int i = 0; i < memory.length; i+=8)
        {
            nextline = buildLine(i);
            if (! lastline.equals(nextline))
            {
                star = false;
                if (needNewline) System.out.println(); 
                System.out.print(Tools.pad(Integer.toHexString(address), 4, "0",
                                           Direction.RIGHT) + ":\t");
                System.out.print(nextline);  
                needNewline = true;
            } else if (lastline.equals(nextline) && (star == false))
            {
               System.out.println(" *");
               needNewline = false;
               star = true;
            } 
            address = address + 32;
            lastline = nextline;
        }
        System.out.println();
    }

    //needed by the GUI
    public int[] cloneMemory()
    {
        return memory.clone();
    }

}


