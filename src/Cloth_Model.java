
public class Cloth_Model {
	Layer weft;				// collection of fibers
	Layer warp;				// collection of fibers
	
	public Cloth_Model() {
		weft = new Layer(Run_Simulation.h, false, true);
		warp = new Layer(Run_Simulation.w, true, false);
	}
	
	public boolean isGap(Diffusion_Cell d) {
		return (weft.isGap(d) || warp.isGap(d));
	}
	
	public Diffusion_Cell index(int x, int y, int layer) {
		int cell_size = (Run_Simulation.fiber_size + Run_Simulation.fiber_gap);
		int cloth_x = x / cell_size;
		int cloth_y = y / cell_size;
		int diff_x = x % cell_size;
		int diff_y = y % cell_size;
		if(layer==0) {
			Cloth_Cell cloth_cell = weft.fibers.get(cloth_x).get(cloth_y);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
		else {
			Cloth_Cell cloth_cell = warp.fibers.get(cloth_x).get(cloth_y);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
	}
}
