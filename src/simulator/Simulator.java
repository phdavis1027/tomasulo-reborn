package simulator;
import java.io.*;
import java.util.*;
import tsgui.*;

public class Simulator
{
    //both of these are for the GUI 
    boolean gui;
    private ArrayList<CycleSnapShot> snapshots = null;


    public Simulator(String file, boolean flag) throws IOException
    {
        //create your functional units in here
        System.out.println("In constructor!");
        gui = flag;
    }

    public void simulate()
    {
        //initialize variables passed to gui
        long cdbResult = 0;
        String cdbSrc = "none";
        int PC = 0;
        int instruction = 0;

        boolean halt = false;
        //this will be your driver loop which will execute until
        //the halt is executed
	// TODO: Instantiate CDB
	// TODO: Instiate FUs 
	// TODO: Detect when stall is called for, 
	// that is, when no functional unit was able to accept the instruction
	CDB cdb = CDB.getInstance();
        while (halt == false)
        {
	    boolean stall = true;
	    // ISSUE
	    // for (FU fu : FUS) {
	    // 	stall =  stall && fu.tryIssueInstruction(instruction)
	    // }
	    // EXECUTE
	    // TODO: Check FUs for values to write on the CDB, but only one
	    // for (FU fu : FUS) {
	    // 	fu.execute(instruction, cdb, ...)
	    // }
	    //
	    // 
	    //
	    // WRITEBACK
	    // for (FU fu : FUS) {
	    // 	fu.tryWrite(instruction, cdb, ...)
	    // }

	    if (!stall) {
		// TODO: Get next instruction 
	    }

	    cdb.busy = false;
            if (gui) addSnapShot(instruction, PC, cdbResult, cdbSrc);
        }
        if (gui == true) new TSGui(snapshots);
    }
	
    //This method is for the GUI, do not modify this
    public void addSnapShot(int instr, int PCValue, 
                            long cdbValue, String cdbSrc)
    {
         if (snapshots == null) snapshots = new ArrayList<CycleSnapShot>();
          
          snapshots.add(new CycleSnapShot(Clock.getInstance().get(), instr,
                        PCValue, buildFunctionalUnitImageList(), cdbValue,
                        cdbSrc));
     }

     //You'll need to modify this method to use the GUI
     private ArrayList<FUnitImage> buildFunctionalUnitImageList()
     {
          ArrayList<FUnitImage> list = new ArrayList<FUnitImage>();

          //you'll need to create a new FUnitImage object for each
          //of your Functional Units and pass that object to list.add
          //Each Functional Unit will have an array of Stations,
          //a count of the Stations, an executionCount, the currentInstruction,
          //a flag indicating whether it is busy or not, and the
          //number of remaining execution Cycles
          //
          //Here is a sample call, although it won't work until there
          //is an intUnit object.
/*
          list.add(new FUnitImage("integer", intUnit.RS, intUnit.RScount,
                   intUnit.executionCount, intUnit.currentInstruction,
                   intUnit.FUbusy, intUnit.executionCycles));

*/
          return list;
     }
}

