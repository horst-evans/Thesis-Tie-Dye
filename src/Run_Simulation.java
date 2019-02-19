import processing.core.PApplet;
import processing.core.PImage;

public class Run_Simulation extends PApplet{
	//NOTE: a diffusion cell ~= to one pixel
	//TODO fix isWeft, vpos, and up_orientation (all closely (?) related)
	//TODO doesn't work if w =/= h
	//TODO check lower and right bound (weird small bounce on only those sides)
	//TODO try something without corners
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
	public static float delta_d = 1.75f;//0.05f;		// mm
	public static float dye_concentration = 1f; // "defined arbitrarily"
	
	//public static String pattern = "plain";	// crisscross
	//plain is currently the default, will add more later
	// thread sizes = total size of thread(including gaps)
	public static int thread_weft_size = 6;
	public static int thread_warp_size = 6;
	public static int gap_size = 1;
	public static int w = 100; //weft ==
	public static int h = 100; //warp ||
	
	//variables for main loops
	public Cloth_Model cm;
	double[][] rates_top;
	double[][] rates_bot;
	PImage cloth_render;
	int iterations;
	int max_iter = 100;
	int dye_iter;
	int max_dye = 0;
	int iteration_mod = 10;
	String shape = "Square";
	
	public static void main(String[] args) {
		PApplet.main("Run_Simulation");
	}
	
	//neccessary for eclipse rendering
	public void settings(){
		size(w,h);
	}
	
    public void setup(){
    	cm = new Cloth_Model();
    	rates_top = new double[width][height];
    	rates_bot = new double[width][height];
    	cloth_render = createImage(w,h,RGB);
    	iterations  = 0;
    	dye_iter = 0;
    	dye(w/2,h/2,20);
    }

    public void draw(){
    	//print to screen every modulo
    	if (iterations % iteration_mod == 0){
    		System.out.println(iterations);
	    	//TODO save in 0000X format for GIF
	    	String number = String.format("%05d", iterations);
	    	save_image(shape+"_Gif/cloth_render_"+number+".jpg");
	    	//put on console
    		image(cloth_render,0,0);
    	}
    	//diffusion statement
    	if(iterations < max_iter) {
	    	//run fick's
    		for(int x=0; x<w; x++) {
	    		for(int y=0; y<h; y++) {
	    			//use the diffusion cell that is up between the two?
	    			ficks2nd(x,y,0);
	    			ficks2nd(x,y,1);
	    		}
	    	}
    		for(int x=0; x<w; x++) {
	    		for(int y=0; y<h; y++) {
	    			//use the diffusion cell that is up between the two?
	    			Diffusion_Cell top = cm.index(x, y, 0);
	    			Diffusion_Cell bot = cm.index(x, y, 1);
	    			top.diffusion_density = top.diffusion_density + (float) rates_top[x][y];
	    			bot.diffusion_density = bot.diffusion_density + (float) rates_bot[x][y];
	    		}
	    	}
	    	//increment clock
	    	iterations++;
	    	//re-apply dye to source
	    	if(dye_iter < max_dye) {
	    		dye(w/2,h/2,20);
	    		dye_iter++;
	    	}
    	}
    	//finished looping, so save file and end draw() loop
    	else {
    		save_image("cloth_render.jpg");
	    	//exit draw() loop
    		exit();
    	}
    }
    
    public void save_image(String filename) {
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
    			cloth_render.pixels[index] = color(255*(1-ratio),255*(1-ratio),255);
    		}
    	}
    	cloth_render.save(filename);
    	cloth_render.updatePixels();
    }
    
    //draws shape with top corner as x / y
    public void dye(int input_x, int input_y, int size) {
    	//Triangle
    	if(shape == "Triangle") {
    		int x = input_x - size/2;
    		int y = input_y - size/2;
	    	for(int i=0; i < size; i++) {
	    		for(int j=i; j < size; j++) {
	    			cm.index(x+i, y+j, 0).diffusion_density = 1;
	    	    	cm.index(x+i, y+j, 1).diffusion_density = 1;
	    		}
	    	}
    	}
    	//Square
    	else if(shape == "Square") {
    		int x = input_x - size/2;
    		int y = input_y - size/2;
	    	for(int i=0; i < size; i++) {
	    		for(int j=0; j < size; j++) {
	    			cm.index(x+i, y+j, 0).diffusion_density = 1;
	    	    	cm.index(x+i, y+j, 1).diffusion_density = 1;
	    		}
	    	}
    	}
    	//Circle
    	else if(shape == "Circle") {
    		int x = input_x;
    		int y = input_y;
    		int radius = size/2;
    		final double PI = 3.1415926535;
    		double i, angle, x1, y1;
    		for(i = 0; i < 360; i += 0.1) {
	    	    angle = i;
	    	    for(int j=0; j<=radius; j++) {
		    	    x1 = j * Math.cos(angle * PI / 180);
		    	    y1 = j * Math.sin(angle * PI / 180);
		    	    cm.index((int)(x+x1), (int)(y+y1), 0).diffusion_density = 1;
	    	    	cm.index((int)(x+x1), (int)(y+y1), 1).diffusion_density = 1;
	    	    }
    	    }
    	}
    	//Not Specified
    	else {
    		System.out.println("Specified Shape is not Implemented");
    		exit();
    	}
    }
    
    // (1) - (2)
    //calculates the rate of dye transfer for the target cell from the surrounding cells
    //positive means dye flowing in, negative means dye leaving
    public double ficks2nd(int dx, int dy, int cell_layer) {
    	int i = dx;
    	int j = dy;
    	Diffusion_Cell current_cell = cm.index(i, j, cell_layer);
    	//terms in fick's second law
    	//NOTE: D()'s '1/2' has been changed to '1' to fit with index logic
    	double d1 = D(current_cell, (cm.index(i+1,j,cell_layer)));
    	double m1 = (cm.index(i+1, j, cell_layer).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	double d2 = D(current_cell, (cm.index(i-1,j,cell_layer)));
    	double m2 = (cm.index(i-1, j, cell_layer).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	double d3 = D(current_cell, (cm.index(i,j+1,cell_layer)));
    	double m3 = (cm.index(i, j+1, cell_layer).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	double d4 = D(current_cell, (cm.index(i,j-1,cell_layer)));
    	double m4 = (cm.index(i, j-1, cell_layer).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	double d5 = D(current_cell, (cm.index(i,j,(cell_layer+1)%2)));
    	double m5 = (cm.index(i, j, (cell_layer+1)%2).diffusion_density - current_cell.diffusion_density)/delta_d;
    	
    	//equation
    	double eq = (d1*m1 + d2*m2 + d3*m3 + d4*m4 + d5*m5) / delta_d;
    	//bounds checking
    	if(eq > 1) eq = 1;
    	else if(eq < -1) eq  = -1;
    	//if(eq != 0) System.out.println("eq: "+eq);
    	//update corresponding rates
    	if(cell_layer == 0) rates_top[i][j] = eq;
    	else rates_bot[i][j] = eq;
    	return eq;
    }
    
    // (3) //TODO figure t3 out (gaps)
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
