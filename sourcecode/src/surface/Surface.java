package surface;

public class Surface {
	private float kineticCoefficient;
	private float staticCoefficient;
	
	
	public Surface(float kineticCoefficient, float staticCoefficient) {
		super();
		this.kineticCoefficient = kineticCoefficient;
		this.staticCoefficient = staticCoefficient;
	}


	public float getKineticCoefficient() {
		return kineticCoefficient;
	}


	public float getStaticCoefficient() {
		return staticCoefficient;
	}
	
	
}
