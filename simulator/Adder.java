package simulator; 

public class Adder extends FunctionalUnit {
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
}
