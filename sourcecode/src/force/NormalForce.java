package force;

public class NormalForce extends Force{


	public NormalForce() {
		super();
	}
	
	public void calculateNormalForce(Gravity P) {
		
		this.setValue(P.getValue());
	}

	
}
