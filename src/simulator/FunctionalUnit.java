package simulator;

public abstract class FuncionalUnit {
	private ArrayList<Station> reservationStations;	
	private int exCyclesNeeded;
	private int currentInstruction;
	private boolean busy;
	private int cyclesLeft;

	// TODO: Write a clockCycle method to capture everything that should on a clock tick
    //
	// no cycle high/cycle low, that's for nerds
	
	public boolean tick(CDB cdb) {
        // Also need to pull arguments off the CDB for reservation stations
        this.updateReservationStations(cdb);

		if (!cdb.busy) {
		}
	}

	public int findInstructionToExecute() {
		for (int i = 0; i < this.reservationStations.size(); ++i) {
            // TODO: Need to check if the instruction has all it's parameters before executing
			if (!reservationStations.get(i).busy) {
				return i;	
			}
		}
		return -1;
	}
	
	public void execute() {
		Station station;
		if (!this.busy) {
			int i;
			if ((i = this.findInstructionToExecute()) != -1) {
				this.currentInstruction = i;
				this.busy = true;
				this.cyclesLeft = this.exCyclesNeeded;
				station = this.reservationStations.get(i);
				// TODO: Is station.operation the correct thing to pass here?
				StatusTable.getInstance().addInstruction(station.operation, station.name);
			} 
		} else if (--this.cyclesLeft == 0){
				station = this.reservationStations.get(this.currentInstruction);
				station.resultReady = true;
				int result = this.computeResult(station);
				StatusTable.getInstance().updateEndEX(station.name);
		}
	}

	public int findInstructionToWrite() {
		for (int i = 0; i < this.reservationStations.size(); ++i)
			if (this.reservationStations.get(i).resultReady) 
				return i;
		return -1;
	}

	public void updateReservationStations(CDB cdb) {
		for (Station station : this.reservationStations) {
			if (station.Qj == cdb.source) {
				station.Vj = cdb.data;
			} 
			if (station.Qk == cdb.source) {
				station.Vk = cdb.data;
			}
		}
	}
	public void dump() {

		for (Station station : this.reservationStations) 
			station.dump();
	}

	public void cdbWrite() {
		if (cdb.busy)
			return;
		int i;
		if ((i = this.findInstructionToWrite()) == -1) 
			return;
		Station station = this.reservationStations.get(i);

		CDB cdb = CDB.getInstance();
		if (cdb.busy)
			return;

		cdb.busy = true;	
		cdb.source = station.name;
		cdb.data = station.result;
	}

	public abstract long computeResult(Station station);
	public abstract boolean tryIssueInstruction();
}
