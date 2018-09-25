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
}
