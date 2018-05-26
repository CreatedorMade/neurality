package neurality;

import java.lang.reflect.InvocationTargetException;

public class Testing {

	public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		NeuralRegistry registry = new NeuralRegistry(0.5f, 2f);
		registry.register(TestNeuron.class, 0, 0, 0, 0);
		NeuralMap map = new NeuralMap(registry, 10, 5, 1, 1);
		System.out.println(map);
	}

}
