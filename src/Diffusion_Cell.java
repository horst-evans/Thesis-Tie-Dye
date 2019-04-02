
public class Diffusion_Cell {
	//NOTE: a diffusion cell ~= to one pixel
	int cloth_ref;
	int x,y,z;		// position (z is layer)
	boolean isUp;	// if false, is beneath another thread 
	boolean isGap;
	//maximum concentration of dye within cell [0-1] (total)
	float red = 0;
	float green = 0;
	float blue = 0;
	
	public Diffusion_Cell(int ref, int xp, int yp, int zp, boolean up, boolean gap) {
		cloth_ref = ref;
		x = xp;
		y = yp;
		z = zp;
		isUp = up;
		isGap = gap;
	}

}
