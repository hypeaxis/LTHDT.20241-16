package force;

public class NormalForce extends Force{

	public NormalForce() {
		super();
	}
	
	public void calculateNormalForce(Gravity P) {
		this.setDirection("up");
		this.setValue(P.getValue());
	}
	
}
