
public class Diffusion_Cell {
	//NOTE: a diffusion cell ~= to one pixel
	int cloth_ref;
	int x,y,z;				// position
	boolean orientation;	// true: x-parallel (weft), false: y-parallel (warp) 
	//concentration of dye within cell [0-1]
	//TODO r+g+b cannot be greater than 1
	//float diffusion_density = 0;
	float red = 0;
	float green = 0;
	float blue = 0;
	
	public Diffusion_Cell(int ref, int xp, int yp, int zp, boolean ori) {
		cloth_ref = ref;
		x = xp;
		y = yp;
		z = zp;
		orientation = ori;
	}

}
