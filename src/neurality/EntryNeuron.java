package neurality;

class EntryNeuron extends AbstractNeuron {

	public EntryNeuron() {
		super(new AbstractNeuron[0], 0, new double[0]);
	}

	@Override
	protected void start(double[] mods) {}
	
	double value = 0;
	
	@Override
	protected double update(double[] data) {
		return value;
	}
	
	void setValue(double value){
		this.value = value;
	}
	
}
