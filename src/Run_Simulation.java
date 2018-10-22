import processing.core.PApplet;

public class Run_Simulation extends PApplet{
//NOTE: a diffusion cell ~= to one pixel
//TODO fix isWeft, vpos, and up_orientation (all closely (?) related)
	
	public static float t1 = 1f;
	public static float t2 = .47f;
	public static float I = 1;
	public static float II = 1;
	public static float III = 1;
	public static float IV = 1;
	public static float V = 1;
	
	public static float porosity = 0.5f;
	public static int fiber_gap = 1;			// gap between fibers
	public static int fiber_size = 1;
	public static float vmax = 1;				// total volume of a diffusion cell
	public static float diff_density = 1;		// phi (φ)
	//public static String pattern = "plain";	// crisscross
	//plain is currently the default, will add more later
	public static int weft = 0;
	public static int warp = 0;
	public static int w = 400;
	public static int h = 300;
	public Cloth_Model cm;
	
	public static void main(String[] args) {
		PApplet.main("Run_Simulation");
	}
	
	public void settings(){
		size(w,h);
    }

    public void setup(){
    	fill(120,50,240);
    	cm = new Cloth_Model();
    }

    public void draw(){
    	
    }
    
    public void ficks2nd() {
    	//diffusion density (φ - phi)
    	//time
    	//diffusion coefficient
    	//x-axis displacement
    	//y-axis displacement
    	//z-axis displacement   	
    }
    
    // (1) - (2)
    
    // (3)
    public float t3() {
    	return 1f;
    }
    
    public float tortuosity() {	//calculate all 
    	return t1 * t2 * t3();
    }
    
    // (4)
    public float D(float dye_concentration) {
    	//diffusion coefficient
    	return porosity * tortuosity() * (dye_concentration / diff_density);
    }
    
    // (5)
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
