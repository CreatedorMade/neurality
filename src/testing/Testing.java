package testing;
import java.lang.reflect.InvocationTargetException;

import neurality.*;

public class Testing {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		
		NeuralRegistry registry = new NeuralRegistry(1, 5);
		registry.register(ThresholdNeuron.class, -1, 2, -1d, 1d);
		
		boolean running = true;
		int i = 0;
		while(running){
			NeuralMap map = new NeuralMap(registry, 5, 3, 1, 1);
			NeuralNetwork net = new NeuralNetwork(map);
			net.setInput(0, 1);
			net.tick();
			if(net.getOutput(0) > 0.5) running = false;
			System.out.println(net.getOutput(0));
			i++;
		}
		System.out.println(i+" iterations");
	}

}
