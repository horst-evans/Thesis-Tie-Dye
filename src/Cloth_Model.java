
public class Cloth_Model {
	Layer weft;				// collection of fibers
	Layer warp;				// collection of fibers
	
	public Cloth_Model() {
		weft = new Layer(Run_Simulation.h);
		warp = new Layer(Run_Simulation.w);
	}
	
}
