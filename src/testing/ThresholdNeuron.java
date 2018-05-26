package testing;

import neurality.AbstractNeuron;

public class ThresholdNeuron extends AbstractNeuron {

	public ThresholdNeuron(AbstractNeuron[] inputs, int maxInputs, double[] mods) {
		super(inputs, maxInputs, mods);
	}
	
	double threshold;
	double output;
	
	@Override
	protected void start(double[] mods) {
		threshold = mods[0];
		output = mods[1];
	}

	@Override
	protected double update(double[] data) {
		double sum = 0;
		for(double d : data) sum += d;
		if(sum >= threshold) return output;
		return 0;
	}

}
