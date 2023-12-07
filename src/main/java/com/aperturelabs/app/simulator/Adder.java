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
	public static final Set<String> FUNCS = new HashSet<>(Arrays.asList(OP_VALUES));

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
	public Set<String> operations() {
		return OPS;
	}

	@Override
	public Set<String> functions() {
		return FUNCS;
	}
}
