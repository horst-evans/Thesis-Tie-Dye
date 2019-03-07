import java.util.ArrayList;

public class Layer {
	ArrayList<ArrayList<Cloth_Cell>> fibers;
	
	public Layer(int size, boolean first_down, boolean isweft) {
		fibers = new ArrayList<ArrayList<Cloth_Cell>>();
		//TODO fix for weft =/= warp
		/*
		//number of threads than can fit in the weft size (x)
		int weftnum = size/Run_Simulation.thread_weft_size;
		if(size%Run_Simulation.thread_weft_size>0) weftnum++;
		System.out.println(weftnum);
		//number of threads than can fit in the weft size (x)
		int warpnum = size/Run_Simulation.thread_weft_size;
		if(size%Run_Simulation.thread_weft_size>0) warpnum++;
		System.out.println(warpnum);
		*/
		int num = size/Run_Simulation.thread_weft_size;
		if(size%Run_Simulation.thread_weft_size>0) num++;
		int id=0;
		boolean up = first_down;
		boolean fup = up;
		//make "num" threads
		for(int i = 0; i < num; i++) {
			ArrayList<Cloth_Cell> fiber = new ArrayList<Cloth_Cell>();
			//for each thread, make a cloth_cell for 
			for(int j = 0; j < num; j++) {
				int xp = i * Run_Simulation.thread_weft_size;
				int yp = j * Run_Simulation.thread_warp_size;
				Cloth_Cell nc = new Cloth_Cell(id,isweft,0,xp,yp,up);
				up = !up;
				fiber.add(nc);
				id++;
			}
			up = !fup;
			fup = !fup;
			fibers.add(fiber);
		}
	}

	public int hasFiber(Diffusion_Cell d) {
		for(int i=0; i<fibers.size(); i++) {
			for(int j=0; j<fibers.get(0).size(); j++) {
				if(fibers.get(i).get(j).id == d.cloth_ref) {
					if(fibers.get(i).get(j).up_orientation) return	2;
					else return 1;
				}
			}
		}
		return -1;
	}
	
	//TODO have gaps exist
	public boolean isGap(Diffusion_Cell d) {
		return false;
	}
}
