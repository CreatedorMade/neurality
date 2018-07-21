package neurality;

import java.lang.reflect.InvocationTargetException;

/**
 * The class that does all the work. Transcribed from neural maps.
 * @author James
 *
 */
public class NeuralNetwork {
	
	NeuralMap map;
	
	private EntryNeuron[] inputLayer;
	private AbstractNeuron[][] hiddenLayers;
	private ExitNeuron[] outputLayer;
	
	/**
	 * Transcribe a new neural network from a map. This constructs every single neuron in the network. Some details from the map may be omitted during transcription for optimization purposes.
	 * @param map The map to transcribe from. The transcribed network will be identical to any other networks transcribed from the same map.
	 * @throws InstantiationException If something is terribly wrong.
	 * @throws IllegalAccessException If something is terribly wrong.
	 * @throws IllegalArgumentException If something is terribly wrong.
	 * @throws InvocationTargetException If something is terribly wrong.
	 */
	public NeuralNetwork(NeuralMap map) {

		this.map = map;
		
		inputLayer = new EntryNeuron[map.inputs];
		for(int i = 0; i < map.inputs; i++) inputLayer[i] = new EntryNeuron();
		
		boolean[][] dormant = new boolean[map.width][map.height];
		
		for(int ix = map.width - 1; ix >= 0; ix--)
			for(int iy = 0; iy < map.height; iy++){
				if(map.map[ix][iy] != null && !dormant[ix][iy]){
					for(int kx = ix+1; kx < map.width; kx++)
						for(int ky = 0; ky < map.height; ky++){
							if(map.map[kx][ky] != null && map.connections[kx][ky] != null && map.connections[kx][ky].length != 0) 
								for(int i = 0; i < map.connections[kx][ky].length; i++)
									if(map.connections[kx][ky][i][0] - 1 == ix && map.connections[kx][ky][i][1] == ky) dormant[ix][iy] = false;
						}
					for(int i = 0; i < map.outputs; i++)
						if(map.outputConnections[i].length != 0) for(int k = 0; k < map.outputConnections[i].length; k++)
							if(map.outputConnections[i][k][0] - 1 == ix && map.outputConnections[i][k][1] == iy) dormant[ix][iy] = false;
				} else dormant[ix][iy] = true;
			}
		
		hiddenLayers = new AbstractNeuron[map.width][];
		
		for(int ix = 0; ix < map.width; ix++){
			int layerHeight = 0;
			for(int iy = 0; iy < map.height; iy++) if(!dormant[ix][iy]) layerHeight++;
			
			hiddenLayers[ix] = new AbstractNeuron[layerHeight];
			int li = 0;
			for(int iy = 0; iy < map.height; iy++){
				if(!dormant[ix][iy]){
					int inputCount = 0;
					if(map.connections[ix][iy] != null)
						for(int i = 0; i < map.connections[ix][iy].length; i++){
							if(map.connections[ix][iy][i][0] == 0) inputCount++;
							else if(!dormant[map.connections[ix][iy][i][0] - 1][map.connections[ix][iy][i][1]]) inputCount++;
						}
					
					AbstractNeuron[] inputs = new AbstractNeuron[inputCount];
					int ii = 0;
					if(map.connections[ix][iy] != null)
						for(int i = 0; i < map.connections[ix][iy].length; i++) {
							if(map.connections[ix][iy][i][0] == 0){
								inputs[ii] = inputLayer[map.connections[ix][iy][i][1]];
								ii++;
							} else if(!dormant[map.connections[ix][iy][i][0] - 1][map.connections[ix][iy][i][1]]){
								for(AbstractNeuron t : hiddenLayers[map.connections[ix][iy][i][0]-1]) {
									if(t.y == map.connections[ix][iy][i][1]) {
										inputs[ii] = t;
										break;
										//Let's hope this loop breaks eventually
										//If it doesn't your computer is haunted
									}
								}
								ii++;
							}
						}
					
					double[] mods = new double[map.map[ix][iy].modCount];
					
					for(int i = 0; i < map.map[ix][iy].modCount; i++) 
						mods[i] = map.mods[ix][iy][i];
					try {
						hiddenLayers[ix][li] = map.map[ix][iy].constructor.newInstance(inputs, map.map[ix][iy].maxInputs, mods);
						hiddenLayers[ix][li].x = ix;
						hiddenLayers[ix][li].y = iy;
						hiddenLayers[ix][li].start(mods);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						System.err.println("Something is extremely wrong! Please report this error, with the attached stack trace and class that caused it.");
						System.err.println("Please attach source code of this class: "+map.map[ix][iy].constructor.getDeclaringClass().getName());
						System.err.println("Note: this error will result in an unavoidable NullPointerException.");
						e.printStackTrace();
					}
					li++;
				}
			}
		}
		
		outputLayer = new ExitNeuron[map.outputs];
		for(int ix = 0; ix < map.outputs; ix++){
			int inputCount = 0;
			for(int i = 0; i < map.outputConnections[ix].length; i++){
				if(map.outputConnections[ix][i][0] == 0) inputCount++;
				else if(!dormant[map.outputConnections[ix][i][0] - 1][map.outputConnections[ix][i][1]]) inputCount++;
			}
			
			AbstractNeuron[] inputs = new AbstractNeuron[inputCount];
			int ii = 0;
			for(int i = 0; i < map.outputConnections[ix].length; i++) {
				if(map.outputConnections[ix][i][0] == 0){
					inputs[ii] = inputLayer[map.outputConnections[ix][i][1]];
					ii++;
				} else if(!dormant[map.outputConnections[ix][i][0] - 1][map.outputConnections[ix][i][1]]){
					for(AbstractNeuron t : hiddenLayers[map.outputConnections[ix][i][0]-1]) {
						if(t.y == map.outputConnections[ix][i][1]) {
							inputs[ii] = t;
							break;
						}
					}
					ii++;
				}
			}
			
			outputLayer[ix] = new ExitNeuron(inputs);
		}
	}
	
	/**
	 * Update every neuron. Call this every "tick" in your simulation's update loop.
	 */
	public void tick(){
		for(int i = 0; i < inputLayer.length; i++) inputLayer[i].output = inputLayer[i].update(new double[0]);
		for(int ix = 0; ix < hiddenLayers.length; ix++)
			for(int iy = 0; iy < hiddenLayers[ix].length; iy++){
				for(int i = 0; i < hiddenLayers[ix][iy].inputs.length; i++){
					hiddenLayers[ix][iy].values[i] = hiddenLayers[ix][iy].inputs[i].output;
				}
				hiddenLayers[ix][iy].output = hiddenLayers[ix][iy].update(hiddenLayers[ix][iy].values);
			}
		for(int ix = 0; ix < outputLayer.length; ix++){
			for(int i = 0; i < outputLayer[ix].inputs.length; i++){
				outputLayer[ix].values[i] = outputLayer[ix].inputs[i].output;
			}
			outputLayer[ix].output = outputLayer[ix].update(outputLayer[ix].values);
		}
	}
	
	/**
	 * Set the value of the specified input.
	 * @param index The input to set. Values outside of the input layer will generate an ArrayIndexOutOfBoundsException.
	 * @param value The value to set. This gets sent to all neurons that take input from this specific input.
	 * @throws ArrayIndexOutOfBoundsException If the index is outside of the input layer.
	 */
	public void setInput(int index, double value){
		inputLayer[index].setValue(value);
	}
	
	/**
	 * Get the value of the specified output.
	 * @param index The output to get data from.
	 * @return The sum of all data being sent to this output.
	 * @throws ArrayIndexOutOfBoundsException If the index is outside of the output layer.
	 */
	public double getOutput(int index){
		return outputLayer[index].getValue();
	}
	
	/**
	 * Returns a string that can be used to restore the state of all neurons at a later time. NOTE: due to optimizations that are made during transcription, the network may contain fewer neurons than its map. This is normal.
	 * @return A string containing neural data. Save to a file to restore the network's state at a later time.
	 */
	public String saveState(){
		String str = "[[";
		
		for(int i = 0; i < inputLayer.length; i++){
			str += inputLayer[i].output;
			if(i + 1 < inputLayer.length) str += ",";
		}
		
		str += "],[";
		
		for(int x = 0; x < hiddenLayers.length; x++){
			for(int y = 0; y < hiddenLayers[x].length; y++){
				str += "["+hiddenLayers[x][y].output;
				double[] state = hiddenLayers[x][y].saveState();
				if(!(state == null || state.length == 0)){
					str += ",";
					for(int i = 0; i < state.length; i++){
						str += state[i];
						if(i+1 < state.length) str += ",";
					}
				}
				str += "]";
				if(y + 1 < hiddenLayers[x].length) str += ",";
			}
			if(x + 1 < hiddenLayers.length) str += ",";
		}
		
		str += "],[";
		
		for(int i = 0; i < outputLayer.length; i++){
			str += outputLayer[i].getValue();
			if(i + 1 < outputLayer.length) str += ",";
		}
		
		str += "]]";
		return str;
	}
}
