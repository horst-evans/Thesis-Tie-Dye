import java.util.ArrayList;

public class Cloth_Cell {
	int id;
	boolean isWeft;
	int v_pos;
	int x,y;
	boolean up_orientation; // true means up, false means down
	ArrayList<ArrayList<Diffusion_Cell>> d_cells;
	
	public Cloth_Cell(int idp, boolean iswf, int vp, int xp, int yp, boolean ori) {
		id = idp;
		isWeft = iswf;
		v_pos = vp;
		x = xp;
		y = yp;
		up_orientation = ori;
		d_cells = new ArrayList<ArrayList<Diffusion_Cell>>();
		Create_Diffusions();
	}
	
	public void Create_Diffusions() {
		int layer = isWeft ? 0 : 1;
		int x_gap = !isWeft ? Run_Simulation.fiber_gap : 0;
		int y_gap = isWeft ? Run_Simulation.fiber_gap : 0;
		//start at the gap _|||_, continue until the max - the gap
		for(int i = x_gap; i < Run_Simulation.thread_weft_size-x_gap; i++) {
			ArrayList<Diffusion_Cell> row = new ArrayList<Diffusion_Cell> ();
			for(int j = y_gap; j < Run_Simulation.thread_warp_size-y_gap; j++) {
				Diffusion_Cell nd = new Diffusion_Cell(id, x+i, y+j, layer, isWeft);
				row.add(nd);
			}
			d_cells.add(row);
		}
	}
	
}
