import processing.core.PApplet;

public class Run_Simulation extends PApplet{
//NOTE: a diffusion cell ~= to one pixel
//TODO fix isWeft, vpos, and up_orientation (all closely (?) related)

	public static float porosity = 0;
	public static float tortuosity = 0;
	public static int fiber_gap = 0;			// gap between fibers
	public static int fiber_size = 0;
	public static String pattern = "plain";		// crisscross
	public static int weft = 0;
	public static int warp = 0;
	public static int w = 400;
	public static int h = 300;
	
	public static void main(String[] args) {
		PApplet.main("Run_Simulation");
	}
	
	public void settings(){
		size(w,h);
    }

    public void setup(){
    	fill(120,50,240);
    }

    public void draw(){
    	ellipse(width/2,height/2,second(),second());
    }
    
    public void ficks2nd() {
    	
    }
    
    public int tortuosity(int number) {	//calculate all 
    	switch(number)
    	{
    	case 1:
    		return 0;
    	case 2:
    		return 0;
    	case 3:
    		return 0;
    	case 4:
    		return 0;
    	case 5:
    		return 0;
    	default:
    		return -1;
    	}
    }
    
}
