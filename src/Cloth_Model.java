
public class Cloth_Model {
	Layer weft;	// collection of fibers
	Layer warp;	// collection of fibers
	
	public Cloth_Model() {
		weft = new Layer(false, true);
		warp = new Layer(true, false);
	}
}
