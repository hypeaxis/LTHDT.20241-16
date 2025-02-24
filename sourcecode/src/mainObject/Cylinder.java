package mainObject;


import force.AppliedForce;
import force.Friction;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Cylinder extends MainObject implements Reset{
	private static float MAXRADIUS = 150;

	private FloatProperty radius = new SimpleFloatProperty(50);
	private FloatProperty angularPosition = new SimpleFloatProperty(0);
	private FloatProperty angularVelocity = new SimpleFloatProperty(0);
	private FloatProperty angularAcceleration = new SimpleFloatProperty(0);



	public Cylinder(float radius) {
	    super();
	    this.radius.set(radius);

	}


	public float getRadius() {
		return radius.get();
	}
	

	public float getAngularPosition() {
		return angularPosition.get();
	}
	
	public float getAngularVelocity() {
		return angularVelocity.get();
	}
	
	public float getAngularAcceleration() {
		return angularAcceleration.get();
	}
	
	public FloatProperty getAngularPositionProperty() {
		return angularPosition;
	}

	public FloatProperty getAngularVelocityProperty() {
		return angularVelocity;
	}
	
	public FloatProperty getAngularAccelerationProperty() {
		return angularAcceleration;
	}
	
	public void setRadius(float radius) {
		if(radius > MAXRADIUS) {
			return;
		}
		this.radius.set(radius);
	}

	public FloatProperty radiusProperty() {
		return radius;
	}

	@Override
	public void reset() {
		this.setMass(0.1f);
		this.setPosition(0);
		this.setVelocity(0);
		this.setAcceleration(0);
		this.radius.set(50);
		this.angularPosition.set(0);
		this.angularVelocity.set(0);
		this.angularAcceleration.set(0);
	}
	
	
	@Override
	public void updateRotationMotion(AppliedForce F, Friction friction, float deltatime) {
		this.angularAcceleration.set(2000000 * (friction.getValue()) / (this.getMass() * this.radius.get() * this.radius.get()));
		
		this.angularVelocity.set(this.angularVelocity.get() + this.angularAcceleration.get() * deltatime);
		
		this.angularPosition.set(this.angularPosition.get() + this.angularVelocity.get() * deltatime);

		
	}

	

}
