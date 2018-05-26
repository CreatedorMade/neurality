package neurality;

public class ExitNeuron extends AbstractNeuron {

	public ExitNeuron(AbstractNeuron[] inputs, int maxInputs, double[] mods) {
		super(inputs, maxInputs, mods);
	}

	@Override
	protected void start(double[] mods) {}
	
	double value = 0;
	
	@Override
	protected double update(double[] data) {
		value = 0;
		for (double val : data) value += val;
		return 0;
	}
	
	double getValue() { return value; }
	
}
