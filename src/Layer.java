import java.util.ArrayList;

public class Layer {
	ArrayList<ArrayList<Cloth_Cell>> threads;
	
	public Layer(boolean first_down, boolean isweft) {
		threads = new ArrayList<ArrayList<Cloth_Cell>>();
		//number of threads than can fit in the weft/warp size (x/y)
		int weftnum = Run_Simulation.w/Run_Simulation.thread_weft_size;
		int warpnum = Run_Simulation.h/Run_Simulation.thread_warp_size;
		//change inputs based on which layer (weft vs warp)
		int num1, num2;
		if(isweft) {
			num1 = weftnum;
			num2 = warpnum;
		}
		else {
			num1 = warpnum;
			num2 = weftnum;
		}
		//other variables
		int id=0;
		boolean up = first_down;
		boolean fup = up;
		//make "num" threads
		for(int i = 0; i < num1; i++) {
			ArrayList<Cloth_Cell> thread = new ArrayList<Cloth_Cell>();
			//for each thread, make a cloth_cell for 
			for(int j = 0; j < num2; j++) {
				int xp = i * Run_Simulation.thread_weft_size;
				int yp = j * Run_Simulation.thread_warp_size;
				Cloth_Cell nc = new Cloth_Cell(id,isweft,0,xp,yp,up);
				up = !up;
				thread.add(nc);
				id++;
			}
			up = !fup;
			fup = !fup;
			threads.add(thread);
		}
	}
	
}
