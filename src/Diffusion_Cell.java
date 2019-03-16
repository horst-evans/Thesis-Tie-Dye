
public class Diffusion_Cell {
	//NOTE: a diffusion cell ~= to one pixel
	int cloth_ref;
	int x,y,z;				// position (z is layer)
	boolean orientation;	// true: x-parallel (weft), false: y-parallel (warp) 
	boolean isGap;
	//maximum concentration of dye within cell [0-1] (total)
	float red = 0;
	float green = 0;
	float blue = 0;
	
	public Diffusion_Cell(int ref, int xp, int yp, int zp, boolean ori, boolean gap_bool) {
		cloth_ref = ref;
		x = xp;
		y = yp;
		z = zp;
		orientation = ori;
		isGap = gap_bool;
	}

}
