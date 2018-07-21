package neurality;
import java.lang.reflect.*;

/**
 * Class that is used to construct neural maps. Any neural maps within the same simulation or that must be save/load compatible should all be constructed with the same registry.
 * @author James
 *
 */
public class NeuralRegistry {
	
	private float bias = 0;
	private float complexity = 0;
	
	/**
	 * 
	 * @param valueBias The chance that any tile will contain a neuron. Values should be between 0 and 1. Higher values result in more neurons. A value of 0 will result in no neuron generation.
	 * @param connectionChance The value that controls the number of connections a neuron will have. Values should be above 1. Higher values result in more connections. A value of 1 will result in no neurons being connected to each other.
	 */
	public NeuralRegistry(float valueBias, float connectionChance) { bias = valueBias; complexity = connectionChance; }
	
	class NeuronWrapper {
		Constructor<? extends AbstractNeuron> constructor;
		int maxInputs = 0;
		int modCount = 0;
		double minModValue = 0;
		double maxModValue = 0;
	}
	
	private NeuronWrapper[] wrappers = new NeuronWrapper[0];
	
	/**
	 * Used to add a type of neuron to the registry, marking it for generation.
	 * @param type The class of the neuron. Use yourNeuron.class to obtain this value.
	 * @param maxInputs The maximum number of connections any neuron instantiated with this wrapper may have. Set as -1 to allow unlimited connections.
	 * @param modCount The number of modifiers any neuron instantiated with this wrapper may have.
	 * @param minModValue The minimum value of any generated modifiers. Should be less than the maximum value.
	 * @param maxModValue The maximum value of any generated modifiers. Should be more than the minimum value.
	 * @throws NoSuchMethodException If the neuron's constructor does not exactly match the AbstractNeuron constructor.
	 */
	public void register(Class<? extends AbstractNeuron> type, int maxInputs, int modCount, double minModValue, double maxModValue) throws NoSuchMethodException{
		NeuronWrapper wrapper = new NeuronWrapper();
		wrapper.constructor = type.getConstructor(AbstractNeuron[].class, int.class, double[].class);
		wrapper.maxInputs = maxInputs;
		wrapper.modCount = modCount;
		wrapper.minModValue = minModValue;
		wrapper.maxModValue = maxModValue;
		
		NeuronWrapper[] temp = wrappers;
		wrappers = new NeuronWrapper[wrappers.length+1];
		for(int i = 0; i < temp.length; i++) wrappers[i] = temp[i];
		wrappers[wrappers.length-1] = wrapper;
	}
	
	int getIndex(NeuronWrapper target){
		for(int i = 0; i < wrappers.length; i++){
			if(target == wrappers[i]) return i;
		}
		return -1;
	}
	
	NeuronWrapper[] getWrappers(){ return wrappers; }
	float getBias() { return bias; }
	float getComplexity() { return complexity; }
}
