
public class Cloth_Model {
	Layer weft;				// collection of fibers
	Layer warp;				// collection of fibers
	
	public Cloth_Model() {
		weft = new Layer(false, true);
		warp = new Layer(true, false);
	}
	
	public Diffusion_Cell index(int x, int y, int layer) {
		//TODO gaps (cloth_model):	index()
		//boundary cases
		x = x<0 ? 0 : x;
		x = x>Run_Simulation.w - 1 ? Run_Simulation.w - 1 : x;

		y = y<0 ? 0 : y;
		y = y>Run_Simulation.h - 1 ? Run_Simulation.h - 1 : y;
		
		//indices into fiber for cloth cell + overflow
		int cloth_x = x / Run_Simulation.thread_weft_size;
		int cloth_y = y / Run_Simulation.thread_warp_size;
		int diff_x = x % Run_Simulation.thread_weft_size;
		int diff_y = y % Run_Simulation.thread_warp_size;
		
		//index into cloth_cells
		if(layer==0) {
			if(cloth_x > Run_Simulation.w/Run_Simulation.thread_weft_size)System.out.println(cloth_x+", "+cloth_y);
			Cloth_Cell cloth_cell = weft.fibers.get(cloth_x).get(cloth_y);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
		else {
			//switch x and y if w =/= h
			Cloth_Cell cloth_cell = warp.fibers.get(cloth_y).get(cloth_x);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
	}
}
