import processing.core.PApplet;
import processing.core.PImage;
import java.awt.geom.Point2D;

public class Run_Simulation extends PApplet{
	//NOTE: a diffusion cell ~= to one pixel
	public static float t1 = 1f;
	public static float t2 = .47f;
	public static float I = 1;
	public static float II = 1;
	public static float III = 1;
	public static float IV = 1;
	public static float V = 1;
	
	public static float porosity = 0.5f;
	public static float vmax = 1;				//total volume of a diffusion cell
	public static float diff_density = 1;		//phi (φ)
	public static float delta_t = 0.0005f;		//hours
	public static float delta_d = 1.75f;		//(mm)
	//public static float dye_concentration = 1f; //"defined arbitrarily"
	public static float fold_multiplier = .2f;	//arbitrary limiter on 
												//diffusion rate between folds
	
	//public static String pattern = "plain";	// criss-cross
	//plain is currently the default, will add more later
	//thread sizes = total size of thread(including gaps)
	public static int thread_weft_size = 6;
	public static int thread_warp_size = 6;
	public static int gap_size = 1;
	
	public static int w = 200; //weft == (x)
	public static int h = 100; //warp || (y)
	
	//variables for main loops
	//fold relationship
	// "\" => invert indices
	// "/" => invert indices, then max - index
	// "|" => x = max - x
	// "-" => y = max - y
	Point2D[][] fold;
	PImage cloth_render;
	public Cloth_Model cm;
	double[][][] rates_top;
	double[][][] rates_bot;
	
	int iterations = 0;
	int max_iter = 100;
	int dye_iter = 0;
	int max_dye = 0;
	int iteration_mod = 10;
	String shape = "Triangle";
	
	public static void main(String[] args) {
		PApplet.main("Run_Simulation");
	}
	
	//necessary for eclipse rendering
	public void settings(){
		size(w,h);
	}
	
    public void setup(){
    	//change width and height to be evenly divisible by thread size
    	int ow = w;
    	int oh = h;
    	w = w-(w%thread_weft_size);
    	h = h-(h%thread_warp_size);
    	if(w!=ow) System.out.println("Width ("+ow+") Changed to "+w+", which divided by weft thread size ("+thread_weft_size+") creates "+w/thread_weft_size+" threads.");
    	if(h!=oh) System.out.println("Height ("+oh+") Changed to "+h+", which divided by warp thread size ("+thread_warp_size+") creates "+h/thread_warp_size+" threads.");
    	//instantiation & setup
    	cm = new Cloth_Model();
    	rates_top = new double[width][height][3];
    	rates_bot = new double[width][height][3];
    	fold = new Point2D[width][height];
    	//TODO change to ax+b format
    	fold_init(w/2, 0, w/2, h);
    	cloth_render = createImage(w,h,RGB);
    	setup_dye();
    	//dye(4*w/8,4*h/8,30,1);
    }

    public void draw(){
    	//print to screen every modulo
    	if (iterations % iteration_mod == 0){
    		System.out.println(iterations);
	    	//0000X format for GIF creation (so you can sort by number)
	    	String number = String.format("%05d", iterations);
	    	//gaps_visualize(shape+"_Gif/cloth_render_"+number+".jpg");
	    	save_image(shape+"_Gif/cloth_render_"+number+".jpg");
	    	//put on console
    		image(cloth_render,0,0);
    	}
    	//diffusion statement
    	if(iterations < max_iter) {
	    	//run fick's
    		for(int x=0; x<w; x++) {
	    		for(int y=0; y<h; y++) {
	    			//TODO use the diffusion cell that is up between the two (see paper)
	    			ficks2nd(x,y,0);
	    			ficks2nd(x,y,1);
	    		}
	    	}
    		for(int x=0; x<w; x++) {
	    		for(int y=0; y<h; y++) {
	    			//use the diffusion cell that is up between the two?
	    			Diffusion_Cell top = index(x, y, 0);
	    			Diffusion_Cell bot = index(x, y, 1);
	    			//red
	    			top.red = top.red + (float) rates_top[x][y][0];
	    			bot.red = bot.red + (float) rates_bot[x][y][0];
	    			//green
	    			top.green = top.green + (float) rates_top[x][y][1];
	    			bot.green = bot.green + (float) rates_bot[x][y][1];
	    			//blue
	    			top.blue = top.blue + (float) rates_top[x][y][2];
	    			bot.blue = bot.blue + (float) rates_bot[x][y][2];
	    		}
	    	}
	    	//increment clock
	    	iterations++;
	    	//re-apply dye to source
	    	if(dye_iter < max_dye) {
	    		setup_dye();
	    		dye_iter++;
	    	}
    	}
    	//finished looping, so save file and end draw() loop
    	else {
    		//gaps_visualize("cloth_render.jpg");
    		save_image("cloth_render.jpg");
	    	//exit draw() loop
    		exit();
    	}
    }
    
    public void save_image(String filename) {
    	//transcribe colors
    	cloth_render.loadPixels();
    	for(int y=0; y<h; y++) {
    		for(int x=0; x<w; x++) {
    			//TODO use the diffusion cell that is up between the two?
    			Diffusion_Cell dc1 = index(x, y, 0);
    			Diffusion_Cell dc2 = index(x, y, 1);
    			float red_ratio = 1-(dc1.red + dc2.red) / 2;
    			float green_ratio = 1-(dc1.green + dc2.green) / 2;
    			float blue_ratio = 1-(dc1.blue + dc2.blue) / 2;
    			int index = w*y + x;
    			cloth_render.pixels[index] = color(255*green_ratio*blue_ratio, 255*red_ratio*blue_ratio, 255*red_ratio*green_ratio);
    			
    		}
    	}
    	cloth_render.save(filename);
    	cloth_render.updatePixels();
    }
    
    public void gaps_visualize(String filename) {
    	//transcribe colors
    	cloth_render.loadPixels();
    	for(int y=0; y<h; y++) {
    		for(int x=0; x<w; x++) {
    			//use the diffusion cell that is up between the two?
    			Diffusion_Cell dc1 = index(x, y, 0);
    			Diffusion_Cell dc2 = index(x, y, 1);
    			int index = w*y + x;
    			if(dc1.isGap && dc2.isGap) cloth_render.pixels[index] = color(0);
    			else if (dc1.isGap || dc2.isGap) cloth_render.pixels[index] = color(255/2);
    			else cloth_render.pixels[index] = color(255);
    		}
    	}
    	cloth_render.updatePixels();
    	cloth_render.save(filename);
    }
    
    //draws shape with top corner as x / y
    public void dye(int input_x, int input_y, int size, int color) {
    	//TODO don't let dye go out of bounds
    	//Triangle
    	if(shape == "Triangle") {
    		int x = input_x - size/2;
    		int y = input_y - size/2;
	    	for(int i=0; i < size; i++) {
	    		for(int j=i; j < size; j++) {
	    			Diffusion_Cell dc0 = index(x+i, y+j, 0);
	    			Diffusion_Cell dc1 = index(x+i, y+j, 1);
	    			//red
	    			if(color == 0) {
		    	    	dc0.red = 1;
		    	    	dc1.red = 1;
	    			}
	    			//green
	    			else if(color == 1) {
		    	    	dc0.green = 1;
		    	    	dc1.green = 1;
	    			}
	    			//blue
	    			else if(color == 2) {
		    	    	dc0.blue = 1;
		    	    	dc1.blue = 1;
	    			}
	    			//check total saturation
	    			normalize_colors(dc0);
	    			normalize_colors(dc1);
	    		}
	    	}
    	}
    	//Square
    	else if(shape == "Square") {
    		int x = input_x - size/2;
    		int y = input_y - size/2;
	    	for(int i=0; i < size; i++) {
	    		for(int j=0; j < size; j++) {
	    			Diffusion_Cell dc0 = index(x+i, y+j, 0);
	    			Diffusion_Cell dc1 = index(x+i, y+j, 1);
	    			//red
	    			if(color == 0) {
	    				dc1.red = 1;
	    				dc0.red = 1;
	    			}
	    			//green
	    			else if(color == 1) {
	    				dc1.green = 1;
	    				dc0.green = 1;
	    			}
	    			//blue
	    			else if(color == 2) {
	    				dc1.blue = 1;
	    				dc0.blue = 1;
	    			}
	    			//check total saturation
	    			normalize_colors(dc0);
	    			normalize_colors(dc1);
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
    		boolean visited[][] = new boolean[width][height];
    		for(i = 0; i < 360; i += 0.1) {
	    	    angle = i;
	    	    for(int j=0; j<=radius; j++) {
		    	    x1 = j * Math.cos(angle * PI / 180);
		    	    y1 = j * Math.sin(angle * PI / 180);
		    	    int nx = (int)(x+x1);
		    	    int ny = (int)(y+y1);
		    	    //the loops will hit interior cells multiple times
		    	    //only add dye to cell if that cell has not yet been visited
		    	    if(!visited[nx][ny]) {
			    	    Diffusion_Cell dc0 = index(nx, ny, 0);
		    			Diffusion_Cell dc1 = index(nx, ny, 1);
		    			//red
		    			if(color == 0) {
			    	    	dc0.red = 1;
			    	    	dc1.red = 1;
		    			}
		    			//green
		    			else if(color == 1) {
			    	    	dc0.green = 1;
			    	    	dc1.green = 1;
		    			}
		    			//blue
		    			else if(color == 2) {
			    	    	dc0.blue = 1;
			    	    	dc1.blue = 1;
		    			}
		    			//check total saturation
		    			normalize_colors(dc0);
		    			normalize_colors(dc1);
		    			//set visited to true
		    			visited[nx][ny] = true;
		    	    }
	    	    }
    	    }
    	}
    	//Not Specified
    	else {
    		System.out.println("Specified Shape is not Implemented");
    		exit();
    	}
    }
    
    //dyes fabric, calls dye() function, used in setup() and draw() re-dye
    public void setup_dye() {
    	dye(50,50,30,0);
    	dye(65,50,30,2);
    }
    
    //index function
	public Diffusion_Cell index(int x, int y, int layer) {
		//boundary cases
		x = x<0 ? 0 : x;
		x = x>Run_Simulation.w - 1 ? Run_Simulation.w - 1 : x;

		y = y<0 ? 0 : y;
		y = y>Run_Simulation.h - 1 ? Run_Simulation.h - 1 : y;
		
		//indices into fiber for cloth cell + overflow
		int cloth_x = x / Run_Simulation.thread_weft_size;
		int cloth_y = y / Run_Simulation.thread_warp_size;
		int diff_x = x % Run_Simulation.thread_weft_size;
		int diff_y = y % Run_Simulation.thread_warp_size;
		
		//index into cloth_cells
		if(layer==0) {
			Cloth_Cell cloth_cell = cm.weft.threads.get(cloth_x).get(cloth_y);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
		else {
			//switch x and y if w =/= h
			Cloth_Cell cloth_cell = cm.warp.threads.get(cloth_y).get(cloth_x);
			return cloth_cell.d_cells.get(diff_x).get(diff_y);
		}
	}
    
    void normalize_colors(Diffusion_Cell dcell) {
    	float total = dcell.red+dcell.blue+dcell.green;
    	if(total > 1) {
			dcell.red = dcell.red/total;
			dcell.blue = dcell.blue/total;
			dcell.green = dcell.green/total;
		}
    }
    
    //TODO Folds of different slope (defaults to vertical fold in the middle [w/2])
    //TODO have empty space point back to original point
    void fold_init(int x1, int y1, int x2, int y2) {
    	line(x1, y1, x2, y2);
    	for(int i=0; i<w; i++) {
    		for(int j=0; j<h; j++){
    			Point2D p = new Point2D.Double((w-1)-i, j);
    			fold[i][j] = p;
    		}
    	}
    }
    
    // (1) - (2)
    //calculates the rate of dye transfer for the target cell from the surrounding cells
    //positive means dye flowing in, negative means dye leaving
    public void ficks2nd(int dx, int dy, int cell_layer) {
    	int i = dx;
    	int j = dy;
    	Diffusion_Cell current_cell = index(i, j, cell_layer);
    	//terms in fick's second law
    	//NOTE: D()'s '1/2' has been changed to '1' to fit with index logic
    	double d1 = D(current_cell, (index(i+1,j,cell_layer)));
    	double red1 = (index(i+1, j, cell_layer).red - current_cell.red)/delta_d;
    	double green1 = (index(i+1, j, cell_layer).green - current_cell.green)/delta_d;
    	double blue1 = (index(i+1, j, cell_layer).blue - current_cell.blue)/delta_d;
    	
    	double d2 = D(current_cell, (index(i-1,j,cell_layer)));
    	double red2 = (index(i-1, j, cell_layer).red - current_cell.red)/delta_d;
    	double green2 = (index(i-1, j, cell_layer).green - current_cell.green)/delta_d;
    	double blue2 = (index(i-1, j, cell_layer).blue - current_cell.blue)/delta_d;
    	
    	double d3 = D(current_cell, (index(i,j+1,cell_layer)));
    	double red3 = (index(i, j+1, cell_layer).red - current_cell.red)/delta_d;
    	double green3 = (index(i, j+1, cell_layer).green - current_cell.green)/delta_d;
    	double blue3 = (index(i, j+1, cell_layer).blue - current_cell.blue)/delta_d;
    	
    	double d4 = D(current_cell, (index(i,j-1,cell_layer)));
    	double red4 = (index(i, j-1, cell_layer).red - current_cell.red)/delta_d;
    	double green4 = (index(i, j-1, cell_layer).green - current_cell.green)/delta_d;
    	double blue4 = (index(i, j-1, cell_layer).blue - current_cell.blue)/delta_d;
    	
    	double d5 = D(current_cell, (index(i,j,(cell_layer+1)%2)));
    	double red5 = (index(i, j, (cell_layer+1)%2).red - current_cell.red)/delta_d;
    	double green5 = (index(i, j, (cell_layer+1)%2).green - current_cell.green)/delta_d;
    	double blue5 = (index(i, j, (cell_layer+1)%2).blue - current_cell.blue)/delta_d;
    	
    	Point2D fold_point = fold[i][j];
    	double d6 = fold_multiplier * D(current_cell, (index((int)fold_point.getX(), (int)fold_point.getY(), (cell_layer+1)%2)));
    	double red6 = fold_multiplier * (index((int)fold_point.getX(), (int)fold_point.getY(), (cell_layer+1)%2).red - current_cell.red)/delta_d;
    	double green6 = fold_multiplier * (index((int)fold_point.getX(), (int)fold_point.getY(), (cell_layer+1)%2).green - current_cell.green)/delta_d;
    	double blue6 = fold_multiplier * (index((int)fold_point.getX(), (int)fold_point.getY(), (cell_layer+1)%2).blue - current_cell.blue)/delta_d;
    	
    	//equation
    	double eq_red = (d1*red1 + d2*red2 + d3*red3 + d4*red4 + d5*red5 + d6*red6) / delta_d;
    	double eq_green = (d1*green1 + d2*green2 + d3*green3 + d4*green4 + d5*green5 + d6*green6) / delta_d;
    	double eq_blue = (d1*blue1 + d2*blue2 + d3*blue3 + d4*blue4 + d5*blue5 + d6*blue6) / delta_d;
    	
    	//bounds checking
    	if(eq_red > 1) eq_red = 1;
    	else if(eq_red < -1) eq_red  = -1;
    	if(eq_blue > 1) eq_blue = 1;
    	else if(eq_blue < -1) eq_blue  = -1;
    	
    	//total density checking
    	double total = eq_red + eq_blue + eq_green;
		if(total > 1) {
			eq_red = eq_red/total;
			eq_blue = eq_blue/total;
			eq_green = eq_green/total;
		}
		
    	//update corresponding rates
    	if(cell_layer == 0) {
    		rates_top[i][j][0] = eq_red;
    		rates_top[i][j][1] = eq_green;
    		rates_top[i][j][2] = eq_blue;
    	}
    	else {
    		rates_bot[i][j][0] = eq_red;
    		rates_bot[i][j][1] = eq_green;
    		rates_bot[i][j][2] = eq_blue;
    	}
    }
     	
    // (3)
    public float t3(Diffusion_Cell f1, Diffusion_Cell f2) {
    	//different layers
    	if(f1.z != f2.z) return I;
    	//gap and gap
    	else if(f1.isGap && f2.isGap) return IV;
    	//fiber and gap
    	else if(f1.isGap || f2.isGap) return III;
    	//same layer and parallel (weft layer and warp layer)
    	else if(f1.cloth_ref / cm.weft.threads.size() == f2.cloth_ref / cm.weft.threads.size()) return V;
    	else if(f1.cloth_ref / cm.warp.threads.size() == f2.cloth_ref / cm.warp.threads.size()) return V;
    	//same layer and perpendicular
    	else return II;
    }
    
    public float tortuosity(Diffusion_Cell f1, Diffusion_Cell f2) {	//calculate all 
    	return t1 * t2 * t3(f1, f2);
    }
    
    // (4)
    public float D(Diffusion_Cell f1, Diffusion_Cell f2) {
    	//diffusion coefficient
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
    
    //FUNCTIONS BELOW NOT CURRENTLY USED
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
