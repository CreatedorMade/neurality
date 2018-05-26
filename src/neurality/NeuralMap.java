package neurality;

import neurality.NeuralRegistry.NeuronWrapper;

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
	
	public NeuralMap(NeuralMap template, int radiation){
		registry = template.registry;
		width = template.width;
		height = template.height;
		inputs = template.inputs;
		outputs = template.outputs;
		map = template.map;
		connections = template.connections;
		
		for(int i = 0; i < radiation; i++){
			
		}
	}
	
}
