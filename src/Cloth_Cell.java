import java.util.ArrayList;

public class Cloth_Cell {
	int id;
	boolean isWeft; //used for layer
	int x,y;
	boolean up_orientation; // true means up, false means down
	ArrayList<ArrayList<Diffusion_Cell>> d_cells;
	
	public Cloth_Cell(int idp, boolean iswf, int vp, int xp, int yp, boolean ori) {
		id = idp;
		isWeft = iswf;
		x = xp;
		y = yp;
		up_orientation = ori;
		d_cells = new ArrayList<ArrayList<Diffusion_Cell>>();
		Create_Diffusions();
	}
	
	public void Create_Diffusions() {
		int layer = isWeft ? 0 : 1;
		//start at the gap _|||_, continue until the max - the gap
		if(isWeft) {
			for(int i = 0; i < Run_Simulation.thread_weft_size; i++) {
				ArrayList<Diffusion_Cell> row = new ArrayList<Diffusion_Cell> ();
				for(int j = 0; j < Run_Simulation.thread_warp_size; j++) {
					Diffusion_Cell new_d_cell;
					int lower_b = Run_Simulation.gap_size;
					int upper_b = Run_Simulation.thread_warp_size - 1 - Run_Simulation.gap_size;
					if(j >= lower_b && j <= upper_b){
						new_d_cell = new Diffusion_Cell(id, x+i, y+j, layer, up_orientation, false);
					}
					else {
						new_d_cell = new Diffusion_Cell(id, x+i, y+j, layer, up_orientation, true);
					}
					row.add(new_d_cell);
				}
				d_cells.add(row);
			}
		}
		//TODO different thread sizes (check logic)
		else {
			for(int i = 0; i < Run_Simulation.thread_warp_size; i++) {
				ArrayList<Diffusion_Cell> row = new ArrayList<Diffusion_Cell> ();
				for(int j = 0; j < Run_Simulation.thread_weft_size; j++) {
					Diffusion_Cell new_d_cell;
					int lower_b = Run_Simulation.gap_size;
					int upper_b = Run_Simulation.thread_weft_size - 1 - Run_Simulation.gap_size;
					if(i >= lower_b && i <= upper_b){
						new_d_cell = new Diffusion_Cell(id, x+i, y+j, layer, isWeft, false);
					}
					else {
						new_d_cell = new Diffusion_Cell(id, x+i, y+j, layer, isWeft, true);
					}
					row.add(new_d_cell);
				}
				d_cells.add(row);
			}
		}
	}
	
}
