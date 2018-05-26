package neurality;

import neurality.NeuralRegistry.NeuronWrapper;

public class NeuralNetwork {
	
	NeuralMap map;
	
	private EntryNeuron[] inputLayer;
	private AbstractNeuron[][] hiddenLayers;
	private ExitNeuron[] outputLayer;
	
	public NeuralNetwork(NeuralMap map){
		this.map = map;
		
		inputLayer = new EntryNeuron[map.inputs];
		for(int i = 0; i < map.inputs; i++) inputLayer[i] = new EntryNeuron();
		
		boolean[][] dormant = new boolean[map.width][map.height];
		
		for(int ix = 0; ix < map.width; ix++)
			for(int iy = 0; iy < map.height; iy++){
				if(map.map[ix][iy] != null){
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
	}
}
