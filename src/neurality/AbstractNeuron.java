package neurality;

public abstract class AbstractNeuron {
	
	AbstractNeuron[] inputs;
	int maxInputs;
	double output = 0;
	double[] values;
	
	public AbstractNeuron(AbstractNeuron[] inputs, int maxInputs, double[] mods){
		this.maxInputs = maxInputs;
		for(AbstractNeuron n : inputs){
			if(n != null && (maxInputs == -1 || this.inputs.length < maxInputs)){
				AbstractNeuron[] temp = this.inputs;
				this.inputs = new AbstractNeuron[this.inputs.length+1];
				for(int i = 0; i < temp.length; i++) this.inputs[i] = temp[i];
				this.inputs[this.inputs.length-1] = n;
			}
		}
		values = new double[inputs.length];
		start(mods);
	}
	
	protected abstract void start(double[] mods);
	protected abstract double update(double[] data);
	
}
