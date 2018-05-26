package neurality;
import java.lang.reflect.*;

public class NeuralRegistry {
	
	private float bias = 0;
	private float complexity = 0;
	
	public NeuralRegistry(float valueBias, float connectionChance) { bias = valueBias; complexity = connectionChance; }
	
	public class NeuronWrapper {
		Constructor<? extends AbstractNeuron> constructor;
		int maxInputs = 0;
		int modCount = 0;
		double minModValue = 0;
		double maxModValue = 0;
	}
	
	private NeuronWrapper[] wrappers = new NeuronWrapper[0];
	
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
	
	public NeuronWrapper[] getWrappers(){ return wrappers; }
	public float getBias() { return bias; }
	public float getComplexity() { return complexity; }
}
