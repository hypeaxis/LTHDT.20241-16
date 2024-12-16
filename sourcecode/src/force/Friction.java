package force;

import mainObject.Cube;
import mainObject.Cylinder;
import mainObject.MainObject;
import surface.Surface;

public class Friction extends Force{

	public Friction() {
		super();
	}
	

	public void calculateFriction(Surface surface, MainObject obj, NormalForce N, AppliedForce F) {

		if(obj.getVelocity() == 0) {
			
			this.setDirection(F.reverse());
			if(obj instanceof Cube) {
				if(F.getValue() <= N.getValue() * surface.getStaticCoefficient()) {
					this.setValue(F.getValue());
				} else {
					this.setValue(N.getValue() * surface.getKineticCoefficient());
				}
			}
			if(obj instanceof Cylinder) {
				if(F.getValue() <= 3 * N.getValue() * surface.getStaticCoefficient()) {
					this.setValue(F.getValue()/3);
				} else {
					this.setValue(N.getValue() * surface.getKineticCoefficient());
				}
			}
		}

	}


}
