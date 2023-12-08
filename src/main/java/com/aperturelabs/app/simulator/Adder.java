package com.aperturelabs.app.simulator;

import java.util.ArrayList;
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

	public Adder() {
		this.reservationStations = new ArrayList<Station>(Arrays.asList(
				new Station[] {
						new Station("Integer0"),
						new Station("Integer1"),
						new Station("Integer2")
				}));
	}

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
		Registers registers = Registers.getInstance();

		String operation;
		long Vj, Vk;
		int srcReg, srcRegOne, srcRegTwo;
		String Qj, Qk;

		switch (Tools.opcode(instruction)) {
			case Tools.ADD_IMMEDIATE:
			case Tools.ADD_IMMEDIATE_UNSIGNED: {
				operation = "ADD";
				Vk = instruction & 0x0000FFFF;
				Qk = null;
				srcReg = (instruction & 0x03E00000) >> 21;
				if (registers.getRegQi(srcReg) != null) {
					Vj = registers.getReg(srcReg);
					Qj = null;
				} else {
					Vj = 0;
					Qj = registers.getRegQi(srcReg);
				}
				break;
			}
			case Tools.ARITHMETIC:
				// Determine function code
				String function = Tools.functionCode(instruction);
				if (function == Tools.DADD)
					operation = "ADD";
				else if (function == Tools.DSUB)
					operation = "SUB";
				else
					throw new RuntimeException("Invalid function code in Adder.");
				// Determine srcReg1
				srcRegOne = (instruction & 0x03E00000) >> 21;
				if (registers.getRegQi(srcRegOne) != null) {
					Vj = registers.getReg(srcRegOne);
					Qj = null;
				} else {
					Vj = 0;
					Qj = registers.getRegQi(srcRegOne);
				}
				// Determine srcReg2
				srcRegTwo = (instruction & 0x001F0000) >> 16;
				if (registers.getRegQi(srcRegTwo) != null) {
					Vk = registers.getReg(srcRegTwo);
					Qk = null;
				} else {
					Vk = 0;
					Qk = registers.getRegQi(srcRegTwo);
				}
				break;
			default:
				throw new RuntimeException("This instruction should never have made it to the adder.");
		}

		station.issue(operation, Vj, Vk, Qj, Qk);
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
