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
					if(wrapper.maxInputs >= 0) inputCount = Math.min(inputCount, wrapper.maxInputs);
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
	 * Generate this map from a savestring. Use save() to acquire a savestring.
	 * @param registry The registry to draw instantiatable types and parameters from. Should be identical to the registry used to create the original map.
	 * @param savestring The savestring to draw structural data from.
	 */
	public NeuralMap(NeuralRegistry registry, String savestring){
		//TODO do this
	}
	
	/**
	 * Generate this map as a clone of another.
	 * @param template The map to clone.
	 * @param radiation The number of times to mutate the new map. A value of 0 will result in the clone being identical to its template.
	 */
	public NeuralMap(NeuralMap template, int radiation){
		//God damn i hate this method
		registry = template.registry;
		width = template.width;
		height = template.height;
		inputs = template.inputs;
		outputs = template.outputs;
		
		map = new NeuronWrapper[template.map.length][];
		for(int i = 0; i < template.map.length; i++) map[i] = template.map[i].clone();
		
		connections = new int[template.connections.length][][][];
		for(int i = 0; i < template.connections.length; i++) {
			connections[i] = new int[template.connections[i].length][][];
			for(int j = 0; j < template.connections[i].length; j++) {
				if(template.connections[i][j] != null){
					connections[i][j] = new int[template.connections[i][j].length][];
					for(int k = 0; k < template.connections[i][j].length; k++) {
						connections[i][j][k] = template.connections[i][j][k].clone();
					}
				}
			}
		}
		
		outputConnections = new int[template.outputConnections.length][][];
		for(int i = 0; i < template.outputConnections.length; i++) {
			outputConnections[i] = new int[template.outputConnections[i].length][];
			for(int j = 0; j < template.outputConnections[i].length; j++) {
				outputConnections[i][j] = template.outputConnections[i][j].clone();
			}
		}
		
		mods = new double[template.mods.length][][];
		for(int i = 0; i < template.mods.length; i++) {
			mods[i] = new double[template.mods[i].length][];
			for(int j = 0; j < template.mods[i].length; j++) {
				if(template.mods[i][j] != null) mods[i][j] = template.mods[i][j].clone();
			}
		}
		
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
				} else if(outputConnections[y].length != 0) {
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
				if(map[x][y] != null) {
					double s = Math.random();
					if(s < 0.33) {
						NeuronWrapper[] register = registry.getWrappers();
						NeuronWrapper wrapper = register[(int) Math.round(Math.random()*(register.length-1))];
						map[x][y] = wrapper;
						
						mods[x][y] = new double[wrapper.modCount];
						for(int ii = 0; ii < wrapper.modCount; ii++) mods[x][y][ii] = wrapper.minModValue + (Math.random()*(wrapper.maxModValue-wrapper.minModValue));
						
						int inputCount = 0;
						while(Math.random() > 1/registry.getComplexity()) inputCount++;
						if(wrapper.maxInputs >= 0) inputCount = Math.min(inputCount, wrapper.maxInputs);
						connections[x][y] = new int[inputCount][2];
						for(int ii = 0; ii < inputCount; ii++){
							int kx = (int) Math.round(Math.random() * x);
							int ky;
							if(kx == 0) ky = (int) Math.round(Math.random() * (inputs-1));
							else ky = (int) Math.round(Math.random() * (height-1));
							connections[x][y][ii][0] = kx;
							connections[x][y][ii][1] = ky;
						}
					} else if(s < 0.66) {
						int target = 0;
						while(Math.random() > 1/registry.getComplexity()) target++;
						if(map[x][y].maxInputs >= 0) target = Math.min(target, map[x][y].maxInputs);
						if(connections[x][y].length < target){
							int[][] inputs = connections[x][y].clone();
							int[][] temp = inputs.clone();
							inputs = new int[inputs.length+1][2];
							for(int k = 0; k < temp.length; k++) inputs[k] = temp[k];
							int ix = (int) Math.round(Math.random()*x);
							int iy;
							if(ix == 0) iy = (int) Math.round(Math.random()*(this.inputs-1));
							else iy = (int) Math.round(Math.random()*(this.height-1));
							inputs[inputs.length-1][0] = ix;
							inputs[inputs.length-1][1] = iy;
							connections[x][y] = inputs;
						} else if(connections[x][y].length > target){
							int[][] inputs = connections[x][y].clone();
							int[][] temp = inputs.clone();
							int skip = (int) Math.round(Math.random()*(inputs.length-1));
							inputs = new int[inputs.length-1][2];
							for(int k = 0; k < inputs.length; k++) {
								if(k >= skip) inputs[k] = temp[k+1];
								else inputs[k] = temp[k];
							}
							connections[x][y] = inputs;
						}
					} else if(mods[x][y].length != 0){
						int target = (int) Math.round(Math.random()*(mods[x][y].length-1));
						mods[x][y][target] = map[x][y].minModValue + (map[x][y].maxModValue - map[x][y].minModValue)*Math.random();
					}
				} else {
					NeuronWrapper[] register = registry.getWrappers();
					NeuronWrapper wrapper = register[(int) Math.round(Math.random()*(register.length-1))];
					map[x][y] = wrapper;
					
					mods[x][y] = new double[wrapper.modCount];
					for(int ii = 0; ii < wrapper.modCount; ii++) mods[x][y][ii] = wrapper.minModValue + (Math.random()*(wrapper.maxModValue-wrapper.minModValue));
					
					int inputCount = 0;
					while(Math.random() > 1/registry.getComplexity()) inputCount++;
					connections[x][y] = new int[inputCount][2];
					for(int ii = 0; ii < inputCount; ii++){
						int kx = (int) Math.round(Math.random() * x);
						int ky;
						if(kx == 0) ky = (int) Math.round(Math.random() * (inputs-1));
						else ky = (int) Math.round(Math.random() * (height-1));
						connections[x][y][ii][0] = kx;
						connections[x][y][ii][1] = ky;
					}
				}
			}
		}
	}
	
	public String save(){
	/**
	 * Returns a string that can later be used to reconstitute an identical NeuralMap.
	 * @return A string containing '[', ']', ',', and possibly any of the numeral characters. Save this to a file to reconstitute the map later.
	 */
		String str = "[["+registry.getWrappers().length+","+width+","+height+","+inputs+","+outputs+"],[";
		
		for(int iy = 0; iy < height; iy++){
			str += "[";
			for(int ix = 0; ix < width; ix++){
				str += "[";
				
				if(map[ix][iy] != null){
					str += registry.getIndex(map[ix][iy])+",[";
					
					for(int i = 0; i < mods[ix][iy].length; i++){
						str += mods[ix][iy][i];
						if(i + 1 < mods[ix][iy].length) str += ",";
					}
					str += "],[";
					
					for(int i = 0; i < connections[ix][iy].length; i++){
						str += connections[ix][iy][i][0] + "," + connections[ix][iy][i][1];
						if(i + 1 < connections[ix][iy].length) str += ",";
					}
					str += "]";
				}
				
				str += "]";
				if(ix + 1 < width) str += ",";
			}
			str += "]";
			if(iy + 1 < height) str += ",";
		}
		str += "],[";
		
		for(int i = 0; i < outputConnections.length; i++){
			str += "[";
			for(int k = 0; k < outputConnections[i].length; k++){
				str += outputConnections[i][k][0] + "," + outputConnections[i][k][1];
				if(k + 1 < outputConnections[i].length) str += ",";
			}
			str += "]";
			if(i + 1 < outputConnections.length) str += ",";
		}
		str += "]]";
		return str;
	}
	
	public String saveReadable(){
		/**
		 * Like save(), but saves in a human-readable format that cannot be loaded from. Only useful for analyzing the structure of a network.
		 * @return A savestring with added newlines, indentation and labels that make it readable by humans but useless for loading.
		 */
		String str = "[\n map_info: [\n  reg_size: "+registry.getWrappers().length+",\n  width: "+width+",\n  height: "+height+",\n  inputs: "+inputs+",\n  outputs: "+outputs+"\n ],\n map_data: [\n";
		
		for(int ix = 0; ix < width; ix++){
			str += "  layer_"+ix+": [\n";
			for(int iy = 0; iy < height; iy++){
				str += "   neuron_"+iy+": [\n    type_index: ";
				
				if(map[ix][iy] != null){
					str += registry.getIndex(map[ix][iy])+" ("+map[ix][iy].constructor.getDeclaringClass().getSimpleName()+"),\n    mods: [\n     ";
					
					for(int i = 0; i < mods[ix][iy].length; i++){
						str += ("mod_"+i+": ")+mods[ix][iy][i];
						if(i + 1 < mods[ix][iy].length) str += ",\n     ";
					}
					str += "\n    ],\n    connections: [\n     ";
					
					for(int i = 0; i < connections[ix][iy].length; i++){
						str += ("connection_"+i+": ")+connections[ix][iy][i][0] + "," + connections[ix][iy][i][1];
						if(i + 1 < connections[ix][iy].length) str += ",\n     ";
					}
					str += "\n    ]";
				} else str += "null";
				
				str += "\n   ]";
				if(iy + 1 < height) str += ",\n   ";
			}
			str += "\n  ]";
			if(ix + 1 < width) str += ",\n  ";
		}
		str += "\n ],\n outputs: [\n  ";
		
		for(int i = 0; i < outputConnections.length; i++){
			str += "output_"+i+": [\n   ";
			for(int k = 0; k < outputConnections[i].length; k++){
				str += ("connection_"+k+": ")+outputConnections[i][k][0] + "," + outputConnections[i][k][1];
				if(k + 1 < outputConnections[i].length) str += ",\n   ";
			}
			str += "\n  ]";
			if(i + 1 < outputConnections.length) str += ",\n  ";
		}
		str += "\n ]\n]";
		return str;
	}
	
}
