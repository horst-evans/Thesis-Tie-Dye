import java.util.ArrayList;

public class Layer {
	ArrayList<ArrayList<Cloth_Cell>> fibers;
	
	public Layer(int n, boolean first_down, boolean isweft) {
		int cell_size = (Run_Simulation.fiber_size + Run_Simulation.fiber_gap);
		int num = n/cell_size;
		int id=0;
		ArrayList<Cloth_Cell> fiber = new ArrayList<Cloth_Cell>();
		boolean up = first_down;
		boolean fup = up;
		for(int i = 0; i < num; i++) {
			for(int j = 0; j < num; j++) {
				int xp = i * cell_size;
				int yp = i * cell_size;
				Cloth_Cell nc = new Cloth_Cell(id,isweft,0,xp,yp,up);
				up = !up;
				fiber.add(nc);
				id++;
			}
			up = !fup;
			fup = !fup;
			fibers.add(fiber);
		}
	}

	public int hasFiber(Diffusion_Cell d) {
		for(int i=0; i<fibers.size(); i++) {
			for(int j=0; j<fibers.get(0).size(); j++) {
				if(fibers.get(i).get(j).id == d.cloth_ref) {
					if(fibers.get(i).get(j).up_orientation) return	2;
					else return 1;
				}
			}
		}
		return -1;
	}
	
	//TODO have gaps exist
	public boolean isGap(Diffusion_Cell d) {
		return false;
	}
}
