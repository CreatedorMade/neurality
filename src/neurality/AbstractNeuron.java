package neurality;

/**
 * Base class for all neurons.
 * @author James
 * 
 */

public abstract class AbstractNeuron {
	
	AbstractNeuron[] inputs = new AbstractNeuron[0];
	int maxInputs;
	double output = 0;
	double[] values;
	
	int x = 0;
	int y = 0;
	
	/**
	 * The constructor for any subclass of AbstractNeuron should just call super(...). Upon attempting to register a neuron class without an exact match for this constructor, the registry will throw a NoSuchMethodException. 
	 */
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
	
	/**
	 * Called on neuron initialization. Treat this as a constructor.
	 * @param mods Modifiers provided by the NeuralMap configuration.
	 */
	protected abstract void start(double[] mods);
	/**
	 * Called every time the neural network is updated.
	 * @param data An array containing the output values of all backwards-linked neurons. This array's length is variable on construction, but will not change during runtime.
	 * @return Return the value that the neuron will output.
	 */
	protected abstract double update(double[] data);
	
	protected double[] saveState(){
	/**
	 * Called to save data during NeuralNetwork.saveState() for consistency across saving and loading.
	 * @return The data to be saved
	 */
		return new double[0];
	}
	
	protected void loadState(double[] state){
	/**
	 * Called to restore the neuron's state after saving and loading.
	 * @param state The state to load. This data comes from the return value of saveState().
	 */
	}
	
}
