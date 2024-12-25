package mainObject;


import force.AppliedForce;
import force.Friction;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Cylinder extends MainObject /*implements Calculator*/{
	private static float MAXRADIUS = 150;
	private FloatProperty radius = new SimpleFloatProperty();
	private float angularPosition;
	private float angularVelocity;
	float angularAcceleration;


	public Cylinder(float radius) {
	    super();
	    this.radius.set(radius);
	    this.mass.set(1); // Đặt giá trị mặc định
	}


	public float getRadius() {
		return radius.get();
	}
	

	public float getAngularPosition() {
		return angularPosition;
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
	public void updateRotationMotion(AppliedForce F, Friction friction, float deltaTime) {
		this.angularAcceleration = 2 * (friction.getValue()) / (this.getMass() * this.radius.get() * this.radius.get());
		
		this.angularVelocity += this.angularAcceleration * deltaTime;
		
		this.angularPosition += this.angularVelocity * deltaTime;
		
	}


	public void updateTranslationMotion(AppliedForce appliedForce, Friction frictionForce, float f) {
		// TODO Auto-generated method stub
		
	}

	

}
