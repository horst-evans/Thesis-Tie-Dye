import java.util.ArrayList;

public class Layer {
	ArrayList<Cloth_Cell> fibers;
	
	public Layer(int n) {
		int cell_size = (Run_Simulation.fiber_size + Run_Simulation.fiber_gap);
		int num = n/cell_size;
		int id=0;
		fibers = new ArrayList<Cloth_Cell>();
		for(int i = 0; i < num; i++) {
			int xp = i * cell_size;
			int yp = i * cell_size;
			Cloth_Cell nc = new Cloth_Cell(id,false,0,xp,yp,false);
			fibers.add(nc);
			id++;
		}
	}
	
}
