import processing.core.PApplet;
import processing.core.PImage;

public class Run_Simulation extends PApplet{
//NOTE: a diffusion cell ~= to one pixel
//TODO fix isWeft, vpos, and up_orientation (all closely (?) related)
//TODO fix cloth_model index
//TODO doesn't work if w =/= h
	public static float t1 = 1f;
	public static float t2 = .47f;
	public static float I = 1;
	public static float II = 1;
	public static float III = 1;
	public static float IV = 1;
	public static float V = 1;
	
	public static float porosity = 0.5f;
	public static float vmax = 1;				// total volume of a diffusion cell
	public static float diff_density = 1;		// phi (φ)
	public static float delta_t = 0.0005f;		// hours
	public static float delta_d = 0.05f;		// mm
	public static float dye_concentration = 1f; // "defined arbitrarily"
	
	//public static String pattern = "plain";	// crisscross
	//plain is currently the default, will add more later
	// thread sizes = total size of thread(including gaps)
	public static int thread_weft_size = 6;
	public static int thread_warp_size = 6;
	public static int gap_size = 1;
	public static int w = 300; //weft ==
	public static int h = 300; //warp ||
	
	//variables for main loops
	public Cloth_Model cm;
	PImage cloth_render;
	int iterations;
	int max_iter = 10;
	
	public static void main(String[] args) {
		PApplet.main("Run_Simulation");
	}
	
	public void settings(){
		size(w,h);
    }
	
    public void setup(){
    	//fill(120,50,240);
    	cm = new Cloth_Model();
    	cloth_render = createImage(w,h,RGB);
    	iterations  = 0;
    	dye(0,0,20);
    }

    public void draw(){
    	if(iterations < max_iter) {
    		System.out.println(iterations+1);
    		//apply dye
    		//dye(0,0);
	    	//run fick's
    		for(int x=0; x<w; x++) {
	    		for(int y=0; y<h; y++) {
	    			//use the diffusion cell that is up between the two?
	    			ficks2nd(x,y,0);
	    			ficks2nd(x,y,1);
	    		}
	    	}
	    	//increment clock
	    	iterations++;
    	}
    	//finished looping, so save file and end draw() loop
    	else {
    		//transcribe colors
	    	cloth_render.loadPixels();
	    	for(int x=0; x<w; x++) {
	    		for(int y=0; y<h; y++) {
	    			//use the diffusion cell that is up between the two?
	    			Diffusion_Cell dc1 = cm.index(x, y, 0);
	    			Diffusion_Cell dc2 = cm.index(x, y, 1);
	    			//is an average for now
	    			float ratio = (dc1.diffusion_density + dc2.diffusion_density) / 2;
	    			int index = w*x + y;
	    			cloth_render.pixels[index] = color(255*(1-ratio),255,255);
	    		}
	    	}
	    	cloth_render.save("cloth_render.jpg");
	    	cloth_render.updatePixels();
	    	//exit draw() loop
    		exit();
    	}
    }
    
    public void dye(int x, int y, int size) {
    	for(int i=0; i < size; i++) {
    		for(int j=0; j < size; j++) {
    			cm.index(x+i, y+j, 0).diffusion_density = 1;
    	    	cm.index(x+i, y+j, 1).diffusion_density = 1;
    		}
    	}
    }
    
    // (1) - (2)
    //cell_layer starts as weft (e = weft, a = warp)
    //TODO how does this handle overlaping cells?
    public float ficks2nd(int dx, int dy, int cell_layer) {
    	int i = dx;
    	int j = dy;
    	Diffusion_Cell current_cell = cm.index(i, j, cell_layer);
    	//terms
    	//NOTE: D() has been changed to '1's instead of '1/2's, as per the affected cells in m
    	float d1 = D(current_cell, (cm.index(i+1,j,cell_layer)));
    	float m1 = (cm.index(i+1, j, cell_layer).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	float d2 = D(current_cell, (cm.index(i-1,j,cell_layer)));
    	float m2 = (cm.index(i-1, j, cell_layer).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	float d3 = D(current_cell, (cm.index(i,j+1,cell_layer)));
    	float m3 = (cm.index(i, j+1, cell_layer).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	float d4 = D(current_cell, (cm.index(i,j-1,cell_layer)));
    	float m4 = (cm.index(i, j-1, cell_layer).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	float d5 = D(current_cell, (cm.index(i,j,(cell_layer+1)%2)));
    	float m5 = (cm.index(i, j, (cell_layer+1)%2).diffusion_density - current_cell.diffusion_density)/delta_d;
    	//equation
    	float eq = (d1*m1 + d2*m2 + d3*m3 + d4*m4 + d5*m5) / delta_d;
    	return eq;
    }
    
    // (3) //TODO figure this out
    public float t3(Diffusion_Cell f1, Diffusion_Cell f2) {
    	//gap and gap
    	if(cm.isGap(f1) && cm.isGap(f2)) return II;
    	//fiber and gap
    	else if(cm.isGap(f1) || cm.isGap(f2)) return III;
    	//different layers
    	else if(cm.weft.hasFiber(f1) == cm.warp.hasFiber(f2)) return I;
    	//same layer and parallel (weft layer and warp layer)
    	else if(cm.weft.hasFiber(f1) == cm.weft.hasFiber(f2) && cm.weft.hasFiber(f1) > 0) return V;
    	else if(cm.warp.hasFiber(f1) == cm.warp.hasFiber(f2) && cm.warp.hasFiber(f1) > 0) return V;
    	//same layer and perpendicular
    	else return IV;
    }
    
    public float tortuosity(Diffusion_Cell f1, Diffusion_Cell f2) {	//calculate all 
    	return t1 * t2 * t3(f1, f2);
    }
    
    // (4)
    public float D(Diffusion_Cell f1, Diffusion_Cell f2) {
    	//diffusion coefficient
    	//TODO check for correct implementation
    	return D0() * porosity * tortuosity(f1,f2);// * (dye_concentration / diff_density);
    }
    
    // (5)
    public float D0() { return 1.93f; } //see paper defaults
    public float D0(float mol_mass) {
    	//diffusion coefficient in free water
    	//mol_mass is molecular mass of the dye
    	float root = sqrt(76/mol_mass);
    	return 3.6f * root;
    }
    
    // (6)
    public float Vd() {
    	//returns value [0,1)
    	//volume capacity of dye absorption in the diff cell
    	float ratio_of_fiber = 1-porosity;
    	return ratio_of_fiber * vmax;
    }
    
    // (7) - (8)
    public float Ad(float k, float b) {
    	// k = "k is a constant" :/
    	float power = pow(diff_density, b);
    	return k * power;
    }
    
    public float Ad(float Kl) {	//Langmuir equation when b==1
    	//b == 1
    	//Kl = equilibrium constant
    	float numerator = Vd() * Kl * diff_density;
    	float denominator = 1 + (Kl * diff_density);
    	return numerator / denominator;
    }
    
    // (9)
    public float Vu(float B) {
    	//B = volume ratio (0,1]: used in patterns (pressing)
    	float ratio = 1-B;
    	return vmax * porosity * ratio;
    }
    
    public float V(float B) {
    	// b = normalized rgb values in an image
    	return Vu(B) + Vd();
    }
    
}
