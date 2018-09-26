
public class Diffusion_Cell {
	int cloth_ref;
	int layer_ref;
	float fiber_gap;
	float x,y;
	float porosity; 		// [0-1)
	float tortuosity;		// [0-1)
	boolean orientation;	// true: x-parallel, false: y-parallel (this is the fiber's orientation) 
}
