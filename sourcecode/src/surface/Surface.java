package surface;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Surface {
	private FloatProperty kineticCoefficient = new SimpleFloatProperty(0);
	private FloatProperty staticCoefficient = new SimpleFloatProperty(0);
	
	
	public FloatProperty getKineticProperty() {
		return kineticCoefficient;
	}
	public FloatProperty getStaticProperty() {
		return staticCoefficient;
	}
	
	public float getKineticCoefficient() {
		return kineticCoefficient.get();
	}
	
	public float getStaticCoefficient() {
		return staticCoefficient.get();
	}
	
	public void setKineticCoefficient(float kineticCoefficient) {
		this.kineticCoefficient.set(kineticCoefficient);
	}
	public void setStaticCoefficient(float staticCoefficent) {
		this.staticCoefficient.set(staticCoefficent);
	}
	


	
	
	
}
