package neurality;

import neurality.NeuralRegistry.NeuronWrapper;

/**
 * The "blueprint" class for neural networks. Maps do not contain actual neurons, rather they contain the information to build a neural network.
 * @author James
 *
 */
public class NeuralMap {
	
	NeuralRegistry registry;
	
	NeuronWrapper[][] map;
	double[][][] mods;
	
	int[][][][] connections;
	int[][][] outputConnections;
	
	int width;
	int height;
	int inputs;
	int outputs;
	
	/**
	 * Generate a new map from scratch.
	 * @param registry The registry to draw instantiatable types and parameters from.
	 * @param width The number of hidden layers in this map.
	 * @param height The maximum size of all hidden layers.
	 * @param inputs The size of the input layer (i.e. the number of inputs).
	 * @param outputs The size of the output layer (i.e. the number of outputs).
	 */
	public NeuralMap(NeuralRegistry registry, int width, int height, int inputs, int outputs){
		this.registry = registry;
		this.width = width;
		this.height = height;
		this.inputs = inputs;
		this.outputs = outputs;
		
		float bias = registry.getBias();
		float complexity = 1/registry.getComplexity();
		NeuronWrapper[] register = registry.getWrappers();
		
		map = new NeuronWrapper[width][height];
		connections = new int[width][height][][];
		mods = new double[width][height][];
		for(int ix = 0; ix < width; ix++)
			for(int iy = 0; iy < height; iy++){
				if(Math.random() < bias){
					NeuronWrapper wrapper = register[(int) Math.round(Math.random()*(register.length-1))];
					map[ix][iy] = wrapper;
					
					mods[ix][iy] = new double[wrapper.modCount];
					for(int i = 0; i < wrapper.modCount; i++) mods[ix][iy][i] = wrapper.minModValue + (Math.random()*(wrapper.maxModValue-wrapper.minModValue));
					
					int inputCount = 0;
					while(Math.random() > complexity) inputCount++;
					connections[ix][iy] = new int[inputCount][2];
					for(int i = 0; i < inputCount; i++){
						int x = (int) Math.round(Math.random() * ix);
						int y;
						if(x == 0) y = (int) Math.round(Math.random() * (inputs-1));
						else y = (int) Math.round(Math.random() * (height-1));
						connections[ix][iy][i][0] = x;
						connections[ix][iy][i][1] = y;
					}
				}
			}
		
		outputConnections = new int[outputs][][];
		for(int ix = 0; ix < outputs; ix++){
			int inputCount = 0;
			while(Math.random() > complexity) inputCount++;
			outputConnections[ix] = new int[inputCount][2];
			for(int i = 0; i < inputCount; i++){
				int x = (int) Math.round(Math.random() * width);
				int y;
				if(x == 0) y = (int) Math.round(Math.random() * (inputs-1));
				else y = (int) Math.round(Math.random() * (height-1));
				outputConnections[ix][i][0] = x;
				outputConnections[ix][i][1] = y;
			}
		}
	}
	
	/**
	 * Generate this map as a clone of another.
	 * @param template The map to clone.
	 * @param radiation The number of times to mutate the new map. A value of 0 will result in the clone being identical to its template.
	 */
	public NeuralMap(NeuralMap template, int radiation){
		registry = template.registry;
		width = template.width;
		height = template.height;
		inputs = template.inputs;
		outputs = template.outputs;
		map = template.map;
		connections = template.connections;
		
		for(int i = 0; i < radiation; i++){
			int x = (int) Math.round(Math.random()*width);
			if(x == width) {
				int y = (int) Math.round(Math.random()*(outputs-1));
				double s = Math.random();
				if(s < 0.5) {
					int[][] inputs = outputConnections[y];
					int[][] temp = inputs;
					inputs = new int[inputs.length+1][2];
					for(int k = 0; k < temp.length; k++) inputs[k] = temp[k];
					int ix = (int) Math.round(Math.random()*width);
					int iy;
					if(ix == 0) iy = (int) Math.round(Math.random()*(this.inputs-1));
					else iy = (int) Math.round(Math.random()*(this.height-1));
					inputs[inputs.length-1][0] = ix;
					inputs[inputs.length-1][1] = iy;
					outputConnections[y] = inputs;
				} else {
					int[][] inputs = outputConnections[y];
					int[][] temp = inputs;
					int skip = (int) Math.round(Math.random()*(inputs.length-1));
					inputs = new int[inputs.length-1][2];
					for(int k = 0; k < inputs.length; k++) {
						if(k >= skip) inputs[k] = temp[k+1];
						else inputs[k] = temp[k];
					}
					outputConnections[y] = inputs;
				}
			} else {
				int y = (int) Math.round(Math.random()*(height-1));
				double s = Math.random();
				if(s < 0.25) {
					NeuronWrapper[] register = registry.getWrappers();
					NeuronWrapper wrapper = register[(int) Math.round(Math.random()*(register.length-1))];
					map[x][y] = wrapper;
					
					mods[x][y] = new double[wrapper.modCount];
					for(int ii = 0; ii < wrapper.modCount; ii++) mods[x][y][ii] = wrapper.minModValue + (Math.random()*(wrapper.maxModValue-wrapper.minModValue));
					
					int inputCount = 0;
					while(Math.random() > registry.getComplexity()) inputCount++;
					connections[x][y] = new int[inputCount][2];
					for(int ii = 0; ii < inputCount; ii++){
						int kx = (int) Math.round(Math.random() * x);
						int ky;
						if(kx == 0) ky = (int) Math.round(Math.random() * (inputs-1));
						else ky = (int) Math.round(Math.random() * (height-1));
						connections[x][y][ii][0] = kx;
						connections[x][y][ii][1] = ky;
					}
				} else if(s < 0.5) {
					int[][] inputs = connections[x][y];
					int[][] temp = inputs;
					inputs = new int[inputs.length+1][2];
					for(int k = 0; k < temp.length; k++) inputs[k] = temp[k];
					int ix = (int) Math.round(Math.random()*x);
					int iy;
					if(ix == 0) iy = (int) Math.round(Math.random()*(this.inputs-1));
					else iy = (int) Math.round(Math.random()*(this.height-1));
					inputs[inputs.length-1][0] = ix;
					inputs[inputs.length-1][1] = iy;
					connections[x][y] = inputs;
				} else if(s < 0.75) {
					int[][] inputs = connections[x][y];
					int[][] temp = inputs;
					int skip = (int) Math.round(Math.random()*(inputs.length-1));
					inputs = new int[inputs.length-1][2];
					for(int k = 0; k < inputs.length; k++) {
						if(k >= skip) inputs[k] = temp[k+1];
						else inputs[k] = temp[k];
					}
					connections[x][y] = inputs;
				} else {
					int target = (int) Math.round(Math.random()*(mods[x][y].length-1));
					mods[x][y][target] = map[x][y].minModValue + (map[x][y].maxModValue - map[x][y].minModValue)*Math.random();
				}
			}
		}
	}
	
}
