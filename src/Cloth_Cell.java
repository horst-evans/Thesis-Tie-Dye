import java.util.ArrayList;

public class Cloth_Cell {
	int id;
	boolean isWeft;
	float weft;
	float warp;
	int v_pos,x,y;
	float thread_size;
	float cell_gap;			//gap between cells
	boolean up_orientation; //true means up, false means down
	ArrayList<Diffusion_Cell> d_cells;
	
	public Cloth_Cell(int idp, boolean iswf, float wf, float wr, int vp, int xp, int yp, float tsz, float cg, boolean ori) {
		id = idp;
		isWeft = iswf;
		weft = wf;
		warp = wr;
		v_pos = vp;
		x = xp;
		y = yp;
		thread_size = tsz;
		cell_gap = cg;
		up_orientation = ori;
	}
	
	public void Create_Diffusions() {
		for(int i = 0; i < thread_size; i++) {
			//TODO take into account variables such as gap(currently fills entire square)
			Diffusion_Cell nd = new Diffusion_Cell(id, x+i, y+i, isWeft);
			d_cells.add(nd);
		}
	}
	
}
