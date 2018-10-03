
public class Diffusion_Cell {
	//NOTE: a diffusion cell ~= to one pixel
	int cloth_ref;
	int x,y;				// position
	boolean orientation;	// true: x-parallel (weft), false: y-parallel (warp) 

	public Diffusion_Cell(int ref, int xp, int yp, boolean ori) {
		cloth_ref = ref;
		x = xp;
		y = yp;
		orientation = ori;
	}

}
