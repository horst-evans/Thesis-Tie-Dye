
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
		//TODO need to take into account gaps!!
		int cloth_x = x / Run_Simulation.cell_size;
		int cloth_y = y / Run_Simulation.cell_size;
		int diff_x = x % Run_Simulation.cell_size;
		int diff_y = y % Run_Simulation.cell_size;
		//boundary cases
		diff_x = diff_x<0 ? 0 : diff_x;
		diff_x = diff_x>Run_Simulation.thread_weft_size ? Run_Simulation.thread_weft_size : diff_x;

		diff_y = diff_y<0 ? 0 : diff_y;
		diff_y = diff_y>Run_Simulation.thread_warp_size ? Run_Simulation.thread_warp_size : diff_y;
		
		if(layer==0) {
			//TODO d_cells have a gap in them, but ArrayList doesn't know this
			//System.out.println("c: "+cloth_x + " " + cloth_y);
			//System.out.println("d: "+diff_x + " " + diff_y);
			//System.out.println(cloth_cell.d_cells.size() * cloth_cell.d_cells.get(0).size());
			Cloth_Cell cloth_cell = weft.fibers.get(cloth_x).get(cloth_y);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
		else {
			Cloth_Cell cloth_cell = warp.fibers.get(cloth_x).get(cloth_y);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
	}
}
