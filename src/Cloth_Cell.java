import java.util.ArrayList;

public class Cloth_Cell {
	int id;
	boolean isWeft;
	int v_pos;
	int x,y;
	boolean up_orientation; // true means up, false means down
	ArrayList<Diffusion_Cell> d_cells;
	
	public Cloth_Cell(int idp, boolean iswf, int vp, int xp, int yp, boolean ori) {
		id = idp;
		isWeft = iswf;
		v_pos = vp;
		x = xp;
		y = yp;
		up_orientation = ori;
		d_cells = new ArrayList<Diffusion_Cell>();
		Create_Diffusions();
	}
	
	public void Create_Diffusions() {
		int x_gap = !isWeft ? Run_Simulation.fiber_gap : 0;
		int y_gap = isWeft ? Run_Simulation.fiber_gap : 0;
		for(int i = x_gap; i < Run_Simulation.weft-x_gap; i++) {
			for(int j = y_gap; j < Run_Simulation.warp-y_gap; j++) {
				//TODO take into account variables such as gap(currently fills entire square)
				Diffusion_Cell nd = new Diffusion_Cell(id, x+i, y+j, isWeft);
				d_cells.add(nd);
			}
		}
	}
	
}
