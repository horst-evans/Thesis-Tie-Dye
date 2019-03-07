
public class Cloth_Model {
	Layer weft;				// collection of fibers
	Layer warp;				// collection of fibers
	
	public Cloth_Model() {
		weft = new Layer(Run_Simulation.w, false, true);
		warp = new Layer(Run_Simulation.h, true, false);
	}
	
	public boolean isGap(Diffusion_Cell d) {
		return (weft.isGap(d) || warp.isGap(d));
	}
	
	public Diffusion_Cell index(int x, int y, int layer) {
		//TODO need to take into account gaps!!
		//boundary cases
		x = x<0 ? 0 : x;
		x = x>Run_Simulation.w - 1 ? Run_Simulation.w - 1 : x;

		y = y<0 ? 0 : y;
		y = y>Run_Simulation.h - 1 ? Run_Simulation.h - 1 : y;
		
		//indices into fiber for cloth cell + overflow
		int cloth_x = x / Run_Simulation.thread_weft_size;
		int cloth_y = y / Run_Simulation.thread_weft_size;
		int diff_x = x % Run_Simulation.thread_warp_size;
		int diff_y = y % Run_Simulation.thread_warp_size;
		
		//index into cloth_cells
		if(layer==0) {
			//TODO d_cells can have a gap in them, but ArrayList doesn't know this
			Cloth_Cell cloth_cell = weft.fibers.get(cloth_x).get(cloth_y);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
		else {
			Cloth_Cell cloth_cell = warp.fibers.get(cloth_x).get(cloth_y);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
	}
}
