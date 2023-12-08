package com.aperturelabs.app.simulator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Adder extends FunctionalUnit {
	public static final String[] OP_VALUES = new String[] {
			"DADDI",
			"DADDIU",
			"DADD",
			"DSUB"
	};
	public static final Set<String> OPS = new HashSet<>(Arrays.asList(OP_VALUES));

	public static final String[] FUNC_VALUES = new String[] {
			"DADD",
			"DSUB"
	};
	public static final Set<String> FUNCS = new HashSet<>(Arrays.asList(FUNC_VALUES));

	@Override
	public long computeResult(Station station) {
		switch (station.operation) {
			case "ADD":
				return station.Vj + station.Vk;
			case "SUB":
				return station.Vj - station.Vk;
			default:
				return 0;
		}
	}

	@Override
	public void issue(Station station, int instruction, int pc) {
		// Get arguments from instruction		
		String operation = Tools.opcode(instruction);

		long Vj, Vk;
		int srcReg;
		String Qj, Qk;

		switch(operation) {
			case Tools.ADD_IMMEDIATE:
			case Tools.ADD_IMMEDIATE_UNSIGNED:
				Vk = instruction & 0x0000FFFF;
				srcReg = (instruction & 0x03E00000) >> 21;
				break;
		}


		station.issue();
	}

	@Override
	public Set<String> operations() {
		return OPS;
	}

	@Override
	public Set<String> functions() {
		return FUNCS;
	}
}
