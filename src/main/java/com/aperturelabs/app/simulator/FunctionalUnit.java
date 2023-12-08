package com.aperturelabs.app.simulator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class FunctionalUnit {
	protected ArrayList<Station> reservationStations;
	private int exCyclesNeeded;
	private int currentInstruction;
	private boolean busy;
	private int cyclesLeft;

	private static final Set<String> nullFunctionCodes = new HashSet<>();

	/**
	 * Find the next reservation station to execute an instruction from.
	 */
	public int findInstructionToExecute() {
		for (int i = 0; i < this.reservationStations.size(); ++i) {
			if (reservationStations.get(i).ready()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Perform a signle clock cycle of execution on the current insturction
	 * TODO: Save currently executing instruction, if there be one.
	 */
	public void execute() {
		Station station;
		if (!this.busy) {
			int i;
			if ((i = this.findInstructionToExecute()) != -1) {
				this.currentInstruction = i;
				this.busy = true;
				this.cyclesLeft = this.exCyclesNeeded;
				station = this.reservationStations.get(i);
				StatusTable.getInstance().addInstruction(station.operation, station.name);
			}
		} else if (--this.cyclesLeft == 0) {
			station = this.reservationStations.get(this.currentInstruction);
			station.resultReady = true;
			long result = this.computeResult(station);
			station.result = result;
			StatusTable.getInstance().updateEndEX(station.name);
		}
	}

	/**
	 * Dump the contents of the reservation stations
	 */
	public void dump() {

		for (Station station : this.reservationStations)
			station.dump();
	}

	/**
	 * Find the next instruction to write to the CDB
	 */
	public int findInstructionToWrite() {
		for (int i = 0; i < this.reservationStations.size(); ++i) {
			Station s = this.reservationStations.get(i);
			if (s.resultReady && !s.resultWritten) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Update the reservation stations with the result from the CDB
	 */
	public void updateReservationStations() {
		CDB cdb = CDB.getInstance();

		// TODO: Get put
		// Check CDB. Also, if this makes the instruction ready to execute,
		// update state to reflect that.
		for (Station station : this.reservationStations) {
			if (station.Qj == cdb.source) {
				station.Vj = cdb.data;
			}
			if (station.Qk == cdb.source) {
				station.Vk = cdb.data;
			}
		}
	}

	/**
	 * Write the result of the current instruction to the CDB
	 */
	public void cdbWrite() {
		CDB cdb = CDB.getInstance();
		if (cdb.busy)
			return;
		int i;
		if ((i = this.findInstructionToWrite()) == -1)
			return;
		Station station = this.reservationStations.get(i);
		StatusTable.getInstance().updateWrite(station.name);

		cdb.busy = true;
		cdb.source = station.name;
		cdb.data = station.result;
		// Reset the station for the next instruction
		station.clear();
	}

	/**
	 * Compute the result of the current instruction
	 *
	 * This should be reimplemented in the concrete classes
	 */
	public abstract long computeResult(Station station);

	/*
	 * Returns true if the instruction is successfully issued
	 */

	public abstract Set<String> operations();

	/**
	 * Needs FU-specific logic to get arguments out of the instruction
	 */
	public abstract void issue(Station station, int instruction, int pc);

	/*
	 * 
	 * Default implementation returns nothing.
	 * Only functional units for opcode == 0 will have this.
	 */
	public Set<String> functions() {
		return nullFunctionCodes;
	}

	/**
	 * Try to issue an instruction to the reservation stations
	 */
	public boolean tryIssueInstruction(int instruction, int pc) {
		String opcode = Tools.opcode(instruction);
		if (!this.operations().contains(opcode))
			return false;
		if (opcode == "ARITHMETIC_TYPE" && !this.functions().contains(Tools.functionCode(instruction))) {
			return false;
		}

		// TODO: Actually issue the instruction to a station
		int availableStationIdx = this.findInstructionToExecute();
		Station availableStation = this.reservationStations.get(availableStationIdx);
		this.issue(availableStation, instruction, pc);

		return true;
	}
}
