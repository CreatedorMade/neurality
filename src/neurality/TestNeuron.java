package neurality;

public class TestNeuron extends AbstractNeuron {

	public TestNeuron(AbstractNeuron[] inputs, int maxInputs, double[] mods) {
		super(inputs, maxInputs, mods);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void start(double[] mods) {
		
	}

	@Override
	protected double update(double[] data) {
		return Math.random();
	}

}
