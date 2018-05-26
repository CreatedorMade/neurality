package neurality;

import java.lang.reflect.InvocationTargetException;

public class NeuralNetwork {
	
	NeuralMap map;
	
	private EntryNeuron[] inputLayer;
	private AbstractNeuron[][] hiddenLayers;
	private ExitNeuron[] outputLayer;
	
	public NeuralNetwork(NeuralMap map) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		this.map = map;
		
		inputLayer = new EntryNeuron[map.inputs];
		for(int i = 0; i < map.inputs; i++) inputLayer[i] = new EntryNeuron();
		
		boolean[][] dormant = new boolean[map.width][map.height];
		
		for(int ix = map.width - 1; ix >= 0; ix++)
			for(int iy = 0; iy < map.height; iy++){
				if(map.map[ix][iy] != null && !dormant[ix][iy]){
					for(int kx = ix+1; kx < map.width; kx++)
						for(int ky = 0; ky < map.height; ky++){
							if(map.map[kx][ky] != null && map.connections[kx][ky].length != 0) for(int i = 0; i < map.connections[kx][ky].length; i++)
								if(map.connections[kx][ky][i][0] - 1 == ix && map.connections[kx][ky][i][1] == ky) dormant[ix][iy] = false;
						}
					for(int i = 0; i < map.outputs; i++)
						if(map.outputConnections[i].length != 0) for(int k = 0; k < map.outputConnections.length; k++)
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
					for(int i = 0; i < map.connections[ix][iy].length; i++){
						if(map.connections[ix][iy][i][0] == 0) inputCount++;
						else if(!dormant[map.connections[ix][iy][i][0] - 1][map.connections[ix][iy][i][1]]) inputCount++;
					}
					
					AbstractNeuron[] inputs = new AbstractNeuron[inputCount];
					int ii = 0;
					for(int i = 0; i < map.connections[ix][iy].length; i++) {
						if(map.connections[ix][iy][i][0] == 0){
							inputs[ii] = inputLayer[map.connections[ix][iy][i][1]];
							ii++;
						} else if(!dormant[map.connections[ix][iy][i][0] - 1][map.connections[ix][iy][i][1]]){
							inputs[ii] = hiddenLayers[map.connections[ix][iy][i][0] - 1][map.connections[ix][iy][i][1]];
							ii++;
						}
					}
					
					double[] mods = new double[map.map[ix][iy].modCount];
					for(int i = 0; i < map.map[ix][iy].modCount; i++) mods[i] = map.mods[ix][iy][i];
					
					hiddenLayers[ix][li] = map.map[ix][iy].constructor.newInstance(inputs, map.map[ix][iy].maxInputs, mods);
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
					inputs[ii] = hiddenLayers[map.outputConnections[ix][i][0] - 1][map.outputConnections[ix][i][1]];
					ii++;
				}
			}
			
			outputLayer[ix] = new ExitNeuron(inputs);
		}
	}
	
	public void tick(){
		for(int i = 0; i < inputLayer.length; i++) inputLayer[i].update(new double[0]);
		for(int ix = 0; ix < hiddenLayers.length; ix++)
			for(int iy = 0; iy < hiddenLayers[ix].length; iy++){
				for(int i = 0; i < hiddenLayers[ix][iy].inputs.length; i++){
					hiddenLayers[ix][iy].values[i] = hiddenLayers[ix][iy].inputs[i].output;
				}
				hiddenLayers[ix][iy].update(hiddenLayers[ix][iy].values);
			}
		for(int ix = 0; ix < outputLayer.length; ix++){
			for(int i = 0; i < outputLayer[ix].inputs.length; i++){
				outputLayer[ix].values[i] = outputLayer[ix].inputs[i].output;
			}
			outputLayer[ix].update(outputLayer[ix].values);
		}
	}
	
	public void setInput(int index, double value){
		inputLayer[index].setValue(value);
	}
	
	public double getOutput(int index){
		return outputLayer[index].getValue();
	}
	
}
