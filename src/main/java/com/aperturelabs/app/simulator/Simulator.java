package com.aperturelabs.app.simulator;

import java.io.*;
import java.util.*;
import com.aperturelabs.app.tsgui.*;
import com.aperturelabs.app.simulator.*;

public class Simulator {
    // both of these are for the GUI
    boolean gui;
    private ArrayList<CycleSnapShot> snapshots = null;
    private Scanner input;

    public Simulator(String file, boolean flag) throws IOException {
        // create your functional units in here
        gui = flag;
        input = new Scanner(new File(file));
    }

    public void simulate() {
        // initialize variables passed to gui
        long cdbResult = 0;
        String cdbSrc = "none";
        int PC = 0;
        int instruction = 0;

        boolean halt = false;
        // Instiate FUs
        FunctionalUnit[] functionalUnits = {
                //new IntegerUnit(),
                //new BranchUnit(),
                //new MemoryUnit(),
                //new FPAddUnit(),
                //new FPMultUnit(),
                //new FPDivUnit(),
        };

        // TODO: Detect when stall is called for,
        // that is, when no functional unit was able to accept the instruction
        CDB cdb = CDB.getInstance();
	Registers registers = Registers.getInstance();
	Memory memory = Memory.getInstance();
	Simulator.loadInstructionsIntoMemory(input);

        while (halt == false) {
            boolean stall = true;
	    instruction = Memory.getInstance().load32BitWord(PC);
            // ISSUE //
            for (FunctionalUnit fu : functionalUnits) {
                stall = stall && fu.tryIssueInstruction(instruction, PC);
                // TODO: At this point, we need to check if the instruction issues was a halt.
                // When we issue a halt, we need to stop issueing instrucdtions and wait for the
                // reservation stations to empty out.
            }

            // EXECUTE //
            // Reserveration stations need to pull in new data before we execute
            for (FunctionalUnit fu : functionalUnits) {
                fu.updateReservationStations();
            }
            registers.readCDB();

            // Actually execute a single cycle
            for (FunctionalUnit fu : functionalUnits) {
                fu.execute();
            }

            // WRITEBACK //
            for (FunctionalUnit fu : functionalUnits) {
                fu.cdbWrite();
            }

            if (!stall) {
		
                PC += 32;
            }

            cdb.busy = false;
            if (gui)
                addSnapShot(instruction, PC, cdbResult, cdbSrc);
        }
        if (gui == true)
            new TSGui(snapshots);
    }

    // This method is for the GUI, do not modify this
    public void addSnapShot(int instr, int PCValue,
            long cdbValue, String cdbSrc) {
        if (snapshots == null)
            snapshots = new ArrayList<CycleSnapShot>();

        snapshots.add(new CycleSnapShot(Clock.getInstance().get(), instr,
                PCValue, buildFunctionalUnitImageList(), cdbValue,
                cdbSrc));
    }

    static int parseInstruction(String line) {
        return Integer.parseInt(line.substring(0, line.indexOf('#')));
    }

    static void loadInstructionsIntoMemory(Scanner instructions) {
	    Memory mem = Memory.getInstance();
	    int addr = 0;
	    while(instructions.hasNextLine()) {
		mem.store32BitWord(addr, Simulator.parseInstruction(instructions.nextLine()));
		addr += 4;
	    }
    }

    // You'll need to modify this method to use the GUI
    private ArrayList<FUnitImage> buildFunctionalUnitImageList() {
        ArrayList<FUnitImage> list = new ArrayList<FUnitImage>();

        // you'll need to create a new FUnitImage object for each
        // of your Functional Units and pass that object to list.add
        // Each Functional Unit will have an array of Stations,
        // a count of the Stations, an executionCount, the currentInstruction,
        // a flag indicating whether it is busy or not, and the
        // number of remaining execution Cycles
        //
        // Here is a sample call, although it won't work until there
        // is an intUnit object.
        /*
         * list.add(new FUnitImage("integer", intUnit.RS, intUnit.RScount,
         * intUnit.executionCount, intUnit.currentInstruction,
         * intUnit.FUbusy, intUnit.executionCycles));
         * 
         */
        return list;
    }
}
