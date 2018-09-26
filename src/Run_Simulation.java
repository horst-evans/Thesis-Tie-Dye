import processing.core.PApplet;

public class Run_Simulation extends PApplet{

	public static void main(String[] args) {
		PApplet.main("Run_Simulation");
	}
	public void settings(){
		size(400,300);
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
