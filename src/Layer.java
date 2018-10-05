import java.util.ArrayList;

public class Layer {
	ArrayList<Cloth_Cell> row_fibers; 	//rows if weft, columns if warp
	ArrayList<Cloth_Cell> col_fibers;
	
	public Layer() {
		int cell_size = (Run_Simulation.fiber_size + Run_Simulation.fiber_gap);
		int num_row_fibs = Run_Simulation.h / cell_size;
		int num_col_fibs = Run_Simulation.w / cell_size;
		
		int id=0;
		row_fibers = new ArrayList<Cloth_Cell>();
		for(int i = 0; i < num_row_fibs; i++) {
			int xp = i * cell_size;
			int yp = i * cell_size;
			Cloth_Cell nc = new Cloth_Cell(id,false,0,xp,yp,false);
			row_fibers.add(nc);
			id++;
		}
		
		col_fibers = new ArrayList<Cloth_Cell>();
		for(int i = 0; i < num_col_fibs; i++) {
			int xp = i * cell_size;
			int yp = i * cell_size;
			Cloth_Cell nc = new Cloth_Cell(id,false,0,xp,yp,false);
			col_fibers.add(nc);
			id++;
		}
	}
	
}
